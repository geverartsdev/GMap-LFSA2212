import * as firebase from 'firebase';

export class Firebase {

    private static firebaseService: Firebase;

    private constructor() {
        const firebaseConfig = {
            apiKey: 'AIzaSyArKj50OYrjXOAYw--omPG_B7CHXDXYRHE',
            authDomain: 'gmap-lfsa2212.firebaseapp.com',
            databaseURL: 'https://gmap-lfsa2212.firebaseio.com',
            projectId: 'gmap-lfsa2212',
            storageBucket: 'gmap-lfsa2212.appspot.com',
            messagingSenderId: '773621294916',
            appId: '1:773621294916:web:f733028c0e53aaa018d911',
            measurementId: 'G-1CGYKFD9G9'
        };

        firebase.initializeApp(firebaseConfig);
    }

    public static getFirebaseService() {
        if (!this.firebaseService) this.firebaseService = new Firebase();
        return this.firebaseService;
    }
}
