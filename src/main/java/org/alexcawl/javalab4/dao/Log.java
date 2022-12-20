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
@Table(name = "logs")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Log {
    @Id
    @Column(name = "hash", unique = true)
    private String ID;

    @Column(name = "url", unique = true, length = 1024)
    private String URL;

    @Column(name = "depth_limit")
    private Integer depthLimit;

    @Column(name = "last_update")
    private Date lastUpdate;

    public Log(String ID, String URL, Integer depthLimit) {
        this.ID = ID;
        this.URL = URL;
        this.depthLimit = depthLimit;
        this.lastUpdate = new Date();
    }
}
