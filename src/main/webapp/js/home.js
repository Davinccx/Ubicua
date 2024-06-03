function loadGoogleMaps(apiKey) {
    const script = document.createElement('script');
    script.src = `https://maps.googleapis.com/maps/api/js?key=${apiKey}&callback=initMap`;
    script.async = true;
    script.defer = true;
    document.head.appendChild(script);
}


let coordinates = [{lat:37.3923848,lng:-5.9749377},{lat:40.4228,lng:-3.7088},{ lat: 40.5130335, lng: -3.3487276 }]
// Inicializar y añadir el mapa

// Obtener la clave de API desde un archivo de configuración (config.json)
fetch('config.json')
    .then(response => response.json())
    .then(config => {
        loadGoogleMaps(config.apiKey);
    })
    .catch(error => {
        console.error('Error al cargar la clave de API:', error);
    });
    
fetch('getParkings')
    .then(response => response.json())
    .then(data => {
        parkings = data;
        displayParkingInfo();
    })
    .catch(error => {
        console.error('Error al cargar los datos del parking:', error);
    });
    
function displayParkingInfo() {
    const container = document.getElementById('parking-info-container');
    container.innerHTML = '';
    parkings.forEach(parking => {
        const infoDiv = document.createElement('div');
        infoDiv.classList.add('parking-info');
        infoDiv.innerHTML = `   <h3>${parking.nombre}</h3>
            <p>${parking.direccion}</p>
            <p>Capacidad Total: ${parking.capacidad_total}</p>
            <p>Plazas Disponibles: ${parking.plazas_disponibles}</p>
        `;
        container.appendChild(infoDiv);
    });
}

function initMap() {
    // Usar la API de Geolocalización para obtener la ubicación actual del usuario
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
            position => {
                const location = {
                    lat: position.coords.latitude,
                    lng: position.coords.longitude
                };

                // Crear el mapa centrado en la ubicación del usuario
                const map = new google.maps.Map(document.getElementById('map'), {
                    zoom: 6,
                    center: location
                 
                });

                // Añadir un marcador en la ubicación del usuario
                const marker = new google.maps.Marker({ position: location, map: map, title:'Aquí se encuentra usted',icon: {
        url: 'http://maps.google.com/mapfiles/ms/icons/blue-dot.png' // Cambiar el color del marcador a azul
    }});
                
                new google.maps.Marker({ position: location, map: map });
                
                coordinates.forEach(coord => {
                    new google.maps.Marker({
                        position: coord,
                        map: map, 
                        title: 'Ubicación del Parking'
                    });
                });
            },
            error => {
                console.error('Error al obtener la ubicación:', error);
                // Fallback: usar una ubicación predeterminada (por ejemplo, Politécnica UAH)
                const fallbackLocation = { lat: 40.5130335, lng: -3.3487276 };
                const map = new google.maps.Map(document.getElementById('map'), {
                    zoom: 8,
                    center: fallbackLocation
                });
                const marker = new google.maps.Marker({ position: fallbackLocation, map: map });
            }
        );
    } else {
        console.error('Geolocalización no es soportada por este navegador.');
        // Fallback: usar una ubicación predeterminada (por ejemplo, Sidney)
        const fallbackLocation = { lat: -34.397, lng: 150.644 };
        const map = new google.maps.Map(document.getElementById('map'), {
            zoom: 8,
            center: fallbackLocation
        });
        const marker = new google.maps.Marker({ position: fallbackLocation, map: map });
    }
}