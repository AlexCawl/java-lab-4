package org.alexcawl.javalab4.model.dto.scanners;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.alexcawl.javalab4.model.SearchArea;

@ApiModel
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScanningRequest {
    private SearchArea area;
    private String key;
}
