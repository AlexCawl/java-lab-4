package org.alexcawl.javalab4.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "graph_map")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Node {
    @Id
    @Column(name = "hash", unique = true)
    private String ID;

    @Column(name = "url", unique = true, length = 1024)
    private String URL;

    @Column(name = "domain")
    private String domain;

    @Column(name = "node_last_height")
    private Integer height;

    @Column(name = "nesting")
    private Integer nesting;

    @Column(name = "last_update")
    private Date lastUpdate;

    @Column(name = "node_status")
    private NodeStatus nodeStatus;

    public Node(String ID, String URL, String domain, Integer height, NodeStatus nodeStatus, Integer nesting) {
        this.ID = ID;
        this.URL = URL;
        this.domain = domain;
        this.height = height;
        this.lastUpdate = new Date();
        this.nodeStatus = nodeStatus;
        this.nesting = nesting;
    }

    public Node updateDate() {
        this.lastUpdate = new Date();
        return this;
    }
}
