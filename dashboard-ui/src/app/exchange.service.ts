import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ExchangeService {

  BASE_URL = 'http://localhost:4000/api/exchange';
  CURRENCIES = ['AED', 'AFN', 'ALL', 'AMD', 'ANG', 'AOA', 'ARS', 'AUD', 'AWG', 'AZN', 'BAM', 'BBD', 'BDT', 'BGN', 'BHD', 'BIF', 'BMD', 'BND', 'BOB', 'BRL', 'BSD', 'BTC', 'BTN', 'BWP', 'BYN', 'BYR', 'BZD', 'CAD', 'CDF', 'CHF', 'CLF', 'CLP', 'CNY', 'COP', 'CRC', 'CUC', 'CUP', 'CVE', 'CZK', 'DJF', 'DKK', 'DOP', 'DZD', 'EGP', 'ERN', 'ETB', 'EUR', 'FJD', 'FKP', 'GBP', 'GEL', 'GGP', 'GHS', 'GIP', 'GMD', 'GNF', 'GTQ', 'GYD', 'HKD', 'HNL', 'HRK', 'HTG', 'HUF', 'IDR', 'ILS', 'IMP', 'INR', 'IQD', 'IRR', 'ISK', 'JEP', 'JMD', 'JOD', 'JPY', 'KES', 'KGS', 'KHR', 'KMF', 'KPW', 'KRW', 'KWD', 'KYD', 'KZT', 'LAK', 'LBP', 'LKR', 'LRD', 'LSL', 'LTL', 'LVL', 'LYD', 'MAD', 'MDL', 'MGA', 'MKD', 'MMK', 'MNT', 'MOP', 'MRO', 'MUR', 'MVR', 'MWK', 'MXN', 'MYR', 'MZN', 'NAD', 'NGN', 'NIO', 'NOK', 'NPR', 'NZD', 'OMR', 'PAB', 'PEN', 'PGK', 'PHP', 'PKR', 'PLN', 'PYG', 'QAR', 'RON', 'RSD', 'RUB', 'RWF', 'SAR', 'SBD', 'SCR', 'SDG', 'SEK', 'SGD', 'SHP', 'SLE', 'SLL', 'SOS', 'SRD', 'STD', 'SVC', 'SYP', 'SZL', 'THB', 'TJS', 'TMT', 'TND', 'TOP', 'TRY', 'TTD', 'TWD', 'TZS', 'UAH', 'UGX', 'USD', 'UYU', 'UZS', 'VEF', 'VES', 'VND', 'VUV', 'WST', 'XAF', 'XAG', 'XAU', 'XCD', 'XDR', 'XOF', 'XPF', 'YER', 'ZAR', 'ZMK', 'ZMW', 'ZWL']

  constructor(private http: HttpClient) { }

  getExchangeRatesList(base: string, currencies?: string[]): Observable<any> {
    let params = new HttpParams().set('base', base);
    if (currencies) {
      params = params.set('currencies', currencies.join(','));
    }
    return this.http.get<any>(this.BASE_URL + '/rates', { params });
  }

  getExchangeRatesHistorical(params: any, page?: number, size?: number): Observable<any> {
    if (page && size) {
      params['page'] = page.toString();
      params['size'] = size.toString();
    }
    return this.http.get<any>(this.BASE_URL + '/rates/historical', { params });
  }


  getExchangeConvert(params: any): Observable<any> {
    return this.http.get<any>(this.BASE_URL + `/convert?source=${params.source}&target=${params.target.join(',')}&amount=${params.amount}`);
  }

  getExchangeConvertHistorical(params: any, page?: number, size?: number): Observable<any> {
    if (page && size) {
      params['page'] = page.toString();
      params['size'] = size.toString();
    }
    return this.http.get<any>(this.BASE_URL + '/convert/historical', { params });
  }

  postExchangeRatesList(body: any): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/stream+json'
      })
    };
    return this.http.post<any>(this.BASE_URL + '/rates', body, httpOptions);
  }

}
