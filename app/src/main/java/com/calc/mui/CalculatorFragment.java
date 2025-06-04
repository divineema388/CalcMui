package com.calc.mui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class CalculatorFragment extends Fragment implements View.OnClickListener {

    private TextView tvDisplay, tvHistory;
    private String currentNumber = "";
    private String firstOperand = "";
    private String secondOperand = "";
    private String currentOperator = "";
    private boolean isNewCalculation = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);

        tvDisplay = view.findViewById(R.id.tv_display);
        tvHistory = view.findViewById(R.id.tv_history);

        // Set click listeners for all buttons
        int[] buttonIds = {
                R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3,
                R.id.btn_4, R.id.btn_5, R.id.btn_6, R.id.btn_7,
                R.id.btn_8, R.id.btn_9, R.id.btn_add, R.id.btn_subtract,
                R.id.btn_multiply, R.id.btn_divide, R.id.btn_equals,
                R.id.btn_clear, R.id.btn_decimal, R.id.btn_percent,
                R.id.btn_plus_minus
        };

        for (int id : buttonIds) {
            view.findViewById(id).setOnClickListener(this);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        String buttonText = button.getText().toString();

        switch (v.getId()) {
            case R.id.btn_0:
            case R.id.btn_1:
            case R.id.btn_2:
            case R.id.btn_3:
            case R.id.btn_4:
            case R.id.btn_5:
            case R.id.btn_6:
            case R.id.btn_7:
            case R.id.btn_8:
            case R.id.btn_9:
                handleNumberInput(buttonText);
                break;
            case R.id.btn_add:
            case R.id.btn_subtract:
            case R.id.btn_multiply:
            case R.id.btn_divide:
                handleOperator(buttonText);
                break;
            case R.id.btn_equals:
                calculateResult();
                break;
            case R.id.btn_clear:
                clearAll();
                break;
            case R.id.btn_decimal:
                handleDecimal();
                break;
            case R.id.btn_percent:
                handlePercentage();
                break;
            case R.id.btn_plus_minus:
                toggleSign();
                break;
        }
    }

    private void handleNumberInput(String number) {
        if (isNewCalculation) {
            currentNumber = "";
            isNewCalculation = false;
        }
        
        if (currentNumber.length() < 15) {
            currentNumber += number;
            tvDisplay.setText(currentNumber);
        }
    }

    private void handleOperator(String operator) {
        if (!currentNumber.isEmpty()) {
            if (firstOperand.isEmpty()) {
                firstOperand = currentNumber;
            } else if (!currentOperator.isEmpty()) {
                calculateResult();
                firstOperand = currentNumber;
            }
            currentOperator = operator;
            currentNumber = "";
            tvHistory.setText(firstOperand + " " + currentOperator);
        }
    }

    private void calculateResult() {
        if (!firstOperand.isEmpty() && !currentOperator.isEmpty() && !currentNumber.isEmpty()) {
            secondOperand = currentNumber;
            double result = 0;
            double firstNum = Double.parseDouble(firstOperand);
            double secondNum = Double.parseDouble(secondOperand);

            switch (currentOperator) {
                case "+":
                    result = firstNum + secondNum;
                    break;
                case "-":
                    result = firstNum - secondNum;
                    break;
                case "×":
                    result = firstNum * secondNum;
                    break;
                case "÷":
                    if (secondNum != 0) {
                        result = firstNum / secondNum;
                    } else {
                        tvDisplay.setText("Error");
                        return;
                    }
                    break;
            }

            // Format result to remove .0 if it's an integer
            String resultStr;
            if (result == (long) result) {
                resultStr = String.format("%d", (long) result);
            } else {
                resultStr = String.format("%s", result);
            }

            tvHistory.setText(firstOperand + " " + currentOperator + " " + secondOperand + " =");
            tvDisplay.setText(resultStr);
            firstOperand = resultStr;
            currentNumber = resultStr;
            isNewCalculation = true;
        }
    }

    private void clearAll() {
        currentNumber = "";
        firstOperand = "";
        secondOperand = "";
        currentOperator = "";
        tvDisplay.setText("0");
        tvHistory.setText("");
        isNewCalculation = true;
    }

    private void handleDecimal() {
        if (isNewCalculation) {
            currentNumber = "0.";
            isNewCalculation = false;
            tvDisplay.setText(currentNumber);
            return;
        }
        
        if (!currentNumber.contains(".")) {
            if (currentNumber.isEmpty()) {
                currentNumber = "0.";
            } else {
                currentNumber += ".";
            }
            tvDisplay.setText(currentNumber);
        }
    }

    private void handlePercentage() {
        if (!currentNumber.isEmpty()) {
            double number = Double.parseDouble(currentNumber);
            number /= 100;
            currentNumber = String.valueOf(number);
            tvDisplay.setText(currentNumber);
        }
    }

    private void toggleSign() {
        if (!currentNumber.isEmpty()) {
            if (currentNumber.charAt(0) == '-') {
                currentNumber = currentNumber.substring(1);
            } else {
                currentNumber = "-" + currentNumber;
            }
            tvDisplay.setText(currentNumber);
        }
    }
}