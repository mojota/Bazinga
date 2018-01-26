package com.mojota.bazinga;

import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 录音用
 * Created by wangjing on 17-11-15.
 */

public class RecorderManager {

    private static final String TAG = "RecorderManager";
    private static final int VOLUME_UPDATA = 0;
    private static final int UPDATA_INTERVAL = 200;
    private static RecorderManager mRecorderManager;
    private MediaRecorder mMediaRecorder;
    private String FILE_PATH = "bbRecorder";
    private String mAudioFileName;
    private RecorderListerner mRecorderListerner;

    public interface RecorderListerner {
        void onVolumeUpdate(int volume);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case VOLUME_UPDATA:
                    if (mMediaRecorder != null && mRecorderListerner != null) {
                        mRecorderListerner.onVolumeUpdate(mMediaRecorder.getMaxAmplitude());
                    }
                    Log.d(TAG, String.valueOf(mMediaRecorder.getMaxAmplitude()));
                    sendEmptyMessageDelayed(VOLUME_UPDATA, UPDATA_INTERVAL);
                    break;
                default:
                    break;
            }
        }
    };

    public synchronized static RecorderManager getInstance() {
        if (mRecorderManager == null) {
            mRecorderManager = new RecorderManager();
        }
        return mRecorderManager;
    }


    public void setRecorderListerner(RecorderListerner recorderListerner) {
        this.mRecorderListerner = recorderListerner;
    }

    private void updateVolume() {
        mHandler.sendEmptyMessageDelayed(VOLUME_UPDATA, UPDATA_INTERVAL);
    }

    /**
     * 开始录音
     */
    public void startRecord() {
        mMediaRecorder = new MediaRecorder();
        try {
            //从麦克风采集声音数据
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //设置保存文件格式为MP4
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
//            //设置采样频率,44100是所有安卓设备都支持的频率,频率越高，音质越好，当然文件越大
//            mMediaRecorder.setAudioSamplingRate(44100);
            //设置声音数据编码格式,音频通用格式是AAC
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//            //设置编码频率
//            mMediaRecorder.setAudioEncodingBitRate(96000);
            //设置录音保存的文件
            File audioFile = new File(getFileFolder(), getFileName());
            mAudioFileName = audioFile.getAbsolutePath();
            mMediaRecorder.setOutputFile(mAudioFileName);
            //开始录音
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            updateVolume();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private File getFileFolder() {
        File fileFolder = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sdcard = Environment.getExternalStorageDirectory();
            fileFolder = new File(sdcard, FILE_PATH);
            if (!fileFolder.exists()) {
                fileFolder.mkdirs();
            }
        }
        return fileFolder;
    }

    private String getFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = sdf.format(new Date()) + ".amr";
        return fileName;
    }

    public void stopRecord() {
        if (mMediaRecorder != null) {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
        mHandler.removeMessages(VOLUME_UPDATA);

    }

}
