package com.erly.shakedemo.base;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public abstract class BasePresenter<V> {
    protected Reference<V> mReference;

    protected void attachView(V view){
        mReference = new WeakReference<V>(view);
    }

    protected V getView(){
        return mReference.get();
    }

    protected void detachView(){
        if (mReference != null){
            mReference.clear();
            mReference = null;
        }
    }
}
