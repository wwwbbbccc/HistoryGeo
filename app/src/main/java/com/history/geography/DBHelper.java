package com.history.geography;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static String DB_Path = "";
    private static String DB_Name = "StoryDB.db";
    private SQLiteDatabase mDatabase;
    private Context mContext = null;

    public DBHelper(Context context) {
        super(context, DB_Name, null, 1);
        //     if (Build.VERSION.SDK_INT > 17)
        //        DB_Path = context.getApplicationInfo().dataDir + "/database/";
        //    else
        DB_Path = "/data/data/" + context.getPackageName() + "/databases/";
        this.mContext = context;
    }

    @Override
    public synchronized void close() {
        if (mDatabase != null)
            mDatabase.close();
        super.close();
    }

    public boolean checkDataBase() {
        SQLiteDatabase tempDB = null;
        try {
            String path = DB_Path + DB_Name;
            tempDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);

        } catch (Exception ex) {
            Log.println(1, "dbhelper", ex.toString());
            if (tempDB != null)
                tempDB.close();

        }
        return tempDB != null ? true : false;
    }

    public void copyDataBase() {
        try {
            InputStream myInput = mContext.getAssets().open(DB_Name);
            String outputFileName = DB_Path + DB_Name;
            File file = new File(DB_Path);
            if (!file.exists())
                file.mkdirs();
            OutputStream myOutput = new FileOutputStream(outputFileName);
            byte[] buffer = new byte[1024];
            int lenght;
            while ((lenght = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, lenght);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openDataBase() {
        String path = DB_Path + DB_Name;

        mDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void createDatabase() {
        boolean isDBExsist = checkDataBase();
        if (isDBExsist) {

        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (Exception ex) {
                Log.i("dbhelper", ex.toString());
            }
        }
    }

    public ArrayList<Provinces> getAllProvinces() {
        ArrayList<Provinces> temp = new ArrayList<Provinces>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        try {
            c = db.rawQuery("SELECT  * FROM Provinces", null);
            c.moveToFirst();
            do {
                Provinces data = new Provinces(c.getInt(c.getColumnIndex("no")), c.getString(c.getColumnIndex("location_id")), c.getString(c.getColumnIndex("name_chs")),
                        c.getString(c.getColumnIndex("parent_id")), c.getString(c.getColumnIndex("lng")), c.getString(c.getColumnIndex("lat")),
                        c.getString(c.getColumnIndex("source")));
                temp.add(data);
            } while (c.moveToNext());
        } catch (Exception ex) {
            Log.i("dbhelper", ex.toString());
        } finally {
            db.close();
        }
        return temp;
    }

    public Provinces getProvinceByName(String name) {
        Provinces temp = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        try {
            c = db.rawQuery("SELECT  * FROM Provinces WHERE name_chs='" + name + "' LIMIT 1", null);
            c.moveToFirst();
            temp = new Provinces(c.getInt(c.getColumnIndex("no")), c.getString(c.getColumnIndex("location_id")), c.getString(c.getColumnIndex("name_chs")),
                    c.getString(c.getColumnIndex("parent_id")), c.getString(c.getColumnIndex("lng")), c.getString(c.getColumnIndex("lat")),
                    c.getString(c.getColumnIndex("source")));
        } catch (Exception ex) {
            Log.i("dbhelper", ex.toString());
        } finally {
            db.close();
        }
        return temp;
    }

    public ArrayList<Countys> getCountysByMunicipality(String provinceId, int subIndex) {
        ArrayList<Countys> temp = new ArrayList<Countys>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        String id = provinceId.substring(0, subIndex);
        try {
            c = db.rawQuery("SELECT  * FROM Countys WHERE parent_id LIKE '" + id + "%'", null);
            c.moveToFirst();
            do {
                Countys data = new Countys(c.getInt(c.getColumnIndex("no")), c.getString(c.getColumnIndex("location_id")), c.getString(c.getColumnIndex("name_chs")),
                        c.getString(c.getColumnIndex("parent_id")), c.getString(c.getColumnIndex("lng")), c.getString(c.getColumnIndex("lat")),
                        c.getString(c.getColumnIndex("pp_name")), c.getString(c.getColumnIndex("parent_name")), c.getString(c.getColumnIndex("memo1")),
                        c.getString(c.getColumnIndex("source")));
                temp.add(data);
            } while (c.moveToNext());
        } catch (Exception ex) {
            Log.i("dbhelper", ex.toString());
        } finally {
            db.close();
        }
        return temp;
    }

    public ArrayList<Municipalitys> getMunicipalitysByProvince(String provinceId) {
        ArrayList<Municipalitys> temp = new ArrayList<Municipalitys>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        String id = provinceId.substring(0, 5);
        try {
            c = db.rawQuery("SELECT  * FROM Municipalitys WHERE parent_id LIKE '" + id + "%'", null);
            c.moveToFirst();
            do {
                Municipalitys data = new Municipalitys(c.getInt(c.getColumnIndex("no")), c.getString(c.getColumnIndex("location_id")),
                        c.getString(c.getColumnIndex("name_chs")), c.getString(c.getColumnIndex("parent_id")),
                        c.getString(c.getColumnIndex("lng")), c.getString(c.getColumnIndex("lat")),
                        c.getString(c.getColumnIndex("parent_name")), c.getString(c.getColumnIndex("memo1")),
                        c.getString(c.getColumnIndex("source")));
                temp.add(data);
            } while (c.moveToNext());
        } catch (Exception ex) {
            Log.i("dbhelper", ex.toString());
        } finally {
            db.close();
        }
        return temp;
    }

    public ArrayList<Townships> getTownshipsByCounty(String provinceId, int subIndex) {
        ArrayList<Townships> temp = new ArrayList<Townships>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        String id = provinceId.substring(0, subIndex);
        try {
            c = db.rawQuery("SELECT  * FROM Townships WHERE parent_id LIKE '" + id + "%'", null);
            c.moveToFirst();
            do {
                Townships data = new Townships(c.getInt(c.getColumnIndex("no")), c.getString(c.getColumnIndex("location_id")), c.getString(c.getColumnIndex("name_chs")),
                        c.getString(c.getColumnIndex("parent_id")), c.getString(c.getColumnIndex("lng")), c.getString(c.getColumnIndex("lat")),
                        c.getString(c.getColumnIndex("pp_name")), c.getString(c.getColumnIndex("parent_name")), c.getString(c.getColumnIndex("memo1")),
                        c.getString(c.getColumnIndex("source")));
                temp.add(data);
            } while (c.moveToNext());
        } catch (Exception ex) {
            Log.i("dbhelper", ex.toString());
        } finally {
            db.close();
        }
        return temp;
    }

    public ArrayList<Villages> getVillagsByTownship(String provinceId, int subIndex) {
        ArrayList<Villages> temp = new ArrayList<Villages>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        String id = provinceId.substring(0, subIndex);
        try {
            c = db.rawQuery("SELECT  * FROM Villages WHERE parent_id LIKE '" + id + "%'", null);
            c.moveToFirst();
            do {
                Villages data = new Villages(c.getInt(c.getColumnIndex("no")), c.getString(c.getColumnIndex("location_id")), c.getString(c.getColumnIndex("name_chs")),
                        c.getString(c.getColumnIndex("parent_id")), c.getString(c.getColumnIndex("lng")), c.getString(c.getColumnIndex("lat")),
                        c.getString(c.getColumnIndex("pp_name")), c.getString(c.getColumnIndex("parent_name")), c.getString(c.getColumnIndex("memo1")),
                        c.getString(c.getColumnIndex("source")));
                temp.add(data);
            } while (c.moveToNext());
        } catch (Exception ex) {
            Log.i("dbhelper", ex.toString());
        } finally {
            db.close();
        }
        return temp;
    }

    public ArrayList<Historys> getAllHistorys() {
        ArrayList<Historys> temp = new ArrayList<Historys>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        try {
            c = db.rawQuery("SELECT  * FROM historys", null);
            c.moveToFirst();
            do {
                Historys data = new Historys(c.getInt(c.getColumnIndex("no")), c.getString(c.getColumnIndex("region")));
                temp.add(data);
            } while (c.moveToNext());
        } catch (Exception ex) {
            Log.i("dbhelper", ex.toString());
        } finally {
            db.close();
        }
        return temp;
    }

    public ArrayList<HistoryGeographys> getHistoryGeographyByName(String name) {
        ArrayList<HistoryGeographys> temp = new ArrayList<HistoryGeographys>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        try {
            String sql = "select * from historygeographys where name_chs like '%" + name + "%' or memo like '%" + name + "%' UNION " +
                    "select 0 as no,name_chs,'' as province_name,'' as municipality_name,'' as county_name,'' as township_name,'' as village_name,'现代' as history,50 as history_id,source as memo from Provinces where name_chs like '%" + name + "%' or source like '%" + name + "%' UNION " +
                    "select 0 as no,name_chs,parent_name as province_name,'' as municipality_name,'' as county_name,'' as township_name,'' as village_name,'现代' as history,50 as history_id,memo1 as memo from municipalitys where name_chs like '%" + name + "%' or memo1 like '%" + name + "%' UNION " +
                    "select 0 as no,name_chs,pp_name as province_name,parent_name as municipality_name,'' as county_name,'' as township_name,'' as village_name,'现代' as history,50 as history_id,memo1 as memo from countys where name_chs like '%" + name + "%'  or memo1 like '%" + name + "%' UNION " +
                    "select 0 as no,name_chs,'' as province_name,pp_name as municipality_name,parent_name as county_name,'' as township_name,'' as village_name,'现代' as history,50 as history_id,memo1 as memo from townships where name_chs like '%" + name + "%'  or memo1 like '%" + name + "%'  UNION " +
                    "select 0 as no,name_chs,parent_name as province_name,'' as municipality_name,pp_name as county_name,parent_name as township_name,'' as village_name,'现代' as history,50 as history_id,memo1 as memo from villages where name_chs like '%" + name + "%'  or memo1 like '%" + name + "%' " +
                    "ORDER BY history_id";
            c = db.rawQuery(sql, null);
            c.moveToFirst();
            do {
                HistoryGeographys data = new HistoryGeographys(c.getInt(c.getColumnIndex("no")), c.getString(c.getColumnIndex("name_chs")),
                        c.getString(c.getColumnIndex("province_name")), c.getString(c.getColumnIndex("municipality_name")),
                        c.getString(c.getColumnIndex("county_name")), c.getString(c.getColumnIndex("township_name")),
                        c.getString(c.getColumnIndex("village_name")), c.getString(c.getColumnIndex("history")),
                        c.getInt(c.getColumnIndex("history_id")), c.getString(c.getColumnIndex("memo")));
                temp.add(data);
            } while (c.moveToNext());
        } catch (Exception ex) {
            Log.i("dbhelper", ex.toString());
        } finally {
            db.close();
        }
        return temp;
    }

    public HistoryGeographys getHistoryGeographyByNum(int num) {
        HistoryGeographys temp = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        String sql = "";
        if (0 > num) {
            sql = "SELECT  * FROM historygeographys ORDER BY no LIMIT 1";
        } else {
            sql = "SELECT  * FROM historygeographys WHERE no =" + Integer.toString(num) + " ORDER BY no LIMIT 1";
        }
        try {
            c = db.rawQuery(sql, null);
            c.moveToFirst();
            temp = new HistoryGeographys(c.getInt(c.getColumnIndex("no")), c.getString(c.getColumnIndex("name_chs")),
                    c.getString(c.getColumnIndex("province_name")), c.getString(c.getColumnIndex("municipality_name")),
                    c.getString(c.getColumnIndex("county_name")), c.getString(c.getColumnIndex("township_name")),
                    c.getString(c.getColumnIndex("village_name")), c.getString(c.getColumnIndex("history")),
                    c.getInt(c.getColumnIndex("history_id")), c.getString(c.getColumnIndex("memo")));

        } catch (Exception ex) {
            Log.i("dbhelper", ex.toString());
        } finally {
            db.close();
        }
        return temp;
    }

    public HistoryGeographys getHistoryGeographyByNo(String no) {
        HistoryGeographys temp = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        try {
            c = db.rawQuery("SELECT  * FROM historygeographys WHERE no=" + no, null);
            c.moveToFirst();
            temp = new HistoryGeographys(c.getInt(c.getColumnIndex("no")), c.getString(c.getColumnIndex("name_chs")),
                    c.getString(c.getColumnIndex("province_name")), c.getString(c.getColumnIndex("municipality_name")),
                    c.getString(c.getColumnIndex("county_name")), c.getString(c.getColumnIndex("township_name")),
                    c.getString(c.getColumnIndex("village_name")), c.getString(c.getColumnIndex("history")),
                    c.getInt(c.getColumnIndex("history_id")), c.getString(c.getColumnIndex("memo")));

        } catch (Exception ex) {
            Log.i("dbhelper", ex.toString());
        } finally {
            db.close();
        }
        return temp;
    }

    public boolean isExistTownshipName(String name, String parent_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        boolean result = false;
        String sql = "SELECT * FROM Townships WHERE parent_id='" + parent_id + "' AND name_chs='" + name + "' LIMIT 1";
        try {
            c = db.rawQuery(sql, null);
            if (null == c || !c.moveToFirst() || 0 == c.getInt(c.getColumnIndex("no")))
                result = false;
            else
                result = true;
        } catch (Exception ex) {
            Log.i("dbhelper", ex.toString());
        } finally {
            db.close();
        }
        return result;
    }

    public boolean isExisVillageNamet(String name, String parent_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        boolean result = false;
        String sql = "SELECT * FROM Villages WHERE parent_id='" + parent_id + "' AND name_chs='" + name + "' LIMIT 1";
        try {
            c = db.rawQuery(sql, null);
            if (null == c || !c.moveToFirst() || 0 == c.getInt(c.getColumnIndex("no")))
                result = false;
            else
                result = true;
        } catch (Exception ex) {
            Log.i("dbhelper", ex.toString());
        } finally {
            db.close();
        }
        return result;
    }

    public boolean InsertTable(String tableName, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            long i = db.insert(tableName, null, values);
            if (0 < i)
                return true;
            else
                return false;
        } catch (Exception ex) {
            Log.i("dbhelper", ex.toString());
            return false;
        } finally {
            db.close();
        }
    }

    public boolean DeleteTable(String tableName, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            long i = db.delete(tableName, whereClause, whereArgs);
            if (0 < i)
                return true;
            else
                return false;
        } catch (Exception ex) {
            Log.i("dbhelper", ex.toString());
            return false;
        } finally {
            db.close();
        }
    }

    public boolean UpdateTable(String tableName, ContentValues values, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            long i = db.update(tableName, values, whereClause, whereArgs);
            if (0 < i)
                return true;
            else
                return false;
        } catch (Exception ex) {
            Log.i("dbhelper", ex.toString());
            return false;
        } finally {
            db.close();
        }
    }

    public boolean deleteTable(String tableName, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            long i = db.delete(tableName, whereClause, whereArgs);
            if (0 < i)
                return true;
            else
                return false;
        } catch (Exception ex) {
            Log.i("dbhelper", ex.toString());
            return false;
        } finally {
            db.close();
        }
    }

    public Pair<String,String> getDatabasePath() {
        SQLiteDatabase db = this.getWritableDatabase();
        String path = "";
        String dbName="/StoryDB.db";
        String newPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + dbName;
        try {
            path = db.getPath();


        } catch (Exception ex) {
            Log.i("dbhelper", ex.toString());
            return new Pair<String,String>("","复制错误");
        } finally {
            db.close();
        }
        String result="";
        result=  copyFile(path, newPath);
       if(TextUtils.isEmpty( result))
        return new Pair<String,String>(path,newPath);
       else
        return new Pair<String,String>(path,result);
    }
    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径
     * @param newPath String 复制后路径
     * @return boolean
     */
    public static String copyFile(String oldPath, String newPath) {
        String resultStr = "";
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            File newfile = new File(newPath);
            if (!newfile.exists()) {
                newfile.createNewFile();
            }
            if (oldfile.exists()) { // 文件存在时
                InputStream inStream = new FileInputStream(oldPath); // 读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                return resultStr;
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
            resultStr=e.toString();
        }
        return  resultStr;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
