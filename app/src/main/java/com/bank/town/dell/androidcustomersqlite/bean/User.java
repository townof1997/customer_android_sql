package com.bank.town.dell.androidcustomersqlite.bean;

import com.bank.town.dell.androidcustomersqlite.sqlite.DbField;
import com.bank.town.dell.androidcustomersqlite.sqlite.DbTable;

/**
 * Created by dell on 2019/3/23.
 */
@DbTable("tb_user")
public class User {
    @DbField("_id")
    private Integer id;
    @DbField("_name")
    private String name;
    @DbField("_password")
    private String password;

    public User(Integer id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }
}
