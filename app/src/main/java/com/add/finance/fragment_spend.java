package com.add.finance;/*
 * created by Andrew K. <rembozebest@gmail.com> on 18.05.20 15:59
 * copyright (c) 2020
 * last modified 18.05.20 15:59 with ❤
 */


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.add.finance.R;

import java.util.ArrayList;
import java.util.HashMap;

public class fragment_spend extends Fragment {
    private ListView userList1;
    DBHelper mDBHelper;
    SQLiteDatabase mDb;
    private Context mContext;
    Cursor userCursor;
    private SimpleCursorAdapter userAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spend2, container, false);
        userList1 = (ListView) view.findViewById(R.id.list2);

        mDBHelper = new DBHelper(mContext);


        mDb = mDBHelper.getWritableDatabase();

        userList1.invalidateViews();
        //Список клиентов
        final ArrayList<HashMap<String, Object>> clients1 = new ArrayList<HashMap<String, Object>>();

//Список параметров конкретного клиента
        HashMap<String, Object> client1;

//Отправляем запрос в БД
        Cursor cursor1 = mDb.rawQuery("SELECT * FROM " + DBHelper.table2 + " ORDER BY datetime(" + DBHelper.dateSort2 + " ) DESC", null);
        // Cursor cursor = mDb.rawQuery("SELECT * FROM add_summ", null);
        cursor1.moveToFirst();

//Пробегаем по всем клиентам
        while (!cursor1.isAfterLast()) {
            client1 = new HashMap<String, Object>();

            //Заполняем клиента
            client1.put(DBHelper.summ2, cursor1.getString(1));
            client1.put(DBHelper.date2, cursor1.getString(2));
            client1.put(DBHelper.category2, cursor1.getString(3));
            client1.put(DBHelper.account2, cursor1.getString(4));
            client1.put(DBHelper.comment2, cursor1.getString(5));
            //Закидываем клиента в список клиентов
            clients1.add(client1);

            //Переходим к следующему клиенту
            cursor1.moveToNext();
        }
        cursor1.close();

//Какие параметры клиента мы будем отображать в соответствующих
//элементах из разметки adapter_item1.xml

        String[] from1 = {DBHelper.summ2, DBHelper.date2,DBHelper.category2,DBHelper.account2,DBHelper.comment2};
        int[] to1 = {R.id.total_text, R.id.deal_date_text,R.id.category_text,R.id.account_text,R.id.description_text};

//Создаем адаптер
        final SimpleAdapter adapter1 = new SimpleAdapter(mContext, clients1, R.layout.adapter_item1, from1, to1);


        userList1.setAdapter(adapter1);

        // Клик по списку для редактирования операции

        userList1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor res1 = mDBHelper.getPersonnelList1();
                //res.moveToFirst();
                res1.moveToPosition(position);
                Intent intent = new Intent(mContext, updates_rashod.class);
                intent.putExtra("id", res1.getInt(0));
                intent.putExtra("sum", res1.getString(1));
                intent.putExtra("date", res1.getString(2));
                intent.putExtra("cat", res1.getString(3));
                intent.putExtra("acc", res1.getString(4));
                intent.putExtra("comment", res1.getString(5));
                startActivity(intent);

            }


        });

        return view;
    }
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

}

