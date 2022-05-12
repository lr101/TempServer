package com.example.SpringServer.Entities;

import com.example.SpringServer.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class JDBC {

    private final SQL SQL_STRINGS = new SQL();
    private Connection conn;

    @Autowired
    @Lazy
    TypeRepository typeRepository;

    public JDBC()  {

        try {
            Properties p = new Properties();
            p.load(getClass().getResourceAsStream("/application.properties"));

            Class.forName("org.postgresql.Driver");

            Properties props = new Properties();
            props.put("user", System.getenv("DB_USER"));
            props.put("password", System.getenv("DB_PASSWORD"));

            conn = DriverManager.getConnection(System.getenv("DATASOURCE_URL") , props);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private ArrayList<Entry> selectEntries(Object[] params, String sql) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(this.setTableName(sql, (String) params[0]));
        this.prepareStatement(stmt, params);
        ResultSet rs = stmt.executeQuery();
        ArrayList<Entry> entries = new ArrayList<>();
        while (rs.next()) {
            Entry entry = new Entry(rs.getLong("row_id"), rs.getDouble("value"), rs.getTimestamp("date"));
            entries.add(entry);
        }
        rs.close();
        stmt.close();
        return entries;
    }



    public ArrayList<Entry> selectAllEntries(String sensor) throws SQLException {
        ArrayList<Entry> entries = selectEntries(new Object[] {"backup_" + sensor}, SQL_STRINGS.selectAllEntry());
        entries.addAll(selectEntries(new Object[] {"s" + sensor}, SQL_STRINGS.selectAllEntry()));
        return entries;
    }

    public ArrayList<Entry> selectEntriesBetween(String sensor, Timestamp date1, Timestamp date2) throws SQLException {
        ArrayList<Entry> entries = selectEntries(new Object[] {"backup_" + sensor, date1, date2}, SQL_STRINGS.selectEntryBetween());
        entries.addAll(selectEntries(new Object[] {"s" + sensor, date1, date2}, SQL_STRINGS.selectEntryBetween()));
        return entries;
    }

    public ArrayList<Entry> selectEntriesDate1(String sensor, Timestamp date1) throws SQLException {
        ArrayList<Entry> entries = selectEntries(new Object[] {"backup_" + sensor, date1}, SQL_STRINGS.selectEntryDate1());
        entries.addAll(selectEntries(new Object[] {"s" + sensor, date1}, SQL_STRINGS.selectEntryDate1()));
        return entries;
    }

    public ArrayList<Entry> selectEntriesDate2(String sensor, Timestamp date2) throws SQLException {
        ArrayList<Entry> entries = selectEntries(new Object[] {"backup_" + sensor, date2}, SQL_STRINGS.selectEntryDate2());
        entries.addAll(selectEntries(new Object[] {"s" + sensor, date2}, SQL_STRINGS.selectEntryDate2()));
        return entries;
    }

    public ArrayList<Entry> selectFirst(String sensor, Timestamp date2, Integer limit) throws SQLException {
        return selectEntries(new Object[] {"s" + sensor, date2}, SQL_STRINGS.selectFirst(limit));
    }

    public void insertEntry(Entry entry, String sensor)  {
        if (entry.getDate() == null) {
            java.util.Date utilDate = new java.util.Date();
            entry.setDate(new Timestamp(utilDate.getTime()));
        }
        try {
            this.update(SQL_STRINGS.insertEntry(), new Object[]{"s" + sensor, entry.getDate(), entry.getEntryValue()});
        } catch(SQLException e) {
            System.out.println(e);
        }
    }

    public void insertEntryBatch(ArrayList<Entry> entries, String sensor) {
        try {
            PreparedStatement stmt = conn.prepareStatement(this.setTableName(SQL_STRINGS.insertEntry(), "backup_" + sensor));
            for (Entry entry : entries) {
                this.prepareStatement(stmt, new Object[]{"backup_" + sensor, entry.getDate(), entry.getEntryValue()});
                stmt.addBatch();
                stmt.clearParameters();
            }
            stmt.executeBatch();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void createEntryTable(String sensor) {
        try {
            this.update(SQL_STRINGS.createEntryTable(), new Object[]{"s" + sensor});
            this.update(SQL_STRINGS.createEntryTable(), new Object[]{"backup_" + sensor});
        } catch(SQLException e) {
            System.out.println(e);
        }
    }

    public void deleteAllEntryTable(String sensor) {
        try {
            this.update(SQL_STRINGS.deleteAllEntry(), new Object[] {"s" + sensor});
            this.update(SQL_STRINGS.deleteAllEntry(), new Object[] {"backup_" + sensor});
        } catch(SQLException ignored) {}
    }

    public void deleteEntryBetweenTable(String sensor, Timestamp date1, Timestamp date2) {
        try {
            this.update(SQL_STRINGS.deleteEntryBetween(), new Object[] {"s" + sensor, date1, date2});
            this.update(SQL_STRINGS.deleteEntryBetween(), new Object[] {"backup_" + sensor, date1, date2});
        } catch(SQLException ignored) {}
    }

    public void deleteEntryDate1(String sensor, Timestamp date1) {
        try {
            this.update(SQL_STRINGS.deleteEntryDate1(), new Object[] {"s" + sensor, date1});
            this.update(SQL_STRINGS.deleteEntryDate1(), new Object[] {"backup_" + sensor, date1});
        } catch(SQLException e) {
            System.out.println(e);
        }
    }

    public void deleteEntryDate2(String sensor, Timestamp date2) {
        try {
            this.update(SQL_STRINGS.deleteEntryDate2(), new Object[] {"s" + sensor, date2});
            this.update(SQL_STRINGS.deleteEntryDate2(), new Object[] {"backup_" + sensor, date2});
        } catch(SQLException ignored) {}
    }

    public void dropTable(String sensor) {
        try {
           this.update(SQL_STRINGS.deleteEntryTable(), new Object[] {"s" + sensor});
            this.update(SQL_STRINGS.deleteEntryTable(), new Object[] {"backup_" + sensor});
        } catch(SQLException e) {
            System.out.println(e);
        }
    }
    private void prepareStatement(PreparedStatement stmt, Object[] objs) throws SQLException {
        int count = 0;
        for (Object obj : objs) {
            if (count > 0) {
                if (obj instanceof String) {
                    stmt.setString(count, (String) obj);
                } else if (obj instanceof Integer) {
                    stmt.setInt(count, (Integer) obj);
                } else if (obj instanceof Double) {
                    stmt.setDouble(count, (Double) obj);
                }else if (obj instanceof Long) {
                    stmt.setLong(count, (Long) obj);
                } else if (obj instanceof java.sql.Timestamp) {
                    stmt.setTimestamp(count, (java.sql.Timestamp) obj);
                }
            }
            count++;
        }
    }

    public void updateType(Long idOld, Long idNew) {
        try {
            this.update(SQL_STRINGS.updateById(), new Object[] {"type", idOld, idNew});
        }catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    public void updateCategory(Long idOld, Long idNew) {
        try {
            this.update(SQL_STRINGS.updateById(), new Object[] {"category", idOld, idNew});
        }catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    private void update(String sqlQuery, Object[] objs) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(this.setTableName(sqlQuery, (String) objs[0]));
        this.prepareStatement(stmt, objs);
        stmt.executeUpdate();
        stmt.close();
    }

    public Map<String, Object> getDisplayData(String sensor, int interval, Timestamp date1, Timestamp date2) {
        try {
            return formatForGraph(selectEntries(new Object[] {"s" + sensor,interval, interval, interval, interval, date1, date2}, SQL_STRINGS.selectDisplay()));
        } catch (SQLException e) {
            System.out.println(e);
            return new HashMap<>();
        }
    }

    private Map<String, Object> formatForGraph(ArrayList<Entry> entries) {
        double[] values = new double[entries.size()];
        Timestamp[] dates = new Timestamp[entries.size()];
        for (int i = 0; i < entries.size(); i++) {
            values[i] = entries.get(i).getEntryValue();
            dates[i] = entries.get(i).getDate();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("values", values);
        map.put("dates", dates);
        return map;
    }

    private void close() throws SQLException {
        conn.close();
    }

    private String setTableName(String sql, String tableName) {
        return sql.replace("$", tableName);
    }
}
