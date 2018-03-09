package com.example.android.currencycrunch;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConvertFragment extends Fragment {

    private FixerConvert converter;
    private EditText convertedittext;
    private TextView convertabletext;
    private TextView totalTotext;
    private TextView fromToText;
    private TextView fromSignText;
    private TextView totalSign;

    private CurrencyAdapter currencyAdapter;
    private RecyclerView currencyRecyclerView;

    private double[] coinArr;
    private double[] coinArrCounter;

    public ConvertFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_convert, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        convertedittext = (EditText) getView().findViewById(R.id.convertedittext);
        convertedittext.setHint(Preferences.getChosenFromCurrency()+" - "+Preferences.getChosenToCurrency());
        Button convertbutton = (Button) getView().findViewById(R.id.convertbutton);

        convertbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (convertedittext.getText().toString().matches("")) {
                    Toast.makeText(getActivity(), "Please specify text to process", Toast.LENGTH_LONG).show();
                }
                else {
                    new Converting().execute();
                }
            }
        });

        //for the coins list
        currencyRecyclerView = (RecyclerView) getView().findViewById(R.id.recyclerview_coins);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        currencyRecyclerView.setLayoutManager(layoutManager);
        currencyRecyclerView.setHasFixedSize(true);


        int width, height;
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        height = (int)(metrics.heightPixels*0.5);
        currencyRecyclerView.setLayoutParams(new LinearLayout.LayoutParams(width, height));

        currencyAdapter = new CurrencyAdapter(getActivity(), ConvertFragment.this);

        currencyRecyclerView.setAdapter(currencyAdapter);

        coinArr = Preferences.getCoinsFloats(Preferences.getChosenToCurrencyCode());
        coinArrCounter = new double[coinArr.length];


    }

    public void converted(){
        String valueToConvert = convertedittext.getText().toString(); //get the value of text
        Double valueConverted = converter.convert(Preferences.getChosenFromCurrencyCode(), Preferences.getChosenToCurrencyCode(), Double.parseDouble(valueToConvert));
        convertabletext = (TextView) getView().findViewById(R.id.convertabletext);
//        convertabletext.setText(valueConverted.toString());
        convertabletext.setText(String.format( "%.2f", valueConverted ).replace(',','.'));
    }

    public void setTotalTo(int pos, int counter) {

        coinArrCounter[pos] = coinArr[pos]*counter;
        Double sum = 0.0;
        for (double d : coinArrCounter)
            sum += d;
        totalTotext = (TextView) getView().findViewById(R.id.totalToTextView);
        totalTotext.setText( String.format( "%.2f", sum ).replace(',','.') );

        

//        Double valueṬoConvert = sum;
//        System.out.println("@@@@@@@1"+sum.getClass());
//        System.out.println("@@@@@@@1"+sum.getClass());
//        System.out.println(sum.toString().getClass());
//        Double valueCoṇverted = converter.convert(Preferences.getChosenFromCurrencyCode(), Preferences.getChosenToCurrencyCode(),sum);
//        System.out.println("******* " + valueCoṇverted);
//
//
//        fromToText = (TextView) getView().findViewById(R.id.fromValue);
//        fromToText.setText( String.format( "%.2f", valueConverted));
//
//         need double
//        System.out.println("*********get class1: "+String.format( "%.2f", sum ).getClass()); //string
//        System.out.println("*********get class2: "+String.format( "%.2f", sum ).getClass());
//
//
//        String currSigns = Preferences.getChosenCurrSigns();
//        String[] splitSigns = currSigns.split("&&");
//        String fromSign = splitSigns[0];
//        String toSign = splitSigns[1];
//        System.out.println(splitSigns);
//        System.out.println("From:" + fromSign);
//        System.out.println("To:" + toSign);
//        totalSign = (TextView) getView().findViewById(R.id.totalSign);
//        totalSign.setText(toSign.toString());
//        fromSignText = (TextView) getView().findViewById(R.id.fromSign);
//        fromSignText.setText(fromSign.toString());
//
//        convertedittext.setText(String.format( "%.2f", sum ).replace(',','.'));

    }

////////////////////////////////////////////////////////////////////////////////////////////////
    private class Converting extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... params) {

            try {
                converter = new FixerConvert();
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            //start the progress dialog
            progress = ProgressDialog.show(getActivity(), null, "Translating...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            progress.dismiss();

            super.onPostExecute(result);
            converted();
        }


    }


}
