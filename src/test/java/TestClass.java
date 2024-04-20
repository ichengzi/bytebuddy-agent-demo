import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

/**
 * @author ReaJason
 * @since 2024/4/18
 */
public class TestClass {

    public static String stringToUnicodeSequence(String str) {
        StringBuilder sb = new StringBuilder();

        for (char ch : str.toCharArray()) {
            String unicodeSeq = String.format("\\u%04X", (int) ch);
            sb.append(unicodeSeq);
        }

        return sb.toString();
    }

    public static String unicodeSequenceToString(String unicodeStr) {
        StringBuilder sb = new StringBuilder();
        String[] unicodeTokens = unicodeStr.split("\\\\u");

        for (int i = 1; i < unicodeTokens.length; i++) {
            int codePoint = Integer.parseInt(unicodeTokens[i], 16);
            sb.append(Character.toChars(codePoint));
        }

        return sb.toString();
    }

    @Test
    public void test() {
        System.out.println(stringToUnicodeSequence("ჵḚ"));
        System.out.println(unicodeSequenceToString("\\u10F5\\u1E1A"));
    }
}
