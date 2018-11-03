package com.example.bar.sharedrecipes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyMethodsStepsView extends LinearLayout {
    protected EditText methodsET;
    protected TextView methodsCounterTV;
    protected View methodsCancelBtn;
    public MyMethodsStepsView(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (inflater != null) {
            inflater.inflate(R.layout.my_methods_steps, this);
        }
        methodsET = (EditText) findViewById(R.id.method_steps_edit_text);
        methodsCounterTV = (TextView) findViewById(R.id.method_steps_counter);
        methodsCancelBtn =  findViewById(R.id.method_steps_X);
    }
}
