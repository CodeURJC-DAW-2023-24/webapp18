import { Component, ElementRef, ViewChild } from '@angular/core';
import { Person } from '../../models/person.model';
import { Lifeguard } from '../../models/lifeguard.model';
import { Employer } from '../../models/employer.model';
import { Offer } from '../../models/offer.model';
import { Pool } from '../../models/pool.model';
import { ActivatedRoute } from '@angular/router';
import { OfferService } from '../../services/offer.service';
import { CommonModule, NgIf } from '@angular/common';
import { Me } from '../../models/me.model';
import { UserService } from '../../services/user.service';

@Component({
    selector: "offerEdit",
    templateUrl: './offer.create.component.html',
    styleUrls:['./offer.data.css','./offer.messages.css']
})

export class OfferCreateComponent{
   offer:Offer
   pools: string[];
   dateF: string;
   me: Me;
   @ViewChild('description') descripcionInput: ElementRef;
   @ViewChild('journey') journeySelect: ElementRef;
   @ViewChild('date') dateInput: ElementRef;
   @ViewChild('salary') salaryInput: ElementRef;
   @ViewChild('poolN') poolInput: ElementRef;
   errorFlagSalary: boolean
    constructor(activatedRoute: ActivatedRoute, private service: OfferService, private userService: UserService){ 
        this.pools = ["Pisci", "Poya y webos"];
        userService.me().subscribe(
            response => {
                this.me = response as Me
                
                
                
            },
            error => {
                console.log("Auth error")
            }
        );
       
    }
    saveOffer(){
        this.offer = new Offer();
        console.log(JSON.stringify(this.offer))
        this.offer.description = this.descripcionInput.nativeElement.value;
        this.offer.employer = this.me.mail;
        this.offer.type = this.journeySelect.nativeElement.value;
        this.offer.start = this.unformatDate(this.dateInput.nativeElement.value);
       // this.offer.poolID = this.poolInput.nativeElement.value;
       this.offer.poolID = 1;
        this.offer.salary = this.salaryInput.nativeElement.value;
        console.log(JSON.stringify(this.offer))
        if (this.isValid()){
        console.log("Oferta despues")
        console.log(JSON.stringify(this.offer))
        this.service.newOffer(this.offer)
        }
    }

   
    unformatDate(date: string): string {
        const parts = date.split('-');
        return parts[2]+'/'+parts[1]+'/'+parts[0];
      }
    isValid(){
        let desc = this.descripcionInput.nativeElement.value;
        let type2 = this.journeySelect.nativeElement.value;
        let date2 = this.unformatDate(this.dateInput.nativeElement.value);
        let id = this.poolInput.nativeElement.value;
        let salary2 = this.salaryInput.nativeElement.value;
        if (salary2<1300){
            this.errorFlagSalary = true
            return false
        }


        return true
    }
   
}