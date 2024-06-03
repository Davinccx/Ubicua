function loadGoogleMaps(apiKey) {
    const script = document.createElement('script');
    script.src = `https://maps.googleapis.com/maps/api/js?key=${apiKey}&callback=initMap`;
    script.async = true;
    script.defer = true;
    document.head.appendChild(script);
}

// Inicializar y añadir el mapa
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
                    zoom: 15,
                    center: location
                });

                // Añadir un marcador en la ubicación del usuario
                const marker = new google.maps.Marker({ position: location, map: map });
            },
            error => {
                console.error('Error al obtener la ubicación:', error);
                // Fallback: usar una ubicación predeterminada (por ejemplo, Sidney)
                const fallbackLocation = { lat: -34.397, lng: 150.644 };
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

// Obtener la clave de API desde un archivo de configuración (config.json)
fetch('config.json')
    .then(response => response.json())
    .then(config => {
        loadGoogleMaps(config.apiKey);
    })
    .catch(error => {
        console.error('Error al cargar la clave de API:', error);
    });