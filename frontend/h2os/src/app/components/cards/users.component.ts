import { Component, ElementRef, HostListener, OnInit } from '@angular/core';
import { PaginationService } from '../../services/pagination.service';
import { Employer } from '../../models/employer.model';
import { Lifeguard } from '../../models/lifeguard.model';
import { UserService } from '../../services/user.service';
import { Me } from '../../models/me.model';
import { ActivatedRoute, Router } from '@angular/router';
import { Person } from '../../models/person.model';
import { MessageService } from '../../services/message.service';

const USER_TYPES: string[] = ["lifeguards", "employers"];
const TRANSLATIONS: { [key: string]: string } = {
  'employers': 'Empleadores',
  'lifeguards': 'Socorristas'
};

@Component({
  selector: "users",
  templateUrl: './users.component.html',
  styleUrl: './cards.css'
})
export class UsersComponent implements OnInit {
  type: string = USER_TYPES[0];
  users: Person[] = [];
  last_page: number = 0;
  hasMore: boolean = true;
  cardWidth: number = 250 + 2 * 20;
  rowElements: number;
  me: Me;
  title: string = "Empleadores";
  alternative: string = "Socorristas";

  constructor(
    private service: PaginationService,
    private userService: UserService,
    private messageService: MessageService,
    private elementRef: ElementRef,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.checkUser();
    this.updateRowElements();
    this.route.url.subscribe(urlSegments => {
      const path = urlSegments.join('/');

      for (let i = 0; i < USER_TYPES.length; i++) {
        if (path.includes(USER_TYPES[i])) {
          this.type = USER_TYPES[i];
          this.title = TRANSLATIONS[USER_TYPES[i]];
          this.alternative = TRANSLATIONS[USER_TYPES[(i + 1) % 2]]
          break;
        }
      }

      this.loadUsers();
    });
  }

  checkUser() {
    this.userService.me().subscribe(
      response => {
        this.me = response as Me;
        if (this.me.mail != "admin")  // The check is done by mail, not by role
          this.messageService.showFatalError("Solo los administradores pueden acceder a esta página")
      },
      _error =>
        this.messageService.showFatalError("Solo los administradores pueden acceder a esta página")
    );
  }

  usersChange() {
    this.type = this.type === USER_TYPES[0] ? USER_TYPES[1] : USER_TYPES[0];
    this.router.navigate([this.type]);
  }

  loadUsers() {
    this.getObservable().subscribe(
      (users: Person[]) => {
        if (!users) {
          this.hasMore = false;
          return;
        }

        for (const user of users) {
          if (user.type == "e") {
            user.hasImage = (user as Employer).imageCompany;
          } else {
            user.hasImage = (user as Lifeguard).imageUser;
          }
          if (user.hasImage) {
            this.userService.getImage(user).subscribe(
              response => {
                if (response) {
                  const blob = new Blob([response], { type: 'image/jpeg' });
                  user.image = URL.createObjectURL(blob);
                } else {
                  user.image = undefined;
                }

              },
              error => { user.image = undefined; }
            );
          }
        }

        this.users.push(...users);
      },
      (error) => {
        console.log(error);
        this.hasMore = false;
      }
    );
    this.last_page++;
    this.checkHasMore();
  }

  getObservable() {
    if (this.type === USER_TYPES[0])
      return this.service.getLifeguards(this.last_page, this.rowElements);
    else
      return this.service.getEmployers(this.last_page, this.rowElements);
  }

  checkHasMore() {
    this.getObservable().subscribe(
      (users: Person[]) => {
        if (!users)
          this.hasMore = false;
      },
      (error) => {
        console.log(error);
        this.hasMore = false;
      }
    );
  }

  updateRowElements() {
    const cards = this.elementRef.nativeElement.querySelector('.cards');
    const total = Math.floor(cards.offsetWidth / this.cardWidth);
    this.rowElements = Math.max(2, Math.min(5, total));  // Between 2 and 5 cards
  }

  @HostListener('window:resize', ['$event'])
  onResize() {
    this.updateRowElements();
  }
}