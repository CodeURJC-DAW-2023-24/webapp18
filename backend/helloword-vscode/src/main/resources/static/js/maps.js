async function initMap() {
    const center = { lat: 40.335299133707615, lng: -3.8775389329638457 };
    const map = new google.maps.Map(document.getElementById('map'), {
        zoom: 15,
        center: center,
        disableDefaultUI: true
    });

    new google.maps.Marker({
        position: center,
        map: map,
        title: 'HOME'
    });

    var directions = [
        "Piscina municipal Las Cumbres",
        "Piscina El Soto"
    ];

    var geocoder = new google.maps.Geocoder();
    directions.forEach(function(direction) {
        geocoder.geocode({ 'address': direction }, function(results, status) {
            if (status == 'OK') {
                new google.maps.Marker({
                    position: results[0].geometry.location,
                    map: map,
                    title: direction
                });
            } else {
                alert('Geocode was not successful for the following reason: ' + status);
            }
        });
    });
}

initMap();