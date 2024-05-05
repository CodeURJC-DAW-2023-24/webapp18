import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { Pool } from '../models/pool.model';

@Injectable({
  providedIn: 'root'
})
export class PoolsService {

  private apiUrl = '/api/pools';

  constructor(private http: HttpClient) { }

  getPools(page: number, size: number): Observable<Pool[]> {
    const params = { page: page.toString(), size: size.toString() };

    console.log('Getting pools from page ' + page);
    return this.http.get<Pool[]>(this.apiUrl, { params });
  }

  getImage(pool: Pool): Observable<Blob> {
    const id = pool.id;
    const url = "/api/pools/"+id+"/photo"
    return this.http.get(url, { responseType: 'blob' });
  }
}
