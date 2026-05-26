const COLORES = ['#3348cb','#e74c3c','#2ecc71','#f39c12','#9b59b6','#1abc9c','#e67e22','#34495e','#c0392b','#16a085'];

new Chart(document.getElementById('chartEspecialidad'), {
    type: 'doughnut',
    data: {
        labels: espLabels,
        datasets: [{ data: espValues, backgroundColor: COLORES }]
    },
    options: { plugins: { legend: { position: 'bottom' } } }
});

new Chart(document.getElementById('chartNivelImparticion'), {
    type: 'bar',
    data: {
        labels: nivLabels,
        datasets: [{
            label: 'Ponentes',
            data: nivValues,
            backgroundColor: '#2ecc71',
            borderRadius: 6
        }]
    },
    options: {
        plugins: { legend: { display: false } },
        scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } }
    }
});

new Chart(document.getElementById('chartTop'), {
    type: 'bar',
    data: {
        labels: topLabels,
        datasets: [{
            label: 'Ponencias impartidas',
            data: topValues,
            backgroundColor: '#3348cb',
            borderRadius: 6
        }]
    },
    options: {
        indexAxis: 'y',
        plugins: { legend: { display: false } },
        scales: { x: { beginAtZero: true, ticks: { stepSize: 1 } } }
    }
});