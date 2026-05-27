const COLORES = ['#3348cb','#e74c3c','#2ecc71','#f39c12','#9b59b6','#1abc9c','#e67e22','#34495e'];

new Chart(document.getElementById('chartPedEstado'), {
    type: 'doughnut',
    data: { labels: pedEstadoLabels, datasets: [{ data: pedEstadoValues, backgroundColor: COLORES }] },
    options: { plugins: { legend: { position: 'bottom' } } }
});

new Chart(document.getElementById('chartAlbaran'), {
    type: 'doughnut',
    data: { labels: albaranLabels, datasets: [{ data: albaranValues, backgroundColor: COLORES }] },
    options: { plugins: { legend: { position: 'bottom' } } }
});