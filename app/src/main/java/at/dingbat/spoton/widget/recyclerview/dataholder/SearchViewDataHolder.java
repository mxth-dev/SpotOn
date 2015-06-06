package at.dingbat.spoton.widget.recyclerview.dataholder;

import android.os.Parcel;
import android.os.Parcelable;

import at.dingbat.spoton.adapter.SearchAdapter;
import at.dingbat.spoton.widget.recyclerview.DataHolder;

/**
 * Created by bendix on 05.06.15.
 */
public class SearchViewDataHolder implements DataHolder, Parcelable {

    private String text;

    public SearchViewDataHolder(String text) {
        this.text = text;
    }

    public SearchViewDataHolder() {
        text = "";
    }

    public SearchViewDataHolder(Parcel parcel) {
        this.text = parcel.readString();
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public int getItemViewId() {
        return SearchAdapter.TYPE_SEARCH_HEADER;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(text);
    }

}
