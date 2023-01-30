/*
 * created by Andrew K. <rembozebest@gmail.com> on 18.05.20 19:11
 * copyright (c) 2020
 * last modified 18.05.20 19:11 with ❤
 */

package com.add.finance;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class adventFragment extends Fragment {


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    public adventFragment() {
        // Required empty public constructor
    }

    private ListView userList;
    DBHelper mDBHelper;
    SQLiteDatabase mDb;
    private Context mContext;
    Cursor userCursor;
    private SimpleCursorAdapter userAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View View =  inflater.inflate(R.layout.fragment_advent, container, false);
        userList = (ListView) View.findViewById(R.id.list);
        mDBHelper = new DBHelper(mContext);


        mDb = mDBHelper.getWritableDatabase();

        userList.invalidateViews();
//Список клиентов
        final ArrayList<HashMap<String, Object>> clients = new ArrayList<HashMap<String, Object>>();

//Список параметров конкретного клиента
        HashMap<String, Object> client;

//Отправляем запрос в БД
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DBHelper.add_summ + " ORDER BY datetime(" + DBHelper.dateSort1 + ") DESC", null);
        // Cursor cursor = mDb.rawQuery("SELECT * FROM add_summ", null);
        cursor.moveToFirst();

//Пробегаем по всем клиентам
        while (!cursor.isAfterLast()) {
            client = new HashMap<String, Object>();

            //Заполняем клиента
            client.put(DBHelper.summ1, cursor.getString(1));
            client.put(DBHelper.date1, cursor.getString(2));

            client.put(DBHelper.category1, cursor.getString(3));
            client.put(DBHelper.account1, cursor.getString(4));
            client.put(DBHelper.comment1, cursor.getString(5));
            //Закидываем клиента в список клиентов
            clients.add(client);

            //Переходим к следующему клиенту
            cursor.moveToNext();
        }
        cursor.close();

//Какие параметры клиента мы будем отображать в соответствующих
//элементах из разметки adapter_item.xml
        String[] from = {DBHelper.summ1, DBHelper.date1, DBHelper.category1, DBHelper.account1, DBHelper.comment1};
        int[] to = {R.id.total_text, R.id.deal_date_text,R.id.category_text,R.id.account_text,R.id.description_text};

//Создаем адаптер
        final SimpleAdapter adapter = new SimpleAdapter(mContext, clients, R.layout.adapter_item, from, to);


        userList.setAdapter(adapter);

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor res = mDBHelper.getPersonnelList();
                //res.moveToFirst();
                res.moveToPosition(position);
                Intent intent = new Intent(mContext, updates.class);
                intent.putExtra("id", res.getInt(0));
                intent.putExtra("sum", res.getString(1));
                intent.putExtra("date", res.getString(2));
                intent.putExtra("cat", res.getString(3));
                intent.putExtra("acc", res.getString(4));
                intent.putExtra("comment", res.getString(5));
                startActivity(intent);

            }


        });

        return View;
    }

}
