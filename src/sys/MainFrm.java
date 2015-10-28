package sys;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sam
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;

import bas.BasBrandFrm;
import bas.BasContainerFrm;
import bas.BasItemFrm;
import bas.BasLocationFrm;
import bas.UserFrm;
import bas.storerMasterFrm;
import comUtil.comData;
import dmdata.DataManager;
import inbound.POFrm;
import inbound.inbScanFrm;
import inbound.poImportFrm;
import inbound.putawayFrm;
import inventory.InvMoveFrm;
import inventory.InvQueryFrm;
import inventory.InvTransferFrm;
import outbound.ShipmentOubCheck;
import outbound.ShipmentOubScan;
import outbound.ShipmentQueryFrm;
import outbound.StockTakeFrm;
import outbound.StockTakeQueryFrm;
import outbound.TrackingNoScanFrm;
import javax.swing.SwingConstants;

/** */
/**
 *
 * Title: LoonFramework
 * 
 *
 * Description: Copyright: Copyright (c) 2007
 * 
 *
 * Company:
 * 
 * @author @email��
 * @version 0.1
 */
public class MainFrm extends JPanel {
	/** */
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	static JFrame frame;
	
	private final JPanel leftPanel = new JPanel();
	// �ָ��
	private final JSplitPane split = new JSplitPane();
	private final JScrollPane leftScroll;
	private static ImageIcon ico=new ImageIcon("images/aggwms.jpg");
	private final JDesktopPane desktopPane = new JDesktopPane();
	private static DataManager userInfo = new DataManager();
	private static DataManager versionInfo = new DataManager();
	public static String wmsVersion = "1.0";
	private final JLabel lblNewLabel = new JLabel("");
	
	public static DataManager getVersionInfo() {
		return versionInfo;
	}

	public static void setVersionInfo(DataManager versionInfo) {
		MainFrm.versionInfo = versionInfo;
	}

	//CUR_WAREHOUSE_CODE WAREHOUSE_CODE  USER_CODE  USER_NAME  ROLE_NAME
	public static DataManager getUserInfo() {
		return userInfo;
	}

	public static void setUserInfo(DataManager userInfo) {
		MainFrm.userInfo = userInfo;
	}
	
	public void paintComponent(Graphics g){
	     g.drawImage(ico.getImage(),0,0,this);
	  }

	// �۵�Ч��
	public MainFrm() {
		super(new BorderLayout());
		leftPanel.setOpaque(true);
		leftPanel.setBackground(new Color(116, 149, 226));
		// ������
		leftScroll = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		leftScroll.getVerticalScrollBar().setUnitIncrement(10);
		leftScroll.setViewportView(leftPanel);
		// ���������б�
		List<AccordionPanel> panelList = makeList();
		// �趨����
		accordionListener exr = new accordionListener() {
			public void accordionStateChanged(accordionEvent e) {
				initComponent();
			}
		};
		for (Iterator<AccordionPanel> it = panelList.iterator(); it.hasNext();) {
			AccordionPanel epl = (AccordionPanel) it.next();
			addComponent(epl);
			epl.addaccordionListener(exr);
		}
		// ���ع���������
		leftScroll.getViewport().addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				initComponent();
			}
		});
		leftScroll.setPreferredSize(new Dimension(200, 260));
		leftScroll.setMinimumSize(new Dimension(200, 260));
		split.setLeftComponent(leftScroll);
		split.setDividerSize(1);
		split.setBackground(Color.WHITE);
		split.isMaximumSizeSet();
		add(split, BorderLayout.CENTER);
//		desktopPane.setBackground(UIManager.getColor("RadioButtonMenuItem.selectionBackground"));

		split.setRightComponent(desktopPane);
		desktopPane.setLayout(new BorderLayout(0, 0));
		Image image=new ImageIcon("images/bg.gif").getImage();
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setIcon(new ImageIcon(MainFrm.class.getResource("/images/aggwms.png")));
		
		desktopPane.add(lblNewLabel, BorderLayout.CENTER);
	}

	public void initComponent() {
//		loadFrameFont();
		Rectangle re = leftScroll.getViewport().getViewRect();
		Insets ins = leftPanel.getInsets();
		int cw = (int) re.getWidth() - ins.left - ins.right - 20;
		int ch = 10;
		Component[] list = leftPanel.getComponents();
		for (int i = 0; i < list.length; i++) {
			JComponent tmp = (JComponent) list[i];
			int th = tmp.getPreferredSize().height;
			tmp.setPreferredSize(new Dimension(cw, th));
			ch = ch + th + 10;
		}
		leftPanel.setPreferredSize(new Dimension((int) re.getWidth(), ch + ins.top + ins.bottom));
		leftPanel.revalidate();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(loginFrm.class.getResource("/images/logo.png")));
	}

	public void addComponent(Component label) {
		SpringLayout layout = new SpringLayout();
		Component[] list = leftPanel.getComponents();
		if (list.length == 0) {
			layout.putConstraint(SpringLayout.WEST, label, 10, SpringLayout.WEST, leftPanel);
			layout.putConstraint(SpringLayout.NORTH, label, 10, SpringLayout.NORTH, leftPanel);
		} else {
			JComponent cmp = null;
			for (int i = 0; i < list.length; i++) {
				JComponent tmp = (JComponent) list[i];
				layout.putConstraint(SpringLayout.WEST, tmp, 10, SpringLayout.WEST, leftPanel);
				if (cmp == null) {
					layout.putConstraint(SpringLayout.NORTH, tmp, 10, SpringLayout.NORTH, leftPanel);
				} else {
					layout.putConstraint(SpringLayout.NORTH, tmp, 10, SpringLayout.SOUTH, cmp);
				}
				cmp = tmp;
			}
			layout.putConstraint(SpringLayout.WEST, label, 10, SpringLayout.WEST, leftPanel);
			layout.putConstraint(SpringLayout.NORTH, label, 10, SpringLayout.SOUTH, cmp);
		}
		leftPanel.add(label);
		leftPanel.setLayout(layout);
		initComponent();
	}

	private List<AccordionPanel> makeList() {

		List<AccordionPanel> panelList = new ArrayList<AccordionPanel>();
		panelList.add(new AccordionPanel("��������") {
			testFrm testfrm = null;
			storerMasterFrm storerFrm = null;
			UserFrm userfrm = null;
			BasItemFrm basItemFrm = null;
			BasLocationFrm basLocationFrm = null;
			BasBrandFrm basBrandFrm  = null;
			BasContainerFrm bascontainerFrm = null;
			private static final long serialVersionUID = 1L;

			public JPanel makePanel() {
				JPanel pnl = new JPanel(new GridLayout(0, 1));
				JButton binner = new JButton("innerFrm test");
				binner.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						InnerFrame iFrame = new InnerFrame();
						Container cont = iFrame.getContentPane();
						JPanel jp = new JPanel();
						JButton add = new JButton("add");
						jp.add(add);
						cont.add(jp, BorderLayout.NORTH);
						iFrame.setVisible(true);

						desktopPane.add(iFrame);
						try {
							iFrame.setMaximum(true);
						} catch (PropertyVetoException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
//				pnl.add(binner);

				JButton b1 = new JButton("testFrm");
				JButton btnStorer = new JButton("������Ϣ");
				JButton btnBrand = new JButton("Ʒ����Ϣ");
				JButton btnUser = new JButton("�û���Ϣ");
				JButton btnBasItem = new JButton("��Ʒ��Ϣ");
				JButton btnBasLocation = new JButton("��λ��Ϣ");
				JButton btnContainer = new JButton("��������");
				// testFrm
				b1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (testfrm == null) {
							if (!testFrm.getOpenStatus()) {
								testfrm = testFrm.getInstance();
								testfrm.setClosable(true);
								testfrm.setResizable(true);
								testfrm.setVisible(true);
								desktopPane.add(testfrm);
								try {
									testfrm.setMaximum(true);
								} catch (PropertyVetoException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								try {
									testfrm.setSelected(true);
									testfrm = null;
								} catch (java.beans.PropertyVetoException ex) {
									System.out.println("Exception while selecting");
								}
							} else {
								testfrm = testFrm.getInstance();
								testfrm.moveToFront();
								testfrm = null;
							}
						}
					}
				});

				// storerMasterFrm
				btnStorer.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(!comData.getUserMenuPower("������Ϣ")){
							Message.showWarningMessage("�޴˹���Ȩ��");
							return;
						}
						if (storerFrm == null) {
							if (!storerMasterFrm.getOpenStatus()) {
								storerFrm = storerMasterFrm.getInstance();
								storerFrm.setClosable(true);
								storerFrm.setResizable(true);
								storerFrm.setVisible(true);
								desktopPane.add(storerFrm);
								try {
									storerFrm.setMaximum(true);
									storerFrm.setSelected(true);
									storerFrm = null;
									frame.setTitle("AGG WMS ��������Ϣ��");
								} catch (java.beans.PropertyVetoException ex) {
									System.out.println("Exception while selecting");
								}
							} else {
								storerFrm = storerMasterFrm.getInstance();
								storerFrm.moveToFront();
								storerFrm = null;
								frame.setTitle("AGG WMS ��������Ϣ��");
							}
						}
					}
				});
				btnBrand.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(!comData.getUserMenuPower("Ʒ����Ϣ")){
							Message.showWarningMessage("�޴˹���Ȩ��");
							return;
						}
						if (basBrandFrm == null) {
							if (!BasBrandFrm.getOpenStatus()) {
								basBrandFrm = basBrandFrm.getInstance();
								basBrandFrm.setClosable(true);
								basBrandFrm.setResizable(true);
								basBrandFrm.setVisible(true);
								desktopPane.add(basBrandFrm);
								try {
									basBrandFrm.setMaximum(true);
									basBrandFrm.setSelected(true);
									basBrandFrm = null;
									frame.setTitle("AGG WMS ��Ʒ����Ϣ��");
								} catch (java.beans.PropertyVetoException ex) {
									System.out.println("Exception while selecting");
								}
							} else {
								basBrandFrm = BasBrandFrm.getInstance();
								basBrandFrm.moveToFront();
								basBrandFrm = null;
								frame.setTitle("AGG WMS ��Ʒ����Ϣ��");
							}
						}
					}
				});
				// UserFrm
				btnUser.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(!comData.getUserMenuPower("�û���Ϣ")){
							Message.showWarningMessage("�޴˹���Ȩ��");
							return;
						}
						if (userfrm == null) {
							if (!UserFrm.getOpenStatus()) {
								userfrm = UserFrm.getInstance();
								userfrm.setClosable(true);
								userfrm.setResizable(true);
								userfrm.setVisible(true);
								desktopPane.add(userfrm);
								try {
									userfrm.setMaximum(true);
									userfrm.setSelected(true);
									userfrm = null;
									frame.setTitle("AGG WMS ���û���Ϣ��");
								} catch (java.beans.PropertyVetoException ex) {
									System.out.println("Exception while selecting");
								}
							} else {
								userfrm = UserFrm.getInstance();
								userfrm.moveToFront();
								userfrm = null;
								frame.setTitle("AGG WMS ���û���Ϣ��");
							}
						}
					}
				});
				
				//��Ʒ��Ϣ
				btnBasItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if(!comData.getUserMenuPower("��Ʒ��Ϣ")){
							Message.showWarningMessage("�޴˹���Ȩ��");
							return;
						}
						if(basItemFrm == null){
							if(!basItemFrm.getOpenStatus()){
								basItemFrm= BasItemFrm.getInstance();
								basItemFrm.setClosable(true);
								basItemFrm.setResizable(true);
								basItemFrm.setVisible(true);
								desktopPane.add(basItemFrm);
								try {
									basItemFrm.setMaximum(true);
									basItemFrm.setSelected(true);
									basItemFrm = null;
									frame.setTitle("AGG WMS ����Ʒ��Ϣ��");
								} catch (java.beans.PropertyVetoException ex) {
									System.out.println("Exception while selecting");
								}
							}else {
								basItemFrm = BasItemFrm.getInstance();
								basItemFrm.moveToFront();
								basItemFrm = null;
								frame.setTitle("AGG WMS ����Ʒ��Ϣ��");
							}
						}
					}
				});
				//��λ��Ϣ
				btnBasLocation.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(!comData.getUserMenuPower("��λ��Ϣ")){
							Message.showWarningMessage("�޴˹���Ȩ��");
							return;
						}
						if(basLocationFrm == null){
							if(!basLocationFrm.getOpenStatus()){
								basLocationFrm= BasLocationFrm.getInstance();
								basLocationFrm.setClosable(true);
								basLocationFrm.setResizable(true);
								basLocationFrm.setVisible(true);
								desktopPane.add(basLocationFrm);
								try {
									basLocationFrm.setMaximum(true);
									basLocationFrm.setSelected(true);
									basLocationFrm = null;
									frame.setTitle("AGG WMS ����λ��Ϣ��");
								} catch (java.beans.PropertyVetoException ex) {
									System.out.println("Exception while selecting");
								}
							}else {
								basLocationFrm = BasLocationFrm.getInstance();
								basLocationFrm.moveToFront();
								basLocationFrm = null;
								frame.setTitle("AGG WMS ����λ��Ϣ��");
							}
						}	
					}
				});
				
				btnContainer.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(!comData.getUserMenuPower("��������")){
							Message.showWarningMessage("�޴˹���Ȩ��");
							return;
						}
						if(bascontainerFrm == null){
							if(!bascontainerFrm.getOpenStatus()){
								bascontainerFrm= BasContainerFrm.getInstance();
								bascontainerFrm.setClosable(true);
								bascontainerFrm.setResizable(true);
								bascontainerFrm.setVisible(true);
								desktopPane.add(bascontainerFrm);
								try {
									bascontainerFrm.setMaximum(true);
									bascontainerFrm.setSelected(true);
									bascontainerFrm = null;
									frame.setTitle("AGG WMS ����������");
								} catch (java.beans.PropertyVetoException ex) {
									System.out.println("Exception while selecting");
								}
							}else {
								bascontainerFrm = BasContainerFrm.getInstance();
								bascontainerFrm.moveToFront();
								bascontainerFrm = null;
								frame.setTitle("AGG WMS ����������");
							}
						}	
					}
				});
				
//				b1.setOpaque(false);
				btnStorer.setOpaque(false);
				btnUser.setOpaque(false);
				btnBasItem.setOpaque(false);
				btnBasLocation.setOpaque(false);
				btnBrand.setOpaque(false);
				btnContainer.setOpaque(false);
//				pnl.add(b1);
				pnl.add(btnStorer);
				pnl.add(btnBrand);
				pnl.add(btnUser);
				pnl.add(btnBasItem);
				pnl.add(btnBasLocation);
				pnl.add(btnContainer);
				pnl.setSize(new Dimension(0, 250));
				pnl.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
				return pnl;
			}
		});

		panelList.add(new AccordionPanel("�����ҵ") {
			POFrm pofrm = null;
			poImportFrm poimportfrm = null;
			inbScanFrm inbscanfrm = null;
			putawayFrm putawayfrm = null;
			private static final long serialVersionUID = 1L;

			public JPanel makePanel() {
				JPanel pnl = new JPanel(new GridLayout(0, 1));
				JButton b1 = new JButton("POά��");
				b1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(!comData.getUserMenuPower("POά��")){
							Message.showWarningMessage("�޴˹���Ȩ��");
							return;
						}
						if (pofrm == null) {
							if (!POFrm.getOpenStatus()) {
								pofrm = POFrm.getInstance();
								pofrm.setClosable(true);
								pofrm.setResizable(true);
								pofrm.setVisible(true);
								desktopPane.add(pofrm);
								try {
									pofrm.setMaximum(true);
									pofrm.setSelected(true);
									pofrm = null;
									frame.setTitle("AGG WMS ��POά����");
								} catch (java.beans.PropertyVetoException ex) {
									System.out.println("Exception while selecting");
								}
							} else {
								pofrm = POFrm.getInstance();
								pofrm.moveToFront();
								pofrm = null;
								frame.setTitle("AGG WMS ��POά����");
							}
						}
					}
				});
				b1.setOpaque(false);
				pnl.add(b1);
				JButton b2 = new JButton("PO����");
				b2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(!comData.getUserMenuPower("PO����")){
							Message.showWarningMessage("�޴˹���Ȩ��");
							return;
						}
						if (poimportfrm == null) {
							if (!poImportFrm.getOpenStatus()) {
								poimportfrm = poImportFrm.getInstance();
								poimportfrm.setClosable(true);
								poimportfrm.setResizable(true);
								poimportfrm.setVisible(true);
								desktopPane.add(poimportfrm);
								try {
									poimportfrm.setMaximum(true);
									poimportfrm.setSelected(true);
									poimportfrm = null;
									frame.setTitle("AGG WMS ��PO���롿");
								} catch (java.beans.PropertyVetoException ex) {
									System.out.println("Exception while selecting");
								}
							} else {
								poimportfrm = poImportFrm.getInstance();
								poimportfrm.moveToFront();
								poimportfrm = null;
								frame.setTitle("AGG WMS ��PO���롿");
							}
						}
					}
				});
				b2.setOpaque(false);
				pnl.add(b2);
				JButton b3 = new JButton("���ɨ��");
				b3.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(!comData.getUserMenuPower("���ɨ��")){
							Message.showWarningMessage("�޴˹���Ȩ��");
							return;
						}
						if (inbscanfrm == null) {
							if (!inbScanFrm.getOpenStatus()) {
								inbscanfrm = inbScanFrm.getInstance();
								inbscanfrm.setClosable(true);
								inbscanfrm.setResizable(true);
								inbscanfrm.setVisible(true);
								desktopPane.add(inbscanfrm);
								try {
									inbscanfrm.setMaximum(true);
									inbscanfrm.setSelected(true);
									inbscanfrm = null;
									frame.setTitle("AGG WMS �����ɨ�衿");
								} catch (java.beans.PropertyVetoException ex) {
									System.out.println("Exception while selecting");
								}
							} else {
								inbscanfrm = inbScanFrm.getInstance();
								inbscanfrm.moveToFront();
								inbscanfrm = null;
								frame.setTitle("AGG WMS �����ɨ�衿");
							}
						}
					}
				});
				b3.setOpaque(false);
				pnl.add(b3);
				JButton b4 = new JButton("����ϼ�");
				b4.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(!comData.getUserMenuPower("����ϼ�")){
							Message.showWarningMessage("�޴˹���Ȩ��");
							return;
						}
						if (putawayfrm == null) {
							if (!putawayFrm.getOpenStatus()) {
								putawayfrm = putawayFrm.getInstance();
								putawayfrm.setClosable(true);
								putawayfrm.setResizable(true);
								putawayfrm.setVisible(true);
								desktopPane.add(putawayfrm);
								try {
									putawayfrm.setMaximum(true);
									putawayfrm.setSelected(true);
									putawayfrm = null;
									frame.setTitle("AGG WMS ������ϼܡ�");
								} catch (java.beans.PropertyVetoException ex) {
									System.out.println("Exception while selecting");
								}
							} else {
								putawayfrm = putawayFrm.getInstance();
								putawayfrm.moveToFront();
								putawayfrm = null;
								frame.setTitle("AGG WMS ������ϼܡ�");
							}
						}
					}
				});
				b4.setOpaque(false);
				pnl.add(b4);
				pnl.setSize(new Dimension(0, 200));
				pnl.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
				return pnl;
			}
		});
		panelList.add(new AccordionPanel("������ҵ") {
			/** */
			/**
			* 
			*/
			private static final long serialVersionUID = 1L;
			TrackingNoScanFrm trackingNoScanFrm = null;
			ShipmentQueryFrm shipmentQueryFrm = null;
			ShipmentOubScan shipmentoubscanFrm = null;
			ShipmentOubCheck shipmentoubcheckFrm = null;

			public JPanel makePanel() {
				JPanel pnl = new JPanel(new GridLayout(0, 1));
				JButton btnTrackingScan = new JButton("�˵�ɨ��");
				btnTrackingScan.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(!comData.getUserMenuPower("�˵�ɨ��")){
							Message.showWarningMessage("�޴˹���Ȩ��");
							return;
						}
						if (trackingNoScanFrm == null) {
							if (!TrackingNoScanFrm.getOpenStatus()) {
								trackingNoScanFrm = TrackingNoScanFrm.getInstance();
								trackingNoScanFrm.setClosable(true);
								trackingNoScanFrm.setResizable(true);
								trackingNoScanFrm.setVisible(true);
								desktopPane.add(trackingNoScanFrm);
								try {
									trackingNoScanFrm.setMaximum(true);
									trackingNoScanFrm.setSelected(true);
									trackingNoScanFrm = null;
									frame.setTitle("AGG WMS ���˵�ɨ�衿");
								} catch (java.beans.PropertyVetoException ex) {
									System.out.println("Exception while selecting");
								}
							} else {
								trackingNoScanFrm = TrackingNoScanFrm.getInstance();
								trackingNoScanFrm.moveToFront();
								trackingNoScanFrm = null;
								frame.setTitle("AGG WMS ���˵�ɨ�衿");
							}
						}
					}
				});
				btnTrackingScan.setOpaque(false);
				pnl.add(btnTrackingScan);
				
				JButton btnShipmentOubScan = new JButton("�������");
				btnShipmentOubScan.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(!comData.getUserMenuPower("�������")){
							Message.showWarningMessage("�޴˹���Ȩ��");
							return;
						}
						if (shipmentoubscanFrm == null) {
							if (!ShipmentOubScan.getOpenStatus()) {
								shipmentoubscanFrm = ShipmentOubScan.getInstance();
								shipmentoubscanFrm.setClosable(true);
								shipmentoubscanFrm.setResizable(true);
								shipmentoubscanFrm.setVisible(true);
								desktopPane.add(shipmentoubscanFrm);
								try {
									shipmentoubscanFrm.setMaximum(true);
									shipmentoubscanFrm.setSelected(true);
									shipmentoubscanFrm = null;
									frame.setTitle("AGG WMS ��������ˡ�");
								} catch (java.beans.PropertyVetoException ex) {
									System.out.println("Exception while selecting");
								}
							} else {
								shipmentoubscanFrm = ShipmentOubScan.getInstance();
								shipmentoubscanFrm.moveToFront();
								shipmentoubscanFrm = null;
								frame.setTitle("AGG WMS ��������ˡ�");
							}
						}
					}
				});
				btnShipmentOubScan.setOpaque(false);
				pnl.add(btnShipmentOubScan);
				
				JButton btnShipmentOubCheck = new JButton("���⸴��");
				btnShipmentOubCheck.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(!comData.getUserMenuPower("���⸴��")){
							Message.showWarningMessage("�޴˹���Ȩ��");
							return;
						}
						if (shipmentoubcheckFrm == null) {
							if (!ShipmentOubCheck.getOpenStatus()) {
								shipmentoubcheckFrm = ShipmentOubCheck.getInstance();
								shipmentoubcheckFrm.setClosable(true);
								shipmentoubcheckFrm.setResizable(true);
								shipmentoubcheckFrm.setVisible(true);
								desktopPane.add(shipmentoubcheckFrm);
								try {
									shipmentoubcheckFrm.setMaximum(true);
									shipmentoubcheckFrm.setSelected(true);
									shipmentoubcheckFrm = null;
									frame.setTitle("AGG WMS �����⸴�ˡ�");
								} catch (java.beans.PropertyVetoException ex) {
									System.out.println("Exception while selecting");
								}
							} else {
								shipmentoubcheckFrm = ShipmentOubCheck.getInstance();
								shipmentoubcheckFrm.moveToFront();
								shipmentoubcheckFrm = null;
								frame.setTitle("AGG WMS �����⸴�ˡ�");
							}
						}
					}
				});
				btnShipmentOubCheck.setOpaque(false);
				pnl.add(btnShipmentOubCheck);
				
				JButton btnShipmentQuery = new JButton("������ѯ");
				btnShipmentQuery.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(!comData.getUserMenuPower("������ѯ")){
							Message.showWarningMessage("�޴˹���Ȩ��");
							return;
						}
						if (shipmentQueryFrm == null) {
							if (!ShipmentQueryFrm.getOpenStatus()) {
								shipmentQueryFrm = ShipmentQueryFrm.getInstance();
								shipmentQueryFrm.setClosable(true);
								shipmentQueryFrm.setResizable(true);
								shipmentQueryFrm.setVisible(true);
								desktopPane.add(shipmentQueryFrm);
								try {
									shipmentQueryFrm.setMaximum(true);
									shipmentQueryFrm.setSelected(true);
									shipmentQueryFrm = null;
									frame.setTitle("AGG WMS ��������ѯ��");
								} catch (java.beans.PropertyVetoException ex) {
									System.out.println("Exception while selecting");
								}
							} else {
								shipmentQueryFrm = ShipmentQueryFrm.getInstance();
								shipmentQueryFrm.moveToFront();
								shipmentQueryFrm = null;
								frame.setTitle("AGG WMS ��������ѯ��");
							}
						}
					}
				});
				btnShipmentQuery.setOpaque(false);
				pnl.add(btnShipmentQuery);
				
				pnl.setSize(new Dimension(0, 200));
				pnl.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
				return pnl;
				
				
			}
		});
		panelList.add(new AccordionPanel("���ڹ���") {
			private static final long serialVersionUID = 1L;
			InvQueryFrm invqueryFrm = null;
			InvMoveFrm invmoveFrm = null;
			StockTakeFrm stocktakeFrm = null;
			StockTakeQueryFrm stocktakequeryFrm = null;
			InvTransferFrm invTransferFrm = null;

			public JPanel makePanel() {
				JPanel pnl = new JPanel(new GridLayout(0, 1));
				
				JButton btnStockTake = new JButton("�̵���ҵ");
				btnStockTake.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(!comData.getUserMenuPower("�̵���ҵ")){
							Message.showWarningMessage("�޴˹���Ȩ��");
							return;
						}
						if (stocktakeFrm == null) {
							if (!StockTakeFrm.getOpenStatus()) {
								stocktakeFrm = StockTakeFrm.getInstance();
								stocktakeFrm.setClosable(true);
								stocktakeFrm.setResizable(true);
								stocktakeFrm.setVisible(true);
								desktopPane.add(stocktakeFrm);
								try {
									stocktakeFrm.setMaximum(true);
									stocktakeFrm.setSelected(true);
									stocktakeFrm = null;
									frame.setTitle("AGG WMS ���̵���ҵ��");
								} catch (java.beans.PropertyVetoException ex) {
									System.out.println("Exception while selecting");
								}
							} else {
								stocktakeFrm = StockTakeFrm.getInstance();
								stocktakeFrm.moveToFront();
								stocktakeFrm = null;
								frame.setTitle("AGG WMS ���̵���ҵ��");
							}
						}
					}
				});
				btnStockTake.setOpaque(false);
				pnl.add(btnStockTake);
				
				JButton btnStockTakeQuery = new JButton("�̵��ѯ");
				btnStockTakeQuery.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(!comData.getUserMenuPower("�̵��ѯ")){
							Message.showWarningMessage("�޴˹���Ȩ��");
							return;
						}
						if (stocktakequeryFrm == null) {
							if (!StockTakeQueryFrm.getOpenStatus()) {
								stocktakequeryFrm = StockTakeQueryFrm.getInstance();
								stocktakequeryFrm.setClosable(true);
								stocktakequeryFrm.setResizable(true);
								stocktakequeryFrm.setVisible(true);
								desktopPane.add(stocktakequeryFrm);
								try {
									stocktakequeryFrm.setMaximum(true);
									stocktakequeryFrm.setSelected(true);
									stocktakequeryFrm = null;
									frame.setTitle("AGG WMS ���̵��ѯ��");
								} catch (java.beans.PropertyVetoException ex) {
									System.out.println("Exception while selecting");
								}
							} else {
								stocktakequeryFrm = StockTakeQueryFrm.getInstance();
								stocktakequeryFrm.moveToFront();
								stocktakequeryFrm = null;
								frame.setTitle("AGG WMS ���̵��ѯ��");
							}
						}
					}
				});
				btnStockTakeQuery.setOpaque(false);
				pnl.add(btnStockTakeQuery);
				
				JButton btnInvMove = new JButton("�ƿ����");
				btnInvMove.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(!comData.getUserMenuPower("�ƿ����")){
							Message.showWarningMessage("�޴˹���Ȩ��");
							return;
						}
						if (invmoveFrm == null) {
							if (!InvMoveFrm.getOpenStatus()) {
								invmoveFrm = InvMoveFrm.getInstance();
								invmoveFrm.setClosable(true);
								invmoveFrm.setResizable(true);
								invmoveFrm.setVisible(true);
								desktopPane.add(invmoveFrm);
								try {
									invmoveFrm.setMaximum(true);
									invmoveFrm.setSelected(true);
									invmoveFrm = null;
									frame.setTitle("AGG WMS ���ƿ����");
								} catch (java.beans.PropertyVetoException ex) {
									System.out.println("Exception while selecting");
								}
							} else {
								invmoveFrm = InvMoveFrm.getInstance();
								invmoveFrm.moveToFront();
								invmoveFrm = null;
								frame.setTitle("AGG WMS ���ƿ����");
							}
						}
					}
				});
				btnInvMove.setOpaque(false);
				pnl.add(btnInvMove);
				
				JButton btnInvQuery = new JButton("����ѯ");
				btnInvQuery.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(!comData.getUserMenuPower("����ѯ")){
							Message.showWarningMessage("�޴˹���Ȩ��");
							return;
						}
						if (invqueryFrm == null) {
							if (!InvQueryFrm.getOpenStatus()) {
								invqueryFrm = InvQueryFrm.getInstance();
								invqueryFrm.setClosable(true);
								invqueryFrm.setResizable(true);
								invqueryFrm.setVisible(true);
								desktopPane.add(invqueryFrm);
								try {
									invqueryFrm.setMaximum(true);
									invqueryFrm.setSelected(true);
									invqueryFrm = null;
									frame.setTitle("AGG WMS ������ѯ��");
								} catch (java.beans.PropertyVetoException ex) {
									System.out.println("Exception while selecting");
								}
							} else {
								invqueryFrm = InvQueryFrm.getInstance();
								invqueryFrm.moveToFront();
								invqueryFrm = null;
								frame.setTitle("AGG WMS ������ѯ��");
							}
						}
					}
				});
				btnInvQuery.setOpaque(false);
				pnl.add(btnInvQuery);
				
				JButton btnInvTransfer = new JButton("������Ա��");
				btnInvTransfer.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(!comData.getUserMenuPower("������Ա��")){
							Message.showWarningMessage("�޴˹���Ȩ��");
							return;
						}
						if (invTransferFrm == null) {
							if (!InvTransferFrm.getOpenStatus()) {
								invTransferFrm = InvTransferFrm.getInstance();
								invTransferFrm.setClosable(true);
								invTransferFrm.setResizable(true);
								invTransferFrm.setVisible(true);
								desktopPane.add(invTransferFrm);
								try {
									invTransferFrm.setMaximum(true);
									invTransferFrm.setSelected(true);
									invTransferFrm = null;
									frame.setTitle("AGG WMS ��������Ա����");
								} catch (java.beans.PropertyVetoException ex) {
									System.out.println("Exception while selecting");
								}
							} else {
								invTransferFrm = InvTransferFrm.getInstance();
								invTransferFrm.moveToFront();
								invTransferFrm = null;
								frame.setTitle("AGG WMS ��������Ա����");
							}
						}
					}
				});
				btnInvTransfer.setOpaque(false);
				pnl.add(btnInvTransfer);
				
				
				pnl.setSize(new Dimension(0, 250));
				pnl.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
				return pnl;
			}
		});
		return panelList;
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				createUI();
			}
		});
	}

	public static void createUI() {
		frame = new JFrame("AGG WMS");
//		loadFrameFont();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().add(new MainFrm());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
//		frame.setIcon(new ImageIcon(MainFrm.class.getResource("/images/aggwms.png")));
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		int x = (int)(toolkit.getScreenSize().getWidth());
		int y = (int)(toolkit.getScreenSize().getHeight());
		frame.setLocation(0, 0);
		frame.setResizable(true);
		frame.setMinimumSize(new Dimension(x,y));
		frame.setMaximumSize(new Dimension(x,y));
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(loginFrm.class.getResource("/images/logo.png")));
	}
	
	private static void loadFrameFont(){
		try {
			final int SystemFontSize = 12;
			final int DataFontSize = 12;
			final Font SystemFont = new Font("΢���ź�", Font.PLAIN, SystemFontSize);
			final Font DataFont = new Font("simsun", Font.PLAIN, DataFontSize);
			// ����ͨ���޸�����Ĵ���,�任���ڵķ��
			// ��������۷����,����ͨ�������ⲿJAR����ʵ��
			// �������͵�LOOK&FEEL��
			// LookAndFeel alloyLnF = new
			// com.incors.plaf.alloy.AlloyLookAndFeel();
			// UIManager.getSystemLookAndFeelClassName();
			JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);
			javax.swing.plaf.FontUIResource fontResource = new javax.swing.plaf.FontUIResource(SystemFont);
			javax.swing.plaf.FontUIResource datafontResource = new javax.swing.plaf.FontUIResource(DataFont);
			// UI������֮�������
			UIManager.put("Button.font", fontResource);
			UIManager.put("ToggleButton.font", fontResource);
			UIManager.put("RadioButton.font", fontResource);
			UIManager.put("CheckBox.font", fontResource);
			UIManager.put("ColorChooser.font", fontResource);
			UIManager.put("ToggleButton.font", fontResource);
			UIManager.put("ComboBox.font", fontResource);
			UIManager.put("ComboBoxItem.font", fontResource);
			UIManager.put("InternalFrame.titleFont", fontResource);
			UIManager.put("Label.font", fontResource);
			UIManager.put("List.font", datafontResource);
			UIManager.put("MenuBar.font", fontResource);
			UIManager.put("Menu.font", fontResource);
			UIManager.put("MenuItem.font", fontResource);
			UIManager.put("RadioButtonMenuItem.font", fontResource);
			UIManager.put("CheckBoxMenuItem.font", fontResource);
			UIManager.put("PopupMenu.font", fontResource);
			UIManager.put("OptionPane.font", fontResource);
			UIManager.put("Panel.font", fontResource);
			UIManager.put("ProgressBar.font", fontResource);
			UIManager.put("ScrollPane.font", fontResource);
			UIManager.put("Viewport", fontResource);
			UIManager.put("TabbedPane.font", fontResource);
			UIManager.put("TableHeader.font", fontResource);
			UIManager.put("TextField.font", datafontResource);
			UIManager.put("PasswordFiled.font", datafontResource);
			UIManager.put("TextArea.font", datafontResource);
			UIManager.put("TextPane.font", datafontResource);
			UIManager.put("EditorPane.font", fontResource);
			UIManager.put("TitledBorder.font", fontResource);
			UIManager.put("ToolBar.font", fontResource);
			UIManager.put("ToolTip.font", fontResource);
			UIManager.put("Tree.font", datafontResource);
			UIManager.put("TabbedPane.font", datafontResource);
			UIManager.put("ComboBox.font", datafontResource);
			UIManager.put("ProgressBar.repaintInterval", new Integer(150));
			UIManager.put("ProgressBar.cycleTime", new Integer(1050));
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			System.out.println("UIManager �쳣 \r\n" + ex.toString());
		}
		
	}

}

class accordionEvent extends java.util.EventObject {
	/** */
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	public accordionEvent(Object source) {
		super(source);
	}
}

interface accordionListener {
	public void accordionStateChanged(accordionEvent e);
}

abstract class AccordionPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	abstract public JPanel makePanel();

	private final String _title;
	private final JLabel label;
	private final JPanel panel;
	private boolean openFlag = false;

	public AccordionPanel(String title) {
		super(new BorderLayout());
		_title = title;
		label = new JLabel("�� " + title) {
			/** */
			/**
			* 
			*/
			private static final long serialVersionUID = 1L;

			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				// ���ƽ���
				g2.setPaint(new GradientPaint(50, 0, Color.WHITE, getWidth(), getHeight(), new Color(199, 212, 247)));
				g2.fillRect(0, 0, getWidth(), getHeight());
				super.paintComponent(g);
			}
		};
		label.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				openFlag = !openFlag;
				initPanel();
				fireaccordionEvent();
			}
		});
		label.setForeground(new Color(33, 93, 198));
		label.setFont(new Font("����", 1, 20));
		label.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 2));
		panel = makePanel();
		panel.setOpaque(true);
		Border outBorder = BorderFactory.createMatteBorder(0, 2, 2, 2, Color.WHITE);
		Border inBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		Border border = BorderFactory.createCompoundBorder(outBorder, inBorder);
		panel.setBorder(border);
		panel.setBackground(new Color(240, 240, 255));
		add(label, BorderLayout.NORTH);
	}

	public boolean isSelected() {
		return openFlag;
	}

	public void setSelected(boolean flg) {
		openFlag = flg;
		initPanel();
	}

	protected void initPanel() {
		if (isSelected()) {
			label.setText("�� " + _title);
			add(panel, BorderLayout.CENTER);
			setPreferredSize(new Dimension(getSize().width, label.getSize().height + panel.getSize().height));
		} else {
			label.setText("�� " + _title);
			remove(panel);
			setPreferredSize(new Dimension(getSize().width, label.getSize().height));
		}
		revalidate();
	}

	protected ArrayList<accordionListener> accordionListenerList = new ArrayList<accordionListener>();

	public void addaccordionListener(accordionListener listener) {
		if (!accordionListenerList.contains(listener))
			accordionListenerList.add(listener);
	}

	public void removeaccordionListener(accordionListener listener) {
		accordionListenerList.remove(listener);
	}

	public void fireaccordionEvent() {
		@SuppressWarnings("unchecked")
		List<accordionListener> list = (List<accordionListener>) accordionListenerList.clone();
		Iterator<accordionListener> it = list.iterator();
		accordionEvent e = new accordionEvent(this);
		while (it.hasNext()) {
			accordionListener listener = (accordionListener) it.next();
			listener.accordionStateChanged(e);
		}
	}
}
