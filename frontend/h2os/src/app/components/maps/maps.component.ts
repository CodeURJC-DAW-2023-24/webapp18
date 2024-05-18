import { Component, OnInit, ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HttpErrorResponse } from '@angular/common/http';
import { GoogleMap, MapInfoWindow, MapMarker } from '@angular/google-maps';
import { ToastrService } from 'ngx-toastr';
import { GeocoderResponse } from '../../models/geocoder-response.model';
import { GeocodingService } from '../../services/geocoding.service';
import { MapsOffer, MapsResponse } from '../../models/maps-offer.model';
import { Observable } from 'rxjs';


@Component({
  selector: 'maps',
  templateUrl: './maps.component.html',
  styleUrls: ['./maps.component.css']
})
export class MapsComponent implements OnInit {
  offers: any;
  center: string;
  addresses: string[] = [];

  // Google attributes
  mapZoom = 15;
  mapCenter: google.maps.LatLng;
  mapOptions: google.maps.MapOptions = {
    mapTypeId: google.maps.MapTypeId.ROADMAP,
    zoomControl: false,
    scrollwheel: true,
    disableDefaultUI: true,
    disableDoubleClickZoom: false,
    maxZoom: 20,
    minZoom: 4,
  };

  coords: google.maps.LatLng[] = [];
  markers: google.maps.Marker[] = [];
  markerInfoContent = '';
  markerOptions: google.maps.MarkerOptions = {
    draggable: false,
    animation: google.maps.Animation.DROP,
  };

  geocoderWorking = false;
  geolocationWorking = false;

  formattedAddress?: string | null = null;

  @ViewChild(GoogleMap, { static: false }) map: GoogleMap;
  @ViewChild(MapInfoWindow, { static: false }) infoWindow: MapInfoWindow;

  constructor(
    private http: HttpClient,
    private geocodingService: GeocodingService,
    private toastr: ToastrService
  ) { }

  ngOnInit() {
    this.getMapsDTO().subscribe(
      response => {
        this.center = response.center as string;
        this.offers = response.offers as MapsOffer[];
        this.loadAddress(this.center);
        this.loadOffers()
      },
      error => alert('Error: ' + error.message)
    );
  }

  getMapsDTO() {
    return this.http.get<MapsResponse>('/api/maps');
  }

  filterByDistance() {
    throw new Error('Method not implemented.');
  }

  loadOffers() {
    for (const offer of this.offers) {
      const address = offer.address as string;
      this.loadAddress(address);
    };
  }

  loadAddress(address: string) {
    this.findAddress(address).subscribe(
      (response: GeocoderResponse) => {
        if (response.status === 'OK' && response.results?.length)
          this.setMarker(response);
        else
          this.toastr.error(response.error_message, response.status + ": " + address);
      },
      (err: HttpErrorResponse) => console.error('geocoder error', err)
    ).add(() => {
      this.geocoderWorking = false;
    });
  }

  // Google methods
  findAddress(address: string) {
    if (!address || address.length === 0)
      return new Observable<GeocoderResponse>(subscriber =>
        subscriber.error('No address'));

    this.geocoderWorking = true;
    return this.geocodingService.getLocation(address)
  }

  setMarker(response: GeocoderResponse) {
    const location = response.results[0];
    const loc: any = location.geometry.location;

    const locationCoords = new google.maps.LatLng(loc.lat, loc.lng);
    this.coords.push(locationCoords);

    const marker = new google.maps.Marker({
      position: new google.maps.LatLng(loc.lat, loc.lng),
      map: this.map.googleMap,
      title: location.formatted_address
    });

    this.markers.push(marker);
  }

  public openInfoWindow(marker: MapMarker) {
    this.infoWindow.open(marker);
  }

  get isWorking(): boolean {
    return this.geolocationWorking || this.geocoderWorking;
  }
}