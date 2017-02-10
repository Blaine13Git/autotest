package com.windsing.autotest.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ExecCmd {
	
	/**
	 * 执行cmd命令，且输出信息到控制台
	 */
	public void toExecCmd(String cmd) {

		try {
			// String fileName ="D:\\qb_mem.txt";//用于保存结果
			Process p = Runtime.getRuntime().exec(cmd);

			// 正确输出流
			InputStream input = p.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String line = "";
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				// saveToFile(line.trim(), fileName, false);
			}

			// 错误输出流
			InputStream errorInput = p.getErrorStream();
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorInput));
			String eline = "";
			while ((eline = errorReader.readLine()) != null) {
				System.out.println(eline);
				// saveToFile(eline, "D:\\runlog.log", false);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 保存执行结果 isClose = false ,持续写入 isClose = true ,写入最后一行数据
	 */
	public static void saveToFile(String text, String fileName, boolean isClose) {
		try {
			File file = new File(fileName);
			file.delete();
			file.createNewFile();

			BufferedWriter bf = null;
			FileOutputStream outputStream = new FileOutputStream(file, true);
			OutputStreamWriter outWriter = new OutputStreamWriter(outputStream);
			bf = new BufferedWriter(outWriter);
			bf.append(text);
			bf.newLine();
			bf.flush();

			if (isClose) {
				bf.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void writeToFile(String URI, String data) {
		try {

			File file = new File(URI);

			// 如果不存在，创建新的文件
			if (!file.exists()) {
				file.createNewFile();
			}

			// true = append file
			FileWriter fileWritter = new FileWriter(file.getAbsolutePath(), true);

			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			bufferWritter.write(data);
			bufferWritter.newLine();
			bufferWritter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
