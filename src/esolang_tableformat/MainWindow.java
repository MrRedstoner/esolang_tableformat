package esolang_tableformat;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MainWindow {
	private JFrame frame;
	private JTextField txt;
	private JLabel lblFile;
	private JLabel lblSpreadsheet;
	private JComboBox<String> comboBox;
	private JButton btnStart;
	private JButton btnPause;
	private JButton btnConfirm;
	private JLabel label;
	private JButton btnDelete;
	private JTextArea textArea;
	private JButton btnStop;
	private JButton btnClear;
	private JToggleButton tglbtnDebug;
	
	private JFileChooser jfc;
	
	private File program;
	private XSSFWorkbook workbook;
	private EsolangMachine machine;
	private Input input;
	private Output output;
	private Thread runner;
	private boolean on=false;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("TableFormat by MrRedstoner");
		frame.setBounds(100, 100, 450, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		
		jfc=new JFileChooser();
		jfc.setFileFilter(new FileNameExtensionFilter("Excel files","xlsx"));
		jfc.setAcceptAllFileFilterUsed(false);
		
		JButton btnChooseFile = new JButton("choose file");
		btnChooseFile.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(jfc.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
						program=jfc.getSelectedFile();
						FileInputStream excelFile = new FileInputStream(program);
						workbook = new XSSFWorkbook(excelFile);
						
						lblFile.setText(program.getName());
						lblSpreadsheet.setEnabled(true);
						btnStart.setEnabled(true);
						comboBox.setEnabled(true);
						
						comboBox.removeAllItems();
						int limit=workbook.getNumberOfSheets();
						for(int i=0;i<limit;i++){
							comboBox.addItem(workbook.getSheetName(i));
						}
					}
				} catch (IOException e) {
					lblFile.setText("");
					lblSpreadsheet.setEnabled(false);
					btnStart.setEnabled(false);
					comboBox.setEnabled(false);
					e.printStackTrace();
				}
			}
		});
		btnChooseFile.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnChooseFile.setBounds(10, 11, 138, 35);
		frame.getContentPane().add(btnChooseFile);
		
		lblFile = new JLabel("");
		lblFile.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblFile.setBounds(158, 11, 266, 35);
		frame.getContentPane().add(lblFile);
		
		comboBox = new JComboBox<String>();
		comboBox.setEnabled(false);
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 20));
		comboBox.setBounds(158, 57, 266, 35);
		frame.getContentPane().add(comboBox);
		
		lblSpreadsheet = new JLabel("spreadsheet");
		lblSpreadsheet.setEnabled(false);
		lblSpreadsheet.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblSpreadsheet.setBounds(10, 57, 138, 35);
		frame.getContentPane().add(lblSpreadsheet);
		
		runner=new Thread(){
			public void run(){
				while(true){
					if (on) {
						try {
							boolean b = machine.step();
							if (b) {
								onStop();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else{
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		};
		runner.setDaemon(true);
		
		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(machine==null){
					//start running
					machine=new EsolangMachine(workbook.getSheet((String)comboBox.getSelectedItem()),input,output);
					machine.setDebug(tglbtnDebug.isSelected());
					
					btnStop.setEnabled(true);
					btnPause.setEnabled(true);
					btnStart.setEnabled(false);
					on=true;
					
					if(!runner.isAlive())runner.start();
				}else{
					//it was paused
					btnPause.setEnabled(true);
					on=true;
				}
			}
		});
		btnStart.setEnabled(false);
		btnStart.setFont(new Font("Tahoma", Font.PLAIN, 30));
		btnStart.setBounds(10, 103, 130, 50);
		frame.getContentPane().add(btnStart);
		
		btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onStop();
			}
		});
		btnStop.setEnabled(false);
		btnStop.setFont(new Font("Tahoma", Font.PLAIN, 30));
		btnStop.setBounds(294, 103, 130, 50);
		frame.getContentPane().add(btnStop);
		
		txt = new JTextField();
		txt.addKeyListener(new KeyAdapter() {
			@Override public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode()==KeyEvent.VK_ENTER){
					onConfirmed();
				}
			}
		});
		txt.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txt.setBounds(10, 164, 284, 25);
		frame.getContentPane().add(txt);
		txt.setColumns(50);
		
		input=new Input(){
			@Override public int readCharacter() {
				while(label.getText().length()<=0){
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				String s=label.getText();
				label.setText(s.substring(1));
				if(tglbtnDebug.isSelected())System.out.println("char read: "+s.charAt(0));
				return (int)s.charAt(0);
			}

			@Override public int readNumber() {
				boolean madeIt=false;
				int out=0;
				while(!madeIt){
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					try {
						if(label.getText().length()<=0)continue;
						String s0=label.getText();
						String[] s1=s0.split("\\s");
						int sublen;
						if(s1.length<=1){
							sublen=s1[0].length();
						}else{
							sublen=s1[0].length()+1;
						}
						label.setText(s0.substring(sublen));
						s0=s1[0];
						out=Integer.parseInt(s0);
						madeIt=true;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(tglbtnDebug.isSelected())System.out.println("num read: "+out);
				return out;
			}
		};
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(10, 236, 414, 153);
		textArea.setLineWrap(true);
		frame.getContentPane().add(textArea);
		
		output=new Output(){
			@Override public void writeCharacter(int c) {
				textArea.setText(textArea.getText()+((char)c));
			}

			@Override public void writeNumber(int i) {
				textArea.setText(textArea.getText()+i);
			}
		};
		
		btnPause = new JButton("Pause");
		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				on=false;
				btnStart.setEnabled(true);
			}
		});
		btnPause.setFont(new Font("Tahoma", Font.PLAIN, 30));
		btnPause.setEnabled(false);
		btnPause.setBounds(150, 103, 134, 50);
		frame.getContentPane().add(btnPause);
		
		btnConfirm = new JButton("Confirm");
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onConfirmed();
			}
		});
		btnConfirm.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnConfirm.setBounds(304, 164, 120, 25);
		frame.getContentPane().add(btnConfirm);
		
		label = new JLabel("");
		label.setFont(new Font("Tahoma", Font.PLAIN, 15));
		label.setBounds(10, 200, 284, 25);
		frame.getContentPane().add(label);
		
		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				label.setText("");
				btnDelete.setEnabled(false);
			}
		});
		btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnDelete.setEnabled(false);
		btnDelete.setBounds(304, 200, 120, 25);
		frame.getContentPane().add(btnDelete);
		
		btnClear = new JButton("Clear console");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
			}
		});
		btnClear.setFont(new Font("Tahoma", Font.PLAIN, 30));
		btnClear.setBounds(158, 400, 266, 50);
		frame.getContentPane().add(btnClear);
		
		tglbtnDebug = new JToggleButton("Debug");
		tglbtnDebug.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					machine.setDebug(tglbtnDebug.isSelected());
					System.out.println("debug: "+tglbtnDebug.isSelected());
				} catch (Exception e1) {}
			}
		});
		tglbtnDebug.setFont(new Font("Tahoma", Font.PLAIN, 30));
		tglbtnDebug.setBounds(10, 400, 138, 50);
		frame.getContentPane().add(tglbtnDebug);
	}

	private void onConfirmed() {
		if(label.getText().length()==0){
			if(txt.getText().length()>0){
				label.setText(txt.getText());
				txt.setText("");
				btnDelete.setEnabled(true);
			}
		}
	}
	
	private void onStop() {
		btnStop.setEnabled(false);
		btnPause.setEnabled(false);
		on=false;
		machine=null;
		btnStart.setEnabled(true);
	}
}
