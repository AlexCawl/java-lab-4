package org.alexcawl.javalab4.model.dto.parsers;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.alexcawl.javalab4.model.SearchArea;
import org.alexcawl.javalab4.model.SearchType;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ParsingRequest {
    @ApiModelProperty(notes = "Area in site structure to parse from", example = "DOMAIN")
    private SearchArea area;

    @ApiModelProperty(notes = "Type of element to get from parsing", example = "EXTERNAL_URL")
    private SearchType type;

    @ApiModelProperty(notes = "Url or domain to scan from", example = "balbes.com")
    private String key;

    @ApiModelProperty(notes = "Maximum limit of elements to get from parsing", example = "10")
    private Integer limit;
}
