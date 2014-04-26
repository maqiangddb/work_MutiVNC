import java.awt.Color;
import java.awt.Font;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;


public class SelectViewer extends Panel{
	
	private JButton scanButton ;
	private JLabel  scanTip;
	public SelectViewer(){
		setBounds(0, 0, 800, 480);
		setLayout(null);
		setBackground(Color.lightGray);
	}
	
	private void initView(){
		scanButton = new JButton("��ʼɨ��");
		scanButton.setBounds(10, 15, 80, 25);
		scanButton.setFont(new Font("����", Font.PLAIN, 14));
		scanButton.addActionListener(mClickListener);
		
		scanTip = new JLabel("���� ��ʼɨ�� ����ɨ��");
		scanTip.setFont(new Font("����", Font.PLAIN, 10));
		scanTip.setBounds(110, 20, 100, 20);
	}
	
	
	private ActionListener  mClickListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	};

}
