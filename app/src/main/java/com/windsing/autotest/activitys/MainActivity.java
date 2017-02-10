package com.windsing.autotest.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.windsing.autotest.R;
import com.windsing.autotest.tools.ExecCmd;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    protected String cmd;
    protected String run_times = "1"; //设置运行次数
    protected String file = "/sdcard/autoTestSource/smokeScript.txt"; //执行的脚本文件，建议使用环境变量的方式。
    public ExecCmd execCmd;
    private Button run_monkey_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        execCmd = new ExecCmd();

        /*
        运行冒烟测试case
         */
        run_monkey_button = (Button) findViewById(R.id.run_monkey_button);
        run_monkey_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cmd = "monkey -f  " +
                                "--ignore-crashes " +
                                "--ignore-timeouts " +
                                "--ignore-security-exceptions " +
                                "--ignore-native-crashes " + run_times;
                        execCmd.toExecCmd(cmd);

                    }
                });
            }
        });
    }

    /**
     * 运行冒烟测试
     * @param view
     */
    public void run_monkey_test(View view){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    public void monkey_test_report(View view){

    }
    /**
     * 执行cmd命令，且输出信息到控制台
     */
}
