import {Firebase} from './firebase.singleton';
import * as firebase from 'firebase';

export class Firestore {

    private static firestoreService: Firestore;

    private firebaseService: Firebase;
    private db: firebase.firestore.Firestore;

    private constructor() {
        this.firebaseService = Firebase.getFirebaseService();
        this.db = this.db = firebase.firestore();
    }

    public static getDB(): firebase.firestore.Firestore {
        if (!this.firestoreService) this.firestoreService = new Firestore();
        return this.firestoreService.db;
    }
}
