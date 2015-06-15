package xypipeline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class CmdDriver{
	CmdDriver(){
	}
	
	static void cmd(String[] cmd){
		try {
			int exitValue = 0;
			Process proc = Runtime.getRuntime().exec(cmd);
			proc.waitFor();
			exitValue = proc.exitValue();
			if(exitValue == 0){
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	static String[] cmd1(String[] cmd){
		String[] output = new String[2];
		output[0] = "";
		output[1] = "";
		try {
			int exitValue = 0;
			Process proc = Runtime.getRuntime().exec(cmd);
			proc.waitFor();
			exitValue = proc.exitValue();
			InputStream stdout = proc.getInputStream();
			InputStream stderr = proc.getErrorStream();
			BufferedReader brout = new BufferedReader(new InputStreamReader(stdout));
			BufferedReader brerr = new BufferedReader(new InputStreamReader(stderr));
			String line = null;
			while((line = brout.readLine()) != null)
				output[0] += line + "\n";
			while((line = brerr.readLine()) != null)
				output[1] += line + "\n";
			if(exitValue == 0){
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return output;
	}
	static String cmd2(String[] cmd){
		String output = "";
		try {
			int exitValue = 0;
			Process proc = Runtime.getRuntime().exec(cmd);
			proc.waitFor();
			exitValue = proc.exitValue();
			InputStream stdout = proc.getInputStream();
			BufferedReader brout = new BufferedReader(new InputStreamReader(stdout));
			String line = null;
			while((line = brout.readLine()) != null)
				output += line + "\n";
			if(exitValue == 0){
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return output;
	}
}