package full.text.search.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import full.text.search.dto.CreateDocumentRequestDto;
import full.text.search.dto.CreateMultipleDocumentRequestDto;
import full.text.search.model.Document;
import full.text.search.service.DocumentSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class DocumentControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private DocumentSearchService documentSearchService;

    @Autowired
    private DocumentController documentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(documentController)
                .setControllerAdvice(new ErrorController())
                .build();
    }


    @Test
    public void testSaveDocument() throws Exception {
        String content = "Test document content";
        CreateDocumentRequestDto request = new CreateDocumentRequestDto(content);

        mockMvc.perform(post("/text-search-engine/api/documents/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(content))
                .andReturn();
    }

    @Test
    public void testSaveAllDocuments() throws Exception {
        List<String> contents = List.of("Content 1", "Content 2");
        CreateMultipleDocumentRequestDto request = new CreateMultipleDocumentRequestDto(contents);

        mockMvc.perform(post("/text-search-engine/api/documents/all/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(contents.get(0)))
                .andExpect(jsonPath("$[1].content").value(contents.get(1)))
                .andReturn();
    }

    @Test
    public void testGetDocument() throws Exception {
        String content = "Document to retrieve";
        Document savedDocument = documentSearchService.save(content);  // Assuming service is accessible

        mockMvc.perform(get("/text-search-engine/api/documents/" + savedDocument.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Document to retrieve"))
                .andReturn();
    }

    @Test
    public void testGetAllDocuments() throws Exception {
        List<String> contents = List.of("Doc X", "Doc Y", "Doc Z");
        documentSearchService.saveAll(contents);

        mockMvc.perform(get("/text-search-engine/api/documents/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andReturn();
    }

    @Test
    public void testDeleteDocument() throws Exception {
        String content = "Document to delete";
        Document savedDocument = documentSearchService.save(content);  // Assuming service is accessible

        mockMvc.perform(delete("/text-search-engine/api/documents/" + savedDocument.getId()))
                .andExpect(status().isNoContent());
    }

    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
