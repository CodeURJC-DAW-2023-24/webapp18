import { Component } from '@angular/core';
import { Person } from '../../models/person.model';
import { Lifeguard } from '../../models/lifeguard.model';
import { Employer } from '../../models/employer.model';
import { Pool } from '../../models/pool.model';
import { Me } from '../../models/me.model';
import { ActivatedRoute } from '@angular/router';
import { PoolService } from '../../services/pool.service';
import { UserService } from '../../services/user.service';

@Component({
    selector: "pool",
    templateUrl: './pool.component.html',
    styleUrls: ['../../styles/data.css', '../../styles/messages.css']
})
export class PoolComponent {
    me: Me;
    admin: boolean;
    logged: boolean;
    id: number;
    pool: Pool;
    name: string | undefined;
    direction: string | undefined;
    scheduleStart: string | undefined;
    scheduleEnd: string | undefined;
    capacity: number | undefined;
    description: string | undefined;
    photoURL: string;

    constructor(activatedRoute: ActivatedRoute, private service: PoolService, private userService: UserService) {
        activatedRoute.params.subscribe(params => {
            this.id = params['id'];
        });
        service.getPool(this.id).subscribe(
            response => {
                this.pool = response as Pool;
                this.name = this.pool.name;
                this.direction = this.pool.direction;
                this.scheduleStart = this.pool.scheduleStart;
                this.scheduleEnd = this.pool.scheduleEnd;
                this.capacity = this.pool.capacity;
                this.description = this.pool.description;
            },
            error => console.error(error)
        );

        service.getPoolPhoto(this.id).subscribe(
            response => {
                if (response) {
                    const blob = new Blob([response], { type: 'image/jpeg' })
                    this.photoURL = URL.createObjectURL(blob)
                }
            },
            error => {
                console.log("Error al cargar la foto")
            }
        );

        userService.me().subscribe(
            response => {
                this.me = response as Me
                this.admin = (this.me.mail == "admin");
                this.logged = true;
            },
            error => {
                this.admin = false;
                this.logged = false;
            }
        );
    }

    deletePool(id: number | undefined) {
        this.service.deletePool(id);
    }

    showComments(id: number | undefined) {
        // TODO
    }
}