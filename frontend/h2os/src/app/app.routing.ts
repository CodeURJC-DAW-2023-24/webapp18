import { Routes, RouterModule } from '@angular/router';
import { UserLoginComponent } from './components/user/user-login.component';
import { AppComponent } from './app.component';

const appRoutes: Routes = [
    { path: 'login', component: UserLoginComponent },
    { path: 'user/form', component: UserLoginComponent },
    { path: '', redirectTo: '', pathMatch: 'full' }
]

export const routing = RouterModule.forRoot(appRoutes);