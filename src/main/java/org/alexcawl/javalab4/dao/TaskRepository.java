package org.alexcawl.javalab4.dao;


import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Boolean existsByURL(String URL);
}
