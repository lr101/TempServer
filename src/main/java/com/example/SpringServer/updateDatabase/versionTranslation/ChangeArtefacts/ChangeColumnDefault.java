package com.example.SpringServer.updateDatabase.versionTranslation.ChangeArtefacts;

import com.example.SpringServer.updateDatabase.versionTranslation.changeBasics.ChangeAbstract;
import com.example.SpringServer.updateDatabase.versionTranslation.changeBasics.ChangeInterface;
import com.example.SpringServer.updateDatabase.main.Version;
import com.example.SpringServer.updateDatabase.versionTranslation.changeBasics.ChangeType;

public class ChangeColumnDefault extends ChangeAbstract implements ChangeInterface {
    private final String columnName;
    private final Object newDefault;
    private final String entityName;

    public ChangeColumnDefault(String columnName, Object newDefault, Version version, String entityName) {
        super(ChangeType.COLUMN_DEFAULT, version);
        this.columnName = columnName;
        this.newDefault = newDefault;
        this.entityName = entityName;
    }

    @Override
    public void runChange() {
        version.changeColumnDefault(entityName,columnName, newDefault);
    }
}
