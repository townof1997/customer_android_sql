package com.bank.town.dell.androidcustomersqlite.sqlite;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by dell on 2019/3/23.
 */

public class BaseDaoFactory {
    private static final BaseDaoFactory ourInstance = new BaseDaoFactory();
    public static  BaseDaoFactory getOurInstance() {
        return ourInstance;
    }
    private SQLiteDatabase sqLiteDatabase;
    private String sqliteDateBasePath;

    public BaseDaoFactory() {
        if(android.os.Build.VERSION.SDK_INT >= 4.2){
//            sqliteDateBasePath= context.getApplicationInfo().dataDir+"/databases/town.db";
            sqliteDateBasePath= "com.bank.town.dell.androidcustomersqlite"+"/databases/town.db";
        } else {
//            sqliteDateBasePath="/data/data/"+ context.getPackageName()+"/databases/town.db";
            sqliteDateBasePath="/data/data/"+ "com.bank.town.dell.androidcustomersqlite"+"/databases/town.db";
        }
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(sqliteDateBasePath,null);
        //https://blog.csdn.net/u010228448/article/details/52789217
    }

    public <T> BaseDao<T> getBaseDao(Class<T> entityClass) {
        BaseDao baseDao=null;
        try {
            baseDao =BaseDao.class.newInstance();
            baseDao.init(sqLiteDatabase,entityClass);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return baseDao;
    }
}
