package com.robert.autoplayvideo;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Created by robert on 17/08/03.
 */
public class CustomVideoView extends TextureView implements TextureView.SurfaceTextureListener {
    private MediaPlayer mMediaPlayer;
    private Uri mSource;
    //    private MediaPlayer.OnCompletionListener mCompletionListener;
    private boolean isLooping = false, isPaused = false;
    private Callable<Integer> myFuncIn = null;
    private Callable<Integer> showThumb = null;
    private Activity _act;

    public void setShowThumb(Callable<Integer> showThumb) {
        this.showThumb = showThumb;
    }

    public CustomVideoView(Context context) {
        this(context, null, 0);
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public void setSource(Uri source) {
        mSource = source;
    }

    public void set_act(Activity _act) {
        this._act = _act;
    }

    public void setMyFuncIn(Callable<Integer> myFuncIn) {
        this.myFuncIn = myFuncIn;
    }

    public void startVideo() {
        Log.d("k9k9", "startVideo: ");
        if (!isPaused) {
            setSurfaceTextureListener(this);
            if (this.getSurfaceTexture() != null) {
                if (mMediaPlayer != null) {
                    mMediaPlayer.start();
                    try {
                        myFuncIn.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Surface surface = new Surface(this.getSurfaceTexture());
                    try {
                        mMediaPlayer = new MediaPlayer();
                        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN) {
                            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    _act.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                myFuncIn.call();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });
                                }
                            });
                        } else {
                            mMediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                                @Override
                                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                                    if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                                        _act.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    myFuncIn.call();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        });

                                    }
                                    return false;
                                }
                            });
                        }

//                  mMediaPlayer.setOnCompletionListener(mCompletionListener);
//                  mMediaPlayer.setOnBufferingUpdateListener(this);
//                  mMediaPlayer.setOnErrorListener(this);
                        mMediaPlayer.setLooping(isLooping);
                        mMediaPlayer.setDataSource(getContext(), mSource);
                        mMediaPlayer.setSurface(surface);
                        mMediaPlayer.prepare();
                        if (mMediaPlayer != null) mMediaPlayer.start();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

//    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
//        mCompletionListener = listener;
//    }

    public void setLooping(boolean looping) {
        isLooping = looping;
    }

    @Override
    protected void onDetachedFromWindow() {
        // release resources on detach
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        try {
            if (showThumb != null) showThumb.call();
        } catch (Exception e) {
            //e.printStackTrace();
        }
        super.onDetachedFromWindow();
    }


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        Log.d("k9k9", "onSurfaceTextureAvailable: ");
        if (!isPaused) {
            if (mMediaPlayer != null) {
                mMediaPlayer.start();
                try {
                    myFuncIn.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Surface surface = new Surface(surfaceTexture);
                try {
                    mMediaPlayer = new MediaPlayer();
                    if (myFuncIn != null) {
                        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN) {
                            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    _act.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                myFuncIn.call();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });
                                }
                            });
                        } else {
                            mMediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                                @Override
                                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                                    if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                                        _act.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    myFuncIn.call();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        });
                                    }
                                    return false;
                                }
                            });
                        }

                    }

//            mMediaPlayer.setOnCompletionListener(mCompletionListener);
//            mMediaPlayer.setOnBufferingUpdateListener(this);
//            mMediaPlayer.setOnErrorListener(this);
                    mMediaPlayer.setLooping(isLooping);
                    mMediaPlayer.setDataSource(getContext(), mSource);
                    mMediaPlayer.setSurface(surface);
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();

                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        Log.d("k9k9", "onSurfaceTextureDestroyed: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //pre lollipop needs SurfaceTexture it owns before calling onDetachedFromWindow super
            surface.release();
        }
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

        try {
            showThumb.call();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    public void clearAll() {
        if (getSurfaceTexture() != null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //pre lollipop needs SurfaceTexture it owns before calling onDetachedFromWindow super
                getSurfaceTexture().release();
            }
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
        }
        setSurfaceTextureListener(null);
    }

    public void pauseVideo() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    public boolean isPlaying() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    public void muteVideo() {
        if (mMediaPlayer != null)
            mMediaPlayer.setVolume(0f, 0f);
    }

    public void unmuteVideo() {
        if (mMediaPlayer != null)
            mMediaPlayer.setVolume(1f, 1f);
    }

}