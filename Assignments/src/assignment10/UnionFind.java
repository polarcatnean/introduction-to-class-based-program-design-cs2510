package assignment10;

class UnionFind {
  int[] parent;
  int[] rank;

  UnionFind(int size) {
    parent = new int[size];
    rank = new int[size];
    for (int i = 0; i < size; i++) {
      parent[i] = i;
      rank[i] = 0;
    }
  }

  int find(int x) {
    if (parent[x] != x)
      parent[x] = find(parent[x]); // path compression
    return parent[x];
  }

  void union(int x, int y) {
    int rootX = find(x);
    int rootY = find(y);
    if (rootX == rootY)
      return;

    // union by rank
    if (rank[rootX] < rank[rootY]) {
      parent[rootX] = rootY;
    }
    else if (rank[rootX] > rank[rootY]) {
      parent[rootY] = rootX;
    }
    else {
      parent[rootY] = rootX;
      rank[rootX]++;
    }
  }
}
