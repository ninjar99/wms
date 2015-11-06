package sys;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyVetoException;

import javax.swing.JInternalFrame;
import javax.swing.plaf.basic.BasicInternalFrameUI;


public class InnerFrame extends JInternalFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6219573143512455305L;

	/** The is hidden. */ 
    boolean isHidden = false; 
 
    /** The old ui. */ 
    BasicInternalFrameUI oldUi = null; 
 
    /** 
     * Instantiates a new inner frame. 
     */ 
    public InnerFrame() { 
        oldUi = (BasicInternalFrameUI)getUI(); 
        setSize(200, 200); 
        maximizable = true; 
        closable = true; 
        addComponentListener(new ComponentAdapter() { 
            public void componentResized(ComponentEvent e) {
                InnerFrame selectedFrame = (InnerFrame)e.getSource(); 
                if(selectedFrame.isMaximum()){ 
                    selectedFrame.hideNorthPanel(); 
                    try { 
                         selectedFrame.setMaximum(true); 
                    } catch (PropertyVetoException ex) { 
                         ex.printStackTrace(); 
                     } 
                 } 
             } 
        }); 
    } 
 
    /** 
     * Hide north panel. 
     */ 
    public void hideNorthPanel(){ 
        ((BasicInternalFrameUI) this.getUI()).setNorthPane(null); 
        this.putClientProperty("JInternalFrame.isPalette", Boolean.TRUE); 
        isHidden = true; 
    } 
 
    /** 
     * Show north panel. 
     */ 
    public void showNorthPanel() { 
        this.setUI(oldUi); 
        this.putClientProperty("JInternalFrame.isPalette", Boolean.FALSE); 
        isHidden = false; 
    } 
 
    /* (non-Javadoc) 
     * @see javax.swing.JInternalFrame#updateUI() 
     */ 
    public void updateUI() { 
        super.updateUI(); 
        if (isHidden) { 
            hideNorthPanel(); 
        } 
     } 
    
}
