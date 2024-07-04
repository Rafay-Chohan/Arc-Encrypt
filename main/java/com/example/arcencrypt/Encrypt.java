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


public class Encrypt extends Fragment {

    MainActivity mainActivity;
    private EditText inputTextID;
    private EditText adderInputID;
    private EditText multiplierInputID;
    private TextView resultTextID;
    private Button btn;
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
    public Encrypt() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        mainActivity = (MainActivity)getActivity();
        View view= inflater.inflate(R.layout.fragment_encrypt, container, false);
        adderInputID=view.findViewById(R.id.adderInputID);
        adderInputID.setText("1");
        multiplierInputID=view.findViewById(R.id.multiplierInputID);
        multiplierInputID.setText("1");
        resultTextID=view.findViewById(R.id.resultTextID);
        btn=view.findViewById(R.id.cipherButtonID);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultTextID.setText("");
                inputTextID=view.findViewById(R.id.inputTextID);
                String input=inputTextID.getText().toString();
                String adderInput=adderInputID.getText().toString().trim();
                String multiplierInput=multiplierInputID.getText().toString().trim();
                if(!adderInput.equals("") && !multiplierInput.equals("")){
                    int multiplierInputvalue=1;
                    int adderInputvalue=1;
                    multiplierInputvalue=(int)Double.parseDouble(multiplierInput);
                    adderInputvalue=(int)Double.parseDouble(adderInput);
                    if(!input.equals("")) {
                        if(!(adderInputvalue>61 || adderInputvalue<1)){
                            if(!(multiplierInputvalue>61 || multiplierInputvalue<1 || !checkCoPrime(multiplierInputvalue)))
                            {
                                String st = "";
                                char c;
                                int flag=0;
                                for (int i = 0; i < input.length(); i++) {
                                    int letter = ((multiplierInputvalue * getNum(input.charAt(i))) + adderInputvalue) % 62;
                                    if(getNum(input.charAt(i))!= -1){
                                        flag=1;
                                        st = st + getAlphabet(letter);
                                    }else{
                                        st = st + input.charAt(i);}
                                }
                                if(flag==1)
                                    resultTextID.setText(st);
                                else
                                    inputTextID.setError("Invalid Input Text");
                            }
                            else
                            {
                                if(validInput(input)==0)
                                {
                                    inputTextID.setError("Invalid Input Text");
                                }
                                multiplierInputID.setError("Invalid Multiplier Input");
                            }
                        }
                        else{
                            if(validInput(input)==0)
                            {
                                inputTextID.setError("Invalid Input Text");
                            }
                            if(multiplierInputvalue>61 || multiplierInputvalue<1 || !checkCoPrime(multiplierInputvalue))
                            {
                                multiplierInputID.setError("Invalid Multiplier Input");
                            }
                            adderInputID.setError("Invalid Adder Input");
                        }
                    }
                    else{
                        if(multiplierInputvalue>61 || multiplierInputvalue<1 || !checkCoPrime(multiplierInputvalue))
                        {
                            multiplierInputID.setError("Invalid Multiplier Input");
                        }
                        if(adderInputvalue>61 || adderInputvalue<1)
                        {
                            adderInputID.setError("Invalid Adder Input");
                        }
                        inputTextID.setError("Invalid Input Text");
                    }
                }else{
                    if(multiplierInput.equals(""))
                    {
                        multiplierInputID.setError("Invalid Multiplier Input");
                    }
                    if(adderInput.equals(""))
                    {
                        adderInputID.setError("Invalid Adder Input");
                    }
                    if(input.equals("")){
                        inputTextID.setError("Invalid Input Text");
                    }
                    else if(validInput(input)==0)
                    {
                        inputTextID.setError("Invalid Input Text");
                    }

                }
                adderInputID.setText("1");
                multiplierInputID.setText("1");
            }
        });
        resultTextID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard= (ClipboardManager) mainActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip= ClipData.newPlainText("Message",resultTextID.getText().toString());
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
