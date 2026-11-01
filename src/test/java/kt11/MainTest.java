package kt11;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Проверка чтения входных данных и формата вывода для каждой задачи.
 */
class MainTest {

    private static String run(int task, String input) throws IOException {
        StringWriter output = new StringWriter();
        PrintWriter out = new PrintWriter(output);
        FastReader in = new FastReader(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        Main.solve(task, in, out);
        out.flush();
        return output.toString().replace("\r\n", "\n");
    }

    @Test
    void task1() throws IOException {
        assertEquals("30\n", run(1, "4\n10 30 40 20\n"));
    }

    @Test
    void task2() throws IOException {
        assertEquals("90\n", run(2, "3 8\n3 30\n4 50\n5 60\n"));
    }

    @Test
    void task3() throws IOException {
        assertEquals("3\n", run(3, "abcde\nace\n"));
    }

    @Test
    void task4() throws IOException {
        assertEquals("6 2\n", run(4, "2 6\n2 2\n3 3\n"));
        assertEquals("-1\n", run(4, "2 7\n2 5\n4 7\n"));
    }

    @Test
    void task5() throws IOException {
        assertEquals("4 2\n", run(5, "5\n1 3 5 4 7\n"));
    }

    @Test
    void task6() throws IOException {
        assertEquals("cabac\n", run(6, "abac\ncab\n"));
    }

    @Test
    void windowsLineEndings() throws IOException {
        assertEquals("3\n", run(3, "abcde\r\nace\r\n"));
    }

    @Test
    void truncatedInputThrows() {
        assertThrows(EOFException.class, () -> run(2, "3 8\n3 30\n"));
    }

    @Test
    void unknownTaskThrows() {
        assertThrows(IllegalArgumentException.class, () -> run(7, ""));
    }
}
