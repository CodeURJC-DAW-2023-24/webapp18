import { Component } from '@angular/core';
import { Person } from '../../models/person.model';
import { Lifeguard } from '../../models/lifeguard.model';
import { Employer } from '../../models/employer.model';

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

    checkAge(){}
    checkMail(){}
    checkPassword(){}
    checkPhone(){}
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