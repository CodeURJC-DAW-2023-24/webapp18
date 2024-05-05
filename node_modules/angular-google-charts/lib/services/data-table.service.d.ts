/// <reference types="@types/google.visualization" />
import { Column, Row } from '../components/chart-base/chart-base.component';
import { Formatter } from '../types/formatter';
import * as i0 from "@angular/core";
export declare class DataTableService {
    create(data: Row[] | undefined, columns?: Column[], formatters?: Formatter[]): google.visualization.DataTable | undefined;
    private getDataAsTable;
    private applyFormatters;
    static ɵfac: i0.ɵɵFactoryDeclaration<DataTableService, never>;
    static ɵprov: i0.ɵɵInjectableDeclaration<DataTableService>;
}
