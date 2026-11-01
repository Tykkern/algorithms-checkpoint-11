package kt11;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SolutionsTest {

    private static final Random RANDOM = new Random(11);

    private static String randomWord(int length, int letters) {
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < length; i++)
            word.append((char) ('a' + RANDOM.nextInt(letters)));
        return word.toString();
    }

    private static boolean isSubsequence(String part, String whole) {
        int i = 0;
        for (int j = 0; j < whole.length() && i < part.length(); j++)
            if (part.charAt(i) == whole.charAt(j))
                i++;
        return i == part.length();
    }

    @Nested
    class PlatformJumpsTest {

        private long bruteForce(int[] h, int at) {
            if (at == h.length - 1)
                return 0;
            long best = Math.abs((long) h[at + 1] - h[at]) + bruteForce(h, at + 1);
            if (at + 2 < h.length)
                best = Math.min(best, Math.abs((long) h[at + 2] - h[at]) + bruteForce(h, at + 2));
            return best;
        }

        @Test
        void examples() {
            assertEquals(30, PlatformJumps.minCost(new int[] {10, 30, 40, 20}));
            assertEquals(5, PlatformJumps.minCost(new int[] {0, 5}));
            assertEquals(0, PlatformJumps.minCost(new int[] {7, 7, 7}));
        }

        @Test
        void jumpOverPeak() {
            assertEquals(0, PlatformJumps.minCost(new int[] {1, 100, 1}));
        }

        @Test
        void randomMatchesBruteForce() {
            for (int test = 0; test < 500; test++) {
                int[] h = RANDOM.ints(2 + RANDOM.nextInt(14), -20, 21).toArray();
                assertEquals(bruteForce(h, 0), PlatformJumps.minCost(h));
            }
        }

        @Test
        @Timeout(5)
        void largeMonotoneHeights() {
            // На неубывающих высотах любой путь стоит ровно h[n - 1] - h[0], больше int.
            int n = 200_000;
            int[] h = new int[n];
            for (int i = 0; i < n; i++)
                h[i] = -1_000_000_000 + i * 10_000;
            assertEquals((long) h[n - 1] - h[0], PlatformJumps.minCost(h));
        }
    }

    @Nested
    class KnapsackTest {

        private long bruteForce(int[] w, int[] v, int capacity) {
            long best = 0;
            for (int mask = 0; mask < 1 << w.length; mask++) {
                long weight = 0;
                long value = 0;
                for (int i = 0; i < w.length; i++)
                    if ((mask >> i & 1) == 1) {
                        weight += w[i];
                        value += v[i];
                    }
                if (weight <= capacity)
                    best = Math.max(best, value);
            }
            return best;
        }

        @Test
        void example() {
            assertEquals(90, Knapsack.maxValue(new int[] {3, 4, 5}, new int[] {30, 50, 60}, 8));
        }

        @Test
        void itemTakenOnlyOnce() {
            assertEquals(10, Knapsack.maxValue(new int[] {1}, new int[] {10}, 5));
        }

        @Test
        void randomMatchesBruteForce() {
            for (int test = 0; test < 500; test++) {
                int n = 1 + RANDOM.nextInt(10);
                int capacity = 1 + RANDOM.nextInt(30);
                int[] w = RANDOM.ints(n, 1, capacity + 1).toArray();
                int[] v = RANDOM.ints(n, 1, 50).toArray();
                assertEquals(bruteForce(w, v, capacity), Knapsack.maxValue(w, v, capacity));
            }
        }

        @Test
        @Timeout(5)
        void largeInput() {
            int n = 200;
            int capacity = 100_000;
            int[] v = new int[n];
            Arrays.fill(v, 1_000_000_000);

            int[] exact = new int[n];
            Arrays.fill(exact, 500);
            assertEquals(200_000_000_000L, Knapsack.maxValue(exact, v, capacity));

            int[] heavier = new int[n];
            Arrays.fill(heavier, 501);
            assertEquals(199_000_000_000L, Knapsack.maxValue(heavier, v, capacity));
        }
    }

    @Nested
    class CommonSubsequenceTest {

        private int bruteForce(String s, String t) {
            int best = 0;
            for (int mask = 0; mask < 1 << s.length(); mask++) {
                StringBuilder part = new StringBuilder();
                for (int i = 0; i < s.length(); i++)
                    if ((mask >> i & 1) == 1)
                        part.append(s.charAt(i));
                if (part.length() > best && isSubsequence(part.toString(), t))
                    best = part.length();
            }
            return best;
        }

        @Test
        void examples() {
            assertEquals(3, CommonSubsequence.length("abcde", "ace"));
            assertEquals(0, CommonSubsequence.length("abc", "def"));
            assertEquals(4, CommonSubsequence.length("abcbdab", "bdcaba"));
        }

        @Test
        void randomMatchesBruteForce() {
            for (int test = 0; test < 500; test++) {
                String s = randomWord(1 + RANDOM.nextInt(10), 3);
                String t = randomWord(1 + RANDOM.nextInt(10), 3);
                assertEquals(bruteForce(s, t), CommonSubsequence.length(s, t));
            }
        }

        @Test
        @Timeout(5)
        void largeStrings() {
            String s = randomWord(3000, 26);
            assertEquals(3000, CommonSubsequence.length(s, s));
            assertEquals(0, CommonSubsequence.length("a".repeat(3000), "b".repeat(3000)));
        }
    }

    @Nested
    class ExactCargoTest {

        // Перебор всех количеств грузов каждого типа с массой ровно capacity.
        private long[] bruteForce(int[] w, int[] p, int capacity, int type, long profit, int count) {
            if (capacity == 0)
                return new long[] {profit, count};
            if (type == w.length)
                return null;
            long[] best = null;
            for (int k = 0; k * w[type] <= capacity; k++) {
                long[] option = bruteForce(w, p, capacity - k * w[type], type + 1,
                        profit + (long) k * p[type], count + k);
                if (option != null && (best == null || option[0] > best[0]
                        || option[0] == best[0] && option[1] < best[1]))
                    best = option;
            }
            return best;
        }

        @Test
        void maxProfitThenFewerItems() {
            // Масса 6: 2+2+2 дают 6, 3+3 тоже 6, но грузов меньше.
            assertArrayEquals(new long[] {6, 2}, ExactCargo.solve(new int[] {2, 3}, new int[] {2, 3}, 6));
            assertArrayEquals(new long[] {10, 5}, ExactCargo.solve(new int[] {1, 5}, new int[] {2, 9}, 5));
        }

        @Test
        void impossibleMass() {
            assertNull(ExactCargo.solve(new int[] {2, 4}, new int[] {5, 7}, 7));
        }

        @Test
        void randomMatchesBruteForce() {
            for (int test = 0; test < 500; test++) {
                int n = 1 + RANDOM.nextInt(4);
                int capacity = 1 + RANDOM.nextInt(15);
                int[] w = RANDOM.ints(n, 1, capacity + 1).toArray();
                int[] p = RANDOM.ints(n, 1, 6).toArray();
                long[] expected = bruteForce(w, p, capacity, 0, 0, 0);
                long[] actual = ExactCargo.solve(w, p, capacity);
                if (expected == null)
                    assertNull(actual);
                else
                    assertArrayEquals(expected, actual);
            }
        }

        @Test
        @Timeout(5)
        void largeInput() {
            int n = 150;
            int capacity = 200_000;
            int[] w = new int[n];
            int[] p = new int[n];
            for (int i = 0; i < n; i++) {
                w[i] = i + 1;
                p[i] = 1_000_000_000;
            }
            // Больше всего прибыли, если взять C грузов массы 1.
            assertArrayEquals(new long[] {200_000_000_000_000L, 200_000}, ExactCargo.solve(w, p, capacity));
        }
    }

    @Nested
    class IncreasingSubsequencesTest {

        private long[] bruteForce(int[] a) {
            long bestLength = 0;
            long ways = 0;
            for (int mask = 1; mask < 1 << a.length; mask++) {
                int last = Integer.MIN_VALUE;
                boolean increasing = true;
                boolean first = true;
                for (int i = 0; i < a.length && increasing; i++)
                    if ((mask >> i & 1) == 1) {
                        increasing = first || a[i] > last;
                        last = a[i];
                        first = false;
                    }
                if (!increasing)
                    continue;
                int length = Integer.bitCount(mask);
                if (length > bestLength) {
                    bestLength = length;
                    ways = 1;
                } else if (length == bestLength) {
                    ways++;
                }
            }
            return new long[] {bestLength, ways};
        }

        @Test
        void examples() {
            assertArrayEquals(new long[] {4, 2}, IncreasingSubsequences.solve(new int[] {1, 3, 5, 4, 7}));
            assertArrayEquals(new long[] {1, 5}, IncreasingSubsequences.solve(new int[] {2, 2, 2, 2, 2}));
            assertArrayEquals(new long[] {1, 1}, IncreasingSubsequences.solve(new int[] {-1_000_000_000}));
        }

        @Test
        void randomMatchesBruteForce() {
            for (int test = 0; test < 500; test++) {
                int[] a = RANDOM.ints(1 + RANDOM.nextInt(12), -5, 6).toArray();
                assertArrayEquals(bruteForce(a), IncreasingSubsequences.solve(a));
            }
        }

        @Test
        @Timeout(5)
        void largeCountTakenModulo() {
            // Пары (2, 1), (4, 3), ...: длина n / 2, из каждой пары берётся любой элемент.
            int n = 200_000;
            int[] a = new int[n];
            for (int i = 0; i < n; i += 2) {
                a[i] = i + 2;
                a[i + 1] = i + 1;
            }
            long expected = BigInteger.TWO.modPow(BigInteger.valueOf(n / 2),
                    BigInteger.valueOf(IncreasingSubsequences.MOD)).longValue();
            assertArrayEquals(new long[] {n / 2, expected}, IncreasingSubsequences.solve(a));
        }

        @Test
        @Timeout(5)
        void largeSortedInput() {
            int n = 200_000;
            int[] a = new int[n];
            for (int i = 0; i < n; i++)
                a[i] = i * 10_000 - 1_000_000_000;
            assertArrayEquals(new long[] {n, 1}, IncreasingSubsequences.solve(a));
        }
    }

    @Nested
    class ShortestSupersequenceTest {

        // Перебор строк по возрастанию длины и в лексикографическом порядке, первая подходящая и есть ответ.
        private String bruteForce(String s, String t, int letters) {
            for (int length = Math.max(s.length(), t.length()); ; length++) {
                int total = 1;
                for (int i = 0; i < length; i++)
                    total *= letters;
                for (int code = 0; code < total; code++) {
                    char[] word = new char[length];
                    int rest = code;
                    for (int i = length - 1; i >= 0; i--) {
                        word[i] = (char) ('a' + rest % letters);
                        rest /= letters;
                    }
                    String candidate = new String(word);
                    if (isSubsequence(s, candidate) && isSubsequence(t, candidate))
                        return candidate;
                }
            }
        }

        @Test
        void examples() {
            assertEquals("cabac", ShortestSupersequence.build("abac", "cab"));
            assertEquals("ab", ShortestSupersequence.build("a", "b"));
            assertEquals("ab", ShortestSupersequence.build("b", "a"));
            assertEquals("abc", ShortestSupersequence.build("abc", "abc"));
        }

        @Test
        void randomMatchesBruteForce() {
            for (int test = 0; test < 300; test++) {
                String s = randomWord(1 + RANDOM.nextInt(3), 3);
                String t = randomWord(1 + RANDOM.nextInt(3), 3);
                assertEquals(bruteForce(s, t, 3), ShortestSupersequence.build(s, t), s + " " + t);
            }
        }

        @Test
        @Timeout(5)
        void largeStrings() {
            assertEquals("a".repeat(1000) + "b".repeat(1000),
                    ShortestSupersequence.build("b".repeat(1000), "a".repeat(1000)));

            String s = randomWord(1000, 4);
            String t = randomWord(1000, 4);
            String result = ShortestSupersequence.build(s, t);
            assertEquals(2000 - CommonSubsequence.length(s, t), result.length());
            assertTrue(isSubsequence(s, result));
            assertTrue(isSubsequence(t, result));
        }
    }
}
