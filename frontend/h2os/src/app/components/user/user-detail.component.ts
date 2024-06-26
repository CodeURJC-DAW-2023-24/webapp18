import { Component } from '@angular/core';
import { Person } from '../../models/person.model';
import { Lifeguard } from '../../models/lifeguard.model';
import { Employer } from '../../models/employer.model';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { Me } from '../../models/me.model';
import { catchError } from 'rxjs';
import { Offer } from '../../models/offer.model';
import { MessageService } from '../../services/message.service';
import { OfferService } from '../../services/offer.service';

@Component({
    selector: "user-detail",
    templateUrl: './user-detail.component.html',
    styleUrls: ['../../styles/data.css', '../../styles/profile.css']
})
export class UserDetailComponent {
    me:Me;
    user: Person;
    lifeguard: Lifeguard;
    employer: Employer;
    edit:boolean;
    typeUser:string;
    admin: boolean;
    otherUser: boolean;
    editUser:string;
    image:boolean | undefined;
    imageUser: string | undefined;
    offers: Offer[] = [];
    offersAccepted: Offer[] = [];

    constructor(activatedRoute: ActivatedRoute,
        private router:Router,
        private userService: UserService,
        private messageService: MessageService,
        private offerService: OfferService) {

        let id = activatedRoute.snapshot.params['id'];
        let type : string | undefined;
        const routeSegments = activatedRoute.snapshot.url;
        this.user = {mail:"",pass:"",roles:[]}
        this.image = false;
        this.otherUser = true;
        if (routeSegments.length > 0) {
            const firstSegment = routeSegments[0];
            type = firstSegment.path;
        }
        if (id && (type === 'lifeguards')) {
            this.userService.getOffersAccepted(id).subscribe(
                (response:any)=>{
                    this.offersAccepted = response;
                },
                error=>{console.error(error)}
            );
            userService.getLifeguard(id).subscribe(
                response => {this.lifeguard = response as Lifeguard;
                    this.typeUser='lifeguard';
                    this.lifeguardToPerson();
                    this.userService.me().subscribe(
                        (response:any)=>{
                            this.otherUser = response.mail !== this.lifeguard.mail;
                            this.admin = response.mail === "admin";
                            },
                        (error:any) => {}
                    );                                     
                    this.editUser='/lifeguards/edit/'+this.lifeguard.id
                    this.image = this.lifeguard.imageUser;
                    userService.getLifeguardOffers(id).subscribe(
                        (response:any) => {
                            this.offers = response;
                        },
                        (error:any) =>{}
                    )
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
                error => this.messageService.showFatalError("Usuario no encontrado")
            );
        } else if (id && (type === 'employers')){
            userService.getEmployer(id).subscribe(
              employer => {this.employer = employer
                this.typeUser='employer';
                this.employerToPerson();
                this.editUser='/employers/edit/'+this.employer.id;
                this.image = this.employer.imageCompany;
                this.userService.me().subscribe(
                    (response:any)=>{
                        this.otherUser = response.mail !== this.employer.mail;
                        this.admin = response.mail === "admin";
                        },
                    (error:any) => {}
                ); 
                userService.getEmployerOffers(id).subscribe(
                    (response:any) => {
                        this.offers = response;
                    },
                    (error:any) =>{}
                )
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
              error => this.messageService.showFatalError("Usuario no encontrado")
            );
        }
    }

    offerAccepted(offer: Offer):boolean|void{
        let check = false
            for (let o of this.offersAccepted){
                if (o.id === offer.id){
                    check = true
                }
            }
        return check;
    }

    deleteOffer(id: number | undefined) {
        this.offerService.deleteOffer(id).subscribe(
            _ => this.router.navigate(['/login']),
            _error => console.log("Error al borrar la oferta")
        );
    }

    withdraw(id: number | undefined) {
        this.offerService.withdrawApplication(id).subscribe(
            _ => this.router.navigate(['/login']),
            _error => console.log("Error al retirar la solicitud")
        );
    }

    //Obsolet
    showImageLifeguard() {
        return this.lifeguard.imageUser ? '/api/lifeguards' + this.lifeguard.id + '/photoUser' : '/assets/images/noPhotoUser.jpg';
    }

    deleteLifeguard(){
        this.userService.deleteLifeguard(this.lifeguard).subscribe(
            _ => this.router.navigate(['/']),
            error => console.error(error)
        )
    }

    deleteEmployer(){
        this.userService.deleteEmployer(this.employer).subscribe(
            _ => this.router.navigate(['/']),
            error => console.error(error)
        )
    }

    logout(){
        this.userService.logout().subscribe(
            _ => this.router.navigate(['login']),
            error => console.error(error)
        );
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