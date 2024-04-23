import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { routing } from './app.routing';
import { UserLoginComponent } from './components/user/user-login.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { UserFormComponent } from './components/user/user-form.component';
import { HeaderComponent } from './components/header/header.component';

@NgModule({
  declarations: [AppComponent, UserLoginComponent, UserFormComponent, HeaderComponent],
  imports: [NgbModule, BrowserModule, FormsModule, HttpClientModule, routing],
  bootstrap: [AppComponent]
})
export class AppModule { }
