import java.io.BufferedInputStream;
import java.io.*;
import java.nio.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer{

	private ServerSocket serverSocket;
	public final static int SOCKET_PORT = 13267;  // you may change this
	public final static String FILE_TO_SEND = "/home/kls102/Documents/movies.zip";  // you may change this
	public final static String FILE_TO_RECEIVED = "/home/kls102/clientStorage/tesd.txt";
	public final static String SERVER_DIRECTORY = "/home/kls102/clientStorage/";
	public final static int FILE_SIZE = 6022386; // file size is temporary hard coded
	public final static String SERVER = "localhost";  // localhost

	FileOutputStream fos = null;
	BufferedOutputStream bos = null;
	DataInputStream dis=null;
	DataOutputStream dos=null;


	public static void main (String [] args ) throws IOException {
		FileServer fs= new FileServer(SOCKET_PORT);
		// fs.recieveFile();	
		fs.run();	
	}

	public FileServer(int port) {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() throws IOException{
		Socket clientSock = serverSocket.accept();
		this.dis=new DataInputStream(clientSock.getInputStream());
		this.dos=new DataOutputStream(clientSock.getOutputStream()); 
	// try{	
 //  Socket clientSocket = serverSocket.accept(); 
 //  recieveFile(clientSocket);
		// while (true) {
			try
			{

				System.out.println("Waiting for Command ...");
				String Command=this.dis.readUTF();
				if(Command.compareTo("GET")==0)
				{
					System.out.println("\tGET Command Received ...");
					this.sendFile(clientSock);
				}
				else if(Command.compareTo("SEND")==0)
				{
					System.out.println("\tSEND Command Receiced ...");               
					this.recieveFile(clientSock);
				}
				else if(Command.compareTo("DISCONNECT")==0)
				{
					System.out.println("\tDisconnect Command Received ...");
					System.exit(1);
				}
			}catch(Exception e){}
			// String filename=this.dis.readUTF();
			// try {
			// 	Socket clientSock = ss.accept();
			// 	saveFile(clientSock);
			// } catch (IOException e) {
			// 	e.printStackTrace();
			// }
		// }
	}

	public void sendFile(Socket clientSocket) throws IOException{
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		OutputStream os = null;
		try {
			String fileName=this.dis.readUTF();
			System.out.print(fileName + "yhi hai");
			String filepath = SERVER_DIRECTORY + fileName;
			System.out.println("Waiting..." + filepath);
			try {
					// send file
				// File f=new File(filepath);
				// if(!f.exists())
				// {
				// 	this.dos.writeUTF("File Not Found");
				// 	return;
				// }
				File myFile = new File (filepath);
				byte [] mybytearray  = new byte [(int)myFile.length()];
					// to read byte oriented data from file
				fis = new FileInputStream(myFile);
				bis = new BufferedInputStream(fis);
					// It read the bytes from the specified byte-input stream into a specified byte array
				bis.read(mybytearray,0,mybytearray.length);
				os = clientSocket.getOutputStream();
				os.write(mybytearray,0,mybytearray.length);
				os.flush();
				System.out.println("Done.");
			}catch(Exception e){}

		}
		catch(Exception e){}
	}

	public void recieveFile(Socket clientSocket) throws IOException{
		// InputStream initialStream = new FileInputStream(
		// 	new File("/home/kls102/Documents/login.zip"));
		// byte[] buffer = new byte[initialStream.available()];
		// initialStream.read(buffer);

		// File targetFile = new File(FILE_TO_RECEIVED);
		// OutputStream outStream = new FileOutputStream(targetFile);
		// outStream.write(buffer);	
	int bytesRead = 0;
		int current = 0;
		try {
			System.out.println("Connecting...");
       String fileName = this.dis.readUTF();
			// receive file
			byte [] mybytearray  = new byte [FILE_SIZE];
			InputStream is = clientSocket.getInputStream();
			String filepath = SERVER_DIRECTORY + fileName;
			this.fos = new FileOutputStream(filepath);
			this.bos = new BufferedOutputStream(fos);
			bytesRead = is.read(mybytearray,0,mybytearray.length);
			current = bytesRead;
    System.out.println("Connection Working...");
			do {
				 bytesRead =
						is.read(mybytearray, current, (mybytearray.length-current));
				 if(bytesRead >= 0) current += bytesRead;
			} while(bytesRead > -1);
			this.bos.write(mybytearray, 0 , current);
			this.bos.flush();
      System.out.println("Connection Working1...");
			System.out.println("File " + filepath
					+ " downloaded (" + current + " bytes read)");
		}catch(Exception e){System.out.print(e);}
		
	}
}
