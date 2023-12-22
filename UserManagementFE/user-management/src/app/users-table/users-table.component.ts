import { Component } from '@angular/core';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-users-table',
  templateUrl: './users-table.component.html',
  styleUrls: ['./users-table.component.css']
})
export class UsersTableComponent {
  users: any;
  constructor(private userService: UserService) { }

  ngOnInit() {
    this.getAllUsers();
  }

  getAllUsers() {
    console.log("Hello kliknut");
    this.userService.getAllUsers()
    .subscribe({
      error: (err) => alert(err),
      next: (res) => {
        this.users = res;
      },
      complete: () => console.log('completed')
    });
  }
}
