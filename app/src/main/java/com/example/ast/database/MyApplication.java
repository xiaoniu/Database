package com.example.ast.database;

import android.app.Application;

import com.example.ast.database.greendao.gen.DaoMaster;
import com.example.ast.database.greendao.gen.DaoSession;

import org.greenrobot.greendao.database.Database;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by xiaoniu on 2017/6/29.
 */

public class MyApplication extends Application {

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"my-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

        Realm.init(this);
        RealmConfiguration config = new  RealmConfiguration.Builder()
                .name("myRealm.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
