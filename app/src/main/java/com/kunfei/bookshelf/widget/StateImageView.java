package com.kunfei.bookshelf.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class StateImageView extends AppCompatImageView implements Checkable {

    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};


    private boolean checked;

    public StateImageView(Context context) {
        this(context, null);
    }

    public StateImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateImageView(Context context, @Nullable AttributeSet paramAttributeSet, int defStyleAttr) {
        super(context, paramAttributeSet, defStyleAttr);
        boolean bool = false;
        if (paramAttributeSet != null) {
            bool = paramAttributeSet.getAttributeBooleanValue("http://schemas.android.com/apk/res/android", "checked", false);
        }
        this.checked = bool;
    }

    public void setChecked(boolean paramBoolean)
    {
        this.checked = paramBoolean;
        refreshDrawableState();
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        setChecked(!checked);
    }

    @Override
    public int[] onCreateDrawableState(final int extraSpace) {
        // 复写 onCreateDrawableState, 并增加给出新的flag位置, 并根据当前状态，merge进相应的flag
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked())
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        return drawableState;
    }

}
