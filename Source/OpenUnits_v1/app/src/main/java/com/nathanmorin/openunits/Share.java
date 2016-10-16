/**
 * Copyright (c) 2016 Nathan Morin
 * Created by Nathan Morin (nathanmorin.com) as part of CNIT 355 @ Purdue University
 */
package com.nathanmorin.openunits;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

public class Share extends AppCompatActivity {

    //Define data which is sent from Main Activity
    HashMap<String, Double> results = new HashMap<>();
    String conversionValue = "";
    String conversionFrom = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        LinearLayout layout = (LinearLayout)findViewById(R.id.shareScrollLayout);

        /**
         * Get Values from MainActivity
         */
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null)
        {
            results = (HashMap<String, Double>) bundle.getSerializable("conversionResults");
            conversionValue = bundle.getString("conversionValue");
            conversionFrom = bundle.getString("conversionFrom");
        }

        //Render results which can be shared by clicking share button
        Utilities.RenderResults(results,layout,getApplicationContext());

    }

    /**
     * Handler method for share button.  Uses data send from MainActity to share plain text using
     * default android share intent
     * @param v
     */
    public void btnShareClick(View v)
    {
        //Format body of text which will be shared
        String mainText = "Converting " + conversionValue + " " + conversionFrom + " to ...\n";
        for (Map.Entry<String,Double> re : results.entrySet())
        {
            mainText += re.getValue() + " " + re.getKey() + "\n";
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND); //Create new sharing intent
        shareIntent.setType("text/plain");  //Set intent to accept text data
        shareIntent.putExtra(Intent.EXTRA_SUBJECT,"OpenUnits Conversion " + conversionValue + " " + conversionFrom); //Set subject of shared message
        shareIntent.putExtra(Intent.EXTRA_TEXT,mainText); //Set body text of shared message
        startActivity(Intent.createChooser(shareIntent,"Share with")); //Open android share intent
    }
}
