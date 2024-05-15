import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA  } from '@angular/core';
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
import { MapsComponent } from './components/maps/maps.component';
import { GoogleMapsModule } from '@angular/google-maps';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';
import { OffersComponent } from './components/cards/offers.component';
import { PoolsComponent } from './components/cards/pools.component';
import { PoolComponent } from './components/pool/pool.component';
import { StadisticsComponent } from './components/stadistics/stadistics.component';
import { GoogleChartsModule } from 'angular-google-charts';
import { PoolFormComponent } from './components/pools/pool-form.component';
import { MessageComponent } from './components/message/message.component';

@NgModule({
  declarations: [
    AppComponent,
    UserLoginComponent, UserFormComponent, UserDetailComponent,
    HeaderComponent,
    OfferComponent, OfferEditComponent, OfferCreateComponent, OffersComponent,
    PoolsComponent, PoolComponent,
    StadisticsComponent, PoolFormComponent,
    MapsComponent,
    MessageComponent
  ],
  imports: [
    CommonModule,
    NgbModule,
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    routing,
    GoogleChartsModule,
    GoogleMapsModule,
    ToastrModule.forRoot({
      preventDuplicates: false,
      progressBar: true,
      countDuplicates: true,
      extendedTimeOut: 3000,
      positionClass: 'toast-bottom-right',
    }),
  ],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppModule { }
