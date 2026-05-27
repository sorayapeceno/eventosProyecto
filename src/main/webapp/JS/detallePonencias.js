const COLORES = ['#3348cb','#e74c3c','#2ecc71','#f39c12','#9b59b6','#1abc9c','#e67e22'];

new Chart(document.getElementById('chartTipo'), {
    type: 'bar',
    data: {
        labels: tipoLabels,
        datasets: [{ label: 'Ponencias', data: tipoValues, backgroundColor: COLORES, borderRadius: 6 }]
    },
    options: { plugins: { legend: { display: false } }, scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } } }
});

new Chart(document.getElementById('chartNivel'), {
    type: 'doughnut',
    data: { labels: nivelLabels, datasets: [{ data: nivelValues, backgroundColor: COLORES }] },
    options: { plugins: { legend: { position: 'bottom' } } }
});