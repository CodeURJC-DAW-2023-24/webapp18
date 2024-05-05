import { ChangeDetectionStrategy, Component, ContentChildren, ElementRef, EventEmitter, Input, Output, QueryList } from '@angular/core';
import { combineLatest } from 'rxjs';
import { DataTableService } from '../../services/data-table.service';
import { ScriptLoaderService } from '../../services/script-loader.service';
import { ControlWrapperComponent } from '../control-wrapper/control-wrapper.component';
import * as i0 from "@angular/core";
import * as i1 from "../../services/script-loader.service";
import * as i2 from "../../services/data-table.service";
export class DashboardComponent {
    constructor(element, loaderService, dataTableService) {
        this.element = element;
        this.loaderService = loaderService;
        this.dataTableService = dataTableService;
        /**
         * The dashboard has completed drawing and is ready to accept changes.
         *
         * The ready event will also fire:
         * - after the completion of a dashboard refresh triggered by a user or programmatic interaction with one of the controls,
         * - after redrawing any chart on the dashboard.
         */
        this.ready = new EventEmitter();
        /**
         * Emits when an error occurs when attempting to render the dashboard.
         * One or more of the controls and charts that are part of the dashboard may have failed rendering.
         */
        this.error = new EventEmitter();
        this.initialized = false;
    }
    ngOnInit() {
        this.loaderService.loadChartPackages('controls').subscribe(() => {
            this.dataTable = this.dataTableService.create(this.data, this.columns, this.formatters);
            this.createDashboard();
            this.initialized = true;
        });
    }
    ngOnChanges(changes) {
        if (!this.initialized) {
            return;
        }
        if (changes.data || changes.columns || changes.formatters) {
            this.dataTable = this.dataTableService.create(this.data, this.columns, this.formatters);
            this.dashboard.draw(this.dataTable);
        }
    }
    createDashboard() {
        // TODO: This should happen in the control wrapper
        // However, I don't yet know how to do this because then `bind()` would get called multiple times
        // for the same control if something changes. This is not supported by google charts as far as I can tell
        // from their source code.
        const controlWrappersReady$ = this.controlWrappers.map(control => control.wrapperReady$);
        const chartsReady$ = this.controlWrappers
            .map(control => control.for)
            .map(charts => {
            if (Array.isArray(charts)) {
                // CombineLatest waits for all observables
                return combineLatest(charts.map(chart => chart.wrapperReady$));
            }
            else {
                return charts.wrapperReady$;
            }
        });
        // We have to wait for all chart wrappers and control wrappers to be initialized
        // before we can compose them together to create the dashboard
        combineLatest([...controlWrappersReady$, ...chartsReady$]).subscribe(() => {
            this.dashboard = new google.visualization.Dashboard(this.element.nativeElement);
            this.initializeBindings();
            this.registerEvents();
            this.dashboard.draw(this.dataTable);
        });
    }
    registerEvents() {
        google.visualization.events.removeAllListeners(this.dashboard);
        const registerDashEvent = (object, eventName, callback) => {
            google.visualization.events.addListener(object, eventName, callback);
        };
        registerDashEvent(this.dashboard, 'ready', () => this.ready.emit());
        registerDashEvent(this.dashboard, 'error', (error) => this.error.emit(error));
    }
    initializeBindings() {
        this.controlWrappers.forEach(control => {
            if (Array.isArray(control.for)) {
                const chartWrappers = control.for.map(chart => chart.chartWrapper);
                this.dashboard.bind(control.controlWrapper, chartWrappers);
            }
            else {
                this.dashboard.bind(control.controlWrapper, control.for.chartWrapper);
            }
        });
    }
}
DashboardComponent.ɵfac = i0.ɵɵngDeclareFactory({ minVersion: "12.0.0", version: "14.0.3", ngImport: i0, type: DashboardComponent, deps: [{ token: i0.ElementRef }, { token: i1.ScriptLoaderService }, { token: i2.DataTableService }], target: i0.ɵɵFactoryTarget.Component });
DashboardComponent.ɵcmp = i0.ɵɵngDeclareComponent({ minVersion: "14.0.0", version: "14.0.3", type: DashboardComponent, selector: "dashboard", inputs: { data: "data", columns: "columns", formatters: "formatters" }, outputs: { ready: "ready", error: "error" }, host: { classAttribute: "dashboard" }, queries: [{ propertyName: "controlWrappers", predicate: ControlWrapperComponent }], exportAs: ["dashboard"], usesOnChanges: true, ngImport: i0, template: '<ng-content></ng-content>', isInline: true, changeDetection: i0.ChangeDetectionStrategy.OnPush });
i0.ɵɵngDeclareClassMetadata({ minVersion: "12.0.0", version: "14.0.3", ngImport: i0, type: DashboardComponent, decorators: [{
            type: Component,
            args: [{
                    selector: 'dashboard',
                    template: '<ng-content></ng-content>',
                    changeDetection: ChangeDetectionStrategy.OnPush,
                    exportAs: 'dashboard',
                    host: { class: 'dashboard' }
                }]
        }], ctorParameters: function () { return [{ type: i0.ElementRef }, { type: i1.ScriptLoaderService }, { type: i2.DataTableService }]; }, propDecorators: { data: [{
                type: Input
            }], columns: [{
                type: Input
            }], formatters: [{
                type: Input
            }], ready: [{
                type: Output
            }], error: [{
                type: Output
            }], controlWrappers: [{
                type: ContentChildren,
                args: [ControlWrapperComponent]
            }] } });
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZGFzaGJvYXJkLmNvbXBvbmVudC5qcyIsInNvdXJjZVJvb3QiOiIiLCJzb3VyY2VzIjpbIi4uLy4uLy4uLy4uLy4uLy4uL2xpYnMvYW5ndWxhci1nb29nbGUtY2hhcnRzL3NyYy9saWIvY29tcG9uZW50cy9kYXNoYm9hcmQvZGFzaGJvYXJkLmNvbXBvbmVudC50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiQUFBQSxPQUFPLEVBQ0wsdUJBQXVCLEVBQ3ZCLFNBQVMsRUFDVCxlQUFlLEVBQ2YsVUFBVSxFQUNWLFlBQVksRUFDWixLQUFLLEVBR0wsTUFBTSxFQUNOLFNBQVMsRUFFVixNQUFNLGVBQWUsQ0FBQztBQUN2QixPQUFPLEVBQUUsYUFBYSxFQUFFLE1BQU0sTUFBTSxDQUFDO0FBRXJDLE9BQU8sRUFBRSxnQkFBZ0IsRUFBRSxNQUFNLG1DQUFtQyxDQUFDO0FBQ3JFLE9BQU8sRUFBRSxtQkFBbUIsRUFBRSxNQUFNLHNDQUFzQyxDQUFDO0FBSTNFLE9BQU8sRUFBRSx1QkFBdUIsRUFBRSxNQUFNLDhDQUE4QyxDQUFDOzs7O0FBU3ZGLE1BQU0sT0FBTyxrQkFBa0I7SUFtRDdCLFlBQ1UsT0FBbUIsRUFDbkIsYUFBa0MsRUFDbEMsZ0JBQWtDO1FBRmxDLFlBQU8sR0FBUCxPQUFPLENBQVk7UUFDbkIsa0JBQWEsR0FBYixhQUFhLENBQXFCO1FBQ2xDLHFCQUFnQixHQUFoQixnQkFBZ0IsQ0FBa0I7UUEzQjVDOzs7Ozs7V0FNRztRQUVJLFVBQUssR0FBRyxJQUFJLFlBQVksRUFBUSxDQUFDO1FBRXhDOzs7V0FHRztRQUVJLFVBQUssR0FBRyxJQUFJLFlBQVksRUFBbUIsQ0FBQztRQU8zQyxnQkFBVyxHQUFHLEtBQUssQ0FBQztJQU16QixDQUFDO0lBRUcsUUFBUTtRQUNiLElBQUksQ0FBQyxhQUFhLENBQUMsaUJBQWlCLENBQUMsVUFBVSxDQUFDLENBQUMsU0FBUyxDQUFDLEdBQUcsRUFBRTtZQUM5RCxJQUFJLENBQUMsU0FBUyxHQUFHLElBQUksQ0FBQyxnQkFB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OlsiaW1wb3J0IHtcbiAgQ2hhbmdlRGV0ZWN0aW9uU3RyYXRlZ3ksXG4gIENvbXBvbmVudCxcbiAgQ29udGVudENoaWxkcmVuLFxuICBFbGVtZW50UmVmLFxuICBFdmVudEVtaXR0ZXIsXG4gIElucHV0LFxuICBPbkNoYW5nZXMsXG4gIE9uSW5pdCxcbiAgT3V0cHV0LFxuICBRdWVyeUxpc3QsXG4gIFNpbXBsZUNoYW5nZXNcbn0gZnJvbSAnQGFuZ3VsYXIvY29yZSc7XG5pbXBvcnQgeyBjb21iaW5lTGF0ZXN0IH0gZnJvbSAncnhqcyc7XG5cbmltcG9ydCB7IERhdGFUYWJsZVNlcnZpY2UgfSBmcm9tICcuLi8uLi9zZXJ2aWNlcy9kYXRhLXRhYmxlLnNlcnZpY2UnO1xuaW1wb3J0IHsgU2NyaXB0TG9hZGVyU2VydmljZSB9IGZyb20gJy4uLy4uL3NlcnZpY2VzL3NjcmlwdC1sb2FkZXIuc2VydmljZSc7XG5pbXBvcnQgeyBDaGFydEVycm9yRXZlbnQgfSBmcm9tICcuLi8uLi90eXBlcy9ldmVudHMnO1xuaW1wb3J0IHsgRm9ybWF0dGVyIH0gZnJvbSAnLi4vLi4vdHlwZXMvZm9ybWF0dGVyJztcbmltcG9ydCB7IENvbHVtbiwgUm93IH0gZnJvbSAnLi4vY2hhcnQtYmFzZS9jaGFydC1iYXNlLmNvbXBvbmVudCc7XG5pbXBvcnQgeyBDb250cm9sV3JhcHBlckNvbXBvbmVudCB9IGZyb20gJy4uL2NvbnRyb2wtd3JhcHBlci9jb250cm9sLXdyYXBwZXIuY29tcG9uZW50JztcblxuQENvbXBvbmVudCh7XG4gIHNlbGVjdG9yOiAnZGFzaGJvYXJkJyxcbiAgdGVtcGxhdGU6ICc8bmctY29udGVudD48L25nLWNvbnRlbnQ+JyxcbiAgY2hhbmdlRGV0ZWN0aW9uOiBDaGFuZ2VEZXRlY3Rpb25TdHJhdGVneS5PblB1c2gsXG4gIGV4cG9ydEFzOiAnZGFzaGJvYXJkJyxcbiAgaG9zdDogeyBjbGFzczogJ2Rhc2hib2FyZCcgfVxufSlcbmV4cG9ydCBjbGFzcyBEYXNoYm9hcmRDb21wb25lbnQgaW1wbGVtZW50cyBPbkluaXQsIE9uQ2hhbmdlcyB7XG4gIC8qKlxuICAgKiBEYXRhIHVzZWQgdG8gaW5pdGlhbGl6ZSB0aGUgdGFibGUuXG4gICAqXG4gICAqIFRoaXMgbXVzdCBhbHNvIGNvbnRhaW4gYWxsIHJvbGVzIHRoYXQgYXJlIHNldCBpbiB0aGUgYGNvbHVtbnNgIHByb3BlcnR5LlxuICAgKi9cbiAgQElucHV0KClcbiAgcHVibGljIGRhdGEhOiBSb3dbXTtcblxuICAvKipcbiAgICogVGhlIGNvbHVtbnMgdGhlIGBkYXRhYCBjb25zaXN0cyBvZi5cbiAgICogVGhlIGxlbmd0aCBvZiB0aGlzIGFycmF5IG11c3QgbWF0Y2ggdGhlIGxlbmd0aCBvZiBlYWNoIHJvdyBpbiB0aGUgYGRhdGFgIG9iamVjdC5cbiAgICpcbiAgICogSWYge0BsaW5rIGh0dHBzOi8vZGV2ZWxvcGVycy5nb29nbGUuY29tL2NoYXJ0L2ludGVyYWN0aXZlL2RvY3Mvcm9sZXMgcm9sZXN9IHNob3VsZCBiZSBhcHBsaWVkLCB0aGV5IG11c3QgYmUgaW5jbHVkZWQgaW4gdGhpcyBhcnJheSBhcyB3ZWxsLlxuICAgKi9cbiAgQElucHV0KClcbiAgcHVibGljIGNvbHVtbnM/OiBDb2x1bW5bXTtcblxuICAvKipcbiAgICogVXNlZCB0byBjaGFuZ2UgdGhlIGRpc3BsYXllZCB2YWx1ZSBvZiB0aGUgc3BlY2lmaWVkIGNvbHVtbiBpbiBhbGwgcm93cy5cbiAgICpcbiAgICogRWFjaCBhcnJheSBlbGVtZW50IG11c3QgY29uc2lzdCBvZiBhbiBpbnN0YW5jZSBvZiBhIFtgZm9ybWF0dGVyYF0oaHR0cHM6Ly9kZXZlbG9wZXJzLmdvb2dsZS5jb20vY2hhcnQvaW50ZXJhY3RpdmUvZG9jcy9yZWZlcmVuY2UjZm9ybWF0dGVycylcbiAgICogYW5kIHRoZSBpbmRleCBvZiB0aGUgY29sdW1uIHlvdSB3YW50IHRoZSBmb3JtYXR0ZXIgdG8gZ2V0IGFwcGxpZWQgdG8uXG4gICAqL1xuICBASW5wdXQoKVxuICBwdWJsaWMgZm9ybWF0dGVycz86IEZvcm1hdHRlcltdO1xuXG4gIC8qKlxuICAgKiBUaGUgZGFzaGJvYXJkIGhhcyBjb21wbGV0ZWQgZHJhd2luZyBhbmQgaXMgcmVhZHkgdG8gYWNjZXB0IGNoYW5nZXMuXG4gICAqXG4gICAqIFRoZSByZWFkeSBldmVudCB3aWxsIGFsc28gZmlyZTpcbiAgICogLSBhZnRlciB0aGUgY29tcGxldGlvbiBvZiBhIGRhc2hib2FyZCByZWZyZXNoIHRyaWdnZXJlZCBieSBhIHVzZXIgb3IgcHJvZ3JhbW1hdGljIGludGVyYWN0aW9uIHdpdGggb25lIG9mIHRoZSBjb250cm9scyxcbiAgICogLSBhZnRlciByZWRyYXdpbmcgYW55IGNoYXJ0IG9uIHRoZSBkYXNoYm9hcmQuXG4gICAqL1xuICBAT3V0cHV0KClcbiAgcHVibGljIHJlYWR5ID0gbmV3IEV2ZW50RW1pdHRlcjx2b2lkPigpO1xuXG4gIC8qKlxuICAgKiBFbWl0cyB3aGVuIGFuIGVycm9yIG9jY3VycyB3aGVuIGF0dGVtcHRpbmcgdG8gcmVuZGVyIHRoZSBkYXNoYm9hcmQuXG4gICAqIE9uZSBvciBtb3JlIG9mIHRoZSBjb250cm9scyBhbmQgY2hhcnRzIHRoYXQgYXJlIHBhcnQgb2YgdGhlIGRhc2hib2FyZCBtYXkgaGF2ZSBmYWlsZWQgcmVuZGVyaW5nLlxuICAgKi9cbiAgQE91dHB1dCgpXG4gIHB1YmxpYyBlcnJvciA9IG5ldyBFdmVudEVtaXR0ZXI8Q2hhcnRFcnJvckV2ZW50PigpO1xuXG4gIEBDb250ZW50Q2hpbGRyZW4oQ29udHJvbFdyYXBwZXJDb21wb25lbnQpXG4gIHByaXZhdGUgY29udHJvbFdyYXBwZXJzITogUXVlcnlMaXN0PENvbnRyb2xXcmFwcGVyQ29tcG9uZW50PjtcblxuICBwcml2YXRlIGRhc2hib2FyZD86IGdvb2dsZS52aXN1YWxpemF0aW9uLkRhc2hib2FyZDtcbiAgcHJpdmF0ZSBkYXRhVGFibGU/OiBnb29nbGUudmlzdWFsaXphdGlvbi5EYXRhVGFibGU7XG4gIHByaXZhdGUgaW5pdGlhbGl6ZWQgPSBmYWxzZTtcblxuICBjb25zdHJ1Y3RvcihcbiAgICBwcml2YXRlIGVsZW1lbnQ6IEVsZW1lbnRSZWYsXG4gICAgcHJpdmF0ZSBsb2FkZXJTZXJ2aWNlOiBTY3JpcHRMb2FkZXJTZXJ2aWNlLFxuICAgIHByaXZhdGUgZGF0YVRhYmxlU2VydmljZTogRGF0YVRhYmxlU2VydmljZVxuICApIHt9XG5cbiAgcHVibGljIG5nT25Jbml0KCkge1xuICAgIHRoaXMubG9hZGVyU2VydmljZS5sb2FkQ2hhcnRQYWNrYWdlcygnY29udHJvbHMnKS5zdWJzY3JpYmUoKCkgPT4ge1xuICAgICAgdGhpcy5kYXRhVGFibGUgPSB0aGlzLmRhdGFUYWJsZVNlcnZpY2UuY3JlYXRlKHRoaXMuZGF0YSwgdGhpcy5jb2x1bW5zLCB0aGlzLmZvcm1hdHRlcnMpO1xuICAgICAgdGhpcy5jcmVhdGVEYXNoYm9hcmQoKTtcbiAgICAgIHRoaXMuaW5pdGlhbGl6ZWQgPSB0cnVlO1xuICAgIH0pO1xuICB9XG5cbiAgcHVibGljIG5nT25DaGFuZ2VzKGNoYW5nZXM6IFNpbXBsZUNoYW5nZXMpOiB2b2lkIHtcbiAgICBpZiAoIXRoaXMuaW5pdGlhbGl6ZWQpIHtcbiAgICAgIHJldHVybjtcbiAgICB9XG5cbiAgICBpZiAoY2hhbmdlcy5kYXRhIHx8IGNoYW5nZXMuY29sdW1ucyB8fCBjaGFuZ2VzLmZvcm1hdHRlcnMpIHtcbiAgICAgIHRoaXMuZGF0YVRhYmxlID0gdGhpcy5kYXRhVGFibGVTZXJ2aWNlLmNyZWF0ZSh0aGlzLmRhdGEsIHRoaXMuY29sdW1ucywgdGhpcy5mb3JtYXR0ZXJzKTtcbiAgICAgIHRoaXMuZGFzaGJvYXJkIS5kcmF3KHRoaXMuZGF0YVRhYmxlISk7XG4gICAgfVxuICB9XG5cbiAgcHJpdmF0ZSBjcmVhdGVEYXNoYm9hcmQoKTogdm9pZCB7XG4gICAgLy8gVE9ETzogVGhpcyBzaG91bGQgaGFwcGVuIGluIHRoZSBjb250cm9sIHdyYXBwZXJcbiAgICAvLyBIb3dldmVyLCBJIGRvbid0IHlldCBrbm93IGhvdyB0byBkbyB0aGlzIGJlY2F1c2UgdGhlbiBgYmluZCgpYCB3b3VsZCBnZXQgY2FsbGVkIG11bHRpcGxlIHRpbWVzXG4gICAgLy8gZm9yIHRoZSBzYW1lIGNvbnRyb2wgaWYgc29tZXRoaW5nIGNoYW5nZXMuIFRoaXMgaXMgbm90IHN1cHBvcnRlZCBieSBnb29nbGUgY2hhcnRzIGFzIGZhciBhcyBJIGNhbiB0ZWxsXG4gICAgLy8gZnJvbSB0aGVpciBzb3VyY2UgY29kZS5cbiAgICBjb25zdCBjb250cm9sV3JhcHBlcnNSZWFkeSQgPSB0aGlzLmNvbnRyb2xXcmFwcGVycy5tYXAoY29udHJvbCA9PiBjb250cm9sLndyYXBwZXJSZWFkeSQpO1xuICAgIGNvbnN0IGNoYXJ0c1JlYWR5JCA9IHRoaXMuY29udHJvbFdyYXBwZXJzXG4gICAgICAubWFwKGNvbnRyb2wgPT4gY29udHJvbC5mb3IpXG4gICAgICAubWFwKGNoYXJ0cyA9PiB7XG4gICAgICAgIGlmIChBcnJheS5pc0FycmF5KGNoYXJ0cykpIHtcbiAgICAgICAgICAvLyBDb21iaW5lTGF0ZXN0IHdhaXRzIGZvciBhbGwgb2JzZXJ2YWJsZXNcbiAgICAgICAgICByZXR1cm4gY29tYmluZUxhdGVzdChjaGFydHMubWFwKGNoYXJ0ID0+IGNoYXJ0LndyYXBwZXJSZWFkeSQpKTtcbiAgICAgICAgfSBlbHNlIHtcbiAgICAgICAgICByZXR1cm4gY2hhcnRzLndyYXBwZXJSZWFkeSQ7XG4gICAgICAgIH1cbiAgICAgIH0pO1xuXG4gICAgLy8gV2UgaGF2ZSB0byB3YWl0IGZvciBhbGwgY2hhcnQgd3JhcHBlcnMgYW5kIGNvbnRyb2wgd3JhcHBlcnMgdG8gYmUgaW5pdGlhbGl6ZWRcbiAgICAvLyBiZWZvcmUgd2UgY2FuIGNvbXBvc2UgdGhlbSB0b2dldGhlciB0byBjcmVhdGUgdGhlIGRhc2hib2FyZFxuICAgIGNvbWJpbmVMYXRlc3QoWy4uLmNvbnRyb2xXcmFwcGVyc1JlYWR5JCwgLi4uY2hhcnRzUmVhZHkkXSkuc3Vic2NyaWJlKCgpID0+IHtcbiAgICAgIHRoaXMuZGFzaGJvYXJkID0gbmV3IGdvb2dsZS52aXN1YWxpemF0aW9uLkRhc2hib2FyZCh0aGlzLmVsZW1lbnQubmF0aXZlRWxlbWVudCk7XG4gICAgICB0aGlzLmluaXRpYWxpemVCaW5kaW5ncygpO1xuICAgICAgdGhpcy5yZWdpc3RlckV2ZW50cygpO1xuICAgICAgdGhpcy5kYXNoYm9hcmQuZHJhdyh0aGlzLmRhdGFUYWJsZSEpO1xuICAgIH0pO1xuICB9XG5cbiAgcHJpdmF0ZSByZWdpc3RlckV2ZW50cygpOiB2b2lkIHtcbiAgICBnb29nbGUudmlzdWFsaXphdGlvbi5ldmVudHMucmVtb3ZlQWxsTGlzdGVuZXJzKHRoaXMuZGFzaGJvYXJkKTtcblxuICAgIGNvbnN0IHJlZ2lzdGVyRGFzaEV2ZW50ID0gKG9iamVjdDogYW55LCBldmVudE5hbWU6IHN0cmluZywgY2FsbGJhY2s6IEZ1bmN0aW9uKSA9PiB7XG4gICAgICBnb29nbGUudmlzdWFsaXphdGlvbi5ldmVudHMuYWRkTGlzdGVuZXIob2JqZWN0LCBldmVudE5hbWUsIGNhbGxiYWNrKTtcbiAgICB9O1xuXG4gICAgcmVnaXN0ZXJEYXNoRXZlbnQodGhpcy5kYXNoYm9hcmQsICdyZWFkeScsICgpID0+IHRoaXMucmVhZHkuZW1pdCgpKTtcbiAgICByZWdpc3RlckRhc2hFdmVudCh0aGlzLmRhc2hib2FyZCwgJ2Vycm9yJywgKGVycm9yOiBDaGFydEVycm9yRXZlbnQpID0+IHRoaXMuZXJyb3IuZW1pdChlcnJvcikpO1xuICB9XG5cbiAgcHJpdmF0ZSBpbml0aWFsaXplQmluZGluZ3MoKTogdm9pZCB7XG4gICAgdGhpcy5jb250cm9sV3JhcHBlcnMuZm9yRWFjaChjb250cm9sID0+IHtcbiAgICAgIGlmIChBcnJheS5pc0FycmF5KGNvbnRyb2wuZm9yKSkge1xuICAgICAgICBjb25zdCBjaGFydFdyYXBwZXJzID0gY29udHJvbC5mb3IubWFwKGNoYXJ0ID0+IGNoYXJ0LmNoYXJ0V3JhcHBlcik7XG4gICAgICAgIHRoaXMuZGFzaGJvYXJkIS5iaW5kKGNvbnRyb2wuY29udHJvbFdyYXBwZXIsIGNoYXJ0V3JhcHBlcnMpO1xuICAgICAgfSBlbHNlIHtcbiAgICAgICAgdGhpcy5kYXNoYm9hcmQhLmJpbmQoY29udHJvbC5jb250cm9sV3JhcHBlciwgY29udHJvbC5mb3IuY2hhcnRXcmFwcGVyKTtcbiAgICAgIH1cbiAgICB9KTtcbiAgfVxufVxuIl19