package item2;

import java.io.ByteArrayOutputStream;

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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TCPClient {
  public static void main(String args[]) {
    Socket clientSocket = null; // socket do cliente
    Scanner reader = new Scanner(System.in); // ler mensagens via teclado
    String res = "";
    
    try {
      /* Endereço e porta do servidor */
      int serverPort = 6666;
      InetAddress serverAddr = InetAddress.getByName("127.0.0.1");
      String cmd = "";
    
      /* conecta com o servidor */
      clientSocket = new Socket(serverAddr, serverPort);

      /* cria objetos de leitura e escrita */
      DataInputStream in = new DataInputStream(clientSocket.getInputStream());
      DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

      /* protocolo de comunicação */
      while (true) {
        System.out.print("> ");
        cmd = reader.nextLine(); // lê mensagem via teclado
        String[] c_split = cmd.split(" ");

        switch(c_split[0]) {
          case "ADDFILE":
              res = handleAddFile(c_split[1], clientSocket);
              break;
          case "DELETE":
              // buffer = c.handlePwd();
              break;
          case "GETFILESLIST":
              // buffer = c.handleGetFileList();
              break;
          case "GETFILE":
              // buffer = c.handlePwd();
              break;
          case "EXIT":
              break;
          default:
              break;
      }

        if (cmd.equals("PARAR"))
          break;

        cmd = in.readUTF(); // aguarda resposta "do servidor
        System.out.println(cmd);
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
  public static String handleAddFile(String fileName, Socket socket) {
    String name = ("item2/files2/"+fileName);
    File file = new File(name);
    ByteArrayOutputStream message = new ByteArrayOutputStream();

    try {
      FileInputStream fileInputStream = new FileInputStream(file);
      byte[] fileBytes = new byte[(int) file.length()];
      fileInputStream.read(fileBytes);

      message.write(0x01); // mensagem tipo 1
      message.write(0x01); // comando 1
      message.write(file.getName().length()); // tamanho do nome do arquivo
      message.write(file.getName().getBytes()); // nome do arquivo em bytes
      message.write(fileBytes); // dados do arquivo

      byte[] mensagemBytes = message.toByteArray();

      OutputStream outputStream = socket.getOutputStream();

      outputStream.write(mensagemBytes);
      return "1";

    } catch (Exception e) {
      return "-1";
    }
  }
}