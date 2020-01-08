package com.codesense.driverapp.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class AppEdittext extends EditText {

    public OnActionClickListener onActionClickListener;

    public AppEdittext(Context context) {
        super(context);
    }

    public AppEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppEdittext(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnActionClickListener(OnActionClickListener onActionClickListener) {
        this.onActionClickListener = onActionClickListener;
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        if (id == android.R.id.paste) {
            if (null != onActionClickListener) {
                onActionClickListener.update();
                //return false;
            }
        }
        return super.onTextContextMenuItem(id);
    }

    public interface OnActionClickListener {
        void update();
    }
}
