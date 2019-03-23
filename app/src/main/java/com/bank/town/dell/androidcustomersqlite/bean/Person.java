package com.bank.town.dell.androidcustomersqlite.bean;

import com.bank.town.dell.androidcustomersqlite.sqlite.DbField;
import com.bank.town.dell.androidcustomersqlite.sqlite.DbTable;

/**
 * Created by dell on 2019/3/23.
 */

@DbTable("tb_person")
public class Person {
    @DbField("name")
    String name;
    @DbField("age")
    Integer age;

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
