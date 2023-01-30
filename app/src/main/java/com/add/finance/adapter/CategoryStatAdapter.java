package com.add.finance.adapter;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.add.finance.R;
import com.add.finance.data.CategoryStat;
import com.add.finance.utils.TextFormat;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class CategoryStatAdapter extends RecyclerView.Adapter<CategoryStatAdapter.CategoryStatHolder> {
    private static final String TAG = "CategoryStatAdapter";

    @Getter
    private List<CategoryStat> dataSet = new ArrayList<>(0);
    @Setter
    private OnCategoryClick onClickListener;

    @NonNull
    @Override
    public CategoryStatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_stat_item, parent, false);
        return new CategoryStatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryStatHolder holder, int position) {
        holder.bind(dataSet.get(position), onClickListener);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setData(List<CategoryStat> categoryStatList) {
        Log.wtf(TAG, "Data set changed! " + dataSet.size() + "->" + categoryStatList.size());
        dataSet = categoryStatList;
        notifyDataSetChanged();
    }

    public static class CategoryStatHolder extends RecyclerView.ViewHolder {
        private TextView categoryText;
        private TextView totalText;

        public CategoryStatHolder(@NonNull View itemView) {
            super(itemView);
            categoryText = itemView.findViewById(R.id.category);
            totalText = itemView.findViewById(R.id.total);
        }

        public void bind(final CategoryStat categoryStat, final OnCategoryClick onClickListener) {
            categoryText.setText(categoryStat.getCategory());
            totalText.setText(TextFormat.doubleToString(categoryStat.getTotal()));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) {
                        onClickListener.onCategoryClick(categoryStat);
                    }
                }
            });
        }
    }

    public interface OnCategoryClick {
        void onCategoryClick(CategoryStat categoryStat);
    }
}
