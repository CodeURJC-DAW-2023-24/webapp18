export interface MapsResponse {
    center?: string;
    offers?: MapsOffer[];
}

export interface MapsOffer {
    id?: number;
    address?: string;
    poolName?: string;
    type?: string;
    salary?: string;
    start?: string;
}

export interface MapsAddress {
    address: string;
    offers: MapsOffer[];
    coord: google.maps.LatLng;
}