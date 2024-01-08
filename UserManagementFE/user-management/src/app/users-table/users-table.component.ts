import { Component, EventEmitter, Output } from '@angular/core';
import { UserService } from '../services/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-users-table',
  templateUrl: './users-table.component.html',
  styleUrls: ['./users-table.component.css']
})
export class UsersTableComponent {
  @Output() deleteUserEmitter = new EventEmitter<any>();
  users: any;
  canDeleteUsers: boolean = JSON.parse(sessionStorage.getItem('loggedInUser') || '').canDeleteUsers;
  canUpdateUsers: boolean = JSON.parse(sessionStorage.getItem('loggedInUser') || '').canUpdateUsers;
  constructor(private userService: UserService, private router: Router) { }

  ngOnInit() {
    this.getAllUsers();
  }

  getAllUsers() {
    this.userService.getAllUsers()
    .subscribe({
      error: (err) => alert(err),
      next: (res) => {
        this.users = res;
      },
      complete: () => console.log('completed')
    });
  }

  updateUser(selectedUser: any) {
    console.log('Selected user: ', selectedUser);
    sessionStorage.setItem('selectedUser', JSON.stringify(selectedUser));
    this.router.navigate(['/edit']);
  }
  deleteUser(userForDelete: any) {
    this.userService.deleteUser(userForDelete.userId)
    .subscribe({
      error: (err) => { 
        console.log('err: ', err);
        alert(err);
      },
      next: (response) => {
        this.router.navigate(['/home']);
        this.ngOnInit();
      }
    })
  }
}
