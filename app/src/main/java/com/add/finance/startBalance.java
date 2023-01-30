/*
 * created by Andrew K. <rembozebest@gmail.com> on 22.05.20 16:57
 * copyright (c) 2020
 * last modified 22.05.20 16:57 with ❤
 */

package com.add.finance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class startBalance extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{
    DBHelper dbHelper;
    SQLiteDatabase mDb;
    Spinner spinner;
    EditText summ;
    Button edit;
    ListView userList;
    int databaseId;
   SimpleAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_balance);

        dbHelper = new DBHelper(this);
        mDb = dbHelper.getWritableDatabase();
        spinner = (Spinner) findViewById(R.id.accounts);
        summ =(EditText) findViewById(R.id.summ);
        edit=(Button) findViewById(R.id.editbalance);
        userList=(ListView) findViewById(R.id.list);

        summ.setFilters(new InputFilter[] {new startBalance.DecimalDigitsInputFilter(7,2)});

        spinner.setOnItemSelectedListener(this);
        loadSpinnerData2();

        userList.invalidateViews();
        //Список клиентов
        final ArrayList<HashMap<String, Object>> clients1 = new ArrayList<HashMap<String, Object>>();

//Список параметров конкретного клиента
        HashMap<String, Object> client1;

//Отправляем запрос в БД
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DBHelper.startBalance , null);
        // Cursor cursor = mDb.rawQuery("SELECT * FROM add_summ", null);
        cursor.moveToFirst();

//Пробегаем по всем клиентам
        while (!cursor.isAfterLast()) {
            client1 = new HashMap<String, Object>();

            //Заполняем клиента
            client1.put(DBHelper.schet_balance, cursor.getString(1));
            client1.put(DBHelper.sum_balance, cursor.getString(2));
            //Закидываем клиента в список клиентов
            clients1.add(client1);

            //Переходим к следующему клиенту
            cursor.moveToNext();
        }
        cursor.close();

//Какие параметры клиента мы будем отображать в соответствующих
//элементах из разметки adapter_item.xml
        String[] from = {DBHelper.schet_balance, DBHelper.sum_balance};
        int[] to = {R.id.schet_balance1, R.id.summa};

//Создаем адаптер
       adapter = new SimpleAdapter(this, clients1, R.layout.adapter_balance, from, to);


        userList.setAdapter(adapter);




    }

    public void onClickEdit (View V){
        String balance;
        int id;

        balance=spinner.getSelectedItem().toString();



        if(dbHelper.columnExists(balance)==true){
            dbHelper.updateBalance(spinner.getSelectedItem().toString(),summ.getText().toString(),balance);
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        }
        else {
            dbHelper.insertBalance(mDb,spinner.getSelectedItem().toString(),
                    summ.getText().toString());
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);

        }


    }

    private void loadSpinnerData2() {
        DBHelper db = new DBHelper(this);
        List<String> labels = db.getAllLabels2();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }


    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // On selecting a spinner item

        String label = parent.getItemAtPosition(position).toString();



    }


    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    public class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern;

        public DecimalDigitsInputFilter(int digitsBeforeZero,int digitsAfterZero) {
            mPattern= Pattern.compile("[0-9]{0," + (digitsBeforeZero-1) + "}+((\\.[0-9]{0," + (digitsAfterZero-1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Matcher matcher=mPattern.matcher(dest);
            if(!matcher.matches())
                return "";
            return null;
        }

    }

}
