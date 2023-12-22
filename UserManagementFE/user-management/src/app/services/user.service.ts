import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient: HttpClient) { }

  getAllUsers() {
    return this.httpClient.get<any>("http://localhost:8080/api/users/all")
      .pipe(
        catchError(error => {
          return throwError(() => error.status + ': ' + error.error.message);
        })
      )
  }
}
