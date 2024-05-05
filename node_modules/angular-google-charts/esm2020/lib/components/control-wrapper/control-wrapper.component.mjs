import { ChangeDetectionStrategy, Component, EventEmitter, HostBinding, Input, Output } from '@angular/core';
import { ReplaySubject } from 'rxjs';
import { generateRandomId } from '../../helpers/id.helper';
import { ScriptLoaderService } from '../../services/script-loader.service';
import { FilterType } from '../../types/control-type';
import * as i0 from "@angular/core";
import * as i1 from "../../services/script-loader.service";
export class ControlWrapperComponent {
    constructor(loaderService) {
        this.loaderService = loaderService;
        /**
         * Emits when an error occurs when attempting to render the control.
         */
        this.error = new EventEmitter();
        /**
         * The control is ready to accept user interaction and for external method calls.
         *
         * Alternatively, you can listen for a ready event on the dashboard holding the control
         * and call control methods only after the event was fired.
         */
        this.ready = new EventEmitter();
        /**
         * Emits when the user interacts with the control, affecting its state.
         * For example, a `stateChange` event will be emitted whenever you move the thumbs of a range slider control.
         *
         * To retrieve an updated control state after the event fired, call `ControlWrapper.getState()`.
         */
        this.stateChange = new EventEmitter();
        /**
         * A generated id assigned to this components DOM element.
         */
        this.id = generateRandomId();
        this.wrapperReadySubject = new ReplaySubject(1);
    }
    /**
     * Emits after the `ControlWrapper` was created.
     */
    get wrapperReady$() {
        return this.wrapperReadySubject.asObservable();
    }
    get controlWrapper() {
        if (!this._controlWrapper) {
            throw new Error(`Cannot access the control wrapper before it being initialized.`);
        }
        return this._controlWrapper;
    }
    ngOnInit() {
        this.loaderService.loadChartPackages('controls').subscribe(() => {
            this.createControlWrapper();
        });
    }
    ngOnChanges(changes) {
        if (!this._controlWrapper) {
            return;
        }
        if (changes.type) {
            this._controlWrapper.setControlType(this.type);
        }
        if (changes.options) {
            this._controlWrapper.setOptions(this.options || {});
        }
        if (changes.state) {
            this._controlWrapper.setState(this.state || {});
        }
    }
    createControlWrapper() {
        this._controlWrapper = new google.visualization.ControlWrapper({
            containerId: this.id,
            controlType: this.type,
            state: this.state,
            options: this.options
        });
        this.addEventListeners();
        this.wrapperReadySubject.next(this._controlWrapper);
    }
    addEventListeners() {
        google.visualization.events.removeAllListeners(this._controlWrapper);
        google.visualization.events.addListener(this._controlWrapper, 'ready', (event) => this.ready.emit(event));
        google.visualization.events.addListener(this._controlWrapper, 'error', (event) => this.error.emit(event));
        google.visualization.events.addListener(this._controlWrapper, 'statechange', (event) => this.stateChange.emit(event));
    }
}
ControlWrapperComponent.ɵfac = i0.ɵɵngDeclareFactory({ minVersion: "12.0.0", version: "14.0.3", ngImport: i0, type: ControlWrapperComponent, deps: [{ token: i1.ScriptLoaderService }], target: i0.ɵɵFactoryTarget.Component });
ControlWrapperComponent.ɵcmp = i0.ɵɵngDeclareComponent({ minVersion: "14.0.0", version: "14.0.3", type: ControlWrapperComponent, selector: "control-wrapper", inputs: { for: "for", type: "type", options: "options", state: "state" }, outputs: { error: "error", ready: "ready", stateChange: "stateChange" }, host: { properties: { "id": "this.id" }, classAttribute: "control-wrapper" }, exportAs: ["controlWrapper"], usesOnChanges: true, ngImport: i0, template: '', isInline: true, changeDetection: i0.ChangeDetectionStrategy.OnPush });
i0.ɵɵngDeclareClassMetadata({ minVersion: "12.0.0", version: "14.0.3", ngImport: i0, type: ControlWrapperComponent, decorators: [{
            type: Component,
            args: [{
                    selector: 'control-wrapper',
                    template: '',
                    host: { class: 'control-wrapper' },
                    exportAs: 'controlWrapper',
                    changeDetection: ChangeDetectionStrategy.OnPush
                }]
        }], ctorParameters: function () { return [{ type: i1.ScriptLoaderService }]; }, propDecorators: { for: [{
                type: Input
            }], type: [{
                type: Input
            }], options: [{
                type: Input
            }], state: [{
                type: Input
            }], error: [{
                type: Output
            }], ready: [{
                type: Output
            }], stateChange: [{
                type: Output
            }], id: [{
                type: HostBinding,
                args: ['id']
            }] } });
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiY29udHJvbC13cmFwcGVyLmNvbXBvbmVudC5qcyIsInNvdXJjZVJvb3QiOiIiLCJzb3VyY2VzIjpbIi4uLy4uLy4uLy4uLy4uLy4uL2xpYnMvYW5ndWxhci1nb29nbGUtY2hhcnRzL3NyYy9saWIvY29tcG9uZW50cy9jb250cm9sLXdyYXBwZXIvY29udHJvbC13cmFwcGVyLmNvbXBvbmVudC50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiQUFBQSxPQUFPLEVBQ0wsdUJBQXVCLEVBQ3ZCLFNBQVMsRUFDVCxZQUFZLEVBQ1osV0FBVyxFQUNYLEtBQUssRUFHTCxNQUFNLEVBRVAsTUFBTSxlQUFlLENBQUM7QUFDdkIsT0FBTyxFQUFFLGFBQWEsRUFBRSxNQUFNLE1BQU0sQ0FBQztBQUVyQyxPQUFPLEVBQUUsZ0JBQWdCLEVBQUUsTUFBTSx5QkFBeUIsQ0FBQztBQUMzRCxPQUFPLEVBQUUsbUJBQW1CLEVBQUUsTUFBTSxzQ0FBc0MsQ0FBQztBQUMzRSxPQUFPLEVBQUUsVUFBVSxFQUFFLE1BQU0sMEJBQTBCLENBQUM7OztBQVd0RCxNQUFNLE9BQU8sdUJBQXVCO0lBbUZsQyxZQUFvQixhQUFrQztRQUFsQyxrQkFBYSxHQUFiLGFBQWEsQ0FBcUI7UUFqQ3REOztXQUVHO1FBRUksVUFBSyxHQUFHLElBQUksWUFBWSxFQUFtQixDQUFDO1FBRW5EOzs7OztXQUtHO1FBRUksVUFBSyxHQUFHLElBQUksWUFBWSxFQUFtQixDQUFDO1FBRW5EOzs7OztXQUtHO1FBRUksZ0JBQVcsR0FBRyxJQUFJLFlBQVksRUFBVyxDQUFDO1FBRWpEOztXQUVHO1FBRWEsT0FBRSxHQUFHLGdCQUFnQixFQUFFLENBQUM7UUFHaEMsd0JBQW1CLEdBQUcsSUFBSSxhQUFhLENBQXNDLENBQUMsQ0FBQyxDQUFDO0lBRS9CLENBQUM7SUFFMUQ7O09BRUc7SUFDSCxJQUFXLGFBQWE7UUFDdEIsT0FBTyxJQUFJLENBQUMsbUJBQW1CLENBQUMsWUFBWSxFQUFFLENBQUM7SUFDakQsQ0FBQztJQUVELElB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SUFBSTtzQkFEVixLQUFLO2dCQWNDLE9BQU87c0JBRGIsS0FBSztnQkFrQkMsS0FBSztzQkFEWCxLQUFLO2dCQU9DLEtBQUs7c0JBRFgsTUFBTTtnQkFVQSxLQUFLO3NCQURYLE1BQU07Z0JBVUEsV0FBVztzQkFEakIsTUFBTTtnQkFPUyxFQUFFO3NCQURqQixXQUFXO3VCQUFDLElBQUkiLCJzb3VyY2VzQ29udGVudCI6WyJpbXBvcnQge1xuICBDaGFuZ2VEZXRlY3Rpb25TdHJhdGVneSxcbiAgQ29tcG9uZW50LFxuICBFdmVudEVtaXR0ZXIsXG4gIEhvc3RCaW5kaW5nLFxuICBJbnB1dCxcbiAgT25DaGFuZ2VzLFxuICBPbkluaXQsXG4gIE91dHB1dCxcbiAgU2ltcGxlQ2hhbmdlc1xufSBmcm9tICdAYW5ndWxhci9jb3JlJztcbmltcG9ydCB7IFJlcGxheVN1YmplY3QgfSBmcm9tICdyeGpzJztcblxuaW1wb3J0IHsgZ2VuZXJhdGVSYW5kb21JZCB9IGZyb20gJy4uLy4uL2hlbHBlcnMvaWQuaGVscGVyJztcbmltcG9ydCB7IFNjcmlwdExvYWRlclNlcnZpY2UgfSBmcm9tICcuLi8uLi9zZXJ2aWNlcy9zY3JpcHQtbG9hZGVyLnNlcnZpY2UnO1xuaW1wb3J0IHsgRmlsdGVyVHlwZSB9IGZyb20gJy4uLy4uL3R5cGVzL2NvbnRyb2wtdHlwZSc7XG5pbXBvcnQgeyBDaGFydEVycm9yRXZlbnQsIENoYXJ0UmVhZHlFdmVudCB9IGZyb20gJy4uLy4uL3R5cGVzL2V2ZW50cyc7XG5pbXBvcnQgeyBDaGFydEJhc2UgfSBmcm9tICcuLi9jaGFydC1iYXNlL2NoYXJ0LWJhc2UuY29tcG9uZW50JztcblxuQENvbXBvbmVudCh7XG4gIHNlbGVjdG9yOiAnY29udHJvbC13cmFwcGVyJyxcbiAgdGVtcGxhdGU6ICcnLFxuICBob3N0OiB7IGNsYXNzOiAnY29udHJvbC13cmFwcGVyJyB9LFxuICBleHBvcnRBczogJ2NvbnRyb2xXcmFwcGVyJyxcbiAgY2hhbmdlRGV0ZWN0aW9uOiBDaGFuZ2VEZXRlY3Rpb25TdHJhdGVneS5PblB1c2hcbn0pXG5leHBvcnQgY2xhc3MgQ29udHJvbFdyYXBwZXJDb21wb25lbnQgaW1wbGVtZW50cyBPbkluaXQsIE9uQ2hhbmdlcyB7XG4gIC8qKlxuICAgKiBDaGFydHMgY29udHJvbGxlZCBieSB0aGlzIGNvbnRyb2wgd3JhcHBlci4gQ2FuIGJlIGEgc2luZ2xlIGNoYXJ0IG9yIGFuIGFycmF5IG9mIGNoYXJ0cy5cbiAgICovXG4gIEBJbnB1dCgpXG4gIHB1YmxpYyBmb3IhOiBDaGFydEJhc2UgfCBDaGFydEJhc2VbXTtcblxuICAvKipcbiAgICogVGhlIGNsYXNzIG5hbWUgb2YgdGhlIGNvbnRyb2wuXG4gICAqIFRoZSBgZ29vZ2xlLnZpc3VhbGl6YXRpb25gIHBhY2thZ2UgbmFtZSBjYW4gYmUgb21pdHRlZCBmb3IgR29vZ2xlIGNvbnRyb2xzLlxuICAgKlxuICAgKiBAZXhhbXBsZVxuICAgKlxuICAgKiBgYGBodG1sXG4gICAqIDxjb250cm9sLXdyYXBwZXIgdHlwZT1cIkNhdGVnb3J5RmlsdGVyXCI+PC9jb250cm9sLXdyYXBwZXI+XG4gICAqIGBgYFxuICAgKi9cbiAgQElucHV0KClcbiAgcHVibGljIHR5cGUhOiBGaWx0ZXJUeXBlO1xuXG4gIC8qKlxuICAgKiBBbiBvYmplY3QgZGVzY3JpYmluZyB0aGUgb3B0aW9ucyBmb3IgdGhlIGNvbnRyb2wuXG4gICAqIFlvdSBjYW4gdXNlIGVpdGhlciBKYXZhU2NyaXB0IGxpdGVyYWwgbm90YXRpb24sIG9yIHByb3ZpZGUgYSBoYW5kbGUgdG8gdGhlIG9iamVjdC5cbiAgICpcbiAgICogQGV4YW1wbGVcbiAgICpcbiAgICogYGBgaHRtbFxuICAgKiA8Y29udHJvbC13cmFwcGVyIFtvcHRpb25zXT1cInsnZmlsdGVyQ29sdW1uTGFiZWwnOiAnQWdlJywgJ21pblZhbHVlJzogMTAsICdtYXhWYWx1ZSc6IDgwfVwiPjwvY29udHJvbC13cmFwcGVyPlxuICAgKiBgYGBcbiAgICovXG4gIEBJbnB1dCgpXG4gIHB1YmxpYyBvcHRpb25zPzogb2JqZWN0O1xuXG4gIC8qKlxuICAgKiBBbiBvYmplY3QgZGVzY3JpYmluZyB0aGUgc3RhdGUgb2YgdGhlIGNvbnRyb2wuXG4gICAqIFRoZSBzdGF0ZSBjb2xsZWN0cyBhbGwgdGhlIHZhcmlhYmxlcyB0aGF0IHRoZSB1c2VyIG9wZXJhdGluZyB0aGUgY29udHJvbCBjYW4gYWZmZWN0LlxuICAgKlxuICAgKiBGb3IgZXhhbXBsZSwgYSByYW5nZSBzbGlkZXIgc3RhdGUgY2FuIGJlIGRlc2NyaWJlZCBpbiB0ZXJtIG9mIHRoZSBwb3NpdGlvbnMgdGhhdCB0aGUgbG93IGFuZCBoaWdoIHRodW1iXG4gICAqIG9mIHRoZSBzbGlkZXIgb2NjdXB5LlxuICAgKiBZb3UgY2FuIHVzZSBlaXRoZXIgSmF2YXNjcmlwdCBsaXRlcmFsIG5vdGF0aW9uLCBvciBwcm92aWRlIGEgaGFuZGxlIHRvIHRoZSBvYmplY3QuXG4gICAqXG4gICAqIEBleGFtcGxlXG4gICAqXG4gICAqICBgYGBodG1sXG4gICAqIDxjb250cm9sLXdyYXBwZXIgW3N0YXRlXT1cInsnbG93VmFsdWUnOiAyMCwgJ2hpZ2hWYWx1ZSc6IDUwfVwiPjwvY29udHJvbC13cmFwcGVyPlxuICAgKiBgYGBcbiAgICovXG4gIEBJbnB1dCgpXG4gIHB1YmxpYyBzdGF0ZT86IG9iamVjdDtcblxuICAvKipcbiAgICogRW1pdHMgd2hlbiBhbiBlcnJvciBvY2N1cnMgd2hlbiBhdHRlbXB0aW5nIHRvIHJlbmRlciB0aGUgY29udHJvbC5cbiAgICovXG4gIEBPdXRwdXQoKVxuICBwdWJsaWMgZXJyb3IgPSBuZXcgRXZlbnRFbWl0dGVyPENoYXJ0RXJyb3JFdmVudD4oKTtcblxuICAvKipcbiAgICogVGhlIGNvbnRyb2wgaXMgcmVhZHkgdG8gYWNjZXB0IHVzZXIgaW50ZXJhY3Rpb24gYW5kIGZvciBleHRlcm5hbCBtZXRob2QgY2FsbHMuXG4gICAqXG4gICAqIEFsdGVybmF0aXZlbHksIHlvdSBjYW4gbGlzdGVuIGZvciBhIHJlYWR5IGV2ZW50IG9uIHRoZSBkYXNoYm9hcmQgaG9sZGluZyB0aGUgY29udHJvbFxuICAgKiBhbmQgY2FsbCBjb250cm9sIG1ldGhvZHMgb25seSBhZnRlciB0aGUgZXZlbnQgd2FzIGZpcmVkLlxuICAgKi9cbiAgQE91dHB1dCgpXG4gIHB1YmxpYyByZWFkeSA9IG5ldyBFdmVudEVtaXR0ZXI8Q2hhcnRSZWFkeUV2ZW50PigpO1xuXG4gIC8qKlxuICAgKiBFbWl0cyB3aGVuIHRoZSB1c2VyIGludGVyYWN0cyB3aXRoIHRoZSBjb250cm9sLCBhZmZlY3RpbmcgaXRzIHN0YXRlLlxuICAgKiBGb3IgZXhhbXBsZSwgYSBgc3RhdGVDaGFuZ2VgIGV2ZW50IHdpbGwgYmUgZW1pdHRlZCB3aGVuZXZlciB5b3UgbW92ZSB0aGUgdGh1bWJzIG9mIGEgcmFuZ2Ugc2xpZGVyIGNvbnRyb2wuXG4gICAqXG4gICAqIFRvIHJldHJpZXZlIGFuIHVwZGF0ZWQgY29udHJvbCBzdGF0ZSBhZnRlciB0aGUgZXZlbnQgZmlyZWQsIGNhbGwgYENvbnRyb2xXcmFwcGVyLmdldFN0YXRlKClgLlxuICAgKi9cbiAgQE91dHB1dCgpXG4gIHB1YmxpYyBzdGF0ZUNoYW5nZSA9IG5ldyBFdmVudEVtaXR0ZXI8dW5rbm93bj4oKTtcblxuICAvKipcbiAgICogQSBnZW5lcmF0ZWQgaWQgYXNzaWduZWQgdG8gdGhpcyBjb21wb25lbnRzIERPTSBlbGVtZW50LlxuICAgKi9cbiAgQEhvc3RCaW5kaW5nKCdpZCcpXG4gIHB1YmxpYyByZWFkb25seSBpZCA9IGdlbmVyYXRlUmFuZG9tSWQoKTtcblxuICBwcml2YXRlIF9jb250cm9sV3JhcHBlcj86IGdvb2dsZS52aXN1YWxpemF0aW9uLkNvbnRyb2xXcmFwcGVyO1xuICBwcml2YXRlIHdyYXBwZXJSZWFkeVN1YmplY3QgPSBuZXcgUmVwbGF5U3ViamVjdDxnb29nbGUudmlzdWFsaXphdGlvbi5Db250cm9sV3JhcHBlcj4oMSk7XG5cbiAgY29uc3RydWN0b3IocHJpdmF0ZSBsb2FkZXJTZXJ2aWNlOiBTY3JpcHRMb2FkZXJTZXJ2aWNlKSB7fVxuXG4gIC8qKlxuICAgKiBFbWl0cyBhZnRlciB0aGUgYENvbnRyb2xXcmFwcGVyYCB3YXMgY3JlYXRlZC5cbiAgICovXG4gIHB1YmxpYyBnZXQgd3JhcHBlclJlYWR5JCgpIHtcbiAgICByZXR1cm4gdGhpcy53cmFwcGVyUmVhZHlTdWJqZWN0LmFzT2JzZXJ2YWJsZSgpO1xuICB9XG5cbiAgcHVibGljIGdldCBjb250cm9sV3JhcHBlcigpOiBnb29nbGUudmlzdWFsaXphdGlvbi5Db250cm9sV3JhcHBlciB7XG4gICAgaWYgKCF0aGlzLl9jb250cm9sV3JhcHBlcikge1xuICAgICAgdGhyb3cgbmV3IEVycm9yKGBDYW5ub3QgYWNjZXNzIHRoZSBjb250cm9sIHdyYXBwZXIgYmVmb3JlIGl0IGJlaW5nIGluaXRpYWxpemVkLmApO1xuICAgIH1cblxuICAgIHJldHVybiB0aGlzLl9jb250cm9sV3JhcHBlcjtcbiAgfVxuXG4gIHB1YmxpYyBuZ09uSW5pdCgpIHtcbiAgICB0aGlzLmxvYWRlclNlcnZpY2UubG9hZENoYXJ0UGFja2FnZXMoJ2NvbnRyb2xzJykuc3Vic2NyaWJlKCgpID0+IHtcbiAgICAgIHRoaXMuY3JlYXRlQ29udHJvbFdyYXBwZXIoKTtcbiAgICB9KTtcbiAgfVxuXG4gIHB1YmxpYyBuZ09uQ2hhbmdlcyhjaGFuZ2VzOiBTaW1wbGVDaGFuZ2VzKTogdm9pZCB7XG4gICAgaWYgKCF0aGlzLl9jb250cm9sV3JhcHBlcikge1xuICAgICAgcmV0dXJuO1xuICAgIH1cblxuICAgIGlmIChjaGFuZ2VzLnR5cGUpIHtcbiAgICAgIHRoaXMuX2NvbnRyb2xXcmFwcGVyLnNldENvbnRyb2xUeXBlKHRoaXMudHlwZSk7XG4gICAgfVxuXG4gICAgaWYgKGNoYW5nZXMub3B0aW9ucykge1xuICAgICAgdGhpcy5fY29udHJvbFdyYXBwZXIuc2V0T3B0aW9ucyh0aGlzLm9wdGlvbnMgfHwge30pO1xuICAgIH1cblxuICAgIGlmIChjaGFuZ2VzLnN0YXRlKSB7XG4gICAgICB0aGlzLl9jb250cm9sV3JhcHBlci5zZXRTdGF0ZSh0aGlzLnN0YXRlIHx8IHt9KTtcbiAgICB9XG4gIH1cblxuICBwcml2YXRlIGNyZWF0ZUNvbnRyb2xXcmFwcGVyKCkge1xuICAgIHRoaXMuX2NvbnRyb2xXcmFwcGVyID0gbmV3IGdvb2dsZS52aXN1YWxpemF0aW9uLkNvbnRyb2xXcmFwcGVyKHtcbiAgICAgIGNvbnRhaW5lcklkOiB0aGlzLmlkLFxuICAgICAgY29udHJvbFR5cGU6IHRoaXMudHlwZSxcbiAgICAgIHN0YXRlOiB0aGlzLnN0YXRlLFxuICAgICAgb3B0aW9uczogdGhpcy5vcHRpb25zXG4gICAgfSk7XG5cbiAgICB0aGlzLmFkZEV2ZW50TGlzdGVuZXJzKCk7XG4gICAgdGhpcy53cmFwcGVyUmVhZHlTdWJqZWN0Lm5leHQodGhpcy5fY29udHJvbFdyYXBwZXIpO1xuICB9XG5cbiAgcHJpdmF0ZSBhZGRFdmVudExpc3RlbmVycygpIHtcbiAgICBnb29nbGUudmlzdWFsaXphdGlvbi5ldmVudHMucmVtb3ZlQWxsTGlzdGVuZXJzKHRoaXMuX2NvbnRyb2xXcmFwcGVyKTtcblxuICAgIGdvb2dsZS52aXN1YWxpemF0aW9uLmV2ZW50cy5hZGRMaXN0ZW5lcih0aGlzLl9jb250cm9sV3JhcHBlciwgJ3JlYWR5JywgKGV2ZW50OiBDaGFydFJlYWR5RXZlbnQpID0+XG4gICAgICB0aGlzLnJlYWR5LmVtaXQoZXZlbnQpXG4gICAgKTtcbiAgICBnb29nbGUudmlzdWFsaXphdGlvbi5ldmVudHMuYWRkTGlzdGVuZXIodGhpcy5fY29udHJvbFdyYXBwZXIsICdlcnJvcicsIChldmVudDogQ2hhcnRFcnJvckV2ZW50KSA9PlxuICAgICAgdGhpcy5lcnJvci5lbWl0KGV2ZW50KVxuICAgICk7XG4gICAgZ29vZ2xlLnZpc3VhbGl6YXRpb24uZXZlbnRzLmFkZExpc3RlbmVyKHRoaXMuX2NvbnRyb2xXcmFwcGVyLCAnc3RhdGVjaGFuZ2UnLCAoZXZlbnQ6IHVua25vd24pID0+XG4gICAgICB0aGlzLnN0YXRlQ2hhbmdlLmVtaXQoZXZlbnQpXG4gICAgKTtcbiAgfVxufVxuIl19