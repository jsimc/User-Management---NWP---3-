// bearer-token.interceptor.ts
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class BearerTokenInterceptor implements HttpInterceptor {
    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const token = sessionStorage.getItem('jwt');
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