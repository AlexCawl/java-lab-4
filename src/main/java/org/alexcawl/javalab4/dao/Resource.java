package org.alexcawl.javalab4.dao;


import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "resources")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Resource {
    @Id
    @Column(name = "hash", unique = true)
    private String ID;

    @Column(name = "url", unique = true, length = 1024)
    private String URL;

    @Column(name = "domain")
    private String domain;

    @Column(name = "last_updated")
    private Date lastUpdated;

    @Column(name = "nodes_count")
    private Integer nodesCount;

    @Column(name = "nodes", columnDefinition = "text")
    private String nodesAsString;

    public Resource(String ID, String URL, String domain, Integer nodesCount, String nodesAsString) {
        this.ID = ID;
        this.URL = URL;
        this.domain = domain;
        this.lastUpdated = new Date();
        this.nodesCount = nodesCount;
        this.nodesAsString = nodesAsString;
    }

    public List<String> getNodes() {
        return Arrays.stream(nodesAsString.split(" ")).collect(Collectors.toList());
    }
}
