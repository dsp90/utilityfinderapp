package com.pawardushyant.foodfinderapp.views.activities;

import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pawardushyant.foodfinderapp.R;
import com.pawardushyant.foodfinderapp.utils.Commons;
import com.pawardushyant.foodfinderapp.utils.Constants;
import com.pawardushyant.foodfinderapp.viewmodels.SearchResultsListViewModel;
import com.pawardushyant.foodfinderapp.views.SearchPreferenceBottomSheet;
import com.pawardushyant.foodfinderapp.views.adapter.SearchResultsAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends FragmentActivity 
        implements TextWatcher, View.OnClickListener, SearchPreferenceBottomSheet.GetSearchCallback {

    private RecyclerView mRecyclerView;
    private SearchResultsAdapter mAdapter;
    private SearchResultsListViewModel mViewModel;
    private ImageButton ibShowSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideKeyboard();
        mViewModel = ViewModelProviders.of(this).get(SearchResultsListViewModel.class);
        initViews();
        initAdapter();
        subscribeToObservers();
        setDefSearch();
    }

    private void setDefSearch() {
        mViewModel.doSearch(null,null,Constants.DEFAULT_TYPE);
    }

    private void initAdapter() {
        mAdapter = new SearchResultsAdapter(Glide.with(this));
        mRecyclerView.hasFixedSize();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void subscribeToObservers() {
        mViewModel.getResultHits().observe(this, listResource -> {
            if (listResource.data != null){
                switch (listResource.status){
                    case LOADING:
                        break;
                    case SUCCESS:
                        mAdapter.setResultHits(listResource.data);
                        break;
                    case ERROR:
                        mAdapter.setResultHits(listResource.data);
                        Toast.makeText(MainActivity.this,
                                getString(R.string.something_wrong_occurred), Toast.LENGTH_LONG)
                                .show();
                        break;
                }
            }
        });
    }

    private void initViews() {
        EditText etSearch = findViewById(R.id.et_search);
        etSearch.addTextChangedListener(this);
        ibShowSheet = findViewById(R.id.btnSearchPref);
        ibShowSheet.setOnClickListener(this);
        mRecyclerView = findViewById(R.id.rv_search_results);
        findViewById(R.id.rv_parent).setOnClickListener(view -> hideKeyboard());
    }

    private void hideKeyboard() {
        Commons.hideKeyboard(MainActivity.this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String query = charSequence.toString();
        if (!TextUtils.isEmpty(query)){
            mRecyclerView.scrollToPosition(0);
            mViewModel.doSearch(null,null,query);
        } else{
            mViewModel.doSearch(null,null,Constants.DEFAULT_TYPE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    @Override
    public void onClick(View view) {
        openBottomSheet();
    }

    private void openBottomSheet() {
        SearchPreferenceBottomSheet preferenceBottomSheet =
                SearchPreferenceBottomSheet.newInstance();
        preferenceBottomSheet.show(getSupportFragmentManager(),
                SearchPreferenceBottomSheet.TAG);
    }

    @Override
    public void searchCallback(String type, String radius, Location location) {
        String loc = location != null ? location.getLatitude() + "," + location.getLongitude() :
                null;
        mViewModel.doSearch(loc, radius, type);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
