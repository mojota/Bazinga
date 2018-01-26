package com.mojota.bazinga;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.mojota.bazinga.utils.ToastUtil;

/**
 * Created by mojota on 15-10-14.
 */
public class SpeechActivity extends ToolBarActivity implements View.OnClickListener {

    private static final String TAG = SpeechActivity.class.getSimpleName();
    private TextInputLayout mTiUsername;
    private TextView mTiResult;
    private SpeechSynthesizer mTts;
    private EditText mEdUsername;
    private Button mBtTrans;
    //合成监听器
    private SynthesizerListener mSynListener = new SynthesizerListener() {
        //会话结束回调接口，没有错误时，error为null
        public void onCompleted(SpeechError error) {
            ToastUtil.showToast("onCompleted");
        }

        //缓冲进度回调
        //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
        }

        //开始播放
        public void onSpeakBegin() {
            ToastUtil.showToast("onSpeakBegin");
        }

        //暂停播放
        public void onSpeakPaused() {
            ToastUtil.showToast("onSpeakPaused");
        }

        //播放进度回调
        //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            ToastUtil.showToast("onSpeakProgress:" + percent);
        }

        //恢复播放回调接口
        public void onSpeakResumed() {
        }

        //会话事件回调接口
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
            ToastUtil.showToast("onEvent");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);
        mTiUsername = (TextInputLayout) findViewById(R.id.ti_username);
        mTiResult = (TextView) findViewById(R.id.tv_result);
        mEdUsername = mTiUsername.getEditText();
        mBtTrans = (Button)findViewById(R.id.bt_trans);
        mBtTrans.setOnClickListener(this);

        //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        mTts = SpeechSynthesizer.createSynthesizer(this, null);
        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        //设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
        //保存在SD卡需要在AndroidManifest.xml添加写SD卡权限
        //如果不需要保存合成音频，注释该行代码
//        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/bazinga.pcm");




        //1.创建SpeechRecognizer对象，第二个参数：本地听写时传InitListener
        SpeechRecognizer mIat= SpeechRecognizer.createRecognizer(this, null);
        //2.设置听写参数，详见《科大讯飞MSC API手册(Android)》SpeechConstant类
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");
        //3.开始听写   mIat.startListening(mRecoListener);
        //听写监听器
//        private RecognizerListener mRecoListener = new RecognizerListener(){
//            //听写结果回调接口(返回Json格式结果，用户可参见附录12.1)；
//            //一般情况下会通过onResults接口多次返回结果，完整的识别内容是多次结果的累加；
//            //关于解析Json的代码可参见MscDemo中JsonParser类；
//            //isLast等于true时会话结束。
//            public void onResult(RecognizerResult results, boolean isLast) {
//                Log.d("Result:",results.getResultString ());}
//            //会话发生错误回调接口
//            public void onError(SpeechError error) {
//                error.getPlainDescription(true) //获取错误码描述}
//                //开始录音
//                public void onBeginOfSpeech() {}
//                //音量值0~30
//            public void onVolumeChanged(int volume){}
//            //结束录音
//            public void onEndOfSpeech() {}
//            //扩展用接口
//            public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {}
//        };
//
//
//
//
//        }


}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_trans:
                //3.开始合成
                String speechStr = mEdUsername.getText().toString();
                mTts.startSpeaking(speechStr, mSynListener);
        }
    }
}