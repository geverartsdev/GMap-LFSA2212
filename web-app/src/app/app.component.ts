import { Component } from '@angular/core';
import {PotholesService} from './common/services/potholes.service';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styles: [ ]
})
export class AppComponent {

    public isNavbarCollapsed = true;

    constructor(public potholesService: PotholesService) {

    }
}
