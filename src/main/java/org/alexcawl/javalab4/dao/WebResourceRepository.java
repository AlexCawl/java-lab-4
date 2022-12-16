package org.alexcawl.javalab4.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WebResourceRepository extends JpaRepository<WebResource, String> {
    Optional<WebResource> findByURL(String URL);
    Boolean existsByURL(String URL);
}
