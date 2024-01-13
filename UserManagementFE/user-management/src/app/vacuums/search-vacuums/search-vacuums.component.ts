import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { VacuumService } from 'src/app/services/vacuum.service';

@Component({
  selector: 'app-search-vacuums',
  templateUrl: './search-vacuums.component.html',
  styleUrls: ['./search-vacuums.component.css']
})
export class SearchVacuumsComponent {
  formData = {
    name: null,
    options: {
      OFF: false,
      ON: false,
      DISCHARGING: false
    },
    startDate: null,
    endDate: null
  };
  vacuums: any; // list
  canRemoveVacuum!: boolean;

  constructor(private vacuumService: VacuumService, private router: Router) {}

  ngOnInit() {
    this.canRemoveVacuum = JSON.parse(sessionStorage.getItem('loggedInUser') || '').canRemoveVacuum;
    this.vacuumService.search(null, null, null, null)
    .subscribe({
      error: (err) => { 
        console.log('err: ', err);
        alert(err);
      },
      next: (response) => {
        console.log(response);
        this.vacuums = response;
      }
    });
  }

  search() {
    // console.log(this.createOptionString());
    
    // console.log(this.formData);

    this.vacuumService.search(
      this.formData.name,
      this.createOptionString(),
      this.formData.startDate,
      this.formData.endDate
    )
    .subscribe({
      error: (err) => { 
        console.log('err: ', err);
        alert(err);
      },
      next: (response) => {
        console.log(response);
        this.vacuums = response;
      }
    });
  }

  navigateToDetails(id: number) {
    this.vacuumService.findById(id).subscribe({
      error: (err) => { 
        console.log('err: ', err);
        alert(err);
      },
      next: (selectedVacuum) => {
        console.log(selectedVacuum);
        sessionStorage.setItem('selectedVacuumId', selectedVacuum.vacuumId);
         this.router.navigate(['/vacuum-details']);

      }
    });
  }

  removeVacuum(vacuumId: number) {
    this.vacuumService.removeVacuum(vacuumId)
    .subscribe({
      error: (err) => { 
        alert(`${err.error.message}
        timestamp: ${err.error.timestamp}`);
      },
      next: (response) => {
        // this.router.navigate(['/home']);
        this.ngOnInit();
      }
    })
  }

  private createOptionString() {
    let result: string = '';
    result += this.formData.options.OFF ? 'OFF,' : '';
    result += this.formData.options.ON ? 'ON,' : '';
    result += this.formData.options.DISCHARGING ? 'DISCHARGING,' : '';
    return result === '' ? null : result;
  }
  
}
