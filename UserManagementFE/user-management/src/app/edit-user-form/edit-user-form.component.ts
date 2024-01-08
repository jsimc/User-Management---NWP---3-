import { Component, Input } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../services/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-edit-user-form',
  templateUrl: './edit-user-form.component.html',
  styleUrls: ['./edit-user-form.component.css']
})
export class EditUserFormComponent {
  selectedUser: any; // ima userId
  userForm!: FormGroup;
  constructor(private formBuilder: FormBuilder, private userService: UserService, private router: Router) {
    this.selectedUser = JSON.parse(sessionStorage.getItem('selectedUser') || '');
    this.initForm();
  }
  onSubmit() {
    if (this.userForm.valid) {
      const updatedUser = this.userForm.value;
      console.log(updatedUser);
      
      this.userService.updateUser(updatedUser, this.selectedUser.userId)
      .subscribe({
        error: (err) => { 
          console.log('err: ', err);
          alert(err);
        },
        next: (response) => {
          if(this.selectedUser.userId === JSON.parse(sessionStorage.getItem('loggedInUser') || '').userId) {
            sessionStorage.setItem('loggedInUser', JSON.stringify(response));
          }
          sessionStorage.removeItem('selectedUser');
          this.router.navigate(['/home']);
        }
      });
      this.userForm.reset();
    }
  }
  initForm() {
    this.userForm = this.formBuilder.group({
      username: [this.selectedUser.username, Validators.required],
      email: [this.selectedUser.email, [Validators.required, Validators.email]],
      password: [this.selectedUser.password, [Validators.required]],
      canCreateUsers: [this.selectedUser.canCreateUsers],
      canReadUsers: [this.selectedUser.canReadUsers],
      canUpdateUsers: [this.selectedUser.canUpdateUsers],
      canDeleteUsers: [this.selectedUser.canDeleteUsers]
    });
  
  }
}
