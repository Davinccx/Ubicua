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

function fetchAndDisplayTable() {
    fetch('http://localhost:8080/getReservas') // Asegúrate de que la URL sea correcta
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                let tabla = '<table border="1">';
                tabla += '<tr><th>Reserva ID</th><th>Usuario ID</th><th>Parking ID</th><th>Fecha</th><th>Hora Inicio</th><th>Hora Fin</th><th>Número de Plaza</th><th>Acciones</th></tr>';
                data.forEach(objeto => {
                    tabla += `<tr>`;
                    tabla += `<td id= "editable-cell-1" id="reserva_id">${objeto.reserva_id}</td>`;
                    tabla += `<td contenteditable="false" id="id_user">${objeto.user_id}</td>`;
                    tabla += `<td contenteditable="false" id="id_parking">${objeto.parking_id}</td>`;
                    tabla += `<td contenteditable="false" id="fecha">${objeto.fecha_reserva}</td>`;
                    tabla += `<td id= "editable-cell-1" id="hora_inicio">${objeto.hora_inicio}</td>`;
                    tabla += `<td contenteditable="false" id="hora_fin">${objeto.hora_fin}</td>`;
                    tabla += `<td contenteditable="false" id="id_plaza">${objeto.id_plaza}</td>`;

                    tabla += `<td>
                            <i class="fa fa-eye edit-icon" style="cursor:pointer;" onclick="verCompra(${objeto.id_venta})"></i>
                           
                            <i class="fa fa-edit edit-icon" style="cursor:pointer;" onclick="editarVenta(this, ${objeto.id_venta})"></i>
                            
                            <i class="fa fa-trash delete-icon" style="cursor:pointer;" onclick="eliminarReserva(${objeto.reserva_id},${objeto.parking_id},${objeto.id_plaza})"></i>
                          </td>`;
                    tabla += `</tr>`;
                });
                tabla += '</table>';
                document.getElementById('tabla-container').innerHTML = tabla;
            })
            .catch(error => {
                console.error('Fetch error:', error);
                document.getElementById('tabla-container').innerHTML = 'Error al cargar los datos.';
            });
}

// Ejecuta la función cuando la ventana se carga
window.onload = function () {
    fetchAndDisplayTable();
    loadUsername();
};

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

function verCompra(id_venta) {
    // Llamada fetch aquí
    if (!id_venta) {
        console.error('ID de venta no proporcionado');
        return;
    }

    // Llamada fetch para obtener los detalles de la compra
    fetch(`http://localhost:8080/Vendify/getVentaFromId?id_venta=${id_venta}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok: ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                // Verifica que el elemento 'contenidoCompra' existe
                const contenidoCompra = document.getElementById('contenidoCompra');
                if (contenidoCompra) {
                    contenidoCompra.innerHTML = 'Detalles de la compra: ' + JSON.stringify(data, null, 2);
                } else {
                    console.error('Elemento "contenidoCompra" no encontrado');
                }

                // Verifica que el elemento 'popupCompra' y 'overlay' existen y mostrar el popup
                const popupCompra = document.getElementById('popupCompra');
                const overlay = document.getElementById('overlay');
                if (popupCompra && overlay) {
                    popupCompra.style.display = 'block';
                    overlay.style.display = 'block';
                } else {
                    console.error('Elemento "popupCompra" o "overlay" no encontrado');
                }
            })
            .catch(error => {
                console.error('Fetch error:', error);

                // Verifica que el elemento 'tabla-container' existe
                const tablaContainer = document.getElementById('tabla-container');
                if (tablaContainer) {
                    tablaContainer.innerHTML = 'Error al cargar los datos.';
                } else {
                    console.error('Elemento "tabla-container" no encontrado');
                }
            });

}

function cerrarPopup() {
    const popupCompra = document.getElementById('popupCompra');
    const overlay = document.getElementById('overlay');
    if (popupCompra && overlay) {
        popupCompra.style.display = 'none';
        overlay.style.display = 'none';
    } else {
        console.error('Elemento "popupCompra" o "overlay" no encontrado');
    }
}

function editarVenta(icon, id_venta) {
    let row = icon.parentNode.parentNode;
    let cells = row.getElementsByTagName("td");
    for (let i = 1; i < cells.length - 1; i++) { // Excluyendo la columna de acciones
        cells[i].setAttribute("contenteditable", true);
        cells[i].style.border = "2px solid blue"; // Estilo para indicar que es editable
    }

    // Cambiar el icono de editar a guardar
    icon.classList.remove("fa-edit");
    icon.classList.add("fa-save");
    icon.setAttribute("onclick", `guardarCambios(this, ${id_venta})`);
}

function guardarCambios(icon, id_venta) {
    let row = icon.parentNode.parentNode;
    let cells = row.getElementsByTagName("td");

    // Aquí debes obtener los datos de las celdas editables y almacenarlos en una variable
    // Por ejemplo:
    let data = {

        id: id_venta,
        id_usuario: cells[1].innerText,
        id_producto: cells[2].innerText,
        id_maquina: cells[3].innerText
    };

    if (confirm('¿Estás seguro de que deseas actualizar los datos de esta venta?')) {
        // Completar la llamada fetch con el método correcto, headers y body
        fetch(`http://localhost:8080/Vendify/UpdateVenta`, {
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
                        console.error('Error al actualizar la venta');
                    }
                })
                .catch(error => {
                    console.error('Error al realizar la solicitud:', error);
                });
    } // Falta esta llave de cierre para el if

    for (let i = 0; i < cells.length; i++) {
        cells[i].setAttribute("contenteditable", false);
        cells[i].style.border = "none";
    }

    // Cambiar el icono de guardar a editar
    icon.classList.remove("fa-save");
    icon.classList.add("fa-edit");
    icon.setAttribute("onclick", `editarVenta(this, ${id_venta})`);
} 