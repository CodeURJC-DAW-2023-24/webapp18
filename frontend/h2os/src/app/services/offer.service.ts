import { Injectable } from "@angular/core";
import { Observable, throwError, catchError } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { Lifeguard } from "../models/lifeguard.model";
import { Employer } from "../models/employer.model";
import { Offer } from "../models/offer.model";
import { Pool } from "../models/pool.model";

const urlOffer = '/api/offers'
const urlLifeguards = '/lifeguards'

@Injectable({ providedIn: 'root'})
export class OfferService{
    constructor(private httpClient: HttpClient){}

    getOffer(id: number): Observable<Object>{
        return this.httpClient.get(urlOffer+"/"+id);
        return this.httpClient.get<Offer>(urlOffer+"/"+id).pipe(
            catchError(error => this.handleError(error))
        ) as Observable<Offer>;
    }
    getOfferPhoto(id: number): Observable<Object>{
        return this.httpClient.get(urlOffer+"/"+id+"/photo");
        return this.httpClient.get<Offer>(urlOffer+"/"+id).pipe(
            catchError(error => this.handleError(error))
        ) as Observable<Offer>;
    }

    deleteOffer(id: number| undefined) {
        this.httpClient.delete(urlOffer+"/"+id).subscribe();
    
    }

    applyToOffer(id: number | undefined){
        return this.httpClient.post(urlOffer+"/"+id+"/"+urlLifeguards,"").subscribe();
    }

    getApplied(id: number | undefined): Observable<Object>{
        return this.httpClient.get(urlOffer+"/"+id+"/lifeguards");
    }

    setLifeguard(idOffer: number | undefined, idLg: number | undefined){
        this.httpClient.put(urlOffer+"/"+idOffer+"/lifeguards/"+idLg,"").subscribe();
        return true
    }

    unSelectLifeguard(id: number | undefined){
        this.httpClient.delete(urlOffer+"/"+id+"/lifeguards").subscribe();
        return true
    }

    private handleError(error:any){
        console.error(error);
        return throwError("Server error ("+error.status+"):"+error.text())
    }
}