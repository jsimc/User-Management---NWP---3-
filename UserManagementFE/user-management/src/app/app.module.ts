import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { UsersTableComponent } from './users-table/users-table.component';
import { HTTP_INTERCEPTORS, HttpClient, HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { BearerTokenInterceptor } from './bearer-token.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    UsersTableComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule
  ], 
  providers: [
    // Register the BearerTokenInterceptor as an HTTP interceptor
    { provide: HTTP_INTERCEPTORS, useClass: BearerTokenInterceptor, multi: true },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
