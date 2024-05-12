import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { GeocoderResponse } from '../models/geocoder-response.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class GeocodingService {
  constructor(private http: HttpClient) {}

  geocodeLatLng(location: google.maps.LatLngLiteral): Promise<GeocoderResponse> {
    let geocoder = new google.maps.Geocoder();

    return new Promise((resolve, reject) => {
      geocoder.geocode({ 'location': location }, (results, status) => {
        if (results !== null) {
            const response = new GeocoderResponse(status, results);
            resolve(response);
        } else {
            reject(status);
        }
      });
    });
  }

  getLocation(term: string): Observable<GeocoderResponse> {
    const parsedTerm = term.replace(/ /g, '+');
    const url = `https://maps.google.com/maps/api/geocode/json?address=${parsedTerm}&key=${environment.googleApiKey}`;
    console.log(url);
    return this.http.get<GeocoderResponse>(url);
  }
}