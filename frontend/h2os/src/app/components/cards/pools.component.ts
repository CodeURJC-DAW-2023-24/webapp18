import { Component, ElementRef, HostListener, OnInit } from '@angular/core';
import { PaginationService } from '../../services/pagination.service';
import { Pool } from '../../models/pool.model';

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

  constructor(
    private service: PaginationService,
    private elementRef: ElementRef
  ) { }

  ngOnInit(): void {
    this.updateRowElements();
    this.loadPools();
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

        if (pools.length < this.rowElements)
          this.hasMore = false;
      },
      (error) => {
        console.log(error);
        this.hasMore = false;
      }
    );

    this.last_page++;
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