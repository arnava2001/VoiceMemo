import java.awt.*;
import java.io.*;
import javax.sound.sampled.*;
import javax.sound.sampled.Mixer.Info;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JComboBox;
import javax.swing.Box;

public class VoiceMemo {

	private JFrame frame;
	JavaSoundRecorder javaRec = new JavaSoundRecorder(); 
	public static void main(String[] args) {;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VoiceMemo window = new VoiceMemo();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public VoiceMemo() {
		initialize();
	}
	
	private void initialize() {
		
		File theDir = new File("C:\\JavaVoiceMemo");

		// if the directory does not exist, create it
		if (!theDir.exists()) 
		    theDir.mkdir(); 
		    
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setLocationRelativeTo(null);
	//	frame.setUndecorated(true);
		frame.setBounds(100, 100, 862, 471);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(138, 43, 226));
		panel_1.setBounds(0, 0, 91, 434);
		frame.getContentPane().add(panel_1);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 848, 434);
		panel.setBackground(Color.WHITE);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("VoiceMemo\r\n");
		lblNewLabel.setForeground(new Color(138, 43, 226));
		lblNewLabel.setFont(new Font("Impact", Font.BOLD, 30));
		lblNewLabel.setBackground(Color.WHITE);
		lblNewLabel.setBounds(99, 11, 177, 60);
		panel.add(lblNewLabel);
		JComboBox<String> comboBox = new JComboBox<String>();

		JButton btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MakeSound m = new MakeSound(); 
				String filName = comboBox.getSelectedItem().toString(); 
				m.playSound("C:\\JavaVoiceMemo\\"+filName+".wav");
			}
		});
		btnNewButton.setBackground(Color.WHITE);
		java.net.URL imgLogo = getClass().getResource("play.png");
		btnNewButton.setBorderPainted(false);
		btnNewButton.setFocusPainted(false);
		ImageIcon playIcon = new ImageIcon(imgLogo); 
		Image resizedPlay = getScaledImage(playIcon.getImage(), 100, 100); 
		btnNewButton.setIcon(new ImageIcon(resizedPlay)); 
		btnNewButton.setBounds(697, 295, 60, 60);
		panel.add(btnNewButton);
		
		JLabel lblPlayRecording = new JLabel("PLAY RECORDING\r\n");
		lblPlayRecording.setForeground(new Color(138, 43, 226));
		lblPlayRecording.setFont(new Font("Impact", Font.BOLD, 20));
		lblPlayRecording.setBackground(Color.WHITE);
		lblPlayRecording.setBounds(111, 295, 165, 60);
		panel.add(lblPlayRecording);
		
		JLabel lblNewRecording = new JLabel("NEW RECORDING\r\n");
		lblNewRecording.setForeground(new Color(138, 43, 226));
		lblNewRecording.setFont(new Font("Impact", Font.BOLD, 20));
		lblNewRecording.setBackground(Color.WHITE);
		lblNewRecording.setBounds(111, 182, 165, 60);
		panel.add(lblNewRecording);
		
		JTextArea txtrRecordingName = new JTextArea();
		txtrRecordingName.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if(txtrRecordingName.getText().equals("Recording Name"))
					txtrRecordingName.setText("");
			}
			@Override
			public void focusLost(FocusEvent e) {
				if(txtrRecordingName.getText().equals(""))
					txtrRecordingName.setText("Recording Name");
			}
		});
		txtrRecordingName.setWrapStyleWord(true);
		txtrRecordingName.setText("Recording Name");
		txtrRecordingName.setLineWrap(true);
		txtrRecordingName.setForeground(new Color(138, 43, 226));
		txtrRecordingName.setFont(new Font("Berlin Sans FB", Font.BOLD, 15));
		txtrRecordingName.setBackground(Color.WHITE);
		txtrRecordingName.setBounds(325, 204, 165, 40);
		panel.add(txtrRecordingName);
		
		JTextArea txtrLength = new JTextArea();
		txtrLength.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if(txtrLength.getText().equals("LENGTH - Seconds"))
				{
					txtrLength.setText("");
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if(txtrLength.getText().equals(""))
					txtrLength.setText("LENGTH - Seconds"); 
			}
		});
		txtrLength.setWrapStyleWord(true);
		txtrLength.setText("LENGTH - Seconds");
		txtrLength.setLineWrap(true);
		txtrLength.setForeground(new Color(138, 43, 226));
		txtrLength.setFont(new Font("Berlin Sans FB", Font.BOLD, 15));
		txtrLength.setBackground(Color.WHITE);
		txtrLength.setBounds(500, 204, 143, 40);
		panel.add(txtrLength);
		JLabel lblRecordInProgress = new JLabel("");
		
		JButton btnNewButton_1 = new JButton("");
		btnNewButton_1.addActionListener(new ActionListener() {
			long recordTime; 
			String fin = ""; 
			public void actionPerformed(ActionEvent e) {
				try {
					recordTime = Integer.parseInt(txtrLength.getText()) * 1000;
				}
				catch(Exception E)
				{
					lblRecordInProgress.setForeground(Color.RED);
					lblRecordInProgress.setText("Error: Length must be an integer");
				}
				
				if(txtrRecordingName.getText().equals("Recording Name") || txtrRecordingName.getText().equals(""))
				{
					lblRecordInProgress.setForeground(Color.RED);
					lblRecordInProgress.setText("Error: FileName cannot be blank");
				}
				if(!txtrLength.getText().toUpperCase().equals(txtrLength.getText()))
				{
					lblRecordInProgress.setText("Error: Recording length must be an integer");
				}
				else {
					String name = txtrRecordingName.getText(); 
					fin = ""; 
					String[] split = name.split(" "); 
					for(String s : split)
					{
						fin+=s;
					}			
					 final JavaSoundRecorder recorder = new JavaSoundRecorder();
					 recorder.label = lblRecordInProgress; 
					 recorder.wavFile = new File("C:\\JavaVoiceMemo\\"+fin+".wav");
	// creates a new thread that waits for a specified
				        // of time before stopping
				        Thread stopper = new Thread(new Runnable() {
				            public void run() {
				     try {
				                    Thread.sleep(recordTime);
				                } catch (InterruptedException ex) {
				                    ex.printStackTrace();
				                }
				                recorder.finish();
				            }
				        });
				 
				        stopper.start();
				 
				        // start recording
				        recorder.start();
				
				        comboBox.removeAllItems(); 
				        fillUp(comboBox); 
			}
			}
		});
		btnNewButton_1.setBackground(Color.WHITE);
		btnNewButton_1.setFocusPainted(false);
		btnNewButton_1.setBorderPainted(false);
		btnNewButton_1.setOpaque(true);
		java.net.URL imgLogo2 = getClass().getResource("microphone.png"); 
		ImageIcon recIcon = new ImageIcon(imgLogo2); 
		Image resizedRec = getScaledImage(recIcon.getImage(), 100, 100); 
		btnNewButton_1.setIcon(new ImageIcon(resizedRec));
		btnNewButton_1.setBounds(676, 148, 100, 100);
		panel.add(btnNewButton_1);
		
		comboBox.setMaximumRowCount(3);
		comboBox.setBackground(Color.WHITE);
		comboBox.setForeground(new Color(138, 43, 226));
		comboBox.setFont(new Font("Berlin Sans FB", Font.PLAIN, 19));
		fillUp(comboBox);
		comboBox.setOpaque(true);
		comboBox.setBounds(360, 295, 224, 60);
		panel.add(comboBox);
		
		lblRecordInProgress.setForeground(Color.GREEN);
		lblRecordInProgress.setFont(new Font("Impact", Font.BOLD, 20));
		lblRecordInProgress.setBackground(Color.WHITE);
		lblRecordInProgress.setBounds(99, 100, 711, 60);
		panel.add(lblRecordInProgress);
		
		JLabel lblHttpsgithubcomarnava = new JLabel("https://github.com/arnava2001");
		lblHttpsgithubcomarnava.setForeground(new Color(138, 43, 226));
		lblHttpsgithubcomarnava.setFont(new Font("Impact", Font.PLAIN, 15));
		lblHttpsgithubcomarnava.setBackground(Color.WHITE);
		lblHttpsgithubcomarnava.setBounds(604, 374, 244, 60);
		panel.add(lblHttpsgithubcomarnava);
	}
	
	private Image getScaledImage(Image srcImg, int w, int h){
	    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();

	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(srcImg, 0, 0, w, h, null);
	    g2.dispose();

	    return resizedImg;
	}
	
	private void fillUp(JComboBox<String> box) //populate the sound chooser with the previously saved user files
	{  
		File folder = new File("C:\\JavaVoiceMemo");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
		  if (listOfFiles[i].isFile()) {
			  box.addItem(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4));
		  } else if (listOfFiles[i].isDirectory()) {
		    System.out.println("Directory " + listOfFiles[i].getName());
		  }
		}
	}
}

/*
*/