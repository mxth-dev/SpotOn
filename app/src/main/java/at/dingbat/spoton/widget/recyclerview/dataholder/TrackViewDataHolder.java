package at.dingbat.spoton.widget.recyclerview.dataholder;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import at.dingbat.spoton.adapter.Adapter;
import at.dingbat.spoton.models.ParcelableTrack;
import at.dingbat.spoton.widget.recyclerview.DataHolder;

/**
 * Created by Max on 7/5/2015.
 */
public class TrackViewDataHolder implements Parcelable, DataHolder {

    public ParcelableTrack track;
    public ArrayList<ParcelableTrack> playlist;

    public TrackViewDataHolder(ParcelableTrack track, ArrayList<ParcelableTrack> playlist) {
        this.track = track;
        this.playlist = playlist;
    }

    public TrackViewDataHolder(Parcel parcel) {
        this.track = parcel.readParcelable(ParcelableTrack.class.getClassLoader());
        this.playlist = new ArrayList<ParcelableTrack>();
        parcel.readTypedList(this.playlist, ParcelableTrack.CREATOR);
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
        dest.writeTypedList(playlist);
    }

}
