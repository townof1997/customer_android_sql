package com.bank.town.dell.androidcustomersqlite.sqlite;

/**
 * Created by dell on 2019/3/23.
 * 整个操作的顶层接口
 */

public interface IBaseDao<T> {
    /*
    * 插入操作
    * */
    long insert(T entity);
    //......

}
