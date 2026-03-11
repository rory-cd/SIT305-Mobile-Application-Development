package com.rorycd.travelcompanion;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import java.text.DecimalFormat;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText etInput, etOutput;
    Currency inputCurrency, outputCurrency;
    Spinner spinInputCurrency, spinOutputCurrency;
    TextView tvInputPrefix, tvOutputPrefix;

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
        spinInputCurrency = findViewById(R.id.spinInputCurrency);
        spinOutputCurrency = findViewById(R.id.spinOutputCurrency);
        tvInputPrefix = findViewById(R.id.tvInputPrefix);
        tvOutputPrefix = findViewById(R.id.tvOutputPrefix);

        // Set initial currencies and hints
        inputCurrency = Currency.getInstance(spinInputCurrency.getSelectedItem().toString());
        outputCurrency = Currency.getInstance(spinOutputCurrency.getSelectedItem().toString());
        etInput.setHint(String.format(Locale.getDefault(), "%.2f", 0.00));
        etOutput.setHint(String.format(Locale.getDefault(), "%.2f", 0.00));

        // Set listeners
        etInput.addTextChangedListener(inputWatcher);
        etOutput.addTextChangedListener(outputWatcher);
        etInput.setOnFocusChangeListener(new EditTextFocusChangeListener(etInput, inputWatcher));
        etOutput.setOnFocusChangeListener(new EditTextFocusChangeListener(etOutput, outputWatcher));
        spinInputCurrency.setOnItemSelectedListener(new OnSpinnerSelectedListener(tvInputPrefix, c -> inputCurrency = c));
        spinOutputCurrency.setOnItemSelectedListener(new OnSpinnerSelectedListener(tvOutputPrefix, c -> outputCurrency = c));
    }

    protected interface CurrencySelector { void selectCurrency(Currency currency); }

    private class OnSpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        private TextView prefix;
        CurrencySelector selector;

        OnSpinnerSelectedListener(TextView prefix, CurrencySelector selector) {
            this.prefix = prefix;
            this.selector = selector;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // Set currency
            String code = parent.getSelectedItem().toString();
            selector.selectCurrency(Currency.getInstance(code));

            // Apply correct symbol prefix
            String symbol = inputCurrency.getSymbol(Locale.US);  // US uses symbol only
            if (symbol.contains("$")) symbol = "$";              // Ignore "A$" etc.
            prefix.setText(symbol);

            // Update output
            etOutput.removeTextChangedListener(outputWatcher);
            applyConversion(etInput, etOutput);
            etOutput.addTextChangedListener(outputWatcher);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) { }
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
        String inputCode = inputCurrency.getCurrencyCode();
        String outputCode = outputCurrency.getCurrencyCode();

        // Get USD -> target rate
        HashMap<String, Double> rates = new HashMap<>();
        rates.put("AUD", 1.55);
        rates.put("USD", 1.0);
        rates.put("EUR", 0.92);
        rates.put("GBP", 0.78);
        rates.put("JPY", 148.50);

        // Convert to USD
        double valueUSD = value / rates.get(inputCode);

        // Convert to target currency
        Log.d("CONVERT", "USD: " + valueUSD);
        Log.d("CONVERT", "new: " + valueUSD * rates.get(outputCode));
        return valueUSD * rates.get(outputCode);
    }
}