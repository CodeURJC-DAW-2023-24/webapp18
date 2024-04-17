import { Component } from '@angular/core';
import { Person } from '../../models/person.model';
import { Lifeguard } from '../../models/lifeguard.model';
import { Employer } from '../../models/employer.model';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../../services/user.service';

@Component({
    selector: "user-detail",
    templateUrl: './user-detail.component.html'
})

export class UserDetailComponent{
    user: Person;
    lifeguard: Lifeguard;
    employer: Employer;
    edit:boolean;
    typeUser:string;
    admin: boolean;

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
            this.typeUser='lifeguard';
            this.lifeguardToPerson();
        } else if (id && (type === 'employers')){
            service.getEmployer(id).subscribe(
              employer => this.employer = employer,
              error => console.error(error)
            );
            this.typeUser='employer';
            this.employerToPerson();
        }
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

}