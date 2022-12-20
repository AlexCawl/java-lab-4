package org.alexcawl.javalab4.dao;


import com.google.common.hash.Hashing;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.nio.charset.StandardCharsets;


@Entity
@Table(name = "tasks")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Task {
    @Id
    @Column(name = "hash", unique = true)
    private String ID;

    @Column(name = "url", unique = true, length = 1024)
    private String URL;

    @Column(name = "depth_limit")
    private Integer depthLimit;

    public Task(String URL, Integer depthLimit) {
        this.ID = Hashing.sha256().hashString(URL, StandardCharsets.UTF_8).toString();
        this.URL = URL;
        this.depthLimit = depthLimit;
    }
}
