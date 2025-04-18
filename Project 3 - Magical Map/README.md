# Project 3 - Magical Map

## CmpE 250, Data Structures and Algorithms, Fall 2025

### SAs: Alperen Akyol, Şimal Güven

### TAs: İrem Urhan, Kutay Altıntaş

### Instructor: Atay Özgövde

### Due: 16/12/2024 , 23:59 Strict

## 1 Introduction

You have found yourself in the famous magical land of Oz. Normally wizard Oz would be
happy to see you there and brief you enthusiastically about his favorite sites in the land .A
detailed map he drew would have been given to you and he would also let you wander around
the land as you please. But he was fed up with previous visitors and lost his patience a bit.
Overlooking the hidden gems in which he worked so hard to create; visitors have acted like
unrespectful tourists and disrupted his garden, polluted the aesthetic waterfall and broken
his trust. He kept forgiving them for a while and kept fixing the sights. Reputation of the
land have grown in time and wizard Oz’s map changed hands many times.


But just after he felt so disrespected since visitors started to come and go whenever
they please without even greeting him properly and saying goodbye, he have shattered the
whole land into tiny pieces and shuffled all of them. He replaced the previous map into an
enchanted one with some hidden surprises.
From now on the new visitors will be challenged. In order to go home safely each visitor
needs to follow the instructions of the wizard. He might seem harsh but all he wants is you
to visit his favorite sites in the land and share your thoughts with him. After each visit
wizard will appear to you again and describe the next site he desires you to see. On those
encounters, he might choose to help you with the enchanted map or not but it is certain that
he will be there to collect your thoughts and brief you about the next site. Key is that you
should find your way to each magical site as fast as possible your knowledge allows you to.

## 2 Properties

### 2.1 Land

Wizard has shattered the map into squares which are perfectly equal and rearranged them.

- Map is a rectangle.
- Map is divided into equal nodes that has x and y coordinates.
- All nodes have non-negative integers as their types as explained below.
- You can only move between adjacent nodes.
- Time it takes to move between nodes are floating point numbers that range from 0 to
    99999 with 6 significance.

### 2.2 Enchanted Map

As we described above, the map wizard will provide is not the same as before. Colors were
used to distinguish countries but now no one knows where is where. He will correspond each
color to a number just to confuse your mind. And he will select some colors to represent
locks and pieces corresponding to those colors will be locked. Twist is, some pieces will seem
as unlocked to you even though they are locked and map will reveal them only when you are
close enough.

- 0: These are the nodes you can move onto them freely in any condition
- 1: These are the nodes that you cannot move onto them and wizard give you initial
    info about them.
- 2 and integers bigger than 2: These are the impassable nodes that will seem like
    passable to you until you move close to them.


### 2.3 Moving in the Land

Your objective is to move to the magical site wizard wants you to as fast as possible according
to the current information map provides you. As you move in the map, enchanted map reveals
the type of pieces when you are close enough.

- Initial Calculation of Route: At the beginning of each objective, first calculate the
    fastest route to reach your objective, then move to the parts in your route one by one
- Radius of Line of Sight:As you move, we expect you to reveal the unknown parts
    inside the radius(edges included) according to the data that is given to you. Use
    Pythagoras theorem on coordinates of parts to find parts that is inside your line of
    sight. You are encouraged to find a workaround solution to line of sight recalculation
    and out of bounds problem.
- Recalculation of Route:If you find that the route you calculated before is infeasible
    after the reveal of an unknown obstacle, stop immediately and recalculate the optimal
    route again according to you new information.
- Deterministic Cases: You can assume that, for any node, the fastest route to that
    node will NOT be multiple; in other words, the expected path you to find is unique at
    any point, leading to determinism.

### 2.4 Help of the wizard

After you managed to reach to a magical site , wizard may decide to offer you a choice to
make your following trips easier. From the knowledge you have of the map up to the point
you come across the wizard’s question will determine your choice. Wizard will give you
multiple colors to select upon. A help means that selected colored pieces throughout the
whole map will be passable. So correct implementation is selecting the color which enables
your move to the next magical site fastest.

- Choice to Make:You will be given multiple integers(>=2) to choose to change them
    to 0 in ALL of the map. You are expected to find the fastest path among all options.
    An option is a case that an integer is changed to 0 throughout the map (meaning that
    you can move onto them now).
- After making the choice:Now you are expected to change the type to 0 in ALL of
    the map, which means you will change even the types that you don’t know, then move
    to the next objective point.
- Deterministic Choice: You can assume that, the fastest choice will NOT be more
    than one, in other words the choice we are expecting you to make is unique, leading
    to determinism.


## 3 Input-Output

3 input files

### 3.1 Land file

This input file describes the properties of nodes.
First line contains x and y sizes of the grid sequentially.
Following lines contain properties of the nodes as the following:
x-coordinate y-coordinate type-of-node

### 3.2 Travel time between nodes file

This input file gives the travel time between nodes as the following:
x-y-coordinate-of-first-node,x-y-coordinate-of-the-second-node travel-time

### 3.3 Mission file

This input file gives the nodes wizard wants you to see.
First line contains the radius of circle that map will reveal nodes’ type to you.
Second line is the starting node as follows:
x-coordinate y-coordinate
Following lines may be in two different structure as follows:
Type1- When this objective reached wizard will not offer you help
x-coordinate-of-objective y-coordinate-of-objective
Type2- When this objective reached wizard will offer you help
x-coordinate-of-objective y-coordinate-of-objective <option-1 option-2 ... option-n>

### 3.4 Output file

We expect you to print the following in a sequential manner:
1-Each node you move (excluding the starting node):
Moving to x-y
2-Each time you have discovered that current path you are following is infeasible:
Path is impassable!
3-Each time you have reached an objective node
Objective n reached!
4-Number you decide to close when wizard offers you a choice.
Number n is chosen!
An example:

Moving to 0-
Moving to 1-


Path is impassable!
Moving to 0-
Moving to 0-
Moving to 0-
Moving to 1-
Objective 1 reached!
Number 3 is chosen!
Moving to 2-
Moving to 3-
Moving to 3-
Moving to 3-
Moving to 3-
Objective 2 reached!

## 4 Submission

You will be submitting a zip file containing your code via Moodle. Put your .java files in
the zip file. Keep the name of the main function "Main.java". Name the zip file in this format.

```
Structure: <studentNumber>.zip
```
Your code must be able to run correctly with these commands:

javac *.java
java Main <input_file0> <input_file1> <input_file2> <output_file>
Input file 0,1,2 should be placed in the sequence as given in the input-output section of the
description.

## 5 Grading

### 5.1 Stages and Grading

At each stage of grading we will be expecting you to reach to the objective points selecting
the fastest route each time. Our objective does not change between grading types but the
circumstances in which you will be moving on the grid will vary.

5.1.1 Type 1: Simplest Mission

There are no nodes in the grid in which you do not know the property of in other words grid
only contains 0’s and 1’s.


5.1.2 Type 2: Mission with Enchanted Map

Enchanted map hides some nodes’ properties from you and you will be expected to implement
a line of sight to detect which nodes you have unlocked while walking in the land and act
upon that information.

5.1.3 Type 3: Mission with Enchanted Map and Help of the Wizard

At some objective points you may be asked to do a selection between a list of types of
obstacles to make that selected obstacle passable.You are expected to do the selection which
benefits you the most in terms of time.

### 5.2 Grade Distribution

You can see the grade distribution for each input type in Table 1:

```
Type Small Case (%) Large Case (%)
1 36 24
2 18 12
3 6 4
```
```
Table 1: Grade Distribution for Small and Large Cases
```
### 5.3 Constraint Specifications

Constraint specifications are given below in Table.

```
Input Type Node Count Percentage of 0 Nodes Percentage of 1 Nodes Percentage of >=2 Nodes
Small case 64x64 75 10 15
Large case 400x400 80 10 10
```
### 5.4 Time Constraints

Since the nature of this project; graphs in the input cases are generated randomly and same
sized inputs might result in such different times. Therefore we cannot give you an overall
small case and large case time constraints , but instead we will provide you two input cases
, small and big, and run times they took on our machine.

Small case 1st-32-32 -> 0.5 s
Small case 2nd-32-32 -> 0.6 s
Small case 1st-64-64 -> 1 s
Small case 2nd-64-64 -> 1.4 s
Small case 128-128 -> 2.6 s


Large case 1st-300-300 -> 13.5 s
Large case 2nd-300-300 -> 6 s
Large case 1st-400-400 -> 35 s
Large case 2nd-400-400 -> 26 s
Large case 500-500 -> 156 s

This constraints are decided according to our codes execution time. These durations were
measured on an MacBook Air M2, 2022 with 16GB of RAM. Keep this in mind while testing
your code.

## 6 Warnings

- All source codes are checked automatically for similarity with other submissions and
    exercises from previous years. Make sure you write and submit your own code. Any
    sign of cheating will be penalized by at least -100 points at first attempt and disciplinary
    action in case of recurrence.
- Make sure you document your code with necessary inline comments and use meaningful
    variable names. Do not over-comment, or make your variable names unnecessarily long.
    This is very important for partial grading.
- Make sure that the white-spaces in your output is correct. You can disregard the ones
    at the end of the line.
- Please use the discussion forum at Moodle for your questions, and check if it is already
    answered before asking.
- Do NOT upload zip bombs, or any kind of malware.


