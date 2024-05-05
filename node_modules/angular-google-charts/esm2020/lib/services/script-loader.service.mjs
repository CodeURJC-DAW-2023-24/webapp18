import { Inject, Injectable, LOCALE_ID, NgZone } from '@angular/core';
import { Observable, of, Subject } from 'rxjs';
import { map, mergeMap, switchMap } from 'rxjs/operators';
import { getDefaultConfig } from '../helpers/chart.helper';
import { GOOGLE_CHARTS_LAZY_CONFIG } from '../types/google-charts-config';
import * as i0 from "@angular/core";
import * as i1 from "rxjs";
export class ScriptLoaderService {
    constructor(zone, localeId, config$) {
        this.zone = zone;
        this.localeId = localeId;
        this.config$ = config$;
        this.scriptSource = 'https://www.gstatic.com/charts/loader.js';
        this.scriptLoadSubject = new Subject();
    }
    /**
     * Checks whether `google.charts` is available.
     *
     * If not, it can be loaded by calling `loadChartPackages`.
     *
     * @returns `true` if `google.charts` is available, `false` otherwise.
     */
    isGoogleChartsAvailable() {
        if (typeof google === 'undefined' || typeof google.charts === 'undefined') {
            return false;
        }
        return true;
    }
    /**
     * Loads the Google Chart script and the provided chart packages.
     * Can be called multiple times to load more packages.
     *
     * When called without any arguments, this will just load the default package
     * containing the namespaces `google.charts` and `google.visualization` without any charts.
     *
     * @param packages The packages to load.
     * @returns A stream emitting as soon as the chart packages are loaded.
     */
    loadChartPackages(...packages) {
        return this.loadGoogleCharts().pipe(mergeMap(() => this.config$), map(config => {
            return { ...getDefaultConfig(), ...(config || {}) };
        }), switchMap((googleChartsConfig) => {
            return new Observable(observer => {
                const config = {
                    packages,
                    language: this.localeId,
                    mapsApiKey: googleChartsConfig.mapsApiKey,
                    safeMode: googleChartsConfig.safeMode
                };
                google.charts.load(googleChartsConfig.version, config);
                google.charts.setOnLoadCallback(() => {
                    this.zone.run(() => {
                        observer.next();
                        observer.complete();
                    });
                });
            });
        }));
    }
    /**
     * Loads the Google Charts script. After the script is loaded, `google.charts` is defined.
     *
     * @returns A stream emitting as soon as loading has completed.
     * If the google charts script is already loaded, the stream emits immediately.
     */
    loadGoogleCharts() {
        if (this.isGoogleChartsAvailable()) {
            return of(undefined);
        }
        else if (!this.isLoadingGoogleCharts()) {
            const script = this.createGoogleChartsScript();
            script.onload = () => {
                this.zone.run(() => {
                    this.scriptLoadSubject.next();
                    this.scriptLoadSubject.complete();
                });
            };
            script.onerror = () => {
                this.zone.run(() => {
                    console.error('Failed to load the google charts script!');
                    this.scriptLoadSubject.error(new Error('Failed to load the google charts script!'));
                });
            };
        }
        return this.scriptLoadSubject.asObservable();
    }
    isLoadingGoogleCharts() {
        return this.getGoogleChartsScript() != null;
    }
    getGoogleChartsScript() {
        const pageScripts = Array.from(document.getElementsByTagName('script'));
        return pageScripts.find(script => script.src === this.scriptSource);
    }
    createGoogleChartsScript() {
        const script = document.createElement('script');
        script.type = 'text/javascript';
        script.src = this.scriptSource;
        script.async = true;
        document.getElementsByTagName('head')[0].appendChild(script);
        return script;
    }
}
ScriptLoaderService.ɵfac = i0.ɵɵngDeclareFactory({ minVersion: "12.0.0", version: "14.0.3", ngImport: i0, type: ScriptLoaderService, deps: [{ token: i0.NgZone }, { token: LOCALE_ID }, { token: GOOGLE_CHARTS_LAZY_CONFIG }], target: i0.ɵɵFactoryTarget.Injectable });
ScriptLoaderService.ɵprov = i0.ɵɵngDeclareInjectable({ minVersion: "12.0.0", version: "14.0.3", ngImport: i0, type: ScriptLoaderService });
i0.ɵɵngDeclareClassMetadata({ minVersion: "12.0.0", version: "14.0.3", ngImport: i0, type: ScriptLoaderService, decorators: [{
            type: Injectable
        }], ctorParameters: function () { return [{ type: i0.NgZone }, { type: undefined, decorators: [{
                    type: Inject,
                    args: [LOCALE_ID]
                }] }, { type: i1.Observable, decorators: [{
                    type: Inject,
                    args: [GOOGLE_CHARTS_LAZY_CONFIG]
                }] }]; } });
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoic2NyaXB0LWxvYWRlci5zZXJ2aWNlLmpzIiwic291cmNlUm9vdCI6IiIsInNvdXJjZXMiOlsiLi4vLi4vLi4vLi4vLi4vbGlicy9hbmd1bGFyLWdvb2dsZS1jaGFydHMvc3JjL2xpYi9zZXJ2aWNlcy9zY3JpcHQtbG9hZGVyLnNlcnZpY2UudHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IkFBQUEsT0FBTyxFQUFFLE1BQU0sRUFBRSxVQUFVLEVBQUUsU0FBUyxFQUFFLE1BQU0sRUFBRSxNQUFNLGVBQWUsQ0FBQztBQUN0RSxPQUFPLEVBQUUsVUFBVSxFQUFFLEVBQUUsRUFBRSxPQUFPLEVBQUUsTUFBTSxNQUFNLENBQUM7QUFDL0MsT0FBTyxFQUFFLEdBQUcsRUFBRSxRQUFRLEVBQUUsU0FBUyxFQUFFLE1BQU0sZ0JBQWdCLENBQUM7QUFFMUQsT0FBTyxFQUFFLGdCQUFnQixFQUFFLE1BQU0seUJBQXlCLENBQUM7QUFDM0QsT0FBTyxFQUFzQix5QkFBeUIsRUFBRSxNQUFNLCtCQUErQixDQUFDOzs7QUFHOUYsTUFBTSxPQUFPLG1CQUFtQjtJQUk5QixZQUNVLElBQVksRUFDTyxRQUFnQixFQUNTLE9BQXVDO1FBRm5GLFNBQUksR0FBSixJQUFJLENBQVE7UUFDTyxhQUFRLEdBQVIsUUFBUSxDQUFRO1FBQ1MsWUFBTyxHQUFQLE9BQU8sQ0FBZ0M7UUFONUUsaUJBQVksR0FBRywwQ0FBMEMsQ0FBQztRQUMxRCxzQkFBaUIsR0FBRyxJQUFJLE9BQU8sRUFBUSxDQUFDO0lBTXJELENBQUM7SUFFTDs7Ozs7O09BTUc7SUFDSSx1QkFBdUI7UUFDNUIsSUFBSSxPQUFPLE1BQU0sS0FBSyxXQUFXLElBQUksT0FBTyxNQUFNLENBQUMsTUFBTSxLQUFLLFdBQVcsRUFBRTtZQUN6RSxPQUFPLEtBQUssQ0FBQztTQUNkO1FBRUQsT0FBTyxJQUFJLENBQUM7SUFDZCxDQUFDO0lBRUQ7Ozs7Ozs7OztPQVNHO0lBQ0ksaUJBQWlCLENBQUMsR0FBRyxRQUFrQjtRQUM1QyxPQUFPLElB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Q0FBQztRQUNoRCxNQUFNLENBQUMsSUFBSSxHQUFHLGlCQUFpQixDQUFDO1FBQ2hDLE1BQU0sQ0FBQyxHQUFHLEdBQUcsSUFBSSxDQUFDLFlBQVksQ0FBQztRQUMvQixNQUFNLENBQUMsS0FBSyxHQUFHLElBQUksQ0FBQztRQUNwQixRQUFRLENBQUMsb0JBQW9CLENBQUMsTUFBTSxDQUFDLENBQUMsQ0FBQyxDQUFDLENBQUMsV0FBVyxDQUFDLE1BQU0sQ0FBQyxDQUFDO1FBQzdELE9BQU8sTUFBTSxDQUFDO0lBQ2hCLENBQUM7O2dIQTNHVSxtQkFBbUIsd0NBTXBCLFNBQVMsYUFDVCx5QkFBeUI7b0hBUHhCLG1CQUFtQjsyRkFBbkIsbUJBQW1CO2tCQUQvQixVQUFVOzswQkFPTixNQUFNOzJCQUFDLFNBQVM7OzBCQUNoQixNQUFNOzJCQUFDLHlCQUF5QiIsInNvdXJjZXNDb250ZW50IjpbImltcG9ydCB7IEluamVjdCwgSW5qZWN0YWJsZSwgTE9DQUxFX0lELCBOZ1pvbmUgfSBmcm9tICdAYW5ndWxhci9jb3JlJztcbmltcG9ydCB7IE9ic2VydmFibGUsIG9mLCBTdWJqZWN0IH0gZnJvbSAncnhqcyc7XG5pbXBvcnQgeyBtYXAsIG1lcmdlTWFwLCBzd2l0Y2hNYXAgfSBmcm9tICdyeGpzL29wZXJhdG9ycyc7XG5cbmltcG9ydCB7IGdldERlZmF1bHRDb25maWcgfSBmcm9tICcuLi9oZWxwZXJzL2NoYXJ0LmhlbHBlcic7XG5pbXBvcnQgeyBHb29nbGVDaGFydHNDb25maWcsIEdPT0dMRV9DSEFSVFNfTEFaWV9DT05GSUcgfSBmcm9tICcuLi90eXBlcy9nb29nbGUtY2hhcnRzLWNvbmZpZyc7XG5cbkBJbmplY3RhYmxlKClcbmV4cG9ydCBjbGFzcyBTY3JpcHRMb2FkZXJTZXJ2aWNlIHtcbiAgcHJpdmF0ZSByZWFkb25seSBzY3JpcHRTb3VyY2UgPSAnaHR0cHM6Ly93d3cuZ3N0YXRpYy5jb20vY2hhcnRzL2xvYWRlci5qcyc7XG4gIHByaXZhdGUgcmVhZG9ubHkgc2NyaXB0TG9hZFN1YmplY3QgPSBuZXcgU3ViamVjdDx2b2lkPigpO1xuXG4gIGNvbnN0cnVjdG9yKFxuICAgIHByaXZhdGUgem9uZTogTmdab25lLFxuICAgIEBJbmplY3QoTE9DQUxFX0lEKSBwcml2YXRlIGxvY2FsZUlkOiBzdHJpbmcsXG4gICAgQEluamVjdChHT09HTEVfQ0hBUlRTX0xBWllfQ09ORklHKSBwcml2YXRlIHJlYWRvbmx5IGNvbmZpZyQ6IE9ic2VydmFibGU8R29vZ2xlQ2hhcnRzQ29uZmlnPlxuICApIHsgfVxuXG4gIC8qKlxuICAgKiBDaGVja3Mgd2hldGhlciBgZ29vZ2xlLmNoYXJ0c2AgaXMgYXZhaWxhYmxlLlxuICAgKlxuICAgKiBJZiBub3QsIGl0IGNhbiBiZSBsb2FkZWQgYnkgY2FsbGluZyBgbG9hZENoYXJ0UGFja2FnZXNgLlxuICAgKlxuICAgKiBAcmV0dXJucyBgdHJ1ZWAgaWYgYGdvb2dsZS5jaGFydHNgIGlzIGF2YWlsYWJsZSwgYGZhbHNlYCBvdGhlcndpc2UuXG4gICAqL1xuICBwdWJsaWMgaXNHb29nbGVDaGFydHNBdmFpbGFibGUoKTogYm9vbGVhbiB7XG4gICAgaWYgKHR5cGVvZiBnb29nbGUgPT09ICd1bmRlZmluZWQnIHx8IHR5cGVvZiBnb29nbGUuY2hhcnRzID09PSAndW5kZWZpbmVkJykge1xuICAgICAgcmV0dXJuIGZhbHNlO1xuICAgIH1cblxuICAgIHJldHVybiB0cnVlO1xuICB9XG5cbiAgLyoqXG4gICAqIExvYWRzIHRoZSBHb29nbGUgQ2hhcnQgc2NyaXB0IGFuZCB0aGUgcHJvdmlkZWQgY2hhcnQgcGFja2FnZXMuXG4gICAqIENhbiBiZSBjYWxsZWQgbXVsdGlwbGUgdGltZXMgdG8gbG9hZCBtb3JlIHBhY2thZ2VzLlxuICAgKlxuICAgKiBXaGVuIGNhbGxlZCB3aXRob3V0IGFueSBhcmd1bWVudHMsIHRoaXMgd2lsbCBqdXN0IGxvYWQgdGhlIGRlZmF1bHQgcGFja2FnZVxuICAgKiBjb250YWluaW5nIHRoZSBuYW1lc3BhY2VzIGBnb29nbGUuY2hhcnRzYCBhbmQgYGdvb2dsZS52aXN1YWxpemF0aW9uYCB3aXRob3V0IGFueSBjaGFydHMuXG4gICAqXG4gICAqIEBwYXJhbSBwYWNrYWdlcyBUaGUgcGFja2FnZXMgdG8gbG9hZC5cbiAgICogQHJldHVybnMgQSBzdHJlYW0gZW1pdHRpbmcgYXMgc29vbiBhcyB0aGUgY2hhcnQgcGFja2FnZXMgYXJlIGxvYWRlZC5cbiAgICovXG4gIHB1YmxpYyBsb2FkQ2hhcnRQYWNrYWdlcyguLi5wYWNrYWdlczogc3RyaW5nW10pOiBPYnNlcnZhYmxlPG51bGw+IHtcbiAgICByZXR1cm4gdGhpcy5sb2FkR29vZ2xlQ2hhcnRzKCkucGlwZShcbiAgICAgIG1lcmdlTWFwKCgpID0+IHRoaXMuY29uZmlnJCksXG4gICAgICBtYXAoY29uZmlnID0+IHtcbiAgICAgICAgcmV0dXJuIHsgLi4uZ2V0RGVmYXVsdENvbmZpZygpLCAuLi4oY29uZmlnIHx8IHt9KSB9O1xuICAgICAgfSksXG4gICAgICBzd2l0Y2hNYXAoKGdvb2dsZUNoYXJ0c0NvbmZpZzogR29vZ2xlQ2hhcnRzQ29uZmlnKSA9PiB7XG4gICAgICAgIHJldHVybiBuZXcgT2JzZXJ2YWJsZTxudWxsPihvYnNlcnZlciA9PiB7XG4gICAgICAgICAgY29uc3QgY29uZmlnID0ge1xuICAgICAgICAgICAgcGFja2FnZXMsXG4gICAgICAgICAgICBsYW5ndWFnZTogdGhpcy5sb2NhbGVJZCxcbiAgICAgICAgICAgIG1hcHNBcGlLZXk6IGdvb2dsZUNoYXJ0c0NvbmZpZy5tYXBzQXBpS2V5LFxuICAgICAgICAgICAgc2FmZU1vZGU6IGdvb2dsZUNoYXJ0c0NvbmZpZy5zYWZlTW9kZVxuICAgICAgICAgIH07XG5cbiAgICAgICAgICBnb29nbGUuY2hhcnRzLmxvYWQoZ29vZ2xlQ2hhcnRzQ29uZmlnLnZlcnNpb24hLCBjb25maWcpO1xuICAgICAgICAgIGdvb2dsZS5jaGFydHMuc2V0T25Mb2FkQ2FsbGJhY2soKCkgPT4ge1xuICAgICAgICAgICAgdGhpcy56b25lLnJ1bigoKSA9PiB7XG4gICAgICAgICAgICAgIG9ic2VydmVyLm5leHQoKTtcbiAgICAgICAgICAgICAgb2JzZXJ2ZXIuY29tcGxldGUoKTtcbiAgICAgICAgICAgIH0pO1xuICAgICAgICAgIH0pO1xuICAgICAgICB9KTtcbiAgICAgIH0pXG4gICAgKTtcbiAgfVxuXG4gIC8qKlxuICAgKiBMb2FkcyB0aGUgR29vZ2xlIENoYXJ0cyBzY3JpcHQuIEFmdGVyIHRoZSBzY3JpcHQgaXMgbG9hZGVkLCBgZ29vZ2xlLmNoYXJ0c2AgaXMgZGVmaW5lZC5cbiAgICpcbiAgICogQHJldHVybnMgQSBzdHJlYW0gZW1pdHRpbmcgYXMgc29vbiBhcyBsb2FkaW5nIGhhcyBjb21wbGV0ZWQuXG4gICAqIElmIHRoZSBnb29nbGUgY2hhcnRzIHNjcmlwdCBpcyBhbHJlYWR5IGxvYWRlZCwgdGhlIHN0cmVhbSBlbWl0cyBpbW1lZGlhdGVseS5cbiAgICovXG4gIHByaXZhdGUgbG9hZEdvb2dsZUNoYXJ0cygpOiBPYnNlcnZhYmxlPHZvaWQ+IHtcbiAgICBpZiAodGhpcy5pc0dvb2dsZUNoYXJ0c0F2YWlsYWJsZSgpKSB7XG4gICAgICByZXR1cm4gb2YodW5kZWZpbmVkKTtcbiAgICB9IGVsc2UgaWYgKCF0aGlzLmlzTG9hZGluZ0dvb2dsZUNoYXJ0cygpKSB7XG4gICAgICBjb25zdCBzY3JpcHQgPSB0aGlzLmNyZWF0ZUdvb2dsZUNoYXJ0c1NjcmlwdCgpO1xuICAgICAgc2NyaXB0Lm9ubG9hZCA9ICgpID0+IHtcbiAgICAgICAgdGhpcy56b25lLnJ1bigoKSA9PiB7XG4gICAgICAgICAgdGhpcy5zY3JpcHRMb2FkU3ViamVjdC5uZXh0KCk7XG4gICAgICAgICAgdGhpcy5zY3JpcHRMb2FkU3ViamVjdC5jb21wbGV0ZSgpO1xuICAgICAgICB9KTtcbiAgICAgIH07XG5cbiAgICAgIHNjcmlwdC5vbmVycm9yID0gKCkgPT4ge1xuICAgICAgICB0aGlzLnpvbmUucnVuKCgpID0+IHtcbiAgICAgICAgICBjb25zb2xlLmVycm9yKCdGYWlsZWQgdG8gbG9hZCB0aGUgZ29vZ2xlIGNoYXJ0cyBzY3JpcHQhJyk7XG4gICAgICAgICAgdGhpcy5zY3JpcHRMb2FkU3ViamVjdC5lcnJvcihuZXcgRXJyb3IoJ0ZhaWxlZCB0byBsb2FkIHRoZSBnb29nbGUgY2hhcnRzIHNjcmlwdCEnKSk7XG4gICAgICAgIH0pO1xuICAgICAgfTtcbiAgICB9XG5cbiAgICByZXR1cm4gdGhpcy5zY3JpcHRMb2FkU3ViamVjdC5hc09ic2VydmFibGUoKTtcbiAgfVxuXG4gIHByaXZhdGUgaXNMb2FkaW5nR29vZ2xlQ2hhcnRzKCkge1xuICAgIHJldHVybiB0aGlzLmdldEdvb2dsZUNoYXJ0c1NjcmlwdCgpICE9IG51bGw7XG4gIH1cblxuICBwcml2YXRlIGdldEdvb2dsZUNoYXJ0c1NjcmlwdCgpOiBIVE1MU2NyaXB0RWxlbWVudCB8IHVuZGVmaW5lZCB7XG4gICAgY29uc3QgcGFnZVNjcmlwdHMgPSBBcnJheS5mcm9tKGRvY3VtZW50LmdldEVsZW1lbnRzQnlUYWdOYW1lKCdzY3JpcHQnKSk7XG4gICAgcmV0dXJuIHBhZ2VTY3JpcHRzLmZpbmQoc2NyaXB0ID0+IHNjcmlwdC5zcmMgPT09IHRoaXMuc2NyaXB0U291cmNlKTtcbiAgfVxuXG4gIHByaXZhdGUgY3JlYXRlR29vZ2xlQ2hhcnRzU2NyaXB0KCk6IEhUTUxTY3JpcHRFbGVtZW50IHtcbiAgICBjb25zdCBzY3JpcHQgPSBkb2N1bWVudC5jcmVhdGVFbGVtZW50KCdzY3JpcHQnKTtcbiAgICBzY3JpcHQudHlwZSA9ICd0ZXh0L2phdmFzY3JpcHQnO1xuICAgIHNjcmlwdC5zcmMgPSB0aGlzLnNjcmlwdFNvdXJjZTtcbiAgICBzY3JpcHQuYXN5bmMgPSB0cnVlO1xuICAgIGRvY3VtZW50LmdldEVsZW1lbnRzQnlUYWdOYW1lKCdoZWFkJylbMF0uYXBwZW5kQ2hpbGQoc2NyaXB0KTtcbiAgICByZXR1cm4gc2NyaXB0O1xuICB9XG59XG4iXX0=