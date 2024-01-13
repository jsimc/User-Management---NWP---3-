import { Component } from '@angular/core';
import { VacuumService } from 'src/app/services/vacuum.service';

@Component({
  selector: 'app-add-vacuums',
  templateUrl: './add-vacuums.component.html',
  styleUrls: ['./add-vacuums.component.css']
})
export class AddVacuumsComponent {
  vacuumName!: string;
  constructor(private vacuumService: VacuumService) {}

  add() {
    this.vacuumService.add(this.vacuumName).subscribe({
      error: (err) => { 
        console.log('err: ', err.message);
        alert(err);
      },
      next: (addedVacuum) => {
        alert(`Added vacuum: ${addedVacuum.name}`);
      }
    });
  }
}
