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
public class TaskingRequest {
    @ApiModelProperty(notes = "Starting url", example = "http://balbes.com/")
    private String url;
    @ApiModelProperty(notes = "Limited depth", example = "3")
    private Integer depth;
}
