package com.example.arcencrypt;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;


public class Decrypt extends Fragment {

    MainActivity mainActivity;
    private EditText inputTextDecID;

    private EditText adderInputDecID;

    private EditText multiplierInputDecID;
    private TextView resultTextDecID;
    private Button btn2;
    private static final Map<Character, Integer> chartoValue = new HashMap<>();
    private static final Map<Integer, Character> valuetoChar = new HashMap<>();
    static {
        for (int i = 0; i <= 9; i++)
            chartoValue.put((char) ('0' + i), i);
        int j=0;
        for (int i = 0; i < 26; i++) {
            chartoValue.put((char) ('A' + i), 10 + i+j);
            chartoValue.put((char) ('a' + i), 10 + i + 1+j);
            j++;
        }
        for (Map.Entry<Character, Integer> entry : chartoValue.entrySet())
            valuetoChar.put(entry.getValue(), entry.getKey());
    }

    public Decrypt() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainActivity = (MainActivity)getActivity();
        View view = inflater.inflate(R.layout.fragment_decrypt, container, false);
        adderInputDecID=view.findViewById(R.id.adderInputDecID);
        adderInputDecID.setText("1");
        multiplierInputDecID=view.findViewById(R.id.multiplierInputDecID);
        multiplierInputDecID.setText("1");
        resultTextDecID=view.findViewById(R.id.resultTextDecID);
        btn2=view.findViewById(R.id.decipherButtonID);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultTextDecID.setText("");
                inputTextDecID=view.findViewById(R.id.inputTextDecID);
                String input=inputTextDecID.getText().toString();
                String adderInputDec=adderInputDecID.getText().toString().trim();
                String multiplierInputDec=multiplierInputDecID.getText().toString().trim();
                if(!adderInputDec.equals("") && !multiplierInputDec.equals("")){
                    int multiplierInputDecvalue=1;
                    int adderInputDecvalue=1;
                    multiplierInputDecvalue=(int)Double.parseDouble(multiplierInputDec);
                    adderInputDecvalue=(int)Double.parseDouble(adderInputDec);
                    if(!input.equals("")) {
                        if(!(adderInputDecvalue>61 || adderInputDecvalue<1)){
                            if(!(multiplierInputDecvalue>61 || multiplierInputDecvalue<1 || !checkCoPrime(multiplierInputDecvalue)))
                            {
                                String st = "";
                                char c;
                                for (int i = 0; i < input.length(); i++) {
                                    int letter = getNum(input.charAt(i));
                                    int val=decryptChar(letter,multiplierInputDecvalue,adderInputDecvalue);
                                    if(val!= -1){
                                        st = st + getAlphabet(val);
                                    }else{
                                        st = st + input.charAt(i);}
                                }
                                resultTextDecID.setText(st);
                            }
                            else
                            {
                                if(validInput(input)==0)
                                {
                                    inputTextDecID.setError("Invalid Input Text");
                                }
                                multiplierInputDecID.setError("Invalid Multiplier Input");
                            }
                        }
                        else{
                            if(validInput(input)==0)
                            {
                                inputTextDecID.setError("Invalid Input Text");
                            }
                            if(multiplierInputDecvalue>61 || multiplierInputDecvalue<1 || !checkCoPrime(multiplierInputDecvalue))
                            {
                                multiplierInputDecID.setError("Invalid Multiplier Input");
                            }
                            adderInputDecID.setError("Invalid Adder Input");
                        }
                    }
                    else{
                        if(multiplierInputDecvalue>61 || multiplierInputDecvalue<1 || !checkCoPrime(multiplierInputDecvalue))
                        {
                            multiplierInputDecID.setError("Invalid Multiplier Input");
                        }
                        if(adderInputDecvalue>61 || adderInputDecvalue<1)
                        {
                            adderInputDecID.setError("Invalid Adder Input");
                        }
                        inputTextDecID.setError("Invalid Input Text");
                    }
                }else{
                    if(multiplierInputDec.equals(""))
                    {
                        multiplierInputDecID.setError("Invalid Multiplier Input");
                    }
                    if(adderInputDec.equals(""))
                    {
                        adderInputDecID.setError("Invalid Adder Input");
                    }
                    if(input.equals("")){
                        inputTextDecID.setError("Invalid Input Text");
                    }
                    else if(validInput(input)==0)
                    {
                        inputTextDecID.setError("Invalid Input Text");
                    }

                }
                adderInputDecID.setText("1");
                multiplierInputDecID.setText("1");
            }
        });
        resultTextDecID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard= (ClipboardManager) mainActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip= ClipData.newPlainText("Message",resultTextDecID.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(mainActivity,"Copied", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
    public static int getNum(char letter) {
        return chartoValue.getOrDefault(letter, -1);
    }
    public static char getAlphabet(int number) {
        return valuetoChar.getOrDefault(number, ' ');
    }

    public static int decryptChar(int letter,int m,int a){

        for(int i=0;i<62;i++){
            if(((m*i)+a)%62==letter){
                return i;
            }
        }
        return -1;
    }
    public static int validInput(String input){
        for (char ch : input.toCharArray()) {
            if (Character.isLetter(ch)) {
                return 1;
            } else if (Character.isDigit(ch)) {
                return 1;
            }
        }
        return 0;
    }
    public static boolean checkCoPrime(int n) {
        if (n <= 0) {
            return false;
        }
        if (n <= 3) {
            return true;
        }
        if (n % 2 == 0 || n % 3 == 0) {
            return false;
        }
        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }
}
