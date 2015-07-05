package at.dingbat.spoton.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import at.dingbat.spoton.R;

/**
 * Created by Max on 7/3/2015.
 */
public class SubtitleView extends RelativeLayout {

    private Context context;

    private TextView title_view;

    private String title;

    private boolean initialized = false;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public SubtitleView item;
        public ViewHolder(SubtitleView itemView) {
            super(itemView);
            this.item = itemView;
        }
    }

    public SubtitleView(Context context) {
        super(context);

        inflate(context, R.layout.widget_subtitle, this);

        title_view = (TextView) findViewById(R.id.widget_subtitle_title);

        initialized = true;
        if(title != null) setTitle(title);
    }

    public void setTitle(String title) {
        this.title = title;
        if(initialized) {
            title_view.setText(title);
        }
    }

}
