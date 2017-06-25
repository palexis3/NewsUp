package com.example.palexis3.newssum.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.palexis3.newssum.R;

public class FilterDialogFragment extends DialogFragment {

    private Spinner countrySpinner;
    private Spinner languageSpinner;
    private Spinner categorySpinner;
    private TextView tvOkay;
    private TextView tvCancel;

    // empty constructor
    public FilterDialogFragment() {};

    // define a listener interface to pass data back
    public interface FilterDialogListener {
        void onFinishFilter(String data);
    }

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

        countrySpinner = (Spinner) view.findViewById(R.id.spinnerCountry);
        languageSpinner = (Spinner) view.findViewById(R.id.spinnerLanguage);
        categorySpinner = (Spinner) view.findViewById(R.id.spinnerCategory);
        tvOkay = (TextView) view.findViewById(R.id.tv_okay);
        tvCancel = (TextView) view.findViewById(R.id.tv_cancel);

        // create adapters for populating spinners with appropriate items
        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.countries_array,
                android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> languageAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.languages_array,
                android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.categories_array,
                android.R.layout.simple_spinner_dropdown_item);

        // Specify the layout to use when the list of choices appears
        countryAdapter.setDropDownViewResource(R.layout.my_spinner_item);
        languageAdapter.setDropDownViewResource(R.layout.my_spinner_item);
        categoryAdapter.setDropDownViewResource(R.layout.my_spinner_item);


        // Apply the adapters to the spinners
        countrySpinner.setAdapter(countryAdapter);
        languageSpinner.setAdapter(languageAdapter);
        categorySpinner.setAdapter(categoryAdapter);

        // set onclick listeners for the 'ok' and 'cancel' textviews
        tvOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String language = languageSpinner.getSelectedItem().toString();
                if(language.equals("--")) {
                    language = "";
                }
                String country = countrySpinner.getSelectedItem().toString();
                if(country.equals("--")) {
                    country = "";
                }
                String category = categorySpinner.getSelectedItem().toString();
                if(category.equals("--")) {
                    category = "";
                }

                String res = String.format("%s,%s,%s", language, country, category);

                // implement listener to return back to parent activity
                FilterDialogListener listener = (FilterDialogListener) getActivity();
                listener.onFinishFilter(res);

                // close the dialog and return back to parent activity
                dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

    }
}
