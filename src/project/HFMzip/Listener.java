package project.HFMzip;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.print.PrinterException;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Listener extends MouseAdapter implements ActionListener{
	String dir_select = "";
	JLabel label;
	private  Integer currentProgress;
	JProgressBar progressBar;
	
	public void setProgressBar(JProgressBar progressBar) {
		this.progressBar = progressBar;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String btnstr = e.getActionCommand();
		System.out.println(btnstr + "按钮被点击了");
		switch (btnstr) {
		case "BROWSE":
			openFile();
			break;
		case "ZIP":
			zip();
			break;
		case "UNZIP":
			unzip();
		default:
			
			break;
		}
	}
	
	
	
	private void unzip() {
		currentProgress = 0;
		progressBar.setValue(currentProgress);
		HFM_UNZIP hfmunzip = new HFM_UNZIP(dir_select,progressBar);
		hfmunzip.readFile();
		hfmunzip.wirteFile();
		JOptionPane.showMessageDialog(new JFrame(),"UNZIP Complete!");
		currentProgress = 0;
		progressBar.setValue(currentProgress);
	}

	private void zip() {
		currentProgress = 20;
		progressBar.setValue(currentProgress);
		HFM_ZIP hfmzip = new HFM_ZIP(dir_select);
		
		hfmzip.readFile();
		currentProgress = 40;
		progressBar.setValue(currentProgress);
		
		hfmzip.generateHFMTree();
		currentProgress = 60;
		progressBar.setValue(currentProgress);
		
		hfmzip.write();
		currentProgress = 100;
		progressBar.setValue(currentProgress);
		
		JOptionPane.showMessageDialog(new JFrame(),"ZIP Complete!");
		currentProgress = 0;
		progressBar.setValue(currentProgress);
		
		
	}



	public void getLabel(JLabel label) {
		this.label = label;
	}
	
	public void getcurrentProgress(Integer currentProgress) {
		this.currentProgress = currentProgress;
	}
	
	
	
	
	public void openFile() {
		JFileChooser jfc=new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
		jfc.showDialog(new JLabel(), "选择");
		File file=jfc.getSelectedFile();
		if(file!=null && file.isFile()) {
			JOptionPane.showMessageDialog(new JFrame(),"File Selected!");
			dir_select = file.getAbsolutePath();
			label.setText(file.getName());
		}else {
			JOptionPane.showMessageDialog(new JFrame(),"Please select a file!");
		}

	}

}
