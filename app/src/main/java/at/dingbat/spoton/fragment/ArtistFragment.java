package at.dingbat.spoton.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import at.dingbat.spoton.R;
import at.dingbat.spoton.activity.MainActivity;
import at.dingbat.spoton.data.ParcelableArtist;

public class ArtistFragment extends Fragment {

    private MainActivity context;

    private ParcelableArtist artist;

    private RelativeLayout root;

    public ArtistFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = (RelativeLayout) inflater.inflate(R.layout.fragment_artist, null);

        context = (MainActivity) getActivity();

        artist = getArguments().getParcelable("artist");

        context.showToolbar();
        context.setToolbarText(artist.name);
        context.showToolbarBackArrow(true);

        return root;
    }

}
