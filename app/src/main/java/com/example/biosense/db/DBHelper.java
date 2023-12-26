package com.example.biosense.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DataBase";
    private static DBHelper instance;

    // DataBase Fields
    private static final String DATABASE_NAME = "exam_history.db";
    private static final int DATABASE_VERSION = 1;


    // Define the table schema in a contract class
    public static class ExamEntry {
        public static final String TABLE_NAME = "exam_results";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_FILENAME = "filename";
        public static final String COLUMN_NAME_EXAM_CODE = "exam_code";
        public static final String COLUMN_NAME_RESULT = "result";
        public static final String COLUMN_NAME_TECHNIQUE = "technique";
    }

    // SQL statement to create the table
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ExamEntry.TABLE_NAME + " (" +
                    ExamEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    ExamEntry.COLUMN_NAME_FILENAME + " TEXT," +
                    ExamEntry.COLUMN_NAME_EXAM_CODE + " TEXT," +
                    ExamEntry.COLUMN_NAME_RESULT + " INTEGER," +
                    ExamEntry.COLUMN_NAME_TECHNIQUE + " TEXT)";

    // SQL statement to delete the table
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ExamEntry.TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Create");
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Upgrade");
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    // Insert a new Exam into database
    public long insertExamResult(Exam _exam) {

        if (_exam != null) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(ExamEntry.COLUMN_NAME_FILENAME, _exam.getFileName());
            values.put(ExamEntry.COLUMN_NAME_EXAM_CODE, _exam.getExamId());
            values.put(ExamEntry.COLUMN_NAME_TECHNIQUE, _exam.getTechnique());
            values.put(ExamEntry.COLUMN_NAME_RESULT, _exam.getResult());

            long newRow = db.insert(ExamEntry.TABLE_NAME, null, values);
            db.close();
            return newRow;
        }
        return -1;
    }

    public Cursor getResultsByTechnique (String technique) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                ExamEntry.COLUMN_NAME_EXAM_CODE,
                ExamEntry.COLUMN_NAME_TECHNIQUE,
                ExamEntry.COLUMN_NAME_RESULT,
                ExamEntry.COLUMN_NAME_FILENAME,
                ExamEntry.COLUMN_NAME_ID
        };

        String selection = ExamEntry.COLUMN_NAME_TECHNIQUE + " = ?";
        String[] selectionArgs = { technique };

        return db.query(ExamEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
    }

    public Cursor getResultsByStatus (int _status) {

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                ExamEntry.COLUMN_NAME_EXAM_CODE,
                ExamEntry.COLUMN_NAME_TECHNIQUE,
                ExamEntry.COLUMN_NAME_RESULT,
                ExamEntry.COLUMN_NAME_FILENAME,
                ExamEntry.COLUMN_NAME_ID
        };

        String selection = ExamEntry.COLUMN_NAME_RESULT + " = ?";
        String[] selectionArgs = { String.valueOf(_status) };

        return db.query(
                ExamEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }

    public Cursor getResultsByStatusAndTechnique(int _status, String _technique) {
        SQLiteDatabase db = this.getWritableDatabase();

        String[] projection = {
                ExamEntry.COLUMN_NAME_EXAM_CODE,
                ExamEntry.COLUMN_NAME_TECHNIQUE,
                ExamEntry.COLUMN_NAME_RESULT,
                ExamEntry.COLUMN_NAME_FILENAME,
                ExamEntry.COLUMN_NAME_ID
        };

        String selection = ExamEntry.COLUMN_NAME_RESULT + " = ? AND " +
                ExamEntry.COLUMN_NAME_TECHNIQUE + "= ?";

        String[] selectionArgs = { String.valueOf(_status), _technique };

        return db.query(
                ExamEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }

    public Cursor getAllEntries() {
        Log.d(TAG, "getAll");
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                ExamEntry.COLUMN_NAME_FILENAME,
                ExamEntry.COLUMN_NAME_EXAM_CODE,
                ExamEntry.COLUMN_NAME_RESULT,
                ExamEntry.COLUMN_NAME_TECHNIQUE,
                ExamEntry.COLUMN_NAME_ID
        };

        return db.query(
                ExamEntry.TABLE_NAME,
                projection,
                null,  // selection - null to retrieve all entries
                null,  // selectionArgs - null as there's no specific condition
                null,
                null,
                null
        );
    }
}
