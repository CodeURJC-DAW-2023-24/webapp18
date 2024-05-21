import { Injectable } from '@angular/core';
import { Router, NavigationExtras } from '@angular/router';

@Injectable({
    providedIn: 'root',
})
export class MessageService {
    constructor(private router: Router) { }

    showError(message: string) {
        this.showMessage(message, "");
    }

    showFatalError(message: string): void {
        this.showMessage(message, "/");
    }

    showMessage(message: string, back: string) {
        const navigationExtras: NavigationExtras = {
            state: {
                message: message,
                back: back
            }
        };

        this.router.navigate(['/message'], navigationExtras);
    }
}