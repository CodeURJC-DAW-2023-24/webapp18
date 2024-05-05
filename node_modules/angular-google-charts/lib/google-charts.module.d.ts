import { ModuleWithProviders } from '@angular/core';
import { GoogleChartsConfig } from './types/google-charts-config';
import * as i0 from "@angular/core";
import * as i1 from "./components/google-chart/google-chart.component";
import * as i2 from "./components/chart-wrapper/chart-wrapper.component";
import * as i3 from "./components/dashboard/dashboard.component";
import * as i4 from "./components/control-wrapper/control-wrapper.component";
import * as i5 from "./components/chart-editor/chart-editor.component";
export declare class GoogleChartsModule {
    static forRoot(config?: GoogleChartsConfig): ModuleWithProviders<GoogleChartsModule>;
    static ɵfac: i0.ɵɵFactoryDeclaration<GoogleChartsModule, never>;
    static ɵmod: i0.ɵɵNgModuleDeclaration<GoogleChartsModule, [typeof i1.GoogleChartComponent, typeof i2.ChartWrapperComponent, typeof i3.DashboardComponent, typeof i4.ControlWrapperComponent, typeof i5.ChartEditorComponent], never, [typeof i1.GoogleChartComponent, typeof i2.ChartWrapperComponent, typeof i3.DashboardComponent, typeof i4.ControlWrapperComponent, typeof i5.ChartEditorComponent]>;
    static ɵinj: i0.ɵɵInjectorDeclaration<GoogleChartsModule>;
}
