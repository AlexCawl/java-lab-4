package org.alexcawl.javalab4.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContentRepository extends JpaRepository<Content, String> {
    Optional<Content> findByID(String ID);
}
