package util;

import javax.swing.*;

import sys.Message;

import java.awt.*;
import java.net.*;

@SuppressWarnings("serial")
public class WaitingSplash extends JWindow implements Runnable {
	String title= "";
	Thread splashThread; // �����������߳�
	JProgressBar progress; // ������
	boolean isStop = false;

	public WaitingSplash() {
		Container container = getContentPane(); // �õ�����
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); // ���ù��
		URL url = getClass().getResource("login.jpg"); // ͼƬ��λ��
		if (url != null) {
			container.add(new JLabel(new ImageIcon(url)), BorderLayout.CENTER); // ����ͼƬ
		}
		progress = new JProgressBar(1, 100); // ʵ����������
		progress.setForeground(Color.BLUE);
		progress.setFont(new Font("����", Font.BOLD, 18));
		progress.setStringPainted(true); // �������
		if(!this.title.equals("")){
			progress.setString(title); // ������ʾ����
		}else{
			progress.setString("ϵͳ������,���Ժ�......"); // ������ʾ����
		}
		progress.setBackground(Color.ORANGE); // ���ñ���ɫ
		container.add(progress, BorderLayout.SOUTH); // ���ӽ�������������

		Dimension screen = getToolkit().getScreenSize(); // �õ���Ļ�ߴ�
		pack(); // ������Ӧ����ߴ�
		setLocation((screen.width - getSize().width) / 2,
				(screen.height - getSize().height) / 2); // ���ô���λ��
	}
	
	public WaitingSplash(String title){
		this();
		this.title = title;
		Container container = getContentPane(); // �õ�����
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); // ���ù��
		URL url = getClass().getResource("login.jpg"); // ͼƬ��λ��
		if (url != null) {
			container.add(new JLabel(new ImageIcon(url)), BorderLayout.CENTER); // ����ͼƬ
		}
		progress = new JProgressBar(1, 100); // ʵ����������
		progress.setForeground(Color.BLUE);
		progress.setFont(new Font("����", Font.BOLD, 18));
		progress.setStringPainted(true); // �������
		if(!this.title.equals("")){
			progress.setString(title); // ������ʾ����
		}else{
			progress.setString("ϵͳ������,���Ժ�......"); // ������ʾ����
		}
		progress.setBackground(Color.ORANGE); // ���ñ���ɫ
		container.add(progress, BorderLayout.SOUTH); // ���ӽ�������������

		Dimension screen = getToolkit().getScreenSize(); // �õ���Ļ�ߴ�
		pack(); // ������Ӧ����ߴ�
		setLocation((screen.width - getSize().width) / 2,
				(screen.height - getSize().height) / 2); // ���ô���λ��
	}

	public void start() {
		this.toFront(); // ����ǰ����ʾ
		splashThread = new Thread(this); // ʵ�����߳�
		splashThread.start(); // ��ʼ�����߳�
	}
	
	public void stop(){
		isStop = true;
		splashThread.stop();
		this.setVisible(false);
	}

	public void run() {
		setVisible(true); // ��ʾ����
		try {
			for (int i = 0; i < 100; i++) {
				if(isStop) {
					break;
				}
				Thread.sleep(100); // �߳�����
				progress.setValue(progress.getValue() + 1); // ���ý�����ֵ
				if(i==99){
					i=0;
					progress.setValue(0);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		dispose(); // �ͷŴ���
		//showFrame(); // ����������
	}

	static void showFrame() {
		JFrame frame = new JFrame("��������������ʾ"); // ʵ����JFrame����
		frame.setSize(300, 200); // ���ô��ڳߴ�
		frame.setVisible(true); // ���ڿ���
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // �رմ���ʱ�˳�����
	}

	public static void main(String[] args) {
		WaitingSplash splash = new WaitingSplash();
		splash.start(); // ������������
	}
}

