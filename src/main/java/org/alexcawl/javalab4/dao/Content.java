package org.alexcawl.javalab4.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "data")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Content {
    @Id
    @Column(name = "hash", unique = true)
    private String ID;

    @Column(name = "content", columnDefinition = "text")
    private String content;
}
