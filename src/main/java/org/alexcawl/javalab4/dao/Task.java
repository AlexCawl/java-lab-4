package org.alexcawl.javalab4.dao;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;


@Entity
@Table(name = "tasks")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Task {
    @Id
    @Column(name = "id")
    private UUID ID;

    @Column(name = "url", unique = true, length = 500)
    private String URL;

    @Column(name = "depth_limit")
    private Integer depthLimit;

    public Task(String URL, Integer depthLimit) {
        this.ID = UUID.randomUUID();
        this.URL = URL;
        this.depthLimit = depthLimit;
    }
}
