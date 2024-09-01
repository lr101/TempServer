/**
 * logic
 */

function select(sensor_type, sensor_type_id, sensor_type_unit) {
    const editElement = document.getElementById("typeName");
    editElement.value = sensor_type;
    editElement.setAttribute("data-id", sensor_type_id);
    const editElement2 = document.getElementById("typeUnit");
    editElement.setAttribute("data-id", sensor_type_id);
    editElement2.value = sensor_type_unit;
}


function submitData() {
    const sensor_type = document.getElementById("typeName").value;
    const sensor_type_id = document.getElementById("typeName").getAttribute("data-id");
    const sensor_type_unit = document.getElementById("typeUnit").value;
    const ajax = new XMLHttpRequest();

    let json;
    if (sensor_type_id !== "" ) {
        ajax.open("PUT", "/rest/v1/types/" + sensor_type_id, true);
        json = {typeName : sensor_type, id :sensor_type_id, description: sensor_type_unit}
    } else if (sensor_type_id === "") {
        ajax.open("POST", "/rest/v1/types", true);
        json = {typeName : sensor_type, description: sensor_type_unit}
    }
    if (json !== undefined && sensor_type.length > 0) {
        ajax.setRequestHeader("Content-Type", "application/json");
        ajax.send(JSON.stringify(json));
        ajax.onreadystatechange = function () {
            if (ajax.readyState === 4) {
                if (ajax.status === 200 || ajax.status === 201) {
                    updateTypes();

                } else {
                    alert(ajax.responseText);
                }
            }
        };
    }
}

function updateTypes() {
    const ajax = new XMLHttpRequest();
    ajax.open("GET", "/rest/v1/types", true);
    ajax.send();
    ajax.onreadystatechange = function() {
        if (ajax.readyState === 4) {
            if(ajax.status === 200) {
                const listElement = document.getElementById("list");
                listElement.textContent = '';
                const data = JSON.parse(ajax.responseText);
                for (let i = 0; i < data.length + 1; i++) {
                    let btn = document.createElement("button");
                    btn.classList.add('list-group-item','list-group-item-action');
                    if (i === data.length) btn.classList.add('active');
                    btn.id = "list_" + (i === data.length ? "new" : data[i].id);
                    btn.setAttribute("type", "button");
                    btn.setAttribute("data-toggle", "list");
                    btn.setAttribute("onclick", (i === data.length ? "select('','','')" : "select('" + data[i].typeName + "','" + data[i].id + "','" + data[i].description + "')"));
                    btn.innerHTML = (i === data.length ? "+" : data[i].typeName);
                    listElement.appendChild(btn);
                }
                document.getElementById("typeName").value = "";
                document.getElementById("typeUnit").value = "";
                document.getElementById("typeName").setAttribute("data-id", "");
            } else {
                alert(ajax.responseText);
            }
        }
    };
}

document.getElementById("submit").addEventListener("click", function () {
    submitData();
});

document.getElementById("delete").addEventListener("click", function () {
    const sensor_type_id = document.getElementById("typeName").getAttribute("data-id");
    const ajax = new XMLHttpRequest();
    ajax.open("DELETE", "/rest/v1/types" + sensor_type_id, true);
    ajax.send();
    ajax.onreadystatechange = function () {
        if (ajax.readyState === 4) {
            if (ajax.status === 200) {
                updateTypes();
            } else {
                alert(ajax.responseText);
            }
        }
    }
});