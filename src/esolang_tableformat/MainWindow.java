package esolang_tableformat;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MainWindow {

	private JFrame frame;
	private JTextField txt;
	private JFileChooser jfc;
	private File program;
	private JLabel lblFile;
	private JLabel lblSpreadsheet;
	private JComboBox<String> comboBox;
	private XSSFWorkbook workbook;
	private JButton btnStart;
	private JButton btnPause;
	private EsolangMachine machine;
	
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
		frame.setBounds(100, 100, 450, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
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
		
		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(machine==null){
					//start running
					machine=new EsolangMachine(workbook.getSheet((String)comboBox.getSelectedItem()),null,null);
				}else{
					//it was paused
				}
			}
		});
		btnStart.setEnabled(false);
		btnStart.setFont(new Font("Tahoma", Font.PLAIN, 30));
		btnStart.setBounds(10, 103, 130, 50);
		frame.getContentPane().add(btnStart);
		
		JButton btnStop = new JButton("Stop");
		btnStop.setEnabled(false);
		btnStop.setFont(new Font("Tahoma", Font.PLAIN, 30));
		btnStop.setBounds(294, 103, 130, 50);
		frame.getContentPane().add(btnStop);
		
		txt = new JTextField();
		txt.setEnabled(false);
		txt.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txt.setBounds(10, 164, 414, 25);
		frame.getContentPane().add(txt);
		txt.setColumns(10);
		
		JTextArea textArea = new JTextArea();
		textArea.setEnabled(false);
		textArea.setBounds(10, 200, 414, 150);
		frame.getContentPane().add(textArea);
		
		btnPause = new JButton("Pause");
		btnPause.setFont(new Font("Tahoma", Font.PLAIN, 30));
		btnPause.setEnabled(false);
		btnPause.setBounds(150, 103, 134, 50);
		frame.getContentPane().add(btnPause);
	}
}
