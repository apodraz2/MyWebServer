
//referenced libraries
import java.io.*;
import java.net.*;

//class InetServer
public class MyWebServer {
	
	//boolean determines whether loop continues to run,
	//value upon initialization is true
	public static boolean controlSwitch = true;
	
	public static void main(String [] args) throws IOException {
		//max number of arguments
		int q_len = 6;
		//specified port
		int port = 5418;
		
		//This variable will be assigned the client connection
		Socket sock;
		
		//create new ServerSocket and initialize with port and q_len variables
		ServerSocket servsock = new ServerSocket(port, q_len);
		
		//Print out line of text specifying whose server and starting up
		System.out.println("Adam Podraza's Inet Server starting up,"
				+ " listening at port " + port + " \n");
		
		//While the controlSwitch boolean is true, this while loop
		//runs and continuously accepts client connections.
		//For each new client connection, a new worker thread is created
		//to handle it.
		while (controlSwitch) {
			//local variable sock receives client connection
			sock = servsock.accept();
			
			//new worker thread is spawned to handle client requests.
			if (controlSwitch) new Worker(sock).start();
		}
		//close server's connection
		servsock.close();
	}
	
	
}
//Worker class is subclass of Thread, extending thread allows for multiple client connections to server
class Worker extends Thread { 
	//field is local to Worker
	Socket sock;	
	
	//Worker constructor assigns Socket argument to local field
	Worker (Socket s) { 
		sock = s;
	}
	
	public void run() {
		
		//out is local reference to PrintStream provided by argument Socket, not assigned yet
		PrintStream out = null;
		//in is local reference to InputStreamReader provided by argument Socket, not assigned yet
		BufferedReader in = null;
		try {
			//assign in to BufferedReader casted InputStream provided by argument passed through constructor
			in = new BufferedReader
					(new InputStreamReader(sock.getInputStream()));
			//assign out to PrintStream casted OutputStream provided by argument passed through constructor
			out = new PrintStream(sock.getOutputStream());
			
			//if client types "shutdown" server will shutdown.
			//this case prints a shutdown message.
			if(MyWebServer.controlSwitch != true) {
				System.out.println
				("Listener is now shutting down as per client request.");
				out.println("Server is now shutting down. Goodbye!");
			}
			
			else try {
				//local reference to client passed name
				String name;
				//while(MyWebServer.controlSwitch == true) {
					//assign local name reference to String received through
					//client passed input stream.
					name = in.readLine();
				
					//primary use case
					//worker will look up the domain name provided by client
					printRemoteAddress(name, out);
				//}
			} catch (IOException x) {
				System.out.println("Server read error");
				x.printStackTrace();
			}
			
			//close connection on shutdown
			sock.close();
		} catch (IOException ioe) {System.out.println(ioe);}
	}
	
	static void printRemoteAddress(String name, PrintStream out) {
		out.println("Looking up " + name + "...");
	}
		//Not Interesting
		static String toText (byte ip[]) {
			StringBuffer result = new StringBuffer();
			for(int i = 0; i < ip.length; ++ i) {
				if (i>0) result.append(".");
				result.append(0xff & ip[i]);
			}
			return result.toString();
		}
}
