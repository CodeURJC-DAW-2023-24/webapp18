import { Injectable } from "@angular/core";
import { Observable, throwError, catchError } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { Pool } from "../models/pool.model";
import { Message } from "../models/message.model";

const urlPool = '/api/pools'

@Injectable({ providedIn: 'root' })
export class PoolService {
    constructor(private httpClient: HttpClient) { }

    addOrUpdatePool(pool: Pool){
        if (!pool.id){
            return this.addPool(pool);
        } else{
            return this.updatePool(pool);
        }
    }

    getPool(id: number): Observable<Object> {
        return this.httpClient.get(urlPool + "/" + id);
    }

    getPoolPhoto(id: number) {
        return this.httpClient.get(urlPool + "/" + id + "/photo", { responseType: 'arraybuffer' });
    }

    deletePool(id: number | undefined) {
        return this.httpClient.delete(urlPool + "/" + id);
    }

    editPool(id: number | undefined, pool: Pool) {
        this.httpClient.put(urlPool + "/" + id, pool).subscribe();
        return true
    }

    newPool(pool: Pool): Observable<Object>{
        return this.httpClient.post(urlPool, pool);
    }

    setPoolPhoto(pool: Pool, formData: FormData) {
        return this.httpClient.post(urlPool + "/"+ pool.id + '/photo', formData)
                .pipe(
                    catchError(error => this.handleError(error))
                );
    }

    private addPool(pool: Pool){
        return this.httpClient.post(urlPool,pool).pipe(
            catchError(error => this.handleError(error))
        );
    }

    private updatePool(pool:Pool){
        return this.httpClient.put(urlPool+"/"+pool.id,pool).pipe(
            catchError(error => this.handleError(error))
        );
    }

    private handleError(error: any) {
        console.error(error);
        return throwError("Server error (" + error.status + "):" + error.text())
    }

    getPoolMessages(id: number){
        return this.httpClient.get(urlPool+"/"+id+"/messages")
    }

    deleteMessage(idPool: number, idMessage: number){
        this.httpClient.delete(urlPool+"/"+idPool+"/messages/"+idMessage).subscribe()
        return true;
    }

    newMessage(idPool: number, msg: Message){
        this.httpClient.post(urlPool+"/"+idPool+"/messages",msg).subscribe()
    }
}