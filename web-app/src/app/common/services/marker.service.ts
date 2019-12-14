import { PotholesService } from './potholes.service';
import Map from 'ol/Map';
import Overlay from 'ol/Overlay';
import { fromLonLat } from 'ol/proj';
import { Injectable } from '@angular/core';
import {MyMarker} from '../models/mymarker.model';
import {Pothole} from '../models/pothole.model';

@Injectable({providedIn: 'root'})
export class MarkerService {

    private map: Map;
    private myMarkers: MyMarker[] = [];

    constructor(private potholesService: PotholesService) {
        potholesService.onAddedPothole(pothole => {
            const element = document.createElement('div');
            element.innerHTML = '<img src="https://cdn.mapmarker.io/api/v1/fa/stack?size=50&color=DC4C3F&icon=fa-microchip&hoffset=1"  alt="marker"/>';
            const marker = new Overlay({
                position: fromLonLat([pothole.getLocation().longitude, pothole.getLocation().latitude]),
                positioning: 'center-center',
                element: element,
                stopEvent: false
            });
            this.myMarkers.push(new MyMarker(pothole, marker));
            if (this.map) {
                this.map.addOverlay(marker);
                this.center(pothole);
            }
        });

        potholesService.onRemovedPothole(pothole => {
            const position: number = this.findMarkerPosition(pothole);
            if (position !== -1) {
                const marker = this.myMarkers[position].marker;
                this.myMarkers.splice(position, 1);
                if (this.map) {
                    this.map.removeOverlay(marker);
                }
            }
        });
    }

    private center(pothole: Pothole) {
        this.map.getView().setCenter(fromLonLat([pothole.getLocation().longitude, pothole.getLocation().latitude]));
    }

    public setMap(map: Map) {
        this.map = map;
        this.myMarkers.forEach(myMarker => this.map.addOverlay(myMarker.marker));
        if (this.myMarkers.length > 0) this.center(this.myMarkers[0].pothole);
    }

    private findMarkerPosition(pothole: Pothole): number {
        for (let i = 0; i < this.myMarkers.length; i++)
            if (this.myMarkers[i].pothole.getId() === pothole.getId())
                return i;

        return -1;
    }
}
