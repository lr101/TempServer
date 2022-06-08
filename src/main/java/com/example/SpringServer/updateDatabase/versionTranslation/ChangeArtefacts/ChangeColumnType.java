package com.example.SpringServer.updateDatabase.versionTranslation.ChangeArtefacts;


import com.example.SpringServer.updateDatabase.versionTranslation.changeBasics.ChangeAbstract;
import com.example.SpringServer.updateDatabase.versionTranslation.changeBasics.ChangeInterface;
import com.example.SpringServer.updateDatabase.main.Version;
import com.example.SpringServer.updateDatabase.versionTranslation.changeBasics.ChangeType;

public class ChangeColumnType extends ChangeAbstract implements ChangeInterface {
    private final String columnName;
    private final int newType;
    private final String entity;

    public ChangeColumnType(String columnName, int newType, String entity, Version version) {
        super(ChangeType.COLUMN_NAME, version);
        this.columnName = columnName;
        this.newType = newType;
        this.entity = entity;
    }

    @Override
    public void runChange() {
        version.changeColumnType(entity,columnName, newType);
    }
}
