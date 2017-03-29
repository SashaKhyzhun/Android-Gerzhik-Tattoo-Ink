package com.sashakhyzhun.gerzhiktattooink.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sashakhyzhun.gerzhiktattooink.R;

/**
 * @autor SashaKhyzhun
 * Created on 3/30/17.
 */

public class TermsFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terms, container, false);

        TextView tvTerms = (TextView) view.findViewById(R.id.text_view_terms);
        tvTerms.setText("Mama ama criminal");
        tvTerms.setTextColor(Color.WHITE);
        tvTerms.setTextSize(24);

        return view;
    }

}
