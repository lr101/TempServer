package com.example.SpringServer.updateDatabase.versionTranslation.ChangeArtefacts;

import com.example.SpringServer.updateDatabase.versionTranslation.changeBasics.ChangeAbstract;
import com.example.SpringServer.updateDatabase.versionTranslation.changeBasics.ChangeInterface;
import com.example.SpringServer.updateDatabase.JDBC.Entity;
import com.example.SpringServer.updateDatabase.main.Version;
import com.example.SpringServer.updateDatabase.versionTranslation.changeBasics.ChangeType;

public class ChangeCreateEntity extends ChangeAbstract implements ChangeInterface {
    private final Entity entity;

    public ChangeCreateEntity(Entity entity, Version version) {
        super(ChangeType.ENTITY_NAME, version);
        this.entity = entity;
    }

    @Override
    public void runChange() {
        version.addEntity(entity);
    }
}
