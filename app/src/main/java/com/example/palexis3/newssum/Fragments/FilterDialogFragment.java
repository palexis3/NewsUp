package com.example.palexis3.newssum.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.palexis3.newssum.R;

public class FilterDialogFragment extends DialogFragment {

    private Spinner countrySpinner;
    private Spinner languageSpinner;
    private Spinner categorySpinner;

    // empty constructor
    public FilterDialogFragment() {};

    public static FilterDialogFragment newInstance(String title) {
        FilterDialogFragment frag = new FilterDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter, container);
    }

    /*
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String title = getArguments().getString("title");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage("Filter");

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.fragment_filter, null);
        builder.setView(view);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String language = languageSpinner.getSelectedItem().toString();
                String country = countrySpinner.getSelectedItem().toString();
                String category = categorySpinner.getSelectedItem().toString();
                String res = String.format("Language: %s, Country: %s, Category: %s", language, country, category);
                Toast.makeText(getContext(), res, Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }
    */


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        countrySpinner = (Spinner) view.findViewById(R.id.spinnerCountry);
        languageSpinner = (Spinner) view.findViewById(R.id.spinnerLanguage);
        categorySpinner = (Spinner) view.findViewById(R.id.spinnerCategory);

        // create adapters for populating spinners with appropriate items
        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.countries_array,
                android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> languageAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.languages_array,
                android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.categories_array,
                android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapters to the spinners
        countrySpinner.setAdapter(countryAdapter);
        languageSpinner.setAdapter(languageAdapter);
        categorySpinner.setAdapter(categoryAdapter);

    }
}
