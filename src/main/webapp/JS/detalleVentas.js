const COLORES = ['#3348cb','#e74c3c','#2ecc71','#f39c12','#9b59b6','#1abc9c','#e67e22','#34495e'];

new Chart(document.getElementById('chartMetodoPago'), {
    type: 'doughnut',
    data: { labels: metodoPagoLabels, datasets: [{ data: metodoPagoValues, backgroundColor: COLORES }] },
    options: { plugins: { legend: { position: 'bottom' } } }
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