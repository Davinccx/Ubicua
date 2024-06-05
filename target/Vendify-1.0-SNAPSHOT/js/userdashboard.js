function loadUsername() {
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            document.getElementById("username").innerText = this.responseText;
        }
    };
    xhr.open("GET", "userLogin", true);
    xhr.send();
}

function cargarDatosUsuario() {
    fetch('http://localhost:8080/getUserFromUsername')
            .then(response => response.json())
            .then(data => {

                document.getElementById('nombre').value = data.nombre;
                document.getElementById('apellido').value = data.apellido;
                document.getElementById('user_name').value = data.username;
                document.getElementById('email').value = data.email;
                document.getElementById('password').value = data.password;
                document.getElementById('telefono').value = data.telefono;
                document.getElementById('matricula').value = data.matricula;
            })
            .catch(error => console.error('Error:', error));
}

function habilitarEdicion() {

    document.getElementById('password').readOnly = false;
    document.getElementById('telefono').readOnly = false;
    document.getElementById('nombre').readOnly = false;
    document.getElementById('apellido').readOnly = false;


    // Cambiar el valor del botón a "Actualizar" y su función de onclick
    var boton = document.getElementById('boton-editar');
    boton.value = 'Actualizar';
    boton.onclick = actualizarUsuario;
}



function toggleTokenVisibility() {
    var tokenField = document.getElementById('matricula');
    var toggleButton = document.getElementById('toggle-token');
    if (tokenField.type === 'password') {
        tokenField.type = 'text';
        toggleButton.value = 'Ocultar Matricula';
        alert('ATENCIÓN:  NO LO COMPARTAS CON NADIE!.')
    } else {
        tokenField.type = 'password';
        toggleButton.value = 'Mostrar Matricula';
    }
}

function actualizarUsuario() {

    let data = {

        nombre: document.getElementById('nombre').value,
        apellido: document.getElementById('apellido').value,
        email: document.getElementById('email').value,
        password: document.getElementById('password').value,
        username: document.getElementById('user_name').value,
        telefono: document.getElementById('telefono').value,
    };

    if (confirm('¿Estás seguro de que deseas actualizar los datos de este usuario?')) {
        // Completar la llamada fetch con el método correcto, headers y body
        fetch(`http://localhost:8080/updateUser`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(data)
        })
                .then(response => {
                    if (response.ok) {
                        // Actualización exitosa, puedes recargar la página o realizar alguna otra acción
                        location.reload(); // Recargar la página
                    } else {
                        // Mostrar un mensaje de error o realizar otra acción en caso de error
                        console.error(data);
                    }
                })
                .catch(error => {
                    console.error('Error al realizar la solicitud:', error);
                });
    } else {
        location.reload();
    }
}

fetch('getReservasFromUsername')
        .then(response => response.json())
        .then(data => {
            reservas = data;
            displayReservasInfo();
        })
        .catch(error => {
            console.error('Error al cargar los datos del parking:', error);
        });

function displayReservasInfo() {
    const container = document.getElementById('reservas-info-container');
    container.innerHTML = '';
    if (reservas.length === 0) {
        const noReservasLabel = document.createElement('p');
        noReservasLabel.classList.add('no-reservas');
        noReservasLabel.innerText = 'Todavía no hay reservas a tu nombre!';
        container.appendChild(noReservasLabel);
    } else {
        reservas.forEach(reserva => {
            const infoDiv = document.createElement('div');
            infoDiv.classList.add('reserva-info');
            infoDiv.innerHTML = `
                        <h3>Identificador: ${reserva.reserva_id}</h3>
                        <p>Identificador Parking: ${reserva.parking_id}</p>
                        <p>Fecha: ${reserva.fecha_reserva}</p>
                        <p>Hora Inicio: ${reserva.hora_inicio}</p>
                        <p>Hora Fin: ${reserva.hora_fin}</p>
                        <p>Número plaza: ${reserva.id_plaza}</p>
                        <i class="fa fa-trash delete-icon" onclick="eliminarReserva(${reserva.reserva_id},${reserva.parking_id},${reserva.id_plaza})"></i>
                    `;
            container.appendChild(infoDiv);
        });
    }
}

function eliminarReserva(reserva_id, parking_id, plaza_id) {
    if (confirm('¿Estás seguro de que deseas eliminar esta reserva?')) {
        // Hacer la solicitud DELETE al servlet con los parámetros adicionales
        fetch(`http://localhost:8080/deleteReserva?reserva_id=${reserva_id}&parking_id=${parking_id}&plaza_id=${plaza_id}`, {
            method: 'DELETE'
        })
                .then(response => {
                    if (response.ok) {
                        // Eliminación exitosa, puedes recargar la página o realizar alguna otra acción
                        location.reload(); // Recargar la página
                    } else {
                        // Mostrar un mensaje de error o realizar otra acción en caso de error
                        console.error('Error al eliminar la reserva');
                    }
                })
                .catch(error => {
                    console.error('Error al realizar la solicitud:', error);
                });
    }
}

window.onload = function () {
    cargarDatosUsuario();
    loadUsername();
};
