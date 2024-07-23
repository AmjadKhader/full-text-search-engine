package full.text.search.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class Document {
    private UUID id = UUID.randomUUID();
    private String content;

    public Document(String content) {
        this.content = content;
    }
}
