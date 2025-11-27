import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { Task } from './models/task.model';
import { TaskService } from './services/task.service';

@Component({
	selector: 'app-root',
	imports: [CommonModule, FormsModule, HttpClientModule],
	templateUrl: './app.html',
	styleUrl: './app.scss'
})
export class App implements OnInit {
	tasks: Task[] = [];
	newTask: Task = { title: '', description: '', completed: false };

	constructor(private taskService: TaskService) { }

	ngOnInit(): void {
		this.loadTasks();
	}

	loadTasks() {
		this.taskService.getTasks().subscribe(tasks => {
			this.tasks = tasks;
		});
	}

	createTask() {
		if (this.newTask.title.trim()) {
			this.taskService.createTask(this.newTask).subscribe(() => {
				this.newTask = { title: '', description: '', completed: false };
				this.loadTasks();
			});
		}
	}

	toggleTask(task: Task) {
		const updatedTask = { ...task, completed: !task.completed };
		this.taskService.updateTask(task.id!, updatedTask).subscribe(() => {
			this.loadTasks();
		});
	}

	deleteTask(id: number) {
		this.taskService.deleteTask(id).subscribe(() => {
			this.loadTasks();
		});
	}
}
