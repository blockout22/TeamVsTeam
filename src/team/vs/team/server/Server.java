package team.vs.team.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.UUID;

public class Server {
	
	private final int port = 1234;
	private DatagramSocket socket;
	private boolean running = false;
	
	private UUID closePacket;
	
	public Server() {
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
			return;
		}
		
		running = true;
		closePacket = UUID.randomUUID();
	}
	
	public void start() throws IOException
	{
		while(running) {
			byte[] buffer = new byte[1024];
			
			DatagramPacket dataPacket = new DatagramPacket(buffer, buffer.length);
			
			socket.receive(dataPacket);
			String message = new String(dataPacket.getData()).trim();
			
			if(message.equals(closePacket.toString()))
			{
				break;
			}
			
			if(message.equals("ping")) {
				send("pong".getBytes(), dataPacket.getAddress(), dataPacket.getPort());
			}
		}
		socket.close();
	}
	
	public void send(byte[] data, InetAddress ip, int port) throws IOException{
		DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
		socket.send(packet);
	}
	
	public void stop() throws IOException
	{
		running = false;
		byte[] buffer = closePacket.toString().getBytes();
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(""), port);
		socket.send(packet);
	}
	
	public static void main(String[] args) throws IOException {
		Server server = new Server();
		server.start();
	}

}
