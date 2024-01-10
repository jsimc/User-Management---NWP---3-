import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class ErrorService {

  constructor(private httpClient: HttpClient, private router: Router) { }

  getAll() {
    const url = `http://localhost:8080/api/errors/all`;
    return this.httpClient.get<any>(url);
  }
}
