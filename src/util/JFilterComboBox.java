package util;

import java.awt.Component;  
import java.awt.event.FocusEvent;  
import java.awt.event.FocusListener;  
import java.awt.event.ItemEvent;  
import java.awt.event.KeyEvent;  
import java.awt.event.KeyListener;  
import java.awt.event.MouseEvent;  
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.ComboBoxModel;  
import javax.swing.DefaultComboBoxModel;  
import javax.swing.JComboBox;  
import javax.swing.JComponent;  
import javax.swing.JFrame;  
import javax.swing.JList;  
import javax.swing.JPanel;  
import javax.swing.ListCellRenderer;  
import javax.swing.SwingUtilities;  
import javax.swing.event.ListDataEvent;  
import javax.swing.plaf.basic.BasicComboPopup;  
import javax.swing.plaf.basic.ComboPopup;  
import javax.swing.plaf.metal.MetalComboBoxUI;

import DBUtil.DBConnectionManager;
import comUtil.WMSCombobox;  
  
/** 
 * �Զ����������� 
 * @author Sun 
 * 
 */  
@SuppressWarnings({ "serial", "rawtypes" })  
public class JFilterComboBox extends JComboBox {  
	private boolean flag;
	private ConcurrentHashMap<String, String> hm_name_OID;
	private ConcurrentHashMap<String, String> hm_OID_name;
	private Vector<String> items;
	WMSCombobox self;
	private String sql;
      
    /** 
     * ��ʾ��ģ�� 
     */  
	private DefaultComboBoxModel showModel = new DefaultComboBoxModel();  
    /** 
     * ����ѡ�� 
     */  
    private boolean selectingItem;  
      
    /** 
     * ����һ��JFilterComboBox�� 
     * ����ȡ�����е�ComboBoxModel�� 
     * �����ṩ��ComboBoxModel�� 
     * ʹ�ô˹��췽����������Ͽ򲻴���Ĭ����Ͽ�ģ�ͣ� 
     * �����Ӱ����롢�Ƴ�����ӷ�������Ϊ��ʽ�� 
     * @param aModel - �ṩ��ʾ�����б�� ComboBoxModel 
     */  
    @SuppressWarnings("unchecked")
	public JFilterComboBox(ComboBoxModel aModel) {  
        super(aModel);  
        initialize();  
    }  
  
    /** 
     * ��������ָ�������е�Ԫ�ص� JFilterComboBox�� 
     * Ĭ������£�ѡ�������еĵ�һ����Ҳѡ���˸��������ģ�ͣ���  
     * @param items - Ҫ���뵽��Ͽ�Ķ������� 
     */  
    @SuppressWarnings("unchecked")
	public JFilterComboBox(final Object items[]) {  
        super(items);  
        initialize();  
    }  
      
    /** 
     *  ��������ָ�� Vector �е�Ԫ�ص� JFilterComboBox�� 
     * Ĭ������£�ѡ�������еĵ�һ����Ҳѡ���˸��������ģ�ͣ���  
     * @param items - Ҫ���뵽��Ͽ���������� 
     */  
    @SuppressWarnings("unchecked")
	public JFilterComboBox(Vector< ?> items) {  
        super(items);  
        initialize();  
    }
    
    public JFilterComboBox(String sql,boolean flag) {  
        super();  
        this.flag = flag;
		this.sql = sql;
		hm_name_OID = new ConcurrentHashMap<String, String>();
		hm_OID_name = new ConcurrentHashMap<String, String>();
		items = new Vector<String>();
        initialize();  
    }
      
    /** 
     * ��������Ĭ������ģ�͵� JFilterComboBox�� 
     * Ĭ�ϵ�����ģ��Ϊ�ն����б�ʹ�� addItem ���� 
     * Ĭ������£�ѡ������ģ���еĵ�һ� 
     */  
    public JFilterComboBox() {  
        super();  
        initialize();  
    }  
      
    @SuppressWarnings("unchecked")
	private void initialize() {
    	if (flag) {
			this.addItem("");
		}
    	showModel = new DefaultComboBoxModel();
    	showModel.addListDataListener(this); 
		try {
			java.sql.Connection con = DBConnectionManager.getInstance().getConnection("wms");
			java.sql.Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				items.add(NulltoSpace(rs.getString(1)));
				hm_name_OID.put(NulltoSpace(rs.getString(2)), NulltoSpace(rs.getString(1)));
				hm_OID_name.put(NulltoSpace(rs.getString(1)), NulltoSpace(rs.getString(2)));
				this.addItem(NulltoSpace(rs.getString(2)));
				showModel.addElement(NulltoSpace(rs.getString(2)));
			}
			
			DBConnectionManager.getInstance().freeConnection("wms", con);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.updateUI();
         
    }
    
    @SuppressWarnings("unchecked")
	public void refresh() {
		self.removeAllItems();
		hm_name_OID.clear();
		hm_OID_name.clear();
		if (flag) {
			self.addItem("");
		}
		try {
			java.sql.Connection con = DBConnectionManager.getInstance().getConnection("wms");
			java.sql.Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				hm_name_OID.put(NulltoSpace(rs.getString(2)), NulltoSpace(rs.getString(1)));
				hm_OID_name.put(NulltoSpace(rs.getString(1)), NulltoSpace(rs.getString(2)));
				this.addItem(NulltoSpace(rs.getString(2)));
				showModel.addElement(NulltoSpace(rs.getString(2)));
			}
			DBConnectionManager.getInstance().freeConnection("wms", con);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.updateUI();
	}
    
    public String getSelectedOID() {
		String displayOID = NulltoSpace(this.getSelectedItem());
		if (displayOID != null && !displayOID.equals("")) {
			return (String) hm_name_OID.get(displayOID);
		} else {
			return "";
		}
	}

	public void setSelectedDisplayName(String OID) {
		if (OID != null && !OID.equals("")) {
			this.setSelectedItem(hm_OID_name.get(OID));
		} else {
			this.setSelectedItem("");
		}
	}

	public Object[] getAllItems() {

		Object[] s = items.toArray();
		return s;
	}
      
    @Override  
    public void updateUI() {  
        setUI(new MetalFilterComboBoxUI());  
        ListCellRenderer renderer = getRenderer();  
        if (renderer instanceof Component) {  
            SwingUtilities.updateComponentTreeUI((Component) renderer);  
        }  
    }  
      
    @Override  
    public Object getSelectedItem() {  
        return showModel.getSelectedItem();  
    }  
  
    @Override  
    public void setSelectedItem(Object anObject) {  
        Object oldSelection = selectedItemReminder;  
        Object objectToSelect = anObject;  
        if (oldSelection == null || !oldSelection.equals(anObject)) {  
  
            if (anObject != null && !isEditable()) {  
                boolean found = false;  
                for (int i = 0; i < showModel.getSize(); i++) {  
                    Object element = showModel.getElementAt(i);  
                    if (anObject.equals(element)) {  
                        found = true;  
                        objectToSelect = element;  
                        break;  
                    }  
                }  
                if (!found) {  
                    return;  
                }  
            }  
  
            selectingItem = true;  
            showModel.setSelectedItem(objectToSelect);  
            selectingItem = false;  
  
            if (selectedItemReminder != showModel.getSelectedItem()) {  
                selectedItemChanged();  
            }  
        }  
        fireActionEvent();  
    }  
  
    @Override  
    public void setSelectedIndex(int anIndex) {  
        int size = showModel.getSize();  
        if (anIndex == -1 || size == 0) {  
            setSelectedItem(null);  
        } else if (anIndex < -1) {  
            throw new IllegalArgumentException("setSelectedIndex: " + anIndex  
                    + " out of bounds");  
        } else if (anIndex >= size) {  
            setSelectedItem(showModel.getElementAt(size - 1));  
        } else {  
            setSelectedItem(showModel.getElementAt(anIndex));  
        }  
    }  
  
    @Override  
    public int getSelectedIndex() {  
        Object sObject = showModel.getSelectedItem();  
        int i, c;  
        Object obj;  
  
        for (i = 0, c = showModel.getSize(); i < c; i++) {  
            obj = showModel.getElementAt(i);  
            if (obj != null && obj.equals(sObject))  
                return i;  
        }  
        return -1;  
    }  
  
    @Override  
    public void contentsChanged(ListDataEvent e) {  
        Object oldSelection = selectedItemReminder;  
        Object newSelection = showModel.getSelectedItem();  
        if (oldSelection == null || !oldSelection.equals(newSelection)) {  
            selectedItemChanged();  
            if (!selectingItem) {  
                fireActionEvent();  
            }  
        }  
    }  
  
    @SuppressWarnings("unchecked")
	@Override  
    protected void selectedItemChanged() {  
        if (selectedItemReminder != null) {  
            fireItemStateChanged(new ItemEvent(this,  
                    ItemEvent.ITEM_STATE_CHANGED, selectedItemReminder,  
                    ItemEvent.DESELECTED));  
        }  
  
        selectedItemReminder = showModel.getSelectedItem();  
  
        if (selectedItemReminder != null) {  
            fireItemStateChanged(new ItemEvent(this,  
                    ItemEvent.ITEM_STATE_CHANGED, selectedItemReminder,  
                    ItemEvent.SELECTED));  
        }  
    }  
  
    @Override  
    public void intervalAdded(ListDataEvent e) {  
        if (selectedItemReminder != showModel.getSelectedItem()) {  
            selectedItemChanged();  
        }  
    }  
  
    @Override  
    public void setEditable(boolean aFlag) {  
        super.setEditable(true);  
    }  
  
    /** 
     * ������ʾ��ģ�� 
     * @return 
     */  
    public DefaultComboBoxModel getShowModel() {  
    	if(showModel==null){
    		showModel = new DefaultComboBoxModel(); 
    	}
        return showModel;  
    }  
    
    public static String NulltoSpace(Object o) {
		if (o == null)
			return "";
		else if (o.equals("null")) {
			return "";
		} else
			return o.toString().trim();
	}
      
    /** 
     * Metal L&F ���� UI �� 
     * @author Sun 
     * 
     */  
    class MetalFilterComboBoxUI extends MetalComboBoxUI {  
          
        /** 
         * �༭���¼������� 
         */  
        protected EditorListener editorListener;  
        /** 
         * �� UI �ฺ����ƵĿؼ� 
         */  
        protected JFilterComboBox filterComboBox;  
  
        @Override  
        public void installUI(JComponent c) {  
            filterComboBox = (JFilterComboBox) c;  
            filterComboBox.setEditable(true);  
            super.installUI(c);  
        }  
  
        @Override  
        public void configureEditor() {  
            super.configureEditor();  
            editor.addKeyListener(getEditorListener());  
            editor.addMouseListener(getEditorListener());  
            editor.addFocusListener(getEditorListener());  
        }  
          
        @Override  
        public void unconfigureEditor() {  
            super.unconfigureEditor();  
            if (editorListener != null) {  
                editor.removeKeyListener(editorListener);  
                editor.removeMouseListener(editorListener);  
                editor.removeFocusListener(editorListener);  
                editorListener = null;  
            }  
        }  
          
        @Override  
        protected ComboPopup createPopup() {  
            return new FilterComboPopup(filterComboBox);  
        }  
          
        /** 
         * ��ʼ�������ر༭���¼������� 
         * @return 
         */  
        protected EditorListener getEditorListener() {  
            if (editorListener == null) {  
                editorListener = new EditorListener();  
            }  
            return editorListener;  
        }  
  
        /** 
         * ���ؼ��ֽ��в�ѯ���÷����У��������м�����ֲ�ѯ�㷨 
         */  
        @SuppressWarnings("unchecked")
		protected void findMatchs() {  
            ComboBoxModel model = filterComboBox.getModel();  
            DefaultComboBoxModel showModelTMP = filterComboBox.getShowModel();  
            showModelTMP.removeAllElements();  
            for (int i = 0; i < model.getSize(); i++) {  
                String name = model.getElementAt(i).toString();  
                if (name.indexOf(getEditorText()) >= 0) {  
                	showModelTMP.addElement(model.getElementAt(i));  
                }  
            }  
            ((FilterComboPopup)popup ).repaint();  
        }  
          
        /** 
         * ���ر༭���ı� 
         * @return 
         */  
        private String getEditorText() {  
            return filterComboBox.getEditor().getItem().toString();  
        }  
          
        /** 
         * ��������� 
         * @author Sun 
         * 
         */  
        class FilterComboPopup extends BasicComboPopup {  
  
            public FilterComboPopup(JComboBox combo) {  
                super(combo);  
            }  
              
            @SuppressWarnings("unchecked")
			@Override  
            protected JList createList() {  
                JList list = super.createList();  
                list.setModel(filterComboBox.getShowModel());  
                return list;  
            }  
              
            @Override  
            public void setVisible(boolean b) {  
                super.setVisible(b);  
                if (!b) {  
                    comboBox.getEditor().setItem(comboBox.getSelectedItem());  
                }  
            }  
              
            @Override  
            public void show() {  
                findMatchs();  
                super.show();  
            }  
  
        }  
          
        /** 
         * �༭���¼��������� 
         * @author Sun 
         * 
         */  
        class EditorListener implements KeyListener,MouseListener, FocusListener {  
              
            /** 
             * ���ı������ڼ�������ʱ�ıȶ� 
             */  
            private String oldText = "";  
              
            @Override  
            public void keyReleased(KeyEvent e) {  
                String newText = getEditorText();  
                if (!newText.equals(oldText)) {  
                    findMatchs();  
                }  
            }  
              
            @Override  
            public void keyPressed(KeyEvent e) {  
                oldText = getEditorText();  
                if (!isPopupVisible(filterComboBox)) {  
                    setPopupVisible(filterComboBox, true);  
                }  
            }  
  
            @Override  
            public void keyTyped(KeyEvent e) {  
                findMatchs();  
            }  
  
            @Override  
            public void mouseClicked(MouseEvent e) {}  
  
            @Override  
            public void mousePressed(MouseEvent e) {  
                if (!isPopupVisible(filterComboBox)) {  
                    setPopupVisible(filterComboBox, true);  
                }  
            }  
  
            @Override  
            public void mouseReleased(MouseEvent e) {}  
  
            @Override  
            public void mouseEntered(MouseEvent e) {}  
  
            @Override  
            public void mouseExited(MouseEvent e) {}  
              
            @Override  
            public void focusGained(FocusEvent e) {  
                if (!isPopupVisible(filterComboBox)) {  
                    setPopupVisible(filterComboBox, true);  
                }  
            }  
              
            @Override  
            public void focusLost(FocusEvent e) {}  
  
        }  
  
    }  
      
    /** 
     * ʹ��ʾ�� 
     * @param args 
     */  
    public static void main(String... args) {  
        Vector< String> data = new Vector< String>(0);  
        data.add("");  
        data.add("�ձ�");  
        data.add("����");  
        data.add("�й�");  
        JPanel panel = new JPanel();
        JFilterComboBox combo = new JFilterComboBox("SELECT COUNTRY_CODE,country_name FROM bas_country",true);
        panel.add(combo);  
        JFrame frame = new JFrame();  
        frame.setSize(400, 300);  
        frame.setLocationRelativeTo(null);  
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        frame.setContentPane(panel);  
        frame.setVisible(true);  
        combo.setSelectedDisplayName("106");
    }  
  
} 
