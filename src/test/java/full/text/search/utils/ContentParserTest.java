package full.text.search.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

class ContentParserTest {
    @Mock
    List<String> symbols;
    @Mock
    List<String> smallTalk;
    @InjectMocks
    ContentParser contentParser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testParse_RemoveSymbols_Success() {
        String[] result = ContentParser.parse("new content .");
        Assertions.assertArrayEquals(new String[]{"new", "content"}, result);
    }

    @Test
    void testParse_RemoveSymbols_ReturnEmpty() {
        String[] result = ContentParser.parse("!#%.");
        Assertions.assertArrayEquals(new String[]{}, result);
    }

    @Test
    void testParse_RemoveSmallTalk_Success() {
        String[] result = ContentParser.parse("new content the");
        Assertions.assertArrayEquals(new String[]{"new", "content"}, result);
    }

    @Test
    void testParse_RemoveSmallTalk_ReturnEmpty() {
        String[] result = ContentParser.parse("the");
        Assertions.assertArrayEquals(new String[]{}, result);
    }
}