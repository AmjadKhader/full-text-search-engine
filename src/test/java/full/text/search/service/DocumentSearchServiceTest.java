package full.text.search.service;

import full.text.search.exception.DocumentNotFoundException;
import full.text.search.model.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DocumentSearchServiceTest {

    @Autowired
    private DocumentSearchService documentSearchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        documentSearchService.removeAllDocuments();
    }

    @Test
    public void testSaveDocument_Success() {
        String content = "This is a test document.";
        Document savedDocument = documentSearchService.save(content);

        assertNotNull(savedDocument.getId());
        assertEquals(content, savedDocument.getContent());
    }

    @Test
    public void testSaveDocument_Failed() {
        String content = ".";
        Document savedDocument = documentSearchService.save(content);

        assertNull(savedDocument);
    }

    @Test
    public void testSaveMultipleDocuments() {
        List<String> contents = List.of("Doc 1 content", "Doc 2 content");
        List<Document> savedDocuments = documentSearchService.saveAll(contents);

        assertEquals(contents.size(), savedDocuments.size());
        Set<UUID> ids = new HashSet<>();
        for (Document doc : savedDocuments) {
            assertNotNull(doc.getId());
            ids.add(doc.getId());
        }
        assertEquals(ids.size(), contents.size());
    }

    @Test
    public void testSaveMultipleDocuments_OneFailed() {
        List<String> contents = List.of("Doc 1 content", "Doc 2 content", "");
        List<Document> savedDocuments = documentSearchService.saveAll(contents);

        assertEquals(contents.size() - 1, savedDocuments.size());
    }

    @Test
    public void testGetNonexistentDocument() {
        assertThrows(DocumentNotFoundException.class, () -> documentSearchService.get(UUID.randomUUID()));
    }

    @Test
    public void testGetDocument() {
        String content = "Another test document.";
        Document savedDocument = documentSearchService.save(content);
        Document retrievedDocument = documentSearchService.get(savedDocument.getId());

        assertEquals(savedDocument, retrievedDocument);
    }

    @Test
    public void testGetAllDocuments() {
        List<String> contents = List.of("Doc A", "Doc B", "Doc C");
        documentSearchService.saveAll(contents);

        List<Document> allDocuments = documentSearchService.getAll();
        assertEquals(contents.size(), allDocuments.size());
    }

    @Test
    public void testDeleteNonexistentDocument() {
        assertThrows(DocumentNotFoundException.class, () -> documentSearchService.delete(UUID.randomUUID()));
    }

    @Test
    public void testDeleteDocument() {
        String content = "Document to be deleted.";
        Document savedDocument = documentSearchService.save(content);

        documentSearchService.delete(savedDocument.getId());

        assertThrows(DocumentNotFoundException.class, () -> documentSearchService.get(savedDocument.getId()));
    }

    @Test
    public void testSearchDocuments() {
        String doc1Content = "This is a document about cats.";
        String doc2Content = "This is another document about dogs.";

        documentSearchService.save(doc1Content);
        documentSearchService.save(doc2Content);

        List<String> searchResults = documentSearchService.search(new String[]{"cats"});
        assertEquals(1, searchResults.size());
        assertTrue(searchResults.contains(doc1Content));

        searchResults = documentSearchService.search(new String[]{"dogs", "cats"});
        assertEquals(0, searchResults.size());
    }
}
