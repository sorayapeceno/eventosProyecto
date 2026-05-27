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