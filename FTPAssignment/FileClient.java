import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.*;
import java.net.Socket;

public class FileClient{

	public final static int SOCKET_PORT = 13267;      // we can change this
	public final static String SERVER = "localhost";  // localhost
	public final static String FILE_TO_RECEIVED = "./ClientLocal/tesd.txt";
	public final static String CLIENT_DIRECTORY = System.getProperty("user.dir") + File.separator + "ClientLocal/";
	public final static String FILE_TO_SEND = System.getProperty("user.dir") + File.separator + "ClientUpload/";
	public final static int FILE_SIZE = 6022386; // file size is temporary hard coded
	FileOutputStream fos = null;
	BufferedOutputStream bos = null;
	BufferedReader br=null;
	Socket sock = null;
	FileInputStream fis = null;
	BufferedInputStream bis = null;
	OutputStream os = null;
	DataInputStream dis=null;
	DataOutputStream dos=null;

	public static void main (String [] args ) throws Exception {
		FileClient fc = new FileClient();
		fc.displayMenu();
	}

	public void recieveFile(Socket sock) throws IOException{
		System.out.print("Enter File Name :");
		this.br = new BufferedReader(new InputStreamReader(System.in));
		String fileName=this.br.readLine();
		this.dos.writeUTF(fileName);
		String filepath = CLIENT_DIRECTORY + fileName;
  //   System.out.print(filepath);
  //   File targetFile = new File(filepath);
  //   OutputStream outStream = new FileOutputStream(targetFile);
  //    bos = new BufferedOutputStream(outStream);
  //   bos.write(buffer);	
		int bytesRead;
		int current = 0;
		byte [] mybytearray  = new byte [FILE_SIZE];
		InputStream is = sock.getInputStream();
		fos = new FileOutputStream(filepath);
		bos = new BufferedOutputStream(fos);
		bytesRead = is.read(mybytearray,0,mybytearray.length);
		current = bytesRead;

		do {
			bytesRead =
			is.read(mybytearray, current, (mybytearray.length-current));
			if(bytesRead >= 0) current += bytesRead;
		} while(bytesRead > -1);

		bos.write(mybytearray, 0 , current);
		bos.flush();
		System.out.println("File " + FILE_TO_RECEIVED
			+ " downloaded (" + current + " bytes read)");
	}

	public void sendFile(Socket sock) throws IOException{
		try {
			System.out.print("Enter File Name :");
			this.br = new BufferedReader(new InputStreamReader(System.in));
			String fileName=this.br.readLine();
			this.dos.writeUTF(fileName);
			try {

				System.out.println("Accepted connection : " + sock);
					// send file
				String filepath = FILE_TO_SEND + fileName;
				File myFile = new File (filepath);
				byte [] mybytearray  = new byte [(int)myFile.length()];
					// to read byte oriented data from file
				fis = new FileInputStream(myFile);
				bis = new BufferedInputStream(fis);
					// It read the bytes from the specified byte-input stream into a specified byte array
				bis.read(mybytearray,0,mybytearray.length);
				os = sock.getOutputStream();
				System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
				os.write(mybytearray,0,mybytearray.length);
				os.flush();
				System.out.println("Done.");
			}catch(Exception e){}

		}catch(Exception e){}

	}

	public void displayMenu() throws Exception{
		sock = new Socket(SERVER, SOCKET_PORT);
		this.dos = new DataOutputStream(sock.getOutputStream());
		this.dis=new DataInputStream(sock.getInputStream());
		// while(true){    
			System.out.println("[ MENU ]");
			System.out.println("1. Send File");
			System.out.println("2. Receive File");
			System.out.println("3. Exit");
			System.out.print("\nEnter Choice :");
			try{
				int choice=0;
				br =  new BufferedReader(new InputStreamReader(System.in)); 
				choice=Integer.parseInt(br.readLine());
				System.out.println("choice"+choice);
				if(choice==1)
				{ 
					this.dos.writeUTF("SEND");
					this.sendFile(sock);
				}
				else if(choice==2)
				{
					this.dos.writeUTF("GET");
					this.recieveFile(sock);
				}
				else
				{
					this.dos.writeUTF("DISCONNECT");
					System.exit(1);
				}
			}
			catch(Exception e){System.out.print(e);}
		// }

	}

}