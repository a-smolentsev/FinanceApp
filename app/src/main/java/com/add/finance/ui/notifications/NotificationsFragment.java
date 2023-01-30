package com.add.finance.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.add.finance.DBHelper;
import com.add.finance.EditAdvent;
import com.add.finance.EditCategory;
import com.add.finance.EditSchet;
import com.add.finance.R;
import com.add.finance.startBalance;

import java.util.HashMap;


public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private ListView userList;
    public TextView summa;
    Button editCateg;
    Button editschet;
    Button editPrihod;
    Button Balance;
    private TextView textView;
    DBHelper mDBHelper;
    SQLiteDatabase mDb;
    String text;
    private Context mContext;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        notificationsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });



        editCateg = (Button) root.findViewById(R.id.editCateg);
        Balance = (Button) root.findViewById(R.id.balance);

       Balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), com.add.finance.startBalance.class);
                startActivity(intent);

            }
        });


        editCateg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditCategory.class);
                startActivity(intent);

            }
        });

        editschet = (Button) root.findViewById(R.id.editschet);
        editschet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditSchet.class);
                startActivity(intent);

            }
        });

        editPrihod = (Button) root.findViewById(R.id.editPrihod);
        editPrihod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditAdvent.class);
                startActivity(intent);

            }
        });

        return root;

    }



}