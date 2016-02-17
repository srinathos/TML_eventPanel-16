package in.edu.siesgst.eventpanel.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import in.edu.siesgst.eventpanel.fragment.participant.ParticipantContent;

/**
 * Created by vishal on 1/1/16.
 */
public class LocalDBHandler extends SQLiteOpenHelper {

    final String U_TML_ID = "uID";
    final String U_NAME = "uName";
    final String U_EMAIL = "uEmail";
    final String U_PHONE = "uPhone";
    final String YEAR = "Year";
    final String BRANCH = "Branch";
    final String DIVISION = "Division";
    final String COLLEGE = "College";
    final String U_CREATED = "uCreated";
    final String U_MODIFIED = "uModified";
    final String U_PAYMENT_STATUS = "uPaymentStatus";
    final String U_ATTENDANCE_STATUS = "uAttendance";

    final String E_KEY = "eID";
    final String E_NAME = "eName";
    final String E_DAY = "eDay";
    final String E_VENUE = "eVenue";
    final String E_CATEGORY = "eCategory";
    final String E_SUBCATEGORY = "eSubCategory";
    final String E_DETAIL = "eDetails";
    final String EVENT_HEAD_1 = "eHead1";
    final String EVENT_HEAD_2 = "eHead2";
    final String E_PHONE_1 = "ePhone1";
    final String E_PHONE_2 = "ePhone2";
    final String E_CREATED = "eCreated";
    final String E_MODIFIED = "eModified";

    final int DB_VERSION = 1;

    final String USER_TABLE_NAME = "user_table";
    final String EVENT_TABLE_NAME = "event_table";

    final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS user_table(uID VARCHAR DEFAULT NULL," +
            "uName VARCHAR DEFAULT NULL," +
            "uEmail VARCHAR DEFAULT NULL," +
            "uPhone VARCHAR DEFAULT NULL," +
            "Year VARCHAR DEFAULT NULL," +
            "Branch VARCHAR DEFAULT NULL," +
            "Division VARCHAR DEFAULT NULL," +
            "College VARCHAR DEFAULT NULL," +
            "uCreated VARCHAR DEFAULT NULL," +
            "uModified VARCHAR DEFAULT NULL," +
            "uPaymentStatus VARCHAR DEFAULT NULL," +
            "uAttendance VARCHAR DEFAULT NULL )";

    final String CREATE_EVENT_TABLE = "CREATE TABLE IF NOT EXISTS event_table(eID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "eName VARCHAR DEFAULT NULL," +
            "eDay VARCHAR DEFAULT NULL," +
            "eVenue VARCHAR DEFAULT NULL," +
            "eCategory VARCHAR DEFAULT NULL," +
            "eSubCategory VARCHAR DEFAULT NULL," +
            "eDetails VARCHAR DEFAULT NULL," +
            "eHead1 VARCHAR DEFAULT NULL," +
            "ePhone1 VARCHAR DEFAULT NULL," +
            "eHead2 VARCHAR DEFAULT NULL," +
            "ePhone2 VARCHAR DEFAULT NULL," +
            "eCreated VARCHAR DEFAULT NULL," +
            "eModified VARCHAR DEFAULT NULL)";

    public LocalDBHandler(Context context) {
        super(context, "eventPanel_LDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_USER_TABLE);
        database.execSQL(CREATE_EVENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void wapasTableBana() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + EVENT_TABLE_NAME);
        db.execSQL(CREATE_EVENT_TABLE);
        db.close();
    }

    public void dropEventsTable() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + EVENT_TABLE_NAME);
        db.execSQL(CREATE_EVENT_TABLE);
        db.close();
    }


    public boolean doesExists()///TO CHECK IF DB EXISTS
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(USER_TABLE_NAME, new String[]{U_NAME}, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            Log.d("TML", "DATABASE ENTRIES: " + cursor.getCount());
            cursor.close();
            db.close();
            return true;
        }
        Log.d("TML", "NO DATABASE;WILL CREATE ACCORDINGLY");
        db.close();
        return false;
    }

    public boolean doesEventTableExist() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + EVENT_TABLE_NAME + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public boolean isEventDataFilled() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(EVENT_TABLE_NAME, new String[]{E_NAME}, null, null, null, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public void insertPartcipantData(String[] data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(U_TML_ID, data[0]);
        values.put(U_NAME, data[1]);
        values.put(U_EMAIL, data[2]);
        values.put(U_PHONE, data[3]);
        values.put(YEAR, data[4]);
        values.put(BRANCH, data[5]);
        values.put(DIVISION, data[6]);
        values.put(COLLEGE, data[7]);
        values.put(U_CREATED, data[8]);
        values.put(U_MODIFIED, data[9]);
        values.put(U_PAYMENT_STATUS, data[10]);
        values.put(U_ATTENDANCE_STATUS,data[11]);
        db.insert(USER_TABLE_NAME, null, values);
        db.close();
    }

    public void insertEventData(String[] data) {    //TODO Check column names
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(E_NAME, data[0]);
        values.put(E_DAY, data[1]);
        values.put(E_VENUE, data[2]);
        values.put(E_CATEGORY, data[3]);
        values.put(E_SUBCATEGORY, data[4]);
        values.put(E_DETAIL, data[5]);
        values.put(EVENT_HEAD_1, data[6]);
        values.put(E_PHONE_1, data[7]);
        values.put(EVENT_HEAD_2, data[8]);
        values.put(E_PHONE_2, data[9]);
        values.put(E_CREATED, data[10]);
        values.put(E_MODIFIED, data[11]);
        db.insert(EVENT_TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<String> getEventNamesAndDay(String category, String subCategory) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;
        if (subCategory != null) {
            cursor = db.query(EVENT_TABLE_NAME, new String[]{E_NAME, E_DAY}, E_CATEGORY + "='" + category + "' and " + E_SUBCATEGORY + "='" + subCategory + "'", null, null, null, null, null);
        } else {
            cursor = db.query(EVENT_TABLE_NAME, new String[]{E_NAME, E_DAY}, E_CATEGORY + "='" + category + "'", null, null, null, null, null);
        }
        ArrayList<String> arrayList = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            arrayList.add(cursor.getString(cursor.getColumnIndex(E_NAME)));
            arrayList.add(cursor.getString(cursor.getColumnIndex(E_DAY)));
        }
        cursor.close();
        db.close();

        return arrayList;
    }

    public ArrayList<String> getAllEventNames() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(EVENT_TABLE_NAME, new String[]{E_NAME}, null, null, null, null, null, null);
        ArrayList<String> arrayList = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            arrayList.add(cursor.getString(cursor.getColumnIndex(E_NAME)));
        }
        cursor.close();
        db.close();

        return arrayList;
    }

    public ArrayList<String> getEventKaSabKuch(String eventName) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(EVENT_TABLE_NAME, new String[]{E_DETAIL, E_DAY, E_VENUE, EVENT_HEAD_1, EVENT_HEAD_2, E_PHONE_1, E_PHONE_2}, E_NAME + "='" + eventName + "'", null, null, null, null);
        ArrayList<String> arrayList = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            arrayList.add(cursor.getString(cursor.getColumnIndex(E_DETAIL)));
            arrayList.add(cursor.getString(cursor.getColumnIndex(E_DAY)));
            arrayList.add(cursor.getString(cursor.getColumnIndex(E_VENUE)));
            arrayList.add(cursor.getString(cursor.getColumnIndex(EVENT_HEAD_1)));
            arrayList.add(cursor.getString(cursor.getColumnIndex(EVENT_HEAD_2)));
            arrayList.add(cursor.getString(cursor.getColumnIndex(E_PHONE_1)));
            arrayList.add(cursor.getString(cursor.getColumnIndex(E_PHONE_2)));
        }
        cursor.close();
        db.close();

        return arrayList;
    }

    public ArrayList<String> getParticipantNames() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(USER_TABLE_NAME, new String[]{U_NAME}, null, null, null, null, null);
        ArrayList<String> arrayList = new ArrayList<String>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            arrayList.add(cursor.getString(cursor.getColumnIndex(U_NAME)));
        }
        return arrayList;
    }

    public ArrayList<String> getParticipantPaymentStatus() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(USER_TABLE_NAME, new String[]{U_NAME}, null, null, null, null, null);
        ArrayList<String> arrayList = new ArrayList<String>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            arrayList.add(cursor.getString(cursor.getColumnIndex(U_NAME)));
        }
        return arrayList;
    }

    public ArrayList<String> getParticipantPhone() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(USER_TABLE_NAME, new String[]{U_NAME}, null, null, null, null, null);
        ArrayList<String> arrayList = new ArrayList<String>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            arrayList.add(cursor.getString(cursor.getColumnIndex(U_NAME)));
        }
        return arrayList;
    }

    public ParticipantContent.Participant getParticipant(String TML_ID) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(USER_TABLE_NAME, new String[]{U_NAME, U_PHONE, U_PAYMENT_STATUS}, U_TML_ID + "='" + TML_ID + "'", null, null, null, null);
        cursor.moveToFirst();
        String data[] = {cursor.getString(cursor.getColumnIndex(U_NAME)), cursor.getString(cursor.getColumnIndex(U_PHONE)), cursor.getString(cursor.getColumnIndex(U_PAYMENT_STATUS))};
        cursor.close();
        return new ParticipantContent.Participant(data[0], data[1], data[3]);
    }

    public String getTotalParticipants(){
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor = db.query(USER_TABLE_NAME, new String[]{U_ATTENDANCE_STATUS},null, null, null, null, null);
        String data=cursor.getCount()+"";
        cursor.close();
        return data;
    }

    public String getPresentParticipants(){
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor = db.query(USER_TABLE_NAME, new String[]{U_ATTENDANCE_STATUS}, U_ATTENDANCE_STATUS + "='T'", null, null, null, null);
        String presentCount=cursor.getCount()+"";
        cursor.close();
        return presentCount;
    }

    public String getAbsentParticipants(){
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor = db.query(USER_TABLE_NAME, new String[]{U_ATTENDANCE_STATUS},U_ATTENDANCE_STATUS+"='F'", null, null, null, null);
        String presentCount=cursor.getCount()+"";
        cursor.close();
        return presentCount;
    }

    public String getPaymentCompleteParticipants(){ //TODO Ask values from Shata
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor = db.query(USER_TABLE_NAME, new String[]{U_PAYMENT_STATUS},null, null, null, null, null);
        return null;
    }

    public String getPaymentIncompleteParticipants(){
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor = db.query(USER_TABLE_NAME, new String[]{U_PAYMENT_STATUS},null, null, null, null, null);
        return null;
    }

    public String getPaymentPartialCompleteParticipants(){
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor = db.query(USER_TABLE_NAME, new String[]{U_PAYMENT_STATUS},null, null, null, null, null);
        return null;
    }

    public int getDBVersion() {
        return DB_VERSION;
    }
}