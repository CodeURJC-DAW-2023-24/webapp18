import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { Me } from '../../models/me.model';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {

  constructor(private activatedRoute: ActivatedRoute, private userService: UserService, private router: Router) { }

  profile() {
    if(this.userService.isLogged()) {
      if(this.userService.typeUser == "lg"){
        this.router.navigate(["lifeguards", this.userService.lifeguard.id]);
      } else {
        this.router.navigate(["employers", this.userService.employer.id]);
      }
    } else {
      this.router.navigate(["login"]);
    }
  }
}