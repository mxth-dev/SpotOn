package at.dingbat.spoton.adapter;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;

import at.dingbat.spoton.activity.MainActivity;
import at.dingbat.spoton.widget.ArtistHeaderView;
import at.dingbat.spoton.widget.ArtistListItem;
import at.dingbat.spoton.widget.SearchView;
import at.dingbat.spoton.widget.SubtitleView;
import at.dingbat.spoton.widget.TrackView;
import at.dingbat.spoton.widget.recyclerview.DataHolder;
import at.dingbat.spoton.widget.recyclerview.dataholder.ArtistHeaderViewDataHolder;
import at.dingbat.spoton.widget.recyclerview.dataholder.ArtistListItemDataHolder;
import at.dingbat.spoton.widget.recyclerview.dataholder.SearchViewDataHolder;
import at.dingbat.spoton.widget.recyclerview.dataholder.SubtitleViewDataHolder;
import at.dingbat.spoton.widget.recyclerview.dataholder.TrackViewDataHolder;

/**
 * Created by bendix on 05.06.15.
 */
public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Parcelable {

    public static final int TYPE_ARTIST = 0;
    public static final int TYPE_SEARCH_HEADER = 1;
    public static final int TYPE_ARTIST_HEADER = 2;
    public static final int TYPE_TRACK = 3;
    public static final int TYPE_SUBTITLE = 4;

    private ArrayList<DataHolder> items;

    public Adapter(ArrayList<DataHolder> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public Adapter(Parcel parcel) {
        Parcelable[] arr = parcel.readParcelableArray(ClassLoader.getSystemClassLoader());
        items = new ArrayList<DataHolder>();
        for(Parcelable p: arr) {
            items.add((DataHolder) p);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getItemViewId();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ARTIST:
                return new ArtistListItem.ViewHolder(new ArtistListItem(parent.getContext()));
            case TYPE_SEARCH_HEADER:
                return new SearchView.ViewHolder(new SearchView(parent.getContext()));
            case TYPE_ARTIST_HEADER:
                return new ArtistHeaderView.ViewHolder(new ArtistHeaderView(parent.getContext()));
            case TYPE_TRACK:
                return new TrackView.ViewHolder(new TrackView(parent.getContext()));
            case TYPE_SUBTITLE:
                return new SubtitleView.ViewHolder(new SubtitleView(parent.getContext()));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_ARTIST:
                ((ArtistListItem.ViewHolder)holder).item.setArtist(((ArtistListItemDataHolder)items.get(position)).artist);
                break;
            case TYPE_SEARCH_HEADER:
                ((SearchView.ViewHolder)holder).item.setHolder((SearchViewDataHolder)items.get(position));
                break;
            case TYPE_ARTIST_HEADER:
                ((ArtistHeaderView.ViewHolder)holder).item.setArtist(((ArtistHeaderViewDataHolder)items.get(position)).artist);
                break;
            case TYPE_TRACK:
                ((TrackView.ViewHolder)holder).item.setTrack(((TrackViewDataHolder)items.get(position)).track);
                break;
            case TYPE_SUBTITLE:
                ((SubtitleView.ViewHolder)holder).item.setTitle(((SubtitleViewDataHolder)items.get(position)).getTitle());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void replaceAll(int offset, ArrayList<DataHolder> items) {
        this.items = new ArrayList<DataHolder>(this.items.subList(0, offset));
        this.items.addAll(items);
        notifyItemRangeChanged(offset, this.items.size());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        Parcelable[] arr = new Parcelable[items.size()];
        for(int i = 0; i < items.size(); i++) {
            switch (getItemViewType(i)) {
                case TYPE_ARTIST:
                    arr[i] = (ArtistListItemDataHolder) items.get(i);
                    break;
                case TYPE_SEARCH_HEADER:
                    arr[i] = (SearchViewDataHolder) items.get(i);
                    break;
                case TYPE_ARTIST_HEADER:
                    arr[i] = (ArtistHeaderViewDataHolder) items.get(i);
                    break;
                case TYPE_TRACK:
                    arr[i] = (TrackViewDataHolder) items.get(i);
                    break;
                case TYPE_SUBTITLE:
                    arr[i] = (SubtitleViewDataHolder) items.get(i);
                    break;
                default:
                    arr[i] = null;
                    break;
            }
        }
        parcel.writeParcelableArray(arr, flags);
    }

}
