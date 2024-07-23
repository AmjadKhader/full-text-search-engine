package full.text.search.controller;

import full.text.search.dto.CreateDocumentRequestDto;
import full.text.search.dto.CreateMultipleDocumentRequestDto;
import full.text.search.model.Document;
import full.text.search.service.DocumentSearchService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/text-search-engine/api/documents")
public class DocumentController {

    private final DocumentSearchService documentSearchService;

    public DocumentController(DocumentSearchService documentSearchService) {
        this.documentSearchService = documentSearchService;
    }

    @PostMapping("/")
    public Document save(@Valid @RequestBody CreateDocumentRequestDto createDocumentRequestDto) {
        return documentSearchService.save(createDocumentRequestDto.getContent());
    }

    @PostMapping("/all/")
    public List<Document> saveAll(@Valid @RequestBody CreateMultipleDocumentRequestDto createMultipleDocumentRequestDto) {
        return documentSearchService.saveAll(createMultipleDocumentRequestDto.getContent());
    }

    @GetMapping("/{id}")
    public Document getDocument(@PathVariable UUID id) {
        return documentSearchService.get(id);
    }

    @GetMapping("/")
    public List<Document> getAllDocument() {
        return documentSearchService.getAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        documentSearchService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
