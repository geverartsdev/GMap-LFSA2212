import { Pothole } from './pothole.model';
import Overlay from 'ol/Overlay';


export class MyMarker {

    public readonly pothole: Pothole;
    public readonly marker: Overlay;

    constructor(pothole: Pothole, marker: Overlay) {
        this.pothole = pothole;
        this.marker = marker;
    }
}
