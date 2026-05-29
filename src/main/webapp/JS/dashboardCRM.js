const COLORES = ['#3348cb','#e74c3c','#2ecc71','#f39c12','#9b59b6','#1abc9c','#e67e22','#34495e'];

new Chart(document.getElementById('chartPaginas'), {
    type: 'bar',
    data: {
        labels: paginasLabels,
        datasets: [{ label: 'Páginas', data: paginasValues, backgroundColor: COLORES, borderRadius: 6 }]
    },
    options: {
        plugins: { legend: { display: false } },
        scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } }
    }
});