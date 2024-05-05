import { ChangeDetectionStrategy, Component, ElementRef, EventEmitter, Input, Output } from '@angular/core';
import { ReplaySubject } from 'rxjs';
import { ScriptLoaderService } from '../../services/script-loader.service';
import * as i0 from "@angular/core";
import * as i1 from "../../services/script-loader.service";
export class ChartWrapperComponent {
    constructor(element, scriptLoaderService) {
        this.element = element;
        this.scriptLoaderService = scriptLoaderService;
        this.error = new EventEmitter();
        this.ready = new EventEmitter();
        this.select = new EventEmitter();
        this.wrapperReadySubject = new ReplaySubject(1);
        this.initialized = false;
    }
    get chart() {
        return this.chartWrapper.getChart();
    }
    get wrapperReady$() {
        return this.wrapperReadySubject.asObservable();
    }
    get chartWrapper() {
        if (!this.wrapper) {
            throw new Error('Cannot access the chart wrapper before initialization.');
        }
        return this.wrapper;
    }
    set chartWrapper(wrapper) {
        this.wrapper = wrapper;
        this.drawChart();
    }
    ngOnInit() {
        // We don't need to load any chart packages, the chart wrapper will handle this else for us
        this.scriptLoaderService.loadChartPackages().subscribe(() => {
            if (!this.specs) {
                this.specs = {};
            }
            const { containerId, container, ...specs } = this.specs;
            // Only ever create the wrapper once to allow animations to happen if something changes.
            this.wrapper = new google.visualization.ChartWrapper({
                ...specs,
                container: this.element.nativeElement
            });
            this.registerChartEvents();
            this.wrapperReadySubject.next(this.wrapper);
            this.drawChart();
            this.initialized = true;
        });
    }
    ngOnChanges(changes) {
        if (!this.initialized) {
            return;
        }
        if (changes.specs) {
            this.updateChart();
            this.drawChart();
        }
    }
    updateChart() {
        if (!this.specs) {
            // When creating the wrapper with empty specs, the google charts library will show an error
            // If we don't do this, a javascript error will be thrown, which is not as visible to the user
            this.specs = {};
        }
        // The typing here are not correct. These methods accept `undefined` as well.
        // That's why we have to cast to `any`
        this.wrapper.setChartType(this.specs.chartType);
        this.wrapper.setDataTable(this.specs.dataTable);
        this.wrapper.setDataSourceUrl(this.specs.dataSourceUrl);
        this.wrapper.setDataSourceUrl(this.specs.dataSourceUrl);
        this.wrapper.setQuery(this.specs.query);
        this.wrapper.setOptions(this.specs.options);
        this.wrapper.setRefreshInterval(this.specs.refreshInterval);
        this.wrapper.setView(this.specs.view);
    }
    drawChart() {
        if (this.wrapper) {
            this.wrapper.draw();
        }
    }
    registerChartEvents() {
        google.visualization.events.removeAllListeners(this.wrapper);
        const registerChartEvent = (object, eventName, callback) => {
            google.visualization.events.addListener(object, eventName, callback);
        };
        registerChartEvent(this.wrapper, 'ready', () => this.ready.emit({ chart: this.chart }));
        registerChartEvent(this.wrapper, 'error', (error) => this.error.emit(error));
        registerChartEvent(this.wrapper, 'select', () => {
            const selection = this.chart.getSelection();
            this.select.emit({ selection });
        });
    }
}
ChartWrapperComponent.ɵfac = i0.ɵɵngDeclareFactory({ minVersion: "12.0.0", version: "14.0.3", ngImport: i0, type: ChartWrapperComponent, deps: [{ token: i0.ElementRef }, { token: i1.ScriptLoaderService }], target: i0.ɵɵFactoryTarget.Component });
ChartWrapperComponent.ɵcmp = i0.ɵɵngDeclareComponent({ minVersion: "14.0.0", version: "14.0.3", type: ChartWrapperComponent, selector: "chart-wrapper", inputs: { specs: "specs" }, outputs: { error: "error", ready: "ready", select: "select" }, host: { classAttribute: "chart-wrapper" }, exportAs: ["chartWrapper"], usesOnChanges: true, ngImport: i0, template: '', isInline: true, styles: [":host{width:-webkit-fit-content;width:-moz-fit-content;width:fit-content;display:block}\n"], changeDetection: i0.ChangeDetectionStrategy.OnPush });
i0.ɵɵngDeclareClassMetadata({ minVersion: "12.0.0", version: "14.0.3", ngImport: i0, type: ChartWrapperComponent, decorators: [{
            type: Component,
            args: [{ selector: 'chart-wrapper', template: '', host: { class: 'chart-wrapper' }, exportAs: 'chartWrapper', changeDetection: ChangeDetectionStrategy.OnPush, styles: [":host{width:-webkit-fit-content;width:-moz-fit-content;width:fit-content;display:block}\n"] }]
        }], ctorParameters: function () { return [{ type: i0.ElementRef }, { type: i1.ScriptLoaderService }]; }, propDecorators: { specs: [{
                type: Input
            }], error: [{
                type: Output
            }], ready: [{
                type: Output
            }], select: [{
                type: Output
            }] } });
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiY2hhcnQtd3JhcHBlci5jb21wb25lbnQuanMiLCJzb3VyY2VSb290IjoiIiwic291cmNlcyI6WyIuLi8uLi8uLi8uLi8uLi8uLi9saWJzL2FuZ3VsYXItZ29vZ2xlLWNoYXJ0cy9zcmMvbGliL2NvbXBvbmVudHMvY2hhcnQtd3JhcHBlci9jaGFydC13cmFwcGVyLmNvbXBvbmVudC50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiQUFBQSxPQUFPLEVBQ0wsdUJBQXVCLEVBQ3ZCLFNBQVMsRUFDVCxVQUFVLEVBQ1YsWUFBWSxFQUNaLEtBQUssRUFHTCxNQUFNLEVBRVAsTUFBTSxlQUFlLENBQUM7QUFDdkIsT0FBTyxFQUFFLGFBQWEsRUFBRSxNQUFNLE1BQU0sQ0FBQztBQUVyQyxPQUFPLEVBQUUsbUJBQW1CLEVBQUUsTUFBTSxzQ0FBc0MsQ0FBQzs7O0FBWTNFLE1BQU0sT0FBTyxxQkFBcUI7SUF5QmhDLFlBQW9CLE9BQW1CLEVBQVUsbUJBQXdDO1FBQXJFLFlBQU8sR0FBUCxPQUFPLENBQVk7UUFBVSx3QkFBbUIsR0FBbkIsbUJBQW1CLENBQXFCO1FBWmxGLFVBQUssR0FBRyxJQUFJLFlBQVksRUFBbUIsQ0FBQztRQUc1QyxVQUFLLEdBQUcsSUFBSSxZQUFZLEVBQW1CLENBQUM7UUFHNUMsV0FBTSxHQUFHLElBQUksWUFBWSxFQUE4QixDQUFDO1FBR3ZELHdCQUFtQixHQUFHLElBQUksYUFBYSxDQUFvQyxDQUFDLENBQUMsQ0FBQztRQUM5RSxnQkFBVyxHQUFHLEtBQUssQ0FBQztJQUVnRSxDQUFDO0lBRTdGLElBQVcsS0FBSztRQUNkLE9BQU8sSUFBSSxDQUFDLFlBQVksQ0FBQyxRQUFRLEVBQUUsQ0FBQztJQUN0QyxDQUFDO0lBRUQsSUFBVyxhQUFhO1FBQ3RCLE9BQU8sSUFBSSxDQUFDLG1CQUFtQixDQUFDLFlBQVksRUFBRSxDQUFDO0lBQ2pELENBQUM7SUFFRCxJQUFXLFlBQVk7UUFDckIsSUFBSSxDQUFDLElBQUksQ0FBQyxPQUFPLEVBQUU7WUFDakIs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SztzQkFEWCxNQUFNO2dCQUlBLE1BQU07c0JBRFosTUFBTSIsInNvdXJjZXNDb250ZW50IjpbImltcG9ydCB7XG4gIENoYW5nZURldGVjdGlvblN0cmF0ZWd5LFxuICBDb21wb25lbnQsXG4gIEVsZW1lbnRSZWYsXG4gIEV2ZW50RW1pdHRlcixcbiAgSW5wdXQsXG4gIE9uQ2hhbmdlcyxcbiAgT25Jbml0LFxuICBPdXRwdXQsXG4gIFNpbXBsZUNoYW5nZXNcbn0gZnJvbSAnQGFuZ3VsYXIvY29yZSc7XG5pbXBvcnQgeyBSZXBsYXlTdWJqZWN0IH0gZnJvbSAncnhqcyc7XG5cbmltcG9ydCB7IFNjcmlwdExvYWRlclNlcnZpY2UgfSBmcm9tICcuLi8uLi9zZXJ2aWNlcy9zY3JpcHQtbG9hZGVyLnNlcnZpY2UnO1xuaW1wb3J0IHsgQ2hhcnRFcnJvckV2ZW50LCBDaGFydFJlYWR5RXZlbnQsIENoYXJ0U2VsZWN0aW9uQ2hhbmdlZEV2ZW50IH0gZnJvbSAnLi4vLi4vdHlwZXMvZXZlbnRzJztcbmltcG9ydCB7IENoYXJ0QmFzZSB9IGZyb20gJy4uL2NoYXJ0LWJhc2UvY2hhcnQtYmFzZS5jb21wb25lbnQnO1xuXG5AQ29tcG9uZW50KHtcbiAgc2VsZWN0b3I6ICdjaGFydC13cmFwcGVyJyxcbiAgdGVtcGxhdGU6ICcnLFxuICBzdHlsZXM6IFsnOmhvc3QgeyB3aWR0aDogZml0LWNvbnRlbnQ7IGRpc3BsYXk6IGJsb2NrOyB9J10sXG4gIGhvc3Q6IHsgY2xhc3M6ICdjaGFydC13cmFwcGVyJyB9LFxuICBleHBvcnRBczogJ2NoYXJ0V3JhcHBlcicsXG4gIGNoYW5nZURldGVjdGlvbjogQ2hhbmdlRGV0ZWN0aW9uU3RyYXRlZ3kuT25QdXNoXG59KVxuZXhwb3J0IGNsYXNzIENoYXJ0V3JhcHBlckNvbXBvbmVudCBpbXBsZW1lbnRzIENoYXJ0QmFzZSwgT25DaGFuZ2VzLCBPbkluaXQge1xuICAvKipcbiAgICogRWl0aGVyIGEgSlNPTiBvYmplY3QgZGVmaW5pbmcgdGhlIGNoYXJ0LCBvciBhIHNlcmlhbGl6ZWQgc3RyaW5nIHZlcnNpb24gb2YgdGhhdCBvYmplY3QuXG4gICAqIFRoZSBmb3JtYXQgb2YgdGhpcyBvYmplY3QgaXMgc2hvd24gaW4gdGhlXG4gICAqIHtAbGluayBodHRwczovL2RldmVsb3BlcnMuZ29vZ2xlLmNvbS9jaGFydC9pbnRlcmFjdGl2ZS9kb2NzL3JlZmVyZW5jZSNnb29nbGUudmlzdWFsaXphdGlvbi5kcmF3Y2hhcnQgYGRyYXdDaGFydCgpYH0gZG9jdW1lbnRhdGlvbi5cbiAgICpcbiAgICogVGhlIGBjb250YWluZXJgIGFuZCBgY29udGFpbmVySWRgIHdpbGwgYmUgb3ZlcndyaXR0ZW4gYnkgdGhpcyBjb21wb25lbnQgdG8gYWxsb3dcbiAgICogcmVuZGVyaW5nIHRoZSBjaGFydCBpbnRvIHRoZSBjb21wb25lbnRzJyB0ZW1wbGF0ZS5cbiAgICovXG4gIEBJbnB1dCgpXG4gIHB1YmxpYyBzcGVjcz86IGdvb2dsZS52aXN1YWxpemF0aW9uLkNoYXJ0U3BlY3M7XG5cbiAgQE91dHB1dCgpXG4gIHB1YmxpYyBlcnJvciA9IG5ldyBFdmVudEVtaXR0ZXI8Q2hhcnRFcnJvckV2ZW50PigpO1xuXG4gIEBPdXRwdXQoKVxuICBwdWJsaWMgcmVhZHkgPSBuZXcgRXZlbnRFbWl0dGVyPENoYXJ0UmVhZHlFdmVudD4oKTtcblxuICBAT3V0cHV0KClcbiAgcHVibGljIHNlbGVjdCA9IG5ldyBFdmVudEVtaXR0ZXI8Q2hhcnRTZWxlY3Rpb25DaGFuZ2VkRXZlbnQ+KCk7XG5cbiAgcHJpdmF0ZSB3cmFwcGVyOiBnb29nbGUudmlzdWFsaXphdGlvbi5DaGFydFdyYXBwZXIgfCB1bmRlZmluZWQ7XG4gIHByaXZhdGUgd3JhcHBlclJlYWR5U3ViamVjdCA9IG5ldyBSZXBsYXlTdWJqZWN0PGdvb2dsZS52aXN1YWxpemF0aW9uLkNoYXJ0V3JhcHBlcj4oMSk7XG4gIHByaXZhdGUgaW5pdGlhbGl6ZWQgPSBmYWxzZTtcblxuICBjb25zdHJ1Y3Rvcihwcml2YXRlIGVsZW1lbnQ6IEVsZW1lbnRSZWYsIHByaXZhdGUgc2NyaXB0TG9hZGVyU2VydmljZTogU2NyaXB0TG9hZGVyU2VydmljZSkge31cblxuICBwdWJsaWMgZ2V0IGNoYXJ0KCk6IGdvb2dsZS52aXN1YWxpemF0aW9uLkNoYXJ0QmFzZSB8IG51bGwge1xuICAgIHJldHVybiB0aGlzLmNoYXJ0V3JhcHBlci5nZXRDaGFydCgpO1xuICB9XG5cbiAgcHVibGljIGdldCB3cmFwcGVyUmVhZHkkKCkge1xuICAgIHJldHVybiB0aGlzLndyYXBwZXJSZWFkeVN1YmplY3QuYXNPYnNlcnZhYmxlKCk7XG4gIH1cblxuICBwdWJsaWMgZ2V0IGNoYXJ0V3JhcHBlcigpOiBnb29nbGUudmlzdWFsaXphdGlvbi5DaGFydFdyYXBwZXIge1xuICAgIGlmICghdGhpcy53cmFwcGVyKSB7XG4gICAgICB0aHJvdyBuZXcgRXJyb3IoJ0Nhbm5vdCBhY2Nlc3MgdGhlIGNoYXJ0IHdyYXBwZXIgYmVmb3JlIGluaXRpYWxpemF0aW9uLicpO1xuICAgIH1cblxuICAgIHJldHVybiB0aGlzLndyYXBwZXI7XG4gIH1cblxuICBwdWJsaWMgc2V0IGNoYXJ0V3JhcHBlcih3cmFwcGVyOiBnb29nbGUudmlzdWFsaXphdGlvbi5DaGFydFdyYXBwZXIpIHtcbiAgICB0aGlzLndyYXBwZXIgPSB3cmFwcGVyO1xuICAgIHRoaXMuZHJhd0NoYXJ0KCk7XG4gIH1cblxuICBwdWJsaWMgbmdPbkluaXQoKSB7XG4gICAgLy8gV2UgZG9uJ3QgbmVlZCB0byBsb2FkIGFueSBjaGFydCBwYWNrYWdlcywgdGhlIGNoYXJ0IHdyYXBwZXIgd2lsbCBoYW5kbGUgdGhpcyBlbHNlIGZvciB1c1xuICAgIHRoaXMuc2NyaXB0TG9hZGVyU2VydmljZS5sb2FkQ2hhcnRQYWNrYWdlcygpLnN1YnNjcmliZSgoKSA9PiB7XG4gICAgICBpZiAoIXRoaXMuc3BlY3MpIHtcbiAgICAgICAgdGhpcy5zcGVjcyA9IHt9IGFzIGdvb2dsZS52aXN1YWxpemF0aW9uLkNoYXJ0U3BlY3M7XG4gICAgICB9XG5cbiAgICAgIGNvbnN0IHsgY29udGFpbmVySWQsIGNvbnRhaW5lciwgLi4uc3BlY3MgfSA9IHRoaXMuc3BlY3M7XG5cbiAgICAgIC8vIE9ubHkgZXZlciBjcmVhdGUgdGhlIHdyYXBwZXIgb25jZSB0byBhbGxvdyBhbmltYXRpb25zIHRvIGhhcHBlbiBpZiBzb21ldGhpbmcgY2hhbmdlcy5cbiAgICAgIHRoaXMud3JhcHBlciA9IG5ldyBnb29nbGUudmlzdWFsaXphdGlvbi5DaGFydFdyYXBwZXIoe1xuICAgICAgICAuLi5zcGVjcyxcbiAgICAgICAgY29udGFpbmVyOiB0aGlzLmVsZW1lbnQubmF0aXZlRWxlbWVudFxuICAgICAgfSk7XG4gICAgICB0aGlzLnJlZ2lzdGVyQ2hhcnRFdmVudHMoKTtcblxuICAgICAgdGhpcy53cmFwcGVyUmVhZHlTdWJqZWN0Lm5leHQodGhpcy53cmFwcGVyKTtcblxuICAgICAgdGhpcy5kcmF3Q2hhcnQoKTtcbiAgICAgIHRoaXMuaW5pdGlhbGl6ZWQgPSB0cnVlO1xuICAgIH0pO1xuICB9XG5cbiAgcHVibGljIG5nT25DaGFuZ2VzKGNoYW5nZXM6IFNpbXBsZUNoYW5nZXMpIHtcbiAgICBpZiAoIXRoaXMuaW5pdGlhbGl6ZWQpIHtcbiAgICAgIHJldHVybjtcbiAgICB9XG5cbiAgICBpZiAoY2hhbmdlcy5zcGVjcykge1xuICAgICAgdGhpcy51cGRhdGVDaGFydCgpO1xuICAgICAgdGhpcy5kcmF3Q2hhcnQoKTtcbiAgICB9XG4gIH1cblxuICBwcml2YXRlIHVwZGF0ZUNoYXJ0KCkge1xuICAgIGlmICghdGhpcy5zcGVjcykge1xuICAgICAgLy8gV2hlbiBjcmVhdGluZyB0aGUgd3JhcHBlciB3aXRoIGVtcHR5IHNwZWNzLCB0aGUgZ29vZ2xlIGNoYXJ0cyBsaWJyYXJ5IHdpbGwgc2hvdyBhbiBlcnJvclxuICAgICAgLy8gSWYgd2UgZG9uJ3QgZG8gdGhpcywgYSBqYXZhc2NyaXB0IGVycm9yIHdpbGwgYmUgdGhyb3duLCB3aGljaCBpcyBub3QgYXMgdmlzaWJsZSB0byB0aGUgdXNlclxuICAgICAgdGhpcy5zcGVjcyA9IHt9IGFzIGdvb2dsZS52aXN1YWxpemF0aW9uLkNoYXJ0U3BlY3M7XG4gICAgfVxuXG4gICAgLy8gVGhlIHR5cGluZyBoZXJlIGFyZSBub3QgY29ycmVjdC4gVGhlc2UgbWV0aG9kcyBhY2NlcHQgYHVuZGVmaW5lZGAgYXMgd2VsbC5cbiAgICAvLyBUaGF0J3Mgd2h5IHdlIGhhdmUgdG8gY2FzdCB0byBgYW55YFxuXG4gICAgdGhpcy53cmFwcGVyIS5zZXRDaGFydFR5cGUodGhpcy5zcGVjcy5jaGFydFR5cGUpO1xuICAgIHRoaXMud3JhcHBlciEuc2V0RGF0YVRhYmxlKHRoaXMuc3BlY3MuZGF0YVRhYmxlIGFzIGFueSk7XG4gICAgdGhpcy53cmFwcGVyIS5zZXREYXRhU291cmNlVXJsKHRoaXMuc3BlY3MuZGF0YVNvdXJjZVVybCBhcyBhbnkpO1xuICAgIHRoaXMud3JhcHBlciEuc2V0RGF0YVNvdXJjZVVybCh0aGlzLnNwZWNzLmRhdGFTb3VyY2VVcmwgYXMgYW55KTtcbiAgICB0aGlzLndyYXBwZXIhLnNldFF1ZXJ5KHRoaXMuc3BlY3MucXVlcnkgYXMgYW55KTtcbiAgICB0aGlzLndyYXBwZXIhLnNldE9wdGlvbnModGhpcy5zcGVjcy5vcHRpb25zIGFzIGFueSk7XG4gICAgdGhpcy53cmFwcGVyIS5zZXRSZWZyZXNoSW50ZXJ2YWwodGhpcy5zcGVjcy5yZWZyZXNoSW50ZXJ2YWwgYXMgYW55KTtcbiAgICB0aGlzLndyYXBwZXIhLnNldFZpZXcodGhpcy5zcGVjcy52aWV3KTtcbiAgfVxuXG4gIHByaXZhdGUgZHJhd0NoYXJ0KCkge1xuICAgIGlmICh0aGlzLndyYXBwZXIpIHtcbiAgICAgIHRoaXMud3JhcHBlci5kcmF3KCk7XG4gICAgfVxuICB9XG5cbiAgcHJpdmF0ZSByZWdpc3RlckNoYXJ0RXZlbnRzKCkge1xuICAgIGdvb2dsZS52aXN1YWxpemF0aW9uLmV2ZW50cy5yZW1vdmVBbGxMaXN0ZW5lcnModGhpcy53cmFwcGVyKTtcblxuICAgIGNvbnN0IHJlZ2lzdGVyQ2hhcnRFdmVudCA9IChvYmplY3Q6IGFueSwgZXZlbnROYW1lOiBzdHJpbmcsIGNhbGxiYWNrOiBGdW5jdGlvbikgPT4ge1xuICAgICAgZ29vZ2xlLnZpc3VhbGl6YXRpb24uZXZlbnRzLmFkZExpc3RlbmVyKG9iamVjdCwgZXZlbnROYW1lLCBjYWxsYmFjayk7XG4gICAgfTtcblxuICAgIHJlZ2lzdGVyQ2hhcnRFdmVudCh0aGlzLndyYXBwZXIsICdyZWFkeScsICgpID0+IHRoaXMucmVhZHkuZW1pdCh7IGNoYXJ0OiB0aGlzLmNoYXJ0ISB9KSk7XG4gICAgcmVnaXN0ZXJDaGFydEV2ZW50KHRoaXMud3JhcHBlciwgJ2Vycm9yJywgKGVycm9yOiBDaGFydEVycm9yRXZlbnQpID0+IHRoaXMuZXJyb3IuZW1pdChlcnJvcikpO1xuICAgIHJlZ2lzdGVyQ2hhcnRFdmVudCh0aGlzLndyYXBwZXIsICdzZWxlY3QnLCAoKSA9PiB7XG4gICAgICBjb25zdCBzZWxlY3Rpb24gPSB0aGlzLmNoYXJ0IS5nZXRTZWxlY3Rpb24oKTtcbiAgICAgIHRoaXMuc2VsZWN0LmVtaXQoeyBzZWxlY3Rpb24gfSk7XG4gICAgfSk7XG4gIH1cbn1cbiJdfQ==