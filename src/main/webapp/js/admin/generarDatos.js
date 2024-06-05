function loadUsername() {
    document.addEventListener('DOMContentLoaded', function () {
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function () {
            if (this.readyState == 4 && this.status == 200) {
                document.getElementById("username").innerText = this.responseText;
            }
        };
        xhr.open("GET", "../userLogin", true);
        xhr.send();
    });
}

document.addEventListener('DOMContentLoaded', (event) => {
    document.getElementById('boton-generar').addEventListener('click', function () {
        fetch('http://localhost:8080/generateUser', {
            method: 'POST'
        })
                .then(response => response.json())
                .then(data => {
                    if (data.error) {
                        alert('Error: ' + data.error);
                    } else {
                        document.getElementById('email').value = data.email;
                        document.getElementById('nombre').value = data.nombre;
                        document.getElementById('apellido').value = data.apellido;
                        document.getElementById('user_name').value = data.username;
                        document.getElementById('password').value = data.password;
                        document.getElementById('telefono').value = data.telefono;
                        document.getElementById('fecha').value = data.fecha_registro;
                        document.getElementById('matricula').value = data.matricula;
                        alert('El usuario se ha generado correctamente');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                });
    });
});

document.addEventListener('DOMContentLoaded', (event) => {
    document.getElementById('boton-generar-parking').addEventListener('click', function () {
        fetch('http://localhost:8080/generateParking', {
            method: 'POST'
        })
                .then(response => response.json())
                .then(data => {
                    if (data.error) {
                        alert('Error: ' + data.error);
                    } else {
                        document.getElementById('nombreParking').value = data.nombre;
                        document.getElementById('direccion').value = data.direccion;
                        document.getElementById('ciudad').value = data.ciudad;
                        document.getElementById('c_postal').value = data.codigo_postal;
                        document.getElementById('capacidad').value = data.capacidad_total;
                        document.getElementById('disponibles').value = data.plazas_disponibles;
                        alert('El parking se ha generado correctamente');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                });
    });
});

document.addEventListener('DOMContentLoaded', (event) => {
    document.getElementById('boton-generar-reserva').addEventListener('click', function () {
        fetch('http://localhost:8080/generateReserva', {
            method: 'POST'
        })
                .then(response => response.json())
                .then(data => {
                    if (data.error) {
                        alert('Error: ' + data.error);
                    } else {
                        document.getElementById('usuario_id').value = data.user_id;
                        document.getElementById('parking_id').value = data.parking_id;
                        document.getElementById('fecha_reserva').value = data.fecha_reserva;
                        document.getElementById('hora_inicio').value = data.hora_inicio;
                        document.getElementById('hora_fin').value = data.hora_fin;
                        document.getElementById('id_plaza').value = data.plaza_id;
                        alert('La reserva se ha generado correctamente');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                });
    });
});
            