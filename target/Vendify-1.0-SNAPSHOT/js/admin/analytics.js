async function getParkings() {
            try {
                const response = await fetch('../getParkings');  // Cambia '/getParkings' por la URL correcta de tu servlet
                const data = await response.json();
                return data;
            } catch (error) {
                console.error('Error obteniendo los datos:', error);
                return [];
            }
}

 async function createChart() {
            const parkings = await getParkings();

            const nombres = parkings.map(parking => parking.nombre);
            const capacidadTotal = parkings.map(parking => parking.capacidad_total);
            const plazasDisponibles = parkings.map(parking => parking.plazas_disponibles);

            const ctx = document.getElementById('parkingChart').getContext('2d');
            const parkingChart = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: nombres,
                    datasets: [
                        {
                            label: 'Capacidad Total',
                            data: capacidadTotal,
                            backgroundColor: 'rgba(75, 192, 192, 0.2)',
                            borderColor: 'rgba(75, 192, 192, 1)',
                            borderWidth: 1
                        },
                        {
                            label: 'Plazas Disponibles',
                            data: plazasDisponibles,
                            backgroundColor: 'rgba(255, 99, 132, 0.2)',
                            borderColor: 'rgba(255, 99, 132, 1)',
                            borderWidth: 1
                        }
                    ]
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: true,
                            ticks: {
                                color: 'white'  // Color de las etiquetas del eje Y
                            }
                        },
                        x: {
                            ticks: {
                                color: 'white'  // Color de las etiquetas del eje X
                            }
                        }
                    },
                    plugins: {
                        legend: {
                            labels: {
                                color: 'white'  // Color de las etiquetas de la leyenda
                            }
                        }
                    }
                }
            });
        }

        // Llamar a la función para crear el gráfico al cargar la página
        window.onload = createChart;