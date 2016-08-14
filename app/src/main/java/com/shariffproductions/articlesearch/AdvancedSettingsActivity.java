package com.shariffproductions.articlesearch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdvancedSettingsActivity extends AppCompatActivity {
    private EditText beginDate;
    private final List<String> sortOrderOptions = Arrays.asList("Oldest", "Newest");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_settings);

        setUpBeginDateFilter();
        setUpSortOrderFilter();
    }

    private void setUpSortOrderFilter() {
        ArrayAdapter<String> sortOrderArrayAdapter = new ArrayAdapter<>(this, R.layout.sort_order_spinner_item, R.id.sort_order, sortOrderOptions);
        Spinner sortOrderSpinner = (Spinner) findViewById(R.id.spinner_sort_order);
        sortOrderSpinner.setAdapter(sortOrderArrayAdapter);
        sortOrderSpinner.setSelection(sortOrderArrayAdapter.getPosition("Newest"));
    }

    private void setUpBeginDateFilter() {
        beginDate = (EditText) findViewById(R.id.et_begin_date);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i("beforeTextChanged", charSequence.toString());
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i("onTextChanged", charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                switch(beginDate.getText().length()) {
                    case 2:
                        beginDate.append("/");
                        break;
                    case 5:
                        beginDate.append("/");
                        break;
                }
            }
        };
        beginDate.addTextChangedListener(textWatcher);
    }
}
