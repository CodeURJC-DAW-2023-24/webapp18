import { Component, ViewChild } from '@angular/core';
import { Person } from '../../models/person.model';
import { Lifeguard } from '../../models/lifeguard.model';
import { Employer } from '../../models/employer.model';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { HttpClient } from '@angular/common/http';
import { MessageService } from '../../services/message.service';

@Component({
    selector: "user-form",
    templateUrl: './user-form.component.html',
    styleUrl: '../../styles/form.css'
})
export class UserFormComponent{
    user: Person;
    id: number;
    lifeguard: Lifeguard;
    employer: Employer;
    edit:boolean;
    typeUser:string;
    mails:string[];
    message:string;
    color:string;

    reliability:boolean;
    effort:boolean;
    communication:boolean;
    attitude:boolean;
    problemsResolution:boolean;
    leadership:boolean;

    @ViewChild("file")
    file:any;

    constructor(activatedRoute: ActivatedRoute, private router:Router, private service: UserService, private httpClient: HttpClient, private messageService: MessageService) {
        this.id = activatedRoute.snapshot.params['id'];
        let type : string | undefined;
        const routeSegments = activatedRoute.snapshot.url;
        this.edit=false;

        this.user = {mail:"",pass:"",roles:[]}
        this.lifeguard = {mail:"",pass:"",roles:[],skills:[]}
        this.employer = {mail:"",pass:"",roles:[]}

        this.service.getMails().subscribe(
            (response:any)=>{
                this.mails = response;
            },
            error=>{console.error(error)}
        )

        if (routeSegments.length > 0) {
            const firstSegment = routeSegments[0];
            type = firstSegment.path;
        }
        if (this.id && (type === 'lifeguards')) {
            service.getLifeguard(this.id).subscribe(
                lifeguard => {this.lifeguard = lifeguard
                    this.updateSkills();
                    this.lifeguardToPerson();
                    this.edit = true;
                    this.typeUser='lifeguard'
                },
                error => console.error(error)
            );
            this.lifeguardToPerson();
        } else if (this.id && (type === 'employers')){
            service.getEmployer(this.id).subscribe(
              employer => {this.employer = employer
                this.employerToPerson();
                this.edit = true;
                this.typeUser='employer'
              },
              error => console.error(error)
            );
        }
    }

    save() {
        var message = this.checkForm(this.user.mail, this.user.age, this.user.phone)
        if (message !== '')
            this.messageService.showError(message);

        if (this.color !== 'green')
            this.messageService.showError("Email en uso por otro usuario.");

        if (this.typeUser === 'lifeguard') {
            this.updateLifeguard();
            this.service.addOrUpdateLifeguard(this.lifeguard).subscribe(
                (lifeguard: any) => {
                    if (this.edit) {
                        this.uploadLifeguardImage(lifeguard);
                    } else {
                        this.httpClient.post("/api/auth/login", { username: this.user.mail, password: this.user.pass }, { withCredentials: true }).subscribe(
                            _response => this.uploadLifeguardImage(lifeguard),
                            _error => this.messageService.showError("Credenciales inválidas")
                        );
                    }
                },
                _error => {
                    if (!this.edit)
                        this.messageService.showError("Email en uso por otro usuario");
                }
            );
        } else if (this.typeUser === 'employer') {
            this.updateEmployer();
            this.service.addOrUpdateEmployer(this.employer).subscribe(
                (employer: any) => {
                    if (this.edit) {
                        this.uploadEmployerImage(employer);
                    } else {
                        this.httpClient.post("/api/auth/login", { username: this.user.mail, password: this.user.pass }, { withCredentials: true }).subscribe(
                            _response => this.uploadEmployerImage(employer),
                            _error => this.messageService.showError("Credenciales inválidas")
                        );
                    }
                },
                _error => {
                    if (!this.edit)
                        this.messageService.showError("Email en uso por otro usuario");
                }
            );
        }
    }

    /*private setRoles(type:number,...roles: string[]): void {
        if (type === 0){
            this.lifeguard.roles = roles;
        }
        else{
            this.employer.roles = roles;
        }
    }*/

    private uploadLifeguardImage(lifeguard: Lifeguard): void {
        const image = this.file.nativeElement.files[0];
        if (image) {
          let formData = new FormData();
          formData.append("imageFile", image);
          this.service.setLifeguardImage(lifeguard, formData).subscribe(
            response => {
                if (!this.edit){this.httpClient.post('/api/auth/logout', { withCredentials: true }).subscribe((resp: any) => { })};
                this.file.nativeElement.value = null;
                if (this.edit){
                    this.router.navigate(['/lifeguards/'+this.lifeguard.id])
                }else{
                    this.router.navigate(['/login']);
                }
            },
            error => {
                if (!this.edit){this.httpClient.post('/api/auth/logout', { withCredentials: true }).subscribe((resp: any) => { })};
                alert('Error uploading lifeguard image: ' + error)}
          );
        } /*else if(this.removeImage){
          this.service.deleteLifeguardImage(lifeguard).subscribe(
            response => this.router.navigate(['/lifeguards/'+this.lifeguard.id]),
            error => alert('Error deleting lifeguard image: ' + error)
          );
        }*/ else {
            if (!this.edit){this.httpClient.post('/api/auth/logout', { withCredentials: true }).subscribe((resp: any) => { })};
            if (this.edit){
                this.router.navigate(['/lifeguards/'+this.lifeguard.id])
            }else{
                this.router.navigate(['/login']);
            }
        }
      }

      private uploadEmployerImage(employer: Employer): void {
        const image = this.file.nativeElement.files[0];
        if (image) {
          let formData = new FormData();
          formData.append("imageFile", image);
          this.service.setEmployerImage(employer, formData).subscribe(
            response => {
                //this.file.nativeElement.value = null;
                if (!this.edit){this.httpClient.post('/api/auth/logout', { withCredentials: true }).subscribe((resp: any) => { })};
                if (this.edit){
                    this.router.navigate(['/employers/'+this.employer.id])
                }else{
                    this.router.navigate(['/login']);
                }
            },
            error => {
                if (!this.edit){this.httpClient.post('/api/auth/logout', { withCredentials: true }).subscribe((resp: any) => { })};
                alert('Error uploading employer image: ' + error)}
          );
        } /*else if(this.removeImage){
          this.service.deleteEmployerImage(employer).subscribe(
            response => this.router.navigate(['/employers/'+this.employer.id]),
            error => alert('Error deleting employer image: ' + error)
          );
        }*/ else {
            if (!this.edit){this.httpClient.post('/api/auth/logout', { withCredentials: true }).subscribe((resp: any) => { })};
            if (this.edit){
                this.router.navigate(['/employers/'+this.employer.id])
            }else{
                this.router.navigate(['/login']);
            };
        }
      }


    private updateSkills(){
        this.reliability = this.lifeguard.skills.includes("Confianza");
        this.effort = this.lifeguard.skills.includes("Esfuerzo");
        this.communication = this.lifeguard.skills.includes("Comunicacion");
        this.attitude = this.lifeguard.skills.includes("Actitud positiva");
        this.problemsResolution = this.lifeguard.skills.includes("Resolución de problemas");
        this.leadership = this.lifeguard.skills.includes("Liderazgo");
    }

    private lifeguardToPerson(){
        this.user.name = this.lifeguard.name;
        this.user.surname = this.lifeguard.surname;
        this.user.description = this.lifeguard.description;
        this.user.dni = this.lifeguard.dni;
        this.user.mail = this.lifeguard.mail;
        this.user.age = this.lifeguard.age;
        this.user.pass = this.lifeguard.pass;
        this.user.phone = this.lifeguard.phone;
        this.user.country = this.lifeguard.country;
        this.user.locality = this.lifeguard.locality;
        this.user.province = this.lifeguard.province;
        this.user.direction = this.lifeguard.direction;
    }

    private employerToPerson(){
        this.user.name = this.employer.name;
        this.user.surname = this.employer.surname;
        this.user.description = this.employer.description;
        this.user.dni = this.employer.dni;
        this.user.mail = this.employer.mail;
        this.user.age = this.employer.age;
        this.user.pass = this.employer.pass;
        this.user.phone = this.employer.phone;
        this.user.country = this.employer.country;
        this.user.locality = this.employer.locality;
        this.user.province = this.employer.province;
        this.user.direction = this.employer.direction;
    }


    
    private updateLifeguard(){
        this.lifeguard.name = this.user.name;
        this.lifeguard.surname = this.user.surname;
        this.lifeguard.description = this.user.description;
        this.lifeguard.dni = this.user.dni;
        this.lifeguard.mail = this.user.mail;
        this.lifeguard.age = this.user.age;
        this.lifeguard.pass = this.user.pass;
        this.lifeguard.phone = this.user.phone;
        this.lifeguard.country = this.user.country;
        this.lifeguard.locality = this.user.locality;
        this.lifeguard.province = this.user.province;
        this.lifeguard.direction = this.user.direction;

        if (!this.reliability && this.lifeguard.skills?.includes("Confianza")){
            const index = this.lifeguard.skills?.indexOf("Confianza");
            if (index !== undefined && index !== -1) {
                this.lifeguard.skills?.splice(index, 1);
            }
        }
        if (!this.effort && this.lifeguard.skills?.includes("Esfuerzo")){
            const index = this.lifeguard.skills?.indexOf("Esfuerzo");
            if (index !== undefined && index !== -1) {
                this.lifeguard.skills?.splice(index, 1);
            }
        }
        if (!this.communication && this.lifeguard.skills?.includes("Comunicación")){
            const index = this.lifeguard.skills?.indexOf("Comunicación");
            if (index !== undefined && index !== -1) {
                this.lifeguard.skills?.splice(index, 1);
            }
        }
        if (!this.attitude && this.lifeguard.skills?.includes("Actitud positiva")){
            const index = this.lifeguard.skills?.indexOf("Actitud positiva");
            if (index !== undefined && index !== -1) {
                this.lifeguard.skills?.splice(index, 1);
            }
        }
        if (!this.problemsResolution && this.lifeguard.skills?.includes("Resolución de problemas")){
            const index = this.lifeguard.skills?.indexOf("Resolución de problemas");
            if (index !== undefined && index !== -1) {
                this.lifeguard.skills?.splice(index, 1);
            }
        }
        if (!this.leadership && this.lifeguard.skills?.includes("Liderazgo")){
            const index = this.lifeguard.skills?.indexOf("Liderazgo");
            if (index !== undefined && index !== -1) {
                this.lifeguard.skills?.splice(index, 1);
            }
        }

        if (this.reliability && !this.lifeguard.skills?.includes("Confianza")){
            this.lifeguard.skills?.push("Confianza")
        }
        if (this.effort && !this.lifeguard.skills?.includes("Esfuerzo")){
            this.lifeguard.skills?.push("Esfuerzo")
        }
        if (this.communication && !this.lifeguard.skills?.includes("Comunicación")){
            this.lifeguard.skills?.push("Comunicación")
        }
        if (this.attitude && !this.lifeguard.skills?.includes("Actitud positiva")){
            this.lifeguard.skills?.push("Actitud positiva")
        }
        if (this.problemsResolution && !this.lifeguard.skills?.includes("Resolución de problemas")){
            this.lifeguard.skills?.push("Resolución de problemas")
        }
        if (this.leadership && !this.lifeguard.skills?.includes("Liderazgo")){
            this.lifeguard.skills?.push("Liderazgo")
        }
    }

    private updateEmployer(){
        this.employer.name = this.user.name;
        this.employer.surname = this.user.surname;
        this.employer.description = this.user.description;
        this.employer.dni = this.user.dni;
        this.employer.mail = this.user.mail;
        this.employer.age = this.user.age;
        this.employer.pass = this.user.pass;
        this.employer.phone = this.user.phone;
        this.employer.country = this.user.country;
        this.employer.locality = this.user.locality;
        this.employer.province = this.user.province;
        this.employer.direction = this.user.direction;
    }
    

    checkPassword(): void {
        const passInput: HTMLInputElement | null = document.getElementById("pass") as HTMLInputElement;
        if (passInput) {
            const pass: string = passInput.value;
            let message: string = "";
            let color: string = "";
    
            if (!pass) {
                message = "Introduzca una contraseña";
                color = "red";
            } else if (pass.length < 8) {
                message = "Contraseña débil";
                color = "orange";
            } else {
                message = "Contraseña segura";
                color = "green";
            }
            const messageDiv: HTMLElement | null = document.getElementById("passContent");
            if (messageDiv) {
                messageDiv.innerHTML = message;
                messageDiv.style.color = color;
            }
        }
    }
    
    checkAge(): void {
        const ageInput: HTMLInputElement | null = document.getElementById("age") as HTMLInputElement;
        if (ageInput) {
            const age: string = ageInput.value;
            const ageNum: number = parseInt(age);
            let message: string = "";
    
            if (isNaN(ageNum)) {
                message = "La edad debe ser un número";
            } else if (!Number.isInteger(ageNum)) {
                message = "La edad debe ser un número entero";
            } else if (ageNum < 0) {
                message = "La edad debe ser un número positivo";
            }
    
            const messageDiv: HTMLElement | null = document.getElementById("ageContent");
            if (messageDiv) {
                messageDiv.innerHTML = message;
            }
        }
    }
    
    checkPhone(): void {
        const phoneInput: HTMLInputElement | null = document.getElementById("phone") as HTMLInputElement;
        if (phoneInput) {
            const phone: string = phoneInput.value;
            const phoneNum: number = parseInt(phone);
            let message: string = "";
    
            if (isNaN(phoneNum)) {
                message = "El teléfono debe ser un número";
            } else if (!Number.isInteger(phoneNum)) {
                message = "El teléfono debe ser un número entero";
            } else if (phoneNum < 0) {
                message = "El teléfono debe ser un número positivo";
            } else if (phone.length !== 9) {
                message = "El teléfono debe tener 9 cifras";
            }
    
            const messageDiv: HTMLElement | null = document.getElementById("phoneContent");
            if (messageDiv) {
                messageDiv.innerHTML = message;
            }
        }
    }
    
    checkMail(): void {
        const mailInput: HTMLInputElement | null = document.getElementById("mail") as HTMLInputElement;
        if (mailInput) {
            const mail: string = mailInput.value;
            if (this.mails.includes(mail)) {
                this.message = "Email no disponible";
                this.color = "red";
            } else {
                this.message = "Email disponible";
                this.color = "green";
            }
            const messageDiv: HTMLElement | null = document.getElementById("mailContent");
            if (messageDiv) {
                messageDiv.innerHTML = this.message;
                messageDiv.style.color = this.color;
            }
        }
    }

    checkForm(mail: string, age: string | undefined, phone: string | undefined): string {
        let phoneNum: number = 0;
        let ageNum: number = 0;

        if (phone && phone !== "") {
            phoneNum = parseInt(phone);
            if (isNaN(phoneNum))
                return "El teléfono debe ser un número. ";
            else if (phoneNum < 0)
                return "El teléfono debe ser un número positivo. ";
            else if (phone.length !== 9)
                return "El teléfono debe tener 9 cifras. ";
        }

        if (age && age !== "") {
            ageNum = parseInt(age);
            if (isNaN(ageNum))
                return "La edad debe ser un número. ";
            else if (ageNum < 0)
                return "La edad debe ser un número positivo. ";
            else if (ageNum % 1 !== 0)
                return "La edad debe ser un número entero. "
        }

        if (!mail && !this.edit)
            return "Introduzca un email. ";

        if ((!this.user.pass || this.user.pass === "") && !this.edit)
            return "Introduzca una contraseña. ";

        if (!this.typeUser)
            return "Selecciona si eres socorrista o empleado. ";

        return "";
    }

    showForm() {
        const selectedType = this.typeUser;
    
        const lifeguard1 = document.getElementById('lifeguard1');
        const lifeguard2 = document.getElementById('lifeguard2');
        const employer1 = document.getElementById('employer1');
    
        if (lifeguard1 && lifeguard2 && employer1) {
            lifeguard1.style.display = 'none';
            lifeguard2.style.display = 'none';
            employer1.style.display = 'none';
    
            if (selectedType === 'lifeguard') {
                lifeguard1.style.display = 'block';
                lifeguard2.style.display = 'block';
            } else if (selectedType === 'employer') {
                employer1.style.display = 'block';
            }
        }
    }
}