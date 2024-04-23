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

@NgModule({
  declarations: [AppComponent,UserLoginComponent,UserFormComponent, HeaderComponent, OfferComponent, OfferEditComponent],
  imports: [NgbModule, BrowserModule, FormsModule, HttpClientModule, routing],
  bootstrap: [AppComponent]
})
export class AppModule { }
