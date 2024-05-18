import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { Offer } from '../models/offer.model';
import { Pool } from '../models/pool.model';
import { Person } from '../models/person.model';
import { Lifeguard } from '../models/lifeguard.model';
import { Employer } from '../models/employer.model';

@Injectable({
  providedIn: 'root'
})
export class PaginationService {

  private offersURL = '/api/offers';
  private poolsURL = '/api/pools';
  private lifeguardsURL = '/api/lifeguards';
  private employersURL = '/api/employers';

  constructor(private http: HttpClient) { }

  getOffers(page: number, size: number): Observable<Offer[]> {
    const params = { page: page.toString(), size: size.toString() };
    return this.http.get<Offer[]>(this.offersURL, { params })
  }

  getPools(page: number, size: number): Observable<Pool[]> {
    const params = { page: page.toString(), size: size.toString() };
    return this.http.get<Pool[]>(this.poolsURL, { params });
  }

  getLifeguards(page: number, size: number): Observable<Person[]>{
    const params = { page: page.toString(), size: size.toString() };
    return this.http.get<Lifeguard[]>(this.lifeguardsURL, { params })
  }

  getEmployers(page: number, size: number): Observable<Person[]>{
    const params = { page: page.toString(), size: size.toString() };
    return this.http.get<Employer[]>(this.employersURL, { params })
  }

  getOfferImage(offer: Offer): Observable<Blob> {
    const url = this.offersURL + "/" + offer.id + "/photo"
    return this.http.get(url, { responseType: 'blob' });
  }

  getPoolImage(pool: Pool): Observable<Blob> {
    const url = this.poolsURL + "/" + pool.id + "/photo";
    return this.http.get(url, { responseType: 'blob' });
  }
}
