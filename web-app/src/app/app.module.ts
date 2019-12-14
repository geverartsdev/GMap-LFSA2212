import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MapComponent } from './modules/map/map.component';
import { PotholesComponent } from './modules/potholes/potholes.component';
import { SortableDirective } from './common/directives/sortable.directive';
import { PotholesService } from './common/services/potholes.service';
import { MarkerService } from './common/services/marker.service';

@NgModule({
    declarations: [
        AppComponent,
        MapComponent,
        PotholesComponent,
        SortableDirective
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        NgbModule
    ],
    providers: [
        PotholesService,
        MarkerService
    ],
    bootstrap: [AppComponent]
})
export class AppModule { }
