package com.mojota.bazinga;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.MediaController;
import android.widget.ToggleButton;
import android.widget.VideoView;

import com.mojota.bazinga.utils.ToastUtil;

/**
 * Created by jamie on 17-6-8.
 */

public class VideoActivity extends ToolBarActivity implements CompoundButton.OnCheckedChangeListener{
    private VideoView mViewVideo;
    private ToggleButton mTbPlay1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        mTbPlay1 = (ToggleButton) findViewById(R.id.tb_play1);
        mTbPlay1.setOnCheckedChangeListener(this);
        mViewVideo = (VideoView) findViewById(R.id.viewVideo);
        mViewVideo.setMediaController(new MediaController(this));
        mViewVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ToastUtil.showToast("播完了");
            }
        });
        Uri uri = Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        uri = Uri.parse("https://uim.bangcommunity.com/video_explore/111.mp4");
        mViewVideo.setVideoURI(uri);
        mTbPlay1.setChecked(true);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (mTbPlay1.isChecked()){
            mViewVideo.start();
        } else {
            mViewVideo.pause();
        }
    }
}
