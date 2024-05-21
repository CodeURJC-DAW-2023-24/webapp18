import { Component } from '@angular/core';
import { Location } from '@angular/common'
import { Router } from '@angular/router';

@Component({
  selector: 'message',
  templateUrl: './message.component.html',
  styleUrl: './message.component.css'
})
export class MessageComponent {
    message:string;
    back:string;

    constructor(private router: Router, private location: Location) {
        const navigation = this.router.getCurrentNavigation();

        if (navigation?.extras.state) {
          this.message = navigation.extras.state['message'];
          const back =  navigation.extras.state['back'];
          if (back != "") this.back = back;
        }
        else {
          this.message = "Glu glu";
          this.back = "/";
        }
    }

    goBackPage() {
      this.location.back();
    }
}