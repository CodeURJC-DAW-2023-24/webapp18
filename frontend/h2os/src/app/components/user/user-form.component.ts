import { Component } from '@angular/core';
import { Person } from '../../models/person.model';
import { Lifeguard } from '../../models/lifeguard.model';
import { Employer } from '../../models/employer.model';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../../services/user.service';

@Component({
    selector: "user-form",
    templateUrl: './user-form.component.html'
})

export class UserFormComponent{
    user: Person;
    lifeguard: Lifeguard;
    employer: Employer;
    edit:boolean;
    typeUser:string;

    reliability:boolean;
    effort:boolean;
    communication:boolean;
    attitude:boolean;
    problemsResolution:boolean;
    leadership:boolean;

    constructor(activatedRoute: ActivatedRoute, private service: UserService) {
        let id = activatedRoute.snapshot.params['id'];
        let type : string | undefined;
        const routeSegments = activatedRoute.snapshot.url;
        if (routeSegments.length > 0) {
            const firstSegment = routeSegments[0];
            type = firstSegment.path;
        }
        if (id && (type === 'lifeguards')) {
            service.getLifeguard(id).subscribe(
                lifeguard => this.lifeguard = lifeguard,
                error => console.error(error)
            );
            this.lifeguardToPerson();
        } else if (id && (type === 'employers')){
            service.getEmployer(id).subscribe(
              employer => this.employer = employer,
              error => console.error(error)
            );
            this.employerToPerson();
        }
    }

    save() {
        if (this.typeUser==='lifeguard'){
            this.updateLifeguard();
            this.service.addOrUpdateLifeguard(this.lifeguard).subscribe(
            lifeguard => { },
            error => console.error('Error creating new lifeguard: ' + error)
            );
        } else if (this.typeUser==='employer'){
            this.updateEmployer();
            this.service.addOrUpdateEmployer(this.employer).subscribe(
            employer => { },
            error => console.error('Error creating new employer: ' + error)
            );
        }
        window.history.back();
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
        if (this.reliability){
            this.lifeguard.skills?.push("Confianza")
        }
        if (this.effort){
            this.lifeguard.skills?.push("Esfuerzo")
        }
        if (this.communication){
            this.lifeguard.skills?.push("Comunicación")
        }
        if (this.attitude){
            this.lifeguard.skills?.push("Actitud positiva")
        }
        if (this.problemsResolution){
            this.lifeguard.skills?.push("Resolución de problemas")
        }
        if (this.leadership){
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
    
            fetch(`/availableMail?mail=${mail}`)
                .then((response: Response) => response.json())
                .then((responseObj: { available: boolean }) => {
                    let message: string = responseObj.available ? "Email disponible" : "Email no disponible";
                    let color: string = responseObj.available ? "green" : "red";
    
                    const messageDiv: HTMLElement | null = document.getElementById("mailContent");
                    if (messageDiv) {
                        messageDiv.innerHTML = message;
                        messageDiv.style.color = color;
                    }
                });
        }
    }

    showForm() {
        //if (edit === 'true') {
            //showForm();
        //}

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