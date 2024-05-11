import { Component } from '@angular/core';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import { ViewChild, ElementRef } from '@angular/core';


@Component({
    selector: "login",
    templateUrl: './user-login.component.html'
})
export class UserLoginComponent{
    @ViewChild('passwordInput') passwordInput: ElementRef;
    @ViewChild('mailInput') mailInput: ElementRef;

    constructor(private router:Router, private service: UserService){}

    login(){
        let mail: string = this.mailInput.nativeElement.value;
        let pass: string = this.passwordInput.nativeElement.value;

        this.service.login(mail, pass).subscribe(
            user => this.router.navigate([user.type + "s", user.id])
        );
    }
}