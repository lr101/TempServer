/**
 * logic
 */

function dropdown(id) {
    if (id) {
        const ajax = new XMLHttpRequest();
        ajax.open("GET", "/rest/v1/sensors/" + id, true);
        ajax.send(null);
        ajax.onreadystatechange = function () {
            if (ajax.readyState === 4) {
                let values = JSON.parse(ajax.responseText);
                if (values) {
                    document.getElementById("config_name").innerHTML = values.sensorNick;
                    document.getElementById("nickname").value = values.sensorNick;
                    document.getElementById("id").value = values.sensorId;
                    changeSelectedIndex("selectType", values.sensorType);
                    changeSelectedIndexCategory("selectCategory", values.categories);
                }
            }
        };
        checkActive(id);
    }
}

function changeSelectedIndexCategory(docId, sensorTypeOrCategory) {
    let values = [];
    for (let y = 0; y < sensorTypeOrCategory.length; y++) {
        values.push(sensorTypeOrCategory[y].id + "");
    }
    $('#' + docId).selectpicker('val', values);
}

function changeSelectedIndex(docId, sensorTypeOrCategory) {
    let nodes = document.getElementById(docId).children;
    if (sensorTypeOrCategory) {
        for (let i = 0; i < nodes.length; i++) {
            let value = nodes[i].getAttribute("value");
            if (value && value.toString() === sensorTypeOrCategory.id.toString()) {
                document.getElementById(docId).selectedIndex = i.toString();
            }
        }
    }

    $('#'+docId).trigger('change');
}

function insertIndex(docId, values) {
    let select = document.getElementById(docId);
    for(let i = 0; i < values.length; i++) {
        let node = document.createElement("option");
        node.setAttribute("value", values[i].id);
        if (docId === "selectType") {
            node.innerHTML = values[i].typeName;
        } else {
            node.innerHTML = values[i].name;
        }
        select.appendChild(node);
    }
    $('#'+docId).trigger('change');
}

function updateSensorTypes() {
    const ajax = new XMLHttpRequest();
    ajax.open("GET", "/rest/v1/types", true);
    ajax.send(null);
    ajax.onreadystatechange = function() {
        if (ajax.readyState === 4) {
            let values = JSON.parse(ajax.responseText);
            insertIndex("selectType", values)
        }
    };
}

function updateSensorCategories() {
    const ajax = new XMLHttpRequest();
    ajax.open("GET", "/rest/v1/categories", true);
    ajax.send(null);
    ajax.onreadystatechange = function() {
        if (ajax.readyState === 4) {
            let values = JSON.parse(ajax.responseText);
            insertIndex("selectCategory", values)
        }
    };
}



function submitData() {
    const sensor_id = document.getElementById("id").value;
    const select = document.getElementById("selectType");
    const category = document.getElementById("selectCategory");
    const sensor_type_id = select.options[select.selectedIndex].value;
    const sensor_nick = document.getElementById("nickname").value;
    if (sensor_nick.length > 0 && sensor_nick.length < 17 && sensor_type_id !== "" && sensor_id !== "") {
        const ajax = new XMLHttpRequest();
        ajax.open("GET", "/rest/v1/types/" + sensor_type_id, true);
        ajax.send(null);
        ajax.onreadystatechange = function() {
            if (ajax.readyState === 4) {
                let sensorType = JSON.parse(ajax.responseText).typeName;
                let categories = $(category).find("option:selected");
                let sensorCategory = [];
                for (let i = 0; i < categories.length; i++) {
                    let option = {}
                    option["id"] = categories[i].value;
                    option["sensorCategory"] = $(categories[i]).text();
                    sensorCategory.push(option);
                }
                ajax.open("PUT", "/rest/v1/sensors/" + sensor_id, true);
                ajax.setRequestHeader("Content-Type", "application/json");
                let json = {sensorNick: sensor_nick, sensorType: sensorType, categories:sensorCategory}
                ajax.send(JSON.stringify(json));
                ajax.onreadystatechange = function () {
                    if (ajax.readyState === 4) {
                        if (ajax.status === 200) {
                            document.getElementById("list_" + sensor_id).innerHTML = sensor_nick;
                            document.getElementById("dropdown_" + sensor_id).value = sensor_nick;

                        } else {
                            alert(ajax.responseText);
                        }
                    }
                };
            }
        }
    }
}

function checkActive(id) {
    let date = new Date(new Date().getTime());
    date.setMinutes(date.getMinutes() - 10);
    date = date.toISOString();
    const ajax = new XMLHttpRequest();
    ajax.open("GET", "/rest/v1/sensors/" + id + "/entry?date2=" + date + "&limit=1" , true);
    ajax.send(null);
    ajax.onreadystatechange = function() {
        if (ajax.readyState === 4) {
            document.getElementById("active_content").style.color = "white";
            let list = JSON.parse(ajax.responseText);
            if (list.length === 0) {
                document.getElementById("active").innerHTML = "INACTIVE";
                document.getElementById("temp").innerHTML = "";
                document.getElementById("active_content").style.backgroundColor = "rgba(223,26,56,1)";
            } else {
                document.getElementById("active").innerHTML = "ACTIVE";
                document.getElementById("temp").innerHTML = "current: " + list[list.length - 1].value;
                document.getElementById("active_content").style.backgroundColor = "#35fb74";
            }
        }
    };
}

document.getElementById("submit").addEventListener("click", function () {
    submitData();
});

document.getElementById("delete").addEventListener("click", function () {
    const id = document.getElementById("id").value;
    if (id !== "") {
        const ajax = new XMLHttpRequest();
        ajax.open("DELETE", "/rest/v1/sensors/" + id, true);
        ajax.send(null);
        ajax.onreadystatechange = function () {
            if (ajax.readyState === 4) {
                if (ajax.status === 200 || ajax.status === 201) {
                    document.getElementById("list").removeChild(document.getElementById("list_" + id));
                    document.getElementById("dropdown").removeChild(document.getElementById("dropdown_" + id));
                } else {
                    alert(ajax.responseText);
                }
            }
        }
    }
});

window.addEventListener('load', () => {
    let now = new Date();
    now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
    let currentDate = now.toISOString().substring(0,10);
    let currentTime = now.toISOString().substring(11,16);
    document.getElementById('date1_date').value = currentDate;
    document.getElementById("date1_time").value = currentTime;
});

function getTime(elementID) {
    let date = new Date(document.getElementById(elementID + "_date").valueAsDate);
    let time  = document.getElementById( elementID+ "_time").value;
    date.setMinutes(time.split(':')[1]);
    date.setHours(time.split(':')[0]);
    return date;
}

function alterTempData (method) {
    const id = document.getElementById("id").value;
    if (id !== "") {
        let date1 = getTime("date1");
        let date2 = getTime("date2");
        date1.setMinutes(date1.getMinutes());
        date2.setMinutes(date2.getMinutes());
        if (date1 > date2) {
            const ajax = new XMLHttpRequest();
            ajax.open(method, "/rest/v1/sensors/" + id + "/entry?date1=" + date1.toISOString() + "&date2=" + date2.toISOString(), true);
            ajax.setRequestHeader("Content-Type", "text/html");
            ajax.send(null);
            ajax.onreadystatechange = function () {
                if (ajax.readyState === 4 && ajax.status === 200 && method === "GET") {
                    let items = JSON.parse(ajax.responseText);
                    exportCSVFile(Object.keys(items), items, "export_" + id);
                }
            };
        }
    } else {
        alert("Select Sensor first!")
    }
}


document.getElementById("getTempData").addEventListener("click", function () {
    alterTempData("GET");
});

document.getElementById("deleteTempData").addEventListener("click", function () {
    if (confirm("Are you sure to delete?")) {
        alterTempData("DELETE");
    }
});

function convertToCSV(array) {
    let str = '';

    for (let i = 0; i < array.length; i++) {
        let line = '';
        for (let index in array[i]) {
            if (line !== '') line += ','
            line += array[i][index];
        }
        str += line + '\r\n';
    }
    return str;
}

function exportCSVFile(headers, jsonObject, fileTitle) {
    if (jsonObject.length !== 0) {
        let csv = this.convertToCSV(jsonObject);

        let exportedFilename = fileTitle + '.csv' || 'export.csv';

        let blob = new Blob([csv], {type: 'text/csv;charset=utf-8;'});
        if (navigator.msSaveBlob) { // IE 10+
            navigator.msSaveBlob(blob, exportedFilename);
        } else {
            let link = document.createElement("a");
            if (link.download !== undefined) { // feature detection
                // Browsers that support HTML5 download attribute
                let url = URL.createObjectURL(blob);
                link.setAttribute("href", url);
                link.setAttribute("download", exportedFilename);
                link.style.visibility = 'hidden';
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
            }
        }
    } else {
        alert("No Data available");
    }
}



$('#selectType').on('change', function() {
    $('#selectType').selectpicker('refresh');
});

$('#selectCategory').on('change', function() {
    $('#selectCategory').selectpicker('refresh');
});

updateSensorCategories();
updateSensorTypes();