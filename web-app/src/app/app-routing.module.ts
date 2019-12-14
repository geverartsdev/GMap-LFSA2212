import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {MapComponent} from './modules/map/map.component';
import {PotholesComponent} from './modules/potholes/potholes.component';


const routes: Routes = [
  { path: 'potholes', component: PotholesComponent },
  { path: 'map', component: MapComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
