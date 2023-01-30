/*
 * created by Andrew K. <rembozebest@gmail.com> on 18.05.20 19:10
 * copyright (c) 2020
 * last modified 18.05.20 19:10 with ❤
 */

package com.add.finance;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class spendFragment extends Fragment {


    public spendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_spend2, container, false);
    }

}
