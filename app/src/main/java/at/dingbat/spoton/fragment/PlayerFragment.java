package at.dingbat.spoton.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import at.dingbat.spoton.R;
import at.dingbat.spoton.activity.MainActivity;
import at.dingbat.spoton.models.ParcelableTrack;
import at.dingbat.spoton.service.PlayerService;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment {

    private MainActivity context;
    private PlayerService service;

    private RelativeLayout root;
    private ImageView background;

    private ParcelableTrack track;

    public PlayerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = (MainActivity) getActivity();

        root = (RelativeLayout) inflater.inflate(R.layout.fragment_player, container, false);

        background = (ImageView) root.findViewById(R.id.fragment_player_background);

        track = getArguments().getParcelable("track");

        if(track.album.images.size() > 0) Picasso.with(getActivity()).load(track.album.images.get(0).url).into(background);
        else background.setImageResource(R.mipmap.ic_placeholder);

        service = context.getService();

        ArrayList<ParcelableTrack> playlist = new ArrayList<ParcelableTrack>();
        playlist.add(track);

        if(service != null) {
            service.setPlaylist(playlist);
            service.playTrack(0);
        }

        return root;
    }

}
