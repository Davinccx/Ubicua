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
    fetch('http://localhost:8080/getParkings') // Asegúrate de que la URL sea correcta
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                let tabla = '<table border="1">';
                tabla += '<tr><th>ID Parking</th><th>Nombre</th><th>Direccion</th><th>Ciudad</th><th>Codigo Postal</th><th>Capacidad Total</th><th>Plazas Disponibles</th><th>Acciones</th></tr>';
                data.forEach(objeto => {
                    tabla += `<tr>`;
                    tabla += `<td id= "editable-cell-1" id="id_parking">${objeto.parking_id}</td>`;
                    tabla += `<td contenteditable="false" id="nombre">${objeto.nombre}</td>`;
                    tabla += `<td contenteditable="false" id="direccion">${objeto.direccion}</td>`;
                    tabla += `<td contenteditable="false" id="ciudad">${objeto.ciudad}</td>`;
                    tabla += `<td contenteditable="false" id="c_postal">${objeto.c_postal}</td>`;
                    tabla += `<td contenteditable="false" id="capacidsad">${objeto.capacidad_total}</td>`;
                    tabla += `<td contenteditable="false" id="disponibles">${objeto.plazas_disponibles}</td>`;
                    tabla += `<td>
                            
                            <i class="fa fa-edit edit-icon" style="cursor:pointer;" onclick="editarParking(this, ${objeto.parking_id})"></i>
                            &nbsp;
                            <i class="fa fa-trash delete-icon" style="cursor:pointer;" onclick="eliminarParking(${objeto.parking_id})"></i>
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

function eliminarParking(id_parking) {
    if (confirm('¿Estás seguro de que deseas eliminar este parking?')) {
        // Hacer la solicitud DELETE al servlet
        fetch(`http://localhost:8080/deleteParking?parking_id=${id_parking}`, {
            method: 'DELETE'
        })
                .then(response => {
                    if (response.ok) {
                        // Eliminación exitosa, puedes recargar la página o realizar alguna otra acción
                        location.reload(); // Recargar la página
                    } else {
                        // Mostrar un mensaje de error o realizar otra acción en caso de error
                        console.error('Error al eliminar el parking');
                    }
                })
                .catch(error => {
                    console.error('Error al realizar la solicitud:', error);
                });
    }
}
function editarParking(icon, parking_id) {
    let row = icon.parentNode.parentNode;
    let cells = row.getElementsByTagName("td");
    for (let i = 1; i < cells.length - 1; i++) { // Excluyendo la columna de acciones
        cells[i].setAttribute("contenteditable", true);
        cells[i].style.border = "2px solid blue"; // Estilo para indicar que es editable
    }

    // Cambiar el icono de editar a guardar
    icon.classList.remove("fa-edit");
    icon.classList.add("fa-save");
    icon.setAttribute("onclick", `guardarCambios(this, ${parking_id})`);
}

function guardarCambios(icon, id) {
    let row = icon.parentNode.parentNode;
    let cells = row.getElementsByTagName("td");

    // Aquí debes obtener los datos de las celdas editables y almacenarlos en una variable
    // Por ejemplo:
    let data = {

        id: id,
        nombre: cells[1].innerText,
        direccion: cells[2].innerText,
        ciudad: cells[3].innerText,
        c_postal: cells[4].innerText,
        c_total: cells[5].innerText,
        disponibles: cells[6].innerText

    };

    if (confirm('¿Estás seguro de que deseas actualizar los datos de este parking?')) {
        // Completar la llamada fetch con el método correcto, headers y body
        fetch(`http://localhost:8080/updateParking`, {
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
                        console.error('Error al actualizar el parking');
                    }
                })
                .catch(error => {
                    console.error('Error al realizar la solicitud:', error);
                });
    } // Falta esta llave de cierre para el if

    for (let i = 0; i < cells.length - 1; i++) {
        cells[i].setAttribute("contenteditable", false);
        cells[i].style.border = "none";
    }

    // Cambiar el icono de guardar a editar
    icon.classList.remove("fa-save");
    icon.classList.add("fa-edit");
    icon.setAttribute("onclick", `editarParking(this, ${id})`);
} // Falta esta llave de cierre para la función guardarCambios
            