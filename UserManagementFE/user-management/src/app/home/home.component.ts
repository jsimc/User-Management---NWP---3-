import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  loggedUser: any;

  constructor(private router: Router) {}

  ngOnInit() {
    console.log('LOGGED IN: ', sessionStorage.getItem('loggedInUser'));
    
    this.loggedUser = JSON.parse(sessionStorage.getItem('loggedInUser') || '');
  }

  logout() {
    sessionStorage.clear();
    this.router.navigate(['/login']);
  }
}
