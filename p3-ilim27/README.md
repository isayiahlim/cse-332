# CSE 332 Project 3

The purpose of this project is to get familiar with writing sequential code and using parallelism to potentially speed up that code. You will be using the ForkJoin framework to achieve parallelism.

## Overview
The Bellman-Ford algorithm is “an algorithm that computes shortest paths from a single source vertex to all of the other vertices in a weighted directed graph” (Wikipedia). Unlike other shortest-path graph algorithms such as Dijkstra’s, Bellman-Ford works with negative-weight edges and can be used to find negative-cost cycles in a graph. 

One application of the Bellman-Ford algorithm is currency arbitrage: finding a sequence of imbalanced currency exchange rates to potentially generate money through trading (i.e. starting with one currency and exchanging it for other currencies with favorable rates, ending up with more money than you started). Repeat until rich.

You do not need to fully understand why the Bellman-Ford algorithm works, but there are resources online that can help you understand its steps enough to implement it. We have provided you with the pseudocode for the sequential version of the algorithm below.

We have also provided the code to find a negative-cost cycle given a predecessor array. You will compute the predecessor array using one of several versions of the Bellman-Ford algorithm. For all the parallel tasks, it is expected that each individual run can get different valid results on the same graph.



## Sequential Algorithm Pseudocode:
// Initialization
for each vertex v
	dist[v] = GraphUtil.INF
	pred[v] = -1

dist[source] = 0

// Main algorithm
for n times do
for each vertex v
dist_copy[v] = dist[v]
for each edge (v,w)
	if (dist_copy[v] + cost(v,w)) < dist[w]
// Found a shorter path to w 
		dist[w] = dist_copy[v] + cost(v,w)
		pred[w] = v

In this pseudocode:
n is the number of vertices in the graph 
dist and dist_copy are arrays of size n that keep track of the currently computed distance to each vertex from the source vertex
cost(v,w) is the cost of the edge from vertex v to vertex w
pred is an array of size n that keeps track of the predecessor of each vertex
Graph vertices are integers (eg. the source vertex is an int between 0 and n-1).
The for each edge (v,w) loop will be done using two for loops. It will be implemented differently depending on the version of the Bellman-Ford solver. The pseudocode for it is given in each solver’s instructions later in the spec.

When initializing the arrays:
The distance from the source vertex to every other vertex v is unknown at first, so every element of dist (and dist_copy) should be initially set to . In our Java code, we will use an alias for Integer.MAX_VALUE in place of  (see GraphUtil.java). 
The distance from the source vertex to itself is 0.
The predecessor array should be initialized to -1 and only updated when shorter paths to that vertex are found. For every vertex v that cannot be reached from the source vertex, pred[v] should remain -1 at the end. This is important for the getCycle() method we provide to process properly.

Hint: Adding anything to  does not make sense in our context (and besides, adding to Integer.MAX_VALUE can result in integer overflow), so you must ensure this does not happen when implementing the pseudocode.

Our version uses two distance arrays with a copy step at the start of each iteration. This is done to make the parallel implementation easier. You must use this version. 

As you can see, given the nested for loops, the sequential Bellman-Ford algorithm is relatively slow—it runs in 𝒪VE time, which is 𝒪n3 in a complete graph!—so you will also write parallel versions to try to speed it up. 

## Project Restrictions
Unlike in P1 and P2, in this project you may use the built-in Java data structure classes in java.util.* such as java.util.LinkedList. However, you may not import java.util.Arrays to use methods such as Arrays.copyOf(). The main learning outcome is to understand the ForkJoin framework, and part of this is recognizing opportunities to use parallelism!
The design and architecture of your code are a substantial part of your grade.
You may not edit any file in the cse332.* packages.
You may not edit any of the public interfaces.

## Provided Code 
cse332.interfaces 
BellmanFordSolver.java: Defines a class that uses the Bellman-Ford algorithm to find a negative-cost cycle in a given graph.
cse332.graph
GraphUtil.java: 
generate(): Generates a graph given specific parameters (see How does GraphUtil.generate work?). 
getCycle(): Finds a cycle in a graph given a predecessor array.
printAdjMatrix(): Prints the given adjacency matrix to console.
INF: a constant alias for Integer.MAX_VALUE.
main
Main.java: Used to test implementations of BellmanFordSolver.

You will implement Parser (in main/), as well as OutSequential, OutParallelBad, OutParallelLock, and InParallel (in solvers/). You will also need to implement ArrayCopyTask, RelaxOutTaskBad, RelaxOutTaskLock, and RelaxInTask (in paralleltasks/) which are used in the parallel query responders.

Note: All class and function signatures in paralleltasks can be changed. The provided code is just a guideline.

## How does GraphUtil.generate work? 
Graphs generated by GraphUtil.generate() are represented as an adjacency matrix: a 2-dimensional n × n integer array adjMatrix where adjMatrix[v][w] gives the cost of the edge between vertex v and w (that is, the edge from vertex v to vertex w).  We represent a missing edge by setting the edge cost to  GraphUtil.INF. Graph vertices are identified by their numerical index in this array (i.e. the vertices are 0, 1, 2,..., n-1 instead of having labels like A, B, C, etc). The graphs are guaranteed to be connected.

We will test your code on graphs generated by GraphUtil.generate(), and you can use it to check the accuracy of your implementations as well. Alternatively, you can manually make a 2D array adjacency matrix and use that to test your implementations. When debugging, we recommend finding the smallest graph that fails (e.g. 5 vertices) and manually running the Bellman-Ford algorithm on paper to determine the correct answer.

The following information about the graph generator is not necessary to know, but it could be helpful if you would like to use it to write your own tests.

The graph generator has several parameters:
n: number of vertices. 
fp: for forward edges (u < v), the probability that (u,v) is an edge. If so, the cost is generated uniformly between fmin and fmax.
bp: for backward edges (u > v), the probability that (u,v) is an edge. If so, the cost is generated uniformly between bmin and bmax.
fmin: minimum cost for a forward edge.
fmax: maximum cost for a forward edge.
bmin: minimum cost for a backward edge.
bmax: maximum cost for a backward edge.
seed: graphs with the same seed (as well as other parameters) will be the same. Set to 0 to generate a random graph with the other given parameters.

Here are some sample settings for the graph generator—given as (fp, fmin, fmax) (bp, bmin, bmax):

N=5, (0.5, 2, 4),  (0.5, -4, -2) - gives a cycle of length 2 or 3 most of the time.
N=10, (0.5, 2, 4),  (0.5, -4, -2) - gives a cycle of length 3 to 8.
N=20, (0.2, 3, 10),  (0.5, -4, -2) - gives a cycle of length 6 to 10.
N=50, (0.1, 5, 30), (0.2, -6, -3) -  gives a cycle of length 8 to 16.
N=50, (0.1, 70, 100), (0.2, -6, -3) - gives a cycle on about half of inputs.
N=100, (0.1, 10, 100),  (0.1, -10, -1) - give a cycle of length 10 to 20.

To generate graphs with negative cost cycles that require many edges, make forward edge costs positive and backwards edge costs negative. The backwards edges should have a much smaller absolute value cost than the forward edges.

## Project Checkpoints 
This project will have one checkpoint (and a final due date). A checkpoint is a check-in on a certain date to make sure you are making reasonable progress on the project. For each checkpoint, you will submit code.

Checkpoint 1: 
(0), (1) due
P3 Due Date: 
(2), (3), (4), (5), (6) due

Note: (6) Consists of a few short-answer questions to be submitted separately on Gradescope.
(Don’t worry if the P3 Due Date looks like a lot of work; once you write one parallel version, the others are quite similar.)
For the Checkpoint deadline, you will need to submit the code for Part 1 (items 0-1) to Gradescope. 5% of your total grade for Project 3 will be determined by what you submit for the checkpoint. To receive full credit for the checkpoint, you need to be passing all of the tests we have provided for items 0-1 in Gradescope. If you are not passing all tests for items 0-1 at the time the checkpoint is due, please submit what you have at that time for partial credit for the tests you are passing. Please be sure to have items 0-1 working by the final P3 Due Date, as points will still be awarded for passing the test cases provided at that time. Note: late days may not be applied to Checkpoint deadlines, only to final project due dates.
### Part 1: Sequential
(0) Parser Part 1
Before writing the Bellman-Ford algorithm, the first step is to parse the graph given as input to the solve() method. The input graph is represented as an adjacency matrix (adjMatrix, as described here), but it needs to be represented instead as an adjacency list to run the algorithm. Converting the provided adjacency matrix into an adjacency list must happen before running every one of your implementations of the BellmanFordSolver interface.

For this first version of a parser, the adjacency matrix should be transformed into an adjacency list of vertices reached through outgoing edges (yes, this is how adjacency lists work normally, but we are specifically mentioning it here because you will be using incoming edges instead in item (4)). This should be done sequentially.

There are many ways to implement an adjacency list. However, we will eventually need to parallelize the Bellman-Ford algorithm using the ForkJoin framework. As such, we recommend using an ArrayList of HashMaps for your adjacency list. The data structure stored at index v in the array should store the edges and costs from vertex v to all of its neighbors (e.g. if you are using a HashMap, the neighbor vertices are the keys and each edge cost is the associated value). Remember in this project, you may use the built-in Java data structures. Please do not use a separate graph class (e.g. as in CSE 331).

For this item, you will need to implement the parse() method in main/Parser.java. (Don’t worry about the parseInverse() method yet; you will implement that in item (4).) 

(1) Sequential
In total for this project, you will write four different versions of the Bellman-Ford algorithm. This is the first version. Every version implements the BellmanFordSolver interface, which means you will write the body of the solve() method. It has three main parts:
Parsing and Initialization: parsing the adjacency matrix (adjMatrix) to create adjacency lists (using the method you wrote in Parser.java) and initializing the necessary data structures (i.e. the dist and pred arrays). This will be sequential in all versions. 
Running the Bellman-Ford algorithm. This itself has two steps, which will be sequential in this version but parallelized in the other ones (see the sequential pseudocode):
Array copying. This must be done manually (e.g. you may not use Arrays.copyOf(), Object.clone(), or System.arrayCopy()). IntelliJ may show a warning as a result, but you can use @SuppressWarnings("ManualArrayCopy") or just ignore it. 
Relaxing the edges (aka updating the cost to reach each vertex if a new lower-cost path is found). 
In this sequential version, iterating through all the edges (for each edge (v,w)) is done by:
for each vertex v  // done sequentially
for each outgoing edge (v,w) // done sequentially
Finding negative-cost cycles using the predecessors from step 2. The getCycle() method we provide in GraphUtil.java will return the list of vertices that contribute to a negative cycle given a predecessor array, which is what you should return from the solve() method. This will be the same in every version.

For this item, you will need to implement solvers/OutSequential.java.
### Part 2: Parallel with Outgoing Edges 
(2) Naive ForkJoin
Now that you’re more comfortable with how the algorithm works, it’s time to use parallelism to try to speed it up! You will use the Java ForkJoin framework to parallelize both the array copying and edge relaxing steps of the algorithm. 
A beginner's introduction to the ForkJoin Framework can be found here.

In this version, iterating through all the edges (for each edge (v,w)) is done by:
for each vertex v  // done in parallel
for each outgoing edge (v,w)  // done sequentially

This version still works with outgoing edges (ie. use the parse() method you wrote in item (0)), so you will likely notice some incorrect results when running the solver. This is expected! There is no real way to force a concurrency issue, so running the tests may give correct or incorrect results. You will explain why this occurs (as well as why the changes in the next two versions solve this problem) in your answers to the questions in item (6).

For this item, you will need to implement solvers/OutParallelBad.java and two ForkJoin tasks: ArrayCopyTask.java and RelaxOutTaskBad.java in the paralleltasks folder.
(3) ForkJoin with Locks 
We need to fix the incorrect results from the naive parallel version! In this version, our solution will be to use locks to avoid the race condition. You should use Java’s ReentrantLock. You will still be using the parse() method from item (0) (i.e. still working with outgoing edges).

You get to decide how to manage the locks. You could have the distance arrays store objects (that each contain a field storing the value at that index) and lock those objects, or you could have a separate array of just locks. You may not, however, use the synchronized keyword as a workaround for locks.

In this version, iterating through all the edges (for each edge (v,w)) is done the same as in the naive parallel version:
for each vertex v  // done in parallel
for each outgoing edge (v,w)  // done sequentially

For this item, you will need to implement solvers/OutParallelLock.java and one ForkJoin task: paralleltasks/RelaxOutTaskLock.java. You can reuse the ArrayCopyTask you wrote in item (2) because the array copying is the same.
### Part 3: Parallel with Incoming Edges & Short Questions
(4) Parser Part 2
Just like in previous versions, the next Bellman-Ford implementation needs the input graph to be converted from an adjacency matrix to an adjacency list. However, this time, the adjacency list will keep track of incoming edges rather than outgoing edges. This conversion should still be done sequentially.

For this item, you will need to implement the parseInverse() method in main/Parser.java.
(5) ForkJoin with Incoming Edges
Another way to fix the incorrect results from the naive parallel version is working with incoming edges instead of outgoing ones. You should have written a method to create the necessary adjacency list in item (4). This version still uses the Java ForkJoin framework, but it does not use locks. This means you will need a new way to relax the edges.

In this version, iterating through all the edges (for each edge (v,w)) is done by:
for each vertex w  // done in parallel
for each incoming edge (v,w)  // done sequentially

For this item, you will need to implement solvers/InParallel.java and another ForkJoin task: paralleltasks/RelaxInTask. You can reuse the ArrayCopyTask you wrote in item (2) because the array copying is the same.
(6) Short-Answer Questions
Please answer the short-answer questions posted in the separate Gradescope assignment. These are due at the same time as the code for P3.
## Submission and Grading
Submission instructions can be found on the Handouts page of the website. We will grade based on correctness of code (given tests + hidden tests) and manual grading on whether you are following the spec. There will be no grading on style (such as in the introductory courses) but we suggest writing comments to help yourself understand your code and to help us grade your implementation. 


