let chart;
const TIME_ZONE_OFFSET = -60;

const id = document.getElementById("title").getAttribute("data-id");

function chartIt(values, dates) {
    let ctx = document.getElementById('myChart').getContext('2d');

    chart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: dates,
            datasets: [{
                label: 'Values in ' + document.getElementById("current").getAttribute("data-unit") + ' in the given time interval',
                data: values,
                pointRadius: 1,
                borderWidth: 1,
                lineWidth: 2,
                borderColor: 'rgba(240, 40, 40, 1)',
                fill: false
            }]
        },
        options: {

            transition: {
                duration: 1000,
                easing: 'easeInOutElastic'
            },
            responsive: true,
            resizeDelay: 100,
            maintainAspectRatio: false,
            scales: {
                yAxes: [{
                    scaleLabel: {
                        display: true,
                        labelString: 'Value in the given unit'
                    },
                    gridLines: {
                        color: 'rgba(0, 0, 0, 0.5)'
                    },
                    ticks: {
                        fontColor: "black",
                        autoSkip: true,
                    }
                }],
                xAxes: [{
                    showXLabels: 10,
                    scaleLabel: {
                        display: true,
                        labelString: 'Time in hours and minutes'
                    },
                    gridLines: {
                        color: 'rgba(0, 0, 0, 0.5)'
                    },
                    ticks: {
                        fontColor: "black",
                        autoSkip: true,
                        maxTicksLimit: 20
                    },

                    type: 'time',
                    distribution: 'linear',
                    time: {
                        unit: 'day', //TODO test if works
                        tooltipFormat: 'HH:mm'
                    },

                }]
            }
        }
    });
    chart.resize();
}

function getTimezone() {
    return (new Date(new Date().getTime() + 2 * 3600 * 1000).getTimezoneOffset() + TIME_ZONE_OFFSET);
}

function getDataGraph(offset, date1, date2) {
    if (offset  === undefined) offset = 60;
    if (date1 === undefined)  date1 = getCurrentDate(0);
    if (date2 === undefined)  date2 = getCurrentDate(offset);
    const ajax = new XMLHttpRequest();
    ajax.open("GET", `/rest/v1/sensors/${id}/entry?sensor_id=` + id + "&date1=" + date1 + "&date2=" + date2 + "&interval=" + (offset/2) , true);
    ajax.send(null);
    ajax.onreadystatechange = function () {
        if (ajax.readyState === 4) {
            let values = JSON.parse(ajax.responseText);
            let dates = values.map(value => (new Date(value.timestamp)).toLocaleTimeString().substring(0,5));
            chart.data.datasets[0].data = values.map(value => value.value);
            chart.data.labels = dates;
            chart.update();
        }
    };
}

function getCurrentDate(offset) {
    let date = new Date(new Date().getTime());
    date.setMinutes(date.getMinutes() - offset);
    return date.toISOString();
}


function getDataCurrent() {
    const ajax = new XMLHttpRequest();
    ajax.open("GET", "/rest/v1/sensors/" + id + "/entry?date2=" + getCurrentDate(10) + "&limit=1" , true);
    ajax.send(null);
    ajax.onreadystatechange = function() {
        if (ajax.readyState === 4) {
            let list = JSON.parse(ajax.responseText);
            if (list.length === 0) {
                document.getElementById("current").innerHTML = "No Data available";
            } else {
                let unit = document.getElementById("current").getAttribute("data-unit");
                document.getElementById("current").innerHTML = "Current Value: " + list[list.length - 1].value + unit;
            }
        }
    };
}

$(function () {
    chartIt([], []);
    getDataCurrent();
    getDataGraph(720);
    setInterval(getDataCurrent, 20000);
});