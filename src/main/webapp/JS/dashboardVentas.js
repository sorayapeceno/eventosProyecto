const COLORES = ['#3348cb','#e74c3c','#2ecc71','#f39c12','#9b59b6','#1abc9c','#e67e22'];

new Chart(document.getElementById('chartCiudad'), {
    type: 'bar',
    data: {
        labels: ciudadLabels,
        datasets: [{
            label: 'Participantes',
            data: ciudadValues,
            backgroundColor: COLORES,
            borderRadius: 6
        }]
    },
    options: {
        indexAxis: 'y',
        plugins: { legend: { display: false } },
        scales: { x: { beginAtZero: true, ticks: { stepSize: 1 } } }
    }
});