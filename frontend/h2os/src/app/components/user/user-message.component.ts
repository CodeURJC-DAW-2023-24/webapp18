import { Component, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'user-message',
  templateUrl: './user-message.component.html',
})
export class UserMessageComponent {
    constructor(private route: ActivatedRoute) {}
    message:string;

    ngOnInit() {
        this.route.params.subscribe(params => {
            this.message = params['message'];
        });
    }
}