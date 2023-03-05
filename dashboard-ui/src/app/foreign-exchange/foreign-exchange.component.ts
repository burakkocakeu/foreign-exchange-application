import {Component, OnInit} from '@angular/core';
import {ExchangeService} from '../exchange.service';
import {MatDialog} from '@angular/material/dialog';
import {CurrencySelectionComponent} from '../currency-selection/currency-selection.component';
import {DatePipe} from "@angular/common";


@Component({
  selector: 'app-foreign-exchange',
  templateUrl: './foreign-exchange.component.html',
  styleUrls: ['./foreign-exchange.component.css']
})
export class ForeignExchangeComponent implements OnInit {
  selectedCurrency: string = 'EUR';
  targetCurrencies: Set<string> = new Set(['USD', 'GBP', 'TRY']);
  exchangeRates: any;
  historicalExchangeRates: any;
  showTargetRatesHistory: boolean = false;
  showTargetRatesHistoryItem: string = '';

  sourceCurrency: string = 'EUR';
  sourceAmount: bigint = BigInt(1.0);
  targetCurrency: string = 'USD';
  targetAmount: bigint = BigInt(1.0);
  convertBySource: boolean = true;
  historicalExchangeConversions: any;
  showTargetConversionsHistory: boolean = false;
  showTargetConversionsHistoryItem: string = '';

  today = new Date().toISOString().slice(0, 10);
  minus3Days: string;

  exchanges: any[] = [];

  constructor(private exchangeService: ExchangeService, private dialog: MatDialog, private datePipe: DatePipe) {
    const now = new Date();
    const minus3DaysDate = new Date(now);
    minus3DaysDate.setDate(now.getDate() - 3);
    this.minus3Days = minus3DaysDate.toISOString().slice(0, 10);
  }

  ngOnInit(): void {
    this.getExchangeRates();
    this.getExchangeConversion();
    this.exchanges = [{ currency: 'USD', amount: 1, date: this.today }];
  }

  get getCurrencies(): string[] {
    return this.exchangeService.CURRENCIES;
  }

  selectCurrency(currency: string) {
    this.selectedCurrency = currency;
    this.getExchangeRates();
    this.getHistoricalExchangeRates(this.showTargetRatesHistoryItem);
  }

  exchangeConversionBy(isSource: boolean) {
    this.convertBySource = isSource;
    this.getExchangeConversion();
  }

  /**
   * API endpoints
   */
  getExchangeRates(): void {
    this.exchangeService.getExchangeRatesList(this.selectedCurrency, Array.from(this.targetCurrencies))
      .subscribe((data) => {
        this.exchangeRates = data;
      });
  }

  getHistoricalExchangeRates(target: string, showHistoryModal: boolean = false) {
    const params = {
      source: this.selectedCurrency, target: target, start: '2023-03-01', end: '2023-03-06'
    }
    this.exchangeService.getExchangeRatesHistorical(params)
      .subscribe((response: any) => {
        this.historicalExchangeRates = response.targets;
        // show the modal dialog
        if (showHistoryModal) {
          if (this.showTargetRatesHistoryItem === '' ||
            this.showTargetRatesHistoryItem === target ||
            this.showTargetRatesHistory === undefined)
            this.showTargetRatesHistory = !this.showTargetRatesHistory;
          this.showTargetRatesHistoryItem = target;
        }
      });
  }

  getExchangeConversion() {
    const source = this.convertBySource ? this.sourceCurrency : this.targetCurrency;
    const target = this.convertBySource ? this.targetCurrency : this.sourceCurrency;
    const amount = this.convertBySource ? this.sourceAmount : this.targetAmount;
    const exchangeConversionRequest = {
      source: source,
      target: [target],
      amount: amount
    };
    this.exchangeService.getExchangeConvert(exchangeConversionRequest)
      .subscribe((response: any) => {
        if (this.convertBySource)
          this.targetAmount = response.targets[0].amount;
        else
          this.sourceAmount = response.targets[0].amount;
        if (this.showTargetConversionsHistory)
          this.getHistoricalExchangeConversions();
      });
  }

  getHistoricalExchangeConversions(showHistoryModal: boolean = false) {
    const source = this.convertBySource ? this.sourceCurrency : this.targetCurrency;
    const target = this.convertBySource ? this.targetCurrency : this.sourceCurrency;
    const amount = this.convertBySource ? this.sourceAmount : this.targetAmount;
    const params = {
      source: source, target: target, amount: amount, start: '2023-03-01', end: '2023-03-06'
    }
    this.exchangeService.getExchangeConvertHistorical(params).subscribe((response) => {
      this.historicalExchangeConversions = response.targets;
      // show the modal dialog
      if (showHistoryModal) {
        if (this.showTargetConversionsHistoryItem === '' ||
          this.showTargetConversionsHistory === undefined)
          this.showTargetConversionsHistory = !this.showTargetConversionsHistory;
      }
    });
  }

  /**
   * Open Dialogs of other components
   */
  openCurrencySelectionDialog(): void {
    const targetCurrenciesArray = [...this.targetCurrencies];
    targetCurrenciesArray.push(this.selectedCurrency);
    const dialogRef = this.dialog.open(CurrencySelectionComponent, {
      data: { currencies: this.getCurrencies, unListCurrencies: targetCurrenciesArray }
    });

    dialogRef.afterClosed().subscribe((result: string) => {
      if (result) {
        this.targetCurrencies.add(result);
        this.getExchangeRates();
      }
    });
  }

  /**
   * Add new Exchange
   */
  addExchange(): void {
    const newExchange = { currency: 'USD', amount: 1, date: this.today };
    this.exchanges.push({ ...newExchange });
    console.log(this.exchanges);
  }

  saveExchanges(): void {
    const requestBody = this.exchanges.map(item => {
      return {
        currency: item.currency,
        rate: item.amount,
        date: this.datePipe.transform(item.date, 'yyyy-MM-dd') // format date as "YYYY-MM-DD"
      };
    });
    console.log(requestBody);
    // make API call to save exchanges with requestBody
    this.exchangeService.postExchangeRatesList(requestBody).subscribe((result) => {
      if (result) {
        console.log(result);
        this.ngOnInit();
      }
    });
  }

}
