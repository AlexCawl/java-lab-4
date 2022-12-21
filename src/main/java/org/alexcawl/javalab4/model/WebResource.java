package org.alexcawl.javalab4.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.alexcawl.javalab4.dao.NodeStatus;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WebResource {
    @ApiModelProperty(notes = "Web resource url", example = "http://balbes.com/")
    private String url;

    @ApiModelProperty(notes = "Resource status during crawling", example = "OK")
    private NodeStatus status;

    @ApiModelProperty(notes = "Nesting in the site structure", example = "1")
    private Integer nesting;
}
