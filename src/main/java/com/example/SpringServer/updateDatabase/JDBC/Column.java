package com.example.SpringServer.updateDatabase.JDBC;

public class Column {

    private String columnName;
    private int type;
    private Object defaultValue;

    public Column (String columnName, int type, Object defaultValue) {
        this.columnName = columnName;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    public int getType() {
        return type;
    }
    public String getColumnName() {
        return columnName;
    }
    public Object getDefaultValue() {
        return defaultValue;
    }
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

}
