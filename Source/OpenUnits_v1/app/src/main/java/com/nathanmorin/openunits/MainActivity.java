/**
 * Copyright (c) 2016 Nathan Morin
 * Created by Nathan Morin (nathanmorin.com) as part of CNIT 355 @ Purdue University
 */
package com.nathanmorin.openunits;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.gms.common.api.GoogleApiClient;

import android.text.SpannableString;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class MainActivity extends AppCompatActivity {
    private static Context mContext;



    //Order listed is order which will appear on tab.
    private static int[] imageResId = {
            R.drawable.temperature,
            R.drawable.ruler_and_pencil,
            R.drawable.weights,
            R.drawable.information
    };


    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mContext = this;







    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        /**
        Define conversion table class which is used to hold table of ratios for converting between
        like units
         **/
        private class conversionTable {
            private List<String> Units;
            private List<Double> Values;

            public conversionTable(List<String> Units, List<Double> Values) {
                this.Units = Units;
                this.Values = Values;
            }

            public List<String> getUnits() {
                return Units;
            }

            public List<Double> getValues() {
                return Values;
            }
        }

        private conversionTable[] tables = {
                null, //Temperature placeholder, temperature not being a direct ratio
                new conversionTable( //Conversion table for length
                        Arrays.asList("km", "m", "cm", "mm", "in", "ft", "yd", "mile"),
                        Arrays.asList(100.0, 100.0, 10.0, 1.0 / 25.4, 1.0 / 12.0, 1.0 / 3.0, 1.0 / 1760.0)),
                new conversionTable( //Conversion table for weight/mass
                        Arrays.asList("kg", "g", "mg", "oz", "lb", "ton"),
                        Arrays.asList(1000.0, 1000.0, 1.0 / 28349.5, 1.0 / 16.0, 1.0 / 2000.0))
        };

        private static int[] spinnerItems = {
                R.array.array_temperature_units,
                R.array.array_length_units,
                R.array.array_weight_units
        };

        private static String[] categories = {
                "Temperature", "Distance", "Weight"
        };


        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }



        /**
        Calculates set unit conversions given
         {@link double} value to convert from
         {@link String} units to be converted from
         {@link int} ID to drawable string resource which lists units to convert into
         {@link conversionTable} table of values used to convert between units with simple ratio conversions
         **/
        private HashMap<String, Double> CalculateRatioConversions(double value, String unitFrom, int unitsToID, conversionTable table) {
            HashMap<String, Double> results = new HashMap<String, Double>();

            String[] unitsToList = getResources().getStringArray(unitsToID);

            for (String unitTo : unitsToList) {
                results.put(unitTo, convertUnit(value, unitFrom, unitTo, table));
            }


            return results;

        }

        /**
         * Convert from one unit to another given ...
         * @param value numeric value of starting unit
         * @param unitFrom unit of the starting value
         * @param unitTo unit to convert starting value into
         * @param table unit conversion table
         * @return value converted into the new unit
         */
        private Double convertUnit(double value, String unitFrom, String unitTo, conversionTable table) {


            int start = table.getUnits().indexOf(unitFrom);
            int end = table.getUnits().indexOf(unitTo);

            double calc = 1.0;

            for (int i = min(start, end); i < max(start, end); i++)
                calc = calc * table.getValues().get(i);

            if (start == end) {
                return value;
            } else if (start < end) {
                return value * calc;
            } else {
                return value / calc;
            }

        }

        /**
         * Uses RenderResults to update UI when units or value to be converted are changed.
         * @param tabIndex Current UI tab
         * @param spinner Spinner selection object
         * @param text TextEdit input object
         * @param sl Linear Layout containing scroll view
         */
        private void updateView(int tabIndex, Spinner spinner, EditText text, LinearLayout sl) {
            
            Utilities.RenderResults(getResultsFromView(tabIndex,spinner,text), sl, getContext());
            
        }

        /**
         * Calculate hash table of converted units given UI elements
         * @param tabIndex current UI tab
         * @param spinner Spinner selection object
         * @param text TextEdit input object
         * @return HashMap containing String, Double pairs of (UNIT, CONVERTED VALUE) in each pair
         */
        private HashMap<String, Double> getResultsFromView(int tabIndex, Spinner spinner, EditText text) {
            String unitFrom = spinner.getSelectedItem().toString();
            String s = text.getText().toString();
            HashMap<String, Double> tempResults;


            if (s == "") return null;

            double value;
            try {
                value = Double.parseDouble(s.toString());
            } catch (Exception ex) {
                return null;
            }


            if (categories[tabIndex - 1] == "Temperature") {
                tempResults = new HashMap<>();

                switch (unitFrom) {
                    case "C":

                        tempResults.put("F", value * 9.0 / 5.0 + 32.0);
                        tempResults.put("K", value + 273.15);
                        tempResults.put("C", value);

                        break;

                    case "F":

                        tempResults.put("F", value);
                        tempResults.put("K", (value + 459.67) / (9 / 5));
                        tempResults.put("C", value * 9.0 / 5.0 + 32.0);

                        break;
                    case "K":

                        tempResults.put("F", value * 9.0 / 5.0 - 459.67);
                        tempResults.put("K", value);
                        tempResults.put("C", value - 273.15);

                        break;
                }


            } else {


                tempResults = CalculateRatioConversions(value, unitFrom, spinnerItems[tabIndex - 1], tables[tabIndex - 1]);

            }
            return tempResults;
        }


        /**
         * Override the default create view method to insert individual views for each tab
         * @param inflater
         * @param container
         * @param savedInstanceState
         * @return
         */
        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {

            final int tabIndex = getArguments().getInt(ARG_SECTION_NUMBER);

            //Tabs less than or equal to 3 are unit conversion pages and are rendered with fragment_main layout
            if (tabIndex <= 3) {
                
                //Setup variables
                final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

                final EditText numberInput = (EditText) rootView.findViewById(R.id.numberInput);

                final Spinner selectConversion = (Spinner) rootView.findViewById(R.id.selectConversion);

                final LinearLayout sl = (LinearLayout) rootView.findViewById(R.id.scrollLayout);


                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                        spinnerItems[tabIndex - 1], android.R.layout.simple_spinner_dropdown_item);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                selectConversion.setAdapter(adapter);


                /**
                 * Set on selected listener to updateView when new unit is selected in UI Spinner
                 */
                selectConversion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        updateView(tabIndex, selectConversion, numberInput, sl);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


                /**
                 * Set on text changed listener to updateView when input number is changed
                 */
                numberInput.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        updateView(tabIndex, selectConversion, numberInput, sl);

                    }

                    @Override
                    public void afterTextChanged(Editable s) {



                    }

                });


                /**
                 * Setup Floating Action Share button.
                 *
                 * This button launches a second intent which displays text which will be shared
                 * and allows users to share that text in the android share dialog.
                 */
                FloatingActionButton shareButton = (FloatingActionButton)rootView.findViewById(R.id.floatingActionButton);
                shareButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Create new intent based on Share class
                        Intent share = new Intent(mContext, Share.class);
                        Bundle results = new Bundle();

                        //Insert current unit calculations into the new intent
                        results.putSerializable("conversionResults",getResultsFromView(tabIndex,selectConversion,numberInput));
                        share.putExtras(results);
                        share.putExtra("conversionFrom",selectConversion.getSelectedItem().toString());
                        share.putExtra("conversionValue",numberInput.getText().toString());

                        //Launch Intent
                        mContext.startActivity(share);
                    }
                });


                return rootView;

            /**
             * Last Page is information page for app which uses fragment_information layout.
             */
            } else if (tabIndex == 4) { //Render info page
                final View rootView = inflater.inflate(R.layout.fragment_information, container, false);


                return rootView;
            } else {
                return null;
            }


        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }


        @Override
        public CharSequence getPageTitle(int position) {

            Drawable image = ContextCompat.getDrawable(getApplicationContext(), imageResId[position]);
            image.setBounds(0, 0, 100, 100);
            SpannableString sb = new SpannableString(" ");
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            return sb;

        }
    }


}
