import { Component, ElementRef } from '@angular/core';
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
    styleUrls: ['../../styles/data.css', '../../styles/messages.css']
})
export class OfferComponent {
    me: Me;
    user: Person;
    lifeguard: Lifeguard;
    owner: Employer;
    edit: boolean;
    typeUser: string;
    admin: boolean;
    id: number;
    offer: Offer;
    pool: String;
    canApply: boolean;
    canWithdraw: boolean;
    poolName: string | undefined;
    poolPhoto: string;
    poolID: number | undefined;
    applied: boolean;
    selected: string | undefined;
    appliedLg: string[];
    appliedLgDesc: string[];
    photoURL: string;
    lgIds: string[];
    offerLoaded: boolean
    constructor(activatedRoute: ActivatedRoute, private router: Router, private service: OfferService, private userService: UserService) { // Set the permits
        this.offerLoaded = false;
        activatedRoute.params.subscribe(params => {
            this.id = params['id'];
        });
        this.applied = false;
        this.appliedLg = []
        this.appliedLgDesc = []
        this.lgIds = []
        service.getOffer(this.id).subscribe(
            response => {
                this.offer = response as Offer;
                this.poolName = this.offer.poolName;
                this.poolID = this.offer.poolID;
                service.getOfferPhoto(this.id).subscribe(
                    response => {
                        if (response) {
                            const blob = new Blob([response], { type: 'image/jpeg' })
                            this.photoURL = URL.createObjectURL(blob)
                            this.offerLoaded = true;
                            userService.me().subscribe(
                                response => {
                                    this.me = response as Me
                                    this.edit = (this.me.mail == "admin" || this.me.mail == this.offer.employer)
                                    const lifeguard = this.me.type == "lg";
                                    if (lifeguard) {
                                        this.canApply = true;
                                        userService.getLifeguardOffers(this.me.id).subscribe(
                                            response => {
                                                let lista = response as Offer[];

                                                for (let offerA of lista) {
                                                    if (offerA.id == this.offer.id) {
                                                        this.canApply = false
                                                        this.canWithdraw = true
                                                    }
                                                }
                                            },
                                            error => {
                                                console.log("Error al pedir las ofertas propuestas")
                                            }
                                        );
                                    }
                                },
                                error => {
                                    this.me.mail = "-1"
                                    this.me.type = "-1"
                                }
                            );
                        }
                    },
                    error => {
                        console.log("Error al cargar la foto")
                    });
            },
            error => console.error(error)
        );
    }

    showApplied(id: number | undefined) {
        let mapa: Applied
        this.service.getApplied(id).subscribe(
            response => {
                mapa = response as Applied;

                if (mapa.Seleccionado !== null && mapa && mapa.Seleccionado) {
                    this.selected = mapa.Seleccionado[0];
                }
                else this.selected = "-1";
                const intermediate2 = mapa.Propuestos;
                if (intermediate2) {
                    this.appliedLg = intermediate2;
                }
                const intermediate3 = mapa.Descripciones;
                if (intermediate3) {
                    this.appliedLgDesc = intermediate3;
                }
                const intermediate4 = mapa.Ids;
                if (intermediate4) {
                    this.lgIds = intermediate4;
                }
                this.applied = true;
            },
            error => console.error(error)
        );
    }

    deleteOffer(id: number | undefined) {
        this.service.deleteOffer(id).subscribe(
            _ => this.router.navigate(['/offers']),
            _error => console.log("Error al borrar la oferta")
        );
    }

    apply(id: number | undefined) {
        this.service.applyToOffer(id);
        this.canApply = false;
        this.canWithdraw = true;
    }

    withdraw(id: number | undefined) {
        this.service.withdrawApplication(id).subscribe(
            _ => {
                this.canApply = true;
                this.canWithdraw = false;
            },
            _error => console.log("Error al retirar la solicitud")
        );
    }

    setLifeguard(idOffer: number | undefined, idLg: number | undefined) {
        this.service.unselectLifeguard(idOffer);
        this.service.setLifeguard(idOffer, idLg);
        this.applied = false;
    }

    unselectLifeguard(idOffer: number | undefined) {
        this.service.unselectLifeguard(idOffer).subscribe(
            _ => this.applied = false,
            _error => console.log("Error al deseleccionar al socorrista")
        );
    }

    showApplied2(id: number | undefined) {
        let mapa: Map<string, string[]>
        this.selected = "s1";
        this.appliedLg = ["s1", "s2", "s3"];
        this.applied = true;
    }
}