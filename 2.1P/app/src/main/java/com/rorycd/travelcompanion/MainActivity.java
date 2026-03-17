package com.rorycd.travelcompanion;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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
    TabLayout tabLayout;
    TextView tvInputPrefix, tvOutputPrefix;
    EditText etInput, etOutput;
    MaterialAutoCompleteTextView dropdownInput, dropdownOutput;
    Button btnClear;

    // State
    String currentTab;
    public class Unit { public String value; }
    Unit inputUnit = new Unit();
    Unit outputUnit = new Unit();
    Boolean isUpdating;

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
        applyTab();

        // Set listeners
        tabLayout.addOnTabSelectedListener(tabListener);
        etInput.addTextChangedListener(inputWatcher);
        etOutput.addTextChangedListener(outputWatcher);
        etInput.setOnFocusChangeListener(new EditTextFocusChangeListener(etInput));
        etOutput.setOnFocusChangeListener(new EditTextFocusChangeListener(etOutput));
        dropdownInput.setOnItemClickListener(new OnDropdownSelectedListener(inputUnit));
        dropdownOutput.setOnItemClickListener(new OnDropdownSelectedListener(outputUnit));
        btnClear.setOnClickListener(v -> clearInput());
    }

    private void resetConverterState() {
        // Set field input types
        if (currentTab.equals("temperature")) {
            // Signed decimals for temperature
            etInput.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL |
                InputType.TYPE_NUMBER_FLAG_SIGNED
            );
            etOutput.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL |
                InputType.TYPE_NUMBER_FLAG_SIGNED
            );
        } else {
            // Unsigned decimals for fuel and currency
            etInput.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL
            );
            etOutput.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL
            );
        }

        // Assign unit lists
        String[] units;
        switch (currentTab) {
            case "currency":
                units = getResources().getStringArray(R.array.currencies);
                break;
            case "fuel":
                units = getResources().getStringArray(R.array.fuelEfficiencyUnits);
                break;
            default:
                units = getResources().getStringArray(R.array.temperatureUnits);
                break;
        }

        // Set dropdown lists
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, units);
        dropdownInput.setAdapter(adapter);
        dropdownOutput.setAdapter(adapter);
        dropdownInput.setText(units[0], false);
        dropdownOutput.setText(units[1], false);

        // Set initial state
        clearInput();
        inputUnit.value = units[0];
        outputUnit.value = units[1];
        setPrefixes();
    }

    private void clearInput() {
        isUpdating = true;
        etInput.setText("");
        etOutput.setText("");
        isUpdating = false;
    }

    private void setPrefixes() {
        if (currentTab.equals("currency")) {
            tvInputPrefix.setText(getSymbolFor(inputUnit));
            tvOutputPrefix.setText(getSymbolFor(outputUnit));
        } else {
            tvInputPrefix.setText("");
            tvOutputPrefix.setText("");
        }
    }

    private String getSymbolFor(Unit unit) {
        // Get matching symbol prefix
        String result = "";

        switch (currentTab) {
            case "currency":
                Currency c = Currency.getInstance(unit.value);
                String symbol = c.getSymbol(Locale.US);     // US uses symbol only
                if (symbol.contains("$")) symbol = "$";     // Ignore "A$" etc.
                result = symbol;
                break;
            case "temperature":
                result = unit.value.equals("Celsius") ? "°C" : unit.value.equals("Fahrenheit") ? "°F" : " K";
                break;
            default:
                break;
        }

        return result;
    }

    // Sets the current tab based on the selection and reset the converter
    private void applyTab() {
        int tabPos = tabLayout.getSelectedTabPosition();
        TabLayout.Tab tab = tabLayout.getTabAt(tabPos);
        if (tab == null) return;
        String tabString = tab.getText().toString();
        int titleStartIndex = tabString.indexOf(" ") + 1;
        currentTab = tabString.toLowerCase().substring(titleStartIndex);
        resetConverterState();
    }

    // LISTENERS
    private class OnDropdownSelectedListener implements AdapterView.OnItemClickListener {
        Unit unit;

        OnDropdownSelectedListener(Unit unit) {
            this.unit = unit;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
            // Select new unit (Apply it to either input or output)
            unit.value = parent.getItemAtPosition(pos).toString();
            setPrefixes();
            // Update output
            isUpdating = true;
            applyConversion(inputUnit, etInput, outputUnit, etOutput);
            isUpdating = false;
        }
    }

    TabLayout.OnTabSelectedListener tabListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            applyTab();
        }
        @Override public void onTabUnselected(TabLayout.Tab tab) { }
        @Override public void onTabReselected(TabLayout.Tab tab) { }
    };

    TextWatcher inputWatcher = new TextWatcher() {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!isUpdating) applyConversion(inputUnit, etInput, outputUnit, etOutput);
        }
        @Override public void afterTextChanged(Editable s) { }
    };

    TextWatcher outputWatcher = new TextWatcher() {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!isUpdating) applyConversion(outputUnit, etOutput, inputUnit, etInput);
        }
        @Override public void afterTextChanged(Editable s) { }
    };

    // Cleans up input fields on unfocus
    private class EditTextFocusChangeListener implements View.OnFocusChangeListener {

        private EditText editText;

        EditTextFocusChangeListener(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                String s = editText.getText().toString();
                if (s.isEmpty()) return;

                isUpdating = true;

                // Extract input value
                double inputNumber = parseStringToDouble(s);
                // Format input
                    String prettyNumber = formatDoubleAsString(inputNumber, 0, 50);
                    editText.setText(prettyNumber);

                isUpdating = false;
            }
        }
    }

    protected double parseStringToDouble(String s) {
        // Clean input
        s = s.replaceAll(",", "").trim();

        // Handle symbols
        if (s.isEmpty() ||
            s.equals(".") ||
            s.equals("-") ||
            s.equals("-.")) {
            return 0.0;
        }

        // Convert to double
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    protected String formatDoubleAsString(double number, int minFractionDigits, int maxFractionDigits) {
        // Base format on locale
        DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(Locale.getDefault());
        // Set digits after decimal "0.XXX"
        df.setMinimumFractionDigits(minFractionDigits);
        df.setMaximumFractionDigits(maxFractionDigits);
        // Format number as string
        String formatted = df.format(number);
        if (formatted.equals("0") && !currentTab.equals("temperature")) formatted = "";
        return formatted;
    }

    protected void applyConversion(Unit fromUnit, EditText origin, Unit toUnit, EditText target) {
        String strInput = origin.getText().toString().replaceAll(",", "");
        double input = parseStringToDouble(strInput);

        // Convert
        double result;
        switch (currentTab) {
            case "currency":
                result = convertCurrency(input, fromUnit.value, toUnit.value);
                break;
            case "temperature":
                result = convertTemperature(input, fromUnit.value, toUnit.value);
                break;
            default:
                result = convertFuel(input, fromUnit.value, toUnit.value);
                break;
        }

        isUpdating = true;

        // Format converted value as string
        String strResult = strInput.isEmpty() ? "" : formatDoubleAsString(result, 0, 2);
        // Wipe zero values unless they're temperatures
        if (strResult.equals("0.0") && !currentTab.equals("temperature")) strResult = "";

        target.setText(strResult);
        isUpdating = false;
    }

    private double convertCurrency(double value, String fromCurrency, String toCurrency) {
        // Get USD -> target rate
        HashMap<String, Double> rates = new HashMap<>();
        rates.put("AUD", 1.55);
        rates.put("USD", 1.0);
        rates.put("EUR", 0.92);
        rates.put("GBP", 0.78);
        rates.put("JPY", 148.50);

        // Convert to USD
        Double rate = rates.get(fromCurrency);
        if (rate == null) return 0;
        double valueUSD = value / rate;

        // Convert to target currency
        rate = rates.get(toCurrency);
        if (rate == null) return 0;
        return valueUSD * rate;
    }

    private double convertTemperature(double value, String fromUnit, String toUnit) {
        // Convert to Celsius
        double valueC = value;
        switch (fromUnit) {
            case "Fahrenheit":
                valueC = (value - 32) / 1.8;
                break;
            case "Kelvin":
                valueC = value - 273.15;
                break;
            default:
                break;
        }

        // Convert to target unit
        switch (toUnit) {
            case "Fahrenheit":  return (valueC * 1.8) + 32;
            case "Kelvin":      return valueC + 273.15;
            default:            return valueC;
        }
    }

    private double convertFuel(double value, String fromUnit, String toUnit) {
        // Convert to mpg
        double valueMPG = value;
        switch (fromUnit) {
            case "km/L":
                valueMPG = value / 0.425;
                break;
            case "nmpg":
                valueMPG = value * 1.150779;
                break;
            default:
                break;
        }

        // Convert to target unit
        switch (toUnit) {
            case "km/L":    return valueMPG * 0.425;
            case "nmpg":    return valueMPG / 1.150779;
            default:        return valueMPG;
        }
    }
}