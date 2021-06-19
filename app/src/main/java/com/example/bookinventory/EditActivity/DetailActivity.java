package com.example.bookinventory.EditActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookinventory.DataBase.BookEntry;
import com.example.bookinventory.DataBase.EditViewModel;
import com.example.bookinventory.R;

public class DetailActivity extends AppCompatActivity {

    private EditViewModel mEditViewModel;
    private BookEntry mBookEntry;
    private TextView mTitleView,mAuthorView,mPriceTextView,mQuantityTextView,mSupplierView,mSupplierMobView;
    private SharedPreferences sharedPreferences;
    private String Currency_Pref;
    private String[] mSupplierArray;
    private String[] mSupplierMobileArray;
    private int BookId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        init();
        setupViewModel();
        Intent intent = getIntent();
        if(intent.hasExtra("EDIT")){
            BookId = intent.getIntExtra("EDIT",1);
            Toast.makeText(this," SOMETHING ERROR "+BookId,Toast.LENGTH_SHORT).show();
            mBookEntry = mEditViewModel.getBookById(BookId);
            setContent();
        }
        else{
            Toast.makeText(this," SOMETHING ERROR ",Toast.LENGTH_SHORT).show();
        }
    }

    private void setupViewModel() { mEditViewModel = ViewModelProviders.of(this).get(EditViewModel.class);}

    private void setContent(){

        mTitleView.setText(mBookEntry.getTitle());
        mAuthorView.setText(mBookEntry.getAuthor());
        mPriceTextView.setText(String.valueOf(getPrefPrice(mBookEntry.getPrice())));
        mQuantityTextView.setText(String.valueOf(mBookEntry.getQuantity()));
        int SupplierId = mBookEntry.getSupplierId();

        mSupplierView.setText(mSupplierArray[SupplierId]);
        mSupplierMobView.setText(mSupplierMobileArray[SupplierId]);

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
    private void init(){
        mTitleView = findViewById(R.id.textViewTitle);
        mAuthorView = findViewById(R.id.textViewAuthor);
        mPriceTextView = findViewById(R.id.textViewPrice);
        mQuantityTextView = findViewById(R.id.textViewQuantity);
        mSupplierView = findViewById(R.id.textViewSupplier);
        mSupplierMobView = findViewById(R.id.supplierMobileView);
        sharedPreferences  = PreferenceManager.getDefaultSharedPreferences(this);
        Currency_Pref = sharedPreferences.getString(getString(R.string.pref_currency_key),getString(R.string.pref_currency_default));
        mSupplierArray = getResources().getStringArray(R.array.array_supplier_options);
        mSupplierMobileArray = getResources().getStringArray(R.array.array_supplier_phone);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.action_delete:
                                        deleteBook();
                                        break;
            case R.id.action_edit:
                                        toEditorActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void toEditorActivity() {
            Intent intent = new Intent(this, EditorActivity.class);
            intent.putExtra("EDIT",BookId);
            startActivity(intent);
    }

    private void deleteBook() {
        mEditViewModel.deleteBook(mBookEntry);
        onBackPressed();
    }
}