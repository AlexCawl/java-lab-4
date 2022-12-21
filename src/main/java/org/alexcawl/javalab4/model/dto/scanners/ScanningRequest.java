package org.alexcawl.javalab4.model.dto.scanners;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.alexcawl.javalab4.model.SearchArea;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScanningRequest {
    @ApiModelProperty(notes = "Area in site structure to scan from", example = "DOMAIN")
    private SearchArea area;

    @ApiModelProperty(notes = "Url or domain to scan from", example = "balbes.com")
    private String key;
}
