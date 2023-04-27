import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class Chat2p2{
	public static void main(String[] args) throws IOException{
		Scanner scanner = new Scanner(System.in);
	
		System.out.print("Porta Entrada : ");
		int serverPort = Integer.parseInt(scanner.nextLine());
		
		System.out.print("IP : ");
		String IP = scanner.nextLine();
		
		System.out.print("Porta Destino : ");
		int destinationPort = Integer.parseInt(scanner.nextLine());
		
		Channel channel = new Channel();
		channel.bind(serverPort);
		channel.start();
		
		System.out.println("Chat Aberto.");

		System.out.print("Nome : ");
		String nome = scanner.nextLine();
        byte[] apelido = nome.getBytes();
        byte tamanhoApelido = (byte)apelido.length;

		String texto = "";
        byte[] mensagem;
        byte tamanhoMensagem;
    	
		InetSocketAddress address = new InetSocketAddress(IP, destinationPort);
		String tipo = "EXIT";
		while(true){
			String msg = "";
			if(tipo == "EXIT" || texto == "0"){
			System.out.print("Tipo de mensagem:\n 1 - Chat\n 2 - emoji\n 3 - URL\n 4 - ECHO\n");
			tipo = scanner.nextLine();
			}
			byte[] tipoMensagem = tipo.getBytes();
			switch(tipo){
				case "1":
					System.out.print("Mensagem : ");
					texto = scanner.nextLine();
					//System.out.println(texto);
					mensagem = texto.getBytes();
					tamanhoMensagem = (byte)mensagem.length;
				break;
				case "2":
					texto = "*-*";
					mensagem = texto.getBytes();
					tamanhoMensagem = (byte)mensagem.length;
					tipo = "EXIT";
				break;
				case "4":
					texto = "ECHO";
					mensagem = texto.getBytes();
					tamanhoMensagem = (byte)mensagem.length;
					tipo = "EXIT";
				break;
			}
			if(texto.isEmpty())
				texto = "0";
			if(texto.equals("EXIT"))
				break;
			msg = nome + " : " + texto;
			channel.sendTo(address, texto, msg);
			System.out.println(msg);
		}
		scanner.close();
		channel.stop();
		
		System.out.println("Closed.");
	}
}