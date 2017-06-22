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

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterDialogFragment extends DialogFragment {

    @BindView(R.id.spinnerCountry) Spinner countrySpinner;
    @BindView(R.id.spinnerLanguage) Spinner languageSpinner;
    @BindView(R.id.spinnerCategory) Spinner categorySpinner;

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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(getActivity());

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
