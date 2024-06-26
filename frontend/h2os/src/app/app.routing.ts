import { Routes, RouterModule } from '@angular/router';
import { UserLoginComponent } from './components/user/user-login.component';
import { UserFormComponent } from './components/user/user-form.component';
import { UserDetailComponent } from './components/user/user-detail.component';
import { OfferComponent } from './components/offer/offer.component';
import { OfferEditComponent } from './components/offer/offer.edit.component';
import { MessageComponent } from './components/message/message.component';
import { OfferCreateComponent } from './components/offer/offer.create.component';
import { MapsComponent } from './components/maps/maps.component';
import { OffersComponent } from './components/cards/offers.component';
import { PoolsComponent } from './components/cards/pools.component';
import { PoolComponent } from './components/pool/pool.component';
import { StadisticsComponent } from './components/stadistics/stadistics.component';
import { PoolFormComponent } from './components/pools/pool-form.component';
import { UsersComponent } from './components/cards/users.component';

const appRoutes: Routes = [
    { path: 'login', component: UserLoginComponent },
    { path: 'user/form', component: UserFormComponent },
    { path: 'offers', component: OffersComponent},
    { path: 'pools', component: PoolsComponent},
    { path: 'lifeguards', component: UsersComponent },
    { path: 'employers', component: UsersComponent },
    { path: 'offers/new', component: OfferCreateComponent },
    { path: 'pools/new', component: PoolFormComponent },
    { path: 'offers/:id', component: OfferComponent},
    { path: 'offers/:id/edit', component: OfferEditComponent},
    { path: 'lifeguards/:id', component: UserDetailComponent },
    { path: 'employers/:id', component: UserDetailComponent },
    { path: 'lifeguards/edit/:id', component: UserFormComponent },
    { path: 'employers/edit/:id', component: UserFormComponent },
    { path: 'message', component: MessageComponent },
    { path: 'pools/:id', component: PoolComponent},
    { path: 'stadistics', component: StadisticsComponent },
    { path: 'pools/:id/edit', component: PoolFormComponent },
    { path: 'maps', component: MapsComponent},
    { path: '', redirectTo: '/maps', pathMatch: 'full'}
]

export const routing = RouterModule.forRoot(appRoutes);