package com.example.bar.sharedrecipes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

public class MyLoadingFragment extends DialogFragment {

    private ProgressBar progressBar;
    int progress = 0;
    private CountDownTimer countDownTimer;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.loading_fagment,null);
        progressBar = (ProgressBar) view.findViewById(R.id.Loading_Fragment_progress_bar);
        start();
        return new AlertDialog.Builder(getActivity()).setView(view).create();



    }
    public void start(){
        progressBar.setProgress(progress);
        countDownTimer = new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long l) {
                progress += 25;
                progressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                dismiss();
            }
        };
        countDownTimer.start();
    }


}
