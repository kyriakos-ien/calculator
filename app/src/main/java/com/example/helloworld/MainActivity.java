package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {
    


    @BindView(R.id.calcScreen) EditText calcScreen;

    @BindView(R.id.btn0) Button number0;
    @BindView(R.id.btn1) Button number1;
    @BindView(R.id.btn2) Button number2;
    @BindView(R.id.btn3) Button number3;
    @BindView(R.id.btn4) Button number4;
    @BindView(R.id.btn5) Button number5;
    @BindView(R.id.btn6) Button number6;
    @BindView(R.id.btn7) Button number7;
    @BindView(R.id.btn8) Button number8;
    @BindView(R.id.btn9) Button number9;

    private boolean passIsSet;
    private String password;
    private boolean confirmation;
    private double accumulator = Double.MIN_VALUE;
    private String currentOperation;
    private String previousOperation;
    private Boolean negative = false;


    @OnClick(R.id.btn0) void btn0tapped() {numberKeyPressed("0");}
    @OnClick(R.id.btn1) void btn1tapped() {numberKeyPressed("1");}
    @OnClick(R.id.btn2) void btn2tapped() {numberKeyPressed("2");}
    @OnClick(R.id.btn3) void btn3tapped() {numberKeyPressed("3");}
    @OnClick(R.id.btn4) void btn4tapped() {numberKeyPressed("4");}
    @OnClick(R.id.btn5) void btn5tapped() {numberKeyPressed("5");}
    @OnClick(R.id.btn6) void btn6tapped() {numberKeyPressed("6");}
    @OnClick(R.id.btn7) void btn7tapped() {numberKeyPressed("7");}
    @OnClick(R.id.btn8) void btn8tapped() {numberKeyPressed("8");}
    @OnClick(R.id.btn9) void btn9tapped() {numberKeyPressed("9");}

    @BindView(R.id.btn_addition) Button addition;
    @BindView(R.id.btn_subtraction) Button subtraction;
    @BindView(R.id.btn_multiplication) Button multiplication;
    @BindView(R.id.btn_division) Button division;
    @BindView(R.id.btn_equals) Button equals;
    @BindView(R.id.btn_point) Button point;

    @OnClick(R.id.btn_addition) void addition() {operatorPressed("+");}
    @OnClick(R.id.btn_subtraction) void subtraction() {operatorPressed("-");}
    @OnClick(R.id.btn_multiplication) void multiplication() {operatorPressed("*");}
    @OnClick(R.id.btn_division) void division() {operatorPressed("/");}
    @OnClick(R.id.btn_equals) void equals() {equalsPresssed();}
    @OnClick(R.id.btn_point) void point() {pointPressed();}

    @BindView(R.id.btn_clear) Button clear;
    @BindView(R.id.btn_backspace) Button backspace;
    @BindView(R.id.btn_precentage) Button precentage;
    @BindView(R.id.btn_opposite) Button opposite;

    @OnClick(R.id.btn_clear) void clear() {clearPressed();}
    @OnClick(R.id.btn_backspace) void backspace() {backspacePressed();}
    @OnClick(R.id.btn_precentage) void precentage() {precentagePressed();}
    @OnClick(R.id.btn_opposite) void opposite() {oppositePressed();}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        final EditText calcScreen = (EditText)findViewById(R.id.calcScreen);
        calcScreen.setShowSoftInputOnFocus(false);


        SharedPreferences pref = getApplicationContext().getSharedPreferences("preferences", MODE_PRIVATE);
        passIsSet = pref.getBoolean("userFirstLogin", false);
        password = pref.getString("password", "");
        confirmation = pref.getBoolean("firstTime", false);

        // setup the alert builder
        if (isFirstTime()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Set App Password");
            builder.setMessage("Please set your 4-digit password into Calculator and press '%' button to confirm");

            // add a button
            builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }
    private boolean isFirstTime()
    {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.commit();
        }
        return !ranBefore;
    }

    public void numberKeyPressed(String number){

        String string_screen= calcScreen.getText().toString();
        int start = calcScreen.getSelectionStart();

        if (string_screen.length()>0){
            String first = string_screen.substring(0,start);
            String second = string_screen.substring(start,string_screen.length());
            calcScreen.setText(first + number + second );
            calcScreen.setSelection(start+1);
            if (currentOperation != null){
                if (!negative) {
                    calcScreen.getText().clear();
                    calcScreen.setText(number);
                    calcScreen.setSelection(1);
                }else{
                    calcScreen.setText("-" + number);
                    calcScreen.setSelection(2);
                }
                currentOperation = null;
            }
        }else if(!number.equalsIgnoreCase("0")){
            calcScreen.setText(number);
            calcScreen.setSelection(1);
        }
        currentOperation = null;


    }
    public void operatorPressed(String operator){
        Log.v("current","current symbol: "+ currentOperation);
        Log.v("current","past symbol: "+ previousOperation);
        Log.v("current","accumulator symbol: "+ accumulator);
        DecimalFormat df = new DecimalFormat("###.#");
        negative = false;

        if (currentOperation == null) {

            if (accumulator != Double.MIN_VALUE) {

                String stored_num2 = String.valueOf(calcScreen.getText());
                double double_stored_num2 = Double.parseDouble(stored_num2);

                if (previousOperation.equalsIgnoreCase("+")) {
                    accumulator = accumulator + double_stored_num2;

                } else if (previousOperation.equalsIgnoreCase("-")) {
                    accumulator = accumulator - double_stored_num2;

                } else if (previousOperation.equalsIgnoreCase("*")) {
                    accumulator = accumulator * double_stored_num2;

                } else if (previousOperation.equalsIgnoreCase("/")) {
                    accumulator = accumulator / double_stored_num2;

                }
                calcScreen.setText(df.format(accumulator));

            }else{
                String stored_num = String.valueOf(calcScreen.getText());

                try {
                    accumulator = Double.parseDouble(stored_num);
                } catch (NumberFormatException e) {
                    return;
                }

                previousOperation = operator;
                currentOperation = operator;
            }
        }
        else if(currentOperation.equalsIgnoreCase("=")){
            accumulator = Double.parseDouble(String.valueOf(calcScreen.getText()));
        }
        previousOperation = operator;
        currentOperation = operator;
        String screen = String.valueOf(calcScreen.getText());
        double db_screen = Double.parseDouble(screen);


    }
    public void equalsPresssed(){
        String stored_num2 = String.valueOf(calcScreen.getText());
        DecimalFormat df = new DecimalFormat("0.#");
        if (accumulator!=Double.MIN_VALUE) {
            double double_stored_num2 = Double.parseDouble(stored_num2);
            if (previousOperation.equalsIgnoreCase("+")) {
                accumulator = accumulator + double_stored_num2;
                calcScreen.setText(removeTrailingZeros(accumulator));
                currentOperation = "=";
                accumulator = Double.MIN_VALUE;

            } else if (previousOperation.equalsIgnoreCase("-")) {
                accumulator = accumulator - double_stored_num2;
                calcScreen.setText(removeTrailingZeros(accumulator));
                currentOperation = "=";
                accumulator = Double.MIN_VALUE;

            } else if (previousOperation.equalsIgnoreCase("*")) {
                accumulator = accumulator * double_stored_num2;
                calcScreen.setText(removeTrailingZeros(accumulator));
                currentOperation = "=";
                accumulator = Double.MIN_VALUE;

            } else if (previousOperation.equalsIgnoreCase("/")) {
                accumulator = accumulator / double_stored_num2;
                Log.v("asdasd", "" + accumulator);
                calcScreen.setText(removeTrailingZeros(accumulator));
                currentOperation = "=";
                accumulator = Double.MIN_VALUE;

            }
        }else{}



    }
    public void pointPressed(){

        String string_screen= calcScreen.getText().toString();
        int start = calcScreen.getSelectionStart();
        if(!(string_screen.contains("."))) {
            String first = string_screen.substring(0, start);
            String second = string_screen.substring(start, string_screen.length());
            calcScreen.setText(first + "." + second);
            calcScreen.setSelection(start + 1);
        }else{}
        if (currentOperation!= null){
            calcScreen.setText(".");
            calcScreen.setSelection(1);}
    }

    public static String removeTrailingZeros(double d)
    {
        if(d == (long) d)
            return String.format("%d",(long)d);
        else
            return String.format("%s",d);
    }

    public void clearPressed(){
        calcScreen.setText("");
        accumulator = Double.MIN_VALUE;
        currentOperation = null;
        previousOperation = null;
        negative = false;


    }
    public void backspacePressed(){
        if(calcScreen.getText().length()>0) {
            String new_text;
            StringBuilder old_text = new StringBuilder(calcScreen.getText());
            old_text.deleteCharAt(old_text.length()-1);
            new_text = old_text.toString();

            if(new_text!="") {
                calcScreen.setText(new_text);
                if (accumulator != Double.MIN_VALUE) {
                    accumulator = Double.parseDouble(new_text);
                }else{}
                calcScreen.setSelection(calcScreen.getText().length());
            }else{
                calcScreen.setText("");
                accumulator = Double.MIN_VALUE;
                calcScreen.setSelection(calcScreen.getText().length());
            }
        }

    }
    public void precentagePressed(){

        if (!passIsSet && calcScreen.getText().length()==4){
            password = String.valueOf(calcScreen.getText());
            passIsSet = true;
            SharedPreferences pref = getApplicationContext().getSharedPreferences("preferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("userFirstLogin",passIsSet);
            editor.putString("password", password);
            editor.apply();
        }



        if(password.equalsIgnoreCase(String.valueOf(calcScreen.getText())) && confirmation){
            startActivity(new Intent(MainActivity.this, Vault.class));
        }


        if (passIsSet && !(confirmation)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Your password is set");
            builder.setMessage("Now you have set your password: " + password + ".  You are good to go");

            // add a button
            builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
            confirmation = true;
            SharedPreferences pref = getApplicationContext().getSharedPreferences("preferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("firstTime",true);
            editor.apply();
        }

    }
    public void oppositePressed() {
        String screen = String.valueOf(calcScreen.getText());
        if (accumulator!=0) {
            if (screen.contains("-")) {
                screen = screen.substring(1);
                calcScreen.setText(screen);
                calcScreen.setSelection(screen.length());
                negative = false;
                if(currentOperation != null) {
                    accumulator = accumulator * (-1);
                }else{}
                negative = false;

            } else {
                calcScreen.setText("-" + screen);
                calcScreen.setSelection(screen.length()+1);
                if(currentOperation != null) {
                    accumulator = accumulator * (-1);
                }else{}
                negative = true;
            }
        }
    }




}
