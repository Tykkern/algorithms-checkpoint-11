package kt11;

/**
 * Задача 3. Наибольшая общая подпоследовательность двух строк.
 */
public final class CommonSubsequence {

    private CommonSubsequence() {
    }

    /**
     * lcs[i][j] - ответ для префиксов s[0..i) и t[0..j). Если последние символы
     * совпадают, они входят в общую строку: lcs[i - 1][j - 1] + 1. Иначе один
     * из них лишний: max(lcs[i - 1][j], lcs[i][j - 1]).
     *
     * Строка таблицы зависит только от предыдущей, поэтому хранятся две строки.
     * O(|s| * |t|) по времени, O(|t|) по памяти.
     */
    public static int length(String s, String t) {
        int[] previous = new int[t.length() + 1];
        int[] current = new int[t.length() + 1];
        for (int i = 1; i <= s.length(); i++) {
            char c = s.charAt(i - 1);
            for (int j = 1; j <= t.length(); j++)
                current[j] = c == t.charAt(j - 1)
                        ? previous[j - 1] + 1
                        : Math.max(previous[j], current[j - 1]);
            int[] swap = previous;
            previous = current;
            current = swap;
        }
        return previous[t.length()];
    }
}
