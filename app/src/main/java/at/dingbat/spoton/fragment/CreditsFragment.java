package at.dingbat.spoton.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import at.dingbat.spoton.R;
import at.dingbat.spoton.activity.MainActivity;

public class CreditsFragment extends Fragment {

    private MainActivity context;

    private RelativeLayout root;
    private TextView credits;

    public CreditsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = (MainActivity) getActivity();

        root = (RelativeLayout) inflater.inflate(R.layout.fragment_credits, container, false);

        credits = (TextView) root.findViewById(R.id.fragment_credits_credits);
        credits.setText(Html.fromHtml(getResources().getString(R.string.credits)));
        credits.setMovementMethod(new LinkMovementMethod());

        context.setToolbarText(getResources().getString(R.string.label_credits));
        context.showToolbar();
        context.showToolbarBackArrow(true);

        return root;
    }

}
