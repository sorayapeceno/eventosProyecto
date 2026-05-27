const COLORES = ['#3348cb','#e74c3c','#2ecc71','#f39c12','#9b59b6','#1abc9c','#e67e22','#34495e'];

new Chart(document.getElementById('chartOrgTipo'), {
    type: 'doughnut',
    data: { labels: orgTipoLabels, datasets: [{ data: orgTipoValues, backgroundColor: COLORES }] },
    options: { plugins: { legend: { position: 'bottom' } } }
});

new Chart(document.getElementById('chartProdCat'), {
    type: 'bar',
    data: {
        labels: prodCatLabels,
        datasets: [{ label: 'Productos', data: prodCatValues, backgroundColor: '#9b59b6', borderRadius: 6 }]
    },
    options: { plugins: { legend: { display: false } }, scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } } }
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

new Chart(document.getElementById('chartRol'), {
    type: 'doughnut',
    data: { labels: rolLabels, datasets: [{ data: rolValues, backgroundColor: COLORES }] },
    options: { plugins: { legend: { position: 'bottom' } } }
});

new Chart(document.getElementById('chartPaginas'), {
    type: 'bar',
    data: {
        labels: paginasLabels,
        datasets: [{ label: 'Páginas', data: paginasValues, backgroundColor: '#2d97d0', borderRadius: 6 }]
    },
    options: { plugins: { legend: { display: false } }, scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } } }
});