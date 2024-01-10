import { Component } from '@angular/core';
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

  constructor(private vacuumService: VacuumService) {}

  ngOnInit() {
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
    console.log(this.createOptionString());
    
    console.log(this.formData);

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

  private createOptionString() {
    let result: string = '';
    result += this.formData.options.OFF ? 'OFF,' : '';
    result += this.formData.options.ON ? 'ON,' : '';
    result += this.formData.options.DISCHARGING ? 'DISCHARGING,' : '';
    return result === '' ? null : result;
  }
  
}
