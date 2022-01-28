import java.io.*;
import java.util.*;

public class Cycles {
    static List<Integer>[] points;
    static int[] parents;
    static int[] used;
    static int vertices;
    static int edges;
    static int min = Integer.MAX_VALUE;

    private static void filling(final FastScanner in) {
        vertices = in.nextInt();
        edges = in.nextInt();
        int from;
        int to;
        used = new int[vertices + 1];
        parents = new int[vertices + 1];

        //ввод ребер в список cмежности
        points = new ArrayList[vertices + 1];
        for (int i = 0; i < vertices + 1; i++) {
            points[i] = new ArrayList<>();
        }

        for (int i = 0; i < edges; ++i) {
            from = in.nextInt();
            to = in.nextInt();
            points[from].add(to);
            points[to].add(from);
        }
    }

    private static void DFS(int vertex) {
        used[vertex] = 1;
        for (int point : points[vertex]) {
            if (parents[vertex] == point) {
                continue;
            }
            if (used[point] == 0) {
                parents[point] = vertex;
                DFS(point);
            }
            else if (used[point] == 1) {
                findMinInCycle(vertex, point);
                used[vertex] = 2;
                return;
            }
        }
        used[vertex] = 2;
    }

    private static void findMinInCycle(int vertex, int point) {
        min = Math.min(min, point);
        for (int i = vertex; i != point; i = parents[i]) {
            min = Math.min(min, i);
        }
    }

    private static void solve(final FastScanner in, final PrintWriter out) {
        filling(in);

        for (List<Integer> list : points) {
            list.sort(null);
        }

        for (int i = 1; i < vertices + 1; i++) {
            if (used[i] == 0) {
                parents[i] = -1;
                DFS(i);
            }
        }

        if (min == Integer.MAX_VALUE) {
            out.print("No");
        } else {
            out.printf("Yes\n%d", min);
        }
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