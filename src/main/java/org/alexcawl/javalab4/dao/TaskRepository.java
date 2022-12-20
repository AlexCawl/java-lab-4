package org.alexcawl.javalab4.dao;


import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, String> {
    Boolean existsByURL(String URL);
    Boolean existsByID(String ID);
}
