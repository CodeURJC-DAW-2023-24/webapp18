import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../../services/user.service';
import { Me } from '../../models/me.model';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  me:Me;
  loginOrProfile: string;

  constructor(private activatedRoute: ActivatedRoute, private userService: UserService) { }

  ngOnInit(): void {
    this.userService.me().subscribe(
      response => {
        this.me = response as Me;
        if (this.me.type === 'lg') {
          this.loginOrProfile = 'lifeguards/' + this.me.id;
        } else {
          this.loginOrProfile = 'employers/' + this.me.id;
        }
      },
      error => {
        this.loginOrProfile = 'login';
      }
    );

  }
}