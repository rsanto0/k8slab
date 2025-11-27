import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Task } from "../models/task.model";

@Injectable({
	providedIn: 'root'
})

export class TaskService {

	private apiUrl = '/api/tasks';
	constructor(private http: HttpClient) { }


	createTask(newTask: Task): Observable<Task> {
		return this.http.post<Task>(this.apiUrl, newTask);
	}
	updateTask(id: number, updatedTask: Task): Observable<Task> {
		return this.http.put<Task>(`${this.apiUrl}/${id}`, updatedTask);
	}
	deleteTask(id: number): Observable<void> {
		return this.http.delete<void>(`${this.apiUrl}/${id}`);
	}
	getTasks(): Observable<Task[]> {
		return this.http.get<Task[]>(this.apiUrl);
	}

}