import java.awt.Point;
import java.io.DataInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;


public class IPUtil {
	private final int PORT = 10102;
	private Vector<VNCBean> ipList;
	private CheckNotify netNotify;
	private final int MAX_NET = 255;
	
	public IPUtil(){
		ipList = new Vector<VNCBean>();
		netNotify = new  CheckNotify(MAX_NET);
		
	}
	
	// 获取当前的网段 目前暂时获取255.255.255段的IP
	private String getLocalIP(){
		String host = ""; // 网段
		try {
			InetAddress addr = InetAddress.getLocalHost();
			String ip=addr.getHostAddress().toString();//获得本机IP
			
			int i = ip.length() -1;
			for(; i > 0 ; i--){
				char v = ip.charAt(i);
				if(v == '.'){
					break;
				}
			}

			host = ip.substring(0, i+1);
			
			return host;
		} catch (UnknownHostException e) {
			System.out.println("获取网段失败");
			e.printStackTrace();
		}
		
		return host;
	}
	
	public void onDestroy(){
	
		
	}
	
	
	public void getUseableList(MainViewer main){
        //System.out.println("Start ip Scan!!!!!");
		ipList.clear();
		netNotify.clearCurSize();
		
		String host = getLocalIP();

		for(int i = 1 ; i < 256; i++){
			String ip = host + i;
			new Thread(new IPRun(ip)).start();
		}
        //System.out.println("Scan End!!!!!");
		main.scanEnd();

	}
	
	public void addNetListener(Observer observer){
		netNotify.addObserver(observer);
	}
	
	
	class IPRun implements Runnable{
		
		private String host;
		public  IPRun(String host){
			this.host = host;
		}
		
		@Override
		public void run() {
            //System.out.println("IPRun===ip:"+host);
			try {
				Socket socket = new Socket(host, PORT);
				socket.setSoTimeout(600);
				DataInputStream input = new DataInputStream(socket.getInputStream());
				VNCBean bean = new VNCBean();
				
				bean.host = host;
				bean.port = input.readInt() + 100;
				bean.model = input.readUTF();
                System.out.println("send request to:"+host+"  get info port:"+bean.port+" model:"+bean.model);
				ipList.add(bean);
                System.out.println("notify change!!!!");
                netNotify.netNotify();
                //socket.close();
			} catch (Exception  e) {
				//e.printStackTrace();
			}
			finally{
                //System.out.println("notify change!!!!");
				//netNotify.netNotify();
			}
		}
		
	}
	
	
	public class VNCBean{
		public String host;
		public int port;
		public String model;
	}
	
	public VNCBean newBean(){
		return new VNCBean();
	}
	
	
	/**
	 * 获取屏幕最佳的显示尺寸
	 */
	public Point getWindowSize(Vector<VNCBean> list){
		Point maxSize = new Point(800, 480);
		int imaxsize = 35;
		for(VNCBean bean : list){
			int size =  Integer.valueOf(bean.model.substring(3, 6)) ;
			if(size > imaxsize){
				imaxsize = size;
			}
		}
		
		if(imaxsize <= 40){// 3.5寸 4.0寸
			maxSize.x = 320;
			maxSize.y = 240;
		}
		else if(imaxsize <= 50){
			maxSize.x = 480;
			maxSize.y = 272;
		}
		else {
			maxSize.x = 800;
			maxSize.y = 480;
		}

        maxSize.y += 30;
		
		return maxSize;
	}

	
	public Vector<VNCBean> getTeminalList(){
		return ipList;
	}
	
	public class CheckNotify extends Observable {
		private int maxSize;
		private int curSize;
		public CheckNotify( int max){
			maxSize = max;
			curSize = 0;
		}
		
		public synchronized void clearCurSize(){
			curSize = 0;
		}
		
		public synchronized void netNotify( ){
            System.out.println("CheckNotify---netNotify curSize:"+curSize);
			curSize ++;
            setChanged();
            notifyObservers();
            /*
			if(curSize >= maxSize){
				setChanged();
				notifyObservers();
			}
			*/
		}		
		
	}

}
