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
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
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
import bas.BasItemMaterialFrm;
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
import main.PBSUIBaseGrid;
import outbound.ShipmentInputFrm;
import outbound.ShipmentOubCheck;
import outbound.ShipmentOubScan;
import outbound.ShipmentQueryFrm;
import outbound.StockTakeFrm;
import outbound.StockTakeQueryFrm;
import outbound.TrackingNoScanFrm;
import util.WaitingSplash;

import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.JToolBar;
import java.awt.SystemColor;

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
 * @author @email：
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
	// 分割窗体
	private final JSplitPane split = new JSplitPane();
	private final JScrollPane leftScroll;
	private static ImageIcon ico=new ImageIcon("images/aggwms.jpg");
	private final static JDesktopPane desktopPane = new JDesktopPane();
	private static DataManager userInfo = new DataManager();
	private static DataManager versionInfo = new DataManager();
	public static String wmsVersion = "1.55";
	private final JLabel lblNewLabel = new JLabel("");
	private final JToolBar toolBar = new JToolBar();
	private final JLabel lblNewLabel_1 = new JLabel("\u767B\u5F55\u8D26\u6237\uFF1A");
	private final JLabel lbl_user_id = new JLabel("");
	private final JLabel lbl_systime = new JLabel("");
	private final JLabel lblNewLabel_2 = new JLabel(" | \u5F53\u524D\u65F6\u95F4\uFF1A");
	private final static JLabel lbl_mouse = new JLabel("");
	private final JLabel lblNewLabel_3 = new JLabel(" | \u4ED3\u5E93\uFF1A");
	private final JLabel lbl_warehouse = new JLabel("");
	
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
	
	public static String getCurrentDate() {
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sm.format(new Date());
    }
	
	public void refreshCurrentTime(){
		new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
            	while(true){
            		lbl_systime.setText(getCurrentDate());
            		lbl_warehouse.setText(MainFrm.getUserInfo().getString("CUR_WAREHOUSE_NAME", 0));
            		Thread.sleep(1000);
            	}
            }

            @Override
            protected void done() {
            }
        }.execute();
	}

	// 折叠效果
	public MainFrm() {
		super(new BorderLayout());
		leftPanel.setOpaque(true);
		leftPanel.setBackground(new Color(116, 149, 226));
		// 滚动条
		leftScroll = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		leftScroll.getVerticalScrollBar().setUnitIncrement(10);
		leftScroll.setViewportView(leftPanel);
		// 构建数据列表
		List<AccordionPanel> panelList = makeList();
		// 设定监听
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
		// 加载滚动条监听
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
		lbl_user_id.setText(MainFrm.getUserInfo().getString("USER_NAME", 0));
		desktopPane.add(lblNewLabel, BorderLayout.CENTER);
//		setContainerMouseListener(desktopPane);
		
		add(toolBar, BorderLayout.SOUTH);
		
		toolBar.add(lblNewLabel_1);
		
		toolBar.add(lbl_user_id);
		
		toolBar.add(lblNewLabel_2);
		lbl_systime.setForeground(SystemColor.textHighlight);
		
		toolBar.add(lbl_systime);
		
		toolBar.add(lbl_mouse);
		
		toolBar.add(lblNewLabel_3);
		
		toolBar.add(lbl_warehouse);
		refreshCurrentTime();
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
		panelList.add(new AccordionPanel("基础资料") {
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
							e1.printStackTrace();
						}
					}
				});

				JButton btnStorer = new JButton("货主信息");
				JButton btnBrand = new JButton("品牌信息");
				JButton btnUser = new JButton("用户信息");
				JButton btnBasItem = new JButton("货品信息");
				JButton btnBasItemMaterial = new JButton("货品辅料信息");
				JButton btnBasLocation = new JButton("库位信息");
				JButton btnContainer = new JButton("容器管理");

				// storerMasterFrm
				btnStorer.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						initButtonMenu(storerMasterFrm.getInstance(),"AGG WMS 【货主信息】");
					}
				});
				btnBrand.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						initButtonMenu(BasBrandFrm.getInstance(),"AGG WMS 【品牌信息】");
					}
				});
				// UserFrm
				btnUser.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						initButtonMenu(UserFrm.getInstance(),"AGG WMS 【用户信息】");
					}
				});
				
				//货品信息
				btnBasItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						initButtonMenu(BasItemFrm.getInstance(),"AGG WMS 【货品信息】");
					}
				});
				//货品辅料信息
				btnBasItemMaterial.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						initButtonMenu(BasItemMaterialFrm.getInstance(),"AGG WMS 【货品辅料信息】");
					}
				});
				//库位信息
				btnBasLocation.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						initButtonMenu(BasLocationFrm.getInstance(),"AGG WMS 【库位信息】");
					}
				});
				
				btnContainer.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						initButtonMenu(BasContainerFrm.getInstance(),"AGG WMS 【容器管理】");	
					}
				});
				
				btnStorer.setOpaque(false);
				btnUser.setOpaque(false);
				btnBasItem.setOpaque(false);
				btnBasItemMaterial.setOpaque(false);
				btnBasLocation.setOpaque(false);
				btnBrand.setOpaque(false);
				btnContainer.setOpaque(false);
				pnl.add(btnStorer);
				pnl.add(btnBrand);
				pnl.add(btnUser);
				pnl.add(btnBasItem);
				pnl.add(btnBasItemMaterial);
				pnl.add(btnBasLocation);
				pnl.add(btnContainer);
				pnl.setSize(new Dimension(0, 350));
				pnl.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
				return pnl;
			}
		});

		panelList.add(new AccordionPanel("入库作业") {
			private static final long serialVersionUID = 1L;

			public JPanel makePanel() {
				JPanel pnl = new JPanel(new GridLayout(0, 1));
				JButton b1 = new JButton("PO维护");
				b1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						initButtonMenu(POFrm.getInstance(),"AGG WMS 【PO维护】");
					}
				});
				b1.setOpaque(false);
				pnl.add(b1);
				JButton b2 = new JButton("PO导入");
				b2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						initButtonMenu(poImportFrm.getInstance(),"AGG WMS 【PO导入】");
					}
				});
				b2.setOpaque(false);
				pnl.add(b2);
				JButton b3 = new JButton("入库扫描");
				b3.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						initButtonMenu(inbScanFrm.getInstance(),"AGG WMS 【入库扫描】");
					}
				});
				b3.setOpaque(false);
				pnl.add(b3);
				JButton b4 = new JButton("入库上架");
				b4.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						initButtonMenu(putawayFrm.getInstance(),"AGG WMS 【入库上架】");
					}
				});
				b4.setOpaque(false);
				pnl.add(b4);
				pnl.setSize(new Dimension(0, 200));
				pnl.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
				return pnl;
			}
		});
		panelList.add(new AccordionPanel("出库作业") {
			/** */
			/**
			* 
			*/
			private static final long serialVersionUID = 1L;

			public JPanel makePanel() {
				JPanel pnl = new JPanel(new GridLayout(0, 1));
				
				JButton btnShipmentInput = new JButton("订单管理");
				btnShipmentInput.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						initButtonMenu(ShipmentInputFrm.getInstance(),"AGG WMS 【订单管理】");
					}
				});
				btnShipmentInput.setOpaque(false);
				pnl.add(btnShipmentInput);
				
				JButton btnTrackingScan = new JButton("运单扫描");
				btnTrackingScan.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						initButtonMenu(TrackingNoScanFrm.getInstance(),"AGG WMS 【运单扫描】");
					}
				});
				btnTrackingScan.setOpaque(false);
				pnl.add(btnTrackingScan);
				
				JButton btnShipmentOubScan = new JButton("拣货复核");
				btnShipmentOubScan.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						initButtonMenu(ShipmentOubScan.getInstance(),"AGG WMS 【拣货复核】");
					}
				});
				btnShipmentOubScan.setOpaque(false);
				pnl.add(btnShipmentOubScan);
				
				JButton btnShipmentOubCheck = new JButton("出库复核");
				btnShipmentOubCheck.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						initButtonMenu(ShipmentOubCheck.getInstance(),"AGG WMS 【出库复核】");
					}
				});
				btnShipmentOubCheck.setOpaque(false);
				pnl.add(btnShipmentOubCheck);
				
				JButton btnShipmentQuery = new JButton("订单查询");
				btnShipmentQuery.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						initButtonMenu(ShipmentQueryFrm.getInstance(),"AGG WMS 【订单查询】");
					}
				});
				btnShipmentQuery.setOpaque(false);
				pnl.add(btnShipmentQuery);
				
				pnl.setSize(new Dimension(0, 250));
				pnl.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
				return pnl;
				
				
			}
		});
		panelList.add(new AccordionPanel("库内管理") {
			private static final long serialVersionUID = 1L;

			public JPanel makePanel() {
				JPanel pnl = new JPanel(new GridLayout(0, 1));
				
				JButton btnStockTake = new JButton("盘点作业");
				btnStockTake.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						initButtonMenu(StockTakeFrm.getInstance(),"AGG WMS 【盘点作业】");
					}
				});
				btnStockTake.setOpaque(false);
				pnl.add(btnStockTake);
				
				JButton btnStockTakeQuery = new JButton("盘点查询");
				btnStockTakeQuery.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						initButtonMenu(StockTakeQueryFrm.getInstance(),"AGG WMS 【盘点查询】");
					}
				});
				btnStockTakeQuery.setOpaque(false);
				pnl.add(btnStockTakeQuery);
				
				JButton btnInvMove = new JButton("移库管理");
				btnInvMove.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						initButtonMenu(InvMoveFrm.getInstance(),"AGG WMS 【移库管理】");
					}
				});
				btnInvMove.setOpaque(false);
				pnl.add(btnInvMove);
				
				JButton btnInvQuery = new JButton("库存查询");
				btnInvQuery.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						initButtonMenu(InvQueryFrm.getInstance(),"AGG WMS 【库存查询】");
					}
				});
				btnInvQuery.setOpaque(false);
				pnl.add(btnInvQuery);
				
				JButton btnInvTransfer = new JButton("库存属性变更");
				btnInvTransfer.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						initButtonMenu(InvTransferFrm.getInstance(),"AGG WMS 【库存属性变更】");
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
	
	public static void initButtonMenu(InnerFrame innerframe, String menuTitle) {
		if(!comData.getUserMenuPower(menuTitle.substring(menuTitle.indexOf("【")+1,menuTitle.indexOf("】")))){
			Message.showWarningMessage("无此功能权限");
			return;
		}
		System.out.println(innerframe.toString());
		innerframe.setClosable(true);
		innerframe.setResizable(true);
		innerframe.setVisible(true);
		try {
			desktopPane.add(innerframe);
			innerframe.setMaximum(true);
			innerframe.setSelected(true);
			frame.setTitle(menuTitle);
		} catch (Exception ex) {
			try {
				desktopPane.add(innerframe);
				innerframe.moveToFront();
				innerframe.setMaximum(true);
				innerframe.setSelected(true);
			} catch (PropertyVetoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			frame.setTitle(menuTitle);
		}
//		setContainerMouseListener(innerframe);
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
//		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int t = JOptionPane.showConfirmDialog(null, "是否退出系统");
				if(t==0){
					System.exit(0);
				}
			}
		});
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
	
	public static void setContainerMouseListener(Container container){
		// --------设置组建事件--------
        setMouseListener(container);
		// 获取当前面板
//        Container contentPanel = frame.getContentPane();
        // 获取当前面板中的所有组件
        Component[] comps = container.getComponents();
        for (Component c : comps) {
        	System.out.println(c.toString());
            setMouseListener(c);
            // 如果当前面板中含有JPanel组件
            if (c instanceof JPanel) {
                Component[] compsInPanel = ((JPanel) c).getComponents();
                for (Component cc : compsInPanel) {
                    setMouseListener(cc);
                }
            }else if(c instanceof JInternalFrame) {
                Component[] compsInPanel = ((JInternalFrame) c).getComponents();
                for (Component cc : compsInPanel) {
                    setMouseListener(cc);
                }
            }else if(c instanceof JDesktopPane) {
                Component[] compsInPanel = ((JDesktopPane) c).getComponents();
                for (Component cc : compsInPanel) {
                    setMouseListener(cc);
                }
            }else if(c instanceof JRootPane) {
                Component[] compsInPanel = ((JRootPane) c).getComponents();
                for (Component cc : compsInPanel) {
                    setMouseListener(cc);
                }
            }
        }
	}
	
	/**
     * 设置组件事件
     * 
     * @param c
     */
    public static void setMouseListener(Component c) {
    	c.addMouseMotionListener(new MouseMotionListener(){
    		int x=100,y=100;
			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				x=e.getX();
				y=e.getY();
				lbl_mouse.setText(x+"/"+y);
			}
    		
    	});
    }
	
	private static void loadFrameFont(){
		try {
			final int SystemFontSize = 12;
			final int DataFontSize = 12;
			final Font SystemFont = new Font("微软雅黑", Font.PLAIN, SystemFontSize);
			final Font DataFont = new Font("simsun", Font.PLAIN, DataFontSize);
			// 可以通过修改下面的代码,变换窗口的风格
			// 其它的外观风格类,可以通过引入外部JAR包来实现
			// 其它类型的LOOK&FEEL包
			// LookAndFeel alloyLnF = new
			// com.incors.plaf.alloy.AlloyLookAndFeel();
			// UIManager.getSystemLookAndFeelClassName();
			JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);
			javax.swing.plaf.FontUIResource fontResource = new javax.swing.plaf.FontUIResource(SystemFont);
			javax.swing.plaf.FontUIResource datafontResource = new javax.swing.plaf.FontUIResource(DataFont);
			// UI管理器之字体管理
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
			System.out.println("UIManager 异常 \r\n" + ex.toString());
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
		label = new JLabel("↓ " + title) {
			/** */
			/**
			* 
			*/
			private static final long serialVersionUID = 1L;

			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				// 绘制渐变
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
		label.setFont(new Font("宋体", 1, 20));
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
			label.setText("↑ " + _title);
			add(panel, BorderLayout.CENTER);
			setPreferredSize(new Dimension(getSize().width, label.getSize().height + panel.getSize().height));
		} else {
			label.setText("↓ " + _title);
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
