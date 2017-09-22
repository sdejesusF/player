package com.centralway.player.views;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;

import com.centralway.player.R;

/**
 * Created by sergiodejesus on 6/24/17.
 */
public class Checkbox extends AppCompatCheckBox {

    public Checkbox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setChecked(boolean t) {
        if (t) {
            this.setBackgroundResource(R.drawable.checkbox_selected);
        } else {
            this.setBackgroundResource(R.drawable.checkbox_default);
        }
        super.setChecked(t);
    }
}
