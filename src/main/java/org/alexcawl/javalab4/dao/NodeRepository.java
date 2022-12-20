package org.alexcawl.javalab4.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NodeRepository extends JpaRepository<Node, String> {
    Optional<Node> findByID(String ID);
}
