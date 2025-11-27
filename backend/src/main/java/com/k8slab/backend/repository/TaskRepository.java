package com.k8slab.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.k8slab.backend.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{

}
