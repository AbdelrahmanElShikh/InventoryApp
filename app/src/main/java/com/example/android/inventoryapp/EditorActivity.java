package com.example.android.inventoryapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.inventoryapp.data.InventoryContract.ProductEntry;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXISTING_PRODUCT_LOADER = 0;
    private EditText mNameProductEditText;
    private EditText mPriceEditText;
    private EditText mQuantityEditText;
    private EditText mNameSupplierEditText;
    private EditText mPhoneSupplierEditText;

    private Button btnIncrease;
    private Button btnDecrease;
    private Button btnDelete;
    private Button btnOrder;


    Uri currentProductUri;
    private boolean productHasChanged = false;
    private View.OnTouchListener mTouchListner = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            productHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Intent intent = getIntent();
        currentProductUri = intent.getData();
        btnDecrease = findViewById(R.id.qty_minus);
        btnIncrease = findViewById(R.id.qty_plus);
        btnDelete = findViewById(R.id.btn_delete);
        btnOrder = findViewById(R.id.btn_contact);

        if (currentProductUri == null) {
            setTitle(getString(R.string.add_product));
            invalidateOptionsMenu();
            btnDecrease.setVisibility(View.INVISIBLE);
            btnIncrease.setVisibility(View.INVISIBLE);
            btnDelete.setVisibility(View.INVISIBLE);
            btnOrder.setVisibility(View.INVISIBLE);
        } else {
            setTitle(getString(R.string.edit_product));
            getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
        }

        mNameProductEditText =  findViewById(R.id.edit_text_product_name);
        mPriceEditText =  findViewById(R.id.edit_text_product_price);
        mQuantityEditText =  findViewById(R.id.edit_text_product_quantity);
        mNameSupplierEditText =  findViewById(R.id.edit_text_supplier_name);
        mPhoneSupplierEditText =  findViewById(R.id.edit_text_supplier_phone);

        mNameSupplierEditText.setOnTouchListener(mTouchListner);
        mNameProductEditText.setOnTouchListener(mTouchListner);
        mPriceEditText.setOnTouchListener(mTouchListner);
        mQuantityEditText.setOnTouchListener(mTouchListner);
        mPhoneSupplierEditText.setOnTouchListener(mTouchListner);

        btnIncrease.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(mQuantityEditText.getText().toString());
                quantity++;
                mQuantityEditText.setText(quantity + "");
            }
        });

        btnDecrease.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(mQuantityEditText.getText().toString());
                if (quantity > 0) {
                    quantity--;
                    mQuantityEditText.setText(quantity + "");
                } else
                    Toast.makeText(getApplicationContext(), getString(R.string.cannot_exceed_zero), Toast.LENGTH_SHORT).show();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showDeleteConfirmationDialog();
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String phone = mPhoneSupplierEditText.getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });
    }


    private boolean saveProduct() {
        String productName = mNameProductEditText.getText().toString().trim();
        String productPrice = mPriceEditText.getText().toString().trim();
        String productQuantity = mQuantityEditText.getText().toString().trim();
        String supplierName = mNameSupplierEditText.getText().toString().trim();
        String supplierPhone = mPhoneSupplierEditText.getText().toString().trim();

        if (TextUtils.isEmpty(productName)) {
            Toast.makeText(this, getString(R.string.product_name_required), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(supplierName)) {
            Toast.makeText(this, getString(R.string.supplier_name_required), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(supplierPhone)) {
            Toast.makeText(this, getString(R.string.supplier_phone_required), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(productPrice)) {
            Toast.makeText(this, getString(R.string.product_price_required), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(productQuantity)) {
            Toast.makeText(this, getString(R.string.product_quantity_required), Toast.LENGTH_SHORT).show();
            return false;
        }
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, productName);
        values.put(ProductEntry.COLUMN_SUPPLIER_NAME, supplierName);
        values.put(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER, supplierPhone);
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, productPrice);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, productQuantity);

        if (currentProductUri == null) {
            Uri newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, values);
            if (newUri == null)
                Toast.makeText(this, getString(R.string.error_saving_the_product), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, getString(R.string.product_saved), Toast.LENGTH_SHORT).show();
        } else {
            int rowsUpdated = getContentResolver().update(currentProductUri, values, null, null);
            if (rowsUpdated == 0)
                Toast.makeText(this, getString(R.string.error_with_updating_product), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, getString(R.string.product_updated), Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if (saveProduct())
                    finish();
                return true;
            case android.R.id.home:
                if (!productHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_SUPPLIER_NAME,
                ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER};
        return new CursorLoader(this,
                currentProductUri,
                projection,
                null,
                null,
                null);
    }

    public void onBackPressed() {
        if (!productHasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.getCount() < 1) {
            mNameProductEditText.setText("Heeeeey");
            return;
        }
        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER);

            String name = cursor.getString(nameColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            final int quantity = cursor.getInt(quantityColumnIndex);
            String supplierName = cursor.getString(supplierNameColumnIndex);
            String supplierPhone = cursor.getString(supplierPhoneColumnIndex);

            mNameProductEditText.setText(name);
            mPriceEditText.setText(Integer.toString(price));
            mQuantityEditText.setText(Integer.toString(quantity));
            mNameSupplierEditText.setText(supplierName);
            mPhoneSupplierEditText.setText(supplierPhone);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameProductEditText.setText("");
        mPriceEditText.setText("");
        mQuantityEditText.setText("");
        mNameSupplierEditText.setText("");
        mPhoneSupplierEditText.setText("");
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtoncClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.unsaved_change_dialog_message));
        builder.setPositiveButton(getString(R.string.discard), discardButtoncClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteProduct();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteProduct() {
        if (currentProductUri != null) {

            int rowsDeleted = getContentResolver().delete(currentProductUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_product_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
}
