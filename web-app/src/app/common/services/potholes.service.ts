import {Pothole} from '../models/pothole.model';
import {Firestore} from '../singletons/firestore.singleton';
import {Injectable} from '@angular/core';

@Injectable({providedIn: 'root'})
export class PotholesService {

    private COLLECTION_POTHOLES: string = "potholes";

    private potholes: Pothole[] = [];
    private addedListeners: ((pothole: Pothole) => void)[] = [];
    private removedListeners: ((pothole: Pothole) => void)[] = [];

    constructor() {
        Firestore.getDB().collection(this.COLLECTION_POTHOLES)
            .onSnapshot(querySnapshot=> {
                querySnapshot.docChanges().forEach(change => {
                    const data = change.doc.data();
                    if (change.type === "added") {
                        const pothole = new Pothole(
                            data.time,
                            data.location,
                            data.intensity,
                            change.doc.id
                        );
                        this.potholes.unshift(pothole);
                        this.addedListeners.forEach(func => func(pothole));
                    } else if (change.type === "modified") {
                        const pothole = this.findPothole(change.doc.id);
                        if (pothole === null) return;
                        if (data.time) pothole.setTime(data.time);
                        if (data.location) pothole.setLocation(data.location);
                        if (data.intensity) pothole.setIntensity(data.intensity);
                    } else if (change.type === "removed") {
                        const position = this.findPotholePosition(change.doc.id);
                        this.removedListeners.forEach(func => func(this.potholes[position]));
                        if (position !== -1) this.potholes.splice(position, 1);
                    }
                });
            });
    }

    public findPothole(id: string): Pothole {
        this.potholes.forEach(pothole => {
            if (pothole.getId() === id) return pothole;
        });
        return null;
    }

    public findPotholePosition(id: string): number {
        for (let i = 0; i < this.potholes.length; i++)
            if (this.potholes[i].getId() === id) return i;
        return -1;
    }

    public getPotholes(): Pothole[] {
        return this.potholes;
    }

    public deletePothole(id: string) {
        Firestore.getDB().collection(this.COLLECTION_POTHOLES)
            .doc(id)
            .delete()
            .catch(error => console.error("Error removing document: ", error));
    }

    public deleteAllPotholes() {
        const ids = [];
        this.potholes.forEach(pothole => ids.push(pothole.getId()));
        ids.forEach(id => this.deletePothole(id));
    }

    public onAddedPothole(func: (pothole: Pothole) => void) {
        this.addedListeners.push(func);
        this.potholes.forEach(pothole => func(pothole));
    }

    public onRemovedPothole(func: (pothole: Pothole) => void) {
        this.removedListeners.push(func);
    }
}
