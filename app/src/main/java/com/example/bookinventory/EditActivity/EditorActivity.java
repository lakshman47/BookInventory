package com.example.bookinventory.EditActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bookinventory.DataBase.BookAdapter;
import com.example.bookinventory.DataBase.BookEntry;
import com.example.bookinventory.DataBase.EditViewModel;
import com.example.bookinventory.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Currency;

public class EditorActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private EditText mTitleEditText, mAuthorEditText, mQuantityEditText, mPriceEditText;
    private FloatingActionButton mDoneFab;
    private EditViewModel mEditViewModel;
    private BookEntry mBookEntry;
    private Spinner mSupplierSpinner;
    int SupplierId = 0;

    public static final String EDIT_ACTIVITY = "edit-activity";
    public static final String NEW_ACTIVITY = "new-activity";
    public static String ACTIVITY_TAG = NEW_ACTIVITY;
    private SharedPreferences sharedPreferences;
    private String Currency_Pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        init();
        setupViewModel();
        Intent intent = getIntent();
        if(intent.hasExtra("EDIT")){
            int Id = intent.getIntExtra("EDIT",0);
            ACTIVITY_TAG = EDIT_ACTIVITY;
            mBookEntry = mEditViewModel.getBookById(Id);
            setContent(mBookEntry);
        }
        else{
        }
    }

    @Override
    protected void onResume() {
        if(Currency_Pref.equals(getString(R.string.pref_currency_0_label_value))){
            mPriceEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_rupee, 0);
        }
        else if(Currency_Pref.equals(getString(R.string.pref_currency_1_label_value))){
            mPriceEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_dollar_sign_symbol_bold_text_svgrepo_com, 0);
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    private void setContent(BookEntry bookEntry) {
        SupplierId = bookEntry.getSupplierId();
        mTitleEditText.setText(bookEntry.getTitle());
        mAuthorEditText.setText(bookEntry.getAuthor());

        mPriceEditText.setText(String.valueOf(getPrefPrice(bookEntry.getPrice())));
        mQuantityEditText.setText(String.valueOf(bookEntry.getQuantity()));
        mSupplierSpinner.setSelection(SupplierId);
    }

    private void setupSupplierSpinner(){

        ArrayAdapter supplierAdapter = ArrayAdapter.createFromResource(this,R.array.array_supplier_options, android.R.layout.simple_spinner_item);
        supplierAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        mSupplierSpinner.setAdapter(supplierAdapter);
        mSupplierSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SupplierId = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                SupplierId = 0;
            }
        });
    }
    public String getPrefPrice(int Price){
        if(Currency_Pref.equals(getString(R.string.pref_currency_0_label_value))){
            return String.valueOf(Price);
        }
        else{
            double double_price = (Price/80.0);
            String price_in_format = String.format("%.2f",double_price);
            return price_in_format;
        }
    }

    private void setupViewModel() {

        mEditViewModel = ViewModelProviders.of(this).get(EditViewModel.class);

    }

    private void init() {

        mTitleEditText = findViewById(R.id.title_edit_text);
        mAuthorEditText = findViewById(R.id.author_edit_text);
        mQuantityEditText = findViewById(R.id.quantity_edit_text);
        mPriceEditText = findViewById(R.id.price_edit_text);
        mDoneFab = findViewById(R.id.done_fab);
        mSupplierSpinner = findViewById(R.id.supplierSpinner);
        setupSupplierSpinner();
        mDoneFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBookData();
            }
        });
        sharedPreferences  = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        Currency_Pref = sharedPreferences.getString(getString(R.string.pref_currency_key),getString(R.string.pref_currency_default));

    }

    private boolean isDataValid(String Title,String Author,Integer Price,Integer Quantity){

        if(Title == null || Title.isEmpty() || Author == null || Author.isEmpty() || Price == null || Quantity == null || SupplierId == 0){
            return false;
        }

        return true;

    }

    private int convertToRuppee(int Price){
        if(Currency_Pref.equals(getString(R.string.pref_currency_default))){
            return Price;
        }
        else{
            int double_price = (Price*80);
            return double_price;
        }
    }
    private void saveBookData(){

        String Title = mTitleEditText.getText().toString();
        String Author = mAuthorEditText.getText().toString();
        int Price = Integer.parseInt(mPriceEditText.getText().toString());

        int Quantity = Integer.parseInt(mQuantityEditText.getText().toString());

        if(!isDataValid(Title,Author,Price,Quantity)){

            Toast.makeText(this,"In Valid Data",Toast.LENGTH_SHORT).show();
            return;

        }

        if(ACTIVITY_TAG == NEW_ACTIVITY) {

            BookEntry bookEntry = new BookEntry(Title, Author, convertToRuppee(Price), Quantity,SupplierId);
            mEditViewModel.insertBook(bookEntry);

        }
        else{

            mBookEntry.setTitle(Title.trim());
            mBookEntry.setAuthor(Author.trim());
            mBookEntry.setPrice(convertToRuppee(Price));
            mBookEntry.setQuantity(Quantity);
            mEditViewModel.updateBook(mBookEntry);

        }
        onBackPressed();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Currency_Pref = sharedPreferences.getString(getString(R.string.pref_currency_key),getString(R.string.pref_currency_default));
    }
}