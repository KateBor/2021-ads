import java.io.*;
import java.util.*;

public class Condensation {
    static List<Integer>[] points;
    static List<Integer>[] reversedPoints;
    static List<Integer> stack;
    static boolean[] used;
    static int vertices;
    static int edges;
    static int componentCount;
    static int[] vertexComponent;

    private static void filling(final FastScanner in) {
        vertices = in.nextInt();
        edges = in.nextInt();
        used = new boolean[vertices + 1];
        stack = new ArrayList<>();

        //ввод ребер в список смежности
        points = new ArrayList[vertices + 1];
        reversedPoints = new ArrayList[vertices + 1];
        vertexComponent = new int[vertices + 1];
        for (int i = 0; i < vertices + 1; i++) {
            points[i] = new ArrayList<>();
            reversedPoints[i] = new ArrayList<>();
        }

        for (int i = 0; i < edges; i++) {
            int from = in.nextInt();
            int to = in.nextInt();
            points[from].add(to);
            reversedPoints[to].add(from);
        }
    }

    private static void DFS(int vertex) {
        used[vertex] = true;
        for (int point : points[vertex]) {
            if (!used[point]) {
                DFS(point);
            }
        }
        stack.add(vertex);
    }

    private static void reverseDFS(int vertex, int number) {
        used[vertex] = false;
        vertexComponent[vertex] = number;
        for (int point : reversedPoints[vertex]) {
            if (used[point]) {
                reverseDFS(point, number);
            }
        }
    }

    private static void solve(final FastScanner in, final PrintWriter out) {
        filling(in);
        for (int i = 1; i <= vertices; ++i) {
            if (!used[i]) {
                DFS(i);
            }
        }
        for (int i = vertices - 1; i >= 0; --i) {
            int v = stack.get(i);
            if (used[v]) {
                reverseDFS(v, componentCount++);
            }
        }
        Set<AbstractMap.SimpleEntry<Integer, Integer>> edges = new HashSet<>();

        for (int i = 1; i <= vertices; i++) {
            List<Integer> p = points[i];
            for (int j : p)
            {
                if (vertexComponent[i] != vertexComponent[j])
                {
                    edges.add(new AbstractMap.SimpleEntry<>(vertexComponent[i], vertexComponent[j]));
                }
            }
        }
        out.print(edges.size());
    }

    private static class FastScanner {
        private final BufferedReader reader;
        private StringTokenizer tokenizer;

        FastScanner(final InputStream in) {
            reader = new BufferedReader(new InputStreamReader(in));
        }

        String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return tokenizer.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }

    public static void main(final String[] arg) {
        final FastScanner in = new FastScanner(System.in);
        try (PrintWriter out = new PrintWriter(System.out, false)) {
            solve(in, out);
        }
    }
}