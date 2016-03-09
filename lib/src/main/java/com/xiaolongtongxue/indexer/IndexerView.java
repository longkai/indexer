package com.xiaolongtongxue.indexer;

import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * An Android indexer view like Wechat' s.
 */
public class IndexerView extends View {
  private Rect     rect;
  private Paint    paint;
  private int      textColor;
  private float    textSize;
  private String[] alphabets;

  private String                  lastKey;
  private OnIndexerChangeListener onChange;

  public IndexerView(Context context) {
    this(context, null);
  }

  public IndexerView(Context context, AttributeSet attrs) {
    super(context, attrs);
    bootstrap(context, attrs, 0, 0);
  }

  public IndexerView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    bootstrap(context, attrs, defStyleAttr, 0);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public IndexerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    bootstrap(context, attrs, defStyleAttr, defStyleRes);
  }

  private void bootstrap(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    rect = new Rect();
    paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    // default value for text color and size
    textColor = Color.BLACK;
    textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics());

    setOnTouchListener(new OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
          case MotionEvent.ACTION_DOWN:
            return true;
          case MotionEvent.ACTION_UP:
          case MotionEvent.ACTION_MOVE:
          case MotionEvent.ACTION_CANCEL:
            int y = (int) event.getY();
            if (y < getPaddingTop() || y > getHeight() - getPaddingBottom()) {
              return false;
            }
            y -= getPaddingTop();
            float unit = paint.descent() - paint.ascent();
            int index = (int) (y / unit);
            String key = alphabets[index];
            if (!TextUtils.equals(key, lastKey)) {
              if (onChange != null) {
                onChange.onIndexChange(index, key);
              }
              lastKey = key;
            }
            // reset if up or cancel
            if (event.getAction() != MotionEvent.ACTION_MOVE) {
              lastKey = null;
            }
            return true;
        }
        return false;
      }
    });
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    if (emptyIndexer()) {
      setMeasuredDimension(0, 0);
      return;
    }

    int width = 0;
    paint.setTextSize(textSize);
    for (int i = 0; i < alphabets.length; i++) {
      paint.getTextBounds(alphabets[i], 0, alphabets[i].length(), rect);
      width = Math.max(width, rect.width());
    }
    width += getPaddingLeft() + getPaddingRight();

    int height = (int) ((paint.descent() - paint.ascent()) * alphabets.length + getPaddingTop() + getPaddingBottom());

    setMeasuredDimension(resolveSize(width, widthMeasureSpec), height);
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (emptyIndexer()) {
      return;
    }

    canvas.save();
    paint.setTextSize(textSize);
    paint.setColor(textColor);
    int yOffset = getPaddingTop();
    Paint.FontMetrics fontMetrics = paint.getFontMetrics();
    for (int i = 0; i < alphabets.length; i++) {
      String s = alphabets[i];
      paint.getTextBounds(s, 0, s.length(), rect);

      // since the uppercase has no bottom, to makes it looks more nice, put the baseline up to the bottom:)
      canvas.drawText(s, 0, s.length(), (getWidth() - rect.width()) / 2, yOffset - rect.top + fontMetrics.bottom, paint);
      yOffset += (paint.descent() - paint.ascent());
    }
    canvas.restore();
  }

  private boolean emptyIndexer() {
    return alphabets == null || alphabets.length == 0;
  }

  public void setOnIndexChangeListener(OnIndexerChangeListener onChange) {
    this.onChange = onChange;
  }

  public int getTextColor() {
    return textColor;
  }

  public void setTextColor(@ColorInt int textColor) {
    if (this.textColor != textColor) {
      this.textColor = textColor;
      invalidate();
    }
  }

  public float getTextSize() {
    return textSize;
  }

  public void setTextSize(float textSize) {
    if (this.textSize != textSize) {
      this.textSize = textSize;
      requestLayout();
    }
  }

  public void setAlphabets(@NonNull String[] alphabets) {
    this.alphabets = alphabets;
    requestLayout();
  }

  public void setAlphabets(@NonNull List<String> alphabets) {
    this.alphabets = alphabets.toArray(new String[0]);
    requestLayout();
  }

  public void setAlphabets(@NonNull String alphabets) {
    char[] chars = alphabets.toUpperCase().toCharArray();
    this.alphabets = new String[alphabets.length()];
    for (char i = 0; i < chars.length; i++) {
      this.alphabets[i] = Character.toString(chars[i]);
    }
    requestLayout();
  }

  public interface OnIndexerChangeListener {
    void onIndexChange(int index, String key);
  }
}
