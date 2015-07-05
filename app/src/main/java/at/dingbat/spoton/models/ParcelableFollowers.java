package at.dingbat.spoton.models;

import android.os.Parcel;
import android.os.Parcelable;

import kaaes.spotify.webapi.android.models.Followers;

/**
 * Created by bendix on 06.06.15.
 */
public class ParcelableFollowers extends Followers implements Parcelable {

    public ParcelableFollowers(Followers followers) {
        this.href = followers.href;
        this.total = followers.total;
    }

    public ParcelableFollowers(Parcel parcel) {
        href = parcel.readString();
        total = parcel.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(href);
        parcel.writeInt(total);
    }
}
