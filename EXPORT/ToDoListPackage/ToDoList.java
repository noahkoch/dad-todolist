package ToDoListPackage;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
/**
 * Insert the type's description here.
 * Creation date: (4/14/2001 11:44:12 AM)
 * @author: Administrator
 */
public class ToDoList extends Applet implements com.ibm.wiringhelpers.events.ThisActionEventListener, com.ibm.wiringhelpers.events.TriggerNextActionEventListener, ActionListener, WindowListener, java.beans.PropertyChangeListener, FileManipulationPanelListener {
	private Button ivjButton1 = null;
	private com.ibm.uicontrols.BoundList ivjTaskList = null;
	private TextField ivjTaskTextField = null;
	private Button ivjClearButton = null;
	private com.ibm.uicontrols.BoundTextField ivjCountField = null;
	private Label ivjCountFieldLabel = null;
	private Button ivjDoneButton = null;
	private com.ibm.wiringhelpers.Iterator ivjFillList = null;
	private Label ivjLabel1 = null;
	private com.ibm.comparators.Less ivjLessThan = null;
	private com.ibm.sort.SortBean ivjSort = null;
	private com.ibm.uicontrols.BoundList ivjSortedList = null;
	private com.ibm.wiringhelpers.Step ivjClearList = null;
	private Button ivjExitButton = null;
	private com.ibm.uicontrols.QuitDialogBean ivjQuitDialogBean = null;
	private FileManipulationPanel ivjFileManipulationPanel = null;
/**
 * Method to handle events for the ActionListener interface.
 * @param e java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void actionPerformed(java.awt.event.ActionEvent e) {
	// user code begin {1}
	// user code end
	if (e.getSource() == getButton1()) 
		connEtoM1(e);
	if (e.getSource() == getButton1()) 
		connEtoM2(e);
	if (e.getSource() == getButton1()) 
		connEtoM3(e);
	if (e.getSource() == getClearButton()) 
		connEtoM4(e);
	if (e.getSource() == getDoneButton()) 
		connEtoM5(e);
	if (e.getSource() == getExitButton()) 
		connEtoM16(e);
	// user code begin {2}
	// user code end
}
/**
 * connEtoM1:  (Button1.action.actionPerformed(java.awt.event.ActionEvent) --> TaskList.add(Ljava.lang.String;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM1(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		getTaskList().add(getTaskTextField().getText());
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM10:  (FileManipulationPanel1.fileManipulationPanel.loadFileDialogFileSelection_fileSelected(java.util.EventObject) --> ClearList.triggerAction()V)
 * @param arg1 java.util.EventObject
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM10(java.util.EventObject arg1) {
	try {
		// user code begin {1}
		// user code end
		getClearList().triggerAction();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM11:  (ClearList.triggerNextActionEvent.triggerNextAction(com.ibm.wiringhelpers.events.TriggerNextActionEvent) --> FileManipulationPanel1.readFileTriggerAction()V)
 * @param arg1 com.ibm.wiringhelpers.events.TriggerNextActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM11(com.ibm.wiringhelpers.events.TriggerNextActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		getFileManipulationPanel().readFileTriggerAction();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM12:  (ClearList.thisActionEvent.triggerThisAction(com.ibm.wiringhelpers.events.ThisActionEvent) --> TaskList.removeAll()V)
 * @param arg1 com.ibm.wiringhelpers.events.ThisActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM12(com.ibm.wiringhelpers.events.ThisActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		getTaskList().removeAll();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM13:  (FileManipulationPanel1.readFileCurrentStringResult --> TaskList.add(Ljava.lang.String;)V)
 * @param arg1 java.beans.PropertyChangeEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM13(java.beans.PropertyChangeEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		getTaskList().add(String.valueOf(getFileManipulationPanel().getReadFileCurrentStringResult()));
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM14:  (FileManipulationPanel.fileManipulationPanel.saveFileDialogFileSelection_fileSelected(java.util.EventObject) --> FileManipulationPanel.writeFileTriggerAction()V)
 * @param arg1 java.util.EventObject
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM14(java.util.EventObject arg1) {
	try {
		// user code begin {1}
		// user code end
		getFileManipulationPanel().writeFileTriggerAction();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM16:  (ExitButton.action.actionPerformed(java.awt.event.ActionEvent) --> QuitDialogBean.showDialog(Ljava.awt.Component;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM16(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		getQuitDialogBean().showDialog(this);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM2:  (Button1.action.actionPerformed(java.awt.event.ActionEvent) --> TaskTextField.text)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM2(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		getTaskTextField().setText(" ");
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM3:  (Button1.action.actionPerformed(java.awt.event.ActionEvent) --> TaskTextField.requestFocus()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM3(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		getTaskTextField().requestFocus();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM4:  (ClearButton.action.actionPerformed(java.awt.event.ActionEvent) --> TaskList.removeAll()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM4(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		getTaskList().removeAll();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM5:  (DoneButton.action.actionPerformed(java.awt.event.ActionEvent) --> TaskList.remove(Ljava.lang.String;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM5(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		getTaskList().remove(getTaskList().getSelectedItem());
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM6:  (TaskList.items --> SortedList.removeAll()V)
 * @param arg1 java.beans.PropertyChangeEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM6(java.beans.PropertyChangeEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		getSortedList().removeAll();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM7:  (TaskList.items --> Sort.sortStringArray([Ljava.lang.String;)[Ljava.lang.String;)
 * @return java.lang.String[]
 * @param arg1 java.beans.PropertyChangeEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.lang.String[] connEtoM7(java.beans.PropertyChangeEvent arg1) {
	java.lang.String[] connEtoM7Result = null;
	try {
		// user code begin {1}
		// user code end
		connEtoM7Result = getSort().sortStringArray(getTaskList().getItems());
		connEtoM8(connEtoM7Result);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
	return connEtoM7Result;
}
/**
 * connEtoM8:  ( (TaskList,items --> Sort,sortStringArray([Ljava.lang.String;)[Ljava.lang.String;).normalResult --> FillList.inputStringArray)
 * @param result java.lang.String[]
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM8(java.lang.String[] result) {
	try {
		// user code begin {1}
		// user code end
		getFillList().setInputStringArray(result);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM9:  (FillList.currentStringResult --> SortedList.add(Ljava.lang.String;)V)
 * @param arg1 java.beans.PropertyChangeEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM9(java.beans.PropertyChangeEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		getSortedList().add(String.valueOf(getFillList().getCurrentStringResult()));
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connPtoP1SetTarget:  (TaskList.itemCount <--> LessThan.inputFirstArg)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connPtoP1SetTarget() {
	/* Set the target from the source */
	try {
		getLessThan().setInputFirstArg(getTaskList().getItemCount());
		// user code begin {1}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connPtoP2SetTarget:  (CountField.text <--> LessThan.inputSecondArg)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connPtoP2SetTarget() {
	/* Set the target from the source */
	try {
		getLessThan().setInputSecondArg(new Double(getCountField().getText()).doubleValue());
		// user code begin {1}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connPtoP3SetTarget:  (LessThan.result <--> Button1.enabled)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connPtoP3SetTarget() {
	/* Set the target from the source */
	try {
		getButton1().setEnabled(getLessThan().getResult());
		// user code begin {1}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connPtoP4SetTarget:  (TaskList.items <--> FileManipulationPanel1.writeFileInputArray)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connPtoP4SetTarget() {
	/* Set the target from the source */
	try {
		getFileManipulationPanel().setWriteFileInputArray(getTaskList().getItems());
		// user code begin {1}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * Returns information about this applet.
 * @return a string of information about this applet
 */
public String getAppletInfo() {
	return "ToDoList\n" + 
		"\n" + 
		"Insert the type's description here.\n" + 
		"Creation date: (4/14/2001 11:44:12 AM)\n" + 
		"@author: Administrator\n" + 
		"";
}
/**
 * Return the Button1 property value.
 * @return java.awt.Button
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.Button getButton1() {
	if (ivjButton1 == null) {
		try {
			ivjButton1 = new java.awt.Button();
			ivjButton1.setName("Button1");
			ivjButton1.setBounds(61, 20, 56, 23);
			ivjButton1.setLabel("Add");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjButton1;
}
/**
 * Return the ClearButton property value.
 * @return java.awt.Button
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.Button getClearButton() {
	if (ivjClearButton == null) {
		try {
			ivjClearButton = new java.awt.Button();
			ivjClearButton.setName("ClearButton");
			ivjClearButton.setBounds(69, 149, 56, 23);
			ivjClearButton.setLabel("Clear");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjClearButton;
}
/**
 * Return the ClearList property value.
 * @return com.ibm.wiringhelpers.Step
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private com.ibm.wiringhelpers.Step getClearList() {
	if (ivjClearList == null) {
		try {
			ivjClearList = new com.ibm.wiringhelpers.Step();
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjClearList;
}
/**
 * Return the CountField property value.
 * @return com.ibm.uicontrols.BoundTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private com.ibm.uicontrols.BoundTextField getCountField() {
	if (ivjCountField == null) {
		try {
			ivjCountField = new com.ibm.uicontrols.BoundTextField();
			ivjCountField.setName("CountField");
			ivjCountField.setText("10");
			ivjCountField.setBounds(66, 95, 59, 23);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCountField;
}
/**
 * Return the CountFieldLabel property value.
 * @return java.awt.Label
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.Label getCountFieldLabel() {
	if (ivjCountFieldLabel == null) {
		try {
			ivjCountFieldLabel = new java.awt.Label();
			ivjCountFieldLabel.setName("CountFieldLabel");
			ivjCountFieldLabel.setText("Max Entries");
			ivjCountFieldLabel.setBounds(62, 71, 66, 23);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCountFieldLabel;
}
/**
 * Return the DoneButton property value.
 * @return java.awt.Button
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.Button getDoneButton() {
	if (ivjDoneButton == null) {
		try {
			ivjDoneButton = new java.awt.Button();
			ivjDoneButton.setName("DoneButton");
			ivjDoneButton.setBounds(70, 187, 56, 23);
			ivjDoneButton.setLabel("Done");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDoneButton;
}
/**
 * Return the ExitButton property value.
 * @return java.awt.Button
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.Button getExitButton() {
	if (ivjExitButton == null) {
		try {
			ivjExitButton = new java.awt.Button();
			ivjExitButton.setName("ExitButton");
			ivjExitButton.setBounds(478, 244, 56, 23);
			ivjExitButton.setLabel("Exit");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjExitButton;
}
/**
 * Return the FileManipulationPanel1 property value.
 * @return ToDoListPackage.FileManipulationPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private FileManipulationPanel getFileManipulationPanel() {
	if (ivjFileManipulationPanel == null) {
		try {
			ivjFileManipulationPanel = new ToDoListPackage.FileManipulationPanel();
			ivjFileManipulationPanel.setName("FileManipulationPanel");
			ivjFileManipulationPanel.setBounds(16, 282, 426, 46);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjFileManipulationPanel;
}
/**
 * Return the FillList property value.
 * @return com.ibm.wiringhelpers.Iterator
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private com.ibm.wiringhelpers.Iterator getFillList() {
	if (ivjFillList == null) {
		try {
			ivjFillList = new com.ibm.wiringhelpers.Iterator();
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjFillList;
}
/**
 * Return the Label1 property value.
 * @return java.awt.Label
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.Label getLabel1() {
	if (ivjLabel1 == null) {
		try {
			ivjLabel1 = new java.awt.Label();
			ivjLabel1.setName("Label1");
			ivjLabel1.setText("Sorted");
			ivjLabel1.setBounds(395, 27, 44, 23);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjLabel1;
}
/**
 * Return the LessThan property value.
 * @return com.ibm.comparators.Less
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private com.ibm.comparators.Less getLessThan() {
	if (ivjLessThan == null) {
		try {
			ivjLessThan = new com.ibm.comparators.Less();
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjLessThan;
}
/**
 * Return the QuitDialogBean property value.
 * @return com.ibm.uicontrols.QuitDialogBean
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private com.ibm.uicontrols.QuitDialogBean getQuitDialogBean() {
	if (ivjQuitDialogBean == null) {
		try {
			ivjQuitDialogBean = new com.ibm.uicontrols.QuitDialogBean();
			ivjQuitDialogBean.setTitle("Quit dialog window");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjQuitDialogBean;
}
/**
 * Return the Sort property value.
 * @return com.ibm.sort.SortBean
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private com.ibm.sort.SortBean getSort() {
	if (ivjSort == null) {
		try {
			ivjSort = new com.ibm.sort.SortBean();
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSort;
}
/**
 * Return the SortedList property value.
 * @return com.ibm.uicontrols.BoundList
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private com.ibm.uicontrols.BoundList getSortedList() {
	if (ivjSortedList == null) {
		try {
			ivjSortedList = new com.ibm.uicontrols.BoundList();
			ivjSortedList.setName("SortedList");
			ivjSortedList.setBounds(388, 52, 138, 173);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSortedList;
}
/**
 * Return the TaskList property value.
 * @return com.ibm.uicontrols.BoundList
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private com.ibm.uicontrols.BoundList getTaskList() {
	if (ivjTaskList == null) {
		try {
			ivjTaskList = new com.ibm.uicontrols.BoundList();
			ivjTaskList.setName("TaskList");
			ivjTaskList.setBounds(201, 55, 142, 177);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjTaskList;
}
/**
 * Return the TaskTextField property value.
 * @return java.awt.TextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.TextField getTaskTextField() {
	if (ivjTaskTextField == null) {
		try {
			ivjTaskTextField = new java.awt.TextField();
			ivjTaskTextField.setName("TaskTextField");
			ivjTaskTextField.setBounds(199, 18, 140, 23);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjTaskTextField;
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
 * Initializes the applet.
 * 
 * @see #start
 * @see #stop
 * @see #destroy
 */
public void init() {
	try {
		super.init();
		setName("ToDoList");
		setLayout(null);
		setBackground(java.awt.Color.white);
		setSize(538, 344);
		add(getButton1(), getButton1().getName());
		add(getTaskTextField(), getTaskTextField().getName());
		add(getTaskList(), getTaskList().getName());
		add(getClearButton(), getClearButton().getName());
		add(getDoneButton(), getDoneButton().getName());
		add(getCountField(), getCountField().getName());
		add(getCountFieldLabel(), getCountFieldLabel().getName());
		add(getSortedList(), getSortedList().getName());
		add(getLabel1(), getLabel1().getName());
		add(getExitButton(), getExitButton().getName());
		add(getFileManipulationPanel(), getFileManipulationPanel().getName());
		initConnections();
		// user code begin {1}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * Initializes connections
 * @exception java.lang.Exception The exception description.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initConnections() throws java.lang.Exception {
	// user code begin {1}
	// user code end
	getButton1().addActionListener(this);
	getClearButton().addActionListener(this);
	getDoneButton().addActionListener(this);
	getTaskList().addPropertyChangeListener(this);
	getCountField().addPropertyChangeListener(this);
	getLessThan().addPropertyChangeListener(this);
	getFillList().addPropertyChangeListener(this);
	getClearList().addThisActionEventListener(this);
	getExitButton().addActionListener(this);
	getFileManipulationPanel().addFileManipulationPanelListener(this);
	getClearList().addTriggerNextActionEventListener(this);
	getFileManipulationPanel().addPropertyChangeListener(this);
	connPtoP1SetTarget();
	connPtoP2SetTarget();
	connPtoP3SetTarget();
	connPtoP4SetTarget();
}
/**
 * Method to handle events for the FileManipulationPanelListener interface.
 * @param newEvent java.util.EventObject
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void loadFileDialogFileSelection_fileSelected(java.util.EventObject newEvent) {
	// user code begin {1}
	// user code end
	if (newEvent.getSource() == getFileManipulationPanel()) 
		connEtoM10(newEvent);
	// user code begin {2}
	// user code end
}
/**
 * Starts the applet when it is run as an application
 * @param args an array of command-line arguments
 */
public static void main(java.lang.String[] args) {
	ToDoList applet = new ToDoList();
	java.awt.Frame frame = new java.awt.Frame("Applet");

	frame.addWindowListener(applet);
	frame.add("Center", applet);
	frame.setSize(350, 250);
	frame.show();

	applet.init();
	applet.start();
}
/**
 * Paints the applet.
 * If the applet does not need to be painted (e.g. if it is only a container for other
 * awt components) then this method can be safely removed.
 * 
 * @param g  the specified Graphics window
 * @see #update
 */
public void paint(Graphics g) {
	super.paint(g);

	// insert code to paint the applet here
}
/**
 * Method to handle events for the PropertyChangeListener interface.
 * @param evt java.beans.PropertyChangeEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void propertyChange(java.beans.PropertyChangeEvent evt) {
	// user code begin {1}
	// user code end
	if (evt.getSource() == getTaskList() && (evt.getPropertyName().equals("itemCount"))) 
		connPtoP1SetTarget();
	if (evt.getSource() == getCountField() && (evt.getPropertyName().equals("text"))) 
		connPtoP2SetTarget();
	if (evt.getSource() == getLessThan() && (evt.getPropertyName().equals("result"))) 
		connPtoP3SetTarget();
	if (evt.getSource() == getTaskList() && (evt.getPropertyName().equals("items"))) 
		connEtoM6(evt);
	if (evt.getSource() == getTaskList() && (evt.getPropertyName().equals("items"))) 
		connEtoM7(evt);
	if (evt.getSource() == getFillList() && (evt.getPropertyName().equals("currentStringResult"))) 
		connEtoM9(evt);
	if (evt.getSource() == getFileManipulationPanel() && (evt.getPropertyName().equals("readFileCurrentStringResult"))) 
		connEtoM13(evt);
	if (evt.getSource() == getTaskList() && (evt.getPropertyName().equals("items"))) 
		connPtoP4SetTarget();
	// user code begin {2}
	// user code end
}
/**
 * Method to handle events for the FileManipulationPanelListener interface.
 * @param newEvent java.util.EventObject
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void saveFileDialogFileSelection_fileSelected(java.util.EventObject newEvent) {
	// user code begin {1}
	// user code end
	if (newEvent.getSource() == getFileManipulationPanel()) 
		connEtoM14(newEvent);
	// user code begin {2}
	// user code end
}
/**
 * Method to handle events for the TriggerNextActionEventListener interface.
 * @param arg1 com.ibm.wiringhelpers.events.TriggerNextActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void triggerNextAction(com.ibm.wiringhelpers.events.TriggerNextActionEvent arg1) {
	// user code begin {1}
	// user code end
	if (arg1.getSource() == getClearList()) 
		connEtoM11(arg1);
	// user code begin {2}
	// user code end
}
/**
 * Method to handle events for the ThisActionEventListener interface.
 * @param arg1 com.ibm.wiringhelpers.events.ThisActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void triggerThisAction(com.ibm.wiringhelpers.events.ThisActionEvent arg1) {
	// user code begin {1}
	// user code end
	if (arg1.getSource() == getClearList()) 
		connEtoM12(arg1);
	// user code begin {2}
	// user code end
}
/**
 * Invoked when a window is activated.
 * @param e the received event
 */
public void windowActivated(WindowEvent e) {
	// Do nothing.
	// This method is required to comply with the WindowListener interface.
}
/**
 * Invoked when a window has been closed.
 * @param e the received event
 */
public void windowClosed(WindowEvent e) {
	// Do nothing.
	// This method is required to comply with the WindowListener interface.
}
/**
 * Invoked when a window is in the process of being closed.
 * The close operation can be overridden at this point.
 * @param e the received event
 */
public void windowClosing(WindowEvent e) {
	// The window is being closed.  Shut down the system.
	System.exit(0);
}
/**
 * Invoked when a window is deactivated.
 * @param e the received event
 */
public void windowDeactivated(WindowEvent e) {
	// Do nothing.
	// This method is required to comply with the WindowListener interface.
}
/**
 * Invoked when a window is de-iconified.
 * @param e the received event
 */
public void windowDeiconified(WindowEvent e) {
	// Do nothing.
	// This method is required to comply with the WindowListener interface.
}
/**
 * Invoked when a window is iconified.
 * @param e the received event
 */
public void windowIconified(WindowEvent e) {
	// Do nothing.
	// This method is required to comply with the WindowListener interface.
}
/**
 * Invoked when a window has been opened.
 * @param e the received event
 */
public void windowOpened(WindowEvent e) {
	// Do nothing.
	// This method is required to comply with the WindowListener interface.
}
}
