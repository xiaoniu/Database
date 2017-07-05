package com.example.ast.database;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.ast.database.greendao.gen.DaoSession;
import com.example.ast.database.greendao.gen.UserDao;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;


/**
 * Created by xiaoniu on 2017/6/29.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "tiaoshi";

    private Button btnInsert;
    private Button btnDelete;
    private Button btnUpdate;
    private Button btnQuery;
    private RadioButton radioGreendao;
    private RadioButton radioRealm;
    private RadioButton radio1;
    private RadioButton radio10;
    private RadioButton radio100;
    private RadioButton radio1000;
    private TextView textView;
    private ListView listView;

    private int select = 1;
    private int selectNum = 1;

    private UserDao mUserDao;
    private Realm realm;

    private List<User> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initComponent();

        DaoSession daoSession = ((MyApplication) getApplication()).getDaoSession();
        mUserDao = daoSession.getUserDao();

        realm = Realm.getDefaultInstance();

        list = mUserDao.loadAll();
        listView.setAdapter(new MyBaseAdapter(this, list));
    }

    private void initView() {
        btnInsert = (Button) findViewById(R.id.button_insert);
        btnDelete = (Button) findViewById(R.id.button_delete);
        btnUpdate = (Button) findViewById(R.id.button_update);
        btnQuery = (Button) findViewById(R.id.button_query);

        radioGreendao = (RadioButton) findViewById(R.id.radio_greendao);
        radioRealm = (RadioButton) findViewById(R.id.radio_realm);
        radio1 = (RadioButton) findViewById(R.id.radio_1);
        radio10 = (RadioButton) findViewById(R.id.radio_10);
        radio100 = (RadioButton) findViewById(R.id.radio_100);
        radio1000 = (RadioButton) findViewById(R.id.radio_1000);

        textView = (TextView) findViewById(R.id.textView);
        listView = (ListView) findViewById(R.id.listView);
    }

    private void initComponent() {
        btnInsert.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnQuery.setOnClickListener(this);

        radioGreendao.setOnCheckedChangeListener(this);
        radioRealm.setOnCheckedChangeListener(this);
        radio1.setOnCheckedChangeListener(this);
        radio10.setOnCheckedChangeListener(this);
        radio100.setOnCheckedChangeListener(this);
        radio1000.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            switch (compoundButton.getId()) {
                case R.id.radio_greendao:
                    select = 1;
                    break;
                case R.id.radio_realm:
                    select = 2;
                    break;
                case R.id.radio_1:
                    selectNum = 1;
                    break;
                case R.id.radio_10:
                    selectNum = 10;
                    break;
                case R.id.radio_100:
                    selectNum = 100;
                    break;
                case R.id.radio_1000:
                    selectNum = 1000;
                    break;
            }
            Log.i(TAG, "select = " + select);
            Log.i(TAG, "selectNum = " + selectNum);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_insert:
                insert();
                break;
            case R.id.button_delete:
                delete();
                break;
            case R.id.button_update:
                update();
                break;
            case R.id.button_query:
                query();
                break;
        }

    }

    private void insert() {
        switch (select) {
            case 1:
                greenDaoInsert();
                break;
            case 2:
                realmInsert();
                break;
        }
    }

    private void greenDaoInsert() {
        mUserDao.deleteAll();
        List<User> listBuf = new ArrayList<>();
        for (int i = 1; i <= selectNum; i++) {
            User user = new User();
            user.setAge(20);
            user.setName("GreenDAO" + i);
            listBuf.add(user);
        }
        long satrtTime = System.currentTimeMillis();
        for (int i = 0; i < selectNum; i++) {
            mUserDao.insert(listBuf.get(i));
            mUserDao.insertOrReplaceInTx();
        }
        textView.setText("GreenDao添加" + selectNum + "条数据花了" + (System.currentTimeMillis() - satrtTime) + "毫秒");

        list.clear();
        list = mUserDao.loadAll();
        listView.setAdapter(new MyBaseAdapter(this, list));
    }

    private void realmInsert() {
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();

        List<User2> listBuf = new ArrayList<>();
        for (int i = 1; i <= selectNum; i++) {
            User2 user = new User2();
            user.setAge(20);
            user.setName("Realm" + i);
            user.setId(i + "");
            listBuf.add(user);
        }
        long satrtTime = System.currentTimeMillis();
        realm.beginTransaction();
        for (int i = 0; i < selectNum; i++) {
            realm.insert(listBuf.get(i));
        }
        realm.commitTransaction();
        textView.setText("Realm添加" + selectNum + "条数据花了" + (System.currentTimeMillis() - satrtTime) + "毫秒");

        List<User2> list2 = new ArrayList<>();
        list2.clear();
        list2 = realm.where(User2.class).findAll();
        listView.setAdapter(new MyBaseAdapter2(this, list2));
    }

    private void delete() {
        switch (select) {
            case 1:
                greenDaoDelete();
                break;
            case 2:
                realmDelete();
                break;
        }
    }

    //deleteAll方法是清空数据表，不论多少条数据都是10秒左右，不具参考性，这里逐条删除
    private void greenDaoDelete() {
        if (mUserDao.count() != selectNum) {
            textView.setText("请选择[" + mUserDao.count() + "条]进行测试");
        } else {
//            mUserDao.deleteAll();
            List<User> listBuf = mUserDao.queryBuilder()
                    .where(UserDao.Properties.Age.isNotNull())
                    .list();
            long satrtTime = System.currentTimeMillis();
            for (int i = 0; i < selectNum; i++) {
                mUserDao.delete(listBuf.get(i));
            }
            textView.setText("GreenDao删除" + selectNum + "条数据花了" + (System.currentTimeMillis() - satrtTime) + "毫秒");

            list.clear();
            list = mUserDao.loadAll();
            listView.setAdapter(new MyBaseAdapter(this, list));
        }
    }

    private void realmDelete() {
        if (realm.where(User2.class).count() != selectNum) {
            textView.setText("请选择[" + realm.where(User2.class).count() + "条]进行测试");
        } else {
            List<User2> users = realm.where(User2.class).findAll();
            long satrtTime = System.currentTimeMillis();
            realm.beginTransaction();
            /**
             * 刚开始我这样删除数据，但是抛了out of range异常，后面发现list删了一条后，
             * 后面的会补上去，所以当删到中间时，后面的都补到前面了，就会报错了。
             * 所以要么一直删第一条，要么倒着删。
             */
//            for (int i = 0; i < selectNum; i++) {
//                users.get(i).deleteFromRealm();
//            }
            for (int i = 0; i < selectNum; i++) {
                users.get(0).deleteFromRealm();
            }
            realm.commitTransaction();
            textView.setText("Realm删除" + selectNum + "条数据花了" + (System.currentTimeMillis() - satrtTime) + "毫秒");

            List<User2> list2 = new ArrayList<>();
            list2.clear();
            list2 = realm.where(User2.class).findAll();
            listView.setAdapter(new MyBaseAdapter2(this, list2));
        }
    }

    private void update() {
        switch (select) {
            case 1:
                greenDaoUpdate();
                break;
            case 2:
                realmUpdate();
                break;
        }
    }

    private void greenDaoUpdate() {
        if (mUserDao.count() != selectNum) {
            textView.setText("请选择[" + mUserDao.count() + "条]进行测试");
        } else {
            List<User> listBuf = mUserDao.queryBuilder()
                    .where(UserDao.Properties.Age.isNotNull())
                    .list();
            for (int i = 0; i < listBuf.size(); i++) {
                listBuf.get(i).setName("新GreenDAO");
            }
            long satrtTime = System.currentTimeMillis();
            for (int i = 0; i < selectNum; i++) {
                mUserDao.update(listBuf.get(i));
            }
            textView.setText("GreenDao改动" + selectNum + "条数据花了" + (System.currentTimeMillis() - satrtTime) + "毫秒");

            list.clear();
            list = mUserDao.loadAll();
            listView.setAdapter(new MyBaseAdapter(this, list));
        }
    }

    private void realmUpdate() {
        if (realm.where(User2.class).count() != selectNum) {
            textView.setText("请选择[" + realm.where(User2.class).count() + "条]进行测试");
        } else {
            List<User2> users = realm.where(User2.class).findAll();
            long satrtTime = System.currentTimeMillis();
            realm.beginTransaction();
            for (int i = 0; i < selectNum; i++) {
                users.get(i).setName("新Realm");
            }
            realm.commitTransaction();
            textView.setText("Realm更改" + selectNum + "条数据花了" + (System.currentTimeMillis() - satrtTime) + "毫秒");

            List<User2> list2 = new ArrayList<>();
            list2.clear();
            list2 = realm.where(User2.class).findAll();
            listView.setAdapter(new MyBaseAdapter2(this, list2));
        }
    }

    private void query() {
        switch (select) {
            case 1:
                greenDaoQuery();
                break;
            case 2:
                realmQuery();
                break;
        }
    }

    private void greenDaoQuery() {
        if (mUserDao.count() < selectNum) {
            textView.setText("请选择[" + mUserDao.count() + "条]进行测试");
        } else {
            long satrtTime = System.currentTimeMillis();
            List<User> listBuf = mUserDao.queryBuilder()
                    .where(UserDao.Properties.Age.eq(20))
                    .limit(selectNum)
                    .list();
            textView.setText("GreenDao查询" + selectNum + "条数据花了" + (System.currentTimeMillis() - satrtTime) + "毫秒");
            list.clear();
            list = listBuf;
            listView.setAdapter(new MyBaseAdapter(this, list));
        }
    }

    private void realmQuery() {
        if (realm.where(User2.class).count() != selectNum) {
            textView.setText("请选择[" + realm.where(User2.class).count() + "条]进行测试");
        } else {
            List<User2> list2 = new ArrayList<>();
            list2.clear();
            long satrtTime = System.currentTimeMillis();
            list2 = realm.where(User2.class).equalTo("age",20).findAll();
            textView.setText("Realm查询" + selectNum + "条数据花了" + (System.currentTimeMillis() - satrtTime) + "毫秒");
            listView.setAdapter(new MyBaseAdapter2(this, list2));
        }
    }
}


