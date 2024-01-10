import { Component } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  loggedUser: any;
  canCreateUsers: boolean | undefined;
  canSearchVacuum: boolean | undefined;
  canAddVacuum: boolean | undefined;
  ngOnInit() {
    this.loggedUser = sessionStorage.getItem('loggedInUser');
    this.canCreateUsers = JSON.parse(this.loggedUser).canCreateUsers;
    this.canSearchVacuum = JSON.parse(this.loggedUser).canSearchVacuum;
    this.canAddVacuum = JSON.parse(this.loggedUser).canAddVacuum;
  }
}
