import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TargetRatesHistoryComponent } from './target-rates-history.component';

describe('TargetRatesHistoryComponent', () => {
  let component: TargetRatesHistoryComponent;
  let fixture: ComponentFixture<TargetRatesHistoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TargetRatesHistoryComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TargetRatesHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
