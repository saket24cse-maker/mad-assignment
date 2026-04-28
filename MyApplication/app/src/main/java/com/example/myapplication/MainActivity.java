package com.example.q1_currencyconverter;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private EditText amountInput;
    private Spinner fromCurrency, toCurrency;
    private TextView resultText;
    private Button convertBtn, settingsBtn;
    
    // Simple exchange rates relative to INR for demonstration
    private double INR_TO_USD = 0.012;
    private double INR_TO_EUR = 0.011;
    private double INR_TO_JPY = 1.82;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        amountInput = findViewById(R.id.amountInput);
        fromCurrency = findViewById(R.id.fromCurrency);
        toCurrency = findViewById(R.id.toCurrency);
        resultText = findViewById(R.id.resultText);
        convertBtn = findViewById(R.id.convertBtn);
        settingsBtn = findViewById(R.id.settingsBtn);

        String[] currencies = {"INR", "USD", "JPY", "EUR"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, currencies);
        fromCurrency.setAdapter(adapter);
        toCurrency.setAdapter(adapter);

        convertBtn.setOnClickListener(v -> convertCurrency());
        settingsBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SettingsActivity.class)));
    }

    private void convertCurrency() {
        String amountStr = amountInput.getText().toString();
        if (amountStr.isEmpty()) return;
        
        double amount = Double.parseDouble(amountStr);
        String from = fromCurrency.getSelectedItem().toString();
        String to = toCurrency.getSelectedItem().toString();
        
        double inInr = amount;
        switch(from) {
            case "USD": inInr = amount / INR_TO_USD; break;
            case "EUR": inInr = amount / INR_TO_EUR; break;
            case "JPY": inInr = amount / INR_TO_JPY; break;
        }
        
        double result = inInr;
        switch(to) {
            case "USD": result = inInr * INR_TO_USD; break;
            case "EUR": result = inInr * INR_TO_EUR; break;
            case "JPY": result = inInr * INR_TO_JPY; break;
        }
        
        resultText.setText(String.format("%.2f %s = %.2f %s", amount, from, result, to));
    }
}
