package Utilities.Unix;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.SneakyThrows;

public class UnixUtils {

	private static Session session;
	public static boolean connected=false;
	public static Channel channel=null;
	private static ChannelSftp channelSFTP=null;
	private static InputStream is;
	private static OutputStream os;

	@SneakyThrows
	public static boolean connectServer(String host, int port,String userName,String password)
	{
		try {
			JSch jsch=new JSch();

			session=jsch.getSession(userName,host,port);

			Properties config=new Properties();
			config.put("StrictHostKeyChecking", "no");
			config.put("PreferredAuthentications", "password");
			session.setConfig(config);
			session.setPassword(password);

			System.out.println("Connecting");

			session.connect();

			if(session.isConnected()) {
				connected=true;
				System.out.println("Server with hostname "+host+" is connected");

			}

		} catch (Exception e) {
			System.err.println("Cannot connect with host "+host);
			e.printStackTrace();
		}
		return connected;

	}

	@SneakyThrows
	public static void transferFileToUnix(String sourceLocation,String destinationLocation)
	{
		try {
			channelSFTP=(ChannelSftp) session.openChannel("sftp");
			channelSFTP.connect();
			System.out.println("channelSftp connected: "+channelSFTP.isConnected());
			channelSFTP.put(sourceLocation, destinationLocation);
			System.out.println("File transfered to Unix location : "+destinationLocation);
		} catch (Exception e) {
			System.out.println("File transfer to Unix location failed : "+destinationLocation);
			e.printStackTrace();
		}
	}

	@SneakyThrows
	public static void transferFileFromUnix(String sourceLocation,String destinationLocation)
	{
		try {
			channelSFTP=(ChannelSftp) session.openChannel("sftp");
			channelSFTP.connect();
			System.out.println("channelSftp connected: "+channelSFTP.isConnected());
			channelSFTP.get(sourceLocation, destinationLocation);
			System.out.println("File transfer from Unix location sucessful ");
		} catch (Exception e) {
			System.err.println("File transfer from Unix location failed");
			e.printStackTrace();
		}
	}

    @SneakyThrows
	public static void openCommandChannel()
	{
		try {
			channel=session.openChannel("shell");
			Thread.sleep(2000);
			channel.setInputStream(null);
			channel.setOutputStream(null);
			is=channel.getInputStream();
			os=channel.getOutputStream();
			channel.connect();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
   @SneakyThrows
	public static String executeShellCommand(String command,int timeInSeconds) {

		String shellOutput="";
		try {
			Thread.sleep(1000);
			os.write(command.getBytes());
			os.write(("\n").getBytes());
			os.flush();

			Thread.sleep(1000*timeInSeconds);

			shellOutput=readOutput();

		} catch (Exception e) {
			System.err.println(command+" is not executed sucessfully");
			e.printStackTrace();
		}

		return shellOutput;


	}

	
	public static String readOutput()
	{
		byte[] temp=new byte[2048];
		String output="";
		try {
			while(true) {
				while (is.available()>0) {
					int i=is.read(temp,0,2048);
					if(i<0)
						break;
					output=output+new String(temp,0,i);
					Thread.sleep(1000);
				}
				Thread.sleep(2000);
				System.out.println(output.toString());
				break;
			}
		} catch (Exception e) {
			System.err.println("Failed to capture Command response");
			e.printStackTrace();
		}
		return output;

	}
	
	public static void closeSession()
	{
		channel.disconnect();
		session.disconnect();
		connected=false;
		if(!session.isConnected())
			System.out.println(" Server disconnected");
	}
	
	@SneakyThrows
	public static String executeUnixCommand(String host,int port,String user,String pass,String command)
	{
		String output="";
		try {
			connectServer(host, port, user, pass);
			Channel channel=session.openChannel("exec");
			((ChannelExec)channel).setCommand(command);
			channel.setInputStream(new ByteArrayInputStream(command.getBytes(StandardCharsets.UTF_8)));
			((ChannelExec)channel).setErrStream(System.err);
			InputStream is=channel.getInputStream();
			channel.connect();
			byte[] temp=new byte[1024];
			while(true) {
				while (is.available()>0) {
					int i=is.read(temp,0,1024);
					if(i<0)
						break;
					output=output+new String(temp,0,i);
				}
				if(channel.isClosed()) {
					break;
				}
				Thread.sleep(1000);
			}
			channel.disconnect();
			session.disconnect();
			
		} catch (Exception e) {
         e.printStackTrace();		}
		return output;
		
	}


}
