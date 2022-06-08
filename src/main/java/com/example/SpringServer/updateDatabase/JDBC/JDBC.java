package com.example.SpringServer.updateDatabase.JDBC;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JDBC {

    private static final String UPDATE_SQL = "INSERT INTO $ (*) VALUES (#)";
    private static final String SELECT_SQL = "SELECT * FROM $";
    private static final String DROP_TABLE_SQL = "DROP TABLE $ CASCADE";
    private static final String DELETE_SQL = "DELETE FROM $";
    private Connection conn;
    public JDBC()  {

        try {
            Class.forName("org.postgresql.Driver");
            Properties props = new Properties();
            props.put("user", System.getenv("DB_USER"));
            props.put("password", System.getenv("DB_PASSWORD"));

            conn = DriverManager.getConnection(System.getenv("DATASOURCE_URL") , props);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void insertData(Entity entity) {
        try {
            PreparedStatement stmt = conn.prepareStatement(this.createInsertStatement(UPDATE_SQL, entity));
            entity.getRows().forEach(e -> addToBatch(stmt, e, entity.getColumns()));
            stmt.executeBatch();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private void addToBatch(PreparedStatement stmt, Row row, List<Column> columns) {
        try {
            this.prepareStatement(stmt, row, columns);
            stmt.addBatch();
            stmt.clearParameters();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void prepareStatement(PreparedStatement stmt, Row row, List<Column> columns) {
        int[] idx = { 1 };
        columns.forEach(e -> {
            try {
                this.prepare(stmt, row.getData().get(e), idx[0]++, e);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void prepare(PreparedStatement stmt, Object obj, int count, Column column) throws SQLException {
        switch (column.getType()) {
            case Types.VARCHAR -> stmt.setString(count, castString(obj,column.getDefaultValue()));
            case Types.INTEGER -> stmt.setInt(count, castInt(obj, column.getDefaultValue()));
            case Types.DOUBLE -> stmt.setDouble(count, castDouble(obj, column.getDefaultValue()));
            case Types.BIGINT -> stmt.setLong(count, castLong(obj, column.getDefaultValue()));
            case Types.TIMESTAMP_WITH_TIMEZONE -> stmt.setTimestamp(count, (Timestamp) obj);
        }
    }

    private String castString(Object obj, Object defaultValue) {
        if (obj != null) {
            return obj.toString();
        } else {
            return defaultValue.toString();
        }
    }

    private Integer castInt(Object obj, Object defaultValue) {
        if (obj instanceof Integer) {
            return (Integer) obj;
        } else if (obj instanceof Double) {
            return ((Double) obj).intValue();
        } else if (obj instanceof Long) {
            return ((Long) obj).intValue();
        } else if (obj instanceof String) {
            return Integer.parseInt((String) obj);
        } else {
            return (Integer) defaultValue;
        }
    }

    private Double castDouble(Object obj, Object defaultValue) {
        if (obj instanceof Integer) {
            return ((Integer) obj).doubleValue();
        } else if (obj instanceof Double) {
            return (Double) obj;
        } else if (obj instanceof Long) {
            return ((Long) obj).doubleValue();
        } else if (obj instanceof String) {
            return Double.parseDouble((String) obj);
        } else {
            return (Double) defaultValue;
        }
    }

    private Long castLong(Object obj, Object defaultValue) {
        if (obj instanceof Integer) {
            return ((Integer) obj).longValue();
        } else if (obj instanceof Double) {
            return ((Double) obj).longValue();
        } else if (obj instanceof Long) {
            return (Long) obj;
        } else if (obj instanceof String) {
            return Long.parseLong((String) obj);
        } else {
            return (Long) defaultValue;
        }
    }

    private void update(String sqlQuery, Row row, String tableName, List<Column> columns) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(this.setTableName(sqlQuery, tableName));
        this.prepareStatement(stmt, row, columns);
        stmt.executeUpdate();
        stmt.close();
    }

    public void dropTable(String tableName) {
        try {
            update(DROP_TABLE_SQL, new Row(), tableName, new ArrayList<>());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteContent(String tableName) {
        try {
            update(DELETE_SQL, new Row(), tableName, new ArrayList<>());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Row> select(Entity entity) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(this.setTableName(SELECT_SQL, entity.getTableName()));
        ResultSet rs = stmt.executeQuery();
        List<Row> rows = new ArrayList<>();
        int columnCount = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            Row row = new Row();
            for (int i = 1; i <= columnCount; i++) {
                try {
                    Column column = entity.findColumnByName(rs.getMetaData().getColumnName(i));
                    addColumn(column, row, rs, i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            entity.getColumns().stream()
                    .filter(e -> !row.getData().containsKey(e))
                    .forEach(e -> row.addColumn(e, e.getDefaultValue()));
            rows.add(row);
            System.out.println(entity.getTableName());
            System.out.println(row);
        }
        rs.close();
        stmt.close();
        return rows;
    }

    private void addColumn(Column column, Row row, ResultSet rs, int i) throws SQLException {
        int type = rs.getMetaData().getColumnType(i);
        switch (type) {
            case Types.VARCHAR -> row.addColumn(column, rs.getString(i));
            case Types.BIGINT  -> row.addColumn(column, rs.getLong(i));
            case Types.DOUBLE  -> row.addColumn(column, rs.getDouble(i));
            case Types.INTEGER -> row.addColumn(column, rs.getInt(i));
        }
    }



    private void close() throws SQLException {
        conn.close();
    }

    private String createInsertStatement(String sql, Entity entity) {
        String returnString = setTableName(sql, entity.getTableName());
        returnString = setColumnNames(returnString, entity.getColumns());
        returnString = replaceValues(returnString, entity.getColumns().size());
        return returnString;
    }

    private String replaceValues(String sql, int num) {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < num; i++) {
            string.append("?").append((i + 1 == num) ? "" : ",");
        }
        return sql.replace("#",string.toString());
    }
    private String setColumnNames(String sql, List<Column> columns) {
        StringBuilder string = new StringBuilder();
        columns.forEach(e -> string.append(e.getColumnName()).append(","));
        string.deleteCharAt(string.length() - 1);
        String s = string.toString();
        return sql.replace("*", s);
    }

    private String setTableName(String sql, String tableName) {
        return sql.replace("$", tableName);
    }
}
