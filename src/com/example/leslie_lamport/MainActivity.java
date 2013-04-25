package com.example.leslie_lamport;
 
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import android.app.Activity;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	public Thread thread;
	public TextView chat_field;
	public EditText message_input,editText1;
	private static final String TAG = "Discovery";
 
	private static final int DISCOVERY_PORT = 2562;
	private static final int TIMEOUT_MS = 500;
	private Handler bridge;
	public String message = "";
	public DatagramSocket socket,socket_listen ;
	public   String SERVERIP = "127.0.0.1"; // 'Within' the emulator!
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
//----------------------------------------------------------------------------------------------
	
		chat_field =  (TextView)findViewById(R.id.textView1);
		message_input = (EditText)findViewById(R.id.editText1);
		chat_field.setText("");// vaciamos el chat
		message_input.setText("");
//----------------------------------------------------------------------------------------------
		try{
			  socket = new DatagramSocket(DISCOVERY_PORT);
		      socket.setBroadcast(true);
		      
		     
		}catch (IOException e) {
			 
			}
//----------------------------------------------------------------------------------------------	
		bridge = new Handler() {
			  @Override
			  public void handleMessage(Message msg) {
			    
				   String new_text = String.valueOf(msg.obj);
				   
				   chat_field.append("- l "+new_text+"\n");
				 //  message_input.setText("");
			  }
			 };
		
 //----------------------------------------------------------------------------------------------
			
	 
		thread = new Thread(new Runnable(){
			@Override
			public void run(){
	 
		 
				
				 try {
					
					 Log.d(TAG, "Tryin reading data ");
 
				      //socket.setSoTimeout(TIMEOUT_MS);
				       
				      listenForResponses(socket);
	 
				} catch (IOException e) {
					 Log.d(TAG, "fail reading Sending data ");
				}
			 
				
		 
			}
			
			
		}
				
		);
		
		thread.start();
 
	}
	 //----------------------------------------------------------------------------------------------
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/* SENDING */
	/* ********************************************************************************* */
	
	
	  private void sendDiscoveryRequest(DatagramSocket socket) throws IOException {
		    
		  getcurrentMessage();
		  
		  	String data = message;
		  	
		  	String SERVERIP = "34";
		    
		   
		    
		    
		      DatagramSocket datagramSocket = new DatagramSocket();

              byte[] buffer = message.getBytes();
    //        InetAddress receiverAddress = InetAddress.getLocalHost();
     
             
           // Log.d(TAG, "Sending data " + data +" to "+receiverAddress);
             
              DatagramPacket packet = new DatagramPacket(
            	      buffer, buffer.length, getBroadcastAddress(), DISCOVERY_PORT);
               //buffer, buffer.length,  receiverAddress , DISCOVERY_PORT);
            		  
            		  datagramSocket.send(packet);
		    
		    /*
		    //tv1.setText(data );
		    //InetAddress serverAddr = InetAddress.getByName(SERVERIP);
		    DatagramPacket packet = new DatagramPacket(data.getBytes(), data.length(),
		    		getBroadcastAddress(), DISCOVERY_PORT);
		    //serverAddr, DISCOVERY_PORT);
		    socket.send(packet);
		    */
		  }
	
	
	/* LISTEN */
	/* ********************************************************************************* */
	
	 
	  private void listenForResponses(DatagramSocket socket) throws IOException {
		    byte[] buf = new byte[1024];
		    try {
		      while (true) {
		        DatagramPacket packet = new DatagramPacket(buf, buf.length);
		        socket.receive(packet);
		        String s = new String(packet.getData(), 0, packet.getLength(), "US-ASCII");
		        
		        
 		         Message msg = new Message();
 		         msg.obj = s;
 		       bridge.sendMessage(msg);
		        
		        
		        Log.d(TAG, "Received response " + s);
		      }
		      
		      
		    } catch (SocketTimeoutException e) {
		      Log.d(TAG, "Receive timed out");
		    }
		  }

	/* ********************************************************************************* */
	
 
 
	InetAddress getBroadcastAddress() throws IOException {
		WifiManager wifi = (WifiManager) getSystemService(WIFI_SERVICE);
 
	    DhcpInfo dhcp = wifi.getDhcpInfo();
	    // handle null somehow

	    int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
	    byte[] quads = new byte[4];
	    for (int k = 0; k < 4; k++)
	      quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
	    return InetAddress.getByAddress(quads);
	}

	
	/* ********************************************************************************* */
	
	
 public void getTicket(View view){
	 
 
     
		thread = new Thread(new Runnable(){
			@Override
			public void run(){
	 
				
				 try {
					 
					 Log.d(TAG, "Trying Sending data ");

					 
				      //socket.setSoTimeout(TIMEOUT_MS);
				      sendDiscoveryRequest(socket);
				     // listenForResponses(socket);
	 
				} catch (IOException e) {
				 
				}
			 
				
		 
			}
			
			
		}
				
		);
		
		thread.start();
 
    
     
 }// end send_text
 
	
	/* ********************************************************************************* */
	
 
 public void getcurrentMessage(){
	 
	 message = " "+message_input.getText().toString();
	 
 }
	
	 
	
}



