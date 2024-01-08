import { Component } from '@angular/core';
import { UserService } from './services/user.service';
import { tokenExpired } from './services/jwt-decoder.service';
import { NavigationStart, Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'user-management';
  showHead: boolean = false;

  constructor(private userService: UserService, private router: Router) { }
  ngOnInit() {
    const token = sessionStorage.getItem('jwt');
    this.router.events.forEach((event) => {
      if (event instanceof NavigationStart) {
        if (event['url'] == '/login') {
          this.showHead = false;
        } else {
          // console.log("NU")
          this.showHead = true;
        }
      }
    });
    if (tokenExpired(token || '')) {
      this.router.navigate(['/login'])
    }
  }
}
