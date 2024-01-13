import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { UsersTableComponent } from './users-table/users-table.component';
import { canActivate, canActivateCreate, canActivateRead, canActivateUpdate } from './auth.guard';
import { UserFormComponent } from './user-form/user-form.component';
import { EditUserFormComponent } from './edit-user-form/edit-user-form.component';
import { HomeComponent } from './home/home.component';
import { SearchVacuumsComponent } from './vacuums/search-vacuums/search-vacuums.component';
import { AddVacuumsComponent } from './vacuums/add-vacuums/add-vacuums.component';
import { ErrorHistoryComponent } from './vacuums/error-history/error-history.component';
import { OperationVacuumsComponent } from './vacuums/operation-vacuums/operation-vacuums.component';

const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'home', component: HomeComponent, canActivate: [canActivate] },
    { path: 'create', component: UserFormComponent, canActivate: [canActivate, canActivateCreate] },
    { path: 'edit', component: EditUserFormComponent, canActivate: [canActivate, canActivateUpdate] },
    { path: 'read', component: UsersTableComponent, canActivate: [canActivate, canActivateRead] },
    // search vacuums
    { path: 'search', component: SearchVacuumsComponent, canActivate: [canActivate] },
    { path: 'add', component: AddVacuumsComponent, canActivate: [canActivate] },
    { path: 'errors', component: ErrorHistoryComponent, canActivate: [canActivate] },
    { path: 'vacuum-details', component: OperationVacuumsComponent, canActivate: [canActivate] },

    // Add other routes as needed
    { path: '', redirectTo: '/home', pathMatch: 'full' }, // Redirect to home if no path is provided
  ];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }
