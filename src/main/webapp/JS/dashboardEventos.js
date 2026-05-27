const COLORES = ['#3348cb','#e74c3c','#2ecc71','#f39c12','#9b59b6','#1abc9c','#e67e22'];

new Chart(document.getElementById('chartEstado'), {
    type: 'bar',
    data: {
        labels: estadoLabels,
        datasets: [{
            label: 'Eventos',
            data: estadoValues,
            backgroundColor: COLORES,
            borderRadius: 6
        }]
    },
    options: {
        plugins: { legend: { display: false } },
        scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } }
    }
});

new Chart(document.getElementById('chartModalidad'), {
    type: 'doughnut',
    data: {
        labels: modalLabels,
        datasets: [{ data: modalValues, backgroundColor: COLORES }]
    },
    options: { plugins: { legend: { position: 'bottom' } } }
});

new Chart(document.getElementById('chartCiudad'), {
    type: 'bar',
    data: {
        labels: ciudadLabels,
        datasets: [{
            label: 'Eventos',
            data: ciudadValues,
            backgroundColor: '#2d97d0',
            borderRadius: 6
        }]
    },
    options: {
        indexAxis: 'y',
        plugins: { legend: { display: false } },
        scales: { x: { beginAtZero: true, ticks: { stepSize: 1 } } }
    }
});