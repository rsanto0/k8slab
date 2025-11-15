package com.k8slab.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.k8slab.backend.model.Task;
import com.k8slab.backend.service.TaskService;

/**
 * Controlador REST para gerenciamento de tarefas.
 * Fornece endpoints para operações CRUD em tarefas.
 */
@RestController
@RequestMapping("/api")
public class TaskController {

	@Autowired
	private TaskService taskService;

	/**
	 * Retorna todas as tarefas cadastradas.
	 * 
	 * @return Lista de todas as tarefas
	 */
	@GetMapping("/tasks")
	public List<Task>getAllTasks(){
		return taskService.getAllTasks();
	}
	
	/**
	 * Busca uma tarefa específica pelo ID.
	 * 
	 * @param id ID da tarefa a ser buscada
	 * @return ResponseEntity com a tarefa encontrada ou 404 se não encontrada
	 */
	@GetMapping("/tasks/{id}")
	public ResponseEntity<Task> getTaskId(@PathVariable long id) {
		Task task = taskService.getTaskById(id);
		return task!=null ? ResponseEntity.ok(task) :  ResponseEntity.notFound().build();
	}
	
	/**
	 * Cria uma nova tarefa.
	 * 
	 * @param task Dados da tarefa a ser criada
	 * @return Tarefa criada com ID gerado
	 */
	@PostMapping("/tasks")
	public Task createTask(@RequestBody Task task) {
		return taskService.createTask(task);
	}
	
	/**
	 * Atualiza uma tarefa existente.
	 * 
	 * @param id ID da tarefa a ser atualizada
	 * @param task Novos dados da tarefa
	 * @return ResponseEntity com a tarefa atualizada ou 404 se não encontrada
	 */
	@PutMapping("/tasks/{id}")
	public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
		Task updatedTask = taskService.update(id, task);
		return updatedTask != null ? ResponseEntity.ok(updatedTask) : ResponseEntity.notFound().build();
	}
	
	/**
	 * Remove uma tarefa pelo ID.
	 * 
	 * @param id ID da tarefa a ser removida
	 * @return ResponseEntity com status 200 se removida ou 404 se não encontrada
	 */
	@DeleteMapping("/tasks/{id}")
	public ResponseEntity<Void>deleteTask(@PathVariable Long id){
		return taskService.deleteTask(id)?ResponseEntity.ok().build() : ResponseEntity.notFound().build();
	}
	
	/**
	 * Endpoint de teste para verificar se a API está funcionando.
	 * 
	 * @return Mensagem de confirmação
	 */
	@GetMapping("/")
	public String ola() {
		return "K8sLab To-Do API funcionando!";
	}
}