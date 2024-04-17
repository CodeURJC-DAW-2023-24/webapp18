import { Component } from '@angular/core';
import { Person } from '../../models/person.model';
import { Lifeguard } from '../../models/lifeguard.model';
import { Employer } from '../../models/employer.model';
import { Offer } from '../../models/offer.model';
import { Pool } from '../../models/pool.model';
import { ActivatedRoute } from '@angular/router';
import { OfferService } from '../../services/offer.service';

@Component({
    selector: "offer",
    templateUrl: './offer.component.html'
})

export class OfferComponent{
    user: Person;
    lifeguard: Lifeguard;
    owner: Employer;
    edit:boolean;
    typeUser:string;
    admin: boolean;
    offer: Offer;
    pool: Pool;
    hasPhoto: boolean;
    canApply: boolean;
    constructor(activatedRoute: ActivatedRoute, private service: OfferService){ // Set the permits
        let id = activatedRoute.snapshot.params['id'];
        service.getOffer(id).subscribe(
            offer => this.offer = offer,
            error => console.error(error)
        );
        this.hasPhoto = false;
        this.pool = this.offer.pool;
        this.edit = false
        this.canApply = false;
    }

    showApplyed(){
        console.log("GG")
    }
    deleteOffer(){
        console.log("GG")
    }
}