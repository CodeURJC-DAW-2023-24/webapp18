import { Component } from '@angular/core';
import { Person } from '../../models/person.model';
import { Lifeguard } from '../../models/lifeguard.model';
import { Employer } from '../../models/employer.model';
import { Offer } from '../../models/offer.model';
import { Pool } from '../../models/pool.model';
import { Me } from '../../models/me.model';
import { ActivatedRoute, Router } from '@angular/router';
import { OfferService } from '../../services/offer.service';
import { CommonModule, NgIf } from '@angular/common';
import { UserService } from '../../services/user.service';
import { Applied } from '../../models/applied.model';

@Component({
    selector: "offer",
    templateUrl: './offer.component.html',
    styleUrls:['./offer.data.css','./offer.messages.css']
})

export class OfferComponent{
    me: Me;
    user: Person;
    lifeguard: Lifeguard;
    owner: Employer;
    edit:boolean;
    typeUser:string;
    admin: boolean;
    offer: Offer;
    pool: String;
    hasPhoto: boolean;
    canApply: boolean;
    poolName: string;
    poolPhoto: string;
    poolID: number;
    applied: boolean;
    selected: string | undefined;
    appliedLg: string[];
    appliedLgDesc: string[];
    photoURL: string;
    constructor(activatedRoute: ActivatedRoute, private service: OfferService, private userService: UserService){ // Set the permits
        let id = activatedRoute.snapshot.params['id'];
        id = 1;
        this.applied = false;
        this.appliedLg = []
        this.appliedLgDesc= []
        service.getOffer(id).subscribe(
            response => {
                this.offer = response as Offer;
                this.hasPhoto = false;
                this.poolName = this.offer.poolName;
                this.poolID = this.offer.poolID;
                service.getOfferPhoto(id).subscribe(
                    response =>{ if(response){
                        const blob = new Blob([response], { type: 'image/jpeg' })
                        this.photoURL = URL.createObjectURL(blob)
                        this.hasPhoto = true;

                        userService.me().subscribe(
                            response => {
                                this.me = response as Me
                                console.log(this.offer);
                                console.log("hola")
                                console.log(this.me);
                                console.log(this.me.mail);
                                this.edit = (this.me.mail=="admin" || this.me.mail==this.offer.employer)
                                this.canApply = this.me.type=="lg";
                                userService.getLifeguardOffers(this.me.id).subscribe(
                                    response => {
                                        console.log(response)
                                        let lista = response as Offer[];
        
                                        for(let offerA of lista){
                                            if(offerA.id==this.offer.id){
                                                this.canApply = false
                                            }
                                        }
                        
                                    },
                                    error =>{ 
                                        console.log("Error al pedir las ofertas propuestas")}
                                );
                                
                            },
                            error => {
                                this.me.mail = "-1"
                                this.me.type = "-1"
                            }
                        );
                        
                        
                    }
                    },
                    error =>{
                        console.log("Error al cargar la foto")
                    });
                
            },
            error => console.error(error)
        );

        
        
            
        
    }

    showApplied(id: number|undefined){
        let mapa: Applied
        this.service.getApplied(id).subscribe(
            response => {
                console.log(response)
                mapa = response as Applied;

                if(mapa.Seleccionado!== null && mapa && mapa.Seleccionado){
                    this.selected = mapa.Seleccionado[0];
                }
                else this.selected = "-1";
                const intermediate2 = mapa.Propuestos;
                console.log(intermediate2)
                if(intermediate2){
                    this.appliedLg = intermediate2;
                }
                const intermediate3 = mapa.Descripciones;
                if(intermediate3){
                    this.appliedLgDesc = intermediate3;
                }
                this.applied = true;

            },
            error => console.error(error)
        );
    }
    deleteOffer(id: number|undefined){
        this.service.deleteOffer(id);
    }
    apply(id: number|undefined){
        this.service.applyToOffer(id);
        this.canApply = false;
    }
    setLifeguard(idOffer: number|undefined, idLg: number|undefined){
        this.service.setLifeguard(idOffer,idLg);
        this.applied = this.service.unSelectLifeguard(idOffer);
        this.applied = false;

    }
    unSelectLifeguard(idOffer: number|undefined){
        this.service.unSelectLifeguard(idOffer);
        this.applied = false;

    }
    showApplied2(id: number|undefined){
        let mapa: Map<string, string[]>
        this.selected = "s1";
        this.appliedLg = ["s1","s2","s3"];
        this.applied = true;
    }
}