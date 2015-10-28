package sys;

import javax.swing.*;

import java.awt.event.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

class MDITest extends JFrame implements ActionListener {

	JDesktopPane desktopPane;
	JPanel p;
	JButton b1;
	JButton b2;
	int count = 1;

	public MDITest() {
		super("MDITest Frame");
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());

		/*
		 * ����һ���µ�JDesktopPane��������contentPane��
		 */
		desktopPane = new JDesktopPane();
		contentPane.add(desktopPane);
		p = new JPanel(new FlowLayout());
		contentPane.add(p, BorderLayout.SOUTH);

		b1 = new JButton("Create New Internal Frames");
		b1.addActionListener(this);// ���û����°�ťʱ��������actionPerformed()�еĳ���
		p.add(b1);

		b2 = new JButton("Close Selected Frame");
		b2.addActionListener(this);//
		p.add(b2);

		setSize(500, 400);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b1) {
			/*
			 * ����һ���ɹرա��ɸı��С�����б��⡢���������С����Internal Frame.
			 */
			JInternalFrame internalFrame = new JInternalFrame("Internal Frame " + (count++), true, true, true, true);

			internalFrame.setLocation(20, 20);
			internalFrame.setSize(300, 300);
			internalFrame.setVisible(true);
			// ��JInternalFrame����JDesktopPane��
			desktopPane.add(internalFrame);

			try {
				internalFrame.setSelected(true);
			} catch (java.beans.PropertyVetoException ex) {
				System.out.println("Exception while selecting");
			}

			// ���������б����һ�� VetoableChangeListener��Ϊ��������ע���������
			internalFrame.addVetoableChangeListener(new VetoableChangeListener() {

				public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException {
					JInternalFrame frame = (JInternalFrame) e.getSource();
					String name = e.getPropertyName();
					Object value = e.getNewValue();
					if (name.equals("closed") && value.equals(Boolean.TRUE)) // ���ڱ��ر�
					{
						System.out.println("���ڱ��ر�");
					}
				}
			});
		} else if (e.getSource() == b2) {
			// ���ش� JDesktopPane �е�ǰ���JInternalFrame
			JInternalFrame iframe = desktopPane.getSelectedFrame();
			if (iframe != null) {
				iframe.dispose();
			}
		}
	}

	public static void main(String[] args) {
		new MDITest();
	}
}
