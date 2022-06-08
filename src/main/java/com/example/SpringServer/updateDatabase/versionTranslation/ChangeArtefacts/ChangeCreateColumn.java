package com.example.SpringServer.updateDatabase.versionTranslation.ChangeArtefacts;


import com.example.SpringServer.updateDatabase.versionTranslation.changeBasics.ChangeAbstract;
import com.example.SpringServer.updateDatabase.versionTranslation.changeBasics.ChangeInterface;
import com.example.SpringServer.updateDatabase.JDBC.Column;
import com.example.SpringServer.updateDatabase.main.Version;
import com.example.SpringServer.updateDatabase.versionTranslation.changeBasics.ChangeType;

public class ChangeCreateColumn extends ChangeAbstract implements ChangeInterface {
    private final Column column;
    private final String entity;

    public ChangeCreateColumn(Column column, String entity, Version version) {
        super(ChangeType.CREATE_COLUMN, version);
        this.column = column;
        this.entity = entity;
    }

    @Override
    public void runChange() {
        version.changeAddColumn(entity, column);
    }
}
