import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule, Routes } from '@angular/router';

import { AppComponent } from './app.component';
import { ForeignExchangeComponent } from './foreign-exchange/foreign-exchange.component';
import { FormsModule } from "@angular/forms";
import { MatSelectModule } from '@angular/material/select';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from "@angular/material/card";
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { CurrencySelectionComponent } from './currency-selection/currency-selection.component';
import { MatListModule } from "@angular/material/list";
import { TargetRatesHistoryComponent } from './target-rates-history/target-rates-history.component';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatAutocompleteModule } from "@angular/material/autocomplete";
import { MatDatepickerModule } from "@angular/material/datepicker";
import { MatNativeDateModule } from '@angular/material/core';
import { DatePipe } from '@angular/common';

const routes: Routes = [
  { path: '', component: ForeignExchangeComponent }
];

@NgModule({
  declarations: [
    AppComponent,
    ForeignExchangeComponent,
    CurrencySelectionComponent,
    TargetRatesHistoryComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot(routes),
    FormsModule,
    MatCardModule,
    MatSelectModule,
    MatTableModule,
    MatIconModule,
    MatButtonModule,
    MatDialogModule,
    BrowserAnimationsModule,
    MatListModule,
    MatFormFieldModule,
    MatInputModule,
    MatAutocompleteModule,
    MatDatepickerModule,
    MatNativeDateModule
  ],
  providers: [
    MatDialog,
    DatePipe
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
