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
  logged: boolean = false;
  user: any;
  offers: any;
  pools: any;
  center: string;
  alternative: any;
  addresses: string[] = [];

  constructor(
    private http: HttpClient,
    private geocodingService: GeocodingService,
    private toastr: ToastrService
  ) { }

  ngOnInit() {
    this.mapCenter = new google.maps.LatLng(40.335299133707615, -3.8775389329638457);  // URJC

    this.getMapsDTO().subscribe(
      response => {
        this.center = response.center as string;
        this.offers = response.offers as MapsOffer[];
        this.getAddresses();
        this.loadOffers();
      },
      error => alert('Error: ' + error.message)
    );
  }

  getMapsDTO() {
    return this.http.get<MapsResponse>('/api/maps');
  }

  getAddresses() {
    for (const offer of this.offers) {
      this.addresses.push(offer.address);
    }
  }

  filterByDistance() {
    throw new Error('Method not implemented.');
  }

  loadOffers() {
    this.addresses.forEach(address => {
      this.findAddress(address).subscribe(
        (response: GeocoderResponse) => {
          if (response.status === 'OK' && response.results?.length) {
            const location = response.results[0];
            const loc: any = location.geometry.location;
  
            this.locationCoords = new google.maps.LatLng(loc.lat, loc.lng);
            this.coords.push(this.locationCoords);
  
            const marker = new google.maps.Marker({
              position: new google.maps.LatLng(loc.lat, loc.lng),
              map: this.map.googleMap,
              title: location.formatted_address
            });
  
            this.markers.push(marker);
          } else {
            this.toastr.error(response.error_message, response.status + ": " + address);
          }
        },
        (err: HttpErrorResponse) => {
          console.error('geocoder error', err);
        }
      )
        .add(() => {
          this.geocoderWorking = false;
        });
    });
  }


  @ViewChild(GoogleMap, { static: false }) map: GoogleMap;
  @ViewChild(MapInfoWindow, { static: false }) infoWindow: MapInfoWindow;

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

  address: string;
  formattedAddress?: string | null = null;
  locationCoords?: google.maps.LatLng | null = null;

  openInfoWindow(marker: MapMarker) {
    this.infoWindow.open(marker);
  }

  get isWorking(): boolean {
    return this.geolocationWorking || this.geocoderWorking;
  }

  findAddress(address: string) {
    this.address = address;
    if (!this.address || this.address.length === 0) {
      return new Observable<GeocoderResponse>(subscriber => {
        subscriber.error('No address');
      })
    }

    this.geocoderWorking = true;
    return this.geocodingService.getLocation(this.address)
  }

}