package com.mojota.bazinga;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.transition.ChangeBounds;
import android.transition.ChangeClipBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by jamie on 16-7-25.
 */
public class PicActivity extends ToolBarActivity {
    private ImageView ivPic;
    private ViewGroup mLayoutPic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setEnterTransition(new Fade());
//        getWindow().setSharedElementEnterTransition(new ChangeBounds());
        getWindow().setSharedElementsUseOverlay(false);
        setContentView(R.layout.activity_pic);
        ivPic = (ImageView) findViewById(R.id.iv_pic);
        mLayoutPic = (ViewGroup) findViewById(R.id.layout_pic);
        Intent intent = getIntent();
        int resId = intent.getIntExtra("resId", 0);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
        Palette.Builder pb = Palette.from(bitmap);
        pb.generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                mLayoutPic.setBackgroundColor(palette.getDarkMutedColor(Color.BLACK));
            }
        });
        ivPic.setImageBitmap(bitmap);
    }
}
