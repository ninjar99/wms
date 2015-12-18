package sys;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import DBUtil.DBOperator;
import dmdata.DataManager;
import main.PBSUIBaseGrid;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import javax.swing.SwingConstants;

public class tableEditDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4551066906292028844L;
	private tableEditDialog tableQuery;
	private DataManager dm;
	public static DataManager resultDM;
	private String querySQL = "";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			tableEditDialog dialog = new tableEditDialog("");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public tableEditDialog(String sql) {
		tableQuery = this;
		this.querySQL = sql;
		setBounds(100, 100, 1024, 600);
		getContentPane().setLayout(new BorderLayout());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.LEFT));
			getContentPane().add(buttonPane, BorderLayout.NORTH);
			{
				JButton btnSave = new JButton("\u4FDD\u5B58");
				btnSave.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						tableQuery.dispose();
					}
				});
				buttonPane.add(btnSave);
			}
			{
				JButton btnClose = new JButton("\u5173\u95ED");
				btnClose.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						tableQuery.dispose();
					}
				});
				btnClose.setActionCommand("OK");
				buttonPane.add(btnClose);
				getRootPane().setDefaultButton(btnClose);
			}
		}
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(20, 0, 0, 0));
		dm = DBOperator.DoSelect2DM(sql);
		JPanel subPanel = null;
		for(int i=0;i<dm.getColCount();i++){
			if(i % 3 ==0){
				subPanel = new JPanel();
				subPanel.setLayout(new GridLayout(0, 6, 0, 0));
				panel.add(subPanel);
			}
			JLabel lab = new JLabel();
			lab.setHorizontalAlignment(SwingConstants.RIGHT);
			lab.setText(dm.getCol(i).toString());
			subPanel.add(lab);
			JTextField txt = new JTextField();
			txt.setToolTipText(dm.getCol(i).toString());
			txt.setColumns(10);
			subPanel.add(txt);
		}
		
		

	}

}
