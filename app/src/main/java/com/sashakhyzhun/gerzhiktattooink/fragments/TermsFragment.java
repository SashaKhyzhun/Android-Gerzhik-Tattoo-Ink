package com.sashakhyzhun.gerzhiktattooink.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.sashakhyzhun.gerzhiktattooink.R;
import com.sashakhyzhun.gerzhiktattooink.receiver.MyReceiver;
import com.sashakhyzhun.locationhelper.LocationDailyChecker;

/**
 * @autor SashaKhyzhun
 * Created on 3/30/17.
 */

public class TermsFragment extends Fragment {

    private TextView tvTerms;
    private TimePicker timePicker;
    private Button buttonCreate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terms, container, false);

        tvTerms = (TextView) view.findViewById(R.id.text_view_terms);
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        buttonCreate = (Button) view.findViewById(R.id.button_create_location_checker);

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = timePicker.getCurrentHour();
                int min = timePicker.getCurrentMinute();
                int requestCode = Integer.parseInt(hour + "" + min);

                System.out.println("buttonCreate | hour: " + hour);
                System.out.println("buttonCreate | min: " + min);
                System.out.println("buttonCreate | RequestCode: " + requestCode);

                LocationDailyChecker dlc = new LocationDailyChecker(getContext());
                dlc.enableDailyNotificationReminder(hour, min, requestCode, MyReceiver.class);


            }
        });



        return view;

    }

}
