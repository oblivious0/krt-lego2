package com.krt.lego.oc.imp.custom;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.krt.lego.R;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author xzy
 * @package krt.wid.tour_ja.ui.view
 * @description
 * @time 2020/8/11
 */
public class MapView extends FrameLayout {

    /**
     * 左上角的坐标
     */
    private PointF leftPoint = new PointF();
    /**
     * 右上角的坐标
     */
    private PointF topPoint = new PointF();
    /**
     * 右下角的坐标
     */
    private PointF rightPoint = new PointF();
    /**
     * 左下角的坐标
     */
    private PointF bottomPoint = new PointF();
    /**
     * 触碰中心的点
     */
    private PointF midPoint = new PointF();
    /**
     * 缩放的等级
     */
    private float scaleLevel = 1.0f;
    private float maxLevel = 2.0f;
    private float minLevel = 0.8f;
    /**
     * 背景的ID
     */
    private int backgroundResId;
    /**
     * 背景的图片
     */
    private Bitmap bitmapSrc;
    /**
     * 是否在运动中
     */
    private boolean isAnmiting;
    /**
     * 上一个点的X坐标
     */
    private float lastX;
    /**
     * 上一个点的Y坐标
     */
    private float lastY;
    /**
     * 是否是多点触控
     */
    private boolean isMultiPoint = false;
    /**
     * 上一个两点距离的位置
     */
    private float lastDistance;
    /**
     * 控件的宽
     */
    private int width;
    /**
     * 控件的高
     */
    private int height;
    /**
     * 动画结束的Y坐标
     */
    private float endY;
    /**
     * 动画结束的x坐标
     */
    private float endX;
    /**
     * 动画开始的x坐标
     */
    private float startX;
    /**
     * 动画结束的x坐标
     */
    private float startY;
    /**
     * 动画的时间
     */
    private long animatingDuring = 400;
    /**
     * 垂直位置最大偏移量
     */
    private int maxHeightOffset = 225;
    /**
     * 水平位置最大偏移量
     */
    private int maWidthOffset = 150;
    /**
     * 储存holder的缓存
     */
    private HashMap<Integer, ArrayList<ViewHolder>> mHolderPool = new HashMap<>();
    /**
     * 储存添加在控件里面的控件
     */
    private HashMap<Integer, ViewHolder> addedPools = new HashMap<>();

    private int imageWidth = -1;
    private int imageHeight = -1;

    /**
     * 适配器
     */
    private Adapter mAdapter;
    /**
     * 背景
     */
    private ImageView backgroundMap;
    /**
     * 转化为单指操作
     */
    private boolean isSwitchToSingle = false;

    public MapView(@NonNull Context context) {
        super(context);
    }

    public MapView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }


    public MapView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void setImageSize(int w, int h) {
        imageWidth = w;
        imageHeight = h;
    }

    /**
     * 初始化控件
     *
     * @param attrs
     */
    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MapView);
//        backgroundResId = typedArray.getResourceId(R.styleable.MapView_map_background,R.mipmap.map);
//        backgroundMap.setBackgroundResource(backgroundResId);
    }

    @SuppressLint("CheckResult")
    public void draw(String imgurl) {
        LogUtils.e(imgurl);
        backgroundMap = new ImageView(getContext());
        Observable.create(emitter -> {

            emitter.onNext(imgurl);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .map(s -> {
                    Bitmap bitmap = Glide.with(getContext())
                            .asBitmap()
                            .centerCrop()
                            .load(s)
                            .submit()
                            .get();

                    return bitmap;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bitmap -> {
                    bitmapSrc = bitmap;
                    backgroundMap.setImageBitmap(bitmap);
                    addView(backgroundMap, 0, new LayoutParams(imageWidth, imageHeight));
                });

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (width == 0 && height == 0) {
            width = getMeasuredWidth();
            height = getMeasuredHeight();
            Log.w("xzy", 757 * ScreenUtils.getScreenDensity() / 3 + ";" + (int) (1033 * ScreenUtils.getScreenDensity() / 2.95));
            leftPoint.set(757 * ScreenUtils.getScreenDensity() / 3, (int) (1033 * ScreenUtils.getScreenDensity() / 2.95));
            updatePoint();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (bitmapSrc == null) {
            return;
        }
        float bitmapLeft = -leftPoint.x * scaleLevel;
        float bitmapTop = -leftPoint.y * scaleLevel;
        backgroundMap.layout((int) bitmapLeft, (int) bitmapTop, (int) (bitmapLeft + bitmapSrc.getWidth() * scaleLevel), (int) (bitmapTop + bitmapSrc.getHeight() * scaleLevel));
        if (mAdapter != null) {
            for (int i = 1; i < getChildCount(); i++) {
                int tag = (int) getChildAt(i).getTag();
                PointF position = mAdapter.onBindPosition(tag);
                int viewLeft = (int) ((position.x - leftPoint.x) * scaleLevel);
                int viewTop = (int) ((position.y - leftPoint.y) * scaleLevel);
                getChildAt(i).layout(viewLeft, viewTop, viewLeft + getChildAt(i).getMeasuredWidth(), viewTop + getChildAt(i).getMeasuredHeight());

            }

        }
    }


    /**
     * 判断点是否在区域里
     *
     * @param position
     * @return
     */

    private boolean isInScreen(PointF position) {
        return position.x > leftPoint.x && position.y > leftPoint.y && position.x < rightPoint.x && position.y < rightPoint.y;
    }

    /**
     * 获取holder
     *
     * @param itemViewType
     * @return
     */
    private ViewHolder getHolder(int itemViewType) {
        ViewHolder holder;
        if (mHolderPool.get(itemViewType) == null || mHolderPool.get(itemViewType).size() == 0) {
            holder = mAdapter.onCreateViewHolder(MapView.this, itemViewType);
        } else {
            holder = mHolderPool.get(itemViewType).get(0);
            mHolderPool.get(itemViewType).remove(0);
        }

        return holder;

    }

    public void setScaleLevel(float scaleLevel) {
        this.scaleLevel = scaleLevel;
    }

    public void setMaxLevel(float maxLevel) {
        this.maxLevel = maxLevel;
    }

    public void setMinLevel(float minLevel) {
        this.minLevel = minLevel;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (isAnmiting) {
            return false;
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:

                lastX = event.getX();
                lastY = event.getY();

                break;
            case MotionEvent.ACTION_MOVE:

                if (isMultiPoint) {
                    float distance = calculateDistance(event);
                    if (distance * scaleLevel / lastDistance >= 0.8 && distance * scaleLevel / lastDistance <= 1.8) {
                        float centerX = leftPoint.x + midPoint.x / scaleLevel;
                        float centerY = leftPoint.y + midPoint.y / scaleLevel;

                        scaleLevel = distance * scaleLevel / lastDistance;
                        if (scaleLevel > maxLevel) scaleLevel = maxLevel;
                        if (scaleLevel < minLevel) scaleLevel = minLevel;

                        leftPoint.x = centerX - midPoint.x / scaleLevel;
                        leftPoint.y = centerY - midPoint.y / scaleLevel;

                        updatePoint();
                        lastDistance = distance;
                    }
                } else {

                    if (!isSwitchToSingle) {

                        leftPoint.set(leftPoint.x + (lastX - event.getX()) / scaleLevel, leftPoint.y + (lastY - event.getY()) / scaleLevel);


                        if (leftPoint.x < -maWidthOffset) {
                            leftPoint.x = -maWidthOffset;
                        } else if (leftPoint.x > 0 && bitmapSrc.getWidth() * scaleLevel > width && leftPoint.x > bitmapSrc.getWidth() - width / scaleLevel + maWidthOffset) {
                            leftPoint.x = bitmapSrc.getWidth() - width / scaleLevel + maWidthOffset;
                        }

                        if (leftPoint.y < -maxHeightOffset) {
                            leftPoint.y = -maxHeightOffset;
                        } else if (leftPoint.y > 0 && bitmapSrc.getHeight() * scaleLevel > height && leftPoint.y > bitmapSrc.getHeight() - height / scaleLevel + maxHeightOffset) {
                            leftPoint.y = bitmapSrc.getHeight() - height / scaleLevel + maxHeightOffset;
                        }

                        updatePoint();

                    } else {
                        isSwitchToSingle = false;
                    }


                }
                lastX = event.getX();
                lastY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (scaleLevel <= 0.8 || scaleLevel >= 1.8) {
                    return false;
                }
                if (leftPoint.x < 0 || leftPoint.y < 0 || (leftPoint.x > 0 && bitmapSrc.getWidth() * scaleLevel > width && leftPoint.x > bitmapSrc.getWidth() - width / scaleLevel) || (leftPoint.y > 0 && bitmapSrc.getHeight() * scaleLevel > height && leftPoint.y > bitmapSrc.getHeight() - height / scaleLevel)) {
                    isAnmiting = true;


                    startX = leftPoint.x;
                    startY = leftPoint.y;
                    endY = leftPoint.y;
                    endX = leftPoint.x;

                    if (leftPoint.x < 0) {
                        endX = 0;


                    }
                    if (leftPoint.y < 0) {
                        endY = 0;

                    }
                    if (leftPoint.x > 0 && bitmapSrc.getWidth() * scaleLevel > width && leftPoint.x > bitmapSrc.getWidth() - width / scaleLevel) {
                        endX = bitmapSrc.getWidth() - width / scaleLevel;
                    }
                    if (leftPoint.y > 0 && bitmapSrc.getHeight() * scaleLevel > height && leftPoint.y > bitmapSrc.getHeight() - height / scaleLevel) {
                        endY = bitmapSrc.getHeight() - height / scaleLevel;
                    }


                    ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
                    valueAnimator.setDuration(animatingDuring);
                    valueAnimator.start();


                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {

                            leftPoint.x = startX + (endX - startX) * Float.parseFloat(animation.getAnimatedValue().toString()) * 0.01f;
                            leftPoint.y = startY + (endY - startY) * Float.parseFloat(animation.getAnimatedValue().toString()) * 0.01f;
                            updatePoint();
                            requestLayout();
                        }
                    });
                    valueAnimator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                            isAnmiting = true;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {

                            isAnmiting = false;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                            isAnmiting = false;
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {


                        }
                    });
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:

                isMultiPoint = true;
                lastDistance = calculateDistance(event);
                midPoint(midPoint, event);
                break;

            case MotionEvent.ACTION_POINTER_UP:
                isSwitchToSingle = true;
                isMultiPoint = false;
                break;
            default:
                break;
        }

        requestLayout();
        return true;

    }

    /**
     * 更新数据
     */
    public void notifyDataChanged() {
        removeAllViews();
        addView(backgroundMap, 0);
        if (mAdapter != null) {
            for (int i = 0; i < mAdapter.getItemCount(); i++) {
                int itemViewType = mAdapter.getItemViewType(i);
                ViewHolder holder = getHolder(itemViewType);
                mAdapter.onBindViewHolder(holder, i);
                if (holder.itemView.getParent() == null) {
                    addView(holder.itemView);
                    holder.itemView.setTag(i);
                }
            }
        }
    }


    /**
     * 更新四个顶点
     */
    private void updatePoint() {
        float x = leftPoint.x;
        float y = leftPoint.y;
        topPoint.set(x + width / scaleLevel, y + height / scaleLevel);
        bottomPoint.set(x, y + height / scaleLevel);
        rightPoint.set(x + width / scaleLevel, y + height / scaleLevel);
//        Log.w("xzy", "topPoint:"+topPoint + ";bottomPoint=" + bottomPoint + ";leftPoint=" + leftPoint + ";rightPoint=" + rightPoint);
    }

    // 计算两个触摸点之间的距离
    private float calculateDistance(MotionEvent event) {
        try {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return (float) Math.sqrt(x * x + y * y);
        } catch (Exception e) {
        }
        return 1;
    }

    /**
     * 计算中点
     *
     * @param point
     * @param event
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /**
     * 设置 适配器
     *
     * @param mAdapter
     */
    public void setAdapter(Adapter mAdapter) {
        this.mAdapter = mAdapter;
        if (mAdapter != null) {
            for (int i = 0; i < mAdapter.getItemCount(); i++) {
                int itemViewType = mAdapter.getItemViewType(i);
                PointF position = mAdapter.onBindPosition(i);
                ViewHolder holder = getHolder(itemViewType);
                mAdapter.onBindViewHolder(holder, i);
                if (holder.itemView.getParent() == null) {
                    addView(holder.itemView);
                    holder.itemView.setTag(i);
                }
            }
        }
    }

    public Adapter getAdapter() {
        return mAdapter;
    }

    public abstract static class Adapter<VH extends ViewHolder> {

        public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

        public abstract void onBindViewHolder(VH holder, int position);

        /**
         * 返回这个点所在的坐标
         *
         * @param position
         * @return
         */
        public abstract PointF onBindPosition(int position);

        public int getItemViewType(int position) {
            return 0;
        }

        public long getItemId(int position) {
            return NO_ID;
        }

        public abstract int getItemCount();

        public final void notifyDataSetChanged() {
        }

        public final void notifyItemChanged(int position) {

        }

        public final void notifyItemMoved(int fromPosition, int toPosition) {

        }

        public final void notifyItemRemoved(int position) {

        }

    }

    public static abstract class ViewHolder {

        public final View itemView;

        public ViewHolder(View itemView) {
            if (itemView == null) {
                throw new IllegalArgumentException("itemView may not be null");
            }
            this.itemView = itemView;
        }
    }
}
