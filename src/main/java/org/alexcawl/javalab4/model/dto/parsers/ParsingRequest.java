package org.alexcawl.javalab4.model.dto.parsers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.alexcawl.javalab4.model.SearchArea;
import org.alexcawl.javalab4.model.SearchType;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ParsingRequest {
    private SearchArea area;
    private SearchType type;
    private String key;
    private Integer limit;
}
