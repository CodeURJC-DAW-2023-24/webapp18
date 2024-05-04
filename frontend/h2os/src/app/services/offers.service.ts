import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { Offer } from '../models/offer.model';

@Injectable({
  providedIn: 'root'
})
export class OffersService {

  private apiUrl = '/api/offers';

  constructor(private http: HttpClient) { }

  getOffers(page: number, size: number): Observable<Offer[]> {
    const params = { page: page.toString(), size: size.toString() };

    console.log('Getting offers from page ' + page);
    return this.http.get<Offer[]>(this.apiUrl, { params })
  }

  getImage(offer: Offer): Observable<Blob> {
    const id = offer.id;
    const url = "/api/offers/"+id+"/photo"
    return this.http.get(url, { responseType: 'blob' });
  }
}
