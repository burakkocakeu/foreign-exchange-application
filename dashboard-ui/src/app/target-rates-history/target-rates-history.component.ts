import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-target-rates-history',
  templateUrl: './target-rates-history.component.html',
  styleUrls: ['./target-rates-history.component.css']
})
export class TargetRatesHistoryComponent {
  @Input() showForm: boolean = false;
  @Output() showFormChange = new EventEmitter<boolean>();
  @Input() targets: any[] = [];

  constructor() {
  }

  closeForm() {
    this.showForm = false;
    this.showFormChange.emit();
  }
}
