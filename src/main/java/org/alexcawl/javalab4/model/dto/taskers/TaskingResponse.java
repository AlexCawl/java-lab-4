package org.alexcawl.javalab4.model.dto.taskers;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskingResponse {
    @ApiModelProperty(notes = "Response code", example = "200")
    private Integer code;
    @ApiModelProperty(notes = "Response message", example = "OK")
    private String message;
}
