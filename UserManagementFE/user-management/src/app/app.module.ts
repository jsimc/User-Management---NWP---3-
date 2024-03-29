import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { UsersTableComponent } from './users-table/users-table.component';
import { HTTP_INTERCEPTORS, HttpClient, HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { BearerTokenInterceptor } from './bearer-token.interceptor';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HeaderComponent } from './header/header.component';
import { UserFormComponent } from './user-form/user-form.component';
import { EditUserFormComponent } from './edit-user-form/edit-user-form.component';
import { HomeComponent } from './home/home.component';
import { SearchVacuumsComponent } from './vacuums/search-vacuums/search-vacuums.component';
import { AddVacuumsComponent } from './vacuums/add-vacuums/add-vacuums.component';
import { ErrorHistoryComponent } from './vacuums/error-history/error-history.component';
import { OperationVacuumsComponent } from './vacuums/operation-vacuums/operation-vacuums.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    UsersTableComponent,
    HeaderComponent,
    UserFormComponent,
    EditUserFormComponent,
    HomeComponent,
    SearchVacuumsComponent,
    AddVacuumsComponent,
    ErrorHistoryComponent,
    OperationVacuumsComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule
  ], 
  providers: [
    // Register the BearerTokenInterceptor as an HTTP interceptor
    { provide: HTTP_INTERCEPTORS, useClass: BearerTokenInterceptor, multi: true },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
