import { Component, ElementRef, HostListener, OnInit } from '@angular/core';
import { PaginationService } from '../../services/pagination.service';
import { Offer } from '../../models/offer.model';
import { UserService } from '../../services/user.service';
import { Me } from '../../models/me.model';

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
  canAdd: boolean = false;
  me: Me;

  constructor(
    private service: PaginationService,
    private userService: UserService,
    private elementRef: ElementRef
  ) { }

  ngOnInit(): void {
    this.updateRowElements();
    this.loadOffers();
    this.checkUser();
  }

  private checkUser() {
    this.userService.me().subscribe(
      response => {
        this.me = response as Me;
        this.canAdd = (this.me.type == "e");
      },
      _error => console.log("Error al obtener el usuario")
    );
  }

  loadOffers() {
    this.service.getOffers(this.last_page, this.rowElements).subscribe(
      (offers: Offer[]) => {
        if (!offers) {
          this.hasMore = false;
          return;
        }

        for (let offer of offers) {
          this.service.getOfferImage(offer).subscribe(
            (image) => {
              offer.image = URL.createObjectURL(image);
            },
            (error) => {
              console.log(error);
            }
          );
        }

        this.offers.push(...offers);
      },
      (error) => {
        console.log(error);
        this.hasMore = false;
      }
    );

    this.last_page++;

    this.service.getOffers(this.last_page, this.rowElements).subscribe(
      (offers: Offer[]) => {
        if (!offers)
          this.hasMore = false;
      },
      (error) => {
        console.log(error);
        this.hasMore = false;
      }
    );
  }

  private updateRowElements() {
    const cards = this.elementRef.nativeElement.querySelector('.cards');
    const total = Math.floor(cards.offsetWidth / this.cardWidth);
    this.rowElements = Math.max(2, Math.min(5, total));  // Between 2 and 5 cards
  }

  @HostListener('window:resize', ['$event'])
  onResize() {
    this.updateRowElements();
  }
}