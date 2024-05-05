import { Component, ViewChild } from '@angular/core';
import { Pool } from '../../models/pool.model';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { PoolService } from '../../services/pool.service';

@Component({
    selector: "pool-form",
    templateUrl: './pool-form.component.html'
})

export class PoolFormComponent{
    pool:Pool;
    edit:boolean;

    @ViewChild("file")
    file:any;

    constructor(activatedRoute: ActivatedRoute, private router:Router, private service: PoolService, private httpClient: HttpClient) {
        let id = activatedRoute.snapshot.params['id'];
        let type : string | undefined;
        const routeSegments = activatedRoute.snapshot.url;
        this.edit=false;

        this.pool = { }
        if (id){
            this.edit = true;

        }

        if (routeSegments.length > 0) {
            const firstSegment = routeSegments[0];
            type = firstSegment.path;
        }
        if (id) {
            service.getPool(id).subscribe(
                pool => {
                    this.pool = pool
                    this.edit = true;
                },
                error => console.error(error)
            );
        }
    }

    save() {
        this.service.addOrUpdatePool(this.pool).subscribe(
        (pool:any)=> {
            this.uploadPoolImage(pool);               
        },
        error => {console.error('Error creating new pool: ' + error)
            this.router.navigate(['/'])
            }
        );
    }


      private uploadPoolImage(pool: Pool): void {
        const image = this.file.nativeElement.files[0];
        if (image) {
          let formData = new FormData();
          formData.append("imageFile", image);
          this.service.setPoolPhoto(pool, formData).subscribe(
            response => {
                this.router.navigate(['/']);
            },
            error => {
                alert('Error uploading pool image: ' + error);
                this.router.navigate(['/']);}
          );
        } 
        else {
                this.router.navigate(['/']);
            };
        }

}