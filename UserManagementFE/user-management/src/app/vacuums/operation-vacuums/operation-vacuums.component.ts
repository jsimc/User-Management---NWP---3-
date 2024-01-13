import { Component } from '@angular/core';
import { VacuumService } from 'src/app/services/vacuum.service';

@Component({
  selector: 'app-operation-vacuums',
  templateUrl: './operation-vacuums.component.html',
  styleUrls: ['./operation-vacuums.component.css']
})
export class OperationVacuumsComponent {
  selectedVacuum: any;
  // loading: boolean = false;
  canStartVacuum!: boolean;
  canStopVacuum!: boolean;
  canDischargeVacuum!: boolean;

  selectedOperation: string = 'start';
  selectedDateTime: string = '';

  ngOnInit() {
    const loggedUser = JSON.parse(sessionStorage.getItem('loggedInUser') || '');
    this.vacuumService.findById(Number(sessionStorage.getItem('selectedVacuumId')))
    .subscribe({
      error: (err) => {
        alert(`${err.error.message}`);
      },
      next: (response) => {
        this.selectedVacuum = response;
      }
    });
    this.canStartVacuum = loggedUser.canStartVacuum;
    this.canStopVacuum = loggedUser.canStopVacuum;
    this.canDischargeVacuum = loggedUser.canDischargeVacuum;
  }

  constructor(private vacuumService: VacuumService) {}

  start() {
    this.vacuumService.start(this.selectedVacuum.vacuumId)
    .subscribe({
      error: (err) => {
        alert(`${err.error.message}
        timestamp: ${err.error.timestamp}`);
      },
      next: (response) => {
        console.log(response);
        // this.selectedVacuum = response;
        // this.loading = true; // mozda ovo ipak ne
      }
    });
  }

  stop() {
    this.vacuumService.stop(this.selectedVacuum.vacuumId)
    .subscribe({
      error: (err) => { 
        alert(`${err.error.message}
        timestamp: ${err.error.timestamp}`);
      },
      next: (response) => {
        console.log(response);
        // this.selectedVacuum = response;

        // this.loading = true; // mozda ovo ipak ne
      }
    });
  }

  discharge() {
    this.vacuumService.discharge(this.selectedVacuum.vacuumId)
    .subscribe({
      error: (err) => { 
        alert(`${err.error.message}
        timestamp: ${err.error.timestamp}`);
      },
      next: (response) => {
        // this.selectedVacuum = response;
        // this.loading = true; // mozda ovo ipak ne
      }
    });
  }

  schedule() {
    //id, operation, Date and time 
    console.log('datetime: ', this.selectedDateTime);
    console.log(this.selectedOperation);
    
    this.vacuumService.schedule(this.selectedVacuum.vacuumId, this.selectedOperation, this.selectedDateTime)
    .subscribe({
      error: (err) => { 
        alert(`${err.error.message}
        timestamp: ${err.error.timestamp}`);
      },
      next: (response) => {
        alert(`Vacuum scheduled for ${this.selectedOperation} at ${this.selectedDateTime}`);
      }
    });
  }

  
}
