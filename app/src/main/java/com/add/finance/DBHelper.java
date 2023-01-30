package com.add.finance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.add.finance.data.CategoryStat;
import com.add.finance.data.FinanceOperation;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBHelper";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "contacts";

    public static final String add_summ = "contacts"; // Таблица расходов
    public static final String KEY_ID = "_id"; // id
    public static final String summ1 = "summ"; // сумма
    public static final String date1 = "date"; // дата в виде даты и времени
    public static final String dateSort1 = "datesort"; // дата в виде даты и времени для сортировки
    public static final String category1 = "category"; // категория
    public static final String account1 = "account"; // счет
    public static final String comment1 = "comment"; // комментарий

    public static final String table2 = "cons"; // таблица доходов
    public static final String id2 = "_id2";  // id
    public static final String summ2 = "summa"; // сумма дохода
    public static final String date2 = "data"; // дата в виде даты и времени
    public static final String dateSort2 = "datesortt"; // дата в виде даты и времени для сортировки
    public static final String category2 = "categor"; // категория
    public static final String account2 = "accounts"; // счет
    public static final String comment2 = "comments"; // комментарий

    public static final String tableCategAdd = "prihod";
    public static final String idCateg = "_id3";
    public static final String categoryprihod = "category";


    public static final String tableScheta = "scheta";
    public static final String schet = "schet";
    public static final String idSchet = "_id3";

    public static final String accountall = "accountall";
    public static final String schetacc = "schet";
    public static final String idacc = "_id3";

    public static final String tableсategRashod = "Rashod";
    public static final String idCategRashod = "_id4";
    public static final String rashod = "rashod";

    public static final String startBalance = "StartBalance";
    public static final String idbalance = "_id5";
    public static final String schet_balance = "schet_balance";
    public static final String sum_balance = "summa_balance";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + add_summ + "(" + KEY_ID
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," + summ1 + " NUMERIC NOT NULL," + date1 + " TEXT NOT NULL," +
                category1 + " TEXT NOT NULL," +
                account1 + " TEXT NOT NULL,"
                + comment1 + " TEXT NOT NULL,"
                + dateSort1 + " text" + ");");


        db.execSQL("create table " + table2 + "(" + id2
                + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," + summ2 + " NUMERIC NOT NULL," + date2 + " TEXT NOT NULL," +
                category2 + " TEXT NOT NULL," +
                account2 + " TEXT NOT NULL,"
                + comment2 + " TEXT NOT NULL,"
                + dateSort2 + " text" + ");");

        db.execSQL("create table " + tableCategAdd + "(" + idCateg + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " + categoryprihod + " TEXT NOT NULL" + ");");
        db.execSQL("create table " + tableScheta + "(" + idSchet+ " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " + schet + " TEXT NOT NULL" + ");");
        db.execSQL("create table " + tableсategRashod + "(" + idCategRashod + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " + rashod + " TEXT NOT NULL" + ");");
        db.execSQL("create table " + accountall + "(" + idacc + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " + schetacc + " TEXT NOT NULL" + ");");

        //Таблица с начальным балансом
        db.execSQL("create table " + startBalance + "(" + idbalance + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " + schet_balance + " TEXT NOT NULL," +
                sum_balance + " NUMERIC NOT NULL"+");");

        this.insertLabelAdd(db,"Зарплата");
        this.insertLabelAdd(db,"Премия");
        this.insertLabelAdd(db,"Подарок");
        this.insertLabelAdd(db,"Перевод");

    this.insertLabelSchet(db,"Сбербанк");
    this.insertLabelSchet(db,"Наличные");
    this.insertLabelSchet(db,"ВТБ");


    this.insertLabelRashod(db,"Питание");
    this.insertLabelRashod(db,"Развлечения");
    this.insertLabelRashod(db,"Автомобиль");
    this.insertLabelRashod(db,"Образование");
    this.insertLabelRashod(db,"Общественный транспорт");
        this.insertLabelRashod(db,"Перевод");

        this.insertLabelAcc(db,"Все счета");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + add_summ);
        db.execSQL("drop table if exists " + table2);
        onCreate(db);

    }


    // Функция добавления данных в таблицу доходов
    public boolean insertData(String sum,String date, String categ, String acc, String comm,String datasort) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(summ1,sum);
        contentValues.put(date1,date);
        contentValues.put(category1,categ);
        contentValues.put(account1,acc);
        contentValues.put(comment1,comm);
        contentValues.put(dateSort1,datasort);
        long result = db.insert(add_summ,null ,contentValues);
       /* if(result == -1)
            return false;
        else
            return true;*/
       return true;
    }
    // Функция добавления данных в таблицу расходов
    public boolean insertNew(String a,String b, String d, String f, String g,String datasort) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(summ2,a);
        content.put(date2,b);
        content.put(category2,d);
        content.put(account2,f);
        content.put(comment2,g);
        content.put(dateSort2,datasort);
        long result = db.insert(table2,null ,content);
        Log.d("Inserted ", "Done");
        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }




    public boolean updateData(String sum,String date, String categ, String acc, String comm,int id,String datasort) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(summ1,sum);
        contentValues.put(date1,date);
        contentValues.put(category1,categ);
        contentValues.put(account1,acc);
        contentValues.put(comment1,comm);
        contentValues.put(KEY_ID,id);
        contentValues.put(dateSort1,datasort);

        db.update(add_summ, contentValues, KEY_ID + "= ?", new String[]{String.valueOf(id)});
        return true;
    }

    public boolean updateBalance(String schet1,String sum1,String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(schet_balance,schet1);
        contentValues.put(sum_balance,sum1);


        db.update(startBalance, contentValues, schet_balance + "= ?",new String[]{String.valueOf(name)});
        return true;
    }

   public void delete (int id){
       SQLiteDatabase db = this.getWritableDatabase();
       db.delete(add_summ,KEY_ID +" =?",new String[]{String.valueOf(id)});
   }

    public void delete_rashod (int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table2,id2 +" =?",new String[]{String.valueOf(id)});
    }

    //удаление категории - расход
    public void delete_categ (int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableсategRashod,idCategRashod +" =?",new String[]{String.valueOf(id)});
    }

    //удаление категории - счета
    public void delete_schet (int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableScheta,idSchet +" =?",new String[]{String.valueOf(id)});
    }

    //удаление категории - приход
    public void delete_prihod (int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableCategAdd,idCateg +" =?",new String[]{String.valueOf(id)});
    }

    public boolean updateData_rashod(String sum,String date, String categ, String acc, String comm,int id,String datasort) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(summ2,sum);
        contentValues.put(date2,date);
        contentValues.put(category2,categ);
        contentValues.put(account2,acc);
        contentValues.put(comment2,comm);
        contentValues.put(id2,id);
        contentValues.put(dateSort2,datasort);
        db.update(table2, contentValues, id2 + "= ?", new String[]{String.valueOf(id)});
        return true;
    }





    public Cursor getPersonnelList(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + add_summ + " ORDER BY datetime(" + dateSort1 + ") DESC", null);
        return res;
    }

    public Cursor getPersonnelList1(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + table2 + " ORDER BY datetime(" + dateSort2 + " ) DESC", null);
        return res;
    }

    public Cursor getPersonneRashod(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + tableсategRashod, null);
        return res;
    }
    public Cursor getPersonneSchet(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + tableScheta, null);
        return res;
    }

    public Cursor getPersonnePrihod(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + tableCategAdd, null);
        return res;
    }










    //функция вывода категории прихода
    public List<String> getAllLabels(){
        List<String> list = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + tableCategAdd;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(1));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
    }

    //функция вывода счетов
    public List<String> getAllLabels2(){
        List<String> list = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + tableScheta;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(1));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();


        db.close();
        // returning lables
        return list;
    }



    //функция вывода категории расхода
    public List<String> getAllLabels3(){
        List<String> list = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + tableсategRashod;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(1));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
    }

    // Все счета и счета другие
    public List<String> getAllLabels4(){
        List<String> list = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + tableScheta;
        String selectQuery2 = "SELECT  * FROM " + accountall;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor2 = db.rawQuery(selectQuery2, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor2.moveToFirst()) {
            do {
                list.add(cursor2.getString(1));//adding 2nd column data
            } while (cursor2.moveToNext());
        }
        // closing connection
        cursor2.close();

        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(1));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();



        db.close();
        // returning lables
        return list;
    }





    public void insertLabelAdd(SQLiteDatabase db,String label){
       // SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(categoryprihod, label);//column name, column value

        // Inserting Row
        db.insert(tableCategAdd, null, values);//tableName, nullColumnHack, CotentValues
       // db.close(); // Closing database connection
    }

    public void insertLabelSchet(SQLiteDatabase db,String label){
        // SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(schet, label);//column name, column value

        // Inserting Row
        db.insert(tableScheta, null, values);//tableName, nullColumnHack, CotentValues
        // db.close(); // Closing database connection
    }

    public void insertLabelRashod(SQLiteDatabase db,String label){
        // SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(rashod, label);//column name, column value

        // Inserting Row
        db.insert(tableсategRashod, null, values);//tableName, nullColumnHack, CotentValues
        // db.close(); // Closing database connection
    }

    public void insertLabelAcc(SQLiteDatabase db,String label){
        // SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(schetacc, label);//column name, column value

        // Inserting Row
        db.insert(accountall, null, values);//tableName, nullColumnHack, CotentValues
        // db.close(); // Closing database connection
    }

    // Добавление начального баланса
    public void insertBalance(SQLiteDatabase db,String label,String sumbalance){
        // SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(schet_balance, label);//column name, column value
        values.put(sum_balance, sumbalance);//column name, column value
        // Inserting Row
        db.insert(startBalance, null, values);//tableName, nullColumnHack, CotentValues
        // db.close(); // Closing database connection
    }

    public static FinanceOperation getFinanceOperationFromCursor(final Cursor cursor) {
        FinanceOperation financeOperation = new FinanceOperation(
                cursor.getString(4),
                cursor.getString(3),
                cursor.getString(2),
                cursor.getString(5),
                cursor.getDouble(1)
        );
        return financeOperation;
    }

    public List<FinanceOperation> getIncomeFinanceOperationsInPeriod(String category, List<String> accounts, String from, String to) {
        List<FinanceOperation> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("" +
                        "SELECT * FROM " + add_summ +
                        " WHERE " + category1 + "=?" +
                        " AND instr(?," + account1 + ")" +
                        " AND (datetime(" + dateSort1 + ") BETWEEN datetime(?) AND datetime(?))",
                new String[] {category, TextUtils.join(",", accounts), from, to});
        Log.wtf(TAG, cursor.getCount() + "");
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                FinanceOperation financeOperation = getFinanceOperationFromCursor(cursor);
                Log.wtf(TAG, financeOperation.toString());
                result.add(financeOperation);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return result;
    }

    public List<CategoryStat> getIncomeCategoriesStat(List<String> accounts, String from, String to) {
        List<CategoryStat> result = new ArrayList<>();
        List<String> categoryList = getAllLabels();
        SQLiteDatabase db = getReadableDatabase();
        for (String category: categoryList) {
            Cursor cursor = db.rawQuery("" +
                            "SELECT SUM(" + summ1 + ") FROM " + add_summ +
                            " WHERE " + category1 + "=?" +
                            " AND instr(?," + account1 + ")" +
                            " AND (datetime(" + dateSort1 + ") BETWEEN datetime(?) AND datetime(?))",
                    new String[] {category, TextUtils.join(",", accounts), from, to});
            cursor.moveToFirst();

            CategoryStat categoryStat = new CategoryStat(
                    category,
                    cursor.getDouble(0)
            );

            result.add(categoryStat);
            cursor.close();
        }
        db.close();
        return result;
    }

    public List<FinanceOperation> getOutgoFinanceOperationsInPeriod(String category, List<String> accounts, String from, String to) {
        List<FinanceOperation> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("" +
                        "SELECT * FROM " + table2 +
                        " WHERE " + category2 + "=?" +
                        " AND instr(?," + account2 + ")" +
                        " AND (datetime(" + dateSort2 + ") BETWEEN datetime(?) AND datetime(?))",
                new String[] {category, TextUtils.join(",", accounts), from, to});
        Log.wtf(TAG, cursor.getCount() + "");
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                FinanceOperation financeOperation = getFinanceOperationFromCursor(cursor);
                Log.wtf(TAG, financeOperation.toString());
                result.add(financeOperation);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return result;
    }

    public List<CategoryStat> getOutgoCategoriesStat(List<String> accounts, String from, String to) {
        List<CategoryStat> result = new ArrayList<>();
        List<String> categoryList = getAllLabels3();
        SQLiteDatabase db = this.getReadableDatabase();
        for (String category: categoryList) {
            Cursor cursor = db.rawQuery("" +
                "SELECT SUM(" + summ2 + ") FROM " + table2 +
                " WHERE " + category2 + "=?" +
                " AND instr(?," + account2 + ")" +
                " AND (datetime(" + dateSort2 + ") BETWEEN datetime(?) AND datetime(?))",
                new String[] {category, TextUtils.join(",", accounts), from, to});
            cursor.moveToFirst();

            CategoryStat categoryStat = new CategoryStat(
                    category,
                    cursor.getDouble(0)
            );

            result.add(categoryStat);
            cursor.close();
        }
        db.close();
        return result;
    }


    public double summs() {
        double sum=0;
        SQLiteDatabase db = this.getReadableDatabase();
        String sumQuery=String.format("SELECT SUM(%s) as Total FROM %s",summ1,add_summ);
        Cursor cursor=db.rawQuery(sumQuery,null);
        if(cursor.moveToFirst())
            sum= cursor.getDouble(0);
        return sum;
    }
    public double rashod() {
        double sum=0;
        SQLiteDatabase db = this.getReadableDatabase();
        String sumQuery=String.format("SELECT SUM(%s) as Total FROM %s",summ2,table2);
        Cursor cursor=db.rawQuery(sumQuery,null);
        if(cursor.moveToFirst())
            sum= cursor.getDouble(0);
        return sum;
    }


    //проверка, существует ли определенная запись

    public boolean columnExists(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = String.format("SELECT EXISTS (SELECT * FROM %s WHERE %s = '%s' LIMIT 1)",startBalance,schet_balance,value);
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        // cursor.getInt(0) is 1 if column with value exists
        if (cursor.getInt(0) == 1) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }






}
