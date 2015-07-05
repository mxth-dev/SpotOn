package at.dingbat.spoton.widget.recyclerview.dataholder;

import android.os.Parcel;
import android.os.Parcelable;

import at.dingbat.spoton.adapter.Adapter;
import at.dingbat.spoton.models.ParcelableTrack;
import at.dingbat.spoton.widget.recyclerview.DataHolder;

/**
 * Created by Max on 7/5/2015.
 */
public class TrackViewDataHolder implements Parcelable, DataHolder {

    public ParcelableTrack track;

    public TrackViewDataHolder(ParcelableTrack track) {
        this.track = track;
    }

    public TrackViewDataHolder(Parcel parcel) {
        this.track = parcel.readParcelable(ParcelableTrack.class.getClassLoader());
    }

    @Override
    public int getItemViewId() {
        return Adapter.TYPE_TRACK;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(track, flags);
    }

}
