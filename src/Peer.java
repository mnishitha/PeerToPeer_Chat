import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.json.Json;
public class Peer {
	
	public static void main(String args[]) throws Exception {
		Scanner sc = new Scanner(System.in);
		char input;
		 String LogicalName;
		int portno; 
		System.out.println("Choose one of the following operations to perform :\n"
				+ " J - JOIN \n M - SENDMESSAGE \n L - LEAVE\n");
		input = sc.next().charAt(0);
		if(input == 'J') { 
			System.out.println( "for join");
       BufferedReader buf_reader = new BufferedReader(new InputStreamReader(System.in));
         System.out.println("Enter logical name");
         LogicalName = buf_reader.readLine();
			System.out.println("Enter portno to be connected");
		portno = Integer.parseInt(buf_reader.readLine());
	        //active_Node.Join(node_info.LogicalName,node_info.portno);
         ActiveNode active_Node = new ActiveNode(portno);
         active_Node.start();
         new Peer().updateListentoPeers(buf_reader,LogicalName,active_Node);
		}
		sc.close();
}
	public void updateListentoPeers(BufferedReader buf_reader,String username,ActiveNode active_Node) throws  Exception{
		System.out.println("Enter hostname and port");
		//System.out.println("Peers to receive messages from (s to skip)");
		String inputs = buf_reader.readLine();
		String[] inputvalues = inputs.split(" ");
		if(!inputs.equals("s")) 
			for(int i = 0 ; i < inputvalues.length; i++) {
				String[] address = inputvalues[i].split(":");
				System.out.println("address" + address[1]);
		        Socket socket = null;
		        try {
		        	socket = new Socket(address[0],Integer.valueOf(address[1]));
		        	new ChatNode(socket).start();
		        }
		        catch(Exception e) {
		        	if(socket != null) socket.close();
		        	else System.out.println("Invalid input skip tonext line");
		        }
			}
			Communicate(buf_reader,username,active_Node);
	    
	}
	public void Communicate(BufferedReader buf_readere,String username,ActiveNode active_Node) throws Exception{
		
		try {
			System.out.println("> u can communicate[e exit, c change]");
			boolean flag = true;
			while(flag) {
				String message = buf_readere.readLine();
				if(message.equals("e")) {
					flag = false;
					break;
				}
				else if(message.equals("c")) {
					updateListentoPeers(buf_readere,username,active_Node);
					}
				else {
					StringWriter stringwriter = new StringWriter();
					Json.createWriter(stringwriter).writeObject(Json.createObjectBuilder()
							.add("username",username)
							.add("message", message)
							.build());
					active_Node.sendMessage(stringwriter.toString());
			}
			}
			System.exit(0);
					
			}
		catch(Exception e) {
			System.out.println("Invalid input");
		}
	
		}

}


/*if(input == 'J') { 
	System.out.println( "for join");
         			System.out.println("Enter logical name to be connected");
         			node_info.LogicalName = buf_reader.readLine();
         			System.out.println("Enter port no to be connected");
         			node_info.portno = Integer.parseInt(buf_reader.readLine());
         	        active_Node.Join(node_info.LogicalName,node_info.portno);
         		
    System.out.println("for Message");
					
    System.out.println("To leave");
				**/