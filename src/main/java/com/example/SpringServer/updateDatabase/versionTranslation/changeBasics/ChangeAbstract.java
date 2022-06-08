package com.example.SpringServer.updateDatabase.versionTranslation.changeBasics;

import com.example.SpringServer.updateDatabase.main.Version;

public abstract class ChangeAbstract {
    protected final ChangeType changeType;
    protected final Version version;

    protected ChangeAbstract(ChangeType changeType, Version version) {
        this.changeType = changeType;
        this.version = version;
    }
}
