import { Routes, RouterModule } from '@angular/router';
import { UserLoginComponent } from './components/user/user-login.component';
import { UserFormComponent } from './components/user/user-form.component';
import { UserDetailComponent } from './components/user/user-detail.component';
import { OfferComponent } from './components/offer/offer.component';
import { OfferEditComponent } from './components/offer/offer.edit.component';
import { UserMessageComponent } from './components/user/user-message.component';
import { OfferCreateComponent } from './components/offer/offer.create.component';
import { OffersComponent } from './components/cards/offers.component';

const appRoutes: Routes = [
    { path: 'login', component: UserLoginComponent },
    { path: 'user/form', component: UserFormComponent },
    //{ path: 'offers', component: OfferComponent},
    { path: 'offers', component: OffersComponent},
    { path: 'offers/edit', component: OfferEditComponent},
    { path: 'lifeguards/:id', component: UserDetailComponent },
    { path: 'employers/:id', component: UserDetailComponent },
    { path: 'lifeguards/edit/:id', component: UserFormComponent },
    { path: 'offers/newOffer', component: OfferCreateComponent },
    { path: 'employers/edit/:id', component: UserFormComponent },
    { path: 'user/message/:message', component: UserMessageComponent },
]

export const routing = RouterModule.forRoot(appRoutes);