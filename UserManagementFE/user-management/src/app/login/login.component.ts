import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { LOGIN } from '../const/api-endpoints.const';
import { UserService } from '../services/user.service';
import { decodeToken } from '../services/jwt-decoder.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username: string = '';
  password: string = '';

  constructor(private userService: UserService, private router: Router) {}

  onSubmit() {
    const loginData = {
      username: this.username,
      password: this.password
    };

    this.userService.login(loginData).subscribe({
      error: (err) => alert(err),
      next: (response) => {
        sessionStorage.setItem('jwt', response.jwt);
        const username = decodeToken(response.jwt)?.sub;
        this.userService.getUserByUsername(username).subscribe({
          error: (err) => alert(err), 
          next: (loggedInUser) => {
            sessionStorage.setItem('loggedInUser', JSON.stringify(loggedInUser));
            this.router.navigate(['/home']);
          }
        });
      }
    });;
  }
}
