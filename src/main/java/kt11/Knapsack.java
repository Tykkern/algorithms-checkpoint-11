package kt11;

/**
 * Задача 2. Рюкзак: каждый предмет можно взять не больше одного раза.
 */
public final class Knapsack {

    private Knapsack() {
    }

    /**
     * best[c] - максимальная полезность набора уже рассмотренных предметов
     * с массой не больше c. Очередной предмет либо не берём, либо берём и
     * добавляем к лучшему набору с массой c - w. Массы перебираются сверху
     * вниз: так best[c - w] ещё не учитывает текущий предмет, и он попадает
     * в набор не больше одного раза.
     *
     * O(n * W) по времени, O(W) по памяти.
     */
    public static long maxValue(int[] weights, int[] values, int capacity) {
        long[] best = new long[capacity + 1];
        for (int i = 0; i < weights.length; i++)
            for (int c = capacity; c >= weights[i]; c--)
                best[c] = Math.max(best[c], best[c - weights[i]] + values[i]);
        return best[capacity];
    }
}
