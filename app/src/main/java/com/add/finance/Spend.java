package com.add.finance;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class Spend extends AppCompatActivity {
    private ListView userList;


    DBHelper mDBHelper;
    SQLiteDatabase mDb;

    Cursor userCursor;
    private SimpleCursorAdapter userAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spend);

        userList = (ListView) findViewById(R.id.list2);

        mDBHelper = new DBHelper(this);


        mDb = mDBHelper.getWritableDatabase();

        userList.invalidateViews();
        //Список клиентов
        final ArrayList<HashMap<String, Object>> clients1 = new ArrayList<HashMap<String, Object>>();

//Список параметров конкретного клиента
        HashMap<String, Object> client1;

//Отправляем запрос в БД
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DBHelper.table2 + " ORDER BY datetime(" + DBHelper.date2 + " ) DESC", null);
        // Cursor cursor = mDb.rawQuery("SELECT * FROM add_summ", null);
        cursor.moveToFirst();

//Пробегаем по всем клиентам
        while (!cursor.isAfterLast()) {
            client1 = new HashMap<String, Object>();

            //Заполняем клиента
            client1.put(DBHelper.summ2, cursor.getString(1));
            client1.put(DBHelper.date2, cursor.getString(2));
            client1.put(DBHelper.category2, cursor.getString(3));
            client1.put(DBHelper.account2, cursor.getString(4));
            client1.put(DBHelper.comment2, cursor.getString(5));
            //Закидываем клиента в список клиентов
            clients1.add(client1);

            //Переходим к следующему клиенту
            cursor.moveToNext();
        }
        cursor.close();

//Какие параметры клиента мы будем отображать в соответствующих
//элементах из разметки adapter_item.xml
        String[] from = {DBHelper.summ2, DBHelper.date2,DBHelper.category2,DBHelper.account2,DBHelper.comment2};
        int[] to = {R.id.total_text, R.id.deal_date_text,R.id.category_text,R.id.account_text,R.id.description_text};

//Создаем адаптер
        final SimpleAdapter adapter = new SimpleAdapter(this, clients1, R.layout.adapter_item1, from, to);


        userList.setAdapter(adapter);

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor res1 = mDBHelper.getPersonnelList1();
                //res.moveToFirst();
                res1.moveToPosition(position);
                Intent intent = new Intent(Spend.this, updates_rashod.class);
                intent.putExtra("id", res1.getInt(0));
                intent.putExtra("sum", res1.getString(1));
                intent.putExtra("date", res1.getString(2));
                intent.putExtra("cat", res1.getString(3));
                intent.putExtra("acc", res1.getString(4));
                intent.putExtra("comment", res1.getString(5));
                startActivity(intent);

            }


        });

    }
}
