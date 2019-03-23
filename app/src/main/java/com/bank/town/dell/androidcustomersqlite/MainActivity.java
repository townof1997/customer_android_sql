package com.bank.town.dell.androidcustomersqlite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bank.town.dell.androidcustomersqlite.bean.User;
import com.bank.town.dell.androidcustomersqlite.sqlite.BaseDao;
import com.bank.town.dell.androidcustomersqlite.sqlite.BaseDaoFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click(View view) {

        BaseDao<User> userBaseDao = BaseDaoFactory.getOurInstance().getBaseDao(User.class);
        userBaseDao.insert(new User(1,"tt","32"));
        Toast.makeText(this, "执行成功", Toast.LENGTH_SHORT).show();
    }
}
