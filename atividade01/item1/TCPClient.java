package atividade01.item1;

/* 
 * TCPClient: Client TCP para estabeler conexao com servidor
 * Descricao: Tenta estabelecer uma conexão TCP com o servidor, apos isso fica ouvindo os 
 * comando utilizado e envia ao servidor esperando por respostas.
 * 
 * 
 * Autores:
 *       Iago Sasaki
 *       Pedro Acácio
 * 
 * Data de Criacao: 06 de abril de 2023
 * Ultima atualizacao: 11 de abril de 2023
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.Scanner;

public class TCPClient {

  
  /** 
   * Funcao que obtem o hash de uma string e a retorna
   * @param password
   * @return String
   */
  static String getSHA512(String password) {
    String toReturn = null;
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-512");
      digest.reset();
      digest.update(password.getBytes("utf8"));
      toReturn = String.format("%0128x", new BigInteger(1, digest.digest()));
    } catch (Exception e) {
      e.printStackTrace();
    }

    return toReturn;
  }

  public static void main(String args[]) {
    Socket clientSocket = null; // socket do cliente
    Scanner reader = new Scanner(System.in); // ler mensagens via teclado

    try {
      /* Endereço e porta do servidor */
      int serverPort = 6666;
      InetAddress serverAddr = InetAddress.getByName("127.0.0.1");
      String buffer = "";

      /* conecta com o servidor */
      clientSocket = new Socket(serverAddr, serverPort);

      /* cria objetos de leitura e escrita */
      DataInputStream in = new DataInputStream(clientSocket.getInputStream());
      DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

      while (true) {
        System.out.println("Aguardando conexao");
        buffer = reader.nextLine(); // lê mensagem via teclado

        String[] command = buffer.split(" ");
        String[] userPassword = command[1].split(",");

        String passwordHash = getSHA512(userPassword[1]);

        out.writeUTF(command[0] + " " + userPassword[0] + "," + passwordHash); // envia a mensagem para o servidor

        buffer = "";

        buffer = in.readUTF(); // aguarda resposta "do servidor

        if (buffer.equals("SUCCESS")) {
          System.out.println(buffer);
          break;
        } else {
          System.out.println(buffer);
        }
        buffer = "";

      }

      /* protocolo de comunicação */
      while (true) {
        System.out.print("> ");
        buffer = reader.nextLine(); // lê mensagem via teclado

        out.writeUTF(buffer); // envia a mensagem para o servidor

        if (buffer.equals("PARAR"))
          break;

        buffer = in.readUTF(); // aguarda resposta "do servidor
        System.out.println("Server disse: " + buffer);
      }
    } catch (UnknownHostException ue) {
      System.out.println("Socket:" + ue.getMessage());
    } catch (EOFException eofe) {
      System.out.println("EOF:" + eofe.getMessage());
    } catch (IOException ioe) {
      System.out.println("IO:" + ioe.getMessage());
    } finally {
      try {
        clientSocket.close();
      } catch (IOException ioe) {
        System.out.println("IO: " + ioe);
        ;
      }
    }
  }
}