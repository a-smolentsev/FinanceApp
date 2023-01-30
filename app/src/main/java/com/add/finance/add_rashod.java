package com.add.finance;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class add_rashod extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{
    DBHelper dbHelper;
    SQLiteDatabase mDb;

    private EditText sum;


    private EditText comment;
    private EditText data;
    private EditText time;

    private EditText add_date;
    private EditText chooseTime;
    private TimePickerDialog timePickerDialog;
    private Calendar calendar;
    private int currentHour;
    private int currentMinute;
    private Spinner categ;
    private  Spinner acc;



    int mYearIni, mMonthIni, mDayIni, sYearIni, sMonthIni, sDayIni, hour,minute,hourFinal,minuteFinal;
    String symbnull,symbnull2,symbnull3,symbnull4;
    static final int DATE_ID = 0;

    Calendar C = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rashod);
        this.setTitle("Добавить расход");
        sMonthIni = C.get(Calendar.MONTH);
        sDayIni = C.get(Calendar.DAY_OF_MONTH);
        sYearIni = C.get(Calendar.YEAR);
        add_date = (EditText) findViewById(R.id.data);

        // Установка даты и времени
        add_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                mYearIni= calendar.get(Calendar.YEAR);
                mMonthIni= calendar.get(Calendar.MONTH);
                mDayIni= calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(add_rashod.this,add_rashod.this,mYearIni,mMonthIni,mDayIni);
                datePickerDialog.show();

            }
        });



        //определеяем текующую дату и время
        String date_n = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentTime = sdf.format(new Date());


        dbHelper = new DBHelper(this);
        //Находим кнопки
        sum = (EditText) findViewById(R.id.rashod);
        comment = (EditText) findViewById(R.id.comm);
        data = (EditText) findViewById(R.id.data);
        categ = (Spinner) findViewById(R.id.categ);
        acc = (Spinner) findViewById(R.id.account);

        data.setText(date_n + " " + currentTime);
        //time.setText(currentTime);
        sum.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(7,2)});

        categ.setOnItemSelectedListener(this);

        // Loading spinner data from database

        loadSpinnerData3();

        acc.setOnItemSelectedListener(this);

        // Loading spinner data from database

        loadSpinnerData2();


    }

    public void onClick(View a) {


        // Получаем текущее время
        Date currentDate = new Date();
        java.text.DateFormat timeFormatbd = new SimpleDateFormat("ss", Locale.getDefault());
        String timeTextbd = timeFormatbd.format(currentDate);

        dbHelper.insertNew(sum.getText().toString(),
                data.getText().toString(),
                categ.getSelectedItem().toString(),
                acc.getSelectedItem().toString(),
                comment.getText().toString(),data.getText().toString()+ ":" +timeTextbd);


        Intent intent = new Intent(getApplicationContext(),
                launcher.class);
        startActivityForResult(intent, 0);

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        sYearIni= year;
        sMonthIni = month+1;
        sDayIni = dayOfMonth;

        Calendar c = Calendar.getInstance();
        hour =c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog (add_rashod.this,add_rashod.this,hour,minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        hourFinal = hourOfDay;
        minuteFinal = minute;
        if (sMonthIni>=10) {
            symbnull = "";
        }
        else
        {
            symbnull = "0";
        }

        if (sDayIni>=10) {
            symbnull2 = "";
        }
        else
        {
            symbnull2 = "0";
        }

        if (hourFinal>=10) {
            symbnull3 = "";
        }
        else
        {
            symbnull3 = "0";
        }

        if (minuteFinal>=10) {
            symbnull4 = "";
        }
        else
        {
            symbnull4 = "0";
        }





        data.setText(sYearIni + "-" + symbnull +sMonthIni +"-"+ symbnull2 + sDayIni + " " + symbnull3 + hourFinal + ":" + symbnull4 + minuteFinal);
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

    private void loadSpinnerData2() {
        DBHelper db = new DBHelper(this);
        List<String> labels = db.getAllLabels2();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        acc.setAdapter(dataAdapter);
    }

    private void loadSpinnerData3() {
        DBHelper db = new DBHelper(this);
        List<String> labels = db.getAllLabels3();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        categ.setAdapter(dataAdapter);
    }


    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // On selecting a spinner item
        String label = parent.getItemAtPosition(position).toString();



    }


    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

}
