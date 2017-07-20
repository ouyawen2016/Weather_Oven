package com.oven.weather_oven.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 *
 *
 * Created by oven on 2017/7/20.
 */

public class AreaDividerItemDecoration extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };
    public static final int HORIZONTAL_LTST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LTST = LinearLayoutManager.VERTICAL;
    /**
     * 绘制间隔样式
     */
    private Drawable mDivider;
    /**
     * 列表的方向，水平/竖直
     */
    private int mOrientation;

    public AreaDividerItemDecoration(Context context, int Orientation) {
        //获取默认主题的属性
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        setOrientation(Orientation);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDraw(c, parent, state);
            //绘制间隔
            if (mOrientation == VERTICAL_LTST) {
                drawVerical(c, parent);
            } else {
                drawHorizontal(c, parent);
            }
        }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
            if (mOrientation == VERTICAL_LTST) {
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            } else {
                outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
            }
        }


    private void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LTST && orientation != VERTICAL_LTST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    /**
     * 绘制间隔竖直列表
     * @param c
     * @param parent
     *
     */
    private  void drawVerical(Canvas c,RecyclerView parent){
    final  int left = parent.getPaddingLeft();
    final  int right = parent.getWidth() - parent.getPaddingRight();
    final  int childCount = parent.getChildCount();
    for (int i = 0; i < childCount;i ++){
        final View child = parent.getChildAt(i);
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)child.getLayoutParams();
        final int top = child.getBottom() + params.bottomMargin + Math.round(ViewCompat.getTranslationY(child));
        final int buttom = top + mDivider.getIntrinsicHeight();
        mDivider.setBounds(left,top,right,buttom);
        mDivider.draw(c);
        }
    }
    /**
     *绘制间隔水平列表
     */
    private  void drawHorizontal(Canvas c,RecyclerView parent){
        final  int top = parent.getPaddingTop();
        final  int bottom = parent.getHeight() - parent.getPaddingBottom();
        final  int childCount = parent.getChildCount();
        for (int i = 0; i < childCount;i ++){
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)child.getLayoutParams();
            final int left= child.getRight() + params.rightMargin + Math.round(ViewCompat.getTranslationY(child));
            final int right= left + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left,top,right,bottom);
            mDivider.draw(c);
        }
    }
}