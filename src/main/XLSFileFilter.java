package main;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class XLSFileFilter extends FileFilter {
  public boolean accept(File f) {
    if (f.isDirectory()) {
      return true;
    }

    String extension = "xls";
    if (getExtension(f).equals(extension))
      return true;
    return false;
  }

  public String getDescription() {
    return "Excel files";
  }

  public static String getExtension(File f) {
    String ext = "";
    String s = f.getName();
    int i = s.lastIndexOf('.');

    if (i > 0 &&  i < s.length() - 1) {
      ext = s.substring(i+1).toLowerCase();
    }
    return ext;
  }
}
