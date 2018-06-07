package com.example.unity;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class AudioCenter {
    private Context activity_context;
    private SoundPool soundpool;
    private AssetManager assetManager;
    private int id;
    private Set<Integer> soundsSet = new HashSet<Integer>();

    public static Object createInstance(Context context, int maxStream){
        return new AudioCenter(context, maxStream);
    }

    @SuppressWarnings("deprecation")
    private void instantiatesoundpool(int maxStream){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundpool = new SoundPool.Builder()
                    .setMaxStreams(maxStream)
                    .setAudioAttributes(audioAttributes)
                    .build();
        }
        else{
            soundpool = new SoundPool(maxStream, AudioManager.STREAM_MUSIC, 0);
        }
    }

    private AudioCenter(Context context, int maxStream){
        if (context == null){
            throw new NullPointerException();
        }

        activity_context = context;
        assetManager = context.getAssets();
        instantiatesoundpool(maxStream);
        soundpool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                soundPool.play(id, 1.0f, 1.0f, 1, 0, 1.0f);
            }
        });
    }

    public int loadSound(String filename){
        AssetFileDescriptor assetFileDescriptor;
        try {
            assetFileDescriptor = assetManager.openFd(filename);
        } catch (IOException e) {
            return -1;
        }
        id = soundpool.load(assetFileDescriptor, 1);
        soundsSet.add(id);
        return id;
    }

    public void play( int soundID )
    {
        if( soundsSet.contains( soundID ) ) {
            soundpool.play( soundID, 1, 1, 1, 0, 1f );
        }
    }

    public void play(int fileID, float leftVolume, float rightVolume, int priority, int loop , float rate)
    {
        if (soundsSet.contains(fileID)){
            soundpool.play(fileID, leftVolume, rightVolume, priority, loop, rate);
        }
    }

    public  void stop(int fileId){
        soundpool.stop(fileId);
    }

    public void release(){
        soundpool.release();;
        soundpool = null;
        assetManager = null;
        activity_context = null;
    }
}