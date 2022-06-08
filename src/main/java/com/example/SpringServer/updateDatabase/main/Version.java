package com.example.SpringServer.updateDatabase.main;


import com.example.SpringServer.updateDatabase.JDBC.Column;
import com.example.SpringServer.updateDatabase.JDBC.Entity;
import com.example.SpringServer.updateDatabase.JDBC.JDBC;
import com.example.SpringServer.updateDatabase.JDBC.Row;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Version {
    protected Set<Entity> versionEntities = new HashSet<>();
    protected JDBC jdbc = new JDBC();

    public void initPull() {
        versionEntities.forEach(this::addData);
    }

    public void initDelete() {
        versionEntities.forEach(e -> jdbc.dropTable(e.getTableName()));
    }

    public void initPush() {
        jdbc.deleteContent("type"); //TODO händisch
        versionEntities.forEach(e -> jdbc.insertData(e));
    }

    public void addEntity(Entity entity) {
        versionEntities.add(entity);
    }

    private void addData(Entity entity) {
        try {
            List<Row> rows = jdbc.select(entity);
            entity.setRows(rows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Entity getEntityByName(String name) {
        List<Entity> entities = versionEntities.stream().filter(e -> e.getTableName().equals(name)).collect(Collectors.toList());
        if (entities.size() != 1) {
            return null;
        } else {
            return entities.get(0);
        }
    }

    private Column getColumnByName(String entityName, String columnName) {
        Entity e = getEntityByName(entityName);
        if (e != null) {
            return e.findColumnByName(columnName);
        } else {
            return null;
        }
    }

    //change methods
    public void changeEntityName(String oldName, String newName) {
        Entity e = getEntityByName(oldName);
        if (e != null) {
            e.setTableName(newName);
        }
    }
    public void changeColumnName(String entityName, String oldName, String newName) {
        Column c = getColumnByName(entityName, oldName);
        if (c != null) {
            c.setColumnName(newName);
        }
    }
    public void changeColumnType(String entityName, String columnName, int newType) {
        Column c = getColumnByName(entityName, columnName);
        if (c != null) {
            c.setType(newType);
        }
    }
    public void changeColumnDefault(String entityName, String columnName, Object newDefault) {
        Column c = getColumnByName(entityName, columnName);
        if (c != null) {
            c.setDefaultValue(newDefault);
        }
    }
    public void changeAddColumn(String entityName, Column column) {
        Entity e = getEntityByName(entityName);
        if (e != null) {
            e.addColumn(column);
        }
    }
}
