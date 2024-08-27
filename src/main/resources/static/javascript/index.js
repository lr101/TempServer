let length = 0;
let list = [];
function getData() {

    let date = new Date(new Date().getTime());
    date.setMinutes(date.getMinutes() - 10);
    date = date.toISOString();
    for (let i = 0; i < length; i++) {

        let id = document.getElementById("sensor_" + i).getAttribute("data-id");
        const ajax = new XMLHttpRequest();
        ajax.open("GET", "/rest/v1/sensors/" + id + "/entry?date2=" + date + "&limit=1" , true);
        ajax.send(null);
        ajax.onreadystatechange = function() {
            if (ajax.readyState === 4) {
                let list = JSON.parse(ajax.responseText);
                if (list.length === 0) {
                    document.getElementById("value_" + i).innerHTML = "No Data";
                } else {
                    let unit = document.getElementById("value_" + i).getAttribute("data-unit");
                    document.getElementById("value_" + i).innerHTML = list[list.length - 1].value + unit;
                }
            }
        };
    }
}

function setListeners(classSelector) {
    let sensorPlaceDOM = document.querySelectorAll(classSelector);
    for (let i = 0; i < sensorPlaceDOM.length; i++) {
        const element = sensorPlaceDOM[i];
        let id = element.firstElementChild.firstElementChild.getAttribute("data-id");
        element.firstElementChild.lastElementChild.setAttribute("value", id);
        element.action = id === "config" ? "/config" : "/display?sensor_id=" + id;
        element.addEventListener("click", function () {
            element.submit();
        });
    }
}

function getSensors() {
    const ajax = new XMLHttpRequest();
    ajax.open("GET", "/rest/v1/sensors" , true);
    ajax.send(null);
    ajax.onreadystatechange = function() {
        if (ajax.readyState === 4) {
            list = JSON.parse(ajax.responseText);
        }
    }
}

function filterByCategory() {
    let parent = document.getElementById("fieldsetParent");
    while (parent.firstChild) {
        parent.removeChild(parent.lastChild);
    }
    let categories = {};
    let len = 0;
    for (let i = 0; i < list.length; i++) {
        let c = list[i].categories;
        for (let y = 0; y < c.length; y++) {
            let category = c[y].id;
            if (categories[category] === undefined) {
                categories[category] = [createFieldset(parent, list[i].categories[y].sensorCategory), 0];
            }
            if (categories[category][1] % 3 === 0) {
                createRow(categories[category][0]);
            }
            createSensorDOM(categories[category][0].lastChild, list[i], len, categories[category][1] % 3)
            categories[category][1]++;
            len++;
        }
    }
    length = len;
}


function filterByType() {
    let parent = document.getElementById("fieldsetParent");
    while (parent.firstChild) {
        parent.removeChild(parent.lastChild);
    }
    let types = {};
    for (let i = 0; i < list.length; i++) {
        let typeId = list[i].sensorType.id;
        if (types[typeId] === undefined) {
            types[typeId] = [createFieldset(parent, list[i].sensorType.sensorType), 0];
        }
        if (types[typeId][1] % 3 === 0) {
            createRow(types[typeId][0]);
        }
        createSensorDOM(types[typeId][0].lastChild, list[i], i, types[typeId][1] % 3)
        types[typeId][1]++;
    }
    length = list.length;
}

function filterAll() {
    let parent = document.getElementById("fieldsetParent");
    while (parent.firstChild) {
        parent.removeChild(parent.lastChild);
    }
    let fieldset = createFieldset(parent, "All");
    for (let i = 0; i < list.length; i++) {
        if (i % 3 === 0) {
            createRow(fieldset);
        }
        createSensorDOM(fieldset.lastChild, list[i], i, i % 3)
    }
    length = list.length;

}

function createFieldset(parent,legend){
    let fieldsetDOM = document.createElement("fieldset");
    fieldsetDOM.classList.add("category");
    let legendDOM = document.createElement("legend");
    legendDOM.classList.add("category-legend");
    legendDOM.innerHTML = legend;
    fieldsetDOM.appendChild(legendDOM);
    parent.appendChild(fieldsetDOM);
    return fieldsetDOM;
}

function createRow (parent) {
    let divDOM = document.createElement("div");
    divDOM.classList.add("row");
    divDOM.classList.add("sensor-row");
    parent.appendChild(divDOM);
    return divDOM;
}

function createSensorDOM(parent, sensor, num, numRow) {
    let formDOM = document.createElement("form");
    let divDOM = document.createElement("div");
    let p1DOM = document.createElement("p");
    let p2DOM = document.createElement("p");
    let inputDOM = document.createElement("input");
    formDOM.classList.add("col-4");
    formDOM.classList.add("text-center");
    formDOM.classList.add("c-" + numRow);
    formDOM.setAttribute("method", "get");
    formDOM.id = "section_" + num;
    divDOM.classList.add("ib");
    p1DOM.classList.add("data");
    p2DOM.classList.add("data-value");
    p1DOM.setAttribute("data-id", sensor.sensorId);
    p2DOM.setAttribute("data-unit", sensor.sensorType.unit);
    p1DOM.setAttribute("id", "sensor_" + num);
    p2DOM.setAttribute("id", "value_" + num);
    p1DOM.innerHTML =  sensor.sensorNick + ":";
    p2DOM.innerHTML ="LOADING";
    inputDOM.setAttribute("name","sensor_id");
    inputDOM.hidden = true;
    divDOM.appendChild(p1DOM);
    divDOM.appendChild(p2DOM);
    divDOM.appendChild(inputDOM);
    formDOM.appendChild(divDOM);
    parent.appendChild(formDOM);
}


function filterListener(id, filterType) {
    let element = document.getElementById(id);
    element.addEventListener("click", function () {
        if (filterType === "type") {
            filterByType();
        } else if (filterType === "category") {
            filterByCategory();
        } else {
            filterAll();
        }
        setListeners(".col-4")
        getData();
    });
}

/**
 * on startup
 */
$(function() {

    getSensors();
    length = document.getElementById("main").getAttribute("data-length");
    setListeners(".col-4")
    getData();
    setInterval(getData, 20000);
    filterListener("sort-type","type");
    filterListener("sort-category","category");
    filterListener("sort-all","all");
});