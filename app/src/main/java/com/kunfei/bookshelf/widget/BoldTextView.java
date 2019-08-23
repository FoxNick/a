package com.kunfei.bookshelf.widget;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class BoldTextView extends AppCompatTextView {

    private Paint mTextPaint;

    public BoldTextView(Context context) {
        super(context);
    }


    public BoldTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public BoldTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
    {
        super(paramContext, paramAttributeSet, paramInt);
        getPaint().setFakeBoldText(true);
    }

}
