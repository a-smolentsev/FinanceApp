package com.add.finance.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.add.finance.R;
import com.add.finance.data.FinanceOperation;
import com.add.finance.utils.TextFormat;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class FinanceOperationAdapter extends RecyclerView.Adapter<FinanceOperationAdapter.FinanceOperationHolder> {

    @Getter
    private List<FinanceOperation> dataSet = new ArrayList<>();

    @NonNull
    @Override
    public FinanceOperationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new FinanceOperationHolder(view);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public void onBindViewHolder(@NonNull FinanceOperationHolder holder, int position) {
        holder.bind(dataSet.get(position));
    }

    public void setDataSet(List<FinanceOperation> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    public static class FinanceOperationHolder extends RecyclerView.ViewHolder {
        private TextView totalText, categoryText, accountText, dealDateText, descriptionText;

        public FinanceOperationHolder(@NonNull View itemView) {
            super(itemView);
            totalText = itemView.findViewById(R.id.total_text);
            categoryText = itemView.findViewById(R.id.category_text);
            accountText = itemView.findViewById(R.id.account_text);
            dealDateText = itemView.findViewById(R.id.deal_date_text);
            descriptionText = itemView.findViewById(R.id.description_text);
        }

        public void bind(FinanceOperation financeOperation) {
            totalText.setText(TextFormat.doubleToString(financeOperation.getTotal()));
            categoryText.setText(financeOperation.getCategory());
            accountText.setText(financeOperation.getAccount());
            dealDateText.setText(financeOperation.getDealDate());
            descriptionText.setText(financeOperation.getDescription());
        }
    }
}
