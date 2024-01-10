import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class VacuumService {

  constructor(private httpClient: HttpClient, private router: Router) { }


  search(name: string | null, status: string | null, dateFrom: any, dateTo: any) {
    const url = `http://localhost:8080/api/vacuum/search`;

    const params: {[key:string]: any} = {
      name,
      status,
      dateFrom,
      dateTo
    };

    Object.keys(params).forEach(key => params[key] === null && delete params[key]);

    console.log('params: ', params);
    
    
    return this.httpClient.get<any>(url, {params: params});
  }

  add(name: string) {
    const url = `http://localhost:8080/api/vacuum/add`;
    const params = new HttpParams().set('name', name);
    return this.httpClient.post<any>(url, {}, {params: params});
  }
}
