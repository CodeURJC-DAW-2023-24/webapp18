import { Component, ElementRef, ViewChild } from '@angular/core';
import { Person } from '../../models/person.model';
import { Lifeguard } from '../../models/lifeguard.model';
import { Employer } from '../../models/employer.model';
import { Pool } from '../../models/pool.model';
import { Me } from '../../models/me.model';
import { ActivatedRoute, Router } from '@angular/router';
import { PoolService } from '../../services/pool.service';
import { UserService } from '../../services/user.service';
import { Message } from '../../models/message.model';


@Component({
    selector: "pool",
    templateUrl: './pool.component.html',
    styleUrls: ['../../styles/data.css', '../../styles/messages.css']
})
export class PoolComponent {
    authors: String[];
    messages: String[];
    owner: boolean[];
    messagesIDs: number[];
    showMessagesFlag: boolean;
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
    @ViewChild('bodyMessage') bodyMessage: ElementRef;

    constructor(activatedRoute: ActivatedRoute, private router: Router, private service: PoolService, private userService: UserService) {
        this.showMessagesFlag = true;
        this.authors = []
        this.owner = []
        this.messages = []
        this.messagesIDs = []
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
        this.service.deletePool(id).subscribe(
            _ => this.router.navigate(['/pools']),
            error => console.error("Error deleting the pool: " + error)
        );
    }

    showMessages(){
        let map: Message[];
        this.showMessagesFlag = false;
        this.service.getPoolMessages(this.id).subscribe(
            response => {
                map = response as Message[];

                for (let i = 0; i < map.length; i++) {
                    let msg = map[i]
                    this.authors.push(msg.author);
                    this.messages.push(msg.body);
                    this.owner.push(msg.author==this.me.mail);
                    this.messagesIDs.push(msg.id);
                  }


            },
            error => {
                console.log("Failed to load pool messages.")
            }
        );
    }

    deleteComment(idM: number){
        this.showMessagesFlag = true;
      
        this.service.deleteMessage(this.id,this.messagesIDs[idM])
        this.authors = []
        this.owner = []
        this.messages = []
        this.messagesIDs = []
    }

    addComment(){
       
        this.showMessagesFlag = true;
        let msg = this.bodyMessage.nativeElement.value;
        this.bodyMessage.nativeElement.value = "";
        let form = new Message(msg);
        this.service.newMessage(this.id, form)
        this.authors = []
        this.owner = []
        this.messages = []
        this.messagesIDs = []
        this.showMessages()
    }

}