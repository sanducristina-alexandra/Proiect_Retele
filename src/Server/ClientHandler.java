package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import Server.Server;

public class ClientHandler implements Runnable{
	
	private Socket socket;
	private BufferedReader reader;
	private PrintWriter writer;
	
	public ClientHandler(Socket socket) throws IOException {
		this.socket = socket;
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.writer = new PrintWriter(socket.getOutputStream());
	}

	@Override
	public void run() {
		while (!socket.isClosed()) {
			try {
				String command = reader.readLine();
				if ("exit".equals(command.strip())) {
					System.out.println("closed");
					socket.close();
				} else if("select".equals(command.strip())) {
					System.out.println("hello");
					writer.println("Adauga codul cartii: ");
					writer.flush();
					int cod = Integer.parseInt(reader.readLine());
					writer.println(Server.selectMap(cod));
					writer.flush();
				}else if("delete".equals(command.strip())){
					writer.println("Adauga codul cartii: ");
					writer.flush();
					int cod = Integer.parseInt(reader.readLine());
					writer.println(Server.stergeCarte(cod));
					writer.flush();
                    //Server.getClienti().forEach(client->{
                    //    if(client!=socket) {
                    //    	writer.println("S-a sters cartea " + cod);
                    //    }
                   // });
				 }else if("update".equals(command.strip())) {
					 writer.println("Adauga codul cartii, noul titlu, noul autor si noul nr de pagini separate prin virgula: ");
					 writer.flush();
					 String string = reader.readLine();
					 String[] vector = string.split(",");
					 int cod = Integer.parseInt(vector[0]);
					 String titluNou = vector[1];
					 String autorNou = vector[2];
					 int nrPaginiNou = Integer.parseInt(vector[3]);
					 writer.println(Server.modificaCarte(cod, titluNou, autorNou, nrPaginiNou));
					 writer.flush();
				 }
			} catch (Exception e) {
				writer.println(e.getMessage());
				writer.flush();
			}	
		}		
	}
	
	
}
