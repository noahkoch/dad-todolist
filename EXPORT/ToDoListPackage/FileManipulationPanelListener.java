package ToDoListPackage;

public interface FileManipulationPanelListener extends java.util.EventListener {
/**
 * 
 * @param newEvent java.util.EventObject
 */
void loadFileDialogFileSelection_fileSelected(java.util.EventObject newEvent);
/**
 * 
 * @param newEvent java.util.EventObject
 */
void saveFileDialogFileSelection_fileSelected(java.util.EventObject newEvent);
}
