import { Component } from '@angular/core';
import { UserService } from '../../services/user.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ViewChild, ElementRef } from '@angular/core';


@Component({
    selector: "book",
    templateUrl: './user-login.component.html'
})

export class UserLoginComponent{
    @ViewChild('passwordInput') passwordInput: ElementRef;
    @ViewChild('mailInput') mailInput: ElementRef;

    constructor(activatedRoute: ActivatedRoute, private router:Router, private service: UserService){}

    login(){       
        let mail: string;
        let pass: string;
        mail = this.mailInput.nativeElement.value;
        pass = this.passwordInput.nativeElement.value;
        this.service.login(mail, pass);
        this.router.navigate(['/']);
    }
}