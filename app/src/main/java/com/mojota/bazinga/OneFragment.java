package com.mojota.bazinga;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mojota.bazinga.view.HttpActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class OneFragment extends Fragment implements View.OnClickListener {

    private Button btVolley;
    private Button btTextInput;
    private Button btVolleyUp;
    private Button btCustomView;
    private Button btAnim;
    private Button btViewPager;
    private Button btSpeech;
    private Button btHttp;

    public static OneFragment newInstance(String param1, String param2) {
        OneFragment fragment = new OneFragment();
        Bundle args = new Bundle();
        args.putString("", param1);
        args.putString("", param2);
        fragment.setArguments(args);
        return fragment;
    }

    public OneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        btVolley = (Button) view.findViewById(R.id.bt_volley);
        btVolleyUp = (Button) view.findViewById(R.id.bt_volley_upload);
        btHttp = (Button) view.findViewById(R.id.bt_http);
        btTextInput = (Button) view.findViewById(R.id.bt_textinput);
        btCustomView = (Button) view.findViewById(R.id.bt_custom_view);
        btAnim = (Button) view.findViewById(R.id.bt_anim);
        btViewPager = (Button) view.findViewById(R.id.bt_viewpager);
        btSpeech = (Button) view.findViewById(R.id.bt_speech);
        btVolley.setOnClickListener(this);
        btVolleyUp.setOnClickListener(this);
        btHttp.setOnClickListener(this);
        btTextInput.setOnClickListener(this);
        btCustomView.setOnClickListener(this);
        btAnim.setOnClickListener(this);
        btViewPager.setOnClickListener(this);
        btSpeech.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_volley:
                Intent volleyIntent = new Intent(getActivity(), VolleyActivity.class);
                startActivity(volleyIntent);
                break;
            case R.id.bt_volley_upload:
                Intent volleyUpIntent = new Intent(getActivity(), VolleyUploadActivity.class);
                startActivity(volleyUpIntent);
                break;
            case R.id.bt_http:
                Intent httpIntent = new Intent(getActivity(), HttpActivity.class);
                startActivity(httpIntent);
                break;
            case R.id.bt_textinput:
                Intent textInputIntent = new Intent(getActivity(), TextInputActivity.class);
                startActivity(textInputIntent);
                break;
            case R.id.bt_custom_view:
                Intent customViewIntent = new Intent(getActivity(), CustomViewActivity.class);
                startActivity(customViewIntent);
                break;
            case R.id.bt_anim:
                Intent animIntent = new Intent(getActivity(), AnimateActivity.class);
                startActivity(animIntent);
                break;
            case R.id.bt_viewpager:
                Intent viewpagerIntent = new Intent(getActivity(), ViewPagerActivity.class);
                startActivity(viewpagerIntent);
            case R.id.bt_speech:
                Intent speechIntent = new Intent(getActivity(), SpeechActivity.class);
                startActivity(speechIntent);
        }

    }
}
