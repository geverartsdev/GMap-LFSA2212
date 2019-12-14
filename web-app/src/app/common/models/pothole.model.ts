import GeoPoint = firebase.firestore.GeoPoint;
import * as firebase from 'firebase';

export class Pothole {
    private time: Date;
    private location: GeoPoint;
    private intensity: number;
    private readonly id: string;

    constructor(time: Date, location: GeoPoint, intensity: number, id: string) {
        this.time = time;
        this.location = location;
        this.intensity = intensity;
        this.id = id;
    }

    public setTime(time: Date) {
        this.time = time;
    }

    public getTime(): Date {
        return this.time;
    }

    public getTimeAsString(): string {
        return this.time.toString();
    }

    public setLocation(location: GeoPoint) {
        this.location = location;
    }

    public getLocation(): GeoPoint {
        return this.location;
    }

    public getLocationAsString(): string {
        if (this.location) return this.location.latitude + ', ' + this.location.longitude;
        return "Unknown";
    }

    public setIntensity(intensity: number) {
        this.intensity = intensity;
    }

    public getIntensity(): number {
        return this.intensity;
    }

    public getId(): string {
        return this.id;
    }
}
