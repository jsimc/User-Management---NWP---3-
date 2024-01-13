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

  start(id: number) {
    return this.httpClient.post<any>(`http://localhost:8080/api/vacuum/start/${id}`, {});
  }

  stop(id: number) {
    return this.httpClient.post<any>(`http://localhost:8080/api/vacuum/stop/${id}`, {});
  }

  discharge(id: number) {
    return this.httpClient.post<any>(`http://localhost:8080/api/vacuum/discharge/${id}`, {});
  }

  removeVacuum(id: number) {
    return this.httpClient.put<any>(`http://localhost:8080/api/vacuum/remove/${id}`, {});
  }

  schedule(id: number, operation: string, dateTime: string) {
    const url = `http://localhost:8080/api/vacuum/schedule?operation=stop&dateTime=2024-01-09T15:13:00&vacuumId=1`;
    const params = new HttpParams()
    .set('vacuumId', id)
    .set('dateTime', dateTime)
    .set('operation', operation);
    return this.httpClient.post<any>(`http://localhost:8080/api/vacuum/schedule`, {}, {params: params});
  }

  findById(id: number) {
    return this.httpClient.get<any>(`http://localhost:8080/api/vacuum/${id}`); 
  }
}
