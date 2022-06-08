package com.example.SpringServer.updateDatabase.main;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import com.example.SpringServer.updateDatabase.JDBC.Column;
import com.example.SpringServer.updateDatabase.JDBC.Entity;
import com.example.SpringServer.updateDatabase.versionTranslation.ChangeArtefacts.ChangeCreateColumn;
import com.example.SpringServer.updateDatabase.versionTranslation.ChangeArtefacts.ChangeCreateEntity;
import com.example.SpringServer.updateDatabase.versionTranslation.changeBasics.ChangeInterface;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;


@Component
public class ChangeManager {

    private static final int startVersion = 1;
    private static final int endVersion = 2;
    private final List<List<ChangeInterface>> changeHistory = new ArrayList<>();
    Version version = new Version();

    public ChangeManager() {
        if (System.getenv("update") != null && Boolean.parseBoolean(System.getenv("update"))) {
            addVersionChanges();
            this.runPull();
        }
    }
    
    private void addVersionChanges() {
        changeHistory.add(version1());
        changeHistory.add(version2());
    }
    
    private List<ChangeInterface> version1() {
        List<ChangeInterface> changes = new ArrayList<>();
        List<Column> columns = new ArrayList<>();
        //id
        columns.add(new Column( "id", Types.BIGINT, -1L));
        columns.add(new Column( "sensor_nick", Types.VARCHAR, "newSensor"));
        columns.add(new Column( "sensor_type_id", Types.BIGINT, 0L));
        columns.add(new Column( "sensor_id", Types.VARCHAR, null));
        changes.add(new ChangeCreateEntity(new Entity("id", columns), version));
        //type
        columns.clear();
        columns.add(new Column( "id", Types.BIGINT, 0L));
        columns.add(new Column( "sensor_type", Types.VARCHAR, "Default"));
        columns.add(new Column( "unit", Types.VARCHAR, "dUnit"));
        columns.add(new Column( "repetitions", Types.INTEGER, 10));
        columns.add(new Column( "sleep_time", Types.INTEGER, 10));
        changes.add(new ChangeCreateEntity(new Entity("type", columns), version));
        return changes;
    }

    private List<ChangeInterface> version2() {
        List<ChangeInterface> changes = new ArrayList<>();
        List<Column> columns = new ArrayList<>();
        //category
        columns.add(new Column( "id", Types.BIGINT, -1L));
        columns.add(new Column( "sensor_category", Types.VARCHAR, null));
        changes.add(new ChangeCreateEntity(new Entity("category", columns), version));
        //id_category
        columns.clear();
        columns.add(new Column( "id", Types.BIGINT, null));
        columns.add(new Column( "category_id", Types.BIGINT, null));
        changes.add(new ChangeCreateEntity(new Entity("id_category", columns), version));
        return changes;
    }

    private void runFromToVersion(int start, int end)  {
        for (int i = start; i < end; i++) {
            changeHistory.get(i).forEach(ChangeInterface::runChange);
        }
    }

    public void runPull() {
       System.out.println("[Selecting data]");
       runFromToVersion(0, startVersion);
       version.initPull();
       System.out.println("[Delete tables]");
       version.initDelete();
    }


    public void runPush() {
        if (System.getenv("update") != null && Boolean.parseBoolean(System.getenv("update"))) {
            System.out.println("[Inserting data]");
            runFromToVersion(startVersion, endVersion);
            version.initPush();
        }
    }
}
