import { Injectable } from "@angular/core";
import { Observable, throwError, catchError } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { withInMemoryScrolling } from "@angular/router";

const urlPieChart = '/api/chart/'

@Injectable({ providedIn: 'root' })
export class StadisticsService {
    constructor(private httpClient: HttpClient){}

    getPieChart(): Observable<Object> {
        return this.httpClient.get(urlPieChart+"/");
    }
}

