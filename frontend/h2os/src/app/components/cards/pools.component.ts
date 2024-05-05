import { Component, ElementRef, HostListener, OnInit } from '@angular/core';
import { PoolsService } from '../../services/pools.service';
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
    private poolsService: PoolsService,
    private elementRef: ElementRef
  ) { }

  ngOnInit(): void {
    this.updateRowElements();
    this.loadPools();
  }

  loadPools() {
    this.poolsService.getPools(this.last_page, this.rowElements).subscribe(
      (pools: Pool[]) => {
        if (pools) {
          this.pools.push(...pools);
          for (let pool of this.pools) {
            this.poolsService.getImage(pool).subscribe(
              (image) => {
                pool.image = URL.createObjectURL(image);
              },
              (error) => {
                console.log(error);
              }
            );
          }
          this.last_page++;
          if (pools.length < this.rowElements) {
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