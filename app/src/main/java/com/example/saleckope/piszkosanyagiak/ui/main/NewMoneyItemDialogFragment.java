package com.example.saleckope.piszkosanyagiak.ui.main;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.saleckope.piszkosanyagiak.R;
import com.example.saleckope.piszkosanyagiak.model.MoneyItem;

/**
 * Created by saleckope on 2016. 11. 16..
 */

public class NewMoneyItemDialogFragment extends AppCompatDialogFragment {
    public static final String TAG = "NewMoneyItemDialogFragment";

    private EditText amountEditText;
    private EditText noteEditText;
    private DatePicker datePicker;
    private Spinner categorySpinner;
    private Spinner typeSpinner;


    public interface INewMoneyItemDialogListener {
        void onMoneyItemCreated(MoneyItem newItem);
    }

    private INewMoneyItemDialogListener listener;

    //Creating fragment and listener to activity
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof INewMoneyItemDialogListener) {
            listener = (INewMoneyItemDialogListener) activity;
        } else {
            throw new RuntimeException("Activity must implement the INewMoneyItemDialogListener interface!");
        }
    }

    //Creates new Dialog window
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Clicking on "OK" generates new MoneyItem
        return new AlertDialog.Builder(getContext()).setTitle(R.string.new_money_item).setView(getContentView()).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (isValid()) {
                    listener.onMoneyItemCreated(getMoneyItem());
                }
            }

            //Amount is compulsory
            private boolean isValid() {
                return amountEditText.getText().length() > 0;
            }

            //Get new MoneyItem's data
            private MoneyItem getMoneyItem() {
                MoneyItem moneyItem = new MoneyItem();
                moneyItem.type = MoneyItem.Type.getByOrdinal(typeSpinner.getSelectedItemPosition());
                switch (moneyItem.type) {
                    case INCOME:
                        moneyItem.amount = Integer.parseInt(amountEditText.getText().toString());
                        //Income has no category
                        moneyItem.category = null;
                        break;
                    case OUTLAY:
                        moneyItem.amount = 0 - Integer.parseInt(amountEditText.getText().toString());
                        moneyItem.category = MoneyItem.Category.getByOrdinal(categorySpinner.getSelectedItemPosition());
                        break;
                    default:
                        moneyItem.amount = 0;
                }
                //Note is not compulsory
                try {
                    moneyItem.note = noteEditText.getText().toString();
                } catch (NumberFormatException e) {
                    moneyItem.note = "";
                }
                Integer day = datePicker.getDayOfMonth();
                Integer month = datePicker.getMonth() + 1;
                Integer year = datePicker.getYear();
                moneyItem.year = year;
                moneyItem.month = month;
                moneyItem.day = day;
                moneyItem.save();
                return moneyItem;
            }
        })
                //Clicking on "CANCEL" button drop data
                .setNegativeButton(R.string.cancel, null).create();
    }

    //Method for get view
    //find views on layout
    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_money_item, null);
        amountEditText = (EditText) contentView.findViewById(R.id.MoneyItemAmountEditText);
        noteEditText = (EditText) contentView.findViewById(R.id.MoneyItemNoteEditText);
        datePicker = (DatePicker) contentView.findViewById(R.id.MoneyItemDatePicker);

        typeSpinner = (Spinner) contentView.findViewById(R.id.MoneyItemTypeSpinner);
        typeSpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.type_items)));
        categorySpinner = (Spinner) contentView.findViewById(R.id.MoneyItemCategorySpinner);
        categorySpinner.setEnabled(false);
        categorySpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.category_items)));

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            //If type item is selected
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MoneyItem item = new MoneyItem();
                //Get selected type
                item.type = MoneyItem.Type.getByOrdinal(typeSpinner.getSelectedItemPosition());

                //If outlay is selected
                if(item.type == MoneyItem.Type.OUTLAY){
                    //Category spinner is active
                    //user can select category
                    categorySpinner.setEnabled(true);
                } else{
                    //If income is selected, user can't select category
                    categorySpinner.setEnabled(false);
                }
            }

            //User can't select nothing
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return contentView;
    }
}