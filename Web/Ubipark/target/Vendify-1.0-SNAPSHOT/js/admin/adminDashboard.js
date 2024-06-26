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
    fetch('http://localhost:8080/getUsers') // Asegúrate de que la URL sea correcta
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                let tabla = '<table border="1">';
                tabla += '<tr><th>ID Usuario</th><th>Username</th><th>Nombre</th><th>Apellido</th><th>Email</th><th>Password</th><th>Teléfono</th><th>Fecha Registro</th><th>Matrícula</th><th>Acciones</th></tr>';
                data.forEach(objeto => {
                    tabla += `<tr>`;
                    tabla += `<td user_id=">${objeto.user_id}</td>`;
                    tabla += `<td id= "editable-cell-1">${objeto.user_id}</td>`;
                    tabla += `<td contenteditable="false" id="username">${objeto.username}</td>`;
                    tabla += `<td contenteditable="false" id="nombre">${objeto.nombre}</td>`;
                    tabla += `<td contenteditable="false" id="apellido">${objeto.apellido}</td>`;
                    tabla += `<td contenteditable="false" id="email">${objeto.email}</td>`;
                    tabla += `<td contenteditable="false" id="password">${objeto.password}</td>`;
                    tabla += `<td contenteditable="false" id="telephone">${objeto.telefono}</td>`;
                    tabla += `<td contenteditable="false" id="fecha_registro">${objeto.fecha_registro}</td>`;
                    tabla += `<td contenteditable="false" id="matricula">${objeto.matricula}</td>`;




                    tabla += `<td>
                            <i class="fa fa-edit edit-icon" style="cursor:pointer;" onclick="editarUsuario(this, ${objeto.id})"></i>
                            &nbsp;
                            <i class="fa fa-trash delete-icon" style="cursor:pointer;" onclick="eliminarUsuario(${objeto.user_id})"></i>
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

function eliminarUsuario(user_id) {
    if (confirm('¿Estás seguro de que deseas eliminar este usuario?')) {
        // Hacer la solicitud DELETE al servlet
        fetch(`http://localhost:8080/deleteUser?id=${user_id}`, {
            method: 'DELETE'
        })
                .then(response => {
                    if (response.ok) {
                        // Eliminación exitosa, puedes recargar la página o realizar alguna otra acción
                        location.reload(); // Recargar la página
                    } else {
                        // Mostrar un mensaje de error o realizar otra acción en caso de error
                        response.text().then(text => alert('Error: ' + text));
                        console.error('Error al eliminar el usuario');
                    }
                })
                .catch(error => {
                    console.error('Error al realizar la solicitud:', error);
                });
    }
}

function editarUsuario(icon, user_id) {
    let row = icon.parentNode.parentNode;
    let cells = row.getElementsByTagName("td");
    for (let i = 1; i < cells.length - 1; i++) { // Excluyendo la columna de acciones
        cells[i].setAttribute("contenteditable", true);
        cells[i].style.border = "2px solid blue"; // Estilo para indicar que es editable
    }

    // Cambiar el icono de editar a guardar
    icon.classList.remove("fa-edit");
    icon.classList.add("fa-save");
    icon.setAttribute("onclick", `guardarCambios(this, ${user_id})`);
}

function guardarCambios(icon, user_id) {
    let row = icon.parentNode.parentNode;
    let cells = row.getElementsByTagName("td");

    // Aquí debes obtener los datos de las celdas editables y almacenarlos en una variable
    // Por ejemplo:
    let data = {

        id: user_id,
        username: cells[1].innerText,
        nombre: cells[2].innerText,
        apellido: cells[3].innerText,
        email: cells[4].innerText,
        password: cells[5].innerText,
        telefono: cells[6].innerText,
        matricula: cells[8].innerText
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
                        console.error('Error al actualizar el usuario');
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
    icon.setAttribute("onclick", `editarUsuario(this, ${user_id})`);
} 

// Ejecuta la función cuando la ventana se carga
window.onload = function () {
    fetchAndDisplayTable();
    loadUsername();
};