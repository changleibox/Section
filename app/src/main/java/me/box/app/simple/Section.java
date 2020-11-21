package me.box.app.simple;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.IntDef;
import androidx.annotation.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class Section extends LinearLayout {
    /**
     * Don't show any dividers.
     */
    public static final int DIVIDER_NONE = 0;
    /**
     * Show a divider at the beginning of the group.
     */
    public static final int DIVIDER_START = 1;
    /**
     * Show dividers between each item in the group.
     */
    public static final int DIVIDER_MIDDLE = 1 << 1;
    /**
     * Show a divider at the end of the group.
     */
    public static final int DIVIDER_END = 1 << 2;

    @IntDef(flag = true, value = {DIVIDER_NONE, DIVIDER_START, DIVIDER_MIDDLE, DIVIDER_END})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DividerModel {
    }

    private final List<View> mDividers = new ArrayList<>();

    private int mDividerColor;
    private int mDividerSize;
    private View mHeader;
    private View mFooter;
    private int mShowDividerModels;
    private DividerBuilder mDividerBuilder;

    public Section(Context context) {
        this(context, null);
    }

    public Section(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Section(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Section, defStyleAttr, 0);
        mDividerColor = typedArray.getColor(R.styleable.Section_dividerColor, Color.TRANSPARENT);
        mDividerSize = typedArray.getDimensionPixelOffset(R.styleable.Section_dividerSize, 0);
        mShowDividerModels = typedArray.getInt(R.styleable.Section_showDividerModel, DIVIDER_NONE);
        final int headerResourceId = typedArray.getResourceId(R.styleable.Section_header, 0);
        final LayoutInflater inflater = LayoutInflater.from(context);
        if (headerResourceId != 0) {
            mHeader = inflater.inflate(headerResourceId, this, false);
        }
        final int footerResourceId = typedArray.getResourceId(R.styleable.Section_footer, 0);
        if (footerResourceId != 0) {
            mFooter = inflater.inflate(footerResourceId, this, false);
        }
        typedArray.recycle();
    }

    /**
     * 设置divider颜色
     *
     * @param dividerColor divider颜色
     */
    public void setDividerColor(@ColorInt int dividerColor) {
        if (mDividerColor == dividerColor) {
            return;
        }
        this.mDividerColor = dividerColor;
        resolveDecorate();
    }

    /**
     * 设置divider的大小
     *
     * @param dividerSize divider大小
     */
    public void setDividerSize(int dividerSize) {
        if (mDividerSize == dividerSize) {
            return;
        }
        this.mDividerSize = dividerSize;
        resolveDecorate();
    }

    /**
     * 设置显示的header
     *
     * @param header 头控件
     */
    public void setHeader(@Nullable View header) {
        if (mHeader == header) {
            return;
        }
        if (mHeader != null) {
            removeView(mHeader);
        }
        this.mHeader = header;
        resolveDecorate();
    }

    /**
     * 设置显示的footer
     *
     * @param footer 脚控件
     */
    public void setFooter(@Nullable View footer) {
        if (mFooter == footer) {
            return;
        }
        if (mFooter != null) {
            removeView(mFooter);
        }
        this.mFooter = footer;
        resolveDecorate();
    }

    /**
     * 设置divider显示模式
     *
     * @param showDividerModels divider显示模式
     */
    public void setShowDividerModels(@DividerModel int showDividerModels) {
        if (mShowDividerModels == showDividerModels) {
            return;
        }
        mShowDividerModels = showDividerModels;
        resolveDecorate();
    }

    /**
     * 设置动态构建divider接口
     *
     * @param dividerBuilder 构建divider接口
     */
    public void setDividerBuilder(DividerBuilder dividerBuilder) {
        if (mDividerBuilder == dividerBuilder) {
            return;
        }
        mDividerBuilder = dividerBuilder;
        resolveDecorate();
    }

    /**
     * 是否显示divider
     */
    public boolean isShowDividers() {
        return mShowDividerModels != DIVIDER_NONE && (mDividerBuilder != null || mDividerSize > 0);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        resolveDecorate();
    }

    private void resolveDecorate() {
        if (mHeader != null) {
            removeView(mHeader);
        }

        if (mFooter != null) {
            removeView(mFooter);
        }

        resolveDivider();

        if (mHeader != null) {
            super.addView(mHeader, 0, mHeader.getLayoutParams());
        }
        if (mFooter != null) {
            super.addView(mFooter, -1, mFooter.getLayoutParams());
        }
    }

    private void resolveDivider() {
        for (View divider : mDividers) {
            removeView(divider);
        }
        mDividers.clear();
        if (!isShowDividers()) {
            return;
        }
        final boolean showStart = (mShowDividerModels & DIVIDER_START) == DIVIDER_START;
        final boolean showMiddle = (mShowDividerModels & DIVIDER_MIDDLE) == DIVIDER_MIDDLE;
        final boolean showEnd = (mShowDividerModels & DIVIDER_END) == DIVIDER_END;
        final int childCount = getChildCount();
        int length = childCount;
        if (showMiddle) {
            length = childCount * 2 - 1;
        }
        if (showStart) {
            length++;
        }
        if (showEnd) {
            length++;
        }
        for (int i = 0; i < length; i++) {
            if (isShowDivider(showStart, showMiddle, childCount, i)) {
                continue;
            }
            View divider = getDivider(i);
            mDividers.add(divider);
            final LayoutParams dividerParams = getDividerLayoutParams(divider);
            super.addView(divider, i, dividerParams);
        }
    }

    private boolean isShowDivider(boolean showStart, boolean showMiddle, int childCount, int i) {
        return showMiddle && (showStart && i % 2 != 0 || !showStart && i % 2 == 0)
                || !showMiddle && ((!showStart || i > 0) && (showStart && i == childCount || i < childCount));
    }

    private View getDivider(int i) {
        View divider;
        if (mDividerBuilder == null) {
            divider = new View(getContext());
            divider.setBackgroundColor(mDividerColor);
        } else {
            divider = mDividerBuilder.build(i / 2, mDividerColor, mDividerSize);
        }
        return divider;
    }

    private LayoutParams getDividerLayoutParams(View divider) {
        LayoutParams dividerParams = (LayoutParams) divider.getLayoutParams();
        if (dividerParams != null) {
            return dividerParams;
        }
        switch (getOrientation()) {
            case LinearLayout.HORIZONTAL:
                return new LayoutParams(mDividerSize, LayoutParams.MATCH_PARENT);
            case LinearLayout.VERTICAL:
                return new LayoutParams(LayoutParams.MATCH_PARENT, mDividerSize);
            default:
                return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        }
    }

    public interface DividerBuilder {
        View build(int index, int dividerColor, int dividerSize);
    }
}