import { Injectable } from "@angular/core";
import { Observable, throwError, catchError } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { Lifeguard } from "../models/lifeguard.model";
import { Employer } from "../models/employer.model";
import { Offer } from "../models/offer.model";
import { Pool } from "../models/pool.model";

const urlOffer = '/api/offers'
const urlLifeguards = '/lifeguards'

@Injectable({ providedIn: 'root' })
export class OfferService {
    constructor(private httpClient: HttpClient) { }

    getOffer(id: number): Observable<Object> {
        return this.httpClient.get(urlOffer + "/" + id);
    }

    getOfferPhoto(id: number) {
        return this.httpClient.get(urlOffer + "/" + id + "/photo", { responseType: 'arraybuffer' });
    }

    deleteOffer(id: number | undefined) {
        return this.httpClient.delete(urlOffer + "/" + id);
    }

    applyToOffer(id: number | undefined) {
        return this.httpClient.post(urlOffer + "/" + id + urlLifeguards, "").subscribe();
    }

    withdrawApplication(id: number | undefined) {
        return this.httpClient.delete(urlOffer + "/" + id + urlLifeguards);
    }

    getApplied(id: number | undefined): Observable<Object> {
        return this.httpClient.get(urlOffer + "/" + id + urlLifeguards);
    }

    setLifeguard(idOffer: number | undefined, idLg: number | undefined) {
        this.httpClient.put(urlOffer + "/" + idOffer + urlLifeguards + "/" + idLg, "").subscribe();
        return true
    }

    unselectLifeguard(id: number | undefined) {
        this.httpClient.delete(urlOffer + "/" + id + urlLifeguards + "/n").subscribe();  // n is a dummy value
        return true
    }

    editOffer(id: number | undefined, offer: Offer) {
        return this.httpClient.put(urlOffer + "/" + id, offer);
    }

    newOffer(offer: Offer): Observable<Object>{
        return this.httpClient.post(urlOffer, offer);
    }

    private handleError(error: any) {
        console.error(error);
        return throwError("Server error (" + error.status + "):" + error.text())
    }
}