import { Component } from '@angular/core';
import {PotholesService} from './common/services/potholes.service';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: []
})
export class AppComponent {

    title = "Home";

    constructor(public potholesService: PotholesService) {

    }
}
