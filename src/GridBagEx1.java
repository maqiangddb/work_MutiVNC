import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JPanel;

/**
 * Created by mqddb on 2014/4/26.
 */
public class GridBagEx1 extends Applet implements WindowListener {



    protected void makebutton(String name, GridBagLayout gridbag,  GridBagConstraints c) {
        Button button = new Button(name);
        gridbag.setConstraints(button, c);
        add(button);
    }

    private Container container;

    public void init() {
        setLayout(new FlowLayout());
        Frame frame = new Frame();
        add(frame);
        container = frame;
        init_flow();
    }

    public void init_flow() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout()); //因为是默认管理器，可以省略
        //setLayout()方法是applet类继承Container类中的方法而来，因此在applet程序中可以直接调用
        for(int i = 0; i<150;i++){
            Button b = new Button("Btn"+i);
            b.setSize(150,150);
            panel.add(b); //加入5个按钮
            //applet类继承Container类中的方法成员add();
        }
        container.add(panel);
    }

    public void init_border()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout()); //因为是默认管理器，可以省略
        panel.add(new Button("按钮上"),BorderLayout.NORTH);
        Button b1 = new Button("b1");
        b1.setSize(15, 15);
        panel.add(b1, BorderLayout.NORTH);
        panel.add(new Button("按钮右"), BorderLayout.EAST);
        //?setHgap(50);想设置组件的水平和垂直的边距，看下面的GridLayout使用方法
        panel.add(new Button("按钮中"),BorderLayout.CENTER);
        //add(new Button("按钮下"),BorderLayout.SOUTH) //引上是为了测试自动填充效果
        //add(new Button("按钮左"),BorderLayout.WEST) //引上是为了测试自动填充效果
        container.add(panel);
    }

    public void init_gridbaga() {

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        setFont(new Font("SansSerif", Font.PLAIN, 14));
        setLayout(gridbag);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        makebutton("Button1", gridbag, c);
        makebutton("Button2", gridbag, c);
        makebutton("Button3", gridbag, c);
        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        makebutton("Button4", gridbag, c);
        c.weightx = 0.0;	   //reset to the default
        makebutton("Button5", gridbag, c); //another row
        c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last in row
        makebutton("Button6", gridbag, c);
        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        makebutton("Button7", gridbag, c);
        c.gridwidth = 1;	     //reset to the default
        c.gridheight = 2;
        c.weighty = 1.0;
        makebutton("Button8", gridbag, c);
        c.weighty = 0.0;	   //reset to the default
        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        c.gridheight = 1;	   //reset to the default
        makebutton("Button9", gridbag, c);
        makebutton("Button10", gridbag, c);

        setSize(300, 100);
    }



    public static void main(String args[]) {

        Frame f = new Frame("GridBag Layout Example");

        GridBagEx1 ex1 = new GridBagEx1();
        ex1.init();
        f.add("Center", ex1);
        f.addWindowListener(ex1);
        f.pack();
        f.setSize(f.getPreferredSize());
        f.setVisible(true);

    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    public void windowClosing(WindowEvent e) {
        System.out.println("Closing window!!");
        System.exit(0);
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

}
