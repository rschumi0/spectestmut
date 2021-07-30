# JavaMutator
For mutating simple Java programs (without includes), e.g. for compiler testing.

<b/>Prerequisites<br /></b>
Java needs to be installed and available as command (javac)<br />
Maven build automation tool https://maven.apache.org/ <br />

<b/>How to Build<br /></b>
mvn package<br />

<b/>How to run<br /></b>
cd core/target<br />
unzip core-1.0-SNAPSHOT.zip<br />
cd core-1.0-SNAPSHOT<br />
./javamu -i "input file" -m "mutation_file (from Sample dir .yaml)"<br />

example execution:

./javamu -i ../../../JavaTest/ -m ../../../Sample/try.yaml 

<b/>How to adjust<br /></b>
Select different .yaml mutation configuration files, or manual 
adjustment of the mutators (core/src/main/java/javamu/mutator).
