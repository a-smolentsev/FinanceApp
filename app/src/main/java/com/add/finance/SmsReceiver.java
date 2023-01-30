package com.add.finance;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class SmsReceiver extends BroadcastReceiver  {

    DBHelper dbHelper;
    SQLiteDatabase mDb;
    private String time = "";     // время
    private String money = "";    // количество(остаток) денег
    private String place = "";    // место покупки
    private String score = "";    // счёт
    private String category = "";    // категория

    @Override
    public void onReceive(Context context, Intent intent) {


        dbHelper = new DBHelper(context);

        mDb = dbHelper.getReadableDatabase();
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DBHelper.tableScheta, null);
        // Cursor cursor = mDb.rawQuery("SELECT * FROM add_summ", null);
        cursor.moveToNext();
        String name = cursor.getString(cursor.getColumnIndex(DBHelper.schet));
        cursor.close();

      Cursor cursor2 = mDb.rawQuery("SELECT * FROM " + DBHelper.tableCategAdd + " WHERE " + DBHelper.categoryprihod + "='Перевод'", null);
        // Cursor cursor = mDb.rawQuery("SELECT * FROM add_summ", null);
        cursor2.moveToNext();
        String categ = cursor2.getString(cursor2.getColumnIndex(DBHelper.categoryprihod));
        cursor2.close();

        Cursor cursor3 = mDb.rawQuery("SELECT * FROM " + DBHelper.tableсategRashod  + " WHERE " + DBHelper.rashod + "='Питание'", null);
        // Cursor cursor = mDb.rawQuery("SELECT * FROM add_summ", null);
        cursor3.moveToNext();
        String categPitanie = cursor3.getString(cursor3.getColumnIndex(DBHelper.rashod));
        cursor3.close();

        // если пришло входящие смс
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {

            // получаем пакет данных из намериния
            Bundle  bundle = intent.getExtras();

            if (bundle != null) {

                // извлекаем данные смс

                Object[] pdu_Objects = (Object[]) bundle.get("pdus");
                if (pdu_Objects != null) {

                    for (Object aObject : pdu_Objects) {

                        String format = bundle.getString("format");
                        SmsMessage currentSMS = SmsMessage.createFromPdu((byte[]) aObject, format);

                        // строка с текстом смс
                        String smsMessage = currentSMS.getDisplayMessageBody();

                        // входящий номер телефона
                        String phoneNumber = currentSMS.getDisplayOriginatingAddress();

                        // Получаем текущее время
                        Date currentDate = new Date();

                        // Форматирование времени как "день.месяц.год"
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String dateText = dateFormat.format(currentDate);

                        // Форматирование времени как "часы:минуты:секунды"
                        DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        String timeText = timeFormat.format(currentDate);

                        DateFormat timeFormatbd = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                        String timeTextbd = timeFormatbd.format(currentDate);

                        if (smsMessage != null) {

                            // если сообщение от номера 900
                            if (phoneNumber.equals("900")) {

                                // разделяем строку через разграничители(пробел)
                                String strGetTxt[] = smsMessage.split(" ");

                                // Рразмер массива
                                int size = strGetTxt.length;

                                if(size >= 6){


                                    // если строка из массива равна строке(покупка)
                                    boolean checkstart = false;
                                    if (strGetTxt[2].equals("Покупка") || strGetTxt[2].equals("Оплата")) {

                                        try {
                                            if (strGetTxt[4].equals("PYATEROCHKA") || strGetTxt[4].equals("LENTA")|| strGetTxt[4].equals("ZELENETSKIY")){
                                                category=categPitanie;
                                            }
                                            else
                                            {
                                                category=" ";
                                            }
                                            // получаем время из текста смс
                                            time = strGetTxt[1];
                                            // получаем количество денег из текста смс и удаляем символ "р"
                                            money = strGetTxt[3].replaceAll("р", "");
                                            // получаем место покупки из текста смс
                                            place = strGetTxt[4];

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        // добавляем запись в строку счёт
                                        score = name;
                                        checkstart = true;
                                        // записываем в в таблицу расходов
                                        dbHelper.insertNew(money, dateText + " " + time, category, score,
                                                place, dateText + " " + timeTextbd);

                                    } else if (strGetTxt[2].equals("Выдача")) {

                                        try {
                                            time = strGetTxt[1];
                                            money = strGetTxt[3].replaceAll("р", "");
                                            place = strGetTxt[4];
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        score = "Наличные";
                                        checkstart = true;
                                        dbHelper.insertNew(money, dateText + " " + time, "", score,
                                                place, dateText + " " + timeTextbd);

                                    } else if (strGetTxt[2].equals("перевод")) {

                                        try {  time = strGetTxt[1];}
                                        catch (Exception e) {
                                            e.printStackTrace();}


                                        if (strGetTxt[5].equals("комиссией")) {

                                            String resultone = strGetTxt[3].replaceAll("р", "");
                                            String resulttwo = strGetTxt[6].replaceAll("р", "");
                                            try {

                                                int sumone = Integer.parseInt(resultone);
                                                int sumtwo = Integer.parseInt(resulttwo);
                                                money = String.valueOf(sumone + sumtwo);

                                            } catch (NumberFormatException e) {
                                                e.printStackTrace();
                                            }

                                        } else {

                                            money = strGetTxt[3].replaceAll("р", "");
                                        }

                                        place = strGetTxt[2];
                                        score = name;
                                        category=categ;
                                        checkstart = true;
                                        dbHelper.insertNew(money, dateText + " " + time, category, score,
                                                place, dateText + " " + timeTextbd);

                                    } else if (strGetTxt[2].equals("мобильный")) {

                                        try {
                                            time = strGetTxt[1];
                                            place = strGetTxt[2] + " " + strGetTxt[3];
                                            money = strGetTxt[6].replaceAll("р", "");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        score = name;
                                        checkstart = true;
                                        dbHelper.insertNew(money, dateText + " " + time, "", score,
                                                place, dateText + " " + timeTextbd);

                                    } else if (strGetTxt[2].equals("зачисление")) {

                                        try {
                                            time = strGetTxt[1];
                                            money = strGetTxt[3].replaceAll("р", "");
                                            place = strGetTxt[4];
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        score = name;
                                        checkstart = true;
                                        dbHelper.insertData(money, dateText + " " + time, "", score,
                                                place, dateText + " " + timeTextbd);

                                    } else {

                                        if (strGetTxt[0].equals("Перевод")) {

                                            time = dateText + " " + timeText;
                                            try {
                                                money = strGetTxt[1].replaceAll("р", "");
                                                place = strGetTxt[3] + " " + strGetTxt[4];
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            score = name;
                                            category=categ;
                                            checkstart = true;
                                            dbHelper.insertData(money,  time,category, score,
                                                    place, dateText + " " + timeTextbd);
                                        }else{

                                            checkstart = false;
                                        }
                                    }

                                   if(checkstart) {

                                       // Отправляем уведомление
                                       Notyification( context,"Счёт: " + score, dateText + " " + time +
                                               " " + money + " " + score + " " + place);
                                       Intent startactivity = null;

                                       try {

                                           // если активити "launcher" запущено то перезагружаем для обновления данных
                                           if (((СheckStartActivity) context.getApplicationContext()).NameActivity().equals("finance")) {

                                               startactivity = new Intent(context, launcher.class);
                                               startactivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                               context.startActivity(startactivity);
                                           }
                                       } catch (Exception e) {
                                           e.printStackTrace();
                                       }
                                   }
                            }
                        }
                      }
                    }
                }
            }
        }
    }


    // Метод отправки уведомления
    private void Notyification( Context context,String tile,String text){

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder( context, "SMS_CHANNEL")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(tile)
                        .setContentText(text)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from( context);
        notificationManager.notify(200, builder.build());
    }




}