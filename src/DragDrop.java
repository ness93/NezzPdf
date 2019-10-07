import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;

import javax.swing.*;

public class DragDrop{
	
	 static JTextField mergelink;
	 static JTextField splittlink;
	 static JTextField remove_pages;
	 static String dropfield = "Drag n drop file";
	 static String[] Files;
	 static JTextArea droparea1;
	 static JTextArea droparea2;
	 static JTextArea droparea_splitt;
	 static JLabel feedback_m = new JLabel("");
	 static JLabel feedback_s = new JLabel("");
	 static void createAndShowGUI() {
		 
        // Create and set up the window.
        final JFrame frame = new JFrame("PDF-MERGE_NEZZ");
 
        // Display the window.
        frame.getContentPane().setLayout(new GridLayout(1, 1));
 
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
 
        tabbedPane.addTab("Merge PDF", mergePanel());
        tabbedPane.addTab("Splitt PDF", splittPanel());
 
        frame.getContentPane().add(tabbedPane);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setSize(700, 350);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
		//####################################################################
	 //Merge panel
		//####################################################################
	private static JPanel mergePanel() {
		
	    ActionListener filelistener = new ActionListener() {
	        public void actionPerformed(ActionEvent ae) {
	        	JFileChooser chooser = new JFileChooser();
	            chooser.setCurrentDirectory(new java.io.File("."));
	            chooser.setDialogTitle("choosertitle");
	            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	            chooser.setAcceptAllFileFilterUsed(false);

	            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) 
	            	mergelink.setText(chooser.getSelectedFile().toString());
	        }
	    
	      };
	      
	     ActionListener merge = new ActionListener() {
		        public void actionPerformed(ActionEvent ae) {
		        	try {
		        		if (droparea1.getText().toString().equalsIgnoreCase(dropfield+"1")) {
		        			feedback_m.setText("Missing file");
		        			}
		        		else if (droparea2.getText().toString().equalsIgnoreCase(dropfield+"2")) {
		        			feedback_m.setText("Missing file");
		        			}
		        		else if(mergelink.getText().toString().equalsIgnoreCase("Filepath")){
		        			feedback_m.setText("Missing filepath");	
		        			}
		        		
		        		else {
		        			
		        			mergedocument();
		        		}
		        		
		        		
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						feedback_m.setText("Failed to merge pages");
						e.printStackTrace();
					}
		        } 	
		      };
		
		
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
       	constraints.anchor = GridBagConstraints.WEST;
       	constraints.insets = new Insets(10, 10, 10, 10);
       	
     // label file 1
        constraints.gridx = 0;
        constraints.gridy = 0;     
        p.add(new JLabel("File1:"), constraints);
        
        //Drag n drop 1
        constraints.gridx = 1;
        droparea1 = new JTextArea(dropfield+"1",3,40); 
        droparea1.setBackground(Color.WHITE);
        p.add(droparea1, constraints);
         
        // label file 2
        constraints.gridx = 0;
        constraints.gridy = 1;     
        p.add(new JLabel("File2:"), constraints);
        
        //Drag n drop 2
        constraints.gridx = 1;
        droparea2 = new JTextArea(dropfield+"2",3,40); 
        droparea2.setBackground(Color.WHITE);
        p.add(droparea2, constraints);
        
        //label destination
        constraints.gridx = 0;
        constraints.gridy = 2;     
        p.add(new JLabel("Destination:"), constraints);
        
        //Textfield for filepath
        constraints.gridx = 1;
        mergelink = new JTextField("Filepath",40);
        mergelink.setBackground(Color.WHITE);
        p.add(mergelink, constraints);
        
        //Get filepath
        constraints.gridx = 4;
        JButton btnGetFile = new JButton("Get filepath");
        btnGetFile.addActionListener(filelistener);
        p.add(btnGetFile, constraints);
        
        //Merge button
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        JButton buttonmerge = new JButton("Merge");
        buttonmerge.addActionListener(merge);
        p.add(buttonmerge, constraints);
        
        //Feedback
        constraints.gridy = 4;
        p.add(feedback_m, constraints);
        
        dropfield(droparea1);
        dropfield(droparea2);
        
        return p;
    }
	
	//####################################################################
	//Splitt panel
	//####################################################################
	private static JPanel splittPanel() {
		
	    ActionListener filelistener2 = new ActionListener() {
	        public void actionPerformed(ActionEvent ae) {
	        	JFileChooser chooser = new JFileChooser();
	            chooser.setCurrentDirectory(new java.io.File("."));
	            chooser.setDialogTitle("choosertitle");
	            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	            chooser.setAcceptAllFileFilterUsed(false);

	            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) 
	            	splittlink.setText(chooser.getSelectedFile().toString());
	        }
	    
	      };
	      
	     ActionListener splitt = new ActionListener() {
		        public void actionPerformed(ActionEvent ae) {
		        	try {
		        		if (droparea_splitt.getText().toString().equalsIgnoreCase(dropfield)) {
		        			feedback_s.setText("Missing file");
		        			}
		        		else if(splittlink.getText().toString().equalsIgnoreCase("Filepath")){
		        			feedback_s.setText("Missing filepath");	
		        			}
		        		else if(parsePages().size() == 0){
		        			feedback_s.setText("No pages defined");	
		        			}
		        		
		        		else {
		        			
		        			splittdocument();
		        		}
		        			
		        			
					} catch (IOException e) {
						// TODO Auto-generated catch block
						feedback_s.setText("Failed to split pages");	
						e.printStackTrace();
					}
		        } 	
		      };
		
		JPanel p2 = new JPanel(new GridBagLayout());
		p2.setLocation(0, 0);
        GridBagConstraints constraints2 = new GridBagConstraints();
        constraints2.anchor = GridBagConstraints.WEST;
        constraints2.insets = new Insets(10, 10, 10, 10);
       	
     // label file 1
        constraints2.gridx = 0;
        constraints2.gridy = 0;     
        p2.add(new JLabel("File:"), constraints2);
        
        //Drag n drop 1
        constraints2.gridx = 1;
        droparea_splitt = new JTextArea(dropfield,3,50); 
        droparea_splitt.setBackground(Color.WHITE);
        droparea_splitt.setEditable(false);
        p2.add(droparea_splitt, constraints2);
         
        // label file 2
        constraints2.gridx = 0;
        constraints2.gridy = 1;     
        p2.add(new JLabel("Pages to remove"), constraints2);
        
        // Text field for pages to remove
        constraints2.gridx = 1;   
        remove_pages = new JTextField("Enter pages 1-3, 5",40);
        Dimension minimumSize =Toolkit.getDefaultToolkit().getScreenSize();;
        minimumSize.width = 120;
        minimumSize.height = 20;
		remove_pages.setMinimumSize(minimumSize);
        remove_pages.setBackground(Color.WHITE);
        remove_pages.addFocusListener(new java.awt.event.FocusAdapter() {
        	void reSet (FocusEvent evt) {
                String temp = remove_pages.getText();
                remove_pages.setText(temp.equals("Enter pages 1-3, 5")? "" : temp);
            }
        	
            public void focusGained(java.awt.event.FocusEvent evt) {
                reSet(evt);
            }
        });
        
        p2.add(remove_pages, constraints2);
        
        //label destination
        constraints2.gridx = 0;
        constraints2.gridy = 2;     
        p2.add(new JLabel("Destination:"), constraints2);
        
        //Textfield for filepath
        constraints2.gridy = 2; 
        constraints2.gridx = 1;
        splittlink = new JTextField("Filepath",40);
        splittlink.setMinimumSize(minimumSize);
        splittlink.setBackground(Color.WHITE);
        splittlink.setEditable(false);
        p2.add(splittlink, constraints2);
        
        //Get filepath
        constraints2.gridx = 2;
        JButton btnGetFile = new JButton("Get filepath");
        btnGetFile.addActionListener(filelistener2);
        p2.add(btnGetFile, constraints2);
        
        //Splitt button
        constraints2.gridx = 0;
        constraints2.gridy = 4;
        constraints2.gridwidth = 2;
        constraints2.anchor = GridBagConstraints.CENTER;
        JButton btn_splitt = new JButton("Splitt");
        btn_splitt.addActionListener(splitt);
        p2.add(btn_splitt, constraints2);
        
        //Feedback
        constraints2.gridy = 5;
        p2.add(feedback_s, constraints2);
        
        dropfield(droparea_splitt);
        
        return p2;
    }
	//####################################################################
	//Merge the pdf
	//####################################################################
	private static void mergedocument() throws IOException {
		
		
	      File impfile = new File(droparea1.getText());
	      PDDocument doc1 = PDDocument.load(impfile);
	       
	      File impfile2 = new File(droparea2.getText());
	      PDDocument doc2 = PDDocument.load(impfile2);
	         
	      //Instantiating PDFMergerUtility class
	      PDFMergerUtility PDFmerger = new PDFMergerUtility();

	      //Setting the destination file
	      PDFmerger.setDestinationFileName(mergelink.getText()+"/merged.pdf");

	      //adding the source files
	      PDFmerger.addSource(impfile);
	      PDFmerger.addSource(impfile2);

	      //Merging the two documents
	      PDFmerger.mergeDocuments();

	      System.out.println("Documents merged");
	      //Closing the documents
	      doc1.close();
	      doc2.close();
	        
	}
	//####################################################################
	//Parse pages to be removed
	//####################################################################
	private static List<Integer> parsePages() {
		
		String input = remove_pages.getText();
		String[] parts = input.split(",");
		List<Integer> list = new ArrayList<>();
		try {
			for (String part : parts) {
			    if (part.contains("-")) {
			        String[] range = part.split("-");
			        int start = Integer.parseInt(range[0]);
			        int end = Integer.parseInt(range[1]);
	
			        for (int i=start; i <= end; ++i) {
			            list.add(i);
			        }
			    }
			    else {
			        int value = Integer.parseInt(part);
			        list.add(value);
			    }
			}
		}
		catch (Exception ex) {
            
        }
	    return list;    
	}
	//####################################################################
	//Removes pages from pdf
	//####################################################################
	private static void splittdocument() throws IOException {
		
		
	      File splittfile = new File(droparea_splitt.getText());
	      PDDocument splittdoc = PDDocument.load(splittfile);
	      System.out.println(splittdoc.getNumberOfPages());
	      List<Integer> pages = parsePages();
	      
	      for(int i : pages) {
	    	  System.out.println("page: " + i);
	    	  splittdoc.removePage(i-1);
	      }
	      //Setting the destination file
	      splittdoc.save(splittlink.getText()+"/splitt.pdf");
	      
	      System.out.println("Documents Splitted");
	      //Closing the documents
	      splittdoc.close();
	        
	}
	//####################################################################
	//Drop field for drag n drop
	//####################################################################
	private static void dropfield(JTextArea ta) {
        ta.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    List<File> droppedFiles = (List<File>) evt
                            .getTransferable().getTransferData(
                                    DataFlavor.javaFileListFlavor);
                    for (File file : droppedFiles) {
                    	
                    	ta.setText(file.getAbsolutePath());
                    }
                } catch (Exception ex) {
                	feedback_s.setText("No pages defined");
                }
            }
        });
    }

	
	 public static void main(String[] args) 
	    { 
		 createAndShowGUI();
	    }
	 
}
