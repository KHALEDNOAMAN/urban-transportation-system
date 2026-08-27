<div align="center">

# 🚇 Urban Transportation System

### A Java-based transit network simulator built with Graph Algorithms & Custom Data Structures

<br>

[![Java](https://img.shields.io/badge/Java-17%2B-orange?style=for-the-badge\&logo=openjdk)](https://www.java.com/)
[![Algorithms](https://img.shields.io/badge/Algorithms-Graph%20Theory-blue?style=for-the-badge)](#-graph-algorithms)
[![Data Structures](https://img.shields.io/badge/Data%20Structures-Custom-green?style=for-the-badge)](#-custom-data-structures)
[![CLI](https://img.shields.io/badge/Interface-CLI-purple?style=for-the-badge)](#-command-line-interface)

</div>

---

## 🚇 Overview

**Urban Transportation System** is a command-line application that models an urban transit network as a **weighted graph**.

Stations are represented as vertices and connections between stations as weighted edges. The system provides algorithms for route finding, network analysis, station searching, minimum spanning trees, dead-end detection, and more.

```text
                 ┌───────────────┐
                 │   Central     │
                 │    Station    │
                 └───────┬───────┘
                         │
                    5.0  │
                         │
              ┌──────────▼──────────┐
              │                     │
        ┌─────▼─────┐         ┌─────▼─────┐
        │  Station A │         │  Station B │
        └─────┬─────┘         └─────┬─────┘
              │                     │
           3.0│                     │2.5
              │                     │
        ┌─────▼─────┐         ┌─────▼─────┐
        │  Station C │────────│  Station D │
        └───────────┘   4.0   └───────────┘
```

The project focuses heavily on implementing the underlying algorithms and data structures rather than relying on Java's built-in collections.

---

# ✨ Features

* 🚉 Add and manage transportation stations
* 🔗 Create weighted connections between stations
* 🔍 Search stations by name prefix
* 🧭 Find routes using **Breadth-First Search**
* 🚀 Find minimum-cost routes using **Dijkstra's algorithm**
* 🌲 Calculate a **Minimum Spanning Tree** using Kruskal's algorithm
* 📏 Calculate network diameter using **Floyd–Warshall**
* 🚧 Detect reachable dead-end stations
* ↩️ Undo graph modifications
* 🧠 Custom implementations of fundamental data structures
* 💻 Interactive command-line interface

---

# 🏗️ Architecture

The project is divided into several focused components:

```text
                     ┌─────────────────────┐
                     │      Main.java      │
                     │    Application      │
                     │      Entry Point    │
                     └──────────┬──────────┘
                                │
                                ▼
                     ┌─────────────────────┐
                     │ CommandProcessor    │
                     │        CLI          │
                     └──────────┬──────────┘
                                │
                ┌───────────────┼────────────────┐
                │               │                │
                ▼               ▼                ▼
          ┌──────────┐   ┌─────────────┐   ┌──────────┐
          │  Graph   │   │ Algorithms  │   │  Undo    │
          │          │   │             │   │ Manager  │
          └────┬─────┘   └─────────────┘   └──────────┘
               │
               ▼
        Custom Data Structures
```

`Main.java` initializes the graph, undo manager, and command processor, then continuously reads commands from standard input.

---

# 🧠 Graph Model

The transportation network is represented as a weighted graph.

```text
Station = Vertex
Connection = Edge
Weight = Cost / Distance
```

The graph implementation uses custom structures including a doubly linked list, hash table, and red-black tree.

```text
       Station A
          │
        5 │
          │
          ▼
       Station B
        /   \
      2/     \7
      /       \
     ▼         ▼
Station C   Station D
```

---

# 🧮 Graph Algorithms

## 🔎 Breadth-First Search

Finds a route based on graph traversal, useful for determining a path with a minimum number of stops.

```text
BFS(A → D)

A
│
├── B
│   └── D ✓
│
└── C
```

---

## 🚀 Dijkstra's Algorithm

Finds a minimum-cost route through the weighted transportation network.

```text
A ──5── B ──2── D
│
3
│
C ──4───────────┘

Shortest:
A → C → D
```

The command processor uses Dijkstra for the `SHORTEST_PATH` command.

---

## 🌲 Kruskal's Minimum Spanning Tree

Builds a minimum spanning tree over the transportation network.

```text
        A
       / \
      2   4
     /     \
    B───3───C
     \     /
      5   6
       \ /
        D
```

Implemented through the `MST` command using `KruskalMST`.

---

## 📏 Floyd–Warshall Diameter

Calculates the network diameter using all-pairs shortest-path analysis.

```text
       A ───── B
      /         \
     /           \
    C ─────────── D

     All-Pairs
         ↓
   Shortest Paths
         ↓
     Diameter
```

The `DIAMETER` command delegates this operation to `FloydWarshallDiameter`.

---

## 🚧 Dead-End Search

Finds reachable stations that represent dead ends from a given starting station.

```text
          A
         / \
        B   C
        │
        D ← Dead End
```

The project implements this using a dedicated `DeadEndSearch` algorithm.

---

# 🧱 Custom Data Structures

A major part of the project is implementing core data structures manually.

```text
structures/
│
├── CustomQueue
├── CustomStack
├── DoublyLinkedList
├── DynamicObjectArray
├── HashTable
├── MinHeap
├── MergeSort
├── RedBlackTree
├── DisjointSet
│
└── Supporting Nodes
    ├── QueueNode
    ├── StackNode
    ├── HashNode
    ├── HeapNode
    └── RedBlackNode
```

These structures are used throughout the graph and algorithm implementations.

### Example: Custom Queue

The project implements its own FIFO queue with `enqueue`, `dequeue`, `peek`, `isEmpty`, and `clear` operations.

```text
enqueue
   ↓
┌────┬────┬────┐
│ A  │ B  │ C  │
└────┴────┴────┘
 ↑
head

dequeue()
   ↓
┌────┬────┐
│ B  │ C  │
└────┴────┘
```

---

# ↩️ Undo System

Graph modifications are tracked through an `UndoManager`.

```text
ADD_STATION
     │
     ▼
┌──────────────┐
│ Undo Record  │
└──────┬───────┘
       │
       ▼
   Undo Stack
       │
       ▼
     UNDO
       │
       ▼
Restore Previous State
```

The command processor records station and edge additions and exposes them through the `UNDO` command.

---

# 💻 Command-Line Interface

The application is controlled through text commands.

### Add a station

```text
ADD_STATION Tehran
```

### Add a connection

```text
ADD_EDGE Tehran Shiraz 5.5
```

### Find a route

```text
BFS Tehran Shiraz
```

### Find the cheapest route

```text
SHORTEST_PATH Tehran Shiraz
```

### Find dead ends

```text
DEADENDS Tehran
```

### Minimum spanning tree

```text
MST
```

### Network diameter

```text
DIAMETER
```

### Search stations by prefix

```text
SEARCH Te
```

### Undo

```text
UNDO
```

### Exit

```text
EXIT
```

These commands are implemented by `CommandProcessor`.

---

# 📁 Project Structure

```text
urban-transportation-system/
│
├── README.md
├── .gitignore
│
└── src/
    │
    ├── Main.java
    │
    ├── algorithms/
    │   ├── BreadthFirstSearch.java
    │   ├── DeadEndSearch.java
    │   ├── DijkstraShortestPath.java
    │   ├── FloydWarshallDiameter.java
    │   ├── KruskalMST.java
    │   └── PathResult.java
    │
    ├── cli/
    │   ├── CommandProcessor.java
    │   ├── NumberFormatter.java
    │   └── OutputMessages.java
    │
    ├── graph/
    │   ├── AdjacencyNode.java
    │   ├── Edge.java
    │   ├── EdgeStore.java
    │   ├── Graph.java
    │   └── Station.java
    │
    ├── structures/
    │   ├── CustomQueue.java
    │   ├── CustomStack.java
    │   ├── DisjointSet.java
    │   ├── DoublyLinkedList.java
    │   ├── DynamicObjectArray.java
    │   ├── HashTable.java
    │   ├── MergeSort.java
    │   ├── MinHeap.java
    │   └── RedBlackTree.java
    │
    ├── undo/
    │   ├── UndoManager.java
    │   ├── UndoRecord.java
    │   └── UndoType.java
    │
    └── tests/
        └── ProjectTestRunner.java
```

The current repository contains dedicated packages for algorithms, CLI handling, graph modeling, data structures, testing, and undo functionality.

---

# 🚀 Getting Started

## Clone

```bash
git clone https://github.com/mominyar/urban-transportation-system.git
cd urban-transportation-system
```

## Compile

This is a plain Java project without a Maven/Gradle build file, so it can be compiled directly with `javac`.

```bash
mkdir -p out
javac -d out $(find src -name "*.java")
```

## Run

```bash
java -cp out Main
```

The application then waits for commands through standard input. `Main` reads each line, passes it to `CommandProcessor`, and prints the resulting output.

---

# 🧪 Testing

The repository includes a dedicated:

```text
src/tests/ProjectTestRunner.java
```

for project-level testing.


<div align="center">

# 🚇 Urban Transportation System

### Algorithms · Data Structures · Graph Theory

<br>

Built with ☕ **Java**

<br>

### 👨‍💻 Mujeeb Mominyar

Computer Engineering · Amirkabir University of Technology

<br>

<a href="https://github.com/mominyar">

<img src="https://img.shields.io/badge/GitHub-mominyar-181717?style=for-the-badge&logo=github">

</a>

<br><br>

<a href="https://github.com/mominyar">
View my GitHub profile →
</a>

</div>
