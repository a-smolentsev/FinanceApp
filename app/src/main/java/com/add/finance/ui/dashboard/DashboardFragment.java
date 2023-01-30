/*
 * created by Andrew K. <rembozebest@gmail.com> on 5/14/20 5:38 AM
 * copyright (c) 2020
 * last modified 5/13/20 4:23 AM with ❤
 */

package com.add.finance.ui.dashboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.add.finance.DBHelper;
import com.add.finance.R;
import com.add.finance.adapter.CategoryStatAdapter;
import com.add.finance.data.CategoryStat;
import com.add.finance.data.Operation;
import com.add.finance.ui.OperationListActivity;
import com.add.finance.ui.RecyclerViewFragment;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DashboardFragment extends Fragment {
    public static final String TAG = "DashboardFragment";

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    private DBHelper dbHelper;
    private Calendar calendar;

    private String startDate = "0000-01-01 00:00:00";
    private String endDate = "9999-12-31 23:59:59";
    private Long startDateUnix;
    private Long endDateUnix;

    private List<String> selectedAccounts = new ArrayList<>();

    private String[] accounts;
    private boolean[] accountsState;

    private CategoryStatAdapter incomeCategoryStatAdapter;
    private CategoryStatAdapter outgoCategoryStatAdapter;

    private RecyclerViewFragment incomeOperationsFragment;
    private RecyclerViewFragment outgoOperationsFragment;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupData();
        setupViews(getView());
        setupViewListeners();
        setupPager();
        setupRecyclers();

        setHasOptionsMenu(true);

        applyFilter();
    }

    private void setupPager() {
        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @Override
            public int getItemCount() {
                return 2;
            }

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 0:
                        return incomeOperationsFragment;
                    case 1:
                        return outgoOperationsFragment;
                    default:
                        return createFragment(0);
                }
            }
        });

        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position) {
                            case 0:
                                tab.setText("доходы");
                                return;
                            case 1:
                                tab.setText("расходы");
                                return;
                            default:
                                tab.setText("unhandled");
                        }
                    }
                }
        ).attach();
    }

    private void setupRecyclers() {
        incomeCategoryStatAdapter = new CategoryStatAdapter();
        outgoCategoryStatAdapter = new CategoryStatAdapter();

        incomeOperationsFragment = new RecyclerViewFragment();
        incomeOperationsFragment.post(new Runnable() {
            @Override
            public void run() {
                incomeOperationsFragment.getRecyclerView().setAdapter(incomeCategoryStatAdapter);
                incomeOperationsFragment.getRecyclerView().setLayoutManager(new LinearLayoutManager(getContext()));
                incomeCategoryStatAdapter.setOnClickListener(new CategoryStatAdapter.OnCategoryClick() {
                    @Override
                    public void onCategoryClick(CategoryStat categoryStat) {
                        Intent intent = new Intent(getContext(), OperationListActivity.class);
                        intent.putExtra(OperationListActivity.OPERATION_TYPE, Operation.INCOME.name());
                        intent.putExtra(OperationListActivity.CATEGORY, categoryStat.getCategory());
                        intent.putExtra(OperationListActivity.ACCOUNTS,
                                selectedAccounts.isEmpty() ? accounts : selectedAccounts.toArray(new String[selectedAccounts.size()])
                        );
                        intent.putExtra(OperationListActivity.FROM_DATE, startDate);
                        intent.putExtra(OperationListActivity.TO_DATE, endDate);
                        getContext().startActivity(intent);
                    }
                });
            }
        });

        outgoOperationsFragment = new RecyclerViewFragment();
        outgoOperationsFragment.post(new Runnable() {
            @Override
            public void run() {
                outgoOperationsFragment.getRecyclerView().setAdapter(outgoCategoryStatAdapter);
                outgoOperationsFragment.getRecyclerView().setLayoutManager(new LinearLayoutManager(getContext()));
                outgoCategoryStatAdapter.setOnClickListener(new CategoryStatAdapter.OnCategoryClick() {
                    @Override
                    public void onCategoryClick(CategoryStat categoryStat) {
                        Intent intent = new Intent(getContext(), OperationListActivity.class);
                        intent.putExtra(OperationListActivity.OPERATION_TYPE, Operation.OUTGO.name());
                        intent.putExtra(OperationListActivity.CATEGORY, categoryStat.getCategory());
                        Log.wtf(TAG, TextUtils.join(",", selectedAccounts));
                        Log.wtf(TAG, TextUtils.join(",", accounts));
                        intent.putExtra(OperationListActivity.ACCOUNTS,
                                selectedAccounts.isEmpty() ? accounts : selectedAccounts.toArray(new String[selectedAccounts.size()])
                        );
                        intent.putExtra(OperationListActivity.FROM_DATE, startDate);
                        intent.putExtra(OperationListActivity.TO_DATE, endDate);
                        getContext().startActivity(intent);
                    }
                });
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filter_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.select_sort_date:
                showDateRangePicker();
                return true;
            case R.id.select_sort_billings:
                showBillingsPicker();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupData() {
        dbHelper = new DBHelper(getContext());
        calendar = Calendar.getInstance();

        List<String> billingsList = dbHelper.getAllLabels2();
        accounts = billingsList.toArray(new String[billingsList.size()]);

        accountsState = new boolean[accounts.length];
    }

    private void setupViews(View view) {
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.pager);
    }

    private void setupViewListeners() {
    }

    public void showDateRangePicker() {
        MaterialDatePicker<Pair<Long, Long>> datePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setSelection(new Pair<>(startDateUnix, endDateUnix))
                .build();
        datePicker.addOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                startDateUnix = selection.first;
                endDateUnix = selection.second;

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                calendar.setTimeInMillis(startDateUnix);
                startDate = simpleDateFormat.format(calendar.getTime());
                simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
                calendar.setTimeInMillis(endDateUnix);
                endDate = simpleDateFormat.format(calendar.getTime());

                applyFilter();
            }
        });
        datePicker.show(getParentFragmentManager(), datePicker.toString());
    }

    public void showBillingsPicker() {
        AlertDialog.Builder billingsPickerBuilder = new AlertDialog.Builder(getActivity());
        billingsPickerBuilder.setTitle("Выберите счет");
        billingsPickerBuilder.setMultiChoiceItems(accounts, accountsState, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                String billing = accounts[which];
                if (selectedAccounts.contains(billing)) {
                    selectedAccounts.remove(billing);
                } else {
                    selectedAccounts.add(billing);
                }
            }
        });
        billingsPickerBuilder.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), TextUtils.join(", ", selectedAccounts), Toast.LENGTH_SHORT).show();
                applyFilter();
                dialog.dismiss();
            }
        });
        billingsPickerBuilder.setCancelable(false);
        billingsPickerBuilder.show();
    }

    private void applyFilter() {
        List<String> accountList = selectedAccounts.isEmpty() ? Arrays.asList(accounts) : selectedAccounts;

        List<CategoryStat> incomeStats = dbHelper.getIncomeCategoriesStat(accountList, startDate, endDate);
        Collections.sort(incomeStats, new Comparator<CategoryStat>() {
            @Override
            public int compare(CategoryStat first, CategoryStat middle) {
                return middle.getTotal().compareTo(first.getTotal());
            }
        });

        List<CategoryStat> outgoStats = dbHelper.getOutgoCategoriesStat(accountList, startDate, endDate);
        Collections.sort(outgoStats, new Comparator<CategoryStat>() {
            @Override
            public int compare(CategoryStat first, CategoryStat middle) {
                return middle.getTotal().compareTo(first.getTotal());
            }
        });

        incomeCategoryStatAdapter.setData(incomeStats);
        outgoCategoryStatAdapter.setData(outgoStats);
    }

}


