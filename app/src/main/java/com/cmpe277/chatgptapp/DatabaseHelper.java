package com.cmpe277.chatgptapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ChatGPTLogs.db";

    private static final String SQL_CREATE_AUDIT_PROMPT =
            "CREATE TABLE " + AuditLogContract.AuditPromptEntry.TABLE_NAME + " (" +
                    AuditLogContract.AuditPromptEntry._ID + " INTEGER PRIMARY KEY," +
                    AuditLogContract.AuditPromptEntry.COLUMN_NAME_SEQUENCE_NUMBER + " TEXT," +
                    AuditLogContract.AuditPromptEntry.COLUMN_NAME_DATE_TIME + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    AuditLogContract.AuditPromptEntry.COLUMN_NAME_PROMPT + " TEXT)";

    private static final String SQL_CREATE_RESPONSES =
            "CREATE TABLE " + AuditLogContract.ResponseEntry.TABLE_NAME + " (" +
                    AuditLogContract.ResponseEntry._ID + " INTEGER PRIMARY KEY," +
                    AuditLogContract.ResponseEntry.COLUMN_NAME_SEQUENCE_NUMBER + " TEXT," +
                    AuditLogContract.ResponseEntry.COLUMN_NAME_DATE_TIME + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    AuditLogContract.ResponseEntry.COLUMN_NAME_RESPONSE + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_AUDIT_PROMPT);
        db.execSQL(SQL_CREATE_RESPONSES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS " + AuditLogContract.AuditPromptEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AuditLogContract.ResponseEntry.TABLE_NAME);
        onCreate(db);
    }
}

