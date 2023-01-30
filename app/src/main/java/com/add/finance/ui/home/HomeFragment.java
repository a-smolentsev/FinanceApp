package com.add.finance.ui.home;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.add.finance.DBHelper;
import com.add.finance.MainActivity;
import com.add.finance.R;
import com.add.finance.Spend;

import com.add.finance.advent;
import com.add.finance.fragment_spend;
import com.add.finance.pagerAdapter;
import com.add.finance.updates;
import com.add.finance.updates_rashod;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HomeFragment extends Fragment  {

    private HomeViewModel homeViewModel;
    private ListView userList;
    private ListView userList1;
    public TextView summa;
    TextView adv;
    TextView spd;
    private TabLayout tabLayout;
    private ViewPager viewpage;
    private TabItem tabItem;
    private TabItem tabItem2;
    private pagerAdapter pagerAdapter;
    DBHelper mDBHelper;
    SQLiteDatabase mDb;
    private TextView tvStatus;

    String balancesum2;
    String balancesum;
    String allbalancesumm;

    Cursor userCursor;
    private SimpleCursorAdapter userAdapter;

    private Context mContext;
    String account;




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {


            }
        });
        mDBHelper = new DBHelper(getContext());
        setHasOptionsMenu(true);

        tabLayout = (TabLayout) root.findViewById(R.id.tablayout);
        tabItem = root.findViewById(R.id.spend);
        tabItem2 = root.findViewById(R.id.advent);
        viewpage = root.findViewById(R.id.view);
        tabLayout.setupWithViewPager(viewpage);

        pagerAdapter = new pagerAdapter(getChildFragmentManager());
        viewpage.setAdapter(pagerAdapter);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //viewpage.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
////////////////////////////////////////////////////////// Тут  Заканчивается ////////////////////////////////////////////////////////////////////////////

        //Баланс
        //Баланс

        double mainSum; //сумма вся Доход - расход

        mainSum = mDBHelper.summs() - mDBHelper.rashod();
        if (mainSum <= 0)
            mainSum = 0;
        DecimalFormat formatter = new DecimalFormat("0.##");

        balancesum2= String.format("%.2f", mainSum);
        allbalancesumm=balancesum2;

        return root;
    }

    @Override
   /* public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




    }*/
   /* public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.balance_menu, menu);
        String bluetoothStatus = "Connected";
        menu.findItem(R.id.statusTextview).setTitle(bluetoothStatus);
        super.onCreateOptionsMenu(menu,inflater);
    }*/


    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.balance_menu, menu);
        MenuItem item = menu.findItem(R.id.spinnerBalance);
        final Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
        final DBHelper db = new DBHelper(getContext());
        List<String> labels = db.getAllLabels4();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                account=spinner.getSelectedItem().toString();

                double mainSum; //сумма вся Доход - расход
                String allacc="Все счета";



                double sumrashod=0;
                SQLiteDatabase mDB = mDBHelper.getReadableDatabase();
                String sumQuery=String.format("SELECT SUM(%s) as Total FROM %s WHERE %s = '%s'",db.summ2,db.table2,db.account2,account);
                Cursor cursor=mDB.rawQuery(sumQuery,null);
                if(cursor.moveToFirst())
                    sumrashod= cursor.getDouble(0);

                double sum=0;

                String sumQuery1=String.format("SELECT SUM(%s) as Total FROM %s WHERE %s = '%s'",db.summ1,db.add_summ,db.account1,account);
                Cursor cursor1=mDB.rawQuery(sumQuery1,null);
                if(cursor1.moveToFirst())
                    sum= cursor1.getDouble(0);


                //сумма вся Доход - расход

                mainSum = sum - sumrashod ;
                if (mainSum <= 0)
                    mainSum = 0;


                balancesum = String.format("%.2f", mainSum);
                allbalancesumm= balancesum;

                //проверка - если в спиннере установлены все счета, то считаем доходы - расходы таблиц
                if(account.equals(allacc))
                            {
                                 allbalancesumm= balancesum2;
                            }


                String balance = "Баланс: ";
                menu.findItem(R.id.statusTextview).setTitle(balance+allbalancesumm);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });






        super.onCreateOptionsMenu(menu, inflater);
    }







}