package com.qianlu.corelibrary;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;


public class VerificationCodeInput extends ViewGroup {

    private final static String TYPE_NUMBER = "number";
    private final static String TYPE_TEXT = "text";
    private final static String TYPE_PASSWORD = "password";
    private final static String TYPE_PHONE = "phone";
    private static final String TAG = "VerificationCodeInput";

    private int box = 4;
    private int boxWidth = 70;
    private int boxHeight = 70;
    private int childHPadding = 14;
    private int childVPadding = 14;
    private String inputType = TYPE_NUMBER;
    private Drawable boxBgFocus = null;
    private Drawable boxBgNormal = null;
    private CompleteListener mCompleteListener;


    public VerificationCodeInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.vericationCodeInput);
        box = a.getInt(R.styleable.vericationCodeInput_box, 4);
        childHPadding = (int) a.getDimension(R.styleable.vericationCodeInput_child_h_padding, 0);
        childVPadding = (int) a.getDimension(R.styleable.vericationCodeInput_child_v_padding, 0);
        boxBgFocus = a.getDrawable(R.styleable.vericationCodeInput_box_bg_focus);
        boxBgNormal = a.getDrawable(R.styleable.vericationCodeInput_box_bg_normal);
        inputType = a.getString(R.styleable.vericationCodeInput_inputType);
        boxWidth = (int) a.getDimension(R.styleable.vericationCodeInput_child_width, boxWidth);
        boxHeight = (int) a.getDimension(R.styleable.vericationCodeInput_child_height, boxHeight);
        a.recycle();

        initEditTexts();
    }


    OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            setBg((EditText) v, hasFocus);
        }
    };


    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 初始化输入框
     */
    private void initEditTexts() {
        for (int i = 0; i < box; i++) {
            final EditText editText = new EditText(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dp2px(getContext(), boxWidth), dp2px(getContext(), boxHeight));
            layoutParams.bottomMargin = childVPadding;
            layoutParams.topMargin = childVPadding;
            layoutParams.leftMargin = childHPadding;
            layoutParams.rightMargin = childHPadding;
            layoutParams.gravity = Gravity.CENTER;

            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 45);
            editText.setTextColor(Color.BLACK);
            editText.setLayoutParams(layoutParams);
            editText.setPadding(0, dp2px(getContext(), 10), 0, 0);
            editText.setGravity(Gravity.CENTER);
            //  最多输入一个
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
            editText.setInputType(InputType.TYPE_NULL);

            editText.setOnFocusChangeListener(onFocusChangeListener);
            editText.setId(i);
            editText.setEms(1);

            addView(editText, i);

            if (i == 0) {
                editText.requestFocus();
            }

            setBg(editText, i == 0);

        }
    }

    /**
     * 输入的结果
     */
    private final String[] nummbers = new String[4];

    /**
     * 模拟软键盘输入
     *
     * @param number 选择的数字
     */
    public void insertNumber(String number) {
        int count = getChildCount();
        EditText editText;
        for (int i = 0; i < count; i++) {
            editText = (EditText) getChildAt(i);
            if (editText.isFocused()) {
                nummbers[i] = number;

                editText.setText("*");

                //  下一个获取焦点
                if (editText.isFocused() && editText.getText().length() == 1 && i < count - 1) {
                    editText = (EditText) getChildAt(i + 1);
                    editText.requestFocus();
                }
                break;
            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (String nummber : nummbers) {
            stringBuilder.append(nummber);
        }

        if (stringBuilder.toString().length() == 4 && mCompleteListener != null) {
            mCompleteListener.onComplete(stringBuilder.toString());
        }
    }


    /**
     * 设置 EditText 背景
     *
     * @param editText
     * @param focus
     */
    private void setBg(EditText editText, boolean focus) {
        if (boxBgNormal != null && !focus) {
            editText.setBackground(boxBgNormal);
        } else if (boxBgFocus != null && focus) {
            editText.setBackground(boxBgFocus);
        }
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LinearLayout.LayoutParams(getContext(), attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int parentWidth = getMeasuredWidth();
        if (parentWidth == LayoutParams.MATCH_PARENT) {
            parentWidth = getScreenWidth();
        }

        Log.d(getClass().getName(), "onMeasure width " + parentWidth);

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }

        if (count > 0) {
            View child = getChildAt(0);
            int cWidth = child.getMeasuredWidth();
            if (parentWidth != LayoutParams.WRAP_CONTENT) {
                //  重新计算 padding
                childHPadding = (parentWidth - cWidth * count) / (count + 1);
            }

            int cHeight = child.getMeasuredHeight();

            int maxH = cHeight + 2 * childVPadding;
            int maxW = (cWidth) * count + childHPadding * (count + 1);

            setMeasuredDimension(
                    resolveSize(maxW, widthMeasureSpec),
                    resolveSize(maxH, heightMeasureSpec)
            );
        }
    }


    private int getScreenWidth() {
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(getClass().getName(), "onLayout width = " + getMeasuredWidth());

        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            child.setVisibility(View.VISIBLE);
            int cWidth = child.getMeasuredWidth();
            int cHeight = child.getMeasuredHeight();
            int cl = childHPadding + (i) * (cWidth + childHPadding);
            int cr = cl + cWidth;
            int ct = childVPadding;
            int cb = ct + cHeight;
            child.layout(cl, ct, cr, cb);
        }
    }


    /**
     * 设置完成事件
     */
    public void setOnCompleteListener(CompleteListener completeListener) {
        this.mCompleteListener = completeListener;
    }


    public interface CompleteListener {
        void onComplete(String content);
    }

}

