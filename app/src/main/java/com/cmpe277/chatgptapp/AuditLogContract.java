package com.cmpe277.chatgptapp;

import android.provider.BaseColumns;

public final class AuditLogContract {

    private AuditLogContract() {}

    public static class AuditPromptEntry implements BaseColumns {
        public static final String TABLE_NAME = "AuditPrompt";
        public static final String COLUMN_NAME_SEQUENCE_NUMBER = "SequenceNumber";
        public static final String COLUMN_NAME_DATE_TIME = "DateTime";
        public static final String COLUMN_NAME_PROMPT = "Prompt";
    }

    public static class ResponseEntry implements BaseColumns {
        public static final String TABLE_NAME = "Responses";
        public static final String COLUMN_NAME_SEQUENCE_NUMBER = "SequenceNumber";
        public static final String COLUMN_NAME_DATE_TIME = "DateTime";
        public static final String COLUMN_NAME_RESPONSE = "Response";
    }
}

