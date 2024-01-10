import { Component } from '@angular/core';
import { VacuumService } from 'src/app/services/vacuum.service';

@Component({
  selector: 'app-add-vacuums',
  templateUrl: './add-vacuums.component.html',
  styleUrls: ['./add-vacuums.component.css']
})
export class AddVacuumsComponent {
  vacuumName!: string;
  response: any;
  constructor(private vacuumService: VacuumService) {}

  add() {
    this.vacuumService.add(this.vacuumName).subscribe({
      error: (err) => { 
        console.log('err: ', err);
        alert(err);
      },
      next: (response) => {
        console.log(response);
        this.response = response;
      }
    });
  }
}
