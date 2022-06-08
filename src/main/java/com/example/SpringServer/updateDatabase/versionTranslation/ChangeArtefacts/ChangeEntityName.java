package com.example.SpringServer.updateDatabase.versionTranslation.ChangeArtefacts;


import com.example.SpringServer.updateDatabase.versionTranslation.changeBasics.ChangeAbstract;
import com.example.SpringServer.updateDatabase.versionTranslation.changeBasics.ChangeInterface;
import com.example.SpringServer.updateDatabase.main.Version;
import com.example.SpringServer.updateDatabase.versionTranslation.changeBasics.ChangeType;

public class ChangeEntityName extends ChangeAbstract implements ChangeInterface {
    private final String newName;
    private final String entity;

    public ChangeEntityName(String newName, String entity, Version version) {
        super(ChangeType.ENTITY_NAME, version);
        this.newName = newName;
        this.entity = entity;
    }

    @Override
    public void runChange() {
        version.changeEntityName(entity, newName);
    }
}
