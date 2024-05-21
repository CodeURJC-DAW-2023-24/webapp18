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
    styleUrls: ['../../styles/data.css', '../../styles/messages.css', '../../styles/form.css']
})
export class OfferEditComponent {
    id: number;
    offer: Offer
    pools: Pool[];
    dateF: string;
    @ViewChild('description') descripcionInput: ElementRef;
    @ViewChild('journey') journeySelect: ElementRef;
    @ViewChild('date') dateInput: ElementRef;
    @ViewChild('salary') salaryInput: ElementRef;
    errorFlagSalary: boolean
    loaded: boolean;
    constructor(activatedRoute: ActivatedRoute, private service: OfferService, private router: Router) { // Set the permits
        this.loaded =  false;
        activatedRoute.params.subscribe(params => {
            this.id = params['id'];
        });
        this.errorFlagSalary = false
        service.getOffer(this.id).subscribe(
            response => {
                this.offer = response as Offer;
                this.formatDate()
                this.loaded = true
            },
            error => console.error(error)
        );
    }

    editOffer() {
        this.offer.description = this.descripcionInput.nativeElement.value;
        this.offer.type = this.journeySelect.nativeElement.value;
        this.offer.start = this.unformatDate(this.dateInput.nativeElement.value);
        this.offer.salary = this.salaryInput.nativeElement.value;
        if (this.isValid()) {
            this.service.editOffer(this.offer.id, this.offer).subscribe(
                _ => this.router.navigate(['/offers', this.offer.id]),
                _error => console.log("Error al editar la oferta")
            )
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
        let salary2 = this.salaryInput.nativeElement.value;
        if (salary2 < 1300) {
            this.errorFlagSalary = true
            return false
        }
        return true
    }
}