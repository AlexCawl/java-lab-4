package org.alexcawl.javalab4.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApplicationConfigurationDTO {
    @ApiModelProperty(notes = "Application name", example = "java-lab-4")
    private String applicationName;

    @ApiModelProperty(notes = "Application port", example = "8000")
    private Integer applicationPort;

    @ApiModelProperty(notes = "Data in DB expiration in hours", example = "2")
    private Integer dataExpiration;

    @ApiModelProperty(notes = "Max depth in crawling", example = "10")
    private Integer maxDepth;

    @ApiModelProperty(notes = "PostgreSQL connect url", example = "jdbc:postgresql://localhost:5432/resources")
    private String datasourceUrl;
}
