import { NgModule } from '@angular/core';
import { ChartEditorComponent } from './components/chart-editor/chart-editor.component';
import { ChartWrapperComponent } from './components/chart-wrapper/chart-wrapper.component';
import { ControlWrapperComponent } from './components/control-wrapper/control-wrapper.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { GoogleChartComponent } from './components/google-chart/google-chart.component';
import { ScriptLoaderService } from './services/script-loader.service';
import { GOOGLE_CHARTS_CONFIG } from './types/google-charts-config';
import * as i0 from "@angular/core";
export class GoogleChartsModule {
    static forRoot(config = {}) {
        return {
            ngModule: GoogleChartsModule,
            providers: [{ provide: GOOGLE_CHARTS_CONFIG, useValue: config }]
        };
    }
}
GoogleChartsModule.ɵfac = i0.ɵɵngDeclareFactory({ minVersion: "12.0.0", version: "14.0.3", ngImport: i0, type: GoogleChartsModule, deps: [], target: i0.ɵɵFactoryTarget.NgModule });
GoogleChartsModule.ɵmod = i0.ɵɵngDeclareNgModule({ minVersion: "14.0.0", version: "14.0.3", ngImport: i0, type: GoogleChartsModule, declarations: [GoogleChartComponent,
        ChartWrapperComponent,
        DashboardComponent,
        ControlWrapperComponent,
        ChartEditorComponent], exports: [GoogleChartComponent,
        ChartWrapperComponent,
        DashboardComponent,
        ControlWrapperComponent,
        ChartEditorComponent] });
GoogleChartsModule.ɵinj = i0.ɵɵngDeclareInjector({ minVersion: "12.0.0", version: "14.0.3", ngImport: i0, type: GoogleChartsModule, providers: [ScriptLoaderService] });
i0.ɵɵngDeclareClassMetadata({ minVersion: "12.0.0", version: "14.0.3", ngImport: i0, type: GoogleChartsModule, decorators: [{
            type: NgModule,
            args: [{
                    declarations: [
                        GoogleChartComponent,
                        ChartWrapperComponent,
                        DashboardComponent,
                        ControlWrapperComponent,
                        ChartEditorComponent
                    ],
                    providers: [ScriptLoaderService],
                    exports: [
                        GoogleChartComponent,
                        ChartWrapperComponent,
                        DashboardComponent,
                        ControlWrapperComponent,
                        ChartEditorComponent
                    ]
                }]
        }] });
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZ29vZ2xlLWNoYXJ0cy5tb2R1bGUuanMiLCJzb3VyY2VSb290IjoiIiwic291cmNlcyI6WyIuLi8uLi8uLi8uLi9saWJzL2FuZ3VsYXItZ29vZ2xlLWNoYXJ0cy9zcmMvbGliL2dvb2dsZS1jaGFydHMubW9kdWxlLnRzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiJBQUFBLE9BQU8sRUFBdUIsUUFBUSxFQUFFLE1BQU0sZUFBZSxDQUFDO0FBRTlELE9BQU8sRUFBRSxvQkFBb0IsRUFBRSxNQUFNLGtEQUFrRCxDQUFDO0FBQ3hGLE9BQU8sRUFBRSxxQkFBcUIsRUFBRSxNQUFNLG9EQUFvRCxDQUFDO0FBQzNGLE9BQU8sRUFBRSx1QkFBdUIsRUFBRSxNQUFNLHdEQUF3RCxDQUFDO0FBQ2pHLE9BQU8sRUFBRSxrQkFBa0IsRUFBRSxNQUFNLDRDQUE0QyxDQUFDO0FBQ2hGLE9BQU8sRUFBRSxvQkFBb0IsRUFBRSxNQUFNLGtEQUFrRCxDQUFDO0FBQ3hGLE9BQU8sRUFBRSxtQkFBbUIsRUFBRSxNQUFNLGtDQUFrQyxDQUFDO0FBQ3ZFLE9BQU8sRUFBc0Isb0JBQW9CLEVBQUUsTUFBTSw4QkFBOEIsQ0FBQzs7QUFtQnhGLE1BQU0sT0FBTyxrQkFBa0I7SUFDdEIsTUFBTSxDQUFDLE9BQU8sQ0FBQyxTQUE2QixFQUFFO1FBQ25ELE9BQU87WUFDTCxRQUFRLEVBQUUsa0JBQWtCO1lBQzVCLFNBQVMsRUFBRSxDQUFDLEVBQUUsT0FBTyxFQUFFLG9CQUFvQixFQUFFLFFBQVEsRUFBRSxNQUFNLEVBQUUsQ0FBQztTQUNqRSxDQUFDO0lBQ0osQ0FBQzs7K0dBTlUsa0JBQWtCO2dIQUFsQixrQkFBa0IsaUJBZjNCLG9CQUFvQjtRQUNwQixxQkFBcUI7UUFDckIsa0JBQWtCO1FBQ2xCLHVCQUF1QjtRQUN2QixvQkFBb0IsYUFJcEIsb0JBQW9CO1FBQ3BCLHFCQUFxQjtRQUNyQixrQkFBa0I7UUFDbEIsdUJBQXVCO1FBQ3ZCLG9CQUFvQjtnSEFHWCxrQkFBa0IsYUFUbEIsQ0FBQyxtQkFBbUIsQ0FBQzsyRkFTckIsa0JBQWtCO2tCQWpCOUIsUUFBUTttQkFBQztvQkFDUixZQUFZLEVBQUU7d0JBQ1osb0JBQW9CO3dCQUNwQixxQkFBcUI7d0JBQ3JCLGtCQUFrQjt3QkFDbEIsdUJBQXVCO3dCQUN2QixvQkFBb0I7cUJBQ3JCO29CQUNELFNBQVMsRUFBRSxDQUFDLG1CQUFtQixDQUFDO29CQUNoQyxPQUFPLEVBQUU7d0JBQ1Asb0JBQW9CO3dCQUNwQixxQkFBcUI7d0JBQ3JCLGtCQUFrQjt3QkFDbEIsdUJBQXVCO3dCQUN2QixvQkFBb0I7cUJBQ3JCO2lCQUNGIiwic291cmNlc0NvbnRlbnQiOlsiaW1wb3J0IHsgTW9kdWxlV2l0aFByb3ZpZGVycywgTmdNb2R1bGUgfSBmcm9tICdAYW5ndWxhci9jb3JlJztcblxuaW1wb3J0IHsgQ2hhcnRFZGl0b3JDb21wb25lbnQgfSBmcm9tICcuL2NvbXBvbmVudHMvY2hhcnQtZWRpdG9yL2NoYXJ0LWVkaXRvci5jb21wb25lbnQnO1xuaW1wb3J0IHsgQ2hhcnRXcmFwcGVyQ29tcG9uZW50IH0gZnJvbSAnLi9jb21wb25lbnRzL2NoYXJ0LXdyYXBwZXIvY2hhcnQtd3JhcHBlci5jb21wb25lbnQnO1xuaW1wb3J0IHsgQ29udHJvbFdyYXBwZXJDb21wb25lbnQgfSBmcm9tICcuL2NvbXBvbmVudHMvY29udHJvbC13cmFwcGVyL2NvbnRyb2wtd3JhcHBlci5jb21wb25lbnQnO1xuaW1wb3J0IHsgRGFzaGJvYXJkQ29tcG9uZW50IH0gZnJvbSAnLi9jb21wb25lbnRzL2Rhc2hib2FyZC9kYXNoYm9hcmQuY29tcG9uZW50JztcbmltcG9ydCB7IEdvb2dsZUNoYXJ0Q29tcG9uZW50IH0gZnJvbSAnLi9jb21wb25lbnRzL2dvb2dsZS1jaGFydC9nb29nbGUtY2hhcnQuY29tcG9uZW50JztcbmltcG9ydCB7IFNjcmlwdExvYWRlclNlcnZpY2UgfSBmcm9tICcuL3NlcnZpY2VzL3NjcmlwdC1sb2FkZXIuc2VydmljZSc7XG5pbXBvcnQgeyBHb29nbGVDaGFydHNDb25maWcsIEdPT0dMRV9DSEFSVFNfQ09ORklHIH0gZnJvbSAnLi90eXBlcy9nb29nbGUtY2hhcnRzLWNvbmZpZyc7XG5cbkBOZ01vZHVsZSh7XG4gIGRlY2xhcmF0aW9uczogW1xuICAgIEdvb2dsZUNoYXJ0Q29tcG9uZW50LFxuICAgIENoYXJ0V3JhcHBlckNvbXBvbmVudCxcbiAgICBEYXNoYm9hcmRDb21wb25lbnQsXG4gICAgQ29udHJvbFdyYXBwZXJDb21wb25lbnQsXG4gICAgQ2hhcnRFZGl0b3JDb21wb25lbnRcbiAgXSxcbiAgcHJvdmlkZXJzOiBbU2NyaXB0TG9hZGVyU2VydmljZV0sXG4gIGV4cG9ydHM6IFtcbiAgICBHb29nbGVDaGFydENvbXBvbmVudCxcbiAgICBDaGFydFdyYXBwZXJDb21wb25lbnQsXG4gICAgRGFzaGJvYXJkQ29tcG9uZW50LFxuICAgIENvbnRyb2xXcmFwcGVyQ29tcG9uZW50LFxuICAgIENoYXJ0RWRpdG9yQ29tcG9uZW50XG4gIF1cbn0pXG5leHBvcnQgY2xhc3MgR29vZ2xlQ2hhcnRzTW9kdWxlIHtcbiAgcHVibGljIHN0YXRpYyBmb3JSb290KGNvbmZpZzogR29vZ2xlQ2hhcnRzQ29uZmlnID0ge30pOiBNb2R1bGVXaXRoUHJvdmlkZXJzPEdvb2dsZUNoYXJ0c01vZHVsZT4ge1xuICAgIHJldHVybiB7XG4gICAgICBuZ01vZHVsZTogR29vZ2xlQ2hhcnRzTW9kdWxlLFxuICAgICAgcHJvdmlkZXJzOiBbeyBwcm92aWRlOiBHT09HTEVfQ0hBUlRTX0NPTkZJRywgdXNlVmFsdWU6IGNvbmZpZyB9XVxuICAgIH07XG4gIH1cbn1cbiJdfQ==