package com.example.SpringServer.updateDatabase.versionTranslation.changeBasics;

public enum ChangeType {
    CREATE_COLUMN(0), COLUMN_TYPE(1), COLUMN_NAME(2), COLUMN_DEFAULT(3), ENTITY_NAME(4);

    ChangeType(int i) {}
}
