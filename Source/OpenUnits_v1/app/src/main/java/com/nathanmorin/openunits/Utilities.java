/**
 * Copyright (c) 2016 Nathan Morin
 * Created by Nathan Morin (nathanmorin.com) as part of CNIT 355 @ Purdue University
 */
package com.nathanmorin.openunits;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Map;

/**
 * Created by nathan on 10/2/16.
 */

public class Utilities {

    /**
     * Builds Unit Conversion Result table into Linear Layout specified
     */
    public static void RenderResults(Map<String, Double> results, LinearLayout sl, Context context) {



        if (results == null || sl == null || context == null) return;

        sl.removeAllViews();

        for (Map.Entry<String, Double> result : results.entrySet()) {
            String unit = result.getKey();
            String value = result.getValue().toString();

            LinearLayout layout = new LinearLayout(context);

            layout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.FILL_PARENT);

            containerParams.setMargins(0, 0, 0, 10);

            layout.setLayoutParams(containerParams);
            layout.setBackgroundResource(R.drawable.customboarder);


            TextView tvValue = new TextView(context);
            TextView tvUnit = new TextView(context);

            //Format text views

            LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1f);


            tvValue.setLayoutParams(tvParams);
            tvUnit.setLayoutParams(tvParams);


            tvValue.setText(value.toString());
            tvUnit.setText(unit.toString());


            layout.addView(tvValue);
            layout.addView(tvUnit);


            sl.addView(layout);
        }


    }
}
