package com.example.sampleretainapp.UI.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.sampleretainapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AutoCompleteDialogFragment extends DialogFragment implements OnClickListener {

    public static AutoCompleteDialogFragment newInstance(String searchText) {
        AutoCompleteDialogFragment myFragment = new AutoCompleteDialogFragment();

        Bundle args = new Bundle();
        args.putString("searchString", searchText);
        myFragment.setArguments(args);

        return myFragment;
    }

    public interface ViewItemListener {
        public void onItemClicked(String item);
    }

    private static final String TAG = "AutoCompleteDialogFragment";
    public ViewItemListener viewItemListener;
    public List<String> cityList;
    //  private ListView list;
    private EditText filterText;
    private NameAdapter nameAdapter;
    private RecyclerView recyclerView;
    private ImageView searchClearButton;
    private String currentSearchTerm = "";
//
    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            nameAdapter.getFilter(s);
            currentSearchTerm = s.toString();
            if (currentSearchTerm.length() > 1) {
                searchClearButton.setVisibility(View.VISIBLE);
            } else {
                searchClearButton.setVisibility(View.GONE);
            }
        }
    };

    private void filter(String charText) {
        ArrayList<String> searchList = new ArrayList<>();
        //charText = charText.toUpperCase(Locale.getDefault());
        for (String model : cityList) {
            if (model.contains(charText)) {
                searchList.add(model);
            }
        }
        nameAdapter.setCities(searchList);
    }


    public List<String> getCityList() {
        return cityList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) dismiss();
        setStyle(DialogFragment.STYLE_NORMAL, R.style.full_screen_dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.autocomplete_dialog_fragment, container,
                false);
        filterText = view.findViewById(R.id.search_text);
        String searchString = getArguments().getString("searchString", "");
        if(filterText.requestFocus()) {
            getDialog().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
        filterText.addTextChangedListener(filterTextWatcher);
        searchClearButton = view.findViewById(R.id.clear_text);
        searchClearButton.setOnClickListener(view1 -> {
            filterText.getText().clear();
            InputMethodManager imm = (InputMethodManager) view1.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        });

        filterText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewItemListener.onItemClicked(textView.getText().toString());
                dismiss();
                return true;
            }
            return false;
        });
        recyclerView = view.findViewById(R.id.list_item_view);
        if (savedInstanceState != null) {
            dismiss();
        }
        Collections.sort(cityList);
        nameAdapter = new NameAdapter(getContext(), cityList,
                R.layout.autocomplete_list_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(nameAdapter);
        ImageButton cancelButton = view.findViewById(R.id.back_arrow);
        cancelButton.setOnClickListener(v -> dismiss());

        filterText.setText(searchString);
        filterText.setSelection(filterText.getText().length());

        return view;
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onStop() {
        super.onStop();
        filterText.removeTextChangedListener(filterTextWatcher);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    public class NameHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AppCompatTextView countryName;

        public NameHolder(Context context, @NonNull View itemView) {
            super(itemView);
            this.countryName = itemView.findViewById(R.id.auto_complete_title);
            itemView.setOnClickListener(this);
        }

        public void setCountryName(String countryName) {
            this.countryName.setText(countryName);
        }

        @Override
        public void onClick(View view) {
            viewItemListener.onItemClicked(countryName.getText().toString());
            AutoCompleteDialogFragment.this.dismiss();
        }
    }

    public class NameAdapter extends RecyclerView.Adapter<NameHolder> {

        void setCities(ArrayList<String> cities) {
            this.cities.clear();
            this.cities.addAll(cities);
            notifyDataSetChanged();
        }

        private ArrayList<String> cities = new ArrayList<>();
        private ArrayList<String> allCities = new ArrayList<>();

        private Context context;
        private int itemResource;

        NameAdapter(Context context, List<String> cities, int itemResource) {
            this.allCities.clear();
            this.cities.clear();
            this.allCities.addAll(cities);
            this.cities.addAll(cities);
            this.context = context;
            this.itemResource = itemResource;
        }

        @NonNull
        @Override
        public NameHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                             int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(this.itemResource, parent, false);
            return new NameHolder(this.context, view);
        }

        @Override
        public void onBindViewHolder(@NonNull NameHolder holder, int position) {
            String city = cities.get(position);
            holder.setCountryName(city);
        }

        @Override
        public int getItemCount() {
            return this.cities.size();
        }

        public void getFilter(CharSequence s) {
            ArrayList<String> temp = (ArrayList<String>) filter(allCities, s.toString());
            setCities(temp);
//      notifyDataSetChanged();
        }

        private List<String> filter(List<String> dataList, String newText) {
            newText = newText.toLowerCase();
            String text;
            ArrayList<String> filteredDataList = new ArrayList<>();
            for (String dataFromDataList : dataList) {
                text = dataFromDataList.toLowerCase();

                if (text.contains(newText)) {
                    filteredDataList.add(dataFromDataList);
                }
            }

            return filteredDataList;
        }
    }


}
