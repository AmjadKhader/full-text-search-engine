package full.text.search.service;

import full.text.search.exception.DocumentNotFoundException;
import full.text.search.model.Document;
import full.text.search.utils.ContentParser;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Service
public class DocumentSearchService {

    private final ConcurrentMap<UUID, Document> allDocuments = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, LinkedList<String>> index = new ConcurrentHashMap<>();
    private final ConcurrentMap<UUID, List<String>> documentsWords = new ConcurrentHashMap<>();

    public List<Document> getAll() {
        return allDocuments.values().stream().toList();
    }

    public Document get(UUID id) {
        Document document = allDocuments.get(id);
        if (Objects.isNull(document)) {
            throw new DocumentNotFoundException("Document with Id " + id.toString() + " was not found");
        }

        return document;
    }

    public List<Document> saveAll(List<String> contents) {
        List<Document> documents = new ArrayList<>();
        for (String content : contents) {
            documents.add(save(content));
        }
        return documents;
    }

    public Document save(String documentContent) {
        String[] words = ContentParser.parse(documentContent);

        for (String word : words) {
            if (word.isEmpty()) continue;

            LinkedList<String> docs = index.getOrDefault(word, new LinkedList<>());
            docs.add(documentContent);

            index.put(word, docs.stream()
                    .distinct() // Remove duplicates if needed
                    .collect(Collectors.toCollection(LinkedList::new)));
        }

        Document document = new Document(documentContent);
        allDocuments.put(document.getId(), document);
        documentsWords.put(document.getId(), List.of(words));
        return document;
    }

    public void delete(UUID id) {
        if (Objects.isNull(allDocuments.getOrDefault(id, null)) ||
                documentsWords.getOrDefault(id, new ArrayList<>()).isEmpty()) {
            throw new DocumentNotFoundException("Document with Id " + id.toString() + " was not found");
        }

        // Delete it from the index
        List<String> wordsInDocument = documentsWords.get(id);
        String content = allDocuments.get(id).getContent();

        for (String word : wordsInDocument) {
            LinkedList<String> indexedDocs = index.getOrDefault(word, new LinkedList<>());

            indexedDocs.removeIf(indexedContent -> indexedContent.equalsIgnoreCase(content));

            index.put(word, indexedDocs);
        }

        // Delete it from other maps
        documentsWords.remove(id);
        allDocuments.remove(id);
    }

    public List<String> search(String[] words) {
        Set<String> documents = new HashSet<>(index.getOrDefault(words[0].trim().toLowerCase(), new LinkedList<>()));

        for (int i = 1; i < words.length; i++) {
            String word = words[i].trim().toLowerCase();
            if (word.isEmpty()) continue;

            Set<String> wordDocuments = new HashSet<>(index.getOrDefault(word, new LinkedList<>()));

            // Filter documents based on present in wordDocuments
            documents.removeIf(doc -> !wordDocuments.contains(doc));
        }

        return new ArrayList<>(documents); // Convert back to List for a final result
    }
}