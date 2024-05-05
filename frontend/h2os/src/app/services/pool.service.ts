import { Injectable } from "@angular/core";
import { Observable, throwError, catchError } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { Pool } from "../models/pool.model";

const urlPool = '/api/pools'

@Injectable({ providedIn: 'root' })
export class PoolService {
    constructor(private httpClient: HttpClient) { }

    getPool(id: number): Observable<Object> {
        return this.httpClient.get(urlPool + "/" + id);
    }

    getPoolPhoto(id: number) {
        return this.httpClient.get(urlPool + "/" + id + "/photo", { responseType: 'arraybuffer' });
    }

    deletePool(id: number | undefined) {
        this.httpClient.delete(urlPool + "/" + id).subscribe();
    }

    editPool(id: number | undefined, pool: Pool) {
        const json = ""
        console.log(JSON.stringify(pool))
        this.httpClient.put(urlPool + "/" + id, pool).subscribe();
        return true
    }

    newPool(pool: Pool): Observable<Object>{
        return this.httpClient.post(urlPool, pool);
    }

    private handleError(error: any) {
        console.error(error);
        return throwError("Server error (" + error.status + "):" + error.text())
    }
}