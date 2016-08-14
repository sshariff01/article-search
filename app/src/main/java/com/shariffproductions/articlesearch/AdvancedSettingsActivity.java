package com.shariffproductions.articlesearch;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdvancedSettingsActivity extends AppCompatActivity {
    private EditText beginDateEditText;
    private Spinner sortOrderSpinner;
    private List<String> sortOrderOptions = Arrays.asList("Oldest", "Newest");
    private HashMap<String, Boolean> newsDeskValuesMap = new HashMap<>();

    public void save(View view) {
        Intent data = new Intent();
        data.putExtra("beginDate", beginDateEditText.getText().toString());
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
        initActivityLayoutOnClickListener();
        initBeginDateFilter();
        initSortOrderFilter();
    }

    private void initActivityLayoutOnClickListener() {
        LinearLayout activityLayout = (LinearLayout) findViewById(R.id.activity_advanced_settings);
        activityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() != R.id.et_begin_date) {
                    exitInputMode();
                }
            }
        });
    }

    private void exitInputMode() {
        beginDateEditText.clearFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private void initSortOrderFilter() {
        ArrayAdapter<String> sortOrderArrayAdapter = new ArrayAdapter<>(this, R.layout.sort_order_spinner_item, R.id.sort_order, sortOrderOptions);
        sortOrderSpinner = (Spinner) findViewById(R.id.spinner_sort_order);
        sortOrderSpinner.setAdapter(sortOrderArrayAdapter);
        sortOrderSpinner.setSelection(sortOrderArrayAdapter.getPosition("Newest"));
    }

    private void initBeginDateFilter() {
        beginDateEditText = (EditText) findViewById(R.id.et_begin_date);
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
                switch(beginDateEditText.getText().length()) {
                    case 2:
                        beginDateEditText.append("/");
                        break;
                    case 5:
                        beginDateEditText.append("/");
                        break;
                }
            }
        };
        beginDateEditText.addTextChangedListener(textWatcher);

        beginDateEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_UP)) {
                    exitInputMode();
                    return true;
                }
                return false;
            }
        });
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
