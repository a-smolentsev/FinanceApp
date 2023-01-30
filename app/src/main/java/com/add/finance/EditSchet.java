package com.add.finance;

import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class EditSchet extends AppCompatActivity {
    private ListView userList;


    DBHelper mDBHelper;
    SQLiteDatabase mDb;

    Cursor userCursor;
    private SimpleCursorAdapter userAdapter;
    int delSchet;
    Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_schet);

        userList = (ListView) findViewById(R.id.listSchet);

        mDBHelper = new DBHelper(this);


        mDb = mDBHelper.getWritableDatabase();

        userList.invalidateViews();
        //Список клиентов
        final ArrayList<HashMap<String, Object>> clients1 = new ArrayList<HashMap<String, Object>>();

        HashMap<String, Object> client1;

//Отправляем запрос в БД
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DBHelper.tableScheta, null);
        // Cursor cursor = mDb.rawQuery("SELECT * FROM add_summ", null);
        cursor.moveToFirst();

//Пробегаем по всем клиентам
        while (!cursor.isAfterLast()) {
            client1 = new HashMap<String, Object>();

            //Заполняем клиента
            client1.put(DBHelper.schet, cursor.getString(1));

            //Закидываем клиента в список клиентов
            clients1.add(client1);

            //Переходим к следующему клиенту
            cursor.moveToNext();
        }
        cursor.close();

//Какие параметры клиента мы будем отображать в соответствующих
//элементах из разметки adapter_item.xml
        String[] from = {DBHelper.schet};
        int[] to = {R.id.category_text};

        //кнопка добавления категории
        btn_add= (Button) findViewById(R.id.btnAdd);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAdd();

            }
        });



//Создаем адаптер
        final SimpleAdapter adapter = new SimpleAdapter(this, clients1, R.layout.adapter_schet, from, to);


        userList.setAdapter(adapter);

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor res1 = mDBHelper.getPersonneSchet();
                //res.moveToFirst();
                res1.moveToPosition(position);
                delSchet=res1.getInt(0);
                showDialogDel1();

            }


        });

    }

    void showDialogDel1 ()
    {
        final Dialog dialog = new Dialog(EditSchet.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_delete1);
        final Button delete = dialog.findViewById(R.id.del);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDBHelper.delete_schet(delSchet);
                dialog.cancel();
                recreate();

            }
        });
        dialog.show();
    }

    void showDialogAdd ()
    {
        final Dialog dialog = new Dialog(EditSchet.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_schet);
        final Button add = dialog.findViewById(R.id.add_categ);
        final EditText addText = dialog.findViewById(R.id.addText);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDBHelper.insertLabelSchet(mDb,addText.getText().toString());
                dialog.cancel();
                recreate();
            }
        });
        dialog.show();
    }


}
