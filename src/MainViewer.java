import java.applet.Applet;
import java.awt.CardLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Point;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class MainViewer  extends  Applet implements WindowListener{
	 public static java.applet.Applet refApplet;

    private boolean Debug = true;
	 Container vncContainer;
	 //Panel mainFrame ;
    JPanel mainFrame;
    //ScrollPane mainFrame;
    GridBagLayout gridbag;
	 Point windowSize = new Point(800, 480);
	 Vector<IPUtil.VNCBean> terminalList = new Vector<IPUtil.VNCBean>();
	 IPUtil ipUtil ;
	 private Frame mFrame = null;
	 
	  public static void main(String[] argv) {
          String model = "AK-070AE";
            System.out.println("model leghth:"+model.length());
		  JFrame frame = new JFrame("显控监控软件");
		 
		  MainViewer mainViewer = new MainViewer();
		  mainViewer.mFrame = frame;
		  mainViewer.init();
		  mainViewer.start();

          mainViewer.gridbag = new GridBagLayout();
          GridBagConstraints gbc = new GridBagConstraints();
          gbc.gridwidth = GridBagConstraints.REMAINDER;
          gbc.anchor = GridBagConstraints.NORTHWEST;
          gbc.weightx = 1.0;
          gbc.weighty = 1.0;
          ScrollPane desktopScrollPane = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
          desktopScrollPane.add(mainViewer);
          mainViewer.mFrame.setLayout(mainViewer.gridbag);
          gbc.fill = GridBagConstraints.BOTH;
          mainViewer.gridbag.setConstraints(desktopScrollPane, gbc);
          mainViewer.mFrame.add(desktopScrollPane);
          mainViewer.mFrame.setVisible(true);
          mainViewer.mFrame.setBackground(new Color(173, 216, 230));
          mainViewer.mFrame.pack();
          mainViewer.mFrame.invalidate();

	 }
	 
	  
	  public void init( ){
		  
		  vncContainer = this;

		  refApplet = this;
		   ipUtil = new  IPUtil();
		   ipUtil.addNetListener(new NetScan());

		  vncContainer.setLayout(null);
		  vncContainer.add(getSelectPanel());
		  vncContainer.setSize(600, 450);
		  vncContainer.setBackground(new Color(173, 216, 230));

		  if(mFrame != null){
              mFrame.addWindowListener(this);
			  mFrame.pack();
			  mFrame.setSize(620, 500);
			  mFrame.invalidate();
		  }
	  }

    public void scanEnd() {
        System.out.println("MainViewer scanEnd!!!");
        scanTip.setVisible(true);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (terminalList.size() > 0) {
            String info = "扫描到 " + terminalList.size() + " 个设备";
            scanTip.setText("扫描结束！"+info);
        } else {
            showTeminalView();
        }
        System.out.println("====================================scanEnd!!!");
        scanButton.setEnabled(true);
        addButton.setEnabled(true);

    }

	  
	  private class NetScan implements Observer{
		  
		@Override
		public void update(Observable o, Object arg) {
			// TODO Auto-generated method stub
            System.out.println("NetScan update!!!");
			for(IPUtil.VNCBean bean: ipUtil.getTeminalList()){
				addterminalList(bean);
			}
			

			windowSize = ipUtil.getWindowSize(terminalList);
			showTeminalView();
		}
		  
	  }
	  
	  private void showTeminalView(){
		  if(terminalList.size() > 0){
				//scanTip.setVisible(false);
				listTip.setVisible(true);
				listPane.setVisible(true);
				listPane.removeAll();
				selectList.clear();
				
				JPanel panel = new JPanel();
				panel.setBackground(Color.LIGHT_GRAY);
				panel.setLayout(new GridLayout(0, 1));
				for(IPUtil.VNCBean bean : terminalList){
					String label = " "+bean.host +"   " + bean.model;
					Checkbox box = new Checkbox(label);
					box.setFont(new Font("宋体", Font.PLAIN, 12));
					box.addItemListener(mItemListener);
					box.setName(bean.host);
					panel.add(box);
				}
				listPane.add(panel);
				
			}
			else {
				scanButton.setEnabled(true);
				enterButton.setEnabled(false);
				listTip.setVisible(false);
				listPane.setVisible(false);
				scanTip.setVisible(true);
				scanTip.setText("没有扫描到 联网的终端设备");
			}
	  }
	  
	  private JButton scanButton ;
	  private JButton addButton;
	  private Label ipLabel;
	  private JTextField ipText;
	  private Label portLabel;
	  private JTextField portText;
      private  Label modelLabel;
      private  JTextField modelText;
	  private JButton confirmButton;
	  private JLabel errorTip;
	  private JButton enterButton;
	  private JLabel  scanTip;
	  private JLabel  selectLabel;
	  private JLabel listTip;
	  private ScrollPane  listPane ;
	  private JPanel  selectPanel = null;
	  public JPanel getSelectPanel(){
		  if(selectPanel == null){
			  selectPanel = new JPanel();
			  selectPanel.setLayout(null);
			  selectPanel.setBackground(new Color(173, 216, 230));
			  selectPanel.setBounds(0, 0, 600, 450);
			  
			  
			  scanButton = new JButton("自动扫描");
			  scanButton.setBounds(10, 10, 100, 25);
			  scanButton.setFont(new Font("宋体", Font.PLAIN, 14));
			  scanButton.addActionListener(mClickListener);
			  selectPanel.add(scanButton);
	
			  scanTip = new JLabel("点击 开始扫描 进行扫描");
			  scanTip.setFont(new Font("宋体", Font.PLAIN, 12));
			  scanTip.setBounds(120, 18, 150, 20);
			  selectPanel.add(scanTip);
			  
			  
			  
			  addButton = new JButton("手动添加");
			  addButton.setBounds(10, 40, 100, 25);
			  addButton.setFont(new Font("宋体", Font.PLAIN, 14));
			  addButton.addActionListener(mClickListener);
			  selectPanel.add(addButton);
			  
			  ipLabel = new Label("IP:");
			  ipLabel.setBounds(115, 40, 25, 25);
			  ipLabel.setFont(new Font("宋体", Font.PLAIN, 16));
			  selectPanel.add(ipLabel);
			  
			  ipText = new JTextField(1);
			  ipText.setBounds(140, 40, 110, 25);
			  ipText.setFont(new Font("宋体", Font.PLAIN, 14));
			  selectPanel.add(ipText);
			  
			  portLabel = new Label("端口:");
			  portLabel.setBounds(252, 40, 40, 25);
			  portLabel.setFont(new Font("宋体", Font.PLAIN, 14));
			  selectPanel.add(portLabel);
			  
			  portText = new JTextField(1);
			  portText.setBounds(292, 40, 40, 25);
			  portText.setFont(new Font("宋体", Font.PLAIN, 14));
			  selectPanel.add(portText);

              modelLabel = new Label("机型:");
              modelLabel.setBounds(335, 40, 40, 25);
              modelLabel.setFont(new Font("宋体", Font.PLAIN, 14));
              selectPanel.add(modelLabel);

              modelText = new JTextField(1);
              modelText.setBounds(375, 40, 60, 25);
              modelText.setFont(new Font("宋体", Font.PLAIN, 14));
              selectPanel.add(modelText);
			  
			  confirmButton = new JButton("确定");
			  confirmButton.setBounds(447, 40, 60, 25);
			  confirmButton.setFont(new Font("宋体", Font.PLAIN, 12));
			  confirmButton.addActionListener(mClickListener);
			  selectPanel.add(confirmButton);
			  
			  errorTip = new JLabel("输入错误");
			  errorTip.setFont(new Font("宋体", Font.PLAIN, 12));
			  errorTip.setBounds(447, 40, 400, 20);
			  errorTip.setForeground(Color.RED);

			  selectPanel.add(errorTip);
			  hideHandAdd();
			  
			  
			  enterButton = new JButton("开始监控");
			  enterButton.setBounds(280, 415, 110, 25);
			  enterButton.setFont(new Font("宋体", Font.PLAIN, 14));
			  enterButton.addActionListener(mClickListener);
			  enterButton.setEnabled(false);
			  selectPanel.add(enterButton);
			  
			  selectLabel =  new JLabel("请选择监控");
			  selectLabel.setBounds(280, 395, 110, 20);
			  selectLabel.setFont(new Font("宋体", Font.PLAIN, 12));
			  selectLabel.setVisible(false);
			  selectPanel.add(selectLabel);
			  
			  listTip = new JLabel("请选择要监控的屏");
			  listTip.setBounds(10, 80, 100, 20);
			  listTip.setFont(new Font("宋体", Font.PLAIN, 12));
			  listTip.setVisible(false);
			  selectPanel.add(listTip);
			  
			  listPane = new ScrollPane();
			  listPane.setVisible(false);
			  listPane.setBounds(10, 100, 250, 300);
			  listPane.setBackground(Color.DARK_GRAY);
			  selectPanel.add(listPane);	  
		  }
		  
		  return selectPanel;
	  }
	  
	  
	  
	  
	  private ActionListener mClickListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource() == scanButton){
                System.out.println("scanButton clicked!!!!");
				hideHandAdd();
                enterButton.setEnabled(false);
				scanButton.setEnabled(false);
				addButton.setEnabled(false);
				terminalList.clear();
				selectList.clear();
                System.out.println("scanTip:"+scanTip.getText());
				scanTip.setText("正在扫描，请稍后...");
                System.out.println("scanTip:"+scanTip.getText());
                if (Debug) {
                    IPUtil.VNCBean bean = ipUtil.newBean();
                    bean.host = "192.168.1.132";
                    bean.port = Integer.valueOf(743) + 100;//VNC的IP需要真实的port+100
                    bean.model = "AK-043AW";
                    for (int i = 0; i < 2; i++) {
                        addterminalList(bean);
                    }
                    windowSize = ipUtil.getWindowSize(terminalList);
                    showTeminalView();
                    scanEnd();
                } else {
                    ipUtil.getUseableList(MainViewer.this);
                }
			}
			else if (e.getSource() == enterButton) {
				if(terminalList.size() > 0){
					selectLabel.setVisible(false);
		
					VNCshow();
				}
				else {
					selectLabel.setVisible(true);
				}
			}
			else if(e.getSource() == addButton){
				showHandAdd();
			}
			else if(e.getSource() == confirmButton){

				String ip = ipText.getText();
				String port = portText.getText();
                String model = modelText.getText();
				if(isValidateIp(ip) && isPosInt(port) && isModelType(model)){//如果是有效输入
					hideHandAdd();
					
					IPUtil.VNCBean bean = ipUtil.newBean();
					bean.host = ip;
					bean.port = Integer.valueOf(port) + 100;//VNC的IP需要真实的port+100
					bean.model = model;
					
					addterminalList(bean);
                    windowSize = ipUtil.getWindowSize(terminalList);
					showTeminalView();
				}
				else {
                    showErrorTip();

				}
			}
			
		}
	};
	
	//显示手动添加
	private void showHandAdd(){
		ipLabel.setVisible(true);
		ipText.setVisible(true);
		ipText.setText("");
		portLabel.setVisible(true);
		portText.setVisible(true);
		portText.setText("");
        modelLabel.setVisible(true);
        modelText.setVisible(true);
        modelText.setText("");
		confirmButton.setVisible(true);
		  
		errorTip.setVisible(false);
		addButton.setEnabled(false);
	}
	
	//隐藏手动添加
	private void hideHandAdd(){
		ipLabel.setVisible(false);
		ipText.setVisible(false);
		portLabel.setVisible(false);
		portText.setVisible(false);
        modelLabel.setVisible(false);
        modelText.setVisible(false);
		confirmButton.setVisible(false);
		  
		errorTip.setVisible(false);
		addButton.setEnabled(true);
	}
	
	private void VNCshow(){
        System.out.println("====================VNCshow==================");
        System.out.println("1vncContainer.conponents:"+vncContainer.getComponents().length);
		vncContainer.removeAll();
        System.out.println("2vncContainer.conponents:"+vncContainer.getComponents().length);


		int width = 400;
		int high = 450;
        System.out.println("mainFrame:"+mainFrame);
        System.out.println("selectList.size:"+selectList.size());
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println("Screen size"+size);
        System.out.println("VncSize:"+windowSize);

        if (mainFrame == null) {
            mainFrame = new JPanel();
            mainFrame.setBorder(BorderFactory.createLineBorder(Color.red, 4));
        }

		if(selectList.size() <4 ){ 
			//mainFrame = new Panel();
			for (int i = 0; i < selectList.size(); i++) {
				IPUtil.VNCBean bean = selectList.get(i);
				VncViewer v =  new VncViewer(bean.port, bean.host, backListenr);

                v.setBorder(BorderFactory.createLineBorder(Color.blue, 3));
				mainFrame.add(v);
			}

			CardLayout cLayout = new CardLayout();
			vncContainer.setLayout(cLayout);
            vncContainer.add(mainFrame);


			width = windowSize.x + 20;
			high = (windowSize.y + 10) * selectList.size();

			//vncContainer.setSize(width, high);
			
		}
		else if(selectList.size() <= 8 ) {
			int row = 2;
			int cols = selectList.size()/ row + (selectList.size() % row != 0 ? 1:0 );
			GridLayout gLayout = new GridLayout(cols, row);
			gLayout.setHgap(20);
			gLayout.setVgap(20);
			//mainFrame = new Panel();
			mainFrame.setLayout(gLayout);
			for (int i = 0; i < selectList.size(); i++) {
				IPUtil.VNCBean bean = selectList.get(i);
				VncViewer v =  new VncViewer(bean.port, bean.host ,backListenr);
				v.setPreferredSize(new Dimension(windowSize.x, windowSize.y));
				mainFrame.add(v);
			}

		  ScrollPane scroll = new ScrollPane();
		  scroll.add(mainFrame);
		  CardLayout cLayout = new CardLayout();
		  vncContainer.setLayout(cLayout);
		  vncContainer.add(scroll);
		  
		  width =  row * windowSize.x + (row - 1) * 20;
		  high =  cols * windowSize.y + (cols + 1)* 20; 
		  vncContainer.setSize(width, high);
		}
		else {
			int row = windowSize.x >= 800 ? 2:3;
			int cols = selectList.size()/ row + (selectList.size() % row != 0 ? 1:0 );
			GridLayout gLayout = new GridLayout(cols, row);
			gLayout.setHgap(20);
			gLayout.setVgap(20);
			//mainFrame = new Panel();
			mainFrame.setLayout(gLayout);
			for (int i = 0; i < selectList.size(); i++) {
				IPUtil.VNCBean bean = selectList.get(i);
				VncViewer v =  new VncViewer(bean.port, bean.host, backListenr);
				v.setPreferredSize(new Dimension(windowSize.x, windowSize.y));
				mainFrame.add(v);
			}

		  ScrollPane scroll = new ScrollPane();
		  scroll.add(mainFrame);
		  CardLayout cLayout = new CardLayout();
		  vncContainer.setLayout(cLayout);
		  vncContainer.add(scroll);
		  
		  width =  row * windowSize.x + (row - 1) * 20;
		  high = width * 4 /3;
		  vncContainer.setSize(width, high);
		}
		
		 invalidate();
		  if(mFrame != null){
			  mFrame.pack();
			  //mFrame.setSize(width +20, high + 35);
			  mFrame.invalidate();
		  }
        System.out.println("3vncContainer.conponents:"+vncContainer.getComponents().length);
        System.out.println("====================END==================");
	}
	
	private ArrayList<IPUtil.VNCBean> selectList = new ArrayList<IPUtil.VNCBean>();
	private ItemListener mItemListener = new ItemListener() {
		
		@Override
		public void itemStateChanged(ItemEvent arg0) {
			// TODO Auto-generated method stub
			Checkbox item = (Checkbox) arg0.getSource();
			IPUtil.VNCBean changeBean = null;
			for(IPUtil.VNCBean bean : terminalList){
				if (item.getName().equals(bean.host)) {
					changeBean = bean;
					break;
				}
			}
			if (arg0.getStateChange() == ItemEvent.SELECTED) {
				selectList.add(changeBean);
			}
			else {
				selectList.remove(changeBean);
			}
			
			// 进行判断 是否能够进入 检测界面
			if(selectList.size() > 0){
				enterButton.setEnabled(true);
			}
			else {
				enterButton.setEnabled(false);
			}
			
		}
	};
	
	
	private ButtonPanel.BackEventListenr backListenr = new ButtonPanel.BackEventListenr() {
		
		public void OnBackEvent() {
            System.out.println("1vncContainer.conponents:"+vncContainer.getComponents().length);
			vncContainer.removeAll();
            mainFrame.removeAll();
            System.out.println("2vncContainer.conponents:"+vncContainer.getComponents().length);
			vncContainer.setLayout(null);
			vncContainer.add(getSelectPanel());
			vncContainer.setSize(600, 450);
			vncContainer.setBackground(new Color(173, 216, 230));
			scanButton.setEnabled(true);
			
			 if(mFrame != null){
				  mFrame.pack();
				  mFrame.setSize(620, 500);
				  mFrame.invalidate();
			  }
		}
	};
	
	public void addterminalList(IPUtil.VNCBean bean){
		for(IPUtil.VNCBean tBean : terminalList){
			if(tBean.host.equals(bean.host) && tBean.port == bean.port){
                if (!Debug) {
                    return;
                }

			}
		}
		
		terminalList.add(bean);
	}
	
	
	/**
	 * 验证ip的合法性
	 * 
	 */
	private  boolean isValidateIp(String ip) {

        StringBuilder sb = new StringBuilder();
		Pattern pattern = Pattern
				.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");

		Matcher matcher = pattern.matcher(ip); // 以验证127.400.600.2为例
        boolean result = matcher.matches();
        if (!result) {
            sb.append("输入ip不正确！;");
        }
        errorTip.setText(sb.toString());
		return result;
	}
	
	/**
	 * 是否是正整数
	 */
	private  boolean isPosInt(String str){
        StringBuilder sb = new StringBuilder();
		if (null == str || "".equals(str)) {
            sb.append("端口号是空的！;");
            errorTip.setText(sb.toString());
			return false;
		}
		boolean resulet=false;
		Pattern pattern = Pattern.compile("[0-9]*");
		resulet=pattern.matcher(str).matches();
        if (!resulet) {
            sb.append("端口号必须是数字！;");
        }

        errorTip.setText(sb.toString());
		return resulet;
	}

    private boolean isModelType(String model) {
        StringBuilder sb = new StringBuilder();
        if (null == model || "".equals(model)) {
            sb.append("机型是空的！;");
            errorTip.setText(sb.toString());
            return false;
        }

        if (model.length() < 8) {
            sb.append("机型输入有误！;");
            errorTip.setText(sb.toString());
            return false;
        }

        String size = model.substring(3, 6);
        Pattern pattern = Pattern.compile("[0-9]*");
        if (!pattern.matcher(size).matches()) {
            sb.append("机型输入有误！;");
            errorTip.setText(sb.toString());
            return false;
        }

        String s = model.substring(0, 2);
        boolean result;
        if (s.equals("AK")) {
            result = true;
        } else {
            result = false;
            sb.append("机型输入有误！;");
        }
        errorTip.setText(sb.toString());
        return result;
    }

    private void showErrorTip() {
        confirmButton.setVisible(false);
        errorTip.setVisible(true);
        addButton.setEnabled(true);
        System.out.println("errorTip visible:"+errorTip.isVisible());
    }
	  

	public void windowActivated(WindowEvent e) {
	}
	public void windowClosed(WindowEvent e) {
	}
	public void windowClosing(WindowEvent e) {
		System.out.println("Closing window!!");
			System.exit(0);
	}
	public void windowDeactivated(WindowEvent e) {
	}
	public void windowDeiconified(WindowEvent e) {
	}
	public void windowIconified(WindowEvent e) {
	}
	public void windowOpened(WindowEvent e) {
	}


	
}
