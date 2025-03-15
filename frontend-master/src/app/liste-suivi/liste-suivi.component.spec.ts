import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListeSuiviComponent } from './liste-suivi.component';

describe('ListeSuiviComponent', () => {
  let component: ListeSuiviComponent;
  let fixture: ComponentFixture<ListeSuiviComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListeSuiviComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListeSuiviComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
