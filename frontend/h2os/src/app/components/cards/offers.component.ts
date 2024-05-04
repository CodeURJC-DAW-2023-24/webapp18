import { Component, ElementRef, HostListener, OnInit } from '@angular/core';
import { OffersService } from '../../services/offers.service';
import { Offer } from '../../models/offer.model';

@Component({
  selector: "offers",
  templateUrl: './offers.component.html',
  styleUrl: './cards.css'
})

export class OffersComponent implements OnInit {
  offers: Offer[] = [];
  last_page: number = 0;
  hasMore: boolean = true;
  cardWidth: number = 250 + 2 * 20;
  rowElements: number;

  constructor(
    private offersService: OffersService,
    private elementRef: ElementRef
  ) { }

  ngOnInit(): void {
    this.updateRowElements();
    this.loadOffers();
  }

  loadOffers() {
    this.offersService.getOffers(this.last_page, this.rowElements).subscribe(
      (offers: Offer[]) => {
        if (offers) {
          this.offers.push(...offers);
          for (let offer of this.offers) {
            this.offersService.getImage(offer).subscribe(
              (image) => {
                offer.image = URL.createObjectURL(image);
              },
              (error) => {
                console.log(error);
              }
            );
          }
          this.last_page++;
          if (offers.length < this.rowElements) {
            this.hasMore = false;
          }
        } else {
          this.hasMore = false;
        }
      },
      (error) => {
        console.log(error);
        this.hasMore = false;
      }
    );
  }

  updateRowElements() {
    const cards = this.elementRef.nativeElement.querySelector('.cards');
    this.rowElements = Math.floor(cards.offsetWidth / this.cardWidth);
  }

  @HostListener('window:resize', ['$event'])
  onResize() {
    this.updateRowElements();
  }
}