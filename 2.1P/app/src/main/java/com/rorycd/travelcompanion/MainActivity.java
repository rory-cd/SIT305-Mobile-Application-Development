package com.rorycd.travelcompanion;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText etInput;

    Currency currencyFrom;
    Spinner spinFromCurrency;
    Currency currencyTo;
    Spinner spinToCurrency;

    TextView tvFromPrefix;
    TextView tvToPrefix;
    EditText etOutput;

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

    protected void applyConversion(EditText origin, EditText target) {
        String strInput = origin.getText().toString();
        if (strInput.isEmpty()) strInput = "0.00";
        double newInput = Double.parseDouble(strInput);
        double convertedInput = convertCurrency(newInput, currencyFrom, currencyTo);
        target.setText(String.format(Locale.getDefault(), "%.2f", convertedInput));
    }

    protected double convertCurrency(double value, Currency from, Currency to) {
        String fromCode = from.getCurrencyCode();
        String toCode = to.getCurrencyCode();

        // Get USD -> target rate
        HashMap<String, Double> rates = new HashMap<>();
        rates.put("AUD", 1.55);
        rates.put("USD", 1.0);
        rates.put("EUR", 0.92);
        rates.put("GBP", 0.78);
        rates.put("JPY", 148.50);

        // Convert to USD
        double valueUSD = value / rates.get(fromCode);

        // Convert to target currency
        return valueUSD * rates.get(toCode);
    }

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
        spinFromCurrency = findViewById(R.id.spinFromCurrency);
        spinToCurrency = findViewById(R.id.spinToCurrency);
        tvFromPrefix = findViewById(R.id.tvFromPrefix);
        tvToPrefix = findViewById(R.id.tvToPrefix);

        // Set initial currencies and hints
        currencyFrom = Currency.getInstance(spinFromCurrency.getSelectedItem().toString());
        currencyTo = Currency.getInstance(spinToCurrency.getSelectedItem().toString());
        etInput.setHint(String.format(Locale.getDefault(), "%.2f", 0.00));
        etOutput.setHint(String.format(Locale.getDefault(), "%.2f", 0.00));

        // Set listeners
        etInput.addTextChangedListener(inputWatcher);
        etOutput.addTextChangedListener(outputWatcher);

        // Set "from" currency
        spinFromCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Set currency
                String code = parent.getSelectedItem().toString();
                currencyFrom = Currency.getInstance(code);

                // Apply correct symbol
                String symbol = currencyFrom.getSymbol(Locale.US);  // US uses symbol only
                if (symbol.contains("$")) symbol = "$";             // Ignore "A$" etc.
                tvFromPrefix.setText(symbol);

                etOutput.removeTextChangedListener(outputWatcher);
                applyConversion(etInput, etOutput);
                etOutput.addTextChangedListener(outputWatcher);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // Set "to" currency
        spinToCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Set currency
                String code = parent.getSelectedItem().toString();
                currencyTo = Currency.getInstance(code);

                // Apply correct symbol
                String symbol = currencyTo.getSymbol(Locale.US);    // US uses symbol only
                if (symbol.contains("$")) symbol = "$";             // Ignore "A$" etc.
                tvToPrefix.setText(symbol);

                etOutput.removeTextChangedListener(outputWatcher);
                applyConversion(etInput, etOutput);
                etOutput.addTextChangedListener(outputWatcher);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }
}