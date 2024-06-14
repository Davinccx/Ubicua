// Función para calcular el importe medio de los usuarios
async function calcularImporteMedio() {
    try {
        // Fetch para obtener los datos JSON desde el servlet
        const response = await fetch('../getHistorico');  // Asegúrate de que la URL sea correcta
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const jsonData = await response.json();

        // Calcular el importe medio total
        const totalImporte = jsonData.reduce((sum, data) => sum + data.importe, 0);
        const averageImporte = totalImporte / jsonData.length;

        // Mostrar el importe medio en el párrafo
        document.getElementById('averageImporte').textContent = `Importe medio de los usuarios: ${averageImporte.toFixed(2)} €`;
    } catch (error) {
        console.error('Error fetching or processing data:', error);
    }
}

async function obtenerRegistrosYMostrarEstadisticas() {
    try {
        // Obtener los datos JSON de los registros
        const response = await fetch('../getHistorico');
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const registros = await response.json();

        // Calcular la media de estancia en el parking
        const mediaEstancia = calcularMediaEstancia(registros);

        // Mostrar la media de estancia en el parking en el párrafo correspondiente
        const mediaEstanciaParagraph = document.getElementById('mediaEstancia');
        mediaEstanciaParagraph.textContent = `Media de estancia en el parking: ${mediaEstancia.horas} horas, ${mediaEstancia.minutos} minutos y ${mediaEstancia.segundos} segundos`;

    } catch (error) {
        console.error('Error fetching or processing data:', error);
    }
}


// Función para crear gráficos de porcentaje de ocupación de los parkings
// Función para crear gráficos de porcentaje de ocupación de los parkings
async function crearGraficosPorcentajeOcupacion() {
    try {
        // Fetch para obtener los datos JSON de los parkings
        const response = await fetch('../getParkings');
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const jsonData = await response.json();

        // Crear un gráfico tipo pie por cada parking
        jsonData.forEach(parking => {
            const porcentajeOcupacion = ((parking.capacidad_total - parking.plazas_disponibles) / parking.capacidad_total) * 100;

            // Crear el contenedor del gráfico
            const container = document.createElement('div');
            container.classList.add('chart-container');

            // Crear el canvas para el gráfico
            const canvas = document.createElement('canvas');
            canvas.width = 200;
            canvas.height = 200;
            container.appendChild(canvas);

            // Agregar el contenedor al contenedor principal
            document.getElementById('chartsAndNamesContainer').appendChild(container);

            // Configurar datos y opciones del gráfico
            const ctx = canvas.getContext('2d');
            new Chart(ctx, {
                type: 'doughnut',
                data: {
                    labels: ['Ocupadas', 'Disponibles'],
                    datasets: [{
                            data: [porcentajeOcupacion, 100 - porcentajeOcupacion],
                            backgroundColor: ['#FF6384', '#36A2EB'],
                            hoverBackgroundColor: ['#FF6384', '#36A2EB']
                        }]
                },
                options: {
                    responsive: false,
                    maintainAspectRatio: true,
                    plugins: {
                        datalabels: {
                            display: false // Ocultar las etiquetas porcentuales dentro de las piezas del gráfico
                        }
                    }
                }
            });

            // Agregar el nombre del parking encima del gráfico
            const nameElement = document.createElement('div');
            nameElement.textContent = parking.nombre;
            nameElement.classList.add('chart-name');
            container.appendChild(nameElement);
        });

    } catch (error) {
        console.error('Error fetching or processing data:', error);
    }
}

async function crearGraficoReservasPorParking() {
    try {
        // Fetch para obtener los datos JSON de los parkings
        const response = await fetch('../getParkings');
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const jsonData = await response.json();

        // Ordenar los parkings por cantidad de reservas en orden descendente
        jsonData.sort((a, b) => b.reservas - a.reservas);

        // Obtener los datos para el gráfico
        const labels = jsonData.map(parking => parking.nombre);
        const data = jsonData.map(parking => parking.reservas);

        // Crear el canvas para el gráfico
        const canvas = document.createElement('canvas');
        canvas.width = 400;
        canvas.height = 300;
        document.getElementById('chartsContainer3').appendChild(canvas);

        // Configurar datos y opciones del gráfico
        const ctx = canvas.getContext('2d');
        new Chart(ctx, {
            type: 'horizontalBar',
            data: {
                labels: labels,
                datasets: [{
                        label: 'Reservas por Parking',
                        data: data,
                        backgroundColor: '#4BC0C0' // Color de las barras
                    }]
            },
            options: {
                responsive: false,
                maintainAspectRatio: false,
                scales: {
                    x: {
                        beginAtZero: true,
                        ticks: {
                            callback: function (value) {
                                return value;
                            } // No necesitas agregar nada extra a las etiquetas del eje x
                        }
                    }
                },
                plugins: {
                    legend: {
                        display: false // Ocultar la leyenda
                    },
                    datalabels: {
                        anchor: 'end',
                        align: 'end',
                        color: '#fff' // Color blanco para el texto de las etiquetas
                    }
                }
            }
        });
    } catch (error) {
        console.error('Error fetching or processing data:', error);
    }
}

function calcularMediaEstancia(registros) {
    // Calcular el total de tiempo de estancia en segundos
    const totalTiempo = registros.reduce((acumulador, registro) => {
        const inicio = new Date(registro.hora_inicio);
        const fin = new Date(registro.hora_fin);
        const tiempoEstanciaSegundos = (fin - inicio) / 1000; // Convertir a segundos
        return acumulador + tiempoEstanciaSegundos;
    }, 0);

    // Calcular el número total de registros
    const totalRegistros = registros.length;

    // Calcular la media de estancia en segundos
    const mediaEstanciaSegundos = totalTiempo / totalRegistros;

    // Convertir la media de estancia a horas, minutos y segundos
    const horas = Math.floor(mediaEstanciaSegundos / 3600);
    const minutos = Math.floor((mediaEstanciaSegundos % 3600) / 60);
    const segundos = Math.floor(mediaEstanciaSegundos % 60);

    return {horas, minutos, segundos};
}


async function crearGraficoReservasPorParking() {
    try {
        // Obtener los datos JSON de las reservas
        const response = await fetch('../getReservas');
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const reservas = await response.json();

        // Contar el número de reservas por parking
        const reservasPorParking = {};
        reservas.forEach(reserva => {
            const parkingId = reserva.parking_id;
            if (!reservasPorParking[parkingId]) {
                reservasPorParking[parkingId] = 0;
            }
            reservasPorParking[parkingId]++;
        });

        // Preparar los datos para el gráfico
        const nombresParkings = Object.keys(reservasPorParking);
        const numReservas = Object.values(reservasPorParking);

        // Crear el canvas para el gráfico
        const canvas = document.createElement('canvas');
        canvas.width = 400;
        canvas.height = 300;
        document.getElementById('chartsContainer').appendChild(canvas);

        // Configurar datos y opciones del gráfico
        const ctx = canvas.getContext('2d');
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: nombresParkings,
                datasets: [{
                    label: 'Número de Reservas por Parking',
                    data: numReservas,
                    backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF'] // Colores de las barras
                }]
            },
            options: {
                responsive: false,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            precision: 0 // Mostrar números enteros en el eje y
                        }
                    }
                }
            }
        });

    } catch (error) {
        console.error('Error fetching or processing data:', error);
    }
}

// Llamar a la función para crear los gráficos cuando la página se haya cargado
window.onload = function () {
    obtenerRegistrosYMostrarEstadisticas();
    calcularImporteMedio();
    crearGraficosPorcentajeOcupacion();
    crearGraficoReservasPorParking();

};