import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OperationVacuumsComponent } from './operation-vacuums.component';

describe('OperationVacuumsComponent', () => {
  let component: OperationVacuumsComponent;
  let fixture: ComponentFixture<OperationVacuumsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OperationVacuumsComponent]
    });
    fixture = TestBed.createComponent(OperationVacuumsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
