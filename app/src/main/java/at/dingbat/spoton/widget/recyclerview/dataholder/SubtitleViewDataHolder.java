package at.dingbat.spoton.widget.recyclerview.dataholder;

import android.os.Parcel;
import android.os.Parcelable;

import at.dingbat.spoton.adapter.Adapter;
import at.dingbat.spoton.widget.recyclerview.DataHolder;

/**
 * Created by Max on 7/3/2015.
 */
public class SubtitleViewDataHolder implements DataHolder, Parcelable {

    private String title;

    public SubtitleViewDataHolder(String title) {
        this.title = title;
    }

    public SubtitleViewDataHolder(Parcel parcel) {
        this.title = parcel.readString();
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int getItemViewId() {
        return Adapter.TYPE_SUBTITLE;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
    }

}
