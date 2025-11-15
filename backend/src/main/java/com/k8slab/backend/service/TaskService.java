package com.k8slab.backend.service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;
import com.k8slab.backend.model.Task;

/**
 * Serviço responsável pela logica de negocios das tarefas
 */
@Service
public class TaskService {

	private final Map<Long, Task> tasks = new HashMap<>();
	private final AtomicLong counter = new AtomicLong();

	public TaskService() {

		createTask(new Task(null, "Aprender Kubernetes", "Estudar Pods, Services e Deployments", false));
		createTask(new Task(null, "Criar Api REST", "Implementar CRUD de tarefas", true));
	}

	public Task createTask(Task task) {
		Long id = counter.incrementAndGet();
		task.setId(id);
		tasks.put(id, task);
		return task;
	}

	public Task update(Long id, Task task) {
		if (tasks.containsKey(id)) {
			task.setId(id);
			tasks.put(id, task);
			return task;
		}
		return null;
	}

	public boolean deleteTask(Long id) {
		return tasks.remove(id) != null;
	}

	public List<Task> getAllTasks() {
		return new ArrayList<>(tasks.values());
	}

	public Task getTaskById(Long id) {
		return tasks.get(id);
	}
}
