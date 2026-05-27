const COLORES = ['#3348cb','#e74c3c','#2ecc71','#f39c12','#9b59b6','#1abc9c','#e67e22','#34495e'];

new Chart(document.getElementById('chartMetodoPago'), {
    type: 'doughnut',
    data: { labels: metodoPagoLabels, datasets: [{ data: metodoPagoValues, backgroundColor: COLORES }] },
    options: { plugins: { legend: { position: 'bottom' } } }
});

new Chart(document.getElementById('chartNivel'), {
    type: 'bar',
    data: {
        labels: nivelLabels,
        datasets: [{ label: 'Asistentes', data: nivelValues, backgroundColor: '#3348cb', borderRadius: 6 }]
    },
    options: { plugins: { legend: { display: false } }, scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } } }
});

new Chart(document.getElementById('chartTematica'), {
    type: 'bar',
    data: {
        labels: tematicaLabels,
        datasets: [{ label: 'Asistentes', data: tematicaValues, backgroundColor: '#9b59b6', borderRadius: 6 }]
    },
    options: {
        indexAxis: 'y',
        plugins: { legend: { display: false } },
        scales: { x: { beginAtZero: true, ticks: { stepSize: 1 } } }
    }
});

new Chart(document.getElementById('chartStock'), {
    type: 'bar',
    data: {
        labels: stockLabels,
        datasets: [{ label: 'Stock', data: stockValues, backgroundColor: '#2ecc71', borderRadius: 6 }]
    },
    options: {
        indexAxis: 'y',
        plugins: { legend: { display: false } },
        scales: { x: { beginAtZero: true } }
    }
});

new Chart(document.getElementById('chartEntradas'), {
    type: 'doughnut',
    data: { labels: entradasLabels, datasets: [{ data: entradasValues, backgroundColor: COLORES }] },
    options: { plugins: { legend: { position: 'bottom' } } }
});