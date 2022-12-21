package org.alexcawl.javalab4.model;

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
    private String url;
    private NodeStatus status;
    private Integer nesting;
}
