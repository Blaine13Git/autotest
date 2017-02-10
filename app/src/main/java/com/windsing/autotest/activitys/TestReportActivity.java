package com.windsing.autotest.activitys;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.windsing.autotest.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestReportActivity extends AppCompatActivity {
    public static final String REPORT_DIR_PATH = "/mnt/sdcard/AppTestReport/";
    public static boolean isReport = true;
    public String testInfo;

    private ListView dirList; //存放遍历的目录
    ArrayAdapter<String> adapter;

    public static String no_dir_message = "报告目录不存在";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_report);

        //获取list view 并添加内容
        dirList = (ListView) findViewById(R.id.listViewTestReport);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, getData());
        dirList.setAdapter(adapter);

        //设置list view 在每一个item上的点击事件
        dirList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String dir = REPORT_DIR_PATH + adapter.getItem(position);
                Intent intent = new Intent();
                intent.putExtra("path", dir);
                intent.setClass(getApplicationContext(), TestCaseActivity.class);
                if (dir.contains(no_dir_message)) {

                } else {
                    startActivity(intent);
                }
            }
        });

    }

    /**
     * 生成测试报告
     *
     * @param view
     */
    public void toSaveReport(View view) {

    }

    /**
     * 创建测试报告
     *
     * @param reportFile，报告文件的名字
     */
    public String createReport(String reportFile) {
        //每个文件夹一行结果
        List<String> datas = getData();
        if (datas.get(0).contains(no_dir_message)) {
            return "未检测到测试报告";
        }
        String csvResult = "";
        if (reportFile.equals("")) {
            csvResult = REPORT_DIR_PATH + "Report_" + getCurrentSysTime() + ".csv";
        } else {
            csvResult = REPORT_DIR_PATH + "Report_" + reportFile + ".csv";
            File out = new File(csvResult);
            if (out.exists()) {
                out.delete();
            }
        }

        File csvFile = new File(csvResult);
        saveFile("Task,Tests,Failures,Successful", csvFile);

        for (String s : datas) {
            String dir = REPORT_DIR_PATH + s;
            File file = new File(dir);

            File[] caseFile = file.listFiles();
            int caseNum = 0;
            int failNum = 0;

            String csvFileChild = dir + File.separator + "Report_" + file.getName() + ".csv";
            File csvFileCh = new File(csvFileChild);
            for (int i = 0; i < caseFile.length; i++) {
                if (!caseFile[i].getName().contains("txt")) {
                    continue;
                }
                //保存结果到csv文件中
                String result = readFile(caseFile[i]);
                saveFile(caseFile[i].getName().replaceAll(".txt", "txt") + "," + result + "," + testInfo, csvFileCh);
                caseNum++;
                if(!result.contains("Success")){
                    failNum++;
                }
            }
            double passingRate=(double)(caseNum-failNum)/caseNum*100;
            String result=String.format("%s,%s,%s,%s",s,caseNum,failNum,(int)passingRate+"%");
            saveFile(result + ",", csvFile);
        }
        return csvResult;
    }


    /**
     * 读取测试目录
     *
     * @return
     */
    public static List<String> getData() {
        List<String> dirList = new ArrayList<>();
        File file = new File(REPORT_DIR_PATH);
        if (!file.exists()) {
            dirList.add(no_dir_message);
            return dirList; //直接退出方法
        }

        File[] dir = file.listFiles();
        for (int i = 0; i < dir.length; i++) {
            if (dir[i].isDirectory()) {
                dirList.add(dir[i].getName());
            }
        }
        return dirList;
    }

    /**
     * 保存文件
     *
     * @param line
     * @param file
     */
    public static void saveFile(String line, File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BufferedWriter bw = null;
        try {
            FileOutputStream fo = new FileOutputStream(file, true);
            OutputStreamWriter ow = new OutputStreamWriter(fo);
            bw = new BufferedWriter(ow);
            bw.append(line);
            bw.newLine();
            bw.flush();
            bw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前系统时间
     *
     * @return
     */
    public static String getCurrentSysTime() {
        SimpleDateFormat formattime1 = new SimpleDateFormat("yyyyMMdd_HHmmss");
        long ctime = System.currentTimeMillis();
        String currenttime = formattime1.format(new Date(ctime));
        return currenttime;
    }

    public String readFile(File file) {
        testInfo = "";
        String result = "Success";
        try {
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file));
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    if (lineTxt.contains("Failure in ")) {
                        result = "Failure";
                    } else if (lineTxt.contains("Error in ")) {
                        result = "Error";
                    }
                    testInfo = testInfo + " " + lineTxt;

                }
                read.close();
                bufferedReader.close();
            } else {
                System.out.println("The file no exist!");
            }
        } catch (Exception e) {
            System.out.println("Reader file fail!");
            throw new RuntimeException(e);
        }
        return result;
    }
}
