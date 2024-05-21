import { Component } from '@angular/core';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import { ViewChild, ElementRef } from '@angular/core';
import { Me } from '../../models/me.model';


@Component({
    selector: "login",
    templateUrl: './user-login.component.html',
    styleUrl: '../../styles/form.css'
})
export class UserLoginComponent{
    @ViewChild('passwordInput') passwordInput: ElementRef;
    @ViewChild('mailInput') mailInput: ElementRef;

    constructor(private router:Router, private service: UserService){
        this.service.me().subscribe(
            response => {
                const me = response as Me;
                if (me.type === 'lg')
                    this.router.navigate(['lifeguards', me.id]);
                else
                    this.router.navigate(['employers', me.id]);
            },
            _error => console.log("Not logged in")
        );
    }

    login(){
        let mail: string = this.mailInput.nativeElement.value;
        let pass: string = this.passwordInput.nativeElement.value;

        this.service.login(mail, pass).subscribe(
            user => this.router.navigate([user.type + "s", user.id])
        );
    }
}