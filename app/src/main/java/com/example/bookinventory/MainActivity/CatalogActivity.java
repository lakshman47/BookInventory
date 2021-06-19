package com.example.bookinventory.MainActivity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;
import java.util.Currency;

import com.example.bookinventory.DataBase.BookAdapter;
import com.example.bookinventory.DataBase.BookDataBase;
import com.example.bookinventory.DataBase.BookEntry;
import com.example.bookinventory.DataBase.MainViewModel;
import com.example.bookinventory.EditActivity.DetailActivity;
import com.example.bookinventory.EditActivity.EditorActivity;
import com.example.bookinventory.Preference.PreferenceActivity;
import com.example.bookinventory.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.prefs.PreferenceChangeListener;

public class CatalogActivity extends AppCompatActivity implements BookAdapter.RecyclerViewListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private RecyclerView mListView;
    private RelativeLayout mEmptyView;
    private MainViewModel mainViewModel;
    private FloatingActionButton mFab;
    private Context mContext;
    private Menu MainMenu;
    private BookDataBase mDB;
    private TextView mOrderByToolbarResult;
    private TextView mOrderByToolbar;
    private int orderBy = 1;
    private BookAdapter bookAdapter;
    private LifecycleOwner lifecycleOwner;

    private String[] SORT_BY;

    private String select_sort ;
    private String select_currency;
    private Spinner mToolbarSpinner;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        init();
        setupViewModel();
        setSwipeForDelete();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    private void setupViewModel() {

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

    }

    private void init() {

        String title = getString(R.string.pref_order_by_author_label);
        String author = getString(R.string.pref_order_by_title_label);
        String price = getString(R.string.pref_order_by_price_label);
        String quant = getString(R.string.pref_order_by_stockcount_label);
        SORT_BY= new String[]{title,
                author,
                price,
                quant};
        select_sort = SORT_BY[0];
        mContext = this;
        bookAdapter = new BookAdapter(this, this);
        mListView = (RecyclerView) findViewById(R.id.list);
        mToolbarSpinner = (Spinner) findViewById(R.id.toolbar_spinner);
        setupSpinner();
        mListView.setLayoutManager(new LinearLayoutManager(this));
        mListView.setAdapter(bookAdapter);
        lifecycleOwner = this;
        mEmptyView = (RelativeLayout) findViewById(R.id.empty_view);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toEditorActivity();
            }
        });
        mOrderByToolbarResult = (TextView) findViewById(R.id.orderby_toolbar_results);
    }
    private void setupSharedPreference(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        select_currency = sharedPreferences.getString(getString(R.string.pref_currency_key),getString(R.string.pref_currency_default));
        select_sort = sharedPreferences.getString(getString(R.string.pref_order_by_key),getString(R.string.pref_order_by_default));
    }

    private void setupSpinner(){
        ArrayAdapter sortAdapter = ArrayAdapter.createFromResource(this,R.array.pref_order_by_column_labels,R.layout.spinner_title);
        sortAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        mToolbarSpinner.setAdapter(sortAdapter);

        mToolbarSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
                LiveData<List<BookEntry>> LiveData = mainViewModel.getAllBooks(SORT_BY[i]);
                select_sort = SORT_BY[i];
                setLiveData(LiveData,select_sort);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                setLiveData(mainViewModel.getAllBooks(),select_sort);
            }
        });
    }

    private void setSwipeForDelete() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                List<BookEntry> entries = bookAdapter.getBooksList();
                BookEntry bookEntry = entries.get(position);
                mainViewModel.deleteBook(bookEntry);
            }
        }).attachToRecyclerView(mListView);

    }

    private void setLiveData(LiveData<List<BookEntry>> liveData, final String Sort){
        liveData.observe(this, new Observer<List<BookEntry>>() {
            @Override
            public void onChanged(List<BookEntry> bookEntries) {
                if(select_sort == Sort) {
                    bookAdapter.updateAdapter(bookEntries);
                    Toast.makeText(mContext, "clicked" + Sort + bookEntries.size(), Toast.LENGTH_SHORT).show();
                    mOrderByToolbarResult.setText(bookEntries.size() + " Result Found");
                    if (bookAdapter.getItemCount() == 0) {
                        showEmptyView();
                    } else {
                        showContentView();
                    }
                }
            }
        });
    }

    private void showEmptyView() {
        mListView.setVisibility(View.INVISIBLE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    private void showContentView() {
        mListView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.INVISIBLE);

    }

    public void toEditorActivity() {
        Intent intent = new Intent(this, EditorActivity.class);
        startActivity(intent);
    }
    public void toDetailActivity(){
        Intent intent = new Intent(this, DetailActivity.class);
        startActivity(intent);
    }
    @Override
    public void onClickMethod(int Id) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("EDIT", Id);
        startActivity(intent);
    }

    @Override
    public void onSaleClickMethod(int Id) {
        BookEntry bookEntry = mainViewModel.getBookById(Id);
        int Quantity = bookEntry.getQuantity();

        if (Quantity == 0) {
            Toast.makeText(this, "Quantity is ZERO", Toast.LENGTH_SHORT).show();
            return;
        }
        bookEntry.setQuantity(Quantity-1);
        mainViewModel.updateBook(bookEntry);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int MenuItemId = item.getItemId();
        switch (MenuItemId) {
            case R.id.action_add:
                toEditorActivity();
                break;
            case R.id.action_preferences:
                openPreference();
                break;
            case R.id.action_delete_all_entries:
                deleteAllData();
                break;
        }
        return true;
    }

    private void deleteAllData() {
        mainViewModel.deleteAllBooks();
    }

    private void openPreference(){
        Intent intent = new Intent(this, PreferenceActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.pref_currency_key))) {
            String Value = sharedPreferences.getString(key, getString(R.string.pref_currency_0_label_value));
            bookAdapter.updateCurrency();
        }
        else if(key.equals(getString(R.string.pref_order_by_key))){
            String Value = sharedPreferences.getString(key, getString(R.string.pref_order_by_author_value));
        }
    }

}