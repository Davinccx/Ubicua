// Configuración del gráfico
const config = {
    type: 'bar',
    data: null, // Debes asignar los datos recibidos del servidor aquí
    options: {
        scales: {
            y: {
                beginAtZero: true
            }
        }
    }
};

// Crear el gráfico cuando el DOM esté completamente cargado
document.addEventListener('DOMContentLoaded', function () {
    fetch('../getReservas')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            // Aquí puedes procesar los datos recibidos
            console.log('Datos recibidos:', data);
            
            // Obtener las fechas de las reservas
            const fechas = data.map(reserva => reserva.fecha_reserva);

            // Contar el número de reservas por fecha
            const reservasPorFecha = fechas.reduce((contador, fecha) => {
                contador[fecha] = (contador[fecha] || 0) + 1;
                return contador;
            }, {});

            // Convertir el objeto a arrays de etiquetas y valores
            const labels1 = Object.keys(reservasPorFecha);
            const valores1 = Object.values(reservasPorFecha);

            // Actualizar la configuración del gráfico con los datos obtenidos
            config.data = {
                labels: labels1,
                datasets: [{
                    label: 'Número de reservas',
                    data: valores1,
                    backgroundColor: 'rgba(75, 192, 192, 0.5)', // Cambiar el color de las barras
                    borderColor: 'rgba(75, 192, 192, 1)',
                    borderWidth: 1
                }]
            };

            // Crear el gráfico con la configuración actualizada
            const ctx = document.getElementById('myChart2').getContext('2d');
            const myChart = new Chart(ctx, config);
        })
        .catch(error => {
            console.error('Error al realizar la solicitud:', error);
        });
});

// Fetch para obtener los datos de las reservas
fetch('../getReservas')
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        // Procesar los datos de las reservas para contar las reservas por franja horaria
        const reservasPorHora = {};
        data.forEach(reserva => {
            const horaInicio = new Date(reserva.hora_inicio).getHours(); // Obtener la hora de inicio de la reserva
            // Incrementar el contador de reservas para esta hora
            reservasPorHora[horaInicio] = (reservasPorHora[horaInicio] || 0) + 1;
        });

        // Convertir los datos procesados en arrays de etiquetas (horas) y valores (número de reservas)
        const horas = Object.keys(reservasPorHora);
        const reservasPorHoraArray = Object.values(reservasPorHora);

        // Crear el gráfico de barras con Chart.js
        new Chart(document.getElementById('graficoReservasPorHora'), {
            type: 'bar',
            data: {
                labels: horas,
                datasets: [{
                    label: 'Número de reservas',
                    data: reservasPorHoraArray,
                    backgroundColor: 'rgba(54, 162, 235, 0.2)', // Color de fondo de las barras
                    borderColor: 'rgba(54, 162, 235, 1)', // Color del borde de las barras
                    borderWidth: 1
                }]
            },
            options: {
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    })
    .catch(error => {
        console.error('Error al realizar la solicitud:', error);
    });
