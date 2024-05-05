import { ChangeDetectionStrategy, Component, ElementRef, EventEmitter, Input, Optional, Output } from '@angular/core';
import { fromEvent, ReplaySubject } from 'rxjs';
import { debounceTime } from 'rxjs/operators';
import { getPackageForChart } from '../../helpers/chart.helper';
import { DataTableService } from '../../services/data-table.service';
import { ScriptLoaderService } from '../../services/script-loader.service';
import { ChartType } from '../../types/chart-type';
import { DashboardComponent } from '../dashboard/dashboard.component';
import * as i0 from "@angular/core";
import * as i1 from "../../services/script-loader.service";
import * as i2 from "../../services/data-table.service";
import * as i3 from "../dashboard/dashboard.component";
export class GoogleChartComponent {
    constructor(element, scriptLoaderService, dataTableService, dashboard) {
        this.element = element;
        this.scriptLoaderService = scriptLoaderService;
        this.dataTableService = dataTableService;
        this.dashboard = dashboard;
        /**
         * The chart-specific options. All options listen in the Google Charts documentation applying
         * to the chart type specified can be used here.
         */
        this.options = {};
        /**
         * If this is set to `true`, the chart will be redrawn if the browser window is resized.
         * Defaults to `false` and should only be used when specifying the width or height of the chart
         * in percent.
         *
         * Note that this can impact performance.
         */
        this.dynamicResize = false;
        this.ready = new EventEmitter();
        this.error = new EventEmitter();
        this.select = new EventEmitter();
        this.mouseover = new EventEmitter();
        this.mouseleave = new EventEmitter();
        this.wrapperReadySubject = new ReplaySubject(1);
        this.initialized = false;
        this.eventListeners = new Map();
    }
    get chart() {
        return this.chartWrapper.getChart();
    }
    get wrapperReady$() {
        return this.wrapperReadySubject.asObservable();
    }
    get chartWrapper() {
        if (!this.wrapper) {
            throw new Error('Trying to access the chart wrapper before it was fully initialized');
        }
        return this.wrapper;
    }
    set chartWrapper(wrapper) {
        this.wrapper = wrapper;
        this.drawChart();
    }
    ngOnInit() {
        // We don't need to load any chart packages, the chart wrapper will handle this for us
        this.scriptLoaderService.loadChartPackages(getPackageForChart(this.type)).subscribe(() => {
            this.dataTable = this.dataTableService.create(this.data, this.columns, this.formatters);
            // Only ever create the wrapper once to allow animations to happen when something changes.
            this.wrapper = new google.visualization.ChartWrapper({
                container: this.element.nativeElement,
                chartType: this.type,
                dataTable: this.dataTable,
                options: this.mergeOptions()
            });
            this.registerChartEvents();
            this.wrapperReadySubject.next(this.wrapper);
            this.initialized = true;
            this.drawChart();
        });
    }
    ngOnChanges(changes) {
        if (changes.dynamicResize) {
            this.updateResizeListener();
        }
        if (this.initialized) {
            let shouldRedraw = false;
            if (changes.data || changes.columns || changes.formatters) {
                this.dataTable = this.dataTableService.create(this.data, this.columns, this.formatters);
                this.wrapper.setDataTable(this.dataTable);
                shouldRedraw = true;
            }
            if (changes.type) {
                this.wrapper.setChartType(this.type);
                shouldRedraw = true;
            }
            if (changes.options || changes.width || changes.height || changes.title) {
                this.wrapper.setOptions(this.mergeOptions());
                shouldRedraw = true;
            }
            if (shouldRedraw) {
                this.drawChart();
            }
        }
    }
    ngOnDestroy() {
        this.unsubscribeToResizeIfSubscribed();
    }
    /**
     * For listening to events other than the most common ones (available via Output properties).
     *
     * Can be called after the chart emits that it's "ready".
     *
     * Returns a handle that can be used for `removeEventListener`.
     */
    addEventListener(eventName, callback) {
        const handle = this.registerChartEvent(this.chart, eventName, callback);
        this.eventListeners.set(handle, { eventName, callback, handle });
        return handle;
    }
    removeEventListener(handle) {
        const entry = this.eventListeners.get(handle);
        if (entry) {
            google.visualization.events.removeListener(entry.handle);
            this.eventListeners.delete(handle);
        }
    }
    updateResizeListener() {
        this.unsubscribeToResizeIfSubscribed();
        if (this.dynamicResize) {
            this.resizeSubscription = fromEvent(window, 'resize', { passive: true })
                .pipe(debounceTime(100))
                .subscribe(() => {
                if (this.initialized) {
                    this.drawChart();
                }
            });
        }
    }
    unsubscribeToResizeIfSubscribed() {
        if (this.resizeSubscription != null) {
            this.resizeSubscription.unsubscribe();
            this.resizeSubscription = undefined;
        }
    }
    mergeOptions() {
        return {
            title: this.title,
            width: this.width,
            height: this.height,
            ...this.options
        };
    }
    registerChartEvents() {
        google.visualization.events.removeAllListeners(this.wrapper);
        this.registerChartEvent(this.wrapper, 'ready', () => {
            // This could also be done by checking if we already subscribed to the events
            google.visualization.events.removeAllListeners(this.chart);
            this.registerChartEvent(this.chart, 'onmouseover', (event) => this.mouseover.emit(event));
            this.registerChartEvent(this.chart, 'onmouseout', (event) => this.mouseleave.emit(event));
            this.registerChartEvent(this.chart, 'select', () => {
                const selection = this.chart.getSelection();
                this.select.emit({ selection });
            });
            this.eventListeners.forEach(x => (x.handle = this.registerChartEvent(this.chart, x.eventName, x.callback)));
            this.ready.emit({ chart: this.chart });
        });
        this.registerChartEvent(this.wrapper, 'error', (error) => this.error.emit(error));
    }
    registerChartEvent(object, eventName, callback) {
        return google.visualization.events.addListener(object, eventName, callback);
    }
    drawChart() {
        if (this.dashboard != null) {
            // If this chart is part of a dashboard, the dashboard takes care of drawing
            return;
        }
        this.wrapper.draw();
    }
}
GoogleChartComponent.ɵfac = i0.ɵɵngDeclareFactory({ minVersion: "12.0.0", version: "14.0.3", ngImport: i0, type: GoogleChartComponent, deps: [{ token: i0.ElementRef }, { token: i1.ScriptLoaderService }, { token: i2.DataTableService }, { token: i3.DashboardComponent, optional: true }], target: i0.ɵɵFactoryTarget.Component });
GoogleChartComponent.ɵcmp = i0.ɵɵngDeclareComponent({ minVersion: "14.0.0", version: "14.0.3", type: GoogleChartComponent, selector: "google-chart", inputs: { type: "type", data: "data", columns: "columns", title: "title", width: "width", height: "height", options: "options", formatters: "formatters", dynamicResize: "dynamicResize" }, outputs: { ready: "ready", error: "error", select: "select", mouseover: "mouseover", mouseleave: "mouseleave" }, host: { classAttribute: "google-chart" }, exportAs: ["googleChart"], usesOnChanges: true, ngImport: i0, template: '', isInline: true, styles: [":host{width:-webkit-fit-content;width:-moz-fit-content;width:fit-content;display:block}\n"], changeDetection: i0.ChangeDetectionStrategy.OnPush });
i0.ɵɵngDeclareClassMetadata({ minVersion: "12.0.0", version: "14.0.3", ngImport: i0, type: GoogleChartComponent, decorators: [{
            type: Component,
            args: [{ selector: 'google-chart', template: '', host: { class: 'google-chart' }, exportAs: 'googleChart', changeDetection: ChangeDetectionStrategy.OnPush, styles: [":host{width:-webkit-fit-content;width:-moz-fit-content;width:fit-content;display:block}\n"] }]
        }], ctorParameters: function () { return [{ type: i0.ElementRef }, { type: i1.ScriptLoaderService }, { type: i2.DataTableService }, { type: i3.DashboardComponent, decorators: [{
                    type: Optional
                }] }]; }, propDecorators: { type: [{
                type: Input
            }], data: [{
                type: Input
            }], columns: [{
                type: Input
            }], title: [{
                type: Input
            }], width: [{
                type: Input
            }], height: [{
                type: Input
            }], options: [{
                type: Input
            }], formatters: [{
                type: Input
            }], dynamicResize: [{
                type: Input
            }], ready: [{
                type: Output
            }], error: [{
                type: Output
            }], select: [{
                type: Output
            }], mouseover: [{
                type: Output
            }], mouseleave: [{
                type: Output
            }] } });
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZ29vZ2xlLWNoYXJ0LmNvbXBvbmVudC5qcyIsInNvdXJjZVJvb3QiOiIiLCJzb3VyY2VzIjpbIi4uLy4uLy4uLy4uLy4uLy4uL2xpYnMvYW5ndWxhci1nb29nbGUtY2hhcnRzL3NyYy9saWIvY29tcG9uZW50cy9nb29nbGUtY2hhcnQvZ29vZ2xlLWNoYXJ0LmNvbXBvbmVudC50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiQUFBQSxPQUFPLEVBQ0wsdUJBQXVCLEVBQ3ZCLFNBQVMsRUFDVCxVQUFVLEVBQ1YsWUFBWSxFQUNaLEtBQUssRUFJTCxRQUFRLEVBQ1IsTUFBTSxFQUVQLE1BQU0sZUFBZSxDQUFDO0FBQ3ZCLE9BQU8sRUFBRSxTQUFTLEVBQWMsYUFBYSxFQUFnQixNQUFNLE1BQU0sQ0FBQztBQUMxRSxPQUFPLEVBQUUsWUFBWSxFQUFFLE1BQU0sZ0JBQWdCLENBQUM7QUFFOUMsT0FBTyxFQUFFLGtCQUFrQixFQUFFLE1BQU0sNEJBQTRCLENBQUM7QUFDaEUsT0FBTyxFQUFFLGdCQUFnQixFQUFFLE1BQU0sbUNBQW1DLENBQUM7QUFDckUsT0FBTyxFQUFFLG1CQUFtQixFQUFFLE1BQU0sc0NBQXNDLENBQUM7QUFDM0UsT0FBTyxFQUFFLFNBQVMsRUFBRSxNQUFNLHdCQUF3QixDQUFDO0FBVW5ELE9BQU8sRUFBRSxrQkFBa0IsRUFBRSxNQUFNLGtDQUFrQyxDQUFDOzs7OztBQVV0RSxNQUFNLE9BQU8sb0JBQW9CO0lBaUcvQixZQUNVLE9BQW1CLEVBQ25CLG1CQUF3QyxFQUN4QyxnQkFBa0MsRUFDdEIsU0FBOEI7UUFIMUMsWUFBTyxHQUFQLE9BQU8sQ0FBWTtRQUNuQix3QkFBbUIsR0FBbkIsbUJBQW1CLENBQXFCO1FBQ3hDLHFCQUFnQixHQUFoQixnQkFBZ0IsQ0FBa0I7UUFDdEIsY0FBUyxHQUFULFNBQVMsQ0FBcUI7UUFyRHBEOzs7V0FHRztRQUVJLFlBQU8sR0FBVyxFQUFFLENBQUM7UUFXNUI7Ozs7OztXQU1HO1FBRUksa0JBQWEsR0FBRyxLQUFL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LENBQUMsS0FBSyxDQUFDLElBQUksQ0FBQyxLQUFLLENBQUMsQ0FBQyxDQUFDO0lBQ3JHLENBQUM7SUFFTyxrQkFBa0IsQ0FBQyxNQUFXLEVBQUUsU0FBaUIsRUFBRSxRQUFrQjtRQUMzRSxPQUFPLE1BQU0sQ0FBQyxhQUFhLENBQUMsTUFBTSxDQUFDLFdBQVcsQ0FBQyxNQUFNLEVBQUUsU0FBUyxFQUFFLFFBQVEsQ0FBQyxDQUFDO0lBQzlFLENBQUM7SUFFTyxTQUFTO1FBQ2YsSUFBSSxJQUFJLENBQUMsU0FBUyxJQUFJLElBQUksRUFBRTtZQUMxQiw0RUFBNEU7WUFDNUUsT0FBTztTQUNSO1FBRUQsSUFBSSxDQUFDLE9BQVEsQ0FBQyxJQUFJLEVBQUUsQ0FBQztJQUN2QixDQUFDOztpSEF0UVUsb0JBQW9CO3FHQUFwQixvQkFBb0IsMmJBTnJCLEVBQUU7MkZBTUQsb0JBQW9CO2tCQVJoQyxTQUFTOytCQUNFLGNBQWMsWUFDZCxFQUFFLFFBRU4sRUFBRSxLQUFLLEVBQUUsY0FBYyxFQUFFLFlBQ3JCLGFBQWEsbUJBQ04sdUJBQXVCLENBQUMsTUFBTTs7MEJBdUc1QyxRQUFROzRDQWhHSixJQUFJO3NCQURWLEtBQUs7Z0JBU0MsSUFBSTtzQkFEVixLQUFLO2dCQVVDLE9BQU87c0JBRGIsS0FBSztnQkFTQyxLQUFLO3NCQURYLEtBQUs7Z0JBU0MsS0FBSztzQkFEWCxLQUFLO2dCQVNDLE1BQU07c0JBRFosS0FBSztnQkFRQyxPQUFPO3NCQURiLEtBQUs7Z0JBVUMsVUFBVTtzQkFEaEIsS0FBSztnQkFXQyxhQUFhO3NCQURuQixLQUFLO2dCQUlDLEtBQUs7c0JBRFgsTUFBTTtnQkFJQSxLQUFLO3NCQURYLE1BQU07Z0JBSUEsTUFBTTtzQkFEWixNQUFNO2dCQUlBLFNBQVM7c0JBRGYsTUFBTTtnQkFJQSxVQUFVO3NCQURoQixNQUFNIiwic291cmNlc0NvbnRlbnQiOlsiaW1wb3J0IHtcbiAgQ2hhbmdlRGV0ZWN0aW9uU3RyYXRlZ3ksXG4gIENvbXBvbmVudCxcbiAgRWxlbWVudFJlZixcbiAgRXZlbnRFbWl0dGVyLFxuICBJbnB1dCxcbiAgT25DaGFuZ2VzLFxuICBPbkRlc3Ryb3ksXG4gIE9uSW5pdCxcbiAgT3B0aW9uYWwsXG4gIE91dHB1dCxcbiAgU2ltcGxlQ2hhbmdlc1xufSBmcm9tICdAYW5ndWxhci9jb3JlJztcbmltcG9ydCB7IGZyb21FdmVudCwgT2JzZXJ2YWJsZSwgUmVwbGF5U3ViamVjdCwgU3Vic2NyaXB0aW9uIH0gZnJvbSAncnhqcyc7XG5pbXBvcnQgeyBkZWJvdW5jZVRpbWUgfSBmcm9tICdyeGpzL29wZXJhdG9ycyc7XG5cbmltcG9ydCB7IGdldFBhY2thZ2VGb3JDaGFydCB9IGZyb20gJy4uLy4uL2hlbHBlcnMvY2hhcnQuaGVscGVyJztcbmltcG9ydCB7IERhdGFUYWJsZVNlcnZpY2UgfSBmcm9tICcuLi8uLi9zZXJ2aWNlcy9kYXRhLXRhYmxlLnNlcnZpY2UnO1xuaW1wb3J0IHsgU2NyaXB0TG9hZGVyU2VydmljZSB9IGZyb20gJy4uLy4uL3NlcnZpY2VzL3NjcmlwdC1sb2FkZXIuc2VydmljZSc7XG5pbXBvcnQgeyBDaGFydFR5cGUgfSBmcm9tICcuLi8uLi90eXBlcy9jaGFydC10eXBlJztcbmltcG9ydCB7XG4gIENoYXJ0RXJyb3JFdmVudCxcbiAgQ2hhcnRNb3VzZUxlYXZlRXZlbnQsXG4gIENoYXJ0TW91c2VPdmVyRXZlbnQsXG4gIENoYXJ0UmVhZHlFdmVudCxcbiAgQ2hhcnRTZWxlY3Rpb25DaGFuZ2VkRXZlbnRcbn0gZnJvbSAnLi4vLi4vdHlwZXMvZXZlbnRzJztcbmltcG9ydCB7IEZvcm1hdHRlciB9IGZyb20gJy4uLy4uL3R5cGVzL2Zvcm1hdHRlcic7XG5pbXBvcnQgeyBDaGFydEJhc2UsIENvbHVtbiwgUm93IH0gZnJvbSAnLi4vY2hhcnQtYmFzZS9jaGFydC1iYXNlLmNvbXBvbmVudCc7XG5pbXBvcnQgeyBEYXNoYm9hcmRDb21wb25lbnQgfSBmcm9tICcuLi9kYXNoYm9hcmQvZGFzaGJvYXJkLmNvbXBvbmVudCc7XG5cbkBDb21wb25lbnQoe1xuICBzZWxlY3RvcjogJ2dvb2dsZS1jaGFydCcsXG4gIHRlbXBsYXRlOiAnJyxcbiAgc3R5bGVzOiBbJzpob3N0IHsgd2lkdGg6IGZpdC1jb250ZW50OyBkaXNwbGF5OiBibG9jazsgfSddLFxuICBob3N0OiB7IGNsYXNzOiAnZ29vZ2xlLWNoYXJ0JyB9LFxuICBleHBvcnRBczogJ2dvb2dsZUNoYXJ0JyxcbiAgY2hhbmdlRGV0ZWN0aW9uOiBDaGFuZ2VEZXRlY3Rpb25TdHJhdGVneS5PblB1c2hcbn0pXG5leHBvcnQgY2xhc3MgR29vZ2xlQ2hhcnRDb21wb25lbnQgaW1wbGVtZW50cyBDaGFydEJhc2UsIE9uSW5pdCwgT25DaGFuZ2VzLCBPbkRlc3Ryb3kge1xuICAvKipcbiAgICogVGhlIHR5cGUgb2YgdGhlIGNoYXJ0IHRvIGNyZWF0ZS5cbiAgICovXG4gIEBJbnB1dCgpXG4gIHB1YmxpYyB0eXBlITogQ2hhcnRUeXBlO1xuXG4gIC8qKlxuICAgKiBEYXRhIHVzZWQgdG8gaW5pdGlhbGl6ZSB0aGUgdGFibGUuXG4gICAqXG4gICAqIFRoaXMgbXVzdCBhbHNvIGNvbnRhaW4gYWxsIHJvbGVzIHRoYXQgYXJlIHNldCBpbiB0aGUgYGNvbHVtbnNgIHByb3BlcnR5LlxuICAgKi9cbiAgQElucHV0KClcbiAgcHVibGljIGRhdGEhOiBSb3dbXTtcblxuICAvKipcbiAgICogVGhlIGNvbHVtbnMgdGhlIGBkYXRhYCBjb25zaXN0cyBvZi5cbiAgICogVGhlIGxlbmd0aCBvZiB0aGlzIGFycmF5IG11c3QgbWF0Y2ggdGhlIGxlbmd0aCBvZiBlYWNoIHJvdyBpbiB0aGUgYGRhdGFgIG9iamVjdC5cbiAgICpcbiAgICogSWYge0BsaW5rIGh0dHBzOi8vZGV2ZWxvcGVycy5nb29nbGUuY29tL2NoYXJ0L2ludGVyYWN0aXZlL2RvY3Mvcm9sZXMgcm9sZXN9IHNob3VsZCBiZSBhcHBsaWVkLCB0aGV5IG11c3QgYmUgaW5jbHVkZWQgaW4gdGhpcyBhcnJheSBhcyB3ZWxsLlxuICAgKi9cbiAgQElucHV0KClcbiAgcHVibGljIGNvbHVtbnM/OiBDb2x1bW5bXTtcblxuICAvKipcbiAgICogQSBjb252ZW5pZW5jZSBwcm9wZXJ0eSB1c2VkIHRvIHNldCB0aGUgdGl0bGUgb2YgdGhlIGNoYXJ0LlxuICAgKlxuICAgKiBUaGlzIGNhbiBhbHNvIGJlIHNldCB1c2luZyBgb3B0aW9ucy50aXRsZWAsIHdoaWNoLCBpZiBleGlzdGFudCwgd2lsbCBvdmVyd3JpdGUgdGhpcyB2YWx1ZS5cbiAgICovXG4gIEBJbnB1dCgpXG4gIHB1YmxpYyB0aXRsZT86IHN0cmluZztcblxuICAvKipcbiAgICogQSBjb252ZW5pZW5jZSBwcm9wZXJ0eSB1c2VkIHRvIHNldCB0aGUgd2lkdGggb2YgdGhlIGNoYXJ0IGluIHBpeGVscy5cbiAgICpcbiAgICogVGhpcyBjYW4gYWxzbyBiZSBzZXQgdXNpbmcgYG9wdGlvbnMud2lkdGhgLCB3aGljaCwgaWYgZXhpc3RhbnQsIHdpbGwgb3ZlcndyaXRlIHRoaXMgdmFsdWUuXG4gICAqL1xuICBASW5wdXQoKVxuICBwdWJsaWMgd2lkdGg/OiBudW1iZXI7XG5cbiAgLyoqXG4gICAqIEEgY29udmVuaWVuY2UgcHJvcGVydHkgdXNlZCB0byBzZXQgdGhlIGhlaWdodCBvZiB0aGUgY2hhcnQgaW4gcGl4ZWxzLlxuICAgKlxuICAgKiBUaGlzIGNhbiBhbHNvIGJlIHNldCB1c2luZyBgb3B0aW9ucy5oZWlnaHRgLCB3aGljaCwgaWYgZXhpc3RhbnQsIHdpbGwgb3ZlcndyaXRlIHRoaXMgdmFsdWUuXG4gICAqL1xuICBASW5wdXQoKVxuICBwdWJsaWMgaGVpZ2h0PzogbnVtYmVyO1xuXG4gIC8qKlxuICAgKiBUaGUgY2hhcnQtc3BlY2lmaWMgb3B0aW9ucy4gQWxsIG9wdGlvbnMgbGlzdGVuIGluIHRoZSBHb29nbGUgQ2hhcnRzIGRvY3VtZW50YXRpb24gYXBwbHlpbmdcbiAgICogdG8gdGhlIGNoYXJ0IHR5cGUgc3BlY2lmaWVkIGNhbiBiZSB1c2VkIGhlcmUuXG4gICAqL1xuICBASW5wdXQoKVxuICBwdWJsaWMgb3B0aW9uczogb2JqZWN0ID0ge307XG5cbiAgLyoqXG4gICAqIFVzZWQgdG8gY2hhbmdlIHRoZSBkaXNwbGF5ZWQgdmFsdWUgb2YgdGhlIHNwZWNpZmllZCBjb2x1bW4gaW4gYWxsIHJvd3MuXG4gICAqXG4gICAqIEVhY2ggYXJyYXkgZWxlbWVudCBtdXN0IGNvbnNpc3Qgb2YgYW4gaW5zdGFuY2Ugb2YgYSBbYGZvcm1hdHRlcmBdKGh0dHBzOi8vZGV2ZWxvcGVycy5nb29nbGUuY29tL2NoYXJ0L2ludGVyYWN0aXZlL2RvY3MvcmVmZXJlbmNlI2Zvcm1hdHRlcnMpXG4gICAqIGFuZCB0aGUgaW5kZXggb2YgdGhlIGNvbHVtbiB5b3Ugd2FudCB0aGUgZm9ybWF0dGVyIHRvIGdldCBhcHBsaWVkIHRvLlxuICAgKi9cbiAgQElucHV0KClcbiAgcHVibGljIGZvcm1hdHRlcnM/OiBGb3JtYXR0ZXJbXTtcblxuICAvKipcbiAgICogSWYgdGhpcyBpcyBzZXQgdG8gYHRydWVgLCB0aGUgY2hhcnQgd2lsbCBiZSByZWRyYXduIGlmIHRoZSBicm93c2VyIHdpbmRvdyBpcyByZXNpemVkLlxuICAgKiBEZWZhdWx0cyB0byBgZmFsc2VgIGFuZCBzaG91bGQgb25seSBiZSB1c2VkIHdoZW4gc3BlY2lmeWluZyB0aGUgd2lkdGggb3IgaGVpZ2h0IG9mIHRoZSBjaGFydFxuICAgKiBpbiBwZXJjZW50LlxuICAgKlxuICAgKiBOb3RlIHRoYXQgdGhpcyBjYW4gaW1wYWN0IHBlcmZvcm1hbmNlLlxuICAgKi9cbiAgQElucHV0KClcbiAgcHVibGljIGR5bmFtaWNSZXNpemUgPSBmYWxzZTtcblxuICBAT3V0cHV0KClcbiAgcHVibGljIHJlYWR5ID0gbmV3IEV2ZW50RW1pdHRlcjxDaGFydFJlYWR5RXZlbnQ+KCk7XG5cbiAgQE91dHB1dCgpXG4gIHB1YmxpYyBlcnJvciA9IG5ldyBFdmVudEVtaXR0ZXI8Q2hhcnRFcnJvckV2ZW50PigpO1xuXG4gIEBPdXRwdXQoKVxuICBwdWJsaWMgc2VsZWN0ID0gbmV3IEV2ZW50RW1pdHRlcjxDaGFydFNlbGVjdGlvbkNoYW5nZWRFdmVudD4oKTtcblxuICBAT3V0cHV0KClcbiAgcHVibGljIG1vdXNlb3ZlciA9IG5ldyBFdmVudEVtaXR0ZXI8Q2hhcnRNb3VzZU92ZXJFdmVudD4oKTtcblxuICBAT3V0cHV0KClcbiAgcHVibGljIG1vdXNlbGVhdmUgPSBuZXcgRXZlbnRFbWl0dGVyPENoYXJ0TW91c2VMZWF2ZUV2ZW50PigpO1xuXG4gIHByaXZhdGUgcmVzaXplU3Vic2NyaXB0aW9uPzogU3Vic2NyaXB0aW9uO1xuXG4gIHByaXZhdGUgZGF0YVRhYmxlOiBnb29nbGUudmlzdWFsaXphdGlvbi5EYXRhVGFibGUgfCB1bmRlZmluZWQ7XG4gIHByaXZhdGUgd3JhcHBlcjogZ29vZ2xlLnZpc3VhbGl6YXRpb24uQ2hhcnRXcmFwcGVyIHwgdW5kZWZpbmVkO1xuICBwcml2YXRlIHdyYXBwZXJSZWFkeVN1YmplY3QgPSBuZXcgUmVwbGF5U3ViamVjdDxnb29nbGUudmlzdWFsaXphdGlvbi5DaGFydFdyYXBwZXI+KDEpO1xuICBwcml2YXRlIGluaXRpYWxpemVkID0gZmFsc2U7XG4gIHByaXZhdGUgZXZlbnRMaXN0ZW5lcnMgPSBuZXcgTWFwPGFueSwgeyBldmVudE5hbWU6IHN0cmluZzsgY2FsbGJhY2s6IEZ1bmN0aW9uOyBoYW5kbGU6IGFueSB9PigpO1xuXG4gIGNvbnN0cnVjdG9yKFxuICAgIHByaXZhdGUgZWxlbWVudDogRWxlbWVudFJlZixcbiAgICBwcml2YXRlIHNjcmlwdExvYWRlclNlcnZpY2U6IFNjcmlwdExvYWRlclNlcnZpY2UsXG4gICAgcHJpdmF0ZSBkYXRhVGFibGVTZXJ2aWNlOiBEYXRhVGFibGVTZXJ2aWNlLFxuICAgIEBPcHRpb25hbCgpIHByaXZhdGUgZGFzaGJvYXJkPzogRGFzaGJvYXJkQ29tcG9uZW50XG4gICkge31cblxuICBwdWJsaWMgZ2V0IGNoYXJ0KCk6IGdvb2dsZS52aXN1YWxpemF0aW9uLkNoYXJ0QmFzZSB8IG51bGwge1xuICAgIHJldHVybiB0aGlzLmNoYXJ0V3JhcHBlci5nZXRDaGFydCgpO1xuICB9XG5cbiAgcHVibGljIGdldCB3cmFwcGVyUmVhZHkkKCk6IE9ic2VydmFibGU8Z29vZ2xlLnZpc3VhbGl6YXRpb24uQ2hhcnRXcmFwcGVyPiB7XG4gICAgcmV0dXJuIHRoaXMud3JhcHBlclJlYWR5U3ViamVjdC5hc09ic2VydmFibGUoKTtcbiAgfVxuXG4gIHB1YmxpYyBnZXQgY2hhcnRXcmFwcGVyKCk6IGdvb2dsZS52aXN1YWxpemF0aW9uLkNoYXJ0V3JhcHBlciB7XG4gICAgaWYgKCF0aGlzLndyYXBwZXIpIHtcbiAgICAgIHRocm93IG5ldyBFcnJvcignVHJ5aW5nIHRvIGFjY2VzcyB0aGUgY2hhcnQgd3JhcHBlciBiZWZvcmUgaXQgd2FzIGZ1bGx5IGluaXRpYWxpemVkJyk7XG4gICAgfVxuXG4gICAgcmV0dXJuIHRoaXMud3JhcHBlcjtcbiAgfVxuXG4gIHB1YmxpYyBzZXQgY2hhcnRXcmFwcGVyKHdyYXBwZXI6IGdvb2dsZS52aXN1YWxpemF0aW9uLkNoYXJ0V3JhcHBlcikge1xuICAgIHRoaXMud3JhcHBlciA9IHdyYXBwZXI7XG4gICAgdGhpcy5kcmF3Q2hhcnQoKTtcbiAgfVxuXG4gIHB1YmxpYyBuZ09uSW5pdCgpIHtcbiAgICAvLyBXZSBkb24ndCBuZWVkIHRvIGxvYWQgYW55IGNoYXJ0IHBhY2thZ2VzLCB0aGUgY2hhcnQgd3JhcHBlciB3aWxsIGhhbmRsZSB0aGlzIGZvciB1c1xuICAgIHRoaXMuc2NyaXB0TG9hZGVyU2VydmljZS5sb2FkQ2hhcnRQYWNrYWdlcyhnZXRQYWNrYWdlRm9yQ2hhcnQodGhpcy50eXBlKSkuc3Vic2NyaWJlKCgpID0+IHtcbiAgICAgIHRoaXMuZGF0YVRhYmxlID0gdGhpcy5kYXRhVGFibGVTZXJ2aWNlLmNyZWF0ZSh0aGlzLmRhdGEsIHRoaXMuY29sdW1ucywgdGhpcy5mb3JtYXR0ZXJzKTtcblxuICAgICAgLy8gT25seSBldmVyIGNyZWF0ZSB0aGUgd3JhcHBlciBvbmNlIHRvIGFsbG93IGFuaW1hdGlvbnMgdG8gaGFwcGVuIHdoZW4gc29tZXRoaW5nIGNoYW5nZXMuXG4gICAgICB0aGlzLndyYXBwZXIgPSBuZXcgZ29vZ2xlLnZpc3VhbGl6YXRpb24uQ2hhcnRXcmFwcGVyKHtcbiAgICAgICAgY29udGFpbmVyOiB0aGlzLmVsZW1lbnQubmF0aXZlRWxlbWVudCxcbiAgICAgICAgY2hhcnRUeXBlOiB0aGlzLnR5cGUsXG4gICAgICAgIGRhdGFUYWJsZTogdGhpcy5kYXRhVGFibGUsXG4gICAgICAgIG9wdGlvbnM6IHRoaXMubWVyZ2VPcHRpb25zKClcbiAgICAgIH0pO1xuXG4gICAgICB0aGlzLnJlZ2lzdGVyQ2hhcnRFdmVudHMoKTtcblxuICAgICAgdGhpcy53cmFwcGVyUmVhZHlTdWJqZWN0Lm5leHQodGhpcy53cmFwcGVyKTtcbiAgICAgIHRoaXMuaW5pdGlhbGl6ZWQgPSB0cnVlO1xuXG4gICAgICB0aGlzLmRyYXdDaGFydCgpO1xuICAgIH0pO1xuICB9XG5cbiAgcHVibGljIG5nT25DaGFuZ2VzKGNoYW5nZXM6IFNpbXBsZUNoYW5nZXMpIHtcbiAgICBpZiAoY2hhbmdlcy5keW5hbWljUmVzaXplKSB7XG4gICAgICB0aGlzLnVwZGF0ZVJlc2l6ZUxpc3RlbmVyKCk7XG4gICAgfVxuXG4gICAgaWYgKHRoaXMuaW5pdGlhbGl6ZWQpIHtcbiAgICAgIGxldCBzaG91bGRSZWRyYXcgPSBmYWxzZTtcbiAgICAgIGlmIChjaGFuZ2VzLmRhdGEgfHwgY2hhbmdlcy5jb2x1bW5zIHx8IGNoYW5nZXMuZm9ybWF0dGVycykge1xuICAgICAgICB0aGlzLmRhdGFUYWJsZSA9IHRoaXMuZGF0YVRhYmxlU2VydmljZS5jcmVhdGUodGhpcy5kYXRhLCB0aGlzLmNvbHVtbnMsIHRoaXMuZm9ybWF0dGVycyk7XG4gICAgICAgIHRoaXMud3JhcHBlciEuc2V0RGF0YVRhYmxlKHRoaXMuZGF0YVRhYmxlISk7XG4gICAgICAgIHNob3VsZFJlZHJhdyA9IHRydWU7XG4gICAgICB9XG5cbiAgICAgIGlmIChjaGFuZ2VzLnR5cGUpIHtcbiAgICAgICAgdGhpcy53cmFwcGVyIS5zZXRDaGFydFR5cGUodGhpcy50eXBlKTtcbiAgICAgICAgc2hvdWxkUmVkcmF3ID0gdHJ1ZTtcbiAgICAgIH1cblxuICAgICAgaWYgKGNoYW5nZXMub3B0aW9ucyB8fCBjaGFuZ2VzLndpZHRoIHx8IGNoYW5nZXMuaGVpZ2h0IHx8IGNoYW5nZXMudGl0bGUpIHtcbiAgICAgICAgdGhpcy53cmFwcGVyIS5zZXRPcHRpb25zKHRoaXMubWVyZ2VPcHRpb25zKCkpO1xuICAgICAgICBzaG91bGRSZWRyYXcgPSB0cnVlO1xuICAgICAgfVxuXG4gICAgICBpZiAoc2hvdWxkUmVkcmF3KSB7XG4gICAgICAgIHRoaXMuZHJhd0NoYXJ0KCk7XG4gICAgICB9XG4gICAgfVxuICB9XG5cbiAgcHVibGljIG5nT25EZXN0cm95KCk6IHZvaWQge1xuICAgIHRoaXMudW5zdWJzY3JpYmVUb1Jlc2l6ZUlmU3Vic2NyaWJlZCgpO1xuICB9XG5cbiAgLyoqXG4gICAqIEZvciBsaXN0ZW5pbmcgdG8gZXZlbnRzIG90aGVyIHRoYW4gdGhlIG1vc3QgY29tbW9uIG9uZXMgKGF2YWlsYWJsZSB2aWEgT3V0cHV0IHByb3BlcnRpZXMpLlxuICAgKlxuICAgKiBDYW4gYmUgY2FsbGVkIGFmdGVyIHRoZSBjaGFydCBlbWl0cyB0aGF0IGl0J3MgXCJyZWFkeVwiLlxuICAgKlxuICAgKiBSZXR1cm5zIGEgaGFuZGxlIHRoYXQgY2FuIGJlIHVzZWQgZm9yIGByZW1vdmVFdmVudExpc3RlbmVyYC5cbiAgICovXG4gIHB1YmxpYyBhZGRFdmVudExpc3RlbmVyKGV2ZW50TmFtZTogc3RyaW5nLCBjYWxsYmFjazogRnVuY3Rpb24pOiBhbnkge1xuICAgIGNvbnN0IGhhbmRsZSA9IHRoaXMucmVnaXN0ZXJDaGFydEV2ZW50KHRoaXMuY2hhcnQsIGV2ZW50TmFtZSwgY2FsbGJhY2spO1xuICAgIHRoaXMuZXZlbnRMaXN0ZW5lcnMuc2V0KGhhbmRsZSwgeyBldmVudE5hbWUsIGNhbGxiYWNrLCBoYW5kbGUgfSk7XG4gICAgcmV0dXJuIGhhbmRsZTtcbiAgfVxuXG4gIHB1YmxpYyByZW1vdmVFdmVudExpc3RlbmVyKGhhbmRsZTogYW55KTogdm9pZCB7XG4gICAgY29uc3QgZW50cnkgPSB0aGlzLmV2ZW50TGlzdGVuZXJzLmdldChoYW5kbGUpO1xuICAgIGlmIChlbnRyeSkge1xuICAgICAgZ29vZ2xlLnZpc3VhbGl6YXRpb24uZXZlbnRzLnJlbW92ZUxpc3RlbmVyKGVudHJ5LmhhbmRsZSk7XG4gICAgICB0aGlzLmV2ZW50TGlzdGVuZXJzLmRlbGV0ZShoYW5kbGUpO1xuICAgIH1cbiAgfVxuXG4gIHByaXZhdGUgdXBkYXRlUmVzaXplTGlzdGVuZXIoKSB7XG4gICAgdGhpcy51bnN1YnNjcmliZVRvUmVzaXplSWZTdWJzY3JpYmVkKCk7XG5cbiAgICBpZiAodGhpcy5keW5hbWljUmVzaXplKSB7XG4gICAgICB0aGlzLnJlc2l6ZVN1YnNjcmlwdGlvbiA9IGZyb21FdmVudCh3aW5kb3csICdyZXNpemUnLCB7IHBhc3NpdmU6IHRydWUgfSlcbiAgICAgICAgLnBpcGUoZGVib3VuY2VUaW1lKDEwMCkpXG4gICAgICAgIC5zdWJzY3JpYmUoKCkgPT4ge1xuICAgICAgICAgIGlmICh0aGlzLmluaXRpYWxpemVkKSB7XG4gICAgICAgICAgICB0aGlzLmRyYXdDaGFydCgpO1xuICAgICAgICAgIH1cbiAgICAgICAgfSk7XG4gICAgfVxuICB9XG5cbiAgcHJpdmF0ZSB1bnN1YnNjcmliZVRvUmVzaXplSWZTdWJzY3JpYmVkKCkge1xuICAgIGlmICh0aGlzLnJlc2l6ZVN1YnNjcmlwdGlvbiAhPSBudWxsKSB7XG4gICAgICB0aGlzLnJlc2l6ZVN1YnNjcmlwdGlvbi51bnN1YnNjcmliZSgpO1xuICAgICAgdGhpcy5yZXNpemVTdWJzY3JpcHRpb24gPSB1bmRlZmluZWQ7XG4gICAgfVxuICB9XG5cbiAgcHJpdmF0ZSBtZXJnZU9wdGlvbnMoKTogb2JqZWN0IHtcbiAgICByZXR1cm4ge1xuICAgICAgdGl0bGU6IHRoaXMudGl0bGUsXG4gICAgICB3aWR0aDogdGhpcy53aWR0aCxcbiAgICAgIGhlaWdodDogdGhpcy5oZWlnaHQsXG4gICAgICAuLi50aGlzLm9wdGlvbnNcbiAgICB9O1xuICB9XG5cbiAgcHJpdmF0ZSByZWdpc3RlckNoYXJ0RXZlbnRzKCkge1xuICAgIGdvb2dsZS52aXN1YWxpemF0aW9uLmV2ZW50cy5yZW1vdmVBbGxMaXN0ZW5lcnModGhpcy53cmFwcGVyKTtcblxuICAgIHRoaXMucmVnaXN0ZXJDaGFydEV2ZW50KHRoaXMud3JhcHBlciwgJ3JlYWR5JywgKCkgPT4ge1xuICAgICAgLy8gVGhpcyBjb3VsZCBhbHNvIGJlIGRvbmUgYnkgY2hlY2tpbmcgaWYgd2UgYWxyZWFkeSBzdWJzY3JpYmVkIHRvIHRoZSBldmVudHNcbiAgICAgIGdvb2dsZS52aXN1YWxpemF0aW9uLmV2ZW50cy5yZW1vdmVBbGxMaXN0ZW5lcnModGhpcy5jaGFydCk7XG4gICAgICB0aGlzLnJlZ2lzdGVyQ2hhcnRFdmVudCh0aGlzLmNoYXJ0LCAnb25tb3VzZW92ZXInLCAoZXZlbnQ6IENoYXJ0TW91c2VPdmVyRXZlbnQpID0+IHRoaXMubW91c2VvdmVyLmVtaXQoZXZlbnQpKTtcbiAgICAgIHRoaXMucmVnaXN0ZXJDaGFydEV2ZW50KHRoaXMuY2hhcnQsICdvbm1vdXNlb3V0JywgKGV2ZW50OiBDaGFydE1vdXNlTGVhdmVFdmVudCkgPT4gdGhpcy5tb3VzZWxlYXZlLmVtaXQoZXZlbnQpKTtcbiAgICAgIHRoaXMucmVnaXN0ZXJDaGFydEV2ZW50KHRoaXMuY2hhcnQsICdzZWxlY3QnLCAoKSA9PiB7XG4gICAgICAgIGNvbnN0IHNlbGVjdGlvbiA9IHRoaXMuY2hhcnQhLmdldFNlbGVjdGlvbigpO1xuICAgICAgICB0aGlzLnNlbGVjdC5lbWl0KHsgc2VsZWN0aW9uIH0pO1xuICAgICAgfSk7XG4gICAgICB0aGlzLmV2ZW50TGlzdGVuZXJzLmZvckVhY2goeCA9PiAoeC5oYW5kbGUgPSB0aGlzLnJlZ2lzdGVyQ2hhcnRFdmVudCh0aGlzLmNoYXJ0LCB4LmV2ZW50TmFtZSwgeC5jYWxsYmFjaykpKTtcblxuICAgICAgdGhpcy5yZWFkeS5lbWl0KHsgY2hhcnQ6IHRoaXMuY2hhcnQhIH0pO1xuICAgIH0pO1xuXG4gICAgdGhpcy5yZWdpc3RlckNoYXJ0RXZlbnQodGhpcy53cmFwcGVyLCAnZXJyb3InLCAoZXJyb3I6IENoYXJ0RXJyb3JFdmVudCkgPT4gdGhpcy5lcnJvci5lbWl0KGVycm9yKSk7XG4gIH1cblxuICBwcml2YXRlIHJlZ2lzdGVyQ2hhcnRFdmVudChvYmplY3Q6IGFueSwgZXZlbnROYW1lOiBzdHJpbmcsIGNhbGxiYWNrOiBGdW5jdGlvbik6IGFueSB7XG4gICAgcmV0dXJuIGdvb2dsZS52aXN1YWxpemF0aW9uLmV2ZW50cy5hZGRMaXN0ZW5lcihvYmplY3QsIGV2ZW50TmFtZSwgY2FsbGJhY2spO1xuICB9XG5cbiAgcHJpdmF0ZSBkcmF3Q2hhcnQoKSB7XG4gICAgaWYgKHRoaXMuZGFzaGJvYXJkICE9IG51bGwpIHtcbiAgICAgIC8vIElmIHRoaXMgY2hhcnQgaXMgcGFydCBvZiBhIGRhc2hib2FyZCwgdGhlIGRhc2hib2FyZCB0YWtlcyBjYXJlIG9mIGRyYXdpbmdcbiAgICAgIHJldHVybjtcbiAgICB9XG5cbiAgICB0aGlzLndyYXBwZXIhLmRyYXcoKTtcbiAgfVxufVxuIl19