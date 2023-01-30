/*
 * created by Andrew K. <rembozebest@gmail.com> on 5/14/20 5:38 AM
 * copyright (c) 2020
 * last modified 5/12/20 11:13 AM with ❤
 */

package com.add.finance.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.add.finance.R;

import lombok.Getter;

public class RecyclerViewFragment extends Fragment {
    @Getter 
    private RecyclerView recyclerView;
    private Runnable runnable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (runnable != null) {
            runnable.run();
        }
    }

    public void post(Runnable runnable) {
        this.runnable = runnable;
    }
}
