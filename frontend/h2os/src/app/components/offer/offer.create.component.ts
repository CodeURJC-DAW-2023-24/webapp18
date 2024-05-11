import { Component, ElementRef, ViewChild } from '@angular/core';
import { Person } from '../../models/person.model';
import { Lifeguard } from '../../models/lifeguard.model';
import { Employer } from '../../models/employer.model';
import { Offer } from '../../models/offer.model';
import { Pool } from '../../models/pool.model';
import { ActivatedRoute, Router } from '@angular/router';
import { OfferService } from '../../services/offer.service';
import { CommonModule, NgIf } from '@angular/common';
import { Me } from '../../models/me.model';
import { UserService } from '../../services/user.service';
import { PaginationService } from '../../services/pagination.service';

@Component({
    selector: "offerEdit",
    templateUrl: './offer.create.component.html',
    styleUrls: ['../../styles/data.css', '../../styles/messages.css', '../../styles/form.css']
})
export class OfferCreateComponent {
    offer: Offer
    pools: Pool[];
    dateF: string;
    me: Me;
    @ViewChild('description') descripcionInput: ElementRef;
    @ViewChild('journey') journeySelect: ElementRef;
    @ViewChild('date') dateInput: ElementRef;
    @ViewChild('salary') salaryInput: ElementRef;
    @ViewChild('poolN') poolInput: ElementRef;
    errorFlagSalary: boolean;
    errorFlagDate: boolean;
    constructor(activatedRoute: ActivatedRoute, private service: OfferService, private userService: UserService, private pageService: PaginationService, private router: Router) {
        this.pools = []
        this.loadPools()
        userService.me().subscribe(
            response => {
                this.me = response as Me
                
            },
            error => {
                console.log("Auth error")
            }
        );
    }

    saveOffer() {
        this.offer = new Offer();
        this.offer.description = this.descripcionInput.nativeElement.value;
        this.offer.employer = this.me.mail;
        this.offer.type = this.journeySelect.nativeElement.value;
        this.offer.start = this.unformatDate(this.dateInput.nativeElement.value);
        // this.offer.poolID = this.poolInput.nativeElement.value;
        this.offer.poolID = this.pools[this.poolInput.nativeElement.value].id;
        this.offer.salary = this.salaryInput.nativeElement.value;
        if (this.isValid()) {
            this.service.newOffer(this.offer).subscribe(
                response => {
                    this.offer = response as Offer;
                    this.router.navigate(['/offers', this.offer.id]);
                },
                _error => console.log("Error al guardar la nueva oferta")
            );
        }
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

        if (!this.isValidDate(date2)) {
            this.errorFlagDate = true;
            return false
        }
        return true
    }

    isValidDate(date: String) {
        const parts = date.split('/');
        if (parts[0] == "undefined" || parts[1] == "undefined" || parts[2] == "undefined") return false;
        return true;
    }

    loadPools() {
        this.pageService.getPools(0, 100).subscribe(
          (pools: Pool[]) => {
            if (!pools) {
        
              return;
            }
    
            for (let pool of pools) {
              this.pools.push(pool)
            }
            
          }
     
        );
      }
}
