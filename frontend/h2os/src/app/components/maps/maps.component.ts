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
import { UserService } from '../../services/user.service';


@Component({
  selector: 'maps',
  templateUrl: './maps.component.html',
  styleUrls: ['./maps.component.css', '../cards/cards.css']
})
export class MapsComponent implements OnInit {
  center: string;
  addresses: Map<string, MapsAddress> = new Map<string, MapsAddress>();
  offers: MapsOffer[] = [];
  logged: boolean;
  radius: number = 0;

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

  circleOptions: google.maps.CircleOptions = {
    strokeWeight: 0,
    fillColor: '#0082fe',
    fillOpacity: 0.35,
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
    private userService: UserService,
    private geocodingService: GeocodingService,
    private toastr: ToastrService
  ) {
    this.userService.me().subscribe(
      _response => {
        this.logged = true;
      },
      _error => {
        this.logged = false;
      }
    );
  }

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

  // Filter methods
  async filterByDistance() {
    const distancesMap = new Map<string, number>();
    const distances: number[] = [];

    for (const mapAddress of this.addresses.values()) {
      const address = mapAddress.address;
      const distance = await this.calculateDistance(this.mapCenter, address) as number;
      distancesMap.set(address, distance);

      const insertIndex = this.binarySearchInsert(distances, distance);
      const timesAppeared = mapAddress.offers.length || 1;
      for (let i = 0; i < timesAppeared; i++)
        distances.splice(insertIndex, 0, distance);
    }

    this.radius = this.getLimitDistance(distances);
  }

  async calculateDistance(origin: google.maps.LatLng, destinationAddress: string): Promise<number> {
    try {
      const response = await this.findAddress(destinationAddress).toPromise();
      if (!response) throw Error("No response");

      const loc: any = response.results[0].geometry.location;
      const destination = new google.maps.LatLng(loc.lat, loc.lng);

      return this.calculateEuclideanDistance(origin, destination);
    }
    catch (error) {
      console.error('Error calculating distance', error);
      return 0;
    }
  }

  calculateEuclideanDistance(origin: google.maps.LatLng, destination: google.maps.LatLng): number {
    const earthRadiusKm = 6371;

    const dLat = (destination.lat() - origin.lat()) * (Math.PI / 180);
    const dLon = (destination.lng() - origin.lng()) * (Math.PI / 180);

    const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
      Math.cos(origin.lat() * (Math.PI / 180)) *
      Math.cos(destination.lat() * (Math.PI / 180)) *
      Math.sin(dLon / 2) * Math.sin(dLon / 2);

    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    const distance = earthRadiusKm * c * 1000; // meters

    return distance;
  }

  getLimitDistance(distances: number[]): number {
    const n = distances.length;
    const index = Math.ceil(n / 4) - 1;  // At least 25% of the distances will be inside the circle
    const distance = distances[index];

    return distance * 1.05;
  }

  // Algorithm
  binarySearchInsert(array: number[], value: number): number {
    let low = 0;
    let high = array.length - 1;

    while (low <= high) {
      let mid = Math.floor((low + high) / 2);
      if (array[mid] === value)
        return mid + 1;
      else if (array[mid] < value)
        low = mid + 1;
      else
        high = mid - 1;
    }

    return low;
  }
}