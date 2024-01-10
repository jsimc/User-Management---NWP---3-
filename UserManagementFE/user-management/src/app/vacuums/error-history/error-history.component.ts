import { Component } from '@angular/core';
import { ErrorService } from 'src/app/services/error.service';

@Component({
  selector: 'app-error-history',
  templateUrl: './error-history.component.html',
  styleUrls: ['./error-history.component.css']
})
export class ErrorHistoryComponent {
  errors: any;
  constructor(private errorService: ErrorService){}

  ngOnInit() {
    this.errorService.getAll().subscribe({
      error: (err) => { 
        console.log('err: ', err);
        alert(err);
      },
      next: (response) => {
        console.log(response);
        this.errors = response;
      }
    });
  }
}
