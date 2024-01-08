import { Component } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  canCreateUsers: boolean | undefined;
  ngOnInit() {
    this.canCreateUsers = JSON.parse(sessionStorage.getItem('loggedInUser') || '').canCreateUsers;

  }
}
