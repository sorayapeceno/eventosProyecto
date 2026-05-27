const COLORES = ['#3348cb','#e74c3c','#2ecc71','#f39c12','#9b59b6','#1abc9c','#e67e22','#34495e'];

new Chart(document.getElementById('chartEstado'), {
    type: 'doughnut',
    data: { labels: estadoLabels, datasets: [{ data: estadoValues, backgroundColor: COLORES }] },
    options: { plugins: { legend: { position: 'bottom' } } }
});

new Chart(document.getElementById('chartPatrocinio'), {
    type: 'bar',
    data: { labels: patrocinioLabels, datasets: [{ label: 'Patrocinios', data: patrocinioValues, backgroundColor: ['#cd7f32','#aaa9ad','#ffd700'], borderRadius: 6 }] },
    options: { plugins: { legend: { display: false } }, scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } } }
});

new Chart(document.getElementById('chartColaboracion'), {
    type: 'bar',
    data: { labels: colaboracionLabels, datasets: [{ label: 'Colaboraciones', data: colaboracionValues, backgroundColor: '#2d97d0', borderRadius: 6 }] },
    options: { plugins: { legend: { display: false } }, scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } } }
});

new Chart(document.getElementById('chartCiudad'), {
    type: 'bar',
    data: { labels: ciudadLabels, datasets: [{ label: 'Organizaciones', data: ciudadValues, backgroundColor: '#9b59b6', borderRadius: 6 }] },
    options: { indexAxis: 'y', plugins: { legend: { display: false } }, scales: { x: { beginAtZero: true, ticks: { stepSize: 1 } } } }
});

new Chart(document.getElementById('chartRecinto'), {
    type: 'bar',
    data: { labels: recintoLabels, datasets: [{ label: 'Capacidad', data: recintoValues, backgroundColor: '#2ecc71', borderRadius: 6 }] },
    options: { plugins: { legend: { display: false } }, scales: { y: { beginAtZero: true } } }
});