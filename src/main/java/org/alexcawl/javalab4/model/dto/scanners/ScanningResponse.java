package org.alexcawl.javalab4.model.dto.scanners;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.alexcawl.javalab4.model.WebResource;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScanningResponse {
    @ApiModelProperty(notes = "Site pages found")
    private List<WebResource> resource;

    @ApiModelProperty(notes = "Response code", example = "200")
    private Integer code;

    @ApiModelProperty(notes = "Response message", example = "OK")
    private String message;

    public ScanningResponse(Integer code, String message) {
        this.resource = new ArrayList<>();
        this.code = code;
        this.message = message;
    }
}
