import { Component, OnInit, ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HttpErrorResponse } from '@angular/common/http';
import { GoogleMap, MapInfoWindow, MapMarker } from '@angular/google-maps';
import { ToastrService } from 'ngx-toastr';
import { GeocoderResponse } from '../../models/geocoder-response.model';
import { GeocodingService } from '../../services/geocoding.service';
import { MapsAddress, MapsOffer, MapsResponse } from '../../models/maps-offer.model';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';


@Component({
  selector: 'maps',
  templateUrl: './maps.component.html',
  styleUrls: ['./maps.component.css', '../cards/cards.css']
})
export class MapsComponent implements OnInit {
  center: string;
  addresses: Map<string, MapsAddress> = new Map<string, MapsAddress>();
  offers: MapsOffer[] = [];

  // Google attributes
  geocoderWorking = false;
  bounds = new google.maps.LatLngBounds();

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

  markerOptions: google.maps.MarkerOptions = {
    draggable: false,
    animation: google.maps.Animation.DROP
  };

  homeMarkerOptions: google.maps.MarkerOptions = {
    icon: {
      url: "https://cdn-icons-png.flaticon.com/512/1196/1196783.png",
      scaledSize: new google.maps.Size(44, 44)
    },
    animation: google.maps.Animation.DROP
  };

  @ViewChild(GoogleMap, { static: false }) map: GoogleMap;
  @ViewChild('marker', { static: false }) marker: MapMarker;
  @ViewChild('infoWindow', { static: false }) infoWindow: MapInfoWindow;

  constructor(
    private http: HttpClient,
    private router: Router,
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

  login() {
    this.router.navigate(['login']);
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
      if (!this.addresses.has(address)) {
        this.addresses.set(address, { address: address, offers: [], coord: new google.maps.LatLng(0, 0) })
        this.loadAddress(address);
      }
      const existingAddress = this.addresses.get(address);
      existingAddress?.offers.push(offer);
    };
  }

  loadAddress(address: string) {
    this.findAddress(address).subscribe(
      (response: GeocoderResponse) => {
        if (response.status === 'OK' && response.results?.length)
          this.setMarker(response, address);
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

  setMarker(response: GeocoderResponse, address: string) {
    const location = response.results[0];
    const loc: any = location.geometry.location;

    const locationCoords = new google.maps.LatLng(loc.lat, loc.lng);
    this.fitMapBounds(locationCoords);

    if (address == this.center) {
      this.mapCenter = locationCoords;
      return;
    }

    const mapAddress = this.addresses.get(address);
    if (mapAddress) mapAddress.coord = locationCoords;
  }


  fitMapBounds(coord: google.maps.LatLng) {
    if (this.map && this.map.googleMap) {
      this.bounds.extend(coord);
      this.map.googleMap.fitBounds(this.bounds);
    }
  }

  public openInfoWindow(marker: MapMarker, infoWindow: MapInfoWindow) {
    infoWindow.open(marker);
  }

  get isWorking(): boolean {
    return this.geocoderWorking;
  }
}