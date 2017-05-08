package com.example.saleckope.piszkosanyagiak.ui.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.saleckope.piszkosanyagiak.R;
import com.example.saleckope.piszkosanyagiak.ui.main.MainActivity;
import com.example.saleckope.piszkosanyagiak.ui.main.MoneyAdapter;

public class SettingsFragment extends Fragment {
    //Fragment is found by TAG
    private static final String TAG = "SettingsFragment";

    EditText budgetEditText;
    Button setBudgetButton;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            //Inflate fragment layout
            View view = inflater.inflate(R.layout.fragment_settings, container, false);

            budgetEditText = (EditText) view.findViewById(R.id.budgetEditText);
            setBudgetButton = (Button) view.findViewById(R.id.setBudgetButton);
            //Set budgetEditText to current budget amount
            budgetEditText.setText(Integer.toString(MainActivity.budget));
            //Clicked on "SET" button
            setBudgetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    //Set budget
                    MainActivity.budget = Integer.parseInt(String.valueOf(budgetEditText.getText()));
                    //Recalculate spent/haveLeft money
                    MoneyAdapter.haveLeft();
                    //Show daily budget
                    MainActivity.showDailyBudget();
                }
            });
            return view;
        }
}
