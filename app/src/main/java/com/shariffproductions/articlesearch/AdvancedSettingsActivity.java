package com.shariffproductions.articlesearch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdvancedSettingsActivity extends AppCompatActivity {
    private EditText beginDate;
    private Spinner sortOrderSpinner;
    private List<String> sortOrderOptions = Arrays.asList("Oldest", "Newest");
    private HashMap<String, Boolean> newsDeskValuesMap = new HashMap<>();

    public void save(View view) {
        Intent data = new Intent();
        data.putExtra("beginDate", beginDate.getText().toString());
        data.putExtra("sortOrder", sortOrderSpinner.getSelectedItem().toString());
        for (Map.Entry<String, Boolean> newsDeskValue : newsDeskValuesMap.entrySet()) {
            if (newsDeskValue.getValue()) {
                data.putExtra(newsDeskValue.getKey(), true);
            }
        }
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_settings);

        setUpBeginDateFilter();
        setUpSortOrderFilter();
    }

    private void setUpSortOrderFilter() {
        ArrayAdapter<String> sortOrderArrayAdapter = new ArrayAdapter<>(this, R.layout.sort_order_spinner_item, R.id.sort_order, sortOrderOptions);
        sortOrderSpinner = (Spinner) findViewById(R.id.spinner_sort_order);
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

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.checkbox_arts:
                newsDeskValuesMap.put("Arts", checked);
                break;
            case R.id.checkbox_fashion_style:
                newsDeskValuesMap.put("Fashion & Style", checked);
                break;
            case R.id.sports:
                newsDeskValuesMap.put("Sports", checked);
                break;
        }
    }
}
