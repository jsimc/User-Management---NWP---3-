import { Component, EventEmitter, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.css']
})
export class UserFormComponent {
  userForm!: FormGroup;

  constructor(private formBuilder: FormBuilder, private userService: UserService) {
    this.initForm();
  }

  initForm() {
    this.userForm = this.formBuilder.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
      canCreateUsers: [false],
      canReadUsers: [true],
      canUpdateUsers: [false],
      canDeleteUsers: [false]
    });
  }

  onSubmit() {
    if (this.userForm.valid) {
      const newUser = this.userForm.value;
      console.log(newUser);
      this.userService.createUser(newUser);
      this.userForm.reset();
    }
  }
}
