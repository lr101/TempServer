package com.example.SpringServer.updateDatabase.versionTranslation.ChangeArtefacts;

import com.example.SpringServer.updateDatabase.versionTranslation.changeBasics.ChangeAbstract;
import com.example.SpringServer.updateDatabase.versionTranslation.changeBasics.ChangeInterface;
import com.example.SpringServer.updateDatabase.main.Version;
import com.example.SpringServer.updateDatabase.versionTranslation.changeBasics.ChangeType;

public class ChangeColumnName extends ChangeAbstract implements ChangeInterface {
    private final String oldName;
    private final String newName;
    private final String entity;

    public ChangeColumnName(String oldName, String newName, String entity, Version version) {
        super(ChangeType.COLUMN_NAME, version);
        this.oldName = oldName;
        this.newName = newName;
        this.entity = entity;
    }

    @Override
    public void runChange() {
        version.changeColumnName(entity,oldName, newName);
    }
}
