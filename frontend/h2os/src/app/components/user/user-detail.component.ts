import { Component } from '@angular/core';
import { Person } from '../../models/person.model';
import { Lifeguard } from '../../models/lifeguard.model';
import { Employer } from '../../models/employer.model';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { Me } from '../../models/me.model';
import { catchError } from 'rxjs';

@Component({
    selector: "user-detail",
    templateUrl: './user-detail.component.html'
})

export class UserDetailComponent{
    me:Me;
    user: Person;
    lifeguard: Lifeguard;
    employer: Employer;
    edit:boolean;
    typeUser:string;
    admin: boolean;
    editUser:string;
    image:boolean | undefined;
    imageUser: string | undefined;

    constructor(activatedRoute: ActivatedRoute, private router:Router, private userService: UserService) {
        let id = activatedRoute.snapshot.params['id'];
        let type : string | undefined;
        const routeSegments = activatedRoute.snapshot.url;
        this.user = {mail:"",pass:"",roles:[]}
        this.image = false;
        if (routeSegments.length > 0) {
            const firstSegment = routeSegments[0];
            type = firstSegment.path;
        }
        if (id && (type === 'lifeguards')) {
            userService.getLifeguard(id).subscribe(
                response => {this.lifeguard = response as Lifeguard;
                    this.typeUser='lifeguard';
                    this.lifeguardToPerson();
                    this.editUser='/lifeguards/edit/'+this.lifeguard.id
                    this.image = this.lifeguard.imageUser;
                    if (this.image){
                        userService.getLifeguardImage(this.lifeguard).subscribe(
                            response =>{ if(response){
                                const blob = new Blob([response], {type: 'image/jpeg'});
                                this.imageUser = URL.createObjectURL(blob);
                            }else{
                                this.imageUser = undefined;
                            }
                            
                        },
                        error => {this.imageUser = undefined;}
                        );
                    }
                },
                error => console.error(error)
            );
        } else if (id && (type === 'employers')){
            userService.getEmployer(id).subscribe(
              employer => {this.employer = employer
                this.typeUser='employer';
                this.employerToPerson();
                this.editUser='/employers/edit/'+this.employer.id;
                this.image = this.employer.imageCompany;
                if (this.image){
                    userService.getEmployerImage(this.employer).subscribe(
                        response =>{ if(response){
                            const blob = new Blob([response], {type: 'image/jpeg'});
                            this.imageUser = URL.createObjectURL(blob);
                        }else{
                            this.imageUser = undefined;
                        }
                        
                    },
                    error => {this.imageUser = undefined;}
                    );
                }
              },
              error => console.error(error)
            );
        }
    }

    //Obsolet
    showImageLifeguard() {
        return this.lifeguard.imageUser ? '/api/lifeguards' + this.lifeguard.id + '/photoUser' : '/assets/images/noPhotoUser.jpg';
      }

    logout(){
        this.userService.logOut();
        this.router.navigate(['/']);
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