package shelter.backend.utils.converter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CharsNormalizerTest {

    @Test
    void convertPolishAlphabetToEng() {
        String result = CharsNormalizer.convertToEngChars("ęóąśłżźćńĘÓĄŚŁŻŹĆŃ");
        assertEquals(result, "eoaslzzcnEOASLZZCN");
    }
}

