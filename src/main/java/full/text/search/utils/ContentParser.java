package full.text.search.utils;

import java.util.List;

public class ContentParser {

    private static final List<String> symbols = List.of(
            ",", ".", "!", "@", "#", "$", "%", "^", "`",
            "&", "*", "(", ")", "_", "-", "=", "+", "~", "?", ">", "<", "|", "\\", "]", "[", "'");

    private static final List<String> smallTalk = List.of(" of ", " a ", " an ", " the ", " in ", " out ", " at ");

    public static String[] parse(String content) {
        content = " " + content.toLowerCase() + " ";

        for (String symbol : symbols) {
            content = content.replace(symbol, "");
        }

        for (String talk : smallTalk) {
            content = content.replace(talk, "");
        }

        return content.trim().split(" ");
    }
}
