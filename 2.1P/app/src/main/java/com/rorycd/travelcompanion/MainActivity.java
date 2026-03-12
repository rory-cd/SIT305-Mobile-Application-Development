package com.rorycd.travelcompanion;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.text.DecimalFormat;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // UI Elements
    EditText etInput, etOutput;
    MaterialAutoCompleteTextView dropdownInput, dropdownOutput;
    TextView tvInputPrefix, tvOutputPrefix;
    Button btnClear;
    TabLayout tabLayout;
    String currentTab;

    // State
    String inputUnit, outputUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get UI Elements
        etInput = findViewById(R.id.etInput);
        etOutput = findViewById(R.id.etOutput);
        dropdownInput = findViewById(R.id.dropdownInput);
        dropdownOutput = findViewById(R.id.dropdownOutput);
        tvInputPrefix = findViewById(R.id.tvInputPrefix);
        tvOutputPrefix = findViewById(R.id.tvOutputPrefix);
        btnClear = findViewById(R.id.btnClear);
        tabLayout = findViewById(R.id.tabLayout);

        // Set initial state
        // Tab
        int tabPos = tabLayout.getSelectedTabPosition();
        TabLayout.Tab tab = tabLayout.getTabAt(tabPos);
        currentTab = tab.getText().toString();

        // Lists
        String[] currencies = getResources().getStringArray(R.array.currencies);

        // Set dropdowns
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, currencies);
        dropdownInput.setAdapter(adapter);
        dropdownOutput.setAdapter(adapter);
        dropdownInput.setText(currencies[0], false);
        dropdownOutput.setText(currencies[1], false);

        inputUnit = currencies[0];
        outputUnit = currencies[1];

        // Set prefixes and hints
        String prefix = "";
        switch (currentTab) {
            case "Currency" :
                prefix = getSymbolFor(inputUnit);
        }
        tvInputPrefix.setText(getSymbolFor(inputUnit));
        tvOutputPrefix.setText(getSymbolFor(outputUnit));
        etInput.setHint(String.format(Locale.getDefault(), "%.2f", 0.00));
        etOutput.setHint(String.format(Locale.getDefault(), "%.2f", 0.00));

        // Set listeners
        etInput.addTextChangedListener(inputWatcher);
        etOutput.addTextChangedListener(outputWatcher);
        etInput.setOnFocusChangeListener(new EditTextFocusChangeListener(etInput, inputWatcher));
        etOutput.setOnFocusChangeListener(new EditTextFocusChangeListener(etOutput, outputWatcher));
        dropdownInput.setOnItemClickListener(new OnDropdownSelectedListener(tvInputPrefix, c -> inputUnit = c));
        dropdownOutput.setOnItemClickListener(new OnDropdownSelectedListener(tvOutputPrefix, c -> outputUnit = c));
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etInput.setText("");
            }
        });
    }

    protected interface UnitSelector { void selectUnit(String unit); }

    private class OnDropdownSelectedListener implements AdapterView.OnItemClickListener {

        private TextView prefix;
        UnitSelector selector;

        OnDropdownSelectedListener(TextView prefix, UnitSelector selector) {
            this.prefix = prefix;
            this.selector = selector;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
            // Get newly selected unit
            String unit = parent.getItemAtPosition(pos).toString();
            // Select it (Apply it to either input or output)
            selector.selectUnit(unit);
            // Set corresponding prefix
            prefix.setText(getSymbolFor(unit));
            // Update output
            etOutput.removeTextChangedListener(outputWatcher);
            applyConversion(etInput, etOutput);
            etOutput.addTextChangedListener(outputWatcher);
        }
    }

    private String getSymbolFor(String unit) {
        // Get matching symbol prefix
        Currency c = Currency.getInstance(unit);
        String symbol = c.getSymbol(Locale.US);     // US uses symbol only
        if (symbol.contains("$")) symbol = "$";     // Ignore "A$" etc.
        return symbol;
    }

    TextWatcher inputWatcher = new TextWatcher() {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            etOutput.removeTextChangedListener(outputWatcher);
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            applyConversion(etInput, etOutput);
        }
        @Override public void afterTextChanged(Editable s) {
            etOutput.addTextChangedListener(outputWatcher);
        }
    };

    TextWatcher outputWatcher = new TextWatcher() {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            etInput.removeTextChangedListener(inputWatcher);
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            applyConversion(etOutput, etInput);
        }
        @Override public void afterTextChanged(Editable s) {
            etInput.addTextChangedListener(inputWatcher);
        }
    };

    // Clean up input fields on unfocus
    private class EditTextFocusChangeListener implements View.OnFocusChangeListener {

        private EditText editText;
        private TextWatcher watcher;

        EditTextFocusChangeListener(EditText editText, TextWatcher watcher) {
            this.editText = editText;
            this.watcher = watcher;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                String s = editText.getText().toString();
                if (s.isEmpty()) return;

                editText.removeTextChangedListener(watcher);

                // Clean up input
                double inputNumber = parseStringToDouble(s);
                if (inputNumber == 0) editText.setText("");
                else {
                    String prettyNumber = formatDoubleAsString(inputNumber, 0, 50);
                    editText.setText(prettyNumber);
                }

                editText.addTextChangedListener(watcher);
            }
        }
    }

    protected double parseStringToDouble(String s) {
        s = s.replaceAll(",", "");
        if (s.isEmpty() || s.equals(".")) s = "0";
        return Double.parseDouble(s);
    }

    protected void applyConversion(EditText origin, EditText target) {
        String strInput = origin.getText().toString().replaceAll(",", "");
        double newInput = parseStringToDouble(strInput);
        double convertedInput = convertCurrency(newInput);
        target.setText(formatDoubleAsString(convertedInput, 0, 2));
    }

    protected String formatDoubleAsString(double number, int minFractionDigits, int maxFractionDigits) {
        // Base format on locale
        DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(Locale.getDefault());
        // Set digits after decimal "0.XXX"
        df.setMinimumFractionDigits(minFractionDigits);
        df.setMaximumFractionDigits(maxFractionDigits);
        // Format number as string
        String formatted = df.format(number);
        if (formatted.equals("0")) formatted = "";
        return formatted;
    }

    protected double convertCurrency(double value) {
        // Get USD -> target rate
        HashMap<String, Double> rates = new HashMap<>();
        rates.put("AUD", 1.55);
        rates.put("USD", 1.0);
        rates.put("EUR", 0.92);
        rates.put("GBP", 0.78);
        rates.put("JPY", 148.50);

        // Convert to USD
        double valueUSD = value / rates.get(inputUnit);

        // Convert to target currency
        Log.d("CONVERT", "USD: " + valueUSD);
        Log.d("CONVERT", "new: " + valueUSD * rates.get(outputUnit));
        return valueUSD * rates.get(outputUnit);
    }
}