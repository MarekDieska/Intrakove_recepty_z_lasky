package com.example.dvojplatnicka;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dvojplatnicka.R;

public class MainActivity extends AppCompatActivity {

    private Button button1, button2, buttonBack;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        buttonBack = findViewById(R.id.buttonBack);
        textView = findViewById(R.id.startText);
    }

    public void handleButtonClick(View view) {
        // Hide the buttons at the top
        button1.setVisibility(View.GONE);
        button2.setVisibility(View.GONE);

        // Show the "Späť" button at the bottom
        buttonBack.setVisibility(View.VISIBLE);

        // Handle which button was clicked
        if (view.getId() == R.id.button1) {
            textView.setText("1\n" +
                    "Zemiaky očistíme, nastrúhame. Zmiešame s múkou, celým vajcom a roztlačeným cesnakom. Vzniknuté cesto osolíme a okoreníme.\n" +
                    "\n" +
                    "2\n" +
                    "Placky robíme ako palacinky (t.j. na panvici a oleji robíme tvary aké chceme v dostatočnej hrúbke), buď dávame viac kúskov na jeden krát - bude ich viac menších, alebo po jednej, čiže budú väčšie.\n" +
                    "\n" +
                    "3\n" +
                    "Môžeme ich jesť s kyslou smotanou alebo zakysankou, prípadne niečím podobným.\n");
        } else if (view.getId() == R.id.button2) {
            textView.setText("1.\n" +
                    "V hrnci rozpálime olej. Pridáme nastrúhaný zázvor, nasekanú citrónovú trávu, kari a čili pastu. Za stáleho miešania prilejeme horúci kurací vývar, trstinový cukor a polievku varíme približne 15 minút.\n" +
                    "\n" +
                    "2.\n" +
                    "Do menšieho hrnca nalejeme kokosové mlieko. Doň vložíme huby šitake nakrájané na plátky, za občasného miešania varíme domäkka približne 5 minút.\n" +
                    "\n3.\n" +
                    "Predvarené huby s mliekom vlejeme do hrnca s vývarom. Pridáme na kocky nakrájané kuracie prsia a varíme ďalších 7 minút. Dochutíme soľou, korením a limetovou šťavou. Pred podávaním polievku ozdobíme čili papričkou.\n" +
                    "\n4.\n" +
                    "TIP: Ak máme radi štipľavé môžeme čili papričku nakrájať a zjesť.\n");
        }
    }

    public void handleBackButtonClick(View view) {
        // Show the buttons at the top
        button1.setVisibility(View.VISIBLE);
        button2.setVisibility(View.VISIBLE);

        // Hide the "Späť" button at the bottom
        buttonBack.setVisibility(View.GONE);

        // Reset TextView if needed
        textView.setText("");
    }
}