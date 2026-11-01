package kt11;

/**
 * Задача 1. Минимальная стоимость пути робота по платформам.
 */
public final class PlatformJumps {

    private PlatformJumps() {
    }

    /**
     * cost[i] - минимальная стоимость дойти до платформы i. На неё попадают
     * либо с i - 1, либо с i - 2, поэтому
     * cost[i] = min(cost[i - 1] + |h[i] - h[i - 1]|, cost[i - 2] + |h[i] - h[i - 2]|).
     * Нужны только два предыдущих значения, массив не хранится.
     *
     * Разность высот доходит до 2 * 10^9, поэтому всё в long. O(n) по времени, O(1) по памяти.
     */
    public static long minCost(int[] heights) {
        long beforePrevious = 0;
        long previous = Math.abs((long) heights[1] - heights[0]);
        for (int i = 2; i < heights.length; i++) {
            long current = Math.min(previous + Math.abs((long) heights[i] - heights[i - 1]),
                    beforePrevious + Math.abs((long) heights[i] - heights[i - 2]));
            beforePrevious = previous;
            previous = current;
        }
        return previous;
    }
}
