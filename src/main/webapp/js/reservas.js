var username;
function loadUsername() {
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            username = this.responseText;
            document.getElementById("username").innerText = this.responseText;
        }
    };
    xhr.open("GET", "userLogin", true);
    xhr.send();
}

let parkingsData;

// Función para cargar los IDs de los parkings en el desplegable
function loadParkingIDs() {
    fetch('http://localhost:8080/getParkings')
            .then(response => response.json())
            .then(data => {
                parkingsData = data; // Almacenar los detalles de los parkings
                const parkingSelect = document.getElementById('parkingID');
                data.forEach(parking => {
                    const option = document.createElement('option');
                    option.value = parking.parking_id;
                    option.text = parking.parking_id;
                    parkingSelect.appendChild(option);
                });
            })
            .catch(error => console.error('Error al obtener IDs de parkings:', error));
}

// Llama a la función al cargar la página
window.onload = loadParkingIDs();

// Event listener para el cambio en la selección del parking
document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('parkingID').addEventListener('change', showParkingDetails);
});

function showParkingDetails() {
    // Obtener el ID del parking seleccionado
    const parkingID = this.value;

    // Buscar el parking seleccionado en los datos almacenados
    const selectedParking = parkingsData.find(parking => parking.parking_id === parseInt(parkingID));

    if (selectedParking) {
        // Mostrar los detalles del parking seleccionado
        document.getElementById('nombre').value = selectedParking.nombre;
        document.getElementById('location').value = selectedParking.direccion;
        loadParkingSpaces(parkingID);
    } else {
        console.error('Parking no encontrado en los datos almacenados.');
    }
}

function loadParkingSpaces(parkingID) {
    fetch(`http://localhost:8080/getPlazasFromParking?parking_id=${parkingID}`)
            .then(response => response.json())
            .then(data => {
                const plazasContainer = document.getElementById('plazas-container');
                plazasContainer.innerHTML = ''; // Limpiar el contenedor de plazas

                data.forEach(plaza => {

                    const plazaDiv = document.createElement('div');
                    plazaDiv.className = 'plaza';
                    plazaDiv.style.width = '30px';
                    plazaDiv.style.height = '30px';
                    plazaDiv.style.margin = '5px';
                    plazaDiv.style.display = 'flex';
                    plazaDiv.style.justifyContent = 'center';
                    plazaDiv.style.alignItems = 'center';
                    plazaDiv.style.color = 'white';
                    if (!plaza.ocupado) { // Verificar si la plaza no está ocupada
                        plazaDiv.classList.add('verde'); // Color verde para plazas libres
                        plazaDiv.innerText = plaza.id_plaza; // Mostrar el ID de la plaza    

                        plazaDiv.addEventListener('click', function () {
                            var valorPlaza = this.innerText;
                            document.getElementById('plaza').value = valorPlaza;
                        });
                    } else {
                        plazaDiv.style.backgroundColor = 'red'; // Color rojo para plazas ocupadas
                        plazaDiv.innerText = plaza.id_plaza;
                        plazaDiv.style.cursor = 'not-allowed'; // Cambiar cursor a no permitido
                    }

                    plazasContainer.appendChild(plazaDiv);
                });
            })
            .catch(error => console.error('Error al obtener las plazas:', error));
}

document.addEventListener('DOMContentLoaded', function () {
    var today = new Date();
    var dd = String(today.getDate()).padStart(2, '0');
    var mm = String(today.getMonth() + 1).padStart(2, '0'); // Enero es 0!
    var yyyy = today.getFullYear();

    today = yyyy + '-' + mm + '-' + dd;

    document.getElementById('date').min = today;
});

document.addEventListener('DOMContentLoaded', function () {
    var now = new Date();
    var hh = String(now.getHours()).padStart(2, '0');
    var mm = String(now.getMinutes()).padStart(2, '0');

    var currentTime = hh + ':' + mm;

    document.getElementById('time_inicio').min = currentTime;

});

document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('boton-reservar').addEventListener('click', function () {
        var parkingID = document.getElementById('parkingID').value;
        var date = document.getElementById('date').value;
        var time_inicio = document.getElementById('time_inicio').value;
        var time_fin = document.getElementById('time_fin').value;
        var plaza = document.getElementById('plaza').value;

        var data = {
            username: username,
            parkingID: parkingID,
            date: date,
            time_inicio: time_inicio,
            time_fin: time_fin,
            plaza: plaza
        };

        fetch('registerReserva', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
                .then(response => {
                    if (response.ok) {
                        // La solicitud se envió correctamente
                        console.log('Reserva registrada con éxito');
                        alert('Reserva registrada con éxito');
                        // Aquí puedes redirigir a otra página si es necesario
                    } else {
                        console.error('Error al registrar la reserva');
                    }
                })
                .catch(error => {
                    console.error('Error en la solicitud:', error);
                });
    });
});
