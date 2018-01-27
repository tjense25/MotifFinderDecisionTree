# MotifFinderDecisionTree
<h2>Finding Motifs to peptide datasets using random forrests/Decision trees</h2>

***

<h2>Motif Finder Algorithm:</h2>

MotifFinder takes in a file containing the weka specific output to a
RandomForrest or Decision tree classifier and outputs a list of probable
motifs. 

Java code parses throught he output of the weka classifier trees and recreates
the each of the trees. It then traverses through each node of the tree storing
the position and residue at each branching node. When it gets to a leaf node it
adds the motif to a map of either toxic, neutral, or antitoxic motifs depending on
the classification at that leaf node. The map is a multimap sorted by the
classification score of the motif that was added. When all trees have been
traversed the program will output the k most liekly motifs created from the
decision forest.

Classification score is calculated from the total number of training data
points that were classified in that node divided by the number of training
points in that node that were missclassified. This ensurres that the most
likely motifs are both highly represented, yet still reliably accurate. 

***

<h2>INPUT:</h2>

1. 	A text file with results from a weka Decision Tree, j48, or Random Forrest
	classifier run. 
2.	A number k corresponding to how many motifs in each class you want to
	find. Default = 10. [Use -a or 0 to find all possible motifs]
3. 	-RF if searching in a RandomForrest. By default algorithm searches in a
	single decision tree. Must include the -RF option to search through
	mutliple trees in the output. 

***

<h2>OUTPUT:</h2>

Motif Finder prints the k most probable motifs for each class to standard
output along with the total number of classification instances plus the number
of missclasified instances, and the total score for classification. The motifs
are written in regular expression format. Perfect for using in a grep command
to find the peptides in the original data set that match the specified motif.

Following shows the example output of running the following command:

```bash
java -jar MotifFinder.jar randomForest.txt 10 -RF
```
	
	TOXIC:
	LH...... (62/4) 12.400000
	L...L... (90/7) 11.250000
	...S.... (11/0) 11.000000
	G.....H. (62/5) 10.333333
	G......G (39/3) 9.750000
	..L..L.I (53/5) 8.833333
	.G...ND. (26/2) 8.666667
	..V.V.C. (16/1) 8.000000
	...F..C. (56/6) 8.000000
	L....N.. (16/1) 8.000000
	
	ANTITOXIC:
	.I...... (26/0) 26.000000
	......L. (44/3) 11.000000
	V..V.... (30/2) 10.000000
	.......I (19/1) 9.500000
	.I....V. (84/8) 9.333333
	F......F (98/10) 8.909091
	.I...G.. (168/19) 8.400000
	...F..Y. (33/3) 8.250000
	..C.V... (54/6) 7.714286
	..C...C. (92/11) 7.666667
	
	NEUTRAL:
	....L.R. (72/6) 10.285714
	YSV..... (20/1) 10.000000
	.....NR. (214/22) 9.304348
	V.....R. (46/4) 9.200000
	.......R (16/1) 8.000000
	.L...S.. (53/6) 7.571429
	.....SV. (106/13) 7.571429
	.Y..C... (15/1) 7.500000
	V.....D. (129/17) 7.166667
	......RF (20/2) 6.666667

***

<h2>How to use</h2>

Installed with the git repository is a jar file containing all the necesary
code. Either run Motif Finder with the java -jar command or export the java
file to the CLASSPATH environment variable and run java MotifFinder.

```bash
java -jar path/to/MotifFinder.jar decisionTree.txt -a
```

or:

```bash
export CLASSPATH=/path/to/the/MotifFinder.jar
java MotifFinder decisionTree.txt 30 -RF
```

Adding the following line to the .bashrc or .bash_profile will create a new
alias to use MotifFinder simply by tpying the command MotifFinder:

```bash
alias MotifFinder="java -jar path/to/MotiFFinder.jar"
```

Then From the Command line:

```bash
MotifFinder randomForestTrees.txt 50 -RF
```
