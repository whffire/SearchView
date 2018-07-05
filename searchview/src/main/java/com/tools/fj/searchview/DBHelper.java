package com.tools.fj.searchview;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @name 工程名：yaosuwang
 * @class 包名：com.yaosuce.yaosuwang.util
 * @describe 描述：
 * @anthor 作者：whffi QQ:84569945
 * @time 时间：2017/6/14 23:21
 * @change 变更：
 * @chang 时间：
 * @class 描述：
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "SearchView.db"; //
    private static final int version = 1; //

    //
    public DBHelper(Context context, String string, Object object, int i) {
        super(context, DB_NAME, null, version);

    }

    public DBHelper(Context context) {
        super(context, DB_NAME, null, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //搜索记录
        db.execSQL("create table records(id integer primary key autoincrement,name varchar(200),pinyin text,keyword text)");


//        db.execSQL("insert into ztgl(ztname,ztid) values ('测试1','1')");
//        db.execSQL("insert into ztgl(ztname,ztid) values ('测试2','2')");
        // 若不是第一个版本安装，直接执行数据库升级
        // 请不要修改FIRST_DATABASE_VERSION的值，其为第一个数据库版本大小
        final int FIRST_DATABASE_VERSION = 1;
        onUpgrade(db, FIRST_DATABASE_VERSION, version);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 使用for实现跨版本升级数据库
        Log.i("CREATE TABLE", "CREATE TABLE");
        for (int i = oldVersion; i < newVersion; i++) {
            switch (i) {
                case 1:
                 //   upgradeToVersion2(db);
                    break;
                case 2:
                //    upgradeToVersion3(db);
                    break;
                default:
                    break;
            }
        }

    }
    private void upgradeToVersion2(SQLiteDatabase db) {}

    private void upgradeToVersion3(SQLiteDatabase db) {

    }
}
