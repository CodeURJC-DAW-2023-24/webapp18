import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { Me } from '../../models/me.model';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  me: Me | null = null;

  constructor(private router: Router, private userService: UserService) { }

  login(): void {
    this.userService.me().subscribe(
      response => {
        this.me = response as Me;
        if (this.me.type === 'lg') {
          this.router.navigate(['lifeguards', this.me.id]);
        } else {
          this.router.navigate(['employers', this.me.id]);
        }
      },
      _error => {
        this.router.navigate(['login']);
      }
    );
  }
}