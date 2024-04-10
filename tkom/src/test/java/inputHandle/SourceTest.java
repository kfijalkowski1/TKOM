package inputHandle;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;


class SourceTest {
//    @ParameterizedTest
//    @MethodSource("singularIntegerSource")
    @Test
    @DisplayName("Test singular integer values")
    void testStringSource() {
        Source src = new StringSource("A\nbb\tc");
        Assertions.assertEquals('A', src.getCurrentChar());
        Assertions.assertEquals('\n', src.getNextChar());
    }

    @Test
    @DisplayName("Test singular integer values")
    void testFileSource() throws IOException {
        Source src = new FileSource("src/test/java/inputHandle/test-file.sp");
        Assertions.assertEquals('\"', src.getCurrentChar());
        Assertions.assertEquals('A', src.getNextChar());
        Assertions.assertEquals('\\', src.getNextChar());
    }

}