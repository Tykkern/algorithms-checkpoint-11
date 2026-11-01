package kt11;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Запуск решения по номеру задачи: входные данные читаются из stdin
 * в формате из задания, ответ печатается в stdout.
 *
 * Пример: gradlew run -q --args="2" < input.txt
 */
public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Укажите номер задачи от 1 до 6, входные данные подаются в stdin.");
            System.exit(1);
        }

        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
        solve(Integer.parseInt(args[0]), new FastReader(System.in), out);
        out.flush();
    }

    static void solve(int task, FastReader in, PrintWriter out) throws IOException {
        switch (task) {
            case 1 -> out.println(PlatformJumps.minCost(in.nextInts(in.nextInt())));
            case 2 -> {
                int n = in.nextInt();
                int capacity = in.nextInt();
                int[][] items = readPairs(in, n);
                out.println(Knapsack.maxValue(items[0], items[1], capacity));
            }
            case 3 -> out.println(CommonSubsequence.length(in.next(), in.next()));
            case 4 -> {
                int n = in.nextInt();
                int capacity = in.nextInt();
                int[][] cargo = readPairs(in, n);
                long[] answer = ExactCargo.solve(cargo[0], cargo[1], capacity);
                out.println(answer == null ? "-1" : answer[0] + " " + answer[1]);
            }
            case 5 -> {
                long[] answer = IncreasingSubsequences.solve(in.nextInts(in.nextInt()));
                out.println(answer[0] + " " + answer[1]);
            }
            case 6 -> out.println(ShortestSupersequence.build(in.next(), in.next()));
            default -> throw new IllegalArgumentException("Нет задачи с номером " + task + ", есть 1-6.");
        }
    }

    // n строк по два числа: первые числа идут в [0], вторые в [1].
    private static int[][] readPairs(FastReader in, int n) throws IOException {
        int[][] pairs = new int[2][n];
        for (int i = 0; i < n; i++) {
            pairs[0][i] = in.nextInt();
            pairs[1][i] = in.nextInt();
        }
        return pairs;
    }
}
