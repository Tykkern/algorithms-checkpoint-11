package kt11;

import java.util.Arrays;

/**
 * Задача 5. Длина наибольшей возрастающей подпоследовательности и число таких подпоследовательностей.
 */
public final class IncreasingSubsequences {

    static final int MOD = 1_000_000_007;

    private IncreasingSubsequences() {
    }

    /**
     * Для подпоследовательностей, оканчивающихся на a[i], нужна лучшая длина
     * и число способов среди тех, что оканчиваются на меньшее значение левее i.
     * Значения сжимаются в ранги 1..d, и по рангам строится дерево Фенвика,
     * где в каждой ячейке хранится пара (длина, количество). Запрос на префиксе
     * рангов меньше ранга a[i] даёт лучшую пару для строго меньших значений,
     * затем a[i] записывается в своё место с длиной на 1 больше.
     *
     * Пары объединяются так: большая длина побеждает, при равной длине
     * количества складываются по модулю.
     *
     * Возвращает {длина, количество по модулю 10^9 + 7}.
     * O(n log n) по времени, O(n) по памяти.
     */
    public static long[] solve(int[] a) {
        int[] sorted = a.clone();
        Arrays.sort(sorted);
        int distinct = 0;
        for (int value : sorted)
            if (distinct == 0 || sorted[distinct - 1] != value)
                sorted[distinct++] = value;

        int[] length = new int[distinct + 1];
        long[] ways = new long[distinct + 1];
        int bestLength = 0;
        long bestWays = 0;

        for (int value : a) {
            int rank = Arrays.binarySearch(sorted, 0, distinct, value) + 1;

            int prefixLength = 0;
            long prefixWays = 1;
            for (int i = rank - 1; i > 0; i -= i & -i) {
                if (length[i] > prefixLength) {
                    prefixLength = length[i];
                    prefixWays = ways[i];
                } else if (length[i] == prefixLength && prefixLength > 0) {
                    prefixWays = (prefixWays + ways[i]) % MOD;
                }
            }

            int newLength = prefixLength + 1;
            for (int i = rank; i <= distinct; i += i & -i) {
                if (newLength > length[i]) {
                    length[i] = newLength;
                    ways[i] = prefixWays;
                } else if (newLength == length[i]) {
                    ways[i] = (ways[i] + prefixWays) % MOD;
                }
            }

            if (newLength > bestLength) {
                bestLength = newLength;
                bestWays = prefixWays;
            } else if (newLength == bestLength) {
                bestWays = (bestWays + prefixWays) % MOD;
            }
        }
        return new long[] {bestLength, bestWays};
    }
}
