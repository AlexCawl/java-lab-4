package org.alexcawl.javalab4.model.dto.parsers;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.alexcawl.javalab4.model.ParsingElement;
import org.alexcawl.javalab4.model.WebResource;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ParsingResponse {
    private List<ParsingElement> resource;

    @ApiModelProperty(notes = "Response code", example = "200")
    private Integer code;

    @ApiModelProperty(notes = "Response message", example = "OK")
    private String message;

    public ParsingResponse(Integer code, String message) {
        this.resource = new ArrayList<>();
        this.code = code;
        this.message = message;
    }
}
