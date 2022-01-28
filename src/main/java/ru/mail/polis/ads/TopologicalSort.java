import java.io.*;
import java.util.*;

public class TopologicalSort {
    static Stack<Integer> stack = new Stack<>();
    static List<Integer>[] points;
    static int[] used;
    static int vertices;
    static int edges;
    static boolean isCycle;

    private static void filling(final FastScanner in) {
        vertices = in.nextInt();
        edges = in.nextInt();
        int from;
        int to;
        used = new int[vertices + 1];

        //ввод ребер в список смежности
        points = new ArrayList[vertices + 1];
        for (int i = 0; i < vertices + 1; i++) {
            points[i] = new ArrayList<>();
        }

        for (int i = 0; i < edges; i++) {
            from = in.nextInt();
            to = in.nextInt();
            if (to == from) { // петли
                isCycle = true;
            }
            points[from].add(to);
        }
    }

    private static boolean DFS(int vertex) {
        used[vertex] = 1;
        for (int point : points[vertex]) {
            if (used[point] == 0) {
                DFS(point);
            }
            if (used[point] == 1) {
                return true;
            }
        }
        stack.push(vertex);
        used[vertex] = 2;
        return false;
    }

    private static void solve(final FastScanner in, final PrintWriter out) {
        filling(in);

        //topological sort
        for (int i = 1; i < vertices + 1; i++) {
            if (used[i] == 0 && DFS(i) || isCycle) {
                out.print("-1");
                return;
            }
        }
        while (!stack.isEmpty()) {
            out.printf("%d ", stack.pop());
        }
        out.flush();
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

