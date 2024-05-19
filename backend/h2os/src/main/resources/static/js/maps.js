let map;
let geocoder;
let center;
let homeIcon;
let poolsAddress = [];
let circle;

// -------------------------------------- LOAD MAP ------------------------------------------
async function initMap() {
    geocoder = new google.maps.Geocoder();
    center = await checkUserAddress(document.getElementById('user'));
    for (const element of document.querySelectorAll('[id^="offer-"]')) {
        poolsAddress.push(element.textContent.trim());
    }

    map = new google.maps.Map(document.getElementById('map'), {
        zoom: 15,
        center: center,
        disableDefaultUI: true
    });

    homeIcon = {
        url: "https://cdn-icons-png.flaticon.com/512/1196/1196783.png",
        scaledSize: new google.maps.Size(44, 44)
    }

    setMarkers(center, poolsAddress);
}

async function checkUserAddress(userAddressElement) {
    center = { lat: 40.335299133707615, lng: -3.8775389329638457 };  // URJC

    if (userAddressElement == null || userAddressElement.textContent.trim() == '') return center;
    const userAddress = userAddressElement.textContent.trim();

    try {
        const location = await localizateAddress(userAddress);
        center = location;
    } catch (error) {
        alert('Could not find the provided user location: ' + userAddress + '.\nThe map will be centered at the URJC location');
    }

    return center;
}

async function setMarkers(center, addressList) {
    const bounds = new google.maps.LatLngBounds();
    let visitedAddresses = new Set();

    const home = setMarker(center, 'HOME', homeIcon);
    home.addListener('click', function() {
        window.location.href = '/login';
    });
    bounds.extend(home.getPosition());

    for (const address of addressList) {
        if (visitedAddresses.has(address)) continue
        else visitedAddresses.add(address);

        try {
            const location = await localizateAddress(address);
            const marker = setMarker(location, address);
            setInfoWindow(marker, address);
            bounds.extend(marker.getPosition());
        } catch (error) {
            alert(error.message);
        }
    }

    map.fitBounds(bounds);
}

async function setInfoWindow(marker, address) {
    const response = await fetch('/maps/offers?address=' + address);
    const contentString = await response.text();

    const infoWindow = new google.maps.InfoWindow({
        content: contentString
    });

    marker.addListener('click', function() {
        infoWindow.open(map, marker);
    });

    map.addListener('click', function() {
        infoWindow.close();
    });
}

// Auxiliary functions
async function localizateAddress(address) {
    const [results, status] = await new Promise(resolve => {
        geocoder.geocode({ 'address': address }, function(results, status) {
            resolve([results, status]);
        });
    });

    if (status == 'OK') {
        return results[0].geometry.location;
    } else {
        throw new Error('Geocode was not successful with ' + address + ' for the following reason: ' + status);
    }
}

function setMarker(position, title, icon) {
    return new google.maps.Marker({
        position: position,
        map: map,
        title: title,
        icon: icon,
        animation: google.maps.Animation.DROP
    });
}

// -------------------------------------- FILTER ------------------------------------------
async function filterByDistance() {
    const visitedAddresses = new Set();
    const distancesMap = new Map();
    const distances = [];

    for (const address of poolsAddress) {
        let distance;
        if (visitedAddresses.has(address)) {
            distance = distancesMap.get(address);
        } else {
            try {
                distance = await calculateDistance(center, address);
            }
            catch (error) {
                alert(error.message);
                continue;
            }
            visitedAddresses.add(address);
            distancesMap.set(address, distance);
        }

        const insertIndex = binarySearchInsert(distances, distance);
        distances.splice(insertIndex, 0, distance);
    }

    const distance = getLimitDistance(distances);
    setFilterCircle(distance);
}

function getLimitDistance(distances) {
    const n = distances.length;
    const index = Math.ceil(n/4) - 1;  // At least 25% of the distances will be inside the circle
    const distance = distances[index];

    return distance * 1.05;
}

function setFilterCircle(distance) {
    const circleOptions = {
        strokeWeight: 0,
        fillColor: '#0082fe',
        fillOpacity: 0.35,
        map: map,
        center: center,
        radius: distance
    };

    if (circle) circle.setMap(null);
    circle = new google.maps.Circle(circleOptions);

    const circleBounds = circle.getBounds();
    const padding = 0;
    map.fitBounds(circleBounds, padding);
}

// Auxiliary functions
async function calculateDistance(origin, destinationAddress) {
    try {
        const destination = await localizateAddress(destinationAddress);
        return calculateEuclideanDistance(origin, destination);
    } catch (error) {
        throw new Error('Failed to calculate the distance to the address ' + destinationAddress + '. \n' + error.message);
    }
}

function calculateEuclideanDistance(origin, destination) {
    const earthRadiusKm = 6371;

    const dLat = degreesToRadians(destination.lat() - origin.lat());
    const dLon = degreesToRadians(destination.lng() - origin.lng());

    const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(degreesToRadians(origin.lat())) * Math.cos(degreesToRadians(destination.lat())) *
        Math.sin(dLon / 2) * Math.sin(dLon / 2);

    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    const distance = earthRadiusKm * c * 1000; // meters

    return distance;
}

function degreesToRadians(degrees) {
    return degrees * (Math.PI / 180);
}

function binarySearchInsert(array, value) {
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

/* // OLD FILTER BY DISTANCE (Problem: The distance was calculated by the driving route, so the circle was not accurate)
async function filterByDistance() {
    const distances = await calculateDistances();

    const distance = getNearestDistance(distances);
    setFilterCircle(distance);
}

async function calculateDistances() {
    return new Promise(resolve => {
        const service = new google.maps.DistanceMatrixService();
        service.getDistanceMatrix({
            origins: [center],
            destinations: poolsAddress,
            travelMode: 'DRIVING',
            unitSystem: google.maps.UnitSystem.METRIC
        }, function(response, status) {
            if (status == 'OK') {
                const distances = setDistances(response)
                resolve(distances);
            } else {
                console.error('Error al calcular las distancias: ' + status);
            }
        });
    });
}

function setDistances(response) {
    const results = response.rows[0].elements;
    const distances = [];

    for (const element of results) {
       const distance = distanceStringToMeters(element.distance.text);
       const insertIndex = binarySearchInsert(distances, distance);
       distances.splice(insertIndex, 0, distance);
    }

    return distances;
}

function distanceStringToMeters(distanceString){
    if (distanceString.includes('km')) {
        distanceNumber = parseFloat(distanceString.replace('km', '').replace(',', '.')) * 1000;
    } else if (distanceString.includes('cm')) {
        distanceNumber = parseFloat(distanceString.replace('cm', '').replace(',', '.')) / 100;
    } else {
        distanceNumber = parseFloat(distanceString.replace('m', '').replace(',', '.'));
    }

    return distanceNumber;
}
*/