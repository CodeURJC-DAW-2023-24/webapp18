import { Component } from '@angular/core';
import { Person } from '../../models/person.model';
import { Lifeguard } from '../../models/lifeguard.model';
import { Employer } from '../../models/employer.model';
import { Offer } from '../../models/offer.model';
import { Pool } from '../../models/pool.model';
import { ActivatedRoute } from '@angular/router';
import { OfferService } from '../../services/offer.service';
import { CommonModule, NgIf } from '@angular/common';

@Component({
    selector: "offerEdit",
    templateUrl: './offer.edit.component.html',
    styleUrls:['./offer.data.css','./offer.messages.css']
})

export class OfferEditComponent{
   offer:Offer
    constructor(activatedRoute: ActivatedRoute, private service: OfferService){ // Set the permits
        let id = activatedRoute.snapshot.params['id'];
        service.getOffer(1).subscribe(
            response => this.offer = response as Offer,
            error => console.error(error)
        );
        console.log(this.offer);
       
    }

   
}