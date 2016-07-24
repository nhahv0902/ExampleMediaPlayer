package com.nhahv.mediaplayer.interfaces;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Nhahv on 7/24/2016.
 * <></>
 */
public class OnClickRecyclerView implements RecyclerView.OnItemTouchListener {

    private OnClick mOnClick;
    private GestureDetector mGestureDetector;

    public OnClickRecyclerView(Context context, RecyclerView recyclerView, OnClick onClick) {
        mOnClick = onClick;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null && mOnClick != null && mGestureDetector.onTouchEvent(e)) {
            mOnClick.onClick(child, rv.getChildPosition(child));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
