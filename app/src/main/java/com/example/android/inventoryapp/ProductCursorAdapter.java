package com.example.android.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.InventoryContract.ProductEntry;

/**
 * Created by bodiy_000 on 23-Jun-18.
 */

public class ProductCursorAdapter extends CursorAdapter {

    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        TextView name = view.findViewById(R.id.text_productName);
        TextView price = view.findViewById(R.id.text_productPrice);
        final TextView quantity = view.findViewById(R.id.text_productQuantity);
        Button btnSale = view.findViewById(R.id.btn_sale);

        final int idColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_ID);
        int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        final int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);

        final String productId = cursor.getString(idColumnIndex);
        String productName = cursor.getString(nameColumnIndex);
        int productPrice = cursor.getInt(priceColumnIndex);
        final int productQuantity = cursor.getInt(quantityColumnIndex);
        btnSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (productQuantity > 0) {
                    int quantityValue = productQuantity;
                    quantityValue--;
                    ContentValues values = new ContentValues();
                    values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantityValue);
                    Uri newUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, Integer.parseInt(productId));
                    MainActivity Activity = (MainActivity) context;
                    Activity.saleUpdate(newUri, values);
                } else
                    Toast.makeText(context, context.getResources().getString(R.string.sold_out)
                            , Toast.LENGTH_SHORT).show();
            }

        });


        name.setText(productName);
        price.setText("Price : " + productPrice);
        quantity.setText("Quantity : " + productQuantity);
    }
}
