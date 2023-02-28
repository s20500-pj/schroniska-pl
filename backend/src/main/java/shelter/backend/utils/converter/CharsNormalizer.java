package shelter.backend.utils.converter;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CharsNormalizer {

    private static final String UNICODE_MARK = "\\p{M}";

    private static final Map<String, String> charsToReplaceMap;

    static {
        charsToReplaceMap = new HashMap<>();
        charsToReplaceMap.put("ł", "l");
        charsToReplaceMap.put("Ł", "L");
    }

    public static String convertToEngChars(String stringToReplace) {

        stringToReplace = Arrays.stream(stringToReplace.split(""))
                .map(c -> {
                    String letter = charsToReplaceMap.get(c);
                    return letter == null ? c : letter;
                })
                .collect(Collectors.joining());

        return removeAccent(stringToReplace);
    }

    public static String removeAccent(String string) {

        if (Normalizer.isNormalized(string, Normalizer.Form.NFKD)) {
            return string;
        }
        String stringNormalized = Normalizer.normalize(string, Normalizer.Form.NFKD);
        return stringNormalized.replaceAll(UNICODE_MARK, "");
    }
}
