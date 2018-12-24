package ToDoListPackage;

/**
 * This is the event multicaster class to support the ToDoListPackage.FileManipulationPanelListenerEventMulticaster interface.
 */
public class FileManipulationPanelListenerEventMulticaster extends java.awt.AWTEventMulticaster implements FileManipulationPanelListener {
/**
 * Constructor to support multicast events.
 * @param a java.util.EventListener
 * @param b java.util.EventListener
 */
protected FileManipulationPanelListenerEventMulticaster(java.util.EventListener a, java.util.EventListener b) {
	super(a, b);
}
/**
 * Add new listener to support multicast events.
 * @return ToDoListPackage.FileManipulationPanelListener
 * @param a ToDoListPackage.FileManipulationPanelListener
 * @param b ToDoListPackage.FileManipulationPanelListener
 */
public static ToDoListPackage.FileManipulationPanelListener add(ToDoListPackage.FileManipulationPanelListener a, ToDoListPackage.FileManipulationPanelListener b) {
	return (ToDoListPackage.FileManipulationPanelListener)addInternal(a, b);
}
/**
 * Add new listener to support multicast events.
 * @return java.util.EventListener
 * @param a java.util.EventListener
 * @param b java.util.EventListener
 */
protected static java.util.EventListener addInternal(java.util.EventListener a, java.util.EventListener b) {
	if (a == null)  return b;
	if (b == null)  return a;
	return new FileManipulationPanelListenerEventMulticaster(a, b);
}
/**
 * 
 * @param newEvent java.util.EventObject
 */
public void loadFileDialogFileSelection_fileSelected(java.util.EventObject newEvent) {
	((ToDoListPackage.FileManipulationPanelListener)a).loadFileDialogFileSelection_fileSelected(newEvent);
	((ToDoListPackage.FileManipulationPanelListener)b).loadFileDialogFileSelection_fileSelected(newEvent);
}
/**
 * 
 * @return java.util.EventListener
 * @param oldl ToDoListPackage.FileManipulationPanelListener
 */
protected java.util.EventListener remove(ToDoListPackage.FileManipulationPanelListener oldl) {
	if (oldl == a)  return b;
	if (oldl == b)  return a;
	java.util.EventListener a2 = removeInternal(a, oldl);
	java.util.EventListener b2 = removeInternal(b, oldl);
	if (a2 == a && b2 == b)
		return this;
	return addInternal(a2, b2);
}
/**
 * Remove listener to support multicast events.
 * @return ToDoListPackage.FileManipulationPanelListener
 * @param l ToDoListPackage.FileManipulationPanelListener
 * @param oldl ToDoListPackage.FileManipulationPanelListener
 */
public static ToDoListPackage.FileManipulationPanelListener remove(ToDoListPackage.FileManipulationPanelListener l, ToDoListPackage.FileManipulationPanelListener oldl) {
	if (l == oldl || l == null)
		return null;
	if(l instanceof FileManipulationPanelListenerEventMulticaster)
		return (ToDoListPackage.FileManipulationPanelListener)((ToDoListPackage.FileManipulationPanelListenerEventMulticaster) l).remove(oldl);
	return l;
}
/**
 * 
 * @param newEvent java.util.EventObject
 */
public void saveFileDialogFileSelection_fileSelected(java.util.EventObject newEvent) {
	((ToDoListPackage.FileManipulationPanelListener)a).saveFileDialogFileSelection_fileSelected(newEvent);
	((ToDoListPackage.FileManipulationPanelListener)b).saveFileDialogFileSelection_fileSelected(newEvent);
}
}
