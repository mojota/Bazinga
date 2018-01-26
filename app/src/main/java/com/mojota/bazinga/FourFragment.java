package com.mojota.bazinga;


import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.mojota.bazinga.utils.ToastUtil;


/**
 * A simple {@link Fragment} subclass.
 */
public class FourFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button mBtPlay1;


    public static FourFragment newInstance(String param1, String param2) {
        FourFragment fragment = new FourFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FourFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_four, container, false);
        mBtPlay1 = (Button) view.findViewById(R.id.bt_play1);
        mBtPlay1.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(getActivity(), VideoActivity.class));
    }
}
