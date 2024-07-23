package full.text.search.controller;

import full.text.search.service.DocumentSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/text-search-engine/api/search")
public class SearchController {

    private final DocumentSearchService documentSearchService;

    public SearchController(DocumentSearchService documentSearchService) {
        this.documentSearchService = documentSearchService;
    }

    @GetMapping("")
    public List<String> searchDocuments(@RequestParam String q) {
        return documentSearchService.search(q.split(","));
    }
}
