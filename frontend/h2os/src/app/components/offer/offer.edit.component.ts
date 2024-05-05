import { Component, ElementRef, ViewChild } from '@angular/core';
import { Person } from '../../models/person.model';
import { Lifeguard } from '../../models/lifeguard.model';
import { Employer } from '../../models/employer.model';
import { Offer } from '../../models/offer.model';
import { Pool } from '../../models/pool.model';
import { ActivatedRoute } from '@angular/router';
import { OfferService } from '../../services/offer.service';
import { CommonModule, NgIf } from '@angular/common';
import { Router } from '@angular/router';

@Component({
    selector: "offerEdit",
    templateUrl: './offer.edit.component.html',
    styleUrls: ['./offer.data.css', './offer.messages.css']
})

export class OfferEditComponent {
    offer: Offer
    pools: Pool[];
    dateF: string;
    @ViewChild('description') descripcionInput: ElementRef;
    @ViewChild('journey') journeySelect: ElementRef;
    @ViewChild('date') dateInput: ElementRef;
    @ViewChild('salary') salaryInput: ElementRef;
    @ViewChild('poolN') poolInput: ElementRef;
    errorFlagSalary: boolean

    constructor(activatedRoute: ActivatedRoute, private service: OfferService, private router: Router) { // Set the permits
        let id = activatedRoute.snapshot.params['id'];
        this.errorFlagSalary = false
        service.getOffer(1).subscribe(
            response => {
                this.offer = response as Offer;
                this.formatDate()
            },
            error => console.error(error)
        );
        console.log(this.offer);
    }

    editOffer() {
        console.log("Oferta antes")
        console.log(JSON.stringify(this.offer))
        this.offer.description = this.descripcionInput.nativeElement.value;
        this.offer.type = this.journeySelect.nativeElement.value;
        this.offer.start = this.unformatDate(this.dateInput.nativeElement.value);
        this.offer.poolID = this.poolInput.nativeElement.value;
        this.offer.salary = this.salaryInput.nativeElement.value;
        if (this.isValid()) {
            console.log("Oferta despues")
            console.log(JSON.stringify(this.offer))
            this.service.editOffer(this.offer.id, this.offer)
        }
    }

    formatDate() {
        if (this.offer.start == undefined) this.offer.start = "11/11/1111"
        const parts = this.offer.start.split('/');
        this.dateF = `${parts[2]}-${parts[1]}-${parts[0]}`;
    }

    unformatDate(date: string): string {
        const parts = date.split('-');
        return parts[2] + '/' + parts[1] + '/' + parts[0];
    }

    isValid() {
        let desc = this.descripcionInput.nativeElement.value;
        let type2 = this.journeySelect.nativeElement.value;
        let date2 = this.unformatDate(this.dateInput.nativeElement.value);
        let id = this.poolInput.nativeElement.value;
        let salary2 = this.salaryInput.nativeElement.value;
        if (salary2 < 1300) {
            this.errorFlagSalary = true
            return false
        }
        return true
    }
}