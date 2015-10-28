package main;

import java.awt.*;
import javax.swing.*;

/**
 * A base dialog that will be extended by all Dialogs.
 */

public class PBSUIBaseDialog extends JDialog {

  public PBSUIBaseDialog(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public PBSUIBaseDialog() {
    this(null, "", false);
  }
  private void jbInit() throws Exception {
  }
}