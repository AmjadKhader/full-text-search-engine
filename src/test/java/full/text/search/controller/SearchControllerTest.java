package full.text.search.controller;

import full.text.search.service.DocumentSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class SearchControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private DocumentSearchService documentSearchService;

    @Autowired
    private SearchController searchController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(searchController)
                .setControllerAdvice(new ErrorController())
                .build();
    }

    @Test
    public void testSearchDocuments() throws Exception {
        String doc1Content = "This is a document about cats.";
        String doc2Content = "This is another document about dogs.";

        documentSearchService.save(doc1Content);
        documentSearchService.save(doc2Content);

        mockMvc.perform(get("/text-search-engine/api/search?q=cats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(doc1Content))
                .andReturn();

        mockMvc.perform(get("/text-search-engine/api/search?q=dogs,cats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty())
                .andReturn();
    }
}
