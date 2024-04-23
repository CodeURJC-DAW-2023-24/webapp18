import { Routes, RouterModule } from '@angular/router';
import { UserLoginComponent } from './components/user/user-login.component';
import { AppComponent } from './app.component';
import { UserFormComponent } from './components/user/user-form.component';
import { UserDetailComponent } from './components/user/user-detail.component';
import { OfferComponent } from './components/offer/offer.component';
import { OfferEditComponent } from './components/offer/offer.edit.component';

const appRoutes: Routes = [
    { path: 'login', component: UserLoginComponent },
    { path: 'user/form', component: UserFormComponent },
    { path: 'offers', component: OfferComponent},
    { path: 'offerEdit', component: OfferEditComponent},
    { path: 'profile', component: UserDetailComponent },
    { path: '', redirectTo: '', pathMatch: 'full' }
]

export const routing = RouterModule.forRoot(appRoutes);