# SolidityMutator
For mutating Solidity smart contracts, e.g. for compiler testing.

<b/>Prerequisites<br /></b>
Solidiy compiler needs to be installed and available as command (solc)<br />
https://docs.soliditylang.org/en/v0.4.21/installing-solidity.html<br />
Maven build automation tool https://maven.apache.org/ <br />

<b/>How to Build<br /></b>
mvn package<br />

<b/>How to run<br /></b>
cd core/target<br />
unzip core-1.0-SNAPSHOT.zip<br />
cd core-1.0-SNAPSHOT<br />
./solmu -i "input file" -m "mutation_file (from Sample dir .yaml)"<br />

example execution:

./solmu -i ../../../Contracts/ -m ../../../Sample/modifier.yaml 

<b/>How to adjust<br /></b>
Select different .yaml mutation configuration files, or manual 
adjustment of the mutators (core/src/main/java/solmu/mutator).
