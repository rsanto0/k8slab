package com.k8slab.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.k8slab.backend.model.Task;
import com.k8slab.backend.repository.TaskRepository;

/**
 * Serviço responsável pela lógica de negócios das tarefas
 */
@Service
public class TaskService {

	@Autowired
	private TaskRepository taskRepository;

	public List<Task> getAllTasks() {
		return taskRepository.findAll();
	}

	public Task getTaskById(Long id) {
		return taskRepository.findById(id).orElse(null);
	}

	public Task createTask(Task task) {
		return taskRepository.save(task);
	}

	public Task update(Long id, Task task) {
		if (taskRepository.existsById(id)) {
			task.setId(id);
			return taskRepository.save(task);
		}
		return null;
	}

	public boolean deleteTask(Long id) {
		if (taskRepository.existsById(id)) {
			taskRepository.deleteById(id);
			return true;
		}
		return false;
	}
}
