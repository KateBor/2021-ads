import java.io.*;
import java.util.*;

public class ShortestWay {

    private static void solve(final FastScanner in, final PrintWriter out) {
        int vertices = in.nextInt();
        int edges = in.nextInt();
        int vertexA = in.nextInt();
        int vertexB = in.nextInt();
        int current;
        int from;
        int to;

        //ввод ребер в список смежности
        List<Integer>[] points = new ArrayList[vertices + 1];
        for (int i = 0; i < vertices + 1; i++) {
            points[i] = new ArrayList<>();
        }

        for (int i = 0; i < edges; i++) {
            from = in.nextInt();
            to = in.nextInt();
            points[from].add(to);
            points[to].add(from);
        }

        //BFS
        int[] visitedVerticesLength = new int[vertices + 1];
        int[] visitedVerticesPrev = new int[vertices + 1];
        Queue<Integer> queue = new LinkedList<>();
        queue.add(vertexA);
        visitedVerticesPrev[vertexA] = -1;

        while (!queue.isEmpty()) {
            current = queue.poll();
            for (int i = 0; i < points[current].size(); i++) {
                int j = points[current].get(i);
                if (visitedVerticesPrev[j] == 0) {
                    visitedVerticesLength[j] = visitedVerticesLength[current] + 1;
                    visitedVerticesPrev[j] = current;
                    queue.add(j);
                }
            }
        }

        if (visitedVerticesPrev[vertexB] != 0) {
            LinkedList<Integer> list = new LinkedList<>();
            int index = vertexB;
            while (index != -1) {
                list.add(index);
                index = visitedVerticesPrev[index];
            }
            out.print(visitedVerticesLength[vertexB]);
            out.print('\n');
            for (int i = list.size() - 1; i >= 0 ; i--) {
                out.print(list.get(i));
                out.print(' ');
            }
        } else {
            out.print("-1");
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
