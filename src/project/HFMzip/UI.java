package project.HFMzip;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


import java.awt.Color;
import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;


public class UI extends JFrame{
	
    private  final int MIN_PROGRESS = 0;
    private  final int MAX_PROGRESS = 100;

    private  Integer currentProgress = MIN_PROGRESS;
     JProgressBar progressBar;

	Listener listener = new Listener();
	
	public UI() {
		setTitle("HFM ZIP");
		setSize(600,350);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(null);
		
		JButton button1 = new JButton("ZIP");
		button1.setBounds(140, 250, 85, 35);
		panel.add(button1);
		
		JButton button2 = new JButton("UNZIP");
		button2.setBounds(280, 250, 85, 35);
		panel.add(button2);
		
		JButton button3 = new JButton("BROWSE");
		button3.setBounds(420, 110, 95, 35);
		
		JLabel label = new JLabel("You can drag and drop files here to add them.", JLabel.CENTER); 
		label.setBounds(50, 50, 350, 150);
		Font fnt = new Font("Serief", Font.ITALIC + Font.BOLD, 16);
		label.setFont(fnt);  
		label.setOpaque(true);
		//		label.setBorder(BorderFactory.createLineBorder(Color.red));
		label.setBorder(BorderFactory.createLoweredBevelBorder());
        progressBar = new JProgressBar();
        progressBar.setMinimum(MIN_PROGRESS);
        progressBar.setMaximum(MAX_PROGRESS);

        progressBar.setValue(currentProgress);
        progressBar.setStringPainted(true);
        progressBar.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
//                System.out.println("当前进度值: " + progressBar.getValue() + "; " +
//                        "进度百分比: " + progressBar.getPercentComplete());
            }
        });
        progressBar.setBounds(150, 220, 100, 20);
       
		JLabel progress = new JLabel("Progress", JLabel.CENTER); 
		progress.setBounds(60, 193, 80, 70);
		Font fnt2 = new Font(null, Font.BOLD, 16);
		progress.setFont(fnt2);  

		 
        panel.add(progressBar);
		panel.add(button1);
		panel.add(button2);
		panel.add(button3);
		panel.add(label);
		panel.add(progress);
		add(panel);
		
		listener.getLabel(label);
		listener.getcurrentProgress(currentProgress);
		listener.setProgressBar(progressBar);
		button1.addActionListener(listener);
		button2.addActionListener(listener);
		button3.addActionListener(listener);
		drag(panel, label);
		setVisible(true);	
	}
	
    public void drag(JPanel panel, JLabel label)
    {
        new DropTarget(panel, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetAdapter()
        {
            @Override
            public void drop(DropTargetDropEvent dtde)
            {
                try
                {
                    if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
                    {
                        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                        @SuppressWarnings("unchecked")
						List<File> list =  (List<File>) (dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
                        File file = list.get(list.size()-1);
                        listener.dir_select = file.getAbsolutePath();
                        label.setText(file.getName());
                        dtde.dropComplete(true);
                    }
                    else
                    {
                        dtde.rejectDrop();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
	
	

	public static void main(String[] args) {
		new UI();
	}

}
