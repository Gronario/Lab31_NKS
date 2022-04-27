package lab2_21;

import java.util.ArrayList;
import java.util.List;


public class DFS {
    private final int vertices;
    private List<List<Integer>> adjList;
    private List<List<Integer>> paths;

    public DFS(final int vertices) {
        this.vertices = vertices;
        matrix_fill();
    }

    private void matrix_fill() {
        this.adjList = new ArrayList<>();
        this.paths = new ArrayList<>(100);
        for (int i = 0; i < this.vertices; i++) {
            this.adjList.add(new ArrayList<>());
        }
    }
    private void edge_creation(final Integer node, final Integer connector) {
        this.adjList.get(node).add(connector);
    }
    private void path_print(final Integer source, final Integer dest) {
        final boolean[] isVisited = new boolean[this.vertices];
        final ArrayList<Integer> pathList = new ArrayList<>();
        pathList.add(source);
        System.out.printf("Всі шляхи від %s до %d:\n", source, dest);
        print(source, dest, isVisited, pathList);
    }
    public void printResult(final List<List<Integer>> matrix) {
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).size(); j++) {
                if (matrix.get(i).get(j) == 1) {
                    this.edge_creation(i, j);
                }
            }
        }
        this.path_print(0, matrix.size() - 1);
    }
    private void print(final Integer node,
                       final Integer connector,
                       final boolean[] isVisited,
                       final List<Integer> localPathList
    ) {
        if (node.equals(connector)) {
            System.out.println(localPathList);
            this.paths.add(new ArrayList<>(localPathList));
            return;
        }
        isVisited[node] = true;
        for (final Integer i : this.adjList.get(node)) {
            if (!isVisited[i]) {
                localPathList.add(i);
                print(i, connector, isVisited, localPathList);
                localPathList.remove(i);
            }
        }
        isVisited[node] = false;
    }
    public List<List<Integer>> getPaths() {
        return this.paths;
    }
}
