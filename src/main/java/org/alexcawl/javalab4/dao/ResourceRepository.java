package org.alexcawl.javalab4.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, String> {
    List<Resource> findAllByDomain(String domain);
    List<Resource> findAllByURLStartingWith(String prefix);
}
