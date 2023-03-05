import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-currency-selection',
  templateUrl: './currency-selection.component.html',
  styleUrls: ['./currency-selection.component.css']
})
export class CurrencySelectionComponent implements OnInit {
  currencies!: string[];
  filteredCurrencies: string[] = [];
  searchValue: string = '';

  constructor(@Inject(MAT_DIALOG_DATA) public data: { currencies: string[], unListCurrencies: string[] },
              public dialogRef: MatDialogRef<CurrencySelectionComponent>) {

  }

  ngOnInit(): void {
    this.currencies = this.data.currencies
      .filter(currency => !this.data.unListCurrencies.includes(currency));
    this.filteredCurrencies = this.currencies;
  }

  onSelect(currency: string): void {
    this.dialogRef.close(currency);
  }

  applyFilter(): void {
    this.filteredCurrencies = this.currencies.filter((currency) =>
      currency.toLowerCase().includes(this.searchValue.toLowerCase())
    );
  }

  onClose(): void {
    this.dialogRef.close();
  }
}
