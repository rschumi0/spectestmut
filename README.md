# SpecTest Mutation tools

This repository contains two mutation tools (for Java and Solidity) that target compiler testing.

Due to the high complexity of modern compilers, it is important that a meaningful coverage criterion is applied for compiler testing. 

In SpecTest, we target high semantics coverage in terms of language semantic rules with the following two synergistic parts. 
First, we design and implement a mutator which systematically introduces less-exercised language features into the test programs automatically. 
Second, we apply powerful fuzzing techniques to generate program inputs to exercise all statements including less-used features in the test programs.  

To achieve high semantic coverage, SpecTest employs a two-part solution. 
We apply a test oracle to compute the expected output of a program and we compare it with the actual output of the program produced with a compiler.  
(The test oracle is an executable semantics, e.g., we were working with the k framework and utilised [KSolidity](https://github.com/kframework/solidity-semantics) and [K-Java](https://github.com/kframework/java-semantics).
During the execution of the oracle, we capture feedback on which SOS-rules are not fired (or least fired), which allows us to identify the language features which are associated with the SOS rules.
Based on this information, we can apply our mutator to systematically mutate the seed programs to introduce these less-tested language constructs.

The mutators are the core components of our method and available in this repository.
They work with a code mutation engine which is designed to automatically mutate a given source program to generate new programs (i.e., test cases for the compiler). 
The mutator is essential for our method to maximise the semantic coverage. 
By injecting language features that are rarely or never used into the seed programs, it increases the likelihood that uncovered or poorly covered semantic rules of the specification will be fired during the test execution. In order to 
identify rare features, the mutator requires coverage data about the frequency of the exercised semantic rules. This information is recorded during the execution of the language specification. 

Implementing the mutator is not trivial. For SpecTest, the mutators for Solidity and Java were implemented based on existing parsers through code instrumentation. 
That is, given a language feature and a source program, the mutator first parses the source program to build an AST. 
Afterwards, it identifies potential locations in the AST for introducing the features. 
Lastly, it systematically applies a mutation strategy specifically designed for the language constructs to inject them at all possible or specific pre-defined locations. 
In the following, we introduce three mutation strategies as examples. 

### Mutation for modifiers (Solidity)
We investigated features that were specific for Solidity.
For example, one mutation introduces modifiers for functions, which define conditions that must hold when a function is executed.
The following code shows a smart contract with modifiers written in the Solidity language. 
Unlike traditional programs, smart contracts cannot be modified once they are deployed on the blockchain. As a result, their correctness is crucial. 
So is the correctness of the compiler since the compiled programs are deployed on the blockchain. Furthermore, the Solidity compiler has been under rapid development 
and there are unique language features with sometimes confusing semantics.
Thus, it is a good target for evaluating the effectiveness of SpecTest. 

```
contract AccessRestriction {
  address public owner = msg.sender; 
  //default modifier:
  modifier onlyBy(address account){
  require(msg.sender==account, "Sender not authorized");
  _;	} //injected modifier:
  modifier cgsk(address value){ 
  require(value == address(0x0),"");
  _; } //injected modifier:
  modifier cbhs(address value){ 
  require(value == address(0x0),"");
  _;	} //injected modifier:
  modifier nlwx(address value){ 
  require(value == address(0x0),"");
  _;
  }//Make newOwner the contract owner:
  function changeOwner(address newOwner) public onlyBy(owner) cgsk(address(0x0)) cbhs(address(0x0)) nlwx(address(0x0)){
    owner = newOwner;
  }
}
```

In this example, the modifier ``onlyBy`` ensures that the function ``changeOwner`` can only be called when the address of the contract owner is used. 
By integrating various dummy modifiers (Lines 7, 10 \& 13) into our seed contracts and by adding them to functions (Line 17), 
we noticed that an older version of the Solidity compiler crashed in some cases, when more than a certain number of modifiers are used. 
Such a case is difficult to find with normal tests, since it is rare to use multiple modifiers for a function. 
Given that a less-fired SOS rule is concerned with the modifier construct in Solidity, to introduce modifiers, 
the mutator scans through the AST for function declarations. 
For each function declaration, the mutator randomly adds one or more modifiers.



### Mutation for Labelled Blocks (Java)
We also introduced specific mutations for Java.
For example, our experiments showed that semantic rules associated with labels were not fired. 
Hence, we introduced mutations that target these rules, e.g., a mutation that injects labelled blocks, 
which is a special and rarely used feature that allows an immediate exit of a block with a break statement. 
This mutation is illustrated in the following code, where we injected labelled blocks and breaks (with these labels) 
into a seed program.

```
bibt4QkDIfJ: {
	bsJxhbtSJBu: {
		bHhq23OwDjZ: { 
			try {
				bEdqZ33tKi9: {
					bVm9tCxbul4: {
						if (i >= 5){ break; }
						break bEdqZ33tKi9;
					}  
				}
			}catch(RuntimeException e){
				bQ2yucCPLQr: {
					System.out.print("X");
					break bQ2yucCPLQr;
				} 
			} 
		} 
	} 
}
```

Both for Solidity and Java, we noticed that there are various rules in the K specifications (i.e., 11 rules Java and 17 for Solidity)
concerning mathematical expressions that were not covered, e.g., computations with hex-values. 
In order to cover these rules and to cover unusual usages in different contexts, 
we relied on a random approach in contrast to the other mutations where we injected code at specific places.
We developed mutations that produce a variety of mathematical expressions combining various language features, 
like operations containing variables with different data types, hexadecimal, octal or binary literals, pre- and postfix increment/decrement ``++/--``, 
bitwise and bitshift operators, various combinations of unary operators and arrays.

A simplified example of a mutation produced with this strategy is shown in the code below. 
It can be seen that the increment operators ``++`` is used in an unusual context within a mathematical expression. 

```
contract Test {
	function testFunc(int a) 
		public pure returns (int) {
		int result = a + a++; 
		//produces 3 when a is 1
		return result;  
	} 
}
```

More details about the installation of the mutators can be found in the associated folders.


