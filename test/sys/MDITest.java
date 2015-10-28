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
		 * 建立一个新的JDesktopPane并加入于contentPane中
		 */
		desktopPane = new JDesktopPane();
		contentPane.add(desktopPane);
		p = new JPanel(new FlowLayout());
		contentPane.add(p, BorderLayout.SOUTH);

		b1 = new JButton("Create New Internal Frames");
		b1.addActionListener(this);// 当用户按下按钮时，将运行actionPerformed()中的程序
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
			 * 产生一个可关闭、可改变大小、具有标题、可最大化与最小化的Internal Frame.
			 */
			JInternalFrame internalFrame = new JInternalFrame("Internal Frame " + (count++), true, true, true, true);

			internalFrame.setLocation(20, 20);
			internalFrame.setSize(300, 300);
			internalFrame.setVisible(true);
			// 将JInternalFrame加入JDesktopPane中
			desktopPane.add(internalFrame);

			try {
				internalFrame.setSelected(true);
			} catch (java.beans.PropertyVetoException ex) {
				System.out.println("Exception while selecting");
			}

			// 向侦听器列表添加一个 VetoableChangeListener。为所有属性注册该侦听器
			internalFrame.addVetoableChangeListener(new VetoableChangeListener() {

				public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException {
					JInternalFrame frame = (JInternalFrame) e.getSource();
					String name = e.getPropertyName();
					Object value = e.getNewValue();
					if (name.equals("closed") && value.equals(Boolean.TRUE)) // 窗口被关闭
					{
						System.out.println("窗口被关闭");
					}
				}
			});
		} else if (e.getSource() == b2) {
			// 返回此 JDesktopPane 中当前活动的JInternalFrame
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
