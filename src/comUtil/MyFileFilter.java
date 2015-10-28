package comUtil;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author samzheng
 */
public class MyFileFilter extends FileFilter {

    @SuppressWarnings("rawtypes")
	private Hashtable filters = null;
    private String description = null;
    private String fullDescription = null;
    private boolean useExtensionsInDescription = true;

    public MyFileFilter() {
        this.filters = new Hashtable();
    }

    public MyFileFilter(String extension) {
        this(extension, null);
    }

    public MyFileFilter(String extension, String description) {
        this();
        if (extension != null) {
            addExtension(extension);
        }
        if (description != null) {
            setDescription(description);
        }
    }

    public MyFileFilter(String[] filters) {
        this(filters, null);
    }

    public MyFileFilter(String[] filters, String description) {
        this();
        for (int i = 0; i < filters.length; i++) {
            //   add   filters   one   by   one 
            addExtension(filters[i]);
        }
        if (description != null) {
            setDescription(description);
        }
    }

    public boolean accept(File f) {
        if (f != null) {
            String extension = getExtension(f);
            if (extension != null && filters.get(getExtension(f)) != null) {
                return true;
            };
        }
        return false;
    }

    public String getExtension(File f) {
        if (f != null) {
            String filename = f.getName();
            int i = filename.lastIndexOf('.');
            if (i > 0 && i < filename.length() - 1) {
                return filename.substring(i + 1).toLowerCase();
            };
        }
        return null;
    }

    public void addExtension(String extension) {
        if (filters == null) {
            filters = new Hashtable(5);
        }
        filters.put(extension.toLowerCase(), this);
        fullDescription = null;
    }

    public String getDescription() {
        if (fullDescription == null) {
            if (description == null || isExtensionListInDescription()) {
                fullDescription = description == null ? "( " : description + "   ( ";
//   build   the   description   from   the   extension   list 
                Enumeration extensions = filters.keys();
                if (extensions != null) {
                    fullDescription += ". " + (String) extensions.nextElement();
                    while (extensions.hasMoreElements()) {
                        fullDescription += ",   " + (String) extensions.nextElement();
                    }
                }
                fullDescription += ") ";
            } else {
                fullDescription = description;
            }
        }
        return fullDescription;
    }

    public void setDescription(String description) {
        this.description = description;
        fullDescription = null;
    }

    public void setExtensionListInDescription(boolean b) {
        useExtensionsInDescription = b;
        fullDescription = null;
    }

    public boolean isExtensionListInDescription() {
        return useExtensionsInDescription;
    }

    public static void main(String[] args) {
        //ʹ�÷���
        String filePath = "";
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        //ȥ����ʾ�����ļ������������
        chooser.setAcceptAllFileFilterUsed(false);

        chooser.addChoosableFileFilter(new MyFileFilter("xls", "Excel Files"));
        chooser.addChoosableFileFilter(new MyFileFilter("xlsx", "Excel Files"));

        int returnVal = chooser.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            filePath = chooser.getSelectedFile().getAbsolutePath();
        }
        if (filePath.equals("")) {
            return;
        }
    }
}

