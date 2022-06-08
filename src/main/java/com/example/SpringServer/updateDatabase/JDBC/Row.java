package com.example.SpringServer.updateDatabase.JDBC;

import java.util.HashMap;
import java.util.Map;

public class Row {
    Map<Column, Object> data = new HashMap<>();

    public Map<Column, Object> getData() {
        return data;
    }

    public void setData(Map<Column, Object> data) {
        this.data = data;
    }

    public void addColumn(Column name, Object data) {
        this.data.put(name, data);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("[");
        data.keySet().forEach(d -> System.out.println(d));
        data.keySet().forEach(d ->  result.append(d.getColumnName()).append(": ").append(data.get(d)).append(", "));
        result.deleteCharAt(result.length() - 1);
        result.deleteCharAt(result.length() - 1);
        result.append("]");

        return result.toString();
    }
}
