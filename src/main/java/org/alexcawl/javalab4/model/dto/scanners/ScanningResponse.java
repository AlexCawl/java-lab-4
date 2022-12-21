package org.alexcawl.javalab4.model.dto.scanners;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.alexcawl.javalab4.model.WebResource;

import java.util.ArrayList;
import java.util.List;

@ApiModel
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScanningResponse {
    private List<WebResource> resource;
    private Integer code;
    private String message;

    public ScanningResponse(Integer code, String message) {
        this.resource = new ArrayList<>();
        this.code = code;
        this.message = message;
    }
}
