/// <reference path="./types.ts" />
/// <reference path="./types.ts" />
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { Subject } from 'rxjs';
import { ScriptLoaderService } from '../../services/script-loader.service';
import { ChartEditorRef } from './chart-editor-ref';
import * as i0 from "@angular/core";
import * as i1 from "../../services/script-loader.service";
export class ChartEditorComponent {
    constructor(scriptLoaderService) {
        this.scriptLoaderService = scriptLoaderService;
        this.initializedSubject = new Subject();
    }
    /**
     * Emits as soon as the chart editor is fully initialized.
     */
    get initialized$() {
        return this.initializedSubject.asObservable();
    }
    ngOnInit() {
        this.scriptLoaderService.loadChartPackages('charteditor').subscribe(() => {
            this.editor = new google.visualization.ChartEditor();
            this.initializedSubject.next(this.editor);
            this.initializedSubject.complete();
        });
    }
    editChart(component, options) {
        if (!component.chartWrapper) {
            throw new Error('Chart wrapper is `undefined`. Please wait for the `initialized$` observable before trying to edit a chart.');
        }
        if (!this.editor) {
            throw new Error('Chart editor is `undefined`. Please wait for the `initialized$` observable before trying to edit a chart.');
        }
        const handle = new ChartEditorRef(this.editor);
        this.editor.openDialog(component.chartWrapper, options || {});
        handle.afterClosed().subscribe(result => {
            if (result) {
                component.chartWrapper = result;
            }
        });
        return handle;
    }
}
ChartEditorComponent.ɵfac = i0.ɵɵngDeclareFactory({ minVersion: "12.0.0", version: "14.0.3", ngImport: i0, type: ChartEditorComponent, deps: [{ token: i1.ScriptLoaderService }], target: i0.ɵɵFactoryTarget.Component });
ChartEditorComponent.ɵcmp = i0.ɵɵngDeclareComponent({ minVersion: "14.0.0", version: "14.0.3", type: ChartEditorComponent, selector: "chart-editor", host: { classAttribute: "chart-editor" }, ngImport: i0, template: `<ng-content></ng-content>`, isInline: true, changeDetection: i0.ChangeDetectionStrategy.OnPush });
i0.ɵɵngDeclareClassMetadata({ minVersion: "12.0.0", version: "14.0.3", ngImport: i0, type: ChartEditorComponent, decorators: [{
            type: Component,
            args: [{
                    selector: 'chart-editor',
                    template: `<ng-content></ng-content>`,
                    host: { class: 'chart-editor' },
                    changeDetection: ChangeDetectionStrategy.OnPush
                }]
        }], ctorParameters: function () { return [{ type: i1.ScriptLoaderService }]; } });
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiY2hhcnQtZWRpdG9yLmNvbXBvbmVudC5qcyIsInNvdXJjZVJvb3QiOiIiLCJzb3VyY2VzIjpbIi4uLy4uLy4uLy4uLy4uLy4uL2xpYnMvYW5ndWxhci1nb29nbGUtY2hhcnRzL3NyYy9saWIvY29tcG9uZW50cy9jaGFydC1lZGl0b3IvY2hhcnQtZWRpdG9yLmNvbXBvbmVudC50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiQUFBQSxtQ0FBbUM7QUFBbkMsbUNBQW1DO0FBRW5DLE9BQU8sRUFBRSx1QkFBdUIsRUFBRSxTQUFTLEVBQVUsTUFBTSxlQUFlLENBQUM7QUFDM0UsT0FBTyxFQUFFLE9BQU8sRUFBRSxNQUFNLE1BQU0sQ0FBQztBQUUvQixPQUFPLEVBQUUsbUJBQW1CLEVBQUUsTUFBTSxzQ0FBc0MsQ0FBQztBQUczRSxPQUFPLEVBQUUsY0FBYyxFQUFFLE1BQU0sb0JBQW9CLENBQUM7OztBQVFwRCxNQUFNLE9BQU8sb0JBQW9CO0lBSS9CLFlBQW9CLG1CQUF3QztRQUF4Qyx3QkFBbUIsR0FBbkIsbUJBQW1CLENBQXFCO1FBRnBELHVCQUFrQixHQUFHLElBQUksT0FBTyxFQUFvQyxDQUFDO0lBRWQsQ0FBQztJQUVoRTs7T0FFRztJQUNILElBQVcsWUFBWTtRQUNyQixPQUFPLElBQUksQ0FBQyxrQkFBa0IsQ0FBQyxZQUFZLEVBQUUsQ0FBQztJQUNoRCxDQUFDO0lBRU0sUUFBUTtRQUNiLElBQUksQ0FBQyxtQkFBbUIsQ0FBQyxpQkFBaUIsQ0FBQyxhQUFhLENBQUMsQ0FBQyxTQUFTLENBQUMsR0FBRyxFQUFFO1lBQ3ZFLElBQUksQ0FBQyxNQUFNLEdBQUcsSUFBSSxNQUFNLENBQUMsYUFBYSxDQUFDLFdBQVcsRUFBRSxDQUFDO1lBQ3JELElBQUksQ0FBQyxrQkFBa0IsQ0FBQyxJQUFJLENBQUMsSUFBSSxDQUFDLE1BQU0sQ0FBQyxDQUFDO1lBQzFDLElBQUksQ0FBQyxrQkFBa0IsQ0FBQyxRQUFRLEVBQUUsQ0FBQztRQUNyQyxDQUFDLENBQUMsQ0FBQztJQUNMLENBQUM7SUFXTSxTQUFTLENBQUMsU0FBb0IsRUFBRSxPQUFpRDtRQUN0RixJQUFJLENBQUMsU0FBUyxDQUFDLFlBQVksRUFBRTtZQUMzQixNQUFNLElBQUksS0FBSyxDQUNiLDRHQUE0RyxDQUM3RyxDQUFDO1NBQ0g7UUFDRCxJQUFJLENBQUMsSUFBSSxDQUFDLE1BQU0sRUFBRTtZQUNoQixNQUFNLElBQUksS0FBSyxDQUNiLDJHQUEyRyxDQUM1RyxDQUFDO1NBQ0g7UUFFRCxNQUFNLE1BQU0sR0FBRyxJQUFJLGNBQWMsQ0FBQyxJQUFJLENBQUMsTUFBTSxDQUFDLENBQUM7UUFDL0MsSUFBSSxDQUFDLE1BQU0sQ0FBQyxVQUFVLENBQUMsU0FBUyxDQUFDLFlBQVksRUFBRSxPQUFPLElBQUksRUFBRSxDQUFDLENBQUM7UUFFOUQsTUFBTSxDQUFDLFdBQVcsRUFBRSxDQUFDLFNBQVMsQ0FBQyxNQUFNLENBQUMsRUFBRTtZQUN0QyxJQUFJLE1BQU0sRUFBRTtnQkFDVixTQUFTLENBQUMsWUFBWSxHQUFHLE1BQU0sQ0FBQzthQUNqQztRQUNILENBQUMsQ0FBQyxDQUFDO1FBRUgsT0FBTyxNQUFNLENBQUM7SUFDaEIsQ0FBQzs7aUhBcERVLG9CQUFvQjtxR0FBcEIsb0JBQW9CLDhGQUpyQiwyQkFBMkI7MkZBSTFCLG9CQUFvQjtrQkFOaEMsU0FBUzttQkFBQztvQkFDVCxRQUFRLEVBQUUsY0FBYztvQkFDeEIsUUFBUSxFQUFFLDJCQUEyQjtvQkFDckMsSUFBSSxFQUFFLEVBQUUsS0FBSyxFQUFFLGNBQWMsRUFBRTtvQkFDL0IsZUFBZSxFQUFFLHVCQUF1QixDQUFDLE1BQU07aUJBQ2hEIiwic291cmNlc0NvbnRlbnQiOlsiLy8vIDxyZWZlcmVuY2UgcGF0aD1cIi4vdHlwZXMudHNcIiAvPlxuXG5pbXBvcnQgeyBDaGFuZ2VEZXRlY3Rpb25TdHJhdGVneSwgQ29tcG9uZW50LCBPbkluaXQgfSBmcm9tICdAYW5ndWxhci9jb3JlJztcbmltcG9ydCB7IFN1YmplY3QgfSBmcm9tICdyeGpzJztcblxuaW1wb3J0IHsgU2NyaXB0TG9hZGVyU2VydmljZSB9IGZyb20gJy4uLy4uL3NlcnZpY2VzL3NjcmlwdC1sb2FkZXIuc2VydmljZSc7XG5pbXBvcnQgeyBDaGFydEJhc2UgfSBmcm9tICcuLi9jaGFydC1iYXNlL2NoYXJ0LWJhc2UuY29tcG9uZW50JztcblxuaW1wb3J0IHsgQ2hhcnRFZGl0b3JSZWYgfSBmcm9tICcuL2NoYXJ0LWVkaXRvci1yZWYnO1xuXG5AQ29tcG9uZW50KHtcbiAgc2VsZWN0b3I6ICdjaGFydC1lZGl0b3InLFxuICB0ZW1wbGF0ZTogYDxuZy1jb250ZW50PjwvbmctY29udGVudD5gLFxuICBob3N0OiB7IGNsYXNzOiAnY2hhcnQtZWRpdG9yJyB9LFxuICBjaGFuZ2VEZXRlY3Rpb246IENoYW5nZURldGVjdGlvblN0cmF0ZWd5Lk9uUHVzaFxufSlcbmV4cG9ydCBjbGFzcyBDaGFydEVkaXRvckNvbXBvbmVudCBpbXBsZW1lbnRzIE9uSW5pdCB7XG4gIHByaXZhdGUgZWRpdG9yOiBnb29nbGUudmlzdWFsaXphdGlvbi5DaGFydEVkaXRvciB8IHVuZGVmaW5lZDtcbiAgcHJpdmF0ZSBpbml0aWFsaXplZFN1YmplY3QgPSBuZXcgU3ViamVjdDxnb29nbGUudmlzdWFsaXphdGlvbi5DaGFydEVkaXRvcj4oKTtcblxuICBjb25zdHJ1Y3Rvcihwcml2YXRlIHNjcmlwdExvYWRlclNlcnZpY2U6IFNjcmlwdExvYWRlclNlcnZpY2UpIHt9XG5cbiAgLyoqXG4gICAqIEVtaXRzIGFzIHNvb24gYXMgdGhlIGNoYXJ0IGVkaXRvciBpcyBmdWxseSBpbml0aWFsaXplZC5cbiAgICovXG4gIHB1YmxpYyBnZXQgaW5pdGlhbGl6ZWQkKCkge1xuICAgIHJldHVybiB0aGlzLmluaXRpYWxpemVkU3ViamVjdC5hc09ic2VydmFibGUoKTtcbiAgfVxuXG4gIHB1YmxpYyBuZ09uSW5pdCgpIHtcbiAgICB0aGlzLnNjcmlwdExvYWRlclNlcnZpY2UubG9hZENoYXJ0UGFja2FnZXMoJ2NoYXJ0ZWRpdG9yJykuc3Vic2NyaWJlKCgpID0+IHtcbiAgICAgIHRoaXMuZWRpdG9yID0gbmV3IGdvb2dsZS52aXN1YWxpemF0aW9uLkNoYXJ0RWRpdG9yKCk7XG4gICAgICB0aGlzLmluaXRpYWxpemVkU3ViamVjdC5uZXh0KHRoaXMuZWRpdG9yKTtcbiAgICAgIHRoaXMuaW5pdGlhbGl6ZWRTdWJqZWN0LmNvbXBsZXRlKCk7XG4gICAgfSk7XG4gIH1cblxuICAvKipcbiAgICogT3BlbnMgdGhlIGNoYXJ0IGVkaXRvciBhcyBhbiBlbWJlZGRlZCBkaWFsb2cgYm94IG9uIHRoZSBwYWdlLlxuICAgKiBJZiB0aGUgZWRpdG9yIGdldHMgc2F2ZWQsIHRoZSBjb21wb25lbnRzJyBjaGFydCB3aWxsIGJlIHVwZGF0ZWQgd2l0aCB0aGUgcmVzdWx0LlxuICAgKlxuICAgKiBAcGFyYW0gY29tcG9uZW50IFRoZSBjaGFydCB0byBiZSBlZGl0ZWQuXG4gICAqIEByZXR1cm5zIEEgcmVmZXJlbmNlIHRvIHRoZSBvcGVuIGVkaXRvci5cbiAgICovXG4gIHB1YmxpYyBlZGl0Q2hhcnQoY29tcG9uZW50OiBDaGFydEJhc2UpOiBDaGFydEVkaXRvclJlZjtcbiAgcHVibGljIGVkaXRDaGFydChjb21wb25lbnQ6IENoYXJ0QmFzZSwgb3B0aW9uczogZ29vZ2xlLnZpc3VhbGl6YXRpb24uQ2hhcnRFZGl0b3JPcHRpb25zKTogQ2hhcnRFZGl0b3JSZWY7XG4gIHB1YmxpYyBlZGl0Q2hhcnQoY29tcG9uZW50OiBDaGFydEJhc2UsIG9wdGlvbnM/OiBnb29nbGUudmlzdWFsaXphdGlvbi5DaGFydEVkaXRvck9wdGlvbnMpIHtcbiAgICBpZiAoIWNvbXBvbmVudC5jaGFydFdyYXBwZXIpIHtcbiAgICAgIHRocm93IG5ldyBFcnJvcihcbiAgICAgICAgJ0NoYXJ0IHdyYXBwZXIgaXMgYHVuZGVmaW5lZGAuIFBsZWFzZSB3YWl0IGZvciB0aGUgYGluaXRpYWxpemVkJGAgb2JzZXJ2YWJsZSBiZWZvcmUgdHJ5aW5nIHRvIGVkaXQgYSBjaGFydC4nXG4gICAgICApO1xuICAgIH1cbiAgICBpZiAoIXRoaXMuZWRpdG9yKSB7XG4gICAgICB0aHJvdyBuZXcgRXJyb3IoXG4gICAgICAgICdDaGFydCBlZGl0b3IgaXMgYHVuZGVmaW5lZGAuIFBsZWFzZSB3YWl0IGZvciB0aGUgYGluaXRpYWxpemVkJGAgb2JzZXJ2YWJsZSBiZWZvcmUgdHJ5aW5nIHRvIGVkaXQgYSBjaGFydC4nXG4gICAgICApO1xuICAgIH1cblxuICAgIGNvbnN0IGhhbmRsZSA9IG5ldyBDaGFydEVkaXRvclJlZih0aGlzLmVkaXRvcik7XG4gICAgdGhpcy5lZGl0b3Iub3BlbkRpYWxvZyhjb21wb25lbnQuY2hhcnRXcmFwcGVyLCBvcHRpb25zIHx8IHt9KTtcblxuICAgIGhhbmRsZS5hZnRlckNsb3NlZCgpLnN1YnNjcmliZShyZXN1bHQgPT4ge1xuICAgICAgaWYgKHJlc3VsdCkge1xuICAgICAgICBjb21wb25lbnQuY2hhcnRXcmFwcGVyID0gcmVzdWx0O1xuICAgICAgfVxuICAgIH0pO1xuXG4gICAgcmV0dXJuIGhhbmRsZTtcbiAgfVxufVxuIl19