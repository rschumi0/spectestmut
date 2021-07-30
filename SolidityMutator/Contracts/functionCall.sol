contract functionCall{
	function g(int a) public returns (int r) {
		a = 2;
	    int c ;
	   // c = functionCall(new B(); g1; a);
		int b;
	    b = a + 2;
	    return b;
	}
}

contract B {
	function g1(int a) public returns (int r) {
		return a + 4;
	}
	constructor () {
		int sum ;
		sum = 3 + 4;
	}
}


contract C {
	int a;
    uint b = 5;
    bool c =false;

	function f (int x) public returns (int y)  {
		int sum;

		if (x < 0 ) {
			sum = 0;
		}
		else {
			sum = x + f(x - 1);
            b = b + 1;
		}

		return sum;

	}
}