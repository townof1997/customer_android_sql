package com.bank.town.dell.androidcustomersqlite.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by dell on 2019/3/23.
 */

public class BaseDao<T> implements IBaseDao<T> {
    //从操作数据库,持有数据库的引用
    private SQLiteDatabase sqLiteDatabase;
    //持有数据库所对应的java类型
    private Class<T> entityClass;
    //表名
    private String tableName;
    //标记，用来标识表是否已经存在
    private boolean isInit = false;
    //定义一个缓存空间（key-字段名，value-成员变量
    private HashMap<String, Field> cacheMap;
    public boolean init(SQLiteDatabase sqLiteDatabase, Class<T> entityClass) {
        this.sqLiteDatabase = sqLiteDatabase;
        this.entityClass = entityClass;
        //自动建表
        if (!isInit) {
            //如果没建表，就建表

            tableName=entityClass.getAnnotation(DbTable.class).value();

            if(!sqLiteDatabase.isOpen()) {
                return false;
            }
            //执行自动建表的动作
            String createTableSql = getCreateTableSql();
            sqLiteDatabase.execSQL(createTableSql);

            //初始化缓存
            cacheMap = new HashMap<>();
            initCacheMap();

            isInit = true;
        }


        return isInit;
    }

    private String getCreateTableSql() {
        //create table if not exists ta-user(_id integer,name varchar2(20),password varchar2(20))
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("create table if not exists ");
        stringBuffer.append(tableName + ")");
        //反射得到所有成员变量
        Field[] fields = entityClass.getDeclaredFields();
        for(Field field:fields) {
            Class type = field.getType();
            if(type == String.class) {
                stringBuffer.append(field.getAnnotation(DbField.class).value()+ " TEXT,");
            } else if (type == Integer.class) {
                stringBuffer.append(field.getAnnotation(DbField.class).value()+ " INTEGER,");
            } else if (type == Long.class) {
                stringBuffer.append(field.getAnnotation(DbField.class).value()+ " BIGINT,");
            } else if (type == Double.class) {
                stringBuffer.append(field.getAnnotation(DbField.class).value()+ " DOUBLE,");
            } else if (type == byte[].class) {
                stringBuffer.append(field.getAnnotation(DbField.class).value()+ " BLOB,");
            } else {//if (type == Integer.class)
                //不支持的类型
                continue;
            }
        }
        if(stringBuffer.charAt(stringBuffer.length()-1) == ',') {
            stringBuffer.deleteCharAt(stringBuffer.length()-1);
        }
        stringBuffer.append(")");
        return stringBuffer.toString();
    }

    private void initCacheMap(){
        //1,取所有的列名 == （差空表）
        String sql = "select * from " + tableName + "limit 1,0";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        String[] columnNames = cursor.getColumnNames();
        //2,取所有的成员变量(反射）
        Field[] columnFields = entityClass.getDeclaredFields();
        //3,进行列名和成员变量的映射，存入到缓存中
        for (String columnName:columnNames) {
            Field resultField = null;
            for (Field field: columnFields) {
                String fieldAnnotionName = field.getAnnotation(DbField.class).value();
                if(columnNames.equals(fieldAnnotionName)) {
                    resultField = field;
                    break;
                }
            }
            if (resultField!=null) {
                cacheMap.put(columnName, resultField);
            }
        }
    }

    @Override
    public long insert(T entity) {
        //字段名+值
        Map<String,String> map = getValues(entity);
        //设置插入内容

        ContentValues values = getContentValues(map);
        //执行插入
        long result = sqLiteDatabase.insert(tableName,null,values);

        return result;
    }

    private Map<String,String> getValues(T entity) {
        HashMap<String,String> map = new HashMap<>();
        Iterator<Field> fieldIterator = cacheMap.values().iterator();
        while (fieldIterator.hasNext()) {
            Field field = fieldIterator.next();
            field.setAccessible(true);
            //获取成员变量的值
            try {
                Object object = field.get(entity);
                if(object == null) {
                    continue;
                }
                String value = object.toString();
                //获取列名
                String key= field.getAnnotation(DbField.class).value();
                if(!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                    map.put(key,value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }


        return map;
    }

    private ContentValues getContentValues(Map<String,String> map) {
        ContentValues contentValues = new ContentValues();
        Set keys = map.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = map.get(key);
            if (value!= null) {
                contentValues.put(key,value);
            }
        }







        return contentValues;
    }
}
