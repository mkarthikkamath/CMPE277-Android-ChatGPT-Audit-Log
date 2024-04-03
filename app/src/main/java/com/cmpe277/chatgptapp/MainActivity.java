package com.cmpe277.chatgptapp;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextPrompt;
    private Button buttonSend, buttonCancel, buttonSave, buttonAudit;
    private TextView textViewResponse;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        editTextPrompt = findViewById(R.id.editTextPrompt);
        buttonSend = findViewById(R.id.buttonSend);
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonSave = findViewById(R.id.buttonSave);
        buttonAudit = findViewById(R.id.buttonAudit);
        textViewResponse = findViewById(R.id.textViewResponse);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prompt = editTextPrompt.getText().toString();
                new ChatGPTAsyncTask(response -> textViewResponse.setText(response)).execute(prompt);
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextPrompt.setText("");
                textViewResponse.setText("");
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        buttonAudit = findViewById(R.id.buttonAudit);
        buttonAudit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchAuditData();
            }
        });

    }

    private void saveData() {
        String promptText = editTextPrompt.getText().toString();
        String responseText = textViewResponse.getText().toString();

        // Insert prompt and response into the database
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues promptValues = new ContentValues();
        promptValues.put(AuditLogContract.AuditPromptEntry.COLUMN_NAME_PROMPT, promptText);

        ContentValues responseValues = new ContentValues();
        responseValues.put(AuditLogContract.ResponseEntry.COLUMN_NAME_RESPONSE, responseText);

        long promptRowId = db.insert(AuditLogContract.AuditPromptEntry.TABLE_NAME, null, promptValues);
        long responseRowId = db.insert(AuditLogContract.ResponseEntry.TABLE_NAME, null, responseValues);

        if (promptRowId != -1 && responseRowId != -1) {
            Toast.makeText(MainActivity.this, "Saved successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Error saving", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    private void fetchAuditData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projectionPrompt = {
                BaseColumns._ID,
                AuditLogContract.AuditPromptEntry.COLUMN_NAME_SEQUENCE_NUMBER,
                AuditLogContract.AuditPromptEntry.COLUMN_NAME_DATE_TIME,
                AuditLogContract.AuditPromptEntry.COLUMN_NAME_PROMPT
        };

        String[] projectionResponse = {
                BaseColumns._ID,
                AuditLogContract.ResponseEntry.COLUMN_NAME_SEQUENCE_NUMBER,
                AuditLogContract.ResponseEntry.COLUMN_NAME_DATE_TIME,
                AuditLogContract.ResponseEntry.COLUMN_NAME_RESPONSE
        };

        Cursor cursorPrompt = db.query(
                AuditLogContract.AuditPromptEntry.TABLE_NAME,
                projectionPrompt,
                null,
                null,
                null,
                null,
                AuditLogContract.AuditPromptEntry.COLUMN_NAME_DATE_TIME + " DESC"
        );

        Cursor cursorResponse = db.query(
                AuditLogContract.ResponseEntry.TABLE_NAME,
                projectionResponse,
                null,
                null,
                null,
                null,
                AuditLogContract.ResponseEntry.COLUMN_NAME_DATE_TIME + " DESC"
        );

        StringBuilder auditDataBuilder = new StringBuilder("Audit Data\n\nPrompts:\n");
        while (cursorPrompt.moveToNext()) {
            String dateTime = cursorPrompt.getString(cursorPrompt.getColumnIndexOrThrow(AuditLogContract.AuditPromptEntry.COLUMN_NAME_DATE_TIME));
            String prompt = cursorPrompt.getString(cursorPrompt.getColumnIndexOrThrow(AuditLogContract.AuditPromptEntry.COLUMN_NAME_PROMPT));

            auditDataBuilder.append(dateTime).append(": ").append(prompt).append("\n");
        }

        auditDataBuilder.append("\nResponses:\n");
        while (cursorResponse.moveToNext()) {
            String dateTime = cursorResponse.getString(cursorResponse.getColumnIndexOrThrow(AuditLogContract.ResponseEntry.COLUMN_NAME_DATE_TIME));
            String response = cursorResponse.getString(cursorResponse.getColumnIndexOrThrow(AuditLogContract.ResponseEntry.COLUMN_NAME_RESPONSE));

            auditDataBuilder.append(dateTime).append(": ").append(response).append("\n");
        }

        cursorPrompt.close();
        cursorResponse.close();
        db.close();

        // Logging
        Log.d("AuditData", auditDataBuilder.toString());
    }
}
