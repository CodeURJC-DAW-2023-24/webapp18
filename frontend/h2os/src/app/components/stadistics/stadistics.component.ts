import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { HttpClient } from '@angular/common/http';
import { Person } from '../../models/person.model';
import { Lifeguard } from '../../models/lifeguard.model';


@Component({
    selector: "stadistics",
    templateUrl: './stadistics.component.html',
})
export class StadisticsComponent{

    constructor(private activatedRoute: ActivatedRoute, private router:Router, private service:UserService, private httpClient: HttpClient){}

    user: Person;
    lifeguard: Lifeguard;
    edit: boolean;
    typeUser: string;

    reliability: boolean;
    effort: boolean;
    communication: boolean;
    attitude: boolean;
    problemsResolution: boolean;
    leadership: boolean;


    title: string;
    type: string;
    data: (string | number)[][];
    columnNames: string[];
    public options: { title: string; colors: string[]; is3D: boolean; };
    width: number;
    height: number;

    ngOnInit(): void {
        this.service.getLifeguard(0).subscribe(
            lifeguard => {
                this.lifeguard = lifeguard
                this.updateSkills();
                this.lifeguardToPerson();
                this.edit = true;
                this.typeUser = 'lifeguard'
            },
            error => console.error(error)
        );
        this.lifeguardToPerson();
        this.title = 'Lifeguard stadistics';
        this.type = 'PieChart';
        this.data = [
          ['Confianza',     11],
          ['Esfuerzo',      2],
          ['Comunicación',  2],
          ['Actitud positiva', 2],
          ['Resolucion',    7],
          ['Liderazgo',    7]

        ];
        /** 
        this.options = {
            title: 'Daily Activities',
            is3D: true,
            colors: ['#33440e'],

        };
        */
        this.width = 550;
        this.height = 400;
    }

    private updateSkills() {
        this.reliability = this.lifeguard.skills.includes("Confianza");
        this.effort = this.lifeguard.skills.includes("Esfuerzo");
        this.communication = this.lifeguard.skills.includes("Comunicacion");
        this.attitude = this.lifeguard.skills.includes("Actitud positiva");
        this.problemsResolution = this.lifeguard.skills.includes("Resolución de problemas");
        this.leadership = this.lifeguard.skills.includes("Liderazgo");
    }

    private lifeguardToPerson() {
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

}
