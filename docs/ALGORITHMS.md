# Urban Transportation - Algorithm Reference

## Core Algorithms

### 1. Dijkstra's Shortest Path
- **Use**: Find fastest route between two stops
- **Complexity**: O((V + E) log V)
- **Data Structure**: Priority Queue (Min-Heap)

### 2. A* Search
- **Use**: Optimized pathfinding with heuristic
- **Complexity**: O(E) best case
- **Heuristic**: Haversine distance between coordinates

### 3. BFS (Breadth-First Search)
- **Use**: Find route with minimum transfers
- **Complexity**: O(V + E)

### 4. Floyd-Warshall
- **Use**: All-pairs shortest paths (pre-computation)
- **Complexity**: O(V³)

## Data Structures
| Structure | Usage |
|-----------|-------|
| Adjacency List | Graph representation |
| Min-Heap | Priority queue for Dijkstra |
| HashMap | Station lookup O(1) |
| LinkedList | Route segments |

## Optimization Techniques
- Edge weight = time + transfers * penalty
- Cache frequently requested routes
- Bidirectional search for long routes
