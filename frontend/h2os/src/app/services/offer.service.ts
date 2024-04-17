import { Injectable } from "@angular/core";
import { Observable, throwError, catchError } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { Lifeguard } from "../models/lifeguard.model";
import { Employer } from "../models/employer.model";
import { Offer } from "../models/offer.model";
import { Pool } from "../models/pool.model";

const urlOffer = 'https://localhost:8443/api/offers'
const urlEmployer = 'http://localhost:4200/offer/'

@Injectable({ providedIn: 'root'})
export class OfferService{
    constructor(private httpClient: HttpClient){}

    getOffer(id: number): Observable<Offer>{
        return this.httpClient.get<Offer>(urlOffer+"/"+id).pipe(
            catchError(error => this.handleError(error))
        ) as Observable<Offer>;
    }
    private handleError(error:any){
        console.error(error);
        return throwError("Server error ("+error.status+"):"+error.text())
    }
}