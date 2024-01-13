import { HttpClient, HttpParams } from '@angular/common/http';
import { ErrorHandler, Injectable } from '@angular/core';
import { BehaviorSubject, catchError, throwError } from 'rxjs';
import { LOGIN } from '../const/api-endpoints.const';
import { Router } from '@angular/router';
import { decodeToken } from './jwt-decoder.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient: HttpClient, private router: Router) { }

  login(loginData: any) {
    return this.httpClient.post<any>(LOGIN, loginData);
  }

  getUserByUsername(username?: string) {
    return this.httpClient.get<any>(`http://localhost:8080/api/users/${username}`);
  }

  getAllUsers() {
    return this.httpClient.get<any>("http://localhost:8080/api/users/all")
      .pipe(
        catchError(error => {
          return throwError(() => error.status + ': ' + error.error.message);
        })
      )
  }

  createUser(newUser: any) {
    return this.httpClient.post<any>('http://localhost:8080/api/users/create', newUser)
    .subscribe({
      error: (err) => { 
        console.log('err: ', err);
        alert(err);
      },
      next: (response) => {
        this.router.navigate(['/home']);
      }
    });
  }

  updateUser(updatedUser: any, id: number) {
    return this.httpClient.put<any>(`http://localhost:8080/api/users/update/${id}`, updatedUser);
  }

  deleteUser(id: number) {
    return this.httpClient.delete<any>(`http://localhost:8080/api/users/delete/${id}`);
  }
}
