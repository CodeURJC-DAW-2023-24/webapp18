import { Injectable } from "@angular/core";
import { Observable, throwError, catchError, tap, take } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { Lifeguard } from "../models/lifeguard.model";
import { Employer } from "../models/employer.model";
import { LoginResponse } from "../models/login-response.model";
import { Offer } from "../models/offer.model";
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

    getMails():Observable<String[]>{
        return this.httpClient.get<String[]>("/api/mails").pipe(
            catchError(error=>this.handleError(error))
        ) as Observable<String[]>;
    }

    getLifeguard(id: number): Observable<Lifeguard>{
        return this.httpClient.get<Lifeguard>(urlLifeguard+"/"+id).pipe(
            catchError(error => this.handleError(error))
        ) as Observable<Lifeguard>
    }

    getOffersAccepted(id:number):Observable<Offer[]>{
        return this.httpClient.get<Offer[]>(urlLifeguard+"/"+id+"/offersAccepted").pipe(
            catchError(error => this.handleError(error))
        ) as Observable<Offer[]>
    }

    addOrUpdateLifeguard(lifeguard: Lifeguard){
        if (!lifeguard.id){
            return this.addLifeguard(lifeguard);
        } else{
            return this.updateLifeguard(lifeguard);
        }
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

    deleteLifeguard(lifeguard:Lifeguard) {
		return this.httpClient.delete(urlLifeguard + "/" + lifeguard.id)
			.pipe(
				catchError(error => this.handleError(error))
			);
	}

    deleteEmployer(employer:Employer) {
		return this.httpClient.delete(urlEmployer + "/" + employer.id)
			.pipe(
				catchError(error => this.handleError(error))
			);
	}

    setLifeguardImage(lifeguard: Lifeguard, formData: FormData) {
		return this.httpClient.post(urlLifeguard + "/"+ lifeguard.id + '/photoUser', formData)
			.pipe(
				catchError(error => this.handleError(error))
			);
	}

	deleteLifeguardImage(lifeguard: Lifeguard) {
		return this.httpClient.delete(urlLifeguard + "/" + lifeguard.id + '/photoUser')
			.pipe(
				catchError(error => this.handleError(error))
			);
	}

    setEmployerImage(employer: Employer, formData: FormData) {
		return this.httpClient.post(urlEmployer + "/" + employer.id + '/photoCompany', formData)
			.pipe(
				catchError(error => this.handleError(error))
			);
	}

	deleteEmployerImage(employer: Employer) {
		return this.httpClient.delete(urlEmployer + "/" + employer.id + '/photoCompany')
			.pipe(
				catchError(error => this.handleError(error))
			);
	}

    getImage(user: Person) {
        if (user.type == "lg")  // It must be checked with roles
            return this.getLifeguardImage(user as Lifeguard)
        else
            return this.getEmployerImage(user as Employer)
    }

    getLifeguardImage(lifeguard:Lifeguard) {
        return this.httpClient.get(urlLifeguard + "/" + lifeguard.id + '/photoUser', { responseType: 'arraybuffer' })
    }

    getEmployerImage(employer:Employer) {
        return this.httpClient.get(urlEmployer + "/" + employer.id + '/photoCompany', { responseType: 'arraybuffer' })
    }

    getLifeguardOffers(id: number): Observable<Object>{
        return this.httpClient.get(urlLifeguard+"/"+id+"/offers");
    }

    getEmployerOffers(id: number): Observable<Object>{
        return this.httpClient.get(urlEmployer+"/"+id+"/offers");
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


    login(mail: string, pass: string): Observable<LoginResponse> {
        return this.httpClient.post("/api/auth/login", { username: mail, password: pass }, { withCredentials: true })
            .pipe(
                take(1),
                tap(_ => this.reqIsLogged()),
                catchError(error => {
                    alert("Wrong credentials");
                    return this.handleError(error)
                })
            ) as Observable<LoginResponse>
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

    logout() {
        this.logged = false;
        if (this.typeUser === 'lg') {
            this.lifeguard = { mail: "", pass: "", roles: [], skills: [] }
            this.typeUser = "";
        } else if (this.typeUser === 'e') {
            this.employer = { mail: "", pass: "", roles: [] }
            this.typeUser = "";
        }

        return this.httpClient.post('/api/auth/logout', { withCredentials: true })
            .pipe(
                catchError(error => this.handleError(error))
            )
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
            return false;
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

    getCurrentLifeguard(): Lifeguard{
        return this.lifeguard
    }

    getCurrentEmployer(): Employer{
        return this.employer
    }

    private handleError(error:any){
        console.error(error);
        return throwError("Server error ("+error.status+"):"+error.text())
    }
}