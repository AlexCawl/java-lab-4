package org.alexcawl.javalab4.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, String> {
    Optional<Resource> findByURL(String URL);
    Boolean existsByURL(String URL);
}
