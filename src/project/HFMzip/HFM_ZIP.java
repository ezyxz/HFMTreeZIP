package project.HFMzip;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import project.HFMzip.HFMTree.HFMNode;

public class HFM_ZIP {
	private List<String> index2binary;
	private List<Byte> index2byte;
	private List<Integer> index2count;
	private byte[] data;
	
	final private String dir;
	public HFM_ZIP(String dir) {
		this.dir = dir;	
	}
	
	public void readFile() {
		  try {
			  java.io.FileInputStream fins=new java.io.FileInputStream(dir);
			  int len=fins.available();
			  System.out.println("Byte Amount | " + len);
			  data=new byte[len];
			  fins.read(data); 		  
		  }catch(Exception ef) {
				ef.printStackTrace();
		  }
			index2binary = new ArrayList<>();
			index2byte = new ArrayList<>();
			index2count = new ArrayList<>();
		  
			for (int i = 0; i < data.length; i++) {
				byte cCandidate = data[i];
				if(index2byte.contains(cCandidate )) {
					int index = index2byte.indexOf(cCandidate);
					int pre_count = index2count.get(index);
					index2count.set(index, ++pre_count);
				}else {
					index2byte.add(cCandidate);
					index2count.add(1);
				}
			}
	}
	
	public void generateHFMTree() {
		List<representation> count2index = new ArrayList<representation>();
		for (int i = 0; i < index2count.size(); i++) {
			count2index.add(new representation(i,index2count.get(i)));
		}
		// generate Hoffman tree
		HFMTree hfmTree = new HFMTree();
		HFMNode root = hfmTree.generateHFMTree(count2index);
		index2binary = Arrays.asList(new String[index2count.size()]);
		insertBinarygenerated(root, new StringBuffer());
	}
	
	public StringBuffer encoding() {
		StringBuffer sbfile_encoding = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			byte cCandidate = data[i];
			int index = index2byte.indexOf(cCandidate);
			sbfile_encoding.append(index2binary.get(index));
		}
		String sfile_encoding = sbfile_encoding.toString();
//		System.out.println("Hoffman encoding:");
//		System.out.println(sfile_encoding);
//		System.out.println("Hoffman capacity:");
//		System.out.println(sfile_encoding.length()/8);
		return sbfile_encoding;
	}
	
	public void write() {
		filewirte( encoding().toString());
	}
	
	private  void filewirte(String content) {
		List<Byte> table = new ArrayList<>();
//		System.out.println("Table");
//		System.out.println((byte) index2binary.size());
		table.add((byte) index2binary.size());
		
		for (int i = 0; i < index2binary.size(); i++) {
//			System.out.println(index2byte.get(i));
			table.add(index2byte.get(i));
			int rep_len = index2binary.get(i).length();
//			System.out.println((byte) rep_len);
			table.add((byte) rep_len);
			if(rep_len > 8) {
				
				String sBinary = index2binary.get(i);
				for (int j = 0; j < sBinary.length()/8; j++) {
					String sTemp = sBinary.substring(j*8, j*8+8);
//					System.out.println(sTemp);
					byte temp = (byte)Integer.parseInt(sTemp, 2);
					table.add(temp);
					}
				int residue = sBinary.length() % 8;
				if(residue != 0) {
					String sTemp = sBinary.substring(sBinary.length()-residue, sBinary.length());
					sTemp = fillZero(sTemp);
//					System.out.println(sTemp);
					byte temp = (byte)Integer.parseInt(sTemp, 2);
					table.add(temp);
				}
				
				
			}else {
				String sBinary = index2binary.get(i);
				sBinary = fillZero(sBinary);
//				System.out.println(sBinary);
				byte temp = (byte)Integer.parseInt(sBinary, 2);
				table.add(temp);
			}			
		}
		
		int length = table.size() + (int)Math.ceil((double)content.length() / 8) + 1;
		byte[] data = new byte[length];
		for (int i = 0; i < table.size(); i++) {
			data[i] = table.get(i);		
		}
		int index = 0;
		String sByte = null;
		int content_length = content.length();
		for (int i = table.size(); i < length-1; i++) {	
			if(index*8+8 > content_length) {
				sByte = (String) content.substring(index*8, content_length);
			}else {
				sByte = (String) content.substring(index*8, index*8+8);
			}
//			System.out.println(sByte);
			data[i] = (byte)Integer.parseInt(sByte, 2);
//			System.out.print(data[i] + " ");
			index++;
		}
		data[length-1] = (byte) (8-sByte.length());
		
		String[] tem = dir.split("\\.");
		String wdir = dir.replaceAll(tem[1], "hfmzip");
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
	
	
	
//	private void filewirte(String dir, String content) {
//		int table_length = 1 + index2binary.size() * 3;
//		int length = table_length + (int)Math.ceil((double)content.length() / 8) + 1; 
//		int conten_length = content.length();
//		byte[] data = new byte[length];
//		data[0] = (byte) index2binary.size();
//		int index = 0;
//		
//		for (int i = 1; i < table_length-2; i++) {
//
//			data[i] = index2byte.get(index);
//			data[i+1] = (byte) index2binary.get(index).length();
//			String sBinary = index2binary.get(index);
//			sBinary = fillZero(sBinary);
//			byte temp = (byte)Integer.parseInt(sBinary, 2);
//			data[i+2] = temp;
//			i += 2;
//			index++;
//			
//		}
//
//		
//		index = 0;
//		String sByte = null;
//		for (int i = table_length; i < length-1; i++) {	
//			if(index*8+8 > conten_length) sByte = (String) content.substring(index*8, conten_length);
//			else sByte = (String) content.substring(index*8, index*8+8);
//			data[i] = (byte)Integer.parseInt(sByte, 2);
////			System.out.print(data[i] + " ");
//			index++;
//		}
//		data[length-1] = (byte) (8-sByte.length());
//				
//		try {
//			OutputStream f = new FileOutputStream(dir);
//			f.write(data);
//			f.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	
	private  String fillZero(String str) {
		while(str.length()<8) {
			str = str.concat("0");
		}
		return str;
	}

	private void insertBinarygenerated(HFMNode root, StringBuffer buf) {
		StringBuffer new_buf_left = new StringBuffer(buf);
		StringBuffer new_buf_right = new StringBuffer(buf);

		if(root == null) return;
		if(root.oRepresentation.index != -1)
			index2binary.set(root.oRepresentation.index, new_buf_left.toString());
		insertBinarygenerated(root.left, new_buf_left.append('0'));
		insertBinarygenerated(root.right, new_buf_right.append('1'));
	}
	
	
	
	

}
