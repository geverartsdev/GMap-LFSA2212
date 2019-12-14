import { Component, OnInit } from '@angular/core';
import {MarkerService} from '../../common/services/marker.service';

import Map from 'ol/Map';
import View from 'ol/View';
import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import { fromLonLat } from 'ol/proj';

@Component({
    selector: 'app-map',
    templateUrl: './map.component.html',
    styles: [
        '.map { position: absolute; left: 0px; top:55px; bottom: 0; right: 0;}'
    ]
})
export class MapComponent implements OnInit {

    private map: Map;

    constructor(private markerService: MarkerService) {}

    ngOnInit() {
        this.map = new Map({
            target: 'map',
            layers: [
                new TileLayer({
                    source: new OSM()
                })
            ],
            view: new View({
                center: fromLonLat([0, 0]),
                zoom: 15
            })
        });

        this.markerService.setMap(this.map);
    }
}
