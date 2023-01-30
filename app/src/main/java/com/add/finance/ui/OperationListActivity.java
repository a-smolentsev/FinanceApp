/*
 * created by Andrew K. <rembozebest@gmail.com> on 5/14/20 5:38 AM
 * copyright (c) 2020
 * last modified 5/13/20 3:13 AM with ❤
 */

package com.add.finance.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.add.finance.DBHelper;
import com.add.finance.R;
import com.add.finance.adapter.FinanceOperationAdapter;
import com.add.finance.data.Operation;

import java.util.Arrays;
import java.util.List;

public class OperationListActivity extends AppCompatActivity {
    public static String OPERATION_TYPE = "OPERATIONS_TYPE";
    public static String CATEGORY = "CATEGORY";
    public static String ACCOUNTS = "ACCOUNTS";
    public static String FROM_DATE = "FROM_DATE";
    public static String TO_DATE = "TO_DATE";

    private RecyclerView recyclerView;
    private FinanceOperationAdapter adapter;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recycler_view);
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new FinanceOperationAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        dbHelper = new DBHelper(getApplicationContext());
        updateDataSet();
    }

    public void updateDataSet() {
        Bundle bundle = getIntent().getExtras();
        Operation operationType = Operation.valueOf(bundle.getString(OPERATION_TYPE));
        String category = bundle.getString(CATEGORY);
        List<String> accounts = Arrays.asList(bundle.getStringArray(ACCOUNTS));
        String dateFrom = bundle.getString(FROM_DATE);
        String dateTo = bundle.getString(TO_DATE);

        if (operationType == Operation.INCOME) {
            adapter.setDataSet(
                    dbHelper.getIncomeFinanceOperationsInPeriod(category, accounts, dateFrom, dateTo)
            );
        } else if (operationType == Operation.OUTGO) {
            adapter.setDataSet(
                    dbHelper.getOutgoFinanceOperationsInPeriod(category, accounts, dateFrom, dateTo)
            );
        }
    }
}
