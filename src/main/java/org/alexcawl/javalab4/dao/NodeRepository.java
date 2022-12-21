package org.alexcawl.javalab4.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NodeRepository extends JpaRepository<Node, String> {
    List<Node> findAllByDomain(String domain);
    List<Node> findAllByURLStartingWith(String prefix);
}
