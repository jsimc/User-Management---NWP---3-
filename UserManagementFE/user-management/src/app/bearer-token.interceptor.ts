// bearer-token.interceptor.ts
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class BearerTokenInterceptor implements HttpInterceptor {
    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        // Retrieve the token from wherever you store it (e.g., localStorage)
        // const token = localStorage.getItem('access_token');
        // jelena 
        const token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqZWxlbmEiLCJleHAiOjE3MDMyOTYyNjcsImlhdCI6MTcwMzI2MDI2N30.vhP_7TwCrYGVPuBgcLiiuWpRQ4weYG0OnZmjcYPY5n9AeyPYjRw4JQkTe-0qkqApstVmw5jaUxYCznzq8dDA1w";
        // user1
        // const token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMSIsImV4cCI6MTcwMzMxMDczMCwiaWF0IjoxNzAzMjc0NzMwfQ.uAd0VffqjhnJaf8ke-3hhmFHT23PG6pYz_1Vb2Y7rdIj6umVYnaB0tySsR6fJBIzcQPvxrO8MKuohG1PgvAOmQ";
        // Clone the request and add the Bearer token to the Authorization header
        if (token) {
            const clonedRequest = request.clone({
                setHeaders: {
                Authorization: `Bearer ${token}`,
                },
            });
            return next.handle(clonedRequest);
        }

        // If there is no token, proceed with the original request
        return next.handle(request);
    }
}