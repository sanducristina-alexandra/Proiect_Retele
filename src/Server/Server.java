package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class Server implements AutoCloseable{

	private ServerSocket serverSocket;
	private static Connection connection;
	private static Map<Integer, Carte> carti = new HashMap<>();
	private static List<Socket> clienti = Collections.synchronizedList(new ArrayList<Socket>());
	
	public Server(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		ExecutorService executorService = Executors.newFixedThreadPool(10 * Runtime.getRuntime().availableProcessors());
		executorService.execute(() -> {
			conexiuneBD();
			while (!serverSocket.isClosed()) {
				try {
					final Socket socket = serverSocket.accept();
					clienti.add(socket);
					executorService.submit(new ClientHandler(socket));
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	
	public static void conexiuneBD() {
		connection  = null;
		try {
			Class.forName("org.sqlite.JDBC");
			connection = 
					DriverManager.getConnection("jdbc:sqlite:database.db");
			connection.setAutoCommit(false);
			creareTabela(connection);
			inserare(connection);
			inserareDictionar(connection);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void creareTabela(Connection connection) {
		String sqlDrop = "DROP TABLE IF EXISTS Carti";
		String sqlCreate = "CREATE TABLE Carti(cod INTEGER PRIMARY KEY, " +
				"titlu TEXT, autor TEXT, numarPagini INTEGER)";
		
		Statement statement;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(sqlDrop);
			statement.executeUpdate(sqlCreate);
			statement.close();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void inserare(Connection connection) {
		
		String sqlInsertWithParams = "INSERT INTO Carti(titlu, autor, numarPagini) " +
										"VALUES(?, ?, ?)";
		try {

			PreparedStatement preparedStatement = connection.prepareStatement(sqlInsertWithParams);
			preparedStatement.setString(1, "Ipoteza Iubirii");
			preparedStatement.setString(2,"Ali Hazelwood");
			preparedStatement.setInt(3,336);	
			preparedStatement.executeUpdate();
			preparedStatement.close();
			connection.commit();
			
			
			PreparedStatement preparedStatement2 = connection.prepareStatement(sqlInsertWithParams);
			preparedStatement2.setString(1, "The Assassin's Blade");
			preparedStatement2.setString(2,"Sarah J. Maas");
			preparedStatement2.setInt(3,448);	
			preparedStatement2.executeUpdate();
			preparedStatement2.close();
			connection.commit();
			
			PreparedStatement preparedStatement3 = connection.prepareStatement(sqlInsertWithParams);
			preparedStatement3.setString(1, "The Red Queen");
			preparedStatement3.setString(2,"Victoria Aveyard");
			preparedStatement3.setInt(3,400);	
			preparedStatement3.executeUpdate();
			preparedStatement3.close();
			connection.commit();

			PreparedStatement preparedStatement4 = connection.prepareStatement(sqlInsertWithParams);
			preparedStatement4.setString(1, "Heartless");
			preparedStatement4.setString(2,"Marissa Meyer");
			preparedStatement4.setInt(3,544);	
			preparedStatement4.executeUpdate();
			preparedStatement4.close();
			connection.commit();

			PreparedStatement preparedStatement5 = connection.prepareStatement(sqlInsertWithParams);
			preparedStatement5.setString(1, "Totul se termina cu noi");
			preparedStatement5.setString(2,"Colleen Hoover");
			preparedStatement5.setInt(3,416);	
			preparedStatement5.executeUpdate();
			preparedStatement5.close();
			connection.commit();

			PreparedStatement preparedStatement6 = connection.prepareStatement(sqlInsertWithParams);
			preparedStatement6.setString(1, "Crima Perfecta");
			preparedStatement6.setString(2,"Holly Jackson");
			preparedStatement6.setInt(3,448);	
			preparedStatement6.executeUpdate();
			preparedStatement6.close();
			connection.commit();

			PreparedStatement preparedStatement7 = connection.prepareStatement(sqlInsertWithParams);
			preparedStatement7.setString(1, "Trei coroane intunecate");
			preparedStatement7.setString(2,"Kendare Blake");
			preparedStatement7.setInt(3,368);	
			preparedStatement7.executeUpdate();
			preparedStatement7.close();
			connection.commit();

			PreparedStatement preparedStatement8 = connection.prepareStatement(sqlInsertWithParams);
			preparedStatement8.setString(1, "Delirium");
			preparedStatement8.setString(2,"Lauren Oliver");
			preparedStatement8.setInt(3,192);	
			preparedStatement8.executeUpdate();
			preparedStatement8.close();
			connection.commit();

			PreparedStatement preparedStatement9 = connection.prepareStatement(sqlInsertWithParams);
			preparedStatement9.setString(1, "The Shadows Between Us");
			preparedStatement9.setString(2,"Tricia Levenseller");
			preparedStatement9.setInt(3,336);	
			preparedStatement9.executeUpdate();
			preparedStatement9.close();
			connection.commit();

			PreparedStatement preparedStatement10 = connection.prepareStatement(sqlInsertWithParams);
			preparedStatement10.setString(1, "Femeia de la fereastra");
			preparedStatement10.setString(2,"A.J. Finn");
			preparedStatement10.setInt(3,544);	
			preparedStatement10.executeUpdate();
			preparedStatement10.close();
			connection.commit();						
						
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void inserareDictionar(Connection connection) {
		String sqlSelect = "SELECT * FROM Carti";
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sqlSelect);
			while(rs.next()) {
				int cod = rs.getInt("cod");
				String titlu = rs.getString("titlu");
				String autor = rs.getString("autor");
				int nrPagini = rs.getInt("numarPagini");
				Carte carte = new Carte(cod, titlu, autor, nrPagini);
				carti.put(carte.getCod(),carte);
			    //System.out.println(carte);
			}
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static String selectMap(int cod) {
		if(carti.containsKey(cod)) {
			Carte carte = carti.get(cod);
			return carte.toString();
		}else {
			return "Nu exista cartea cu acest cod";
		}
	}
	
	public static String stergeCarte(int cod) {
		carti.remove(cod);
		stergeCarteDB(connection, cod);
		String rezultat = "S-a sters cartea care are codul " + cod;
		return rezultat;			 
	}
	

	
	public static void stergeCarteDB(Connection connection,int cod) {
	     String sqlDelete = "DELETE FROM Carti WHERE cod = ?";
	     try {
	    	 PreparedStatement preparedStatement = connection.prepareStatement(sqlDelete);
			 preparedStatement.setInt(1, cod);
			 preparedStatement.executeUpdate();
			 preparedStatement.close();
			 connection.commit();		 

		} catch (Exception e) {
			e.printStackTrace();
		}
		//selectDupaCod2(connection);	 
	    for(Socket client: clienti) {
	    	System.out.println(client);
	    }
	}
	
	public static String modificaCarte(int cod,String titlu, String autor, int nrPagini) {
		if(carti.containsKey(cod)) {
			Carte carte = carti.get(cod);
			carte.setTitlu(titlu);
			carte.setAutor(autor);
			carte.setNrPagini(nrPagini);
			modificaCarteBD(connection,cod,titlu,autor,nrPagini);
			return carte.toString();
		}else {
			return "Nu exista cartea cu acest cod";
		}
	}

	private static void modificaCarteBD(Connection connection, int cod, String titlu, String autor, int nrPagini) {
		String sqlUpdateString = "UPDATE Carti SET titlu = ?, autor = ?, numarPagini = ? WHERE cod = ?";
		try {
	    	 PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdateString);
	    	 preparedStatement.setString(1, titlu);
			 preparedStatement.setString(2,autor);
			 preparedStatement.setInt(3,nrPagini);
			 preparedStatement.setInt(4,cod);
			 preparedStatement.executeUpdate();
			 preparedStatement.close();
			 connection.commit();		 

		} catch (Exception e) {
			e.printStackTrace();
		}
		selectDupaCod2(connection);
	}

	public static void selectDupaCod2(Connection connection) {
		String sqlSelect = "SELECT * FROM Carti";
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sqlSelect);
			while(rs.next()) {
				int cod = rs.getInt("cod");
				String titlu = rs.getString("titlu");
				String autor = rs.getString("autor");
				int nrPagini = rs.getInt("numarPagini");
				Carte carte = new Carte(cod, titlu, autor, nrPagini);
			    System.out.println(carte);
			}
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static List<Socket> getClienti() {
		return clienti;
	}

	@Override
	public void close() throws Exception {
		serverSocket.close();
	}

}
