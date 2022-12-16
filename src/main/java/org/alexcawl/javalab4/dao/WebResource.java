package org.alexcawl.javalab4.dao;


import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "resources")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WebResource {
    @Id
    @Column(name = "id", unique = true)
    private String ID;

    @Column(name = "url", unique = true, length = 500)
    private String URL;

    @Column(name = "domain")
    private String domain;

    @Column(name = "path", unique = true)
    private String path;

    @Column(name = "last_updated")
    private Date lastUpdated;
}
