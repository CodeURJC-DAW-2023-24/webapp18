import { Injectable } from "@angular/core";
import { Observable, throwError, catchError } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { Lifeguard } from "../models/lifeguard.model";
import { Employer } from "../models/employer.model";
import { Person } from "../models/person.model";

const urlLifeguard = '/api/lifeguards'
const urlEmployer = '/api/employers'

@Injectable({ providedIn: 'root'})
export class UserService{
    lifeguard:Lifeguard;
    employer:Employer;
    typeUser:String;
    logged:boolean;

    constructor(private httpClient: HttpClient){
        this.lifeguard = {mail:"",pass:"",roles:[],skills:[]}
        this.employer = {mail:"",pass:"",roles:[]}
    }

    getLifeguards(): Observable<Lifeguard[]>{
        return this.httpClient.get<Lifeguard[]>(urlLifeguard).pipe(
            catchError(error => this.handleError(error))
        ) as Observable<Lifeguard[]>;
    }

    getLifeguard(id: number): Observable<Lifeguard>{
        return this.httpClient.get<Lifeguard>(urlLifeguard+"/"+id).pipe(
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
        return this.httpClient.get<Employer>(urlEmployer+"/"+id).pipe(
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
        return this.httpClient.put(urlLifeguard+"/"+lifeguard.id,lifeguard).pipe(
            catchError(error => this.handleError(error))
        );
    }

    private addEmployer(employer: Employer){
        return this.httpClient.post(urlEmployer,employer).pipe(
            catchError(error => this.handleError(error))
        );
    }

    private updateEmployer(employer:Employer){
        return this.httpClient.put(urlEmployer+"/"+employer.id,employer).pipe(
            catchError(error => this.handleError(error))
        );
    }


    login(mail: string, pass: string) {

        this.httpClient.post("/api/auth/login", { username: mail, password: pass }, { withCredentials: true })
            .subscribe(
                (response) => this.reqIsLogged(),
                (error) => alert("Wrong credentials")
            );

    }

    private reqIsLogged() {
        this.httpClient.get('/api/auth/me', { withCredentials: true }).subscribe(
            (response:any) => {
                if (response.type==='lg'){
                    this.lifeguard = response as Lifeguard;
                    this.typeUser = "lg";
                }else if (response.type==='e'){
                    this.employer = response as Employer;
                    this.typeUser = "e";
                }
                this.logged = true;
            },
            error => {
                if (error.status != 404) {
                    console.error('Error when asking if logged: ' + JSON.stringify(error));
                }
            }
        );

    }

    logOut() {
        return this.httpClient.post('/api/auth/logout', { withCredentials: true })
            .subscribe((resp: any) => {
                console.log("LOGOUT: Successfully");
                this.logged = false;
                if (this.typeUser==='lg'){
                    this.lifeguard = {mail:"",pass:"",roles:[],skills:[]}
                    this.typeUser = "";
                }else if (this.typeUser==='e'){
                    this.employer = {mail:"",pass:"",roles:[]}
                    this.typeUser = "";
                }
            });

    }

    isLogged() {
        return this.logged;
    }

    isAdmin() {
        if (this.typeUser==='lg'){
            return this.lifeguard && this.lifeguard.roles.indexOf('ADMIN') !== -1;
        }else if (this.typeUser==='e'){
            return this.employer && this.employer.roles.indexOf('ADMIN') !== -1;
        }else{
            return null;
        }
    }

    isLifeguard(){
        return this.typeUser==='lg';
    }

    isEmployer(){
        return this.typeUser==='e';
    }

    currentUser() {
        if (this.typeUser==='lg'){
            return this.lifeguard;
        }else if (this.typeUser==='e'){
            return this.employer;
        }else{
            return null;
        }
    }

    me(): Observable<Object>{
        return this.httpClient.get("/api/auth/me");
    }

    private handleError(error:any){
        console.error(error);
        return throwError("Server error ("+error.status+"):"+error.text())
    }
}