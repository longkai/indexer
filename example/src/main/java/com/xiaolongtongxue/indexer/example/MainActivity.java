package com.xiaolongtongxue.indexer.example;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.xiaolongtongxue.indexer.IndexerView;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    IndexerView indexerView = (IndexerView) findViewById(R.id.indexer);
    final TextView textView = (TextView) findViewById(R.id.txt);

    // put list or String[]
    List<String> list = new ArrayList<>();
    for (char i = 'A'; i <= 'Z'; i++) {
      list.add(Character.toString(i));
    }
    indexerView.setAlphabets(list);
    // or just String
    // indexerView.setAlphabets("ABCDEFG");

    // call the listener
    indexerView.setOnIndexChangeListener(new IndexerView.OnIndexerChangeListener() {
      @Override public void onIndexChange(String index) {
        textView.setText(index);
      }
    });

    // you can also change the text size and color
    indexerView.setTextColor(Color.BLACK);
    indexerView.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
