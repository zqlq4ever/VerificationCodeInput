package com.dalimao.verificationcodeinput;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dalimao.corelibrary.VerificationCodeInput;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final VerificationCodeInput input = (VerificationCodeInput) findViewById(R.id.ver_number2);
        final TextView tvNumber = (TextView) findViewById(R.id.tv_number);
        final TextView tv2 = (TextView) findViewById(R.id.tv_2);

        tvNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.insertNumber(tvNumber.getText().toString());
            }
        });

        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.insertNumber(tv2.getText().toString());
            }
        });

        input.setOnCompleteListener(new VerificationCodeInput.CompleteListener() {
            @Override
            public void onComplete(String content) {
                Log.d(TAG, "完成输入：" + content);
            }
        });
    }
}
