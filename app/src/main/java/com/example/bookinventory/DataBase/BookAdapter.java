package com.example.bookinventory.DataBase;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.media.session.IMediaControllerCallback;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookinventory.R;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder>{

    List<BookEntry> mBookList;
    private Context mContext;
    private RecyclerViewListener mViewListener;
    private SharedPreferences sharedPreferences;
    String Currency_Pref;

    public interface RecyclerViewListener{
        void onClickMethod(int Id);
        void onSaleClickMethod(int Id);
    }

    public BookAdapter(Context mContext,RecyclerViewListener listener) {
        this.mContext = mContext;
        mViewListener = listener;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        Currency_Pref = sharedPreferences.getString(mContext.getString(R.string.pref_currency_key),mContext.getString(R.string.pref_currency_default));

    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        holder.mTitleTextView.setText(mBookList.get(position).Title);
        holder.mAuthorTextView.setText(mBookList.get(position).Author);
        int Price = mBookList.get(position).Price;

        holder.mPriceTextView.setText(getPrefPrice(Price));
        int Quantity = mBookList.get(position).Quantity;
        holder.mQuantityTextView.setText(String.valueOf(Quantity));
        if(Quantity==0){
            holder.mSaleButton.setBackgroundResource(R.drawable.sale_button_disabled);
        }
        else{
            holder.mSaleButton.setBackgroundResource(R.drawable.sale_button_selected);
        }
        holder.Id = mBookList.get(position).Id;
    }

    public String getPrefPrice(int Price){
        if(Currency_Pref.equals(mContext.getString(R.string.pref_currency_default))){
            return String.valueOf(Price)+" \u20B9";
        }
        else{
            double double_price = (Price/80.0);
            String price_in_format = String.format("%.2f",double_price)+" $";
            return price_in_format;
        }
    }
    @Override
    public int getItemCount() {
        if(mBookList==null){

            return 0;
        }
        return mBookList.size();
    }

    public List<BookEntry> getBooksList(){
        return mBookList;
    }
    public void updateAdapter(List<BookEntry> list){
        mBookList = list;
        notifyDataSetChanged();
    }

    public void updateCurrency(){
        Currency_Pref =  sharedPreferences.getString(mContext.getString(R.string.pref_currency_key),mContext.getString(R.string.pref_currency_0_label_value));
        Toast.makeText(mContext,""+Currency_Pref,Toast.LENGTH_SHORT).show();
        notifyDataSetChanged();
    }
    public class BookViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {

        TextView mTitleTextView;
        TextView mAuthorTextView;
        TextView mQuantityTextView;
        TextView mPriceTextView;
        int Id;
        Button mSaleButton;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitleTextView = itemView.findViewById(R.id.title);
            mAuthorTextView = itemView.findViewById(R.id.author);
            mPriceTextView = itemView.findViewById(R.id.price);
            mQuantityTextView = itemView.findViewById(R.id.quantity);
            mSaleButton = itemView.findViewById(R.id.sale_button);

            mSaleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int Id = mBookList.get(getAdapterPosition()).Id;
                    mViewListener.onSaleClickMethod(Id);
                }
            });

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int Id = mBookList.get(getAdapterPosition()).Id;
            mViewListener.onClickMethod(Id);
        }
    }
}
