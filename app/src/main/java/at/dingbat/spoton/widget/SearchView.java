package at.dingbat.spoton.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import at.dingbat.spoton.R;
import at.dingbat.spoton.activity.MainActivity;
import at.dingbat.spoton.widget.recyclerview.dataholder.SearchViewDataHolder;

/**
 * Created by bendix on 05.06.15.
 */
public class SearchView extends RelativeLayout {

    private MainActivity context;

    private ImageView image;
    private EditText text;

    private SearchViewDataHolder holder;

    private boolean initialized = false;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public SearchView item;
        public ViewHolder(SearchView itemView) {
            super(itemView);
            this.item = itemView;
        }
    }

    public SearchView(Context context) {
        super(context);

        this.context = (MainActivity) context;

        inflate(context, R.layout.widget_search_view, this);

        text = (EditText) findViewById(R.id.widget_search_view_text);
        text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEARCH) {
                    SearchView.this.context.search(text.getText().toString());
                    if(holder != null) holder.setText(text.getText().toString());
                    return true;
                }
                return false;
            }
        });

        image = (ImageView) findViewById(R.id.widget_search_view_background);

        addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                Bitmap background = decodeSampledBitmapFromResource(getResources(), R.drawable.player, image.getWidth(), image.getHeight());
                image.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
                image.setImageBitmap(background);

                removeOnLayoutChangeListener(this);
            }
        });

        initialized = true;

        if(holder != null) setHolder(holder);

    }

    public void setHolder(SearchViewDataHolder holder) {
        this.holder = holder;
        if(text != null) {
            this.text.setText(holder.getText());
            this.text.setSelection(holder.getText().length());
        }
    }

    /*
     *  Source: http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

}
