import { Injectable } from "@angular/core";
import { Observable, throwError, catchError } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { Lifeguard } from "../models/lifeguard.model";
import { Employer } from "../models/employer.model";

const urlLifeguard = 'http://localhost:4200/lifeguards/'
const urlEmployer = 'http://localhost:4200/employers/'

@Injectable({ providedIn: 'root'})
export class UserService{

    constructor(private httpClient: HttpClient){}
    getLifeguards(): Observable<Lifeguard[]>{
        return this.httpClient.get<Lifeguard[]>(urlLifeguard).pipe(
            catchError(error => this.handleError(error))
        ) as Observable<Lifeguard[]>;
    }

    getLifeguard(id: number): Observable<Lifeguard>{
        return this.httpClient.get<Lifeguard>(urlLifeguard+id).pipe(
            catchError(error => this.handleError(error))
        ) as Observable<Lifeguard>
    }

    addOrUpdateLifeguard(lifeguard: Lifeguard){
        if (!lifeguard.id){
            return this.addLifeguard(lifeguard);
        } else{
            return this.updateLifeguard(lifeguard);
        }
    }

    getEmployers(): Observable<Employer[]>{
        return this.httpClient.get<Employer[]>(urlEmployer).pipe(
            catchError(error => this.handleError(error))
        ) as Observable<Employer[]>;
    }

    getEmployer(id: number): Observable<Employer>{
        return this.httpClient.get<Employer>(urlEmployer+id).pipe(
            catchError(error => this.handleError(error))
        ) as Observable<Employer>
    }

    addOrUpdateEmployer(employer: Employer){
        if (!employer.id){
            return this.addEmployer(employer);
        } else{
            return this.updateEmployer(employer);
        }
    }

    private addLifeguard(lifeguard: Lifeguard){
        return this.httpClient.post(urlLifeguard,lifeguard).pipe(
            catchError(error => this.handleError(error))
        );
    }

    private updateLifeguard(lifeguard:Lifeguard){
        return this.httpClient.put(urlLifeguard+lifeguard.id,lifeguard).pipe(
            catchError(error => this.handleError(error))
        );
    }

    private addEmployer(employer: Employer){
        return this.httpClient.post(urlEmployer,employer).pipe(
            catchError(error => this.handleError(error))
        );
    }

    private updateEmployer(employer:Employer){
        return this.httpClient.put(urlEmployer+employer.id,employer).pipe(
            catchError(error => this.handleError(error))
        );
    }


    login(mail: string, pass: string){
        const loginData = { username: mail, password: pass };
        return this.httpClient.post("/api/auth/login", loginData).subscribe();
    }
    me(): Observable<Object>{
        return this.httpClient.get("/api/auth/me");
    }

    private handleError(error:any){
        console.error(error);
        return throwError("Server error ("+error.status+"):"+error.text())
    }
}