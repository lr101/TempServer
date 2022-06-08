package com.example.SpringServer.updateDatabase.JDBC;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Entity {

    protected String tableName;
    protected List<Column> columns = new ArrayList<>();
    protected List<Row> rows = new ArrayList<>();


    public Entity(String name, List<Column> columns) {
        this.tableName = name;
        this.columns.addAll(columns);
    }

    public String getTableName() {
        return tableName;
    }
    
    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public List<Row> getRows() {
        return rows;
    }

    public Column findColumnByName(String name){
        List<Column> c = columns.stream().filter(e -> e.getColumnName().equals(name)).collect(Collectors.toList());
        if (c.size() == 1) {
            return c.get(0);
        } else {
            return null;
        }
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void addColumn(Column column) {
        if (findColumnByName(column.getColumnName()) == null) {
            columns.add(column);
            rows.forEach(e -> e.getData().put(column, column.getDefaultValue()));
        }
    }

}
