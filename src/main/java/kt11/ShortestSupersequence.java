package kt11;

/**
 * Задача 6. Кратчайшая строка, содержащая s и t как подпоследовательности,
 * а среди кратчайших лексикографически наименьшая.
 */
public final class ShortestSupersequence {

    private ShortestSupersequence() {
    }

    /**
     * rest[i][j] - длина кратчайшей общей надпоследовательности суффиксов
     * s[i..] и t[j..]. Если один суффикс пуст, это длина другого. Если
     * s[i] == t[j], символ общий: 1 + rest[i + 1][j + 1]. Иначе следующим
     * пишется s[i] или t[j]: 1 + min(rest[i + 1][j], rest[i][j + 1]).
     *
     * Затем строка собирается слева направо. Писать символ, которого нет
     * ни в начале s[i..], ни в начале t[j..], бессмысленно: длина не уменьшится.
     * Значит, кандидатов не больше двух, s[i] и t[j], и каждый ведёт в своё
     * состояние. Из тех, что сохраняют минимальную длину, берём меньший
     * символ: все варианты одной длины, поэтому порядок решает первый
     * отличающийся символ.
     *
     * O(|s| * |t|) по времени и памяти.
     */
    public static String build(String s, String t) {
        int n = s.length();
        int m = t.length();
        int[][] rest = new int[n + 1][m + 1];
        for (int i = n; i >= 0; i--)
            for (int j = m; j >= 0; j--) {
                if (i == n)
                    rest[i][j] = m - j;
                else if (j == m)
                    rest[i][j] = n - i;
                else if (s.charAt(i) == t.charAt(j))
                    rest[i][j] = 1 + rest[i + 1][j + 1];
                else
                    rest[i][j] = 1 + Math.min(rest[i + 1][j], rest[i][j + 1]);
            }

        StringBuilder result = new StringBuilder(rest[0][0]);
        int i = 0;
        int j = 0;
        while (i < n || j < m) {
            char next;
            if (i == n)
                next = t.charAt(j);
            else if (j == m)
                next = s.charAt(i);
            else if (s.charAt(i) == t.charAt(j))
                next = s.charAt(i);
            else {
                char a = s.charAt(i);
                char b = t.charAt(j);
                boolean aFits = rest[i + 1][j] + 1 == rest[i][j];
                boolean bFits = rest[i][j + 1] + 1 == rest[i][j];
                next = aFits && bFits ? (char) Math.min(a, b) : aFits ? a : b;
            }

            result.append(next);
            if (i < n && s.charAt(i) == next)
                i++;
            if (j < m && t.charAt(j) == next)
                j++;
        }
        return result.toString();
    }
}
