package com.krt.lego.oc.lifecyler;

import android.annotation.SuppressLint;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * author: MaGua
 * create on:2021/2/3 14:43
 * description Subgrade生命周期观察者
 */
public class MLifecycleObserver implements LifecycleObserver {

    public static final String LIFECYCLE_START = "onStart";
    public static final String LIFECYCLE_RESUME = "onResume";
    public static final String LIFECYCLE_PAUSE = "onPause";
    public static final String LIFECYCLE_STOP = "onStop";
    public static final String LIFECYCLE_DESTROY = "onDestroy";
    public static final String LIFECYCLE_PREPARE = "onPrepare";

    List<MLifecycleEvent> events;
    LifecycleOwner owner;

    public MLifecycleObserver(LifecycleOwner owner) {
        this.owner = owner;
        events = new ArrayList<>();
    }

    public void addLifecycleObserver(MLifecycleEvent observer){
        events.add(observer);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        handleLifecycleEvent(LIFECYCLE_START);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        handleLifecycleEvent(LIFECYCLE_RESUME);

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        handleLifecycleEvent(LIFECYCLE_PAUSE);

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        handleLifecycleEvent(LIFECYCLE_STOP);

    }

    public void onPrepare() {
        handleLifecycleEvent(LIFECYCLE_PREPARE);
    }


    @SuppressLint("CheckResult")
    public void handleLifecycleEvent(String lifecycle_type) {
        Observable.fromIterable(events)
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner.getLifecycle(), Lifecycle.Event.ON_DESTROY)))
                .subscribe(mLifecycleEvent -> {
                    switch (lifecycle_type) {
                        case LIFECYCLE_START:
                            mLifecycleEvent.onStart();
                            break;
                        case LIFECYCLE_RESUME:
                            mLifecycleEvent.onResume();
                            break;
                        case LIFECYCLE_PAUSE:
                            mLifecycleEvent.onPause();
                            break;
                        case LIFECYCLE_STOP:
                            mLifecycleEvent.onStop();
                            break;
                        case LIFECYCLE_DESTROY:
                            mLifecycleEvent.onDestroy();
                            break;
                        case LIFECYCLE_PREPARE:
                            mLifecycleEvent.onPrepare();
                            break;
                        default:
                    }
                });
    }

}
