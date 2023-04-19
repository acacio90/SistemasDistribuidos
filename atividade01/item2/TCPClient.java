package item2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Console;

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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Scanner;

public class TCPClient {
  public static void main(String args[]) throws Exception {
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
              res = handleDelete(c_split[1], clientSocket);
              break;
          case "GETFILESLIST":
              res = handleGetFilesList(clientSocket);
              break;
          case "GETFILE":
              res = handleGetFile(c_split[1], clientSocket);
              break;
          case "EXIT":
              break;
          default:
              break;
      }

        if (cmd.equals("PARAR"))
          break;

        byte[] mensagemBytes = new byte[500];
        int tamanhoMensagem = in.read(mensagemBytes);
        ByteArrayInputStream mensagem = new ByteArrayInputStream(mensagemBytes);
          
        byte tipoMensagem = (byte) mensagem.read();
        byte codigoComando = (byte) mensagem.read();
        byte status = (byte) mensagem.read();

        if(codigoComando == 3){
          byte tamanhoVetor = (byte) mensagem.read();
          for(int i=0; i<tamanhoVetor; i++){
            // Lê o tamanho do nome do arquivo
            int tamanhoNome = mensagem.read();

            // Cria um buffer para armazenar o nome do arquivo
            byte[] nomeArquivoBytes = new byte[tamanhoNome];

            // Lê o nome do arquivo
            mensagem.read(nomeArquivoBytes);

            // Converte o nome do arquivo para uma string
            String nomeArquivo = new String(nomeArquivoBytes);

            // Exibe o nome do arquivo
            System.out.println("Arquivo " + (i+1) + ": " + nomeArquivo);
          }
        }else if (codigoComando == 4) {
          ByteArrayOutputStream message = new ByteArrayOutputStream();

          if (status != 1) {
            throw new Exception("Erro ao enviar mensagem para o servidor.");
          }
          System.out.println(codigoComando);
      
          // Lê os bytes do arquivo
          byte[] arquivoBytes = new byte[500];
          System.out.println(c_split[1]);
          try (FileOutputStream fileOutputStream = new FileOutputStream("item2/files1/" + c_split[1])) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = mensagem.read(arquivoBytes)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
    
            message.write(0x02); // mensagem tipo 2
            message.write(0x01); // comando 1
            message.write(0x01); // Status 1 sucesso
            byte[] messageOut = message.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            message.write(0x02); // mensagem tipo 2
            message.write(0x01); // comando 1
            message.write(0x02); // Status 2 error
            byte[] messageOut = message.toByteArray();
        }
          // Cria o arquivo no disco local
          FileOutputStream fileOutputStream = new FileOutputStream("item2/files2/" + c_split[1]);
          fileOutputStream.write(arquivoBytes);
          fileOutputStream.close();
      
          System.out.println("Arquivo " + c_split[1] + " recebido com sucesso!");
      }
      
        System.out.println(status);
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

  public static String handleGetFile(String fileName, Socket socket) {
    try {
        String filePath = "item2/files1/" + fileName;
        System.out.println(filePath);
        File file = new File(filePath);
        long fileSize = file.length();

        // Cria a mensagem a ser enviada para o servidor
        ByteArrayOutputStream messageOut = new ByteArrayOutputStream();
        messageOut.write(0x01); // mensagem tipo 1
        messageOut.write(0x04); // comando 4
        messageOut.write(file.getName().getBytes().length); // tamanho do nome do arquivo
        messageOut.write(file.getName().getBytes()); // nome do arquivo em bytes
        messageOut.write((int) fileSize); // tamanho do arquivo em bytes

        // Envia a mensagem para o servidor
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(messageOut.toByteArray());
        return "1";
    } catch (Exception e) {
        System.out.println("Erro ao baixar o arquivo " + fileName + ": " + e.getMessage());
        return "-1";
    }
}



  public static String handleDelete(String fileName, Socket socket) {
    ByteArrayOutputStream message = new ByteArrayOutputStream();
    String name = ("item1/files2/"+fileName);
    File file = new File(name);
    try {
      message.write(0x01); // mensagem tipo 1
      message.write(0x02); // comando 2
      message.write(file.getName().length()); // tamanho do nome do arquivo
      message.write(file.getName().getBytes()); // nome do arquivo em bytes
      byte[] mensagemBytes = message.toByteArray();
      OutputStream outputStream = socket.getOutputStream();
      outputStream.write(mensagemBytes);
      return "1";

    } catch (Exception e) {
      return "-1";
    }
  }

  public static String handleGetFilesList(Socket socket) {
    ByteArrayOutputStream message = new ByteArrayOutputStream();
    try {
      message.write(0x01); // mensagem tipo 1
      message.write(0x03); // comando 2
      byte[] mensagemBytes = message.toByteArray();
      OutputStream outputStream = socket.getOutputStream();
      outputStream.write(mensagemBytes);
      return "1";

    } catch (Exception e) {
      return "-1";
    }
  }
}