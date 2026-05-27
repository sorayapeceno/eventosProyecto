const COLORES = ['#3348cb','#e74c3c','#2ecc71','#f39c12','#9b59b6','#1abc9c','#e67e22','#34495e'];

new Chart(document.getElementById('chartProvEstado'), {
    type: 'doughnut',
    data: { labels: provEstadoLabels, datasets: [{ data: provEstadoValues, backgroundColor: COLORES }] },
    options: { plugins: { legend: { position: 'bottom' } } }
});

new Chart(document.getElementById('chartPedEstado'), {
    type: 'doughnut',
    data: { labels: pedEstadoLabels, datasets: [{ data: pedEstadoValues, backgroundColor: COLORES }] },
    options: { plugins: { legend: { position: 'bottom' } } }
});

new Chart(document.getElementById('chartCategoria'), {
    type: 'bar',
    data: { labels: catLabels, datasets: [{ label: 'Mercancías', data: catValues, backgroundColor: '#2d97d0', borderRadius: 6 }] },
    options: { plugins: { legend: { display: false } }, scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } } }
});

new Chart(document.getElementById('chartStock'), {
    type: 'bar',
    data: { labels: stockLabels, datasets: [{ label: 'Stock', data: stockValues, backgroundColor: '#2ecc71', borderRadius: 6 }] },
    options: { indexAxis: 'y', plugins: { legend: { display: false } }, scales: { x: { beginAtZero: true } } }
});

new Chart(document.getElementById('chartAlbaran'), {
    type: 'doughnut',
    data: { labels: albaranLabels, datasets: [{ data: albaranValues, backgroundColor: COLORES }] },
    options: { plugins: { legend: { position: 'bottom' } } }
});

new Chart(document.getElementById('chartProveedor'), {
    type: 'bar',
    data: { labels: proveedorLabels, datasets: [{ label: 'Pedidos', data: proveedorValues, backgroundColor: '#9b59b6', borderRadius: 6 }] },
    options: { plugins: { legend: { display: false } }, scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } } }
});