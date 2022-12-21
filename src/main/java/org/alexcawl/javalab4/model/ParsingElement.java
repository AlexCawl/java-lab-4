package org.alexcawl.javalab4.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ParsingElement {
    @ApiModelProperty(notes = "Element content", example = "http://www.balbes.net")
    private String content;
    @ApiModelProperty(notes = "Element from", example = "http://balbes.com")
    private String from;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParsingElement that = (ParsingElement) o;

        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return content != null ? content.hashCode() : 0;
    }
}
