package kt11;

import java.util.Arrays;

/**
 * Задача 4. Грузы без ограничения количества, масса ровно C.
 */
public final class ExactCargo {

    private ExactCargo() {
    }

    /**
     * Для каждой массы c от 0 до C храним лучшую пару (прибыль, количество):
     * сначала больше прибыль, при равной прибыли меньше грузов. Масса c
     * получается из c - w[i] добавлением одного груза типа i, пара меняется на
     * (+p[i], +1). Такая добавка не меняет порядок пар, поэтому лучшая пара
     * для c строится из лучшей пары для c - w[i].
     *
     * Массы перебираются снизу вверх, поэтому best[c - w] уже может содержать
     * грузы того же типа: так и нужно, количество не ограничено.
     *
     * Возвращает {прибыль, количество} или null, если массу C не набрать.
     * O(n * C) по времени, O(C) по памяти.
     */
    public static long[] solve(int[] weights, int[] profits, int capacity) {
        long[] profit = new long[capacity + 1];
        int[] count = new int[capacity + 1];
        Arrays.fill(profit, 1, capacity + 1, -1);

        for (int i = 0; i < weights.length; i++) {
            int w = weights[i];
            for (int c = w; c <= capacity; c++) {
                if (profit[c - w] < 0)
                    continue;
                long candidate = profit[c - w] + profits[i];
                int items = count[c - w] + 1;
                if (candidate > profit[c] || candidate == profit[c] && items < count[c]) {
                    profit[c] = candidate;
                    count[c] = items;
                }
            }
        }
        return profit[capacity] < 0 ? null : new long[] {profit[capacity], count[capacity]};
    }
}
