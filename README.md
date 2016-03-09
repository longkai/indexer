Android Indexer
===

An indexer view like Wechat' s.

### Screenshot
![Screenshot](art/screenshot.gif)

### Usage
```xml
<com.xiaolongtongxue.indexer.IndexerView
      android:id="@+id/indexer"
      android:layout_alignParentRight="true"
      android:layout_centerInParent="true"
      android:paddingLeft="4dp"
      android:paddingRight="4dp"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content" />
```

```java
// ...
IndexerView indexerView = (IndexerView) findViewById(R.id.indexer);
// put list or String[]
List<String> list = new ArrayList<>();
  for (char i = 'A'; i <= 'Z'; i++) {
  list.add(Character.toString(i));
}
indexerView.setAlphabets(list);
// or just String
indexerView.setAlphabets("ABCDEFG");

// call the listener
indexerView.setOnIndexChangeListener(new IndexerView.OnIndexerChangeListener() {
  @Override public void onIndexChange(String index) {
    // do sth with the new index...
  }
});
```

### TODO
Makes it a gradle library

### License
MIT
