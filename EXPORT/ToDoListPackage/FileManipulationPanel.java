package ToDoListPackage;

/**
 * Insert the type's description here.
 * Creation date: (4/16/2001 5:08:15 PM)
 * @author: Administrator
 */
public class FileManipulationPanel extends java.awt.Panel {
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private java.awt.Label ivjFileName = null;
	private java.awt.Button ivjLoadButton = null;
	private com.ibm.xml.wiringhelpers.ExtendedFileDialog ivjLoadFileDialog = null;
	private com.ibm.filemanagement.ReadFile ivjReadFile = null;
	private java.awt.Button ivjSaveButton = null;
	private com.ibm.xml.wiringhelpers.ExtendedFileDialog ivjSaveFileDialog = null;
	private com.ibm.filemanagement.WriteFile ivjWriteFile = null;
	protected transient ToDoListPackage.FileManipulationPanelListener fieldFileManipulationPanelListenerEventMulticaster = null;

class IvjEventHandler implements com.ibm.xml.wiringhelpers.event.FileSelectionListener, java.awt.event.ActionListener, java.beans.PropertyChangeListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == FileManipulationPanel.this.getLoadButton()) 
				connEtoM1(e);
			if (e.getSource() == FileManipulationPanel.this.getSaveButton()) 
				connEtoM3(e);
		};
		public void fileSelected(com.ibm.xml.wiringhelpers.event.FileSelectionEvent arg1) {
			if (arg1.getSource() == FileManipulationPanel.this.getLoadFileDialog()) 
				connEtoC2(arg1);
			if (arg1.getSource() == FileManipulationPanel.this.getSaveFileDialog()) 
				connEtoC3(arg1);
		};
		public void fileSelectionCancelled(com.ibm.xml.wiringhelpers.event.FileSelectionEvent arg1) {};
		public void propertyChange(java.beans.PropertyChangeEvent evt) {
			if (evt.getSource() == FileManipulationPanel.this.getLoadFileDialog() && (evt.getPropertyName().equals("fullpath"))) 
				connPtoP1SetTarget();
			if (evt.getSource() == FileManipulationPanel.this.getLoadFileDialog() && (evt.getPropertyName().equals("fullpath"))) 
				connPtoP2SetTarget();
			if (evt.getSource() == FileManipulationPanel.this.getSaveFileDialog() && (evt.getPropertyName().equals("fullpath"))) 
				connPtoP3SetTarget();
			if (evt.getSource() == FileManipulationPanel.this.getSaveFileDialog() && (evt.getPropertyName().equals("fullpath"))) 
				connPtoP4SetTarget();
			if (evt.getSource() == FileManipulationPanel.this.getReadFile() && (evt.getPropertyName().equals("currentStringResult"))) 
				connEtoC1(evt);
		};
	};
/**
 * FileManipulationPanel constructor comment.
 */
public FileManipulationPanel() {
	super();
	initialize();
}
/**
 * FileManipulationPanel constructor comment.
 * @param layout java.awt.LayoutManager
 */
public FileManipulationPanel(java.awt.LayoutManager layout) {
	super(layout);
}
/**
 * 
 * @param newListener ToDoListPackage.FileManipulationPanelListener
 */
public void addFileManipulationPanelListener(ToDoListPackage.FileManipulationPanelListener newListener) {
	fieldFileManipulationPanelListenerEventMulticaster = ToDoListPackage.FileManipulationPanelListenerEventMulticaster.add(fieldFileManipulationPanelListenerEventMulticaster, newListener);
	return;
}
/**
 * connEtoC1:  (ReadFile.currentStringResult --> FileManipulationPanel.firePropertyChange(Ljava.lang.String;Ljava.lang.Object;Ljava.lang.Object;)V)
 * @param arg1 java.beans.PropertyChangeEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1(java.beans.PropertyChangeEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.firePropertyChange("readFileCurrentStringResult", arg1.getOldValue(), arg1.getNewValue());
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC2:  (LoadFileDialog.fileSelection.fileSelected(com.ibm.xml.wiringhelpers.event.FileSelectionEvent) --> FileManipulationPanel.fireLoadFileDialogFileSelection_fileSelected(Ljava.util.EventObject;)V)
 * @param arg1 com.ibm.xml.wiringhelpers.event.FileSelectionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC2(com.ibm.xml.wiringhelpers.event.FileSelectionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.fireLoadFileDialogFileSelection_fileSelected(new java.util.EventObject(this));
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC3:  (SaveFileDialog.fileSelection.fileSelected(com.ibm.xml.wiringhelpers.event.FileSelectionEvent) --> FileManipulationPanel.fireSaveFileDialogFileSelection_fileSelected(Ljava.util.EventObject;)V)
 * @param arg1 com.ibm.xml.wiringhelpers.event.FileSelectionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC3(com.ibm.xml.wiringhelpers.event.FileSelectionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.fireSaveFileDialogFileSelection_fileSelected(new java.util.EventObject(this));
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM1:  (LoadButton.action.actionPerformed(java.awt.event.ActionEvent) --> LoadFileDialog.show()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM1(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		getLoadFileDialog().show();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM3:  (SaveButton.action.actionPerformed(java.awt.event.ActionEvent) --> SaveFileDialog.show()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM3(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		getSaveFileDialog().show();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connPtoP1SetTarget:  (LoadFileDialog.fullpath <--> ReadFile.inputFileName)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connPtoP1SetTarget() {
	/* Set the target from the source */
	try {
		getReadFile().setInputFileName(getLoadFileDialog().getFullpath());
		// user code begin {1}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connPtoP2SetTarget:  (LoadFileDialog.fullpath <--> FileName.text)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connPtoP2SetTarget() {
	/* Set the target from the source */
	try {
		getFileName().setText(getLoadFileDialog().getFullpath());
		// user code begin {1}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connPtoP3SetTarget:  (SaveFileDialog.fullpath <--> FileName.text)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connPtoP3SetTarget() {
	/* Set the target from the source */
	try {
		getFileName().setText(getSaveFileDialog().getFullpath());
		// user code begin {1}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connPtoP4SetTarget:  (SaveFileDialog.fullpath <--> WriteFile.inputFileName)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connPtoP4SetTarget() {
	/* Set the target from the source */
	try {
		getWriteFile().setInputFileName(getSaveFileDialog().getFullpath());
		// user code begin {1}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * Method to support listener events.
 * @param newEvent java.util.EventObject
 */
protected void fireLoadFileDialogFileSelection_fileSelected(java.util.EventObject newEvent) {
	if (fieldFileManipulationPanelListenerEventMulticaster == null) {
		return;
	};
	fieldFileManipulationPanelListenerEventMulticaster.loadFileDialogFileSelection_fileSelected(newEvent);
}
/**
 * Method to support listener events.
 * @param newEvent java.util.EventObject
 */
protected void fireSaveFileDialogFileSelection_fileSelected(java.util.EventObject newEvent) {
	if (fieldFileManipulationPanelListenerEventMulticaster == null) {
		return;
	};
	fieldFileManipulationPanelListenerEventMulticaster.saveFileDialogFileSelection_fileSelected(newEvent);
}
/**
 * Return the FileName property value.
 * @return java.awt.Label
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.Label getFileName() {
	if (ivjFileName == null) {
		try {
			ivjFileName = new java.awt.Label();
			ivjFileName.setName("FileName");
			ivjFileName.setAlignment(java.awt.Label.CENTER);
			ivjFileName.setText("");
			ivjFileName.setBackground(java.awt.Color.lightGray);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjFileName;
}
/**
 * Return the LoadButton property value.
 * @return java.awt.Button
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.Button getLoadButton() {
	if (ivjLoadButton == null) {
		try {
			ivjLoadButton = new java.awt.Button();
			ivjLoadButton.setName("LoadButton");
			ivjLoadButton.setLabel("Load");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjLoadButton;
}
/**
 * Return the LoadFileDialog property value.
 * @return com.ibm.xml.wiringhelpers.ExtendedFileDialog
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private com.ibm.xml.wiringhelpers.ExtendedFileDialog getLoadFileDialog() {
	if (ivjLoadFileDialog == null) {
		try {
			ivjLoadFileDialog = new com.ibm.xml.wiringhelpers.ExtendedFileDialog();
			ivjLoadFileDialog.setName("LoadFileDialog");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjLoadFileDialog;
}
/**
 * Return the ReadFile property value.
 * @return com.ibm.filemanagement.ReadFile
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private com.ibm.filemanagement.ReadFile getReadFile() {
	if (ivjReadFile == null) {
		try {
			ivjReadFile = new com.ibm.filemanagement.ReadFile();
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjReadFile;
}
/**
 * Method generated to support the promotion of the readFileCurrentStringResult attribute.
 * @return java.lang.String
 */
public java.lang.String getReadFileCurrentStringResult() {
	return getReadFile().getCurrentStringResult();
}
/**
 * Return the SaveButton property value.
 * @return java.awt.Button
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.Button getSaveButton() {
	if (ivjSaveButton == null) {
		try {
			ivjSaveButton = new java.awt.Button();
			ivjSaveButton.setName("SaveButton");
			ivjSaveButton.setLabel("Save");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSaveButton;
}
/**
 * Return the SaveFileDialog property value.
 * @return com.ibm.xml.wiringhelpers.ExtendedFileDialog
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private com.ibm.xml.wiringhelpers.ExtendedFileDialog getSaveFileDialog() {
	if (ivjSaveFileDialog == null) {
		try {
			ivjSaveFileDialog = new com.ibm.xml.wiringhelpers.ExtendedFileDialog();
			ivjSaveFileDialog.setName("SaveFileDialog");
			ivjSaveFileDialog.setMode(1);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSaveFileDialog;
}
/**
 * Return the WriteFile property value.
 * @return com.ibm.filemanagement.WriteFile
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private com.ibm.filemanagement.WriteFile getWriteFile() {
	if (ivjWriteFile == null) {
		try {
			ivjWriteFile = new com.ibm.filemanagement.WriteFile();
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjWriteFile;
}
/**
 * Method generated to support the promotion of the writeFileInputArray attribute.
 * @return java.lang.String[]
 */
public java.lang.String[] getWriteFileInputArray() {
	return getWriteFile().getInputArray();
}
/**
 * Called whenever the part throws an exception.
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* Uncomment the following lines to print uncaught exceptions to stdout */
	// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
	// exception.printStackTrace(System.out);
}
/**
 * Initializes connections
 * @exception java.lang.Exception The exception description.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initConnections() throws java.lang.Exception {
	// user code begin {1}
	// user code end
	getLoadButton().addActionListener(ivjEventHandler);
	getLoadFileDialog().addPropertyChangeListener(ivjEventHandler);
	getSaveButton().addActionListener(ivjEventHandler);
	getSaveFileDialog().addPropertyChangeListener(ivjEventHandler);
	getReadFile().addPropertyChangeListener(ivjEventHandler);
	getLoadFileDialog().addFileSelectionListener(ivjEventHandler);
	getSaveFileDialog().addFileSelectionListener(ivjEventHandler);
	connPtoP1SetTarget();
	connPtoP2SetTarget();
	connPtoP3SetTarget();
	connPtoP4SetTarget();
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("FileManipulationPanel");
		setLayout(new java.awt.GridBagLayout());
		setSize(426, 46);

		java.awt.GridBagConstraints constraintsLoadButton = new java.awt.GridBagConstraints();
		constraintsLoadButton.gridx = 0; constraintsLoadButton.gridy = 0;
		constraintsLoadButton.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getLoadButton(), constraintsLoadButton);

		java.awt.GridBagConstraints constraintsSaveButton = new java.awt.GridBagConstraints();
		constraintsSaveButton.gridx = 1; constraintsSaveButton.gridy = 0;
		constraintsSaveButton.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getSaveButton(), constraintsSaveButton);

		java.awt.GridBagConstraints constraintsFileName = new java.awt.GridBagConstraints();
		constraintsFileName.gridx = 3; constraintsFileName.gridy = 0;
		constraintsFileName.gridwidth = 2;
		constraintsFileName.fill = java.awt.GridBagConstraints.HORIZONTAL;
		constraintsFileName.weightx = 1.0;
		constraintsFileName.insets = new java.awt.Insets(3, 3, 3, 3);
		add(getFileName(), constraintsFileName);
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	// user code end
}
/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		java.awt.Frame frame = new java.awt.Frame();
		FileManipulationPanel aFileManipulationPanel;
		aFileManipulationPanel = new FileManipulationPanel();
		frame.add("Center", aFileManipulationPanel);
		frame.setSize(aFileManipulationPanel.getSize());
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		frame.show();
		java.awt.Insets insets = frame.getInsets();
		frame.setSize(frame.getWidth() + insets.left + insets.right, frame.getHeight() + insets.top + insets.bottom);
		frame.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of java.awt.Panel");
		exception.printStackTrace(System.out);
	}
}
/**
 * 
 */
public void readFileTriggerAction() {
		getReadFile().triggerAction();
}
/**
 * 
 * @param newListener ToDoListPackage.FileManipulationPanelListener
 */
public void removeFileManipulationPanelListener(ToDoListPackage.FileManipulationPanelListener newListener) {
	fieldFileManipulationPanelListenerEventMulticaster = ToDoListPackage.FileManipulationPanelListenerEventMulticaster.remove(fieldFileManipulationPanelListenerEventMulticaster, newListener);
	return;
}
/**
 * Method generated to support the promotion of the writeFileInputArray attribute.
 * @param arg1 java.lang.String[]
 */
public void setWriteFileInputArray(java.lang.String[] arg1) {
	getWriteFile().setInputArray(arg1);
}
/**
 * 
 */
public void writeFileTriggerAction() {
		getWriteFile().triggerAction();
}
}
