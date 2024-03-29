async function initMap() {
    center = await checkAddress(document.getElementById('user'));

    const map = new google.maps.Map(document.getElementById('map'), {
        zoom: 15,
        center: center,
        disableDefaultUI: true
    });

    const home = new google.maps.Marker({
        position: center,
        map: map,
        title: 'HOME',
        icon: {
            url: "https://cdn-icons-png.flaticon.com/512/1196/1196783.png",
            scaledSize: new google.maps.Size(44, 44) // Aquí puedes especificar el tamaño deseado en píxeles
        }
    });

    const bounds = new google.maps.LatLngBounds();
    bounds.extend(home.getPosition());

    const offerElements = document.querySelectorAll('[id^="offer-"]');
    const poolsAddress = [];

    for (const element of offerElements) {
        const address = element.textContent.trim();
        poolsAddress.push(address);
    }

    const geocoder = new google.maps.Geocoder();
    for (const address of poolsAddress) {
        const [results, status] = await new Promise(resolve => {
            geocoder.geocode({ 'address': address }, function(results, status) {
                resolve([results, status]);
            });
        });

        if (status == 'OK') {
            const marker = new google.maps.Marker({
                position: results[0].geometry.location,
                map: map,
                title: address
            });
            bounds.extend(marker.getPosition());
        } else {
            alert('Geocode was not successful for the following reason: ' + status);
        }
    }

    console.log("Calculando distancias desde HOME a:");
    let distances = await calculateDistances(center, poolsAddress.slice(2));
    console.log(distances);

    map.fitBounds(bounds);
}


async function checkAddress(userAddressElement) {
    let center = { lat: 40.335299133707615, lng: -3.8775389329638457 };  // URJC

    if (userAddressElement == null || userAddressElement.textContent.trim() == '') return center;
    const userAddress = userAddressElement.textContent.trim();

    const [results, status] = await new Promise(resolve => {
        const geocoder = new google.maps.Geocoder();

        geocoder.geocode({ 'address': userAddress }, function(results, status) {
            resolve([results, status]);
        });
    });

    if (status === 'OK' && results && results.length > 0)
        center = results[0].geometry.location;
    else
        alert('No se pudo encontrar la ubicación proporcionada.');

    return center;
}


async function calculateDistances(center, poolsAddress) {
    let distances = {};
    await new Promise(resolve => {
        const service = new google.maps.DistanceMatrixService();
        service.getDistanceMatrix({
            origins: [center],
            destinations: poolsAddress,
            travelMode: 'DRIVING',
            unitSystem: google.maps.UnitSystem.METRIC,
            avoidHighways: false,
            avoidTolls: false
        }, function(response, status) {
            if (status == 'OK') {
                const origins = response.originAddresses;
                const results = response.rows[0].elements;
                for (let i = 0; i < results.length; i++) {
                    const element = results[i];
                    const distance = element.distance.text;
                    const duration = element.duration.text;
                    const destination = poolsAddress[i];
                    distances[destination] = {
                        distance: distance,
                        duration: duration
                    };
                }
                resolve(distances);
            } else {
                console.error('Error al calcular las distancias: ' + status);
            }
        });
    });

    return distances;
}
