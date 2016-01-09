package com.htaihm.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {
    private EditText etEditItem;
    private int itemPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        etEditItem = (EditText) findViewById(R.id.etEditItem);

        Intent i = getIntent();
        String itemText = i.getStringExtra(MainActivity.EDIT_EXTRA_TEXT);
        itemPos = i.getIntExtra(MainActivity.EDIT_EXTRA_POS, -1);
        etEditItem.setText(itemText);
        etEditItem.requestFocus();
        etEditItem.setSelection(itemText.length());
    }

    public void onSaveItem(View view) {
        String itemText = etEditItem.getText().toString();

        Intent data = new Intent();
        data.putExtra(MainActivity.EDIT_EXTRA_TEXT, itemText);
        data.putExtra(MainActivity.EDIT_EXTRA_POS, itemPos);

        setResult(RESULT_OK, data);
        finish();
    }
}
