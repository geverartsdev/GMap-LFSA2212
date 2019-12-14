import { Component, OnInit, QueryList, ViewChildren } from '@angular/core';
import 'firebase/firestore';
import '../../common/models/pothole.model';
import {Pothole} from '../../common/models/pothole.model';
import {SortableDirective, SortEvent} from '../../common/directives/sortable.directive';
import {PotholesService} from '../../common/services/potholes.service';

@Component({
    selector: 'app-potholes',
    templateUrl: './potholes.component.html',
    styleUrls: []
})
export class PotholesComponent implements OnInit {

    public potholes: Pothole[] = [];

    @ViewChildren(SortableDirective) headers: QueryList<SortableDirective>;

    constructor(public potholesService: PotholesService) {
        this.potholes = this.potholesService.getPotholes();
    }

    compare(v1, v2) {
        return v1 < v2 ? -1 : v1 > v2 ? 1 : 0;
    }

    onSort({column, direction}: SortEvent) {

        // resetting other headers
        this.headers.forEach(header => {
            if (header.sortable !== column) {
                header.direction = '';
            }
        });

        // sorting potholes
        if (direction === '') {
        } else {
            this.potholes.sort((a, b) => {
                const res = this.compare(a[column], b[column]);
                return direction === 'asc' ? res : -res;
            });
        }
    }

    ngOnInit() {
    }

}
