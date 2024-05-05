import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { AppComponent } from './app.component';
import { routing } from './app.routing';
import { UserLoginComponent } from './components/user/user-login.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { UserFormComponent } from './components/user/user-form.component';
import { HeaderComponent } from './components/header/header.component';
import { OfferComponent } from './components/offer/offer.component';
import { OfferEditComponent } from './components/offer/offer.edit.component';
import { UserDetailComponent } from './components/user/user-detail.component';
import { OfferCreateComponent } from './components/offer/offer.create.component';
import { OffersComponent } from './components/cards/offers.component';
import { PoolsComponent } from './components/cards/pools.component';

@NgModule({
  declarations: [
    AppComponent,
    UserLoginComponent, UserFormComponent, UserDetailComponent,
    HeaderComponent,
    OfferComponent, OfferEditComponent, OfferCreateComponent, OffersComponent,
    PoolsComponent
  ],
  imports: [
    CommonModule,
    NgbModule,
    BrowserModule,
    FormsModule,
    HttpClientModule,
    routing
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
