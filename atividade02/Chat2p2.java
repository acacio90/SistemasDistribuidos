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

        System.out.print("Tipo de mensagem: 1 - Chat, 2 - emoji, 3 - URL ou 4 - ECHO");
		String tipo = scanner.nextLine();
        byte[] tipoMensagem = tipo.getBytes();
        
        System.out.print("Nome : ");
		String nome = scanner.nextLine();
        byte[] apelido = nome.getBytes();
        byte tamanhoApelido = (byte)apelido.length;

		String texto;
        byte[] mensagem;
        byte tamanhoMensagem;
        
        switch(tipo){
            case "1":
                System.out.print("Mensagem : ");
                texto = scanner.nextLine();
                mensagem = texto.getBytes();
                tamanhoMensagem = (byte)mensagem.length;
            break;
            case "2":
                texto = "*-*";
                mensagem = texto.getBytes();
                tamanhoMensagem = (byte)mensagem.length;
            break;
            case "4":
                texto = "ECHO";
                mensagem = texto.getBytes();
                tamanhoMensagem = (byte)mensagem.length;
            break;
        }

		
		InetSocketAddress address = new InetSocketAddress(IP, destinationPort);
		
		while(true){
			String msg = scanner.nextLine();
			
			if(msg.isEmpty())
				break;
			msg = nome + " : " + msg;
			channel.sendTo(address, msg);
			System.out.println(msg);
		}

		scanner.close();
		channel.stop();
		
		System.out.println("Closed.");
	}
}