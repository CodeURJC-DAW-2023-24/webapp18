import { Injectable } from '@angular/core';
import * as i0 from "@angular/core";
export class DataTableService {
    create(data, columns, formatters) {
        if (data == null) {
            return undefined;
        }
        let firstRowIsData = true;
        if (columns != null) {
            firstRowIsData = false;
        }
        const dataTable = google.visualization.arrayToDataTable(this.getDataAsTable(data, columns), firstRowIsData);
        if (formatters) {
            this.applyFormatters(dataTable, formatters);
        }
        return dataTable;
    }
    getDataAsTable(data, columns) {
        if (columns) {
            return [columns, ...data];
        }
        else {
            return data;
        }
    }
    applyFormatters(dataTable, formatters) {
        for (const val of formatters) {
            val.formatter.format(dataTable, val.colIndex);
        }
    }
}
DataTableService.ɵfac = i0.ɵɵngDeclareFactory({ minVersion: "12.0.0", version: "14.0.3", ngImport: i0, type: DataTableService, deps: [], target: i0.ɵɵFactoryTarget.Injectable });
DataTableService.ɵprov = i0.ɵɵngDeclareInjectable({ minVersion: "12.0.0", version: "14.0.3", ngImport: i0, type: DataTableService, providedIn: 'root' });
i0.ɵɵngDeclareClassMetadata({ minVersion: "12.0.0", version: "14.0.3", ngImport: i0, type: DataTableService, decorators: [{
            type: Injectable,
            args: [{ providedIn: 'root' }]
        }] });
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZGF0YS10YWJsZS5zZXJ2aWNlLmpzIiwic291cmNlUm9vdCI6IiIsInNvdXJjZXMiOlsiLi4vLi4vLi4vLi4vLi4vbGlicy9hbmd1bGFyLWdvb2dsZS1jaGFydHMvc3JjL2xpYi9zZXJ2aWNlcy9kYXRhLXRhYmxlLnNlcnZpY2UudHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IkFBQUEsT0FBTyxFQUFFLFVBQVUsRUFBRSxNQUFNLGVBQWUsQ0FBQzs7QUFNM0MsTUFBTSxPQUFPLGdCQUFnQjtJQUNwQixNQUFNLENBQ1gsSUFBdUIsRUFDdkIsT0FBa0IsRUFDbEIsVUFBd0I7UUFFeEIsSUFBSSxJQUFJLElBQUksSUFBSSxFQUFFO1lBQ2hCLE9BQU8sU0FBUyxDQUFDO1NBQ2xCO1FBRUQsSUFBSSxjQUFjLEdBQUcsSUFBSSxDQUFDO1FBQzFCLElBQUksT0FBTyxJQUFJLElBQUksRUFBRTtZQUNuQixjQUFjLEdBQUcsS0FBSyxDQUFDO1NBQ3hCO1FBRUQsTUFBTSxTQUFTLEdBQUcsTUFBTSxDQUFDLGFBQWEsQ0FBQyxnQkFBZ0IsQ0FBQyxJQUFJLENBQUMsY0FBYyxDQUFDLElBQUksRUFBRSxPQUFPLENBQUMsRUFBRSxjQUFjLENBQUMsQ0FBQztRQUM1RyxJQUFJLFVBQVUsRUFBRTtZQUNkLElBQUksQ0FBQyxlQUFlLENBQUMsU0FBUyxFQUFFLFVBQVUsQ0FBQyxDQUFDO1NBQzdDO1FBRUQsT0FBTyxTQUFTLENBQUM7SUFDbkIsQ0FBQztJQUVPLGNBQWMsQ0FBQyxJQUFXLEVBQUUsT0FBNkI7UUFDL0QsSUFBSSxPQUFPLEVBQUU7WUFDWCxPQUFPLENBQUMsT0FBTyxFQUFFLEdBQUcsSUFBSSxDQUFDLENBQUM7U0FDM0I7YUFBTTtZQUNMLE9BQU8sSUFBSSxDQUFDO1NBQ2I7SUFDSCxDQUFDO0lBRU8sZUFBZSxDQUFDLFNBQXlDLEVBQUUsVUFBdUI7UUFDeEYsS0FBSyxNQUFNLEdBQUcsSUFBSSxVQUFVLEVBQUU7WUFDNUIsR0FBRyxDQUFDLFNBQVMsQ0FBQyxNQUFNLENBQUMsU0FBUyxFQUFFLEdBQUcsQ0FBQyxRQUFRLENBQUMsQ0FBQztTQUMvQztJQUNILENBQUM7OzZHQW5DVSxnQkFBZ0I7aUhBQWhCLGdCQUFnQixjQURILE1BQU07MkZBQ25CLGdCQUFnQjtrQkFENUIsVUFBVTttQkFBQyxFQUFFLFVBQVUsRUFBRSxNQUFNLEVBQUUiLCJzb3VyY2VzQ29udGVudCI6WyJpbXBvcnQgeyBJbmplY3RhYmxlIH0gZnJvbSAnQGFuZ3VsYXIvY29yZSc7XG5cbmltcG9ydCB7IENvbHVtbiwgUm93IH0gZnJvbSAnLi4vY29tcG9uZW50cy9jaGFydC1iYXNlL2NoYXJ0LWJhc2UuY29tcG9uZW50JztcbmltcG9ydCB7IEZvcm1hdHRlciB9IGZyb20gJy4uL3R5cGVzL2Zvcm1hdHRlcic7XG5cbkBJbmplY3RhYmxlKHsgcHJvdmlkZWRJbjogJ3Jvb3QnIH0pXG5leHBvcnQgY2xhc3MgRGF0YVRhYmxlU2VydmljZSB7XG4gIHB1YmxpYyBjcmVhdGUoXG4gICAgZGF0YTogUm93W10gfCB1bmRlZmluZWQsXG4gICAgY29sdW1ucz86IENvbHVtbltdLFxuICAgIGZvcm1hdHRlcnM/OiBGb3JtYXR0ZXJbXVxuICApOiBnb29nbGUudmlzdWFsaXphdGlvbi5EYXRhVGFibGUgfCB1bmRlZmluZWQge1xuICAgIGlmIChkYXRhID09IG51bGwpIHtcbiAgICAgIHJldHVybiB1bmRlZmluZWQ7XG4gICAgfVxuXG4gICAgbGV0IGZpcnN0Um93SXNEYXRhID0gdHJ1ZTtcbiAgICBpZiAoY29sdW1ucyAhPSBudWxsKSB7XG4gICAgICBmaXJzdFJvd0lzRGF0YSA9IGZhbHNlO1xuICAgIH1cblxuICAgIGNvbnN0IGRhdGFUYWJsZSA9IGdvb2dsZS52aXN1YWxpemF0aW9uLmFycmF5VG9EYXRhVGFibGUodGhpcy5nZXREYXRhQXNUYWJsZShkYXRhLCBjb2x1bW5zKSwgZmlyc3RSb3dJc0RhdGEpO1xuICAgIGlmIChmb3JtYXR0ZXJzKSB7XG4gICAgICB0aGlzLmFwcGx5Rm9ybWF0dGVycyhkYXRhVGFibGUsIGZvcm1hdHRlcnMpO1xuICAgIH1cblxuICAgIHJldHVybiBkYXRhVGFibGU7XG4gIH1cblxuICBwcml2YXRlIGdldERhdGFBc1RhYmxlKGRhdGE6IFJvd1tdLCBjb2x1bW5zOiBDb2x1bW5bXSB8IHVuZGVmaW5lZCk6IChSb3cgfCBDb2x1bW5bXSlbXSB7XG4gICAgaWYgKGNvbHVtbnMpIHtcbiAgICAgIHJldHVybiBbY29sdW1ucywgLi4uZGF0YV07XG4gICAgfSBlbHNlIHtcbiAgICAgIHJldHVybiBkYXRhO1xuICAgIH1cbiAgfVxuXG4gIHByaXZhdGUgYXBwbHlGb3JtYXR0ZXJzKGRhdGFUYWJsZTogZ29vZ2xlLnZpc3VhbGl6YXRpb24uRGF0YVRhYmxlLCBmb3JtYXR0ZXJzOiBGb3JtYXR0ZXJbXSk6IHZvaWQge1xuICAgIGZvciAoY29uc3QgdmFsIG9mIGZvcm1hdHRlcnMpIHtcbiAgICAgIHZhbC5mb3JtYXR0ZXIuZm9ybWF0KGRhdGFUYWJsZSwgdmFsLmNvbEluZGV4KTtcbiAgICB9XG4gIH1cbn1cbiJdfQ==