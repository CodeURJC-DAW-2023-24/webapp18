import { Component, ElementRef, HostListener, OnInit } from '@angular/core';
import { PaginationService } from '../../services/pagination.service';
import { Pool } from '../../models/pool.model';
import { UserService } from '../../services/user.service';
import { Me } from '../../models/me.model';

@Component({
  selector: "pools",
  templateUrl: './pools.component.html',
  styleUrl: './cards.css'
})
export class PoolsComponent implements OnInit {
  pools: Pool[] = [];
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
    this.loadPools();
    this.checkUser();
  }

  private checkUser() {
    this.userService.me().subscribe(
      response => {
        this.me = response as Me;
        this.canAdd = (this.me.mail == "admin");
      },
      _error => console.log("Error al obtener el usuario")
    );
  }

  loadPools() {
    this.service.getPools(this.last_page, this.rowElements).subscribe(
      (pools: Pool[]) => {
        if (!pools) {
          this.hasMore = false;
          return;
        }

        for (let pool of pools) {
          this.service.getPoolImage(pool).subscribe(
            (image) => {
              pool.image = URL.createObjectURL(image);
            },
            (error) => {
              console.log(error);
            }
          );
        }

        this.pools.push(...pools);
      },
      (error) => {
        console.log(error);
        this.hasMore = false;
      }
    );

    this.last_page++;

    this.service.getPools(this.last_page, this.rowElements).subscribe(
      (pools: Pool[]) => {
        if (!pools)
          this.hasMore = false;
      },
      (error) => {
        console.log(error);
        this.hasMore = false;
      }
    );
  }

  updateRowElements() {
    const cards = this.elementRef.nativeElement.querySelector('.cards');
    const total = Math.floor(cards.offsetWidth / this.cardWidth);
    this.rowElements = Math.max(2, Math.min(5, total));  // Between 2 and 5 cards
  }

  @HostListener('window:resize', ['$event'])
  onResize() {
    this.updateRowElements();
  }
}