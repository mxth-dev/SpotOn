package at.dingbat.spoton.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;

import at.dingbat.spoton.R;
import at.dingbat.spoton.activity.MainActivity;
import at.dingbat.spoton.adapter.Adapter;
import at.dingbat.spoton.models.ParcelableArtist;
import at.dingbat.spoton.models.ParcelableTrack;
import at.dingbat.spoton.widget.recyclerview.DataHolder;
import at.dingbat.spoton.widget.recyclerview.dataholder.ArtistHeaderViewDataHolder;
import at.dingbat.spoton.widget.recyclerview.dataholder.SubtitleViewDataHolder;
import at.dingbat.spoton.widget.recyclerview.dataholder.TrackViewDataHolder;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ArtistFragment extends Fragment {

    public static final String TAG_ADAPTER = "adapter";
    public static final String TAG_ARTIST = "artist";

    private MainActivity context;

    private ParcelableArtist artist;
    private ArrayList<DataHolder> tracks;

    private RelativeLayout root;
    private RecyclerView recycler;
    private LinearLayoutManager recycler_layout;
    private Adapter recycler_adapter;

    public boolean isToolbarShown;

    public ArtistFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        if(root == null) {
            root = (RelativeLayout) inflater.inflate(R.layout.fragment_artist, null);

            context = (MainActivity) getActivity();

            recycler = (RecyclerView) root.findViewById(R.id.fragment_artist_recycler);
            recycler_layout = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            recycler.setLayoutManager(recycler_layout);

            artist = getArguments().getParcelable(TAG_ARTIST);

            if(savedInstanceState != null) {
                recycler_adapter = savedInstanceState.getParcelable(TAG_ADAPTER);
            } else {
                recycler_adapter = new Adapter(new ArrayList<DataHolder>() {{
                    add(new ArtistHeaderViewDataHolder(artist));
                    add(new SubtitleViewDataHolder(getResources().getString(R.string.label_top_tracks)));
                }});
            }

            recycler.setAdapter(recycler_adapter);

            context.setToolbarText("");
            context.showToolbarBackArrow(true);
            context.hideToolbar();

            isToolbarShown = false;

            recycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    int position = recycler_layout.findFirstVisibleItemPosition();
                    if (position == 0) {
                        View v = recycler_layout.findViewByPosition(position);
                        if (v.getTop() >= 0) {
                            isToolbarShown = false;
                            context.hideToolbar();
                        } else {
                            isToolbarShown = true;
                            context.showToolbar();
                        }
                    }
                }
            });

            tracks = new ArrayList<>();

            if(recycler_adapter.getItemCount() < 3) {
                HashMap<String, Object> params = new HashMap<>();
                params.put("country", "US");

                context.getSpotify().getArtistTopTrack(artist.id, params, new Callback<Tracks>() {
                    @Override
                    public void success(Tracks ts, Response response) {
                        ArrayList<ParcelableTrack> playlist = new ArrayList<ParcelableTrack>();
                        for(Track t: ts.tracks) {
                           playlist.add(new ParcelableTrack(t));
                        }
                        for(ParcelableTrack pt : playlist) {
                            tracks.add(new TrackViewDataHolder(pt, playlist));
                        }
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recycler_adapter.replaceAll(2, tracks);
                                recycler_adapter.notifyDataSetChanged();
                            }
                        });
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
        }

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(TAG_ADAPTER, recycler_adapter);
    }
}
