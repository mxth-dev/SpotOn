package at.dingbat.spoton.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import at.dingbat.spoton.R;
import at.dingbat.spoton.activity.MainActivity;
import at.dingbat.spoton.adapter.Adapter;
import at.dingbat.spoton.data.ParcelableArtist;
import at.dingbat.spoton.widget.recyclerview.DataHolder;
import at.dingbat.spoton.widget.recyclerview.dataholder.ArtistHeaderViewDataHolder;

public class ArtistFragment extends Fragment {

    private MainActivity context;

    private ParcelableArtist artist;

    private RelativeLayout root;
    private RecyclerView recycler;
    private LinearLayoutManager recycler_layout;
    private Adapter recycler_adapter;

    public boolean isToolbarShown;

    public ArtistFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = (RelativeLayout) inflater.inflate(R.layout.fragment_artist, null);

        context = (MainActivity) getActivity();

        recycler = (RecyclerView) root.findViewById(R.id.fragment_artist_recycler);
        recycler_layout = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(recycler_layout);

        artist = getArguments().getParcelable("artist");

        recycler_adapter = new Adapter(new ArrayList<DataHolder>() {{
            add(new ArtistHeaderViewDataHolder(artist));
        }});

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

        return root;
    }

}
