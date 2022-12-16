package org.alexcawl.javalab4.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestDTO {
    private String URL;
    private Integer depth;
}
