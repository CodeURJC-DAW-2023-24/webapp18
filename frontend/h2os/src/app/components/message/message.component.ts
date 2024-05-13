import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'message',
  templateUrl: './message.component.html',
  styleUrl: './message.component.css'
})
export class MessageComponent {
    message:string;
    back:string;

    constructor(private router: Router) {
        const navigation = this.router.getCurrentNavigation();

        this.message = navigation?.extras.state ? navigation.extras.state['message'] : null;
        this.back = navigation?.extras.state ? navigation.extras.state['back'] : "/";
    }
}