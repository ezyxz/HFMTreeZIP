package project.HFMzip;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JProgressBar;

public class HFM_UNZIP {
	private List<String> index2binary;
	private List<Byte> index2byte;
	private List<Integer> index2count;
	private byte[] data;
	int BYTE_LEN;
	final String dir ;
	private StringBuffer binary_buf;
	private JProgressBar progressBar;
	private int currentProgress = 0;
	
	public HFM_UNZIP(String dir, JProgressBar progressBar) {
		this.dir = dir;	
		this.progressBar = progressBar;
	}
	
	public void readFile() {
		index2binary = new ArrayList<>();
		index2byte = new ArrayList<>();
		
		byte[] data = readFile2String(dir);
		currentProgress = 20;
		progressBar.setValue(currentProgress);
		binary_buf = new StringBuffer();
		translate(data);
	}
	
	public void wirteFile() {
		List<Byte> lData = new ArrayList<>();
		
		for (int i = 0; i < binary_buf.length(); i++) {
			int k = i+1;
			while(! index2binary.contains(binary_buf.substring(i, k))) k++;
			int index = index2binary.indexOf(binary_buf.substring(i, k));
			lData.add(index2byte.get(index));
			i=k-1;
//			 currentProgress += Math.ceil(30/binary_buf.length());
			currentProgress += 1;
			 progressBar.setValue(currentProgress);
		}
		byte[] data = new byte[lData.size()];
		for (int i = 0; i < data.length; i++) {
			data[i] = lData.get(i);
		}
		
		String[] tem = dir.split("\\.");
		String wdir = dir.replaceAll(tem[1], "hfmunzip");
		
		
		try {
			OutputStream f = new FileOutputStream(wdir);
			f.write(data);
			f.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	 private  void translate(byte[] data) {
		 
		 int index = 1;
		 int size = data[0];
		 if(size <= 0) size = 256 + size;
		 for (int i = 1; i <= size; i++) {
			 index2byte.add(data[index]);
			 index++;
			 int len = data[index];
			 index++;
			 if(len <= 8) {
				 String num = NomalizeBinary(data[index]);
				 index++;
				 index2binary.add(num.substring(0, len));
			 }else {
				 String full_biy = "";
					for (int j = 0; j < len/8; j++) {
						String num = NomalizeBinary(data[index]);
						index++;
						full_biy = full_biy.concat(num);
					}
					int residue = len % 8;
					if(residue != 0) {
						String num = NomalizeBinary(data[index]);
						index++;
						full_biy = full_biy.concat(num.substring(0,residue));

					}
					index2binary.add(full_biy);
			 }

		}
			currentProgress = 30;
			progressBar.setValue(currentProgress);
		 //到最后第二行
		 for (int i = index; i < BYTE_LEN-2; i++) {
			 String sBinary = NomalizeBinary(data[i]);
			 binary_buf.append(sBinary);
//			 System.out.println(sBinary);
//			 currentProgress += Math.ceil(40.0/(BYTE_LEN-index));
			 currentProgress += 1;
			 progressBar.setValue(currentProgress);
		}
		 int last_0 = data[BYTE_LEN-1];
		 String sBinary = NomalizeBinary(data[BYTE_LEN-2],last_0);
//		 System.out.println(sBinary);
		 binary_buf.append(sBinary);
	 }
	
	
//	 private  void translate(byte[] data) {
//		 
//		 for (int i = 1; i < data[0]*3; i++) {
//			 index2byte.add(data[i]);
//			 int len = data[i+1];
//			 String num = NomalizeBinary(data[i+2]);
//			 index2binary.add(num.substring(0, len));
//			 
////			 if(num.length() == 1) {
////				 String temp = "0";
////				 while(temp.length() < len) {
////					 temp = temp.concat("0");
////				 }
////				 index2binary.add(temp);
////			 }
////			 else index2binary.add(num.substring(24, 24+len));
//			 i+=2;
//			 }
//		 for (int i = data[0]*3+1; i < BYTE_LEN-2; i++) {
//			 String sBinary = NomalizeBinary(data[i]);
//			 binary_buf.append(sBinary);
//		}
//		 int last_0 = data[BYTE_LEN-1];
//		 String sBinary = NomalizeBinary(data[BYTE_LEN-2],last_0);
//		 binary_buf.append(sBinary);
//	 }
//	
	
	
	 private  byte[] readFile2String(String fName) {
		  byte[] data = null;
		  try {
			  java.io.FileInputStream fins=new java.io.FileInputStream(fName);
			  BYTE_LEN=fins.available();
			  System.out.println("Byte Amount | " + BYTE_LEN);
			  data=new byte[BYTE_LEN];
			  fins.read(data); 		  
		  }catch(Exception ef) {
				ef.printStackTrace();
		  }
		  return data;
	  }
	 
		private  String NomalizeBinary(byte b) {
			
			String str = Integer.toBinaryString(b);
			while(str.length() < 8) {
				str = "0".concat(str);
			}
			
			if(str.length() == 0) {
				return "00000000";
			}
			
			if(str.length() == 32) {
				return str.substring(24, 32);
			}
				
			return str;
			
		}
		
		private  String NomalizeBinary(byte b, int i) {
			String str = NomalizeBinary(b);
			return str.substring(0,8-i);
			
		}

}
