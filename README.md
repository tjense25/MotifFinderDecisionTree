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

Combiner iterates over the list of motifs for each of the toxicity classes and
checks to see if each motif matches any of the other motifs. If these two
motifs can be combined, they are put together into a new consesus motif that is
added to a list of combined motifs.

***

<h2>INPUT:</h2>

1. 	A text file with results from a weka Decision Tree, j48, or Random Forrest
	classifier run. 
2.	A number k corresponding to how many motifs in each class you want to
	find. Default = 10. [Use -a or 0 to find all possible motifs]
3. 	-RF if searching in a RandomForrest. By default algorithm searches in a
	single decision tree. Must include the -RF option to search through
	mutliple trees in the output. 
4.	-c: output an additional list of combined motifs. Motifs found from
	finder could be partial motifs. -c calls a combiner class to combine
	these partial motifs together to create a consesus motif. Outputs these
	potential consesus motifs to std.err so they can be seperated from the
	normal motifs

***

<h2>OUTPUT:</h2>

Motif Finder prints the k most probable motifs for each class to standard
output along with the total number of classification instances plus the number
of missclasified instances, and the total score for classification. The motifs
are written in regular expression format. Perfect for using in a grep command
to find the peptides in the original data set that match the specified motif.

Following shows the example output of running the following command:

```bash
java -jar MotifFinder.jar randomForest.txt 10 -c
```
	
	...S....	toxic		(16/0)	 16.000000
	......D.	toxic		(15/0)	 15.000000
	LH......	toxic		(63/4)	 12.600000
	L...L...	toxic		(90/7)	 11.250000
	G.....H.	toxic		(62/5)	 10.333333
	G......G	toxic		(40/3)	 10.000000
	.......V	toxic		(10/0)	 10.000000
	L....N..	toxic		(18/1)	 9.000000
	.G...ND.	toxic		(27/2)	 9.000000
	..N...CD	toxic		(9/0) 	 9.000000
	.I......	anti-toxic	(36/0)	 36.000000
	V..V....	anti-toxic	(30/2)	 10.000000
	.I....V.	anti-toxic	(84/8)	 9.333333
	F......F	anti-toxic	(101/10) 9.181818
	...F..Y.	anti-toxic	(35/3)	 8.750000
	.I...G..	anti-toxic	(168/19) 8.400000
	..C.V...	anti-toxic	(56/6)	 8.000000
	..C...C.	anti-toxic	(93/11)	 7.750000
	.D..F...	anti-toxic	(59/7)	 7.375000
	.....F.H	anti-toxic	(50/6)	 7.142857
	....L.R.	neutral		(72/6)	 10.285714
	YSV.....	neutral		(20/1)	 10.000000
	.....NR.	neutral		(215/22) 9.347826
	V.....R.	neutral		(46/4)	 9.200000
	.Y..C...	neutral		(16/1)	 8.000000
	.L...S..	neutral		(53/6)	 7.571429
	.....SV.	neutral		(106/13) 7.571429
	......RF	neutral		(22/2)	 7.333333
	.....DY.	neutral		(35/4)	 7.000000
	V.....D.	neutral		(130/18) 6.842105

	COMBINED MOTIFS:
	===================
	...S..D.	toxic
	LH.S....	toxic
	L..SL...	toxic
	G..S..H.	toxic
	G..S...G	toxic
	...S...V	toxic
	L..S.N..	toxic
	.G.S.ND.	toxic
	..NS..CD	toxic
	LH....D.	toxic
	L...L.D.	toxic
	G.....DG	toxic
	......DV	toxic
	L....ND.	toxic
	.G...ND.	toxic
	LH..L...	toxic
	LH.....V	toxic
	LH...N..	toxic
	LHN...CD	toxic
	L...L..V	toxic
	L.N..NCD	toxic
	.G...ND.	toxic
	..N...CD	toxic
	VI.V....	antitoxic
	.I....V.	antitoxic
	FI.....F	antitoxic
	.I.F..Y.	antitoxic
	.I...G..	antitoxic
	.IC.V...	antitoxic
	.IC...C.	antitoxic
	.I...F.H	antitoxic
	VI.V..V.	antitoxic
	VI.V.G..	antitoxic
	V.CVV...	antitoxic
	V.CV..C.	antitoxic
	VD.VF...	antitoxic
	V..V.F.H	antitoxic
	FI....VF	antitoxic
	.I...GV.	antitoxic
	.IC.V.V.	antitoxic
	.I...FVH	antitoxic
	F..F..YF	antitoxic
	FI...G.F	antitoxic
	F.C.V..F	antitoxic
	.I...FVH	antitoxic
	F..F..YF	antitoxic
	FI...G.F	antitoxic
	.IC..GC.	antitoxic
	..C.V.C.	antitoxic
	..C.VF.H	antitoxic
	.DC.F.C.	antitoxic
	..C..FCH	antitoxic
	.D..FF.H	antitoxic
	.....F.H	antitoxic
	YSV.L.R.	neutral
	....LNR.	neutral
	V...L.R.	neutral
	.L..LSR.	neutral
	....L.RF	neutral
	YSV..NR.	neutral
	YSV..SV.	neutral
	YSV...RF	neutral
	YSV..DY.	neutral
	V....NR.	neutral
	.Y..CNR.	neutral
	.....NRF	neutral
	VY..C.R.	neutral
	VL...SR.	neutral
	V.....RF	neutral
	.Y..CSV.	neutral
	.Y..C.RF	neutral
	.Y..CDY.	neutral
	VY..C.D.	neutral
	.L...SV.	neutral
	.L...SRF	neutral
	VL...SD.	neutral
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
java MotifFinder decisionTree.txt 30 -c
```

Adding the following line to the .bashrc or .bash_profile will create a new
alias to use MotifFinder simply by tpying the command MotifFinder:

```bash
alias MotifFinder="java -jar path/to/MotiFFinder.jar"
```

Then From the Command line:

```bash
MotifFinder randomForestTrees.txt 50 -c
```
