package com.antonia.uaicamping.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.antonia.uaicamping.data.model.Area;
import com.antonia.uaicamping.data.model.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String DATABASE_NAME = "UaiCampingDB";

    private static final int DATABASE_VERSION = 4;

    // --- Tabela de Usuários ---
    private static final String TABLE_USERS = "users";
    private static final String COL_USER_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";
    private static final String COL_CPF = "cpf";
    private static final String COL_PHONE = "phone";
    private static final String COL_BIRTH_DATE = "birth_date";
    private static final String COL_GENDER = "gender";

    // --- Tabela de Áreas de Camping ---
    private static final String TABLE_AREAS = "areas";
    private static final String COL_AREA_ID = "id";
    private static final String COL_AREA_USER_ID = "user_id";
    private static final String COL_TITLE = "title";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_ADDRESS = "address";
    private static final String COL_PRICE_PER_NIGHT = "price_per_night";
    private static final String COL_MAX_GUESTS = "max_guests";
    private static final String COL_HAS_WATER = "has_water";
    private static final String COL_HAS_ELECTRICITY = "has_electricity";

    // --- Tabela de Favoritos ---
    private static final String TABLE_FAVORITES = "favorites";
    private static final String COL_FAV_USER_ID = "user_id";
    private static final String COL_FAV_AREA_ID = "area_id";

    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
        + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
        + COL_NAME + " TEXT,"
        + COL_EMAIL + " TEXT UNIQUE,"
        + COL_PASSWORD + " TEXT,"
        + COL_CPF + " TEXT,"
        + COL_PHONE + " TEXT,"
        + COL_BIRTH_DATE + " TEXT,"
        + COL_GENDER + " TEXT" + ")";

    private static final String CREATE_TABLE_AREAS = "CREATE TABLE " + TABLE_AREAS + "("
        + COL_AREA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
        + COL_AREA_USER_ID + " INTEGER,"
        + COL_TITLE + " TEXT,"
        + COL_DESCRIPTION + " TEXT,"
        + COL_ADDRESS + " TEXT,"
        + COL_PRICE_PER_NIGHT + " REAL,"
        + COL_MAX_GUESTS + " INTEGER,"
        + COL_HAS_WATER + " INTEGER,"
        + COL_HAS_ELECTRICITY + " INTEGER,"
        + "FOREIGN KEY(" + COL_AREA_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + ")"
        + ")";

    private static final String CREATE_TABLE_FAVORITES = "CREATE TABLE " + TABLE_FAVORITES + "("
        + COL_FAV_USER_ID + " INTEGER NOT NULL,"
        + COL_FAV_AREA_ID + " INTEGER NOT NULL,"
        + "PRIMARY KEY (" + COL_FAV_USER_ID + ", " + COL_FAV_AREA_ID + "),"
        + "FOREIGN KEY(" + COL_FAV_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "),"
        + "FOREIGN KEY(" + COL_FAV_AREA_ID + ") REFERENCES " + TABLE_AREAS + "(" + COL_AREA_ID + ")"
        + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_AREAS);
        db.execSQL(CREATE_TABLE_FAVORITES);
        Log.i(TAG, "Tabelas criadas na versão " + DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AREAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
        Log.i(TAG, "Banco de dados atualizado da versão " + oldVersion + " para " + newVersion);
    }

    public boolean addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, user.getName());
        values.put(COL_EMAIL, user.getEmail());
        values.put(COL_PASSWORD, user.getPassword());
        values.put(COL_CPF, user.getCpf());
        values.put(COL_PHONE, user.getPhone());
        values.put(COL_BIRTH_DATE, user.getBirthDate());
        values.put(COL_GENDER, user.getGender());

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    public boolean updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_NAME, user.getName());
        values.put(COL_CPF, user.getCpf());
        values.put(COL_PHONE, user.getPhone());
        values.put(COL_BIRTH_DATE, user.getBirthDate());
        values.put(COL_GENDER, user.getGender());

        int rowsAffected = db.update(TABLE_USERS, values, COL_USER_ID + " = ?", new String[]{String.valueOf(user.getId())});
        db.close();

        return rowsAffected > 0;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
            new String[]{COL_USER_ID},
            COL_EMAIL + " = ? AND " + COL_PASSWORD + " = ?",
            new String[]{email, password},
            null, null, null, "1");

        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    public int getUserIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = -1;
        Cursor cursor = db.query(TABLE_USERS,
            new String[]{COL_USER_ID},
            COL_EMAIL + " = ?",
            new String[]{email},
            null, null, null);

        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID));
        }
        cursor.close();
        db.close();
        return userId;
    }

    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
            new String[]{COL_USER_ID},
            COL_EMAIL + " = ?",
            new String[]{email},
            null, null, null, "1");

        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    public int updatePassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PASSWORD, newPassword);

        int rowsAffected = db.update(TABLE_USERS,
            values,
            COL_EMAIL + " = ?",
            new String[]{email});

        db.close();
        return rowsAffected;
    }

    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;
        Cursor cursor = db.query(TABLE_USERS,
            new String[]{COL_USER_ID, COL_NAME, COL_EMAIL, COL_PASSWORD, COL_CPF, COL_PHONE, COL_BIRTH_DATE, COL_GENDER},
            COL_USER_ID + " = ?",
            new String[]{String.valueOf(userId)},
            null, null, null);

        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID)));
            user.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COL_PASSWORD)));
            user.setCpf(cursor.getString(cursor.getColumnIndexOrThrow(COL_CPF)));
            user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE)));
            user.setBirthDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_BIRTH_DATE)));
            user.setGender(cursor.getString(cursor.getColumnIndexOrThrow(COL_GENDER)));
        }
        cursor.close();
        db.close();
        return user;
    }

    public long addArea(Area area) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_AREA_USER_ID, area.getUserId());
        values.put(COL_TITLE, area.getTitle());
        values.put(COL_DESCRIPTION, area.getDescription());
        values.put(COL_ADDRESS, area.getAddress());
        values.put(COL_PRICE_PER_NIGHT, area.getPricePerNight());
        values.put(COL_MAX_GUESTS, area.getMaxGuests());
        values.put(COL_HAS_WATER, area.isHasWater() ? 1 : 0);
        values.put(COL_HAS_ELECTRICITY, area.isHasElectricity() ? 1 : 0);

        long result = db.insert(TABLE_AREAS, null, values);
        db.close();
        return result;
    }

    public Area getAreaById(int areaId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Area area = null;
        Cursor cursor = db.query(TABLE_AREAS,
            null,
            COL_AREA_ID + " = ?",
            new String[]{String.valueOf(areaId)},
            null, null, null);

        if (cursor.moveToFirst()) {
            area = new Area();
            area.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_AREA_ID)));
            area.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_AREA_USER_ID)));
            area.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE)));
            area.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION)));
            area.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COL_ADDRESS)));
            area.setPricePerNight(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRICE_PER_NIGHT)));
            area.setMaxGuests(cursor.getInt(cursor.getColumnIndexOrThrow(COL_MAX_GUESTS)));
            area.setHasWater(cursor.getInt(cursor.getColumnIndexOrThrow(COL_HAS_WATER)) == 1);
            area.setHasElectricity(cursor.getInt(cursor.getColumnIndexOrThrow(COL_HAS_ELECTRICITY)) == 1);
        }

        cursor.close();
        db.close();
        return area;
    }

    public List<Area> getAllAreas() {
        List<Area> areaList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_AREAS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Area area = new Area();
                area.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_AREA_ID)));
                area.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_AREA_USER_ID)));
                area.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE)));
                area.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION)));
                area.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COL_ADDRESS)));
                area.setPricePerNight(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRICE_PER_NIGHT)));
                area.setMaxGuests(cursor.getInt(cursor.getColumnIndexOrThrow(COL_MAX_GUESTS)));
                area.setHasWater(cursor.getInt(cursor.getColumnIndexOrThrow(COL_HAS_WATER)) == 1);
                area.setHasElectricity(cursor.getInt(cursor.getColumnIndexOrThrow(COL_HAS_ELECTRICITY)) == 1);
                areaList.add(area);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return areaList;
    }

    public List<Area> getUserAreas(int userId) {
        List<Area> userAreaList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_AREAS,
            null,
            COL_AREA_USER_ID + " = ?",
            new String[]{String.valueOf(userId)},
            null, null, COL_AREA_ID + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Area area = new Area();
                area.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_AREA_ID)));
                area.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_AREA_USER_ID)));
                area.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE)));
                area.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION)));
                area.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COL_ADDRESS)));
                area.setPricePerNight(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRICE_PER_NIGHT)));
                area.setMaxGuests(cursor.getInt(cursor.getColumnIndexOrThrow(COL_MAX_GUESTS)));
                area.setHasWater(cursor.getInt(cursor.getColumnIndexOrThrow(COL_HAS_WATER)) == 1);
                area.setHasElectricity(cursor.getInt(cursor.getColumnIndexOrThrow(COL_HAS_ELECTRICITY)) == 1);
                userAreaList.add(area);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return userAreaList;
    }

    public boolean toggleFavorite(int userId, int areaId, boolean isFavorite) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = -1;

        if (isFavorite) {
            ContentValues values = new ContentValues();
            values.put(COL_FAV_USER_ID, userId);
            values.put(COL_FAV_AREA_ID, areaId);
            result = db.insertWithOnConflict(TABLE_FAVORITES, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        } else {
            result = db.delete(TABLE_FAVORITES,
                COL_FAV_USER_ID + " = ? AND " + COL_FAV_AREA_ID + " = ?",
                new String[]{String.valueOf(userId), String.valueOf(areaId)});
        }
        db.close();
        return result != -1;
    }

    public boolean isAreaFavorite(int userId, int areaId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVORITES,
            new String[]{COL_FAV_AREA_ID},
            COL_FAV_USER_ID + " = ? AND " + COL_FAV_AREA_ID + " = ?",
            new String[]{String.valueOf(userId), String.valueOf(areaId)},
            null, null, null, "1");

        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    public List<Area> getUserFavorites(int userId) {
        List<Area> favoriteList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT T1.* FROM " + TABLE_AREAS + " T1 INNER JOIN " + TABLE_FAVORITES + " T2 ON T1." + COL_AREA_ID + " = T2." + COL_FAV_AREA_ID + " WHERE T2." + COL_FAV_USER_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Area area = new Area();
                area.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_AREA_ID)));
                area.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_AREA_USER_ID)));
                area.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE)));
                area.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION)));
                area.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COL_ADDRESS)));
                area.setPricePerNight(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRICE_PER_NIGHT)));
                area.setMaxGuests(cursor.getInt(cursor.getColumnIndexOrThrow(COL_MAX_GUESTS)));
                area.setHasWater(cursor.getInt(cursor.getColumnIndexOrThrow(COL_HAS_WATER)) == 1);
                area.setHasElectricity(cursor.getInt(cursor.getColumnIndexOrThrow(COL_HAS_ELECTRICITY)) == 1);
                favoriteList.add(area);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return favoriteList;
    }

    public boolean updateArea(Area area) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_TITLE, area.getTitle());
        values.put(COL_DESCRIPTION, area.getDescription());
        values.put(COL_ADDRESS, area.getAddress());
        values.put(COL_PRICE_PER_NIGHT, area.getPricePerNight());
        values.put(COL_MAX_GUESTS, area.getMaxGuests());
        values.put(COL_HAS_WATER, area.isHasWater() ? 1 : 0);
        values.put(COL_HAS_ELECTRICITY, area.isHasElectricity() ? 1 : 0);

        int rows = db.update(TABLE_AREAS, values,
            COL_AREA_ID + " = ?",
            new String[]{String.valueOf(area.getId())});

        db.close();
        return rows > 0;
    }

}
