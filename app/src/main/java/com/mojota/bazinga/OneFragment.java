package com.mojota.bazinga;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class OneFragment extends Fragment implements View.OnClickListener {

    private Button btVolley;
    private Button btTextInput;

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
        btTextInput = (Button) view.findViewById(R.id.bt_textinput);
        btVolley.setOnClickListener(this);
        btTextInput.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_volley:
                Intent volleyIntent = new Intent(getActivity(), VolleyActivity.class);
                startActivity(volleyIntent);
                break;
            case R.id.bt_textinput:
                Intent textInputIntent = new Intent(getActivity(), TextInputActivity.class);
                startActivity(textInputIntent);
                break;
        }

    }
}