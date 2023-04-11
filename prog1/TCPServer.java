package prog1;

import java.io.IOException;
/**
 * TCPServer: Servidor para conexao TCP com Threads Descricao: Recebe uma
 * conexao, cria uma thread, recebe uma mensagem e finaliza a conexao
 */
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

  public static void main(String args[]) {

    try {
      int serverPort = 6666; // porta do servidor

      /* cria um socket e mapeia a porta para aguardar conexao */
      ServerSocket listenSocket = new ServerSocket(serverPort);

      while (true) {
        System.out.println("Servidor aguardando conexao ...");

        /* aguarda conexoes */
        Socket clientSocket = listenSocket.accept();

        System.out.println("Cliente conectado ... Criando thread ...");

        /* cria um thread para atender a conexao */
        ClientThread c = new ClientThread(clientSocket);

        /* inicializa a thread */
        c.start();
      } // while

    } catch (IOException e) {
      System.out.println("Listen socket:" + e.getMessage());
    } // catch
  } // main
} // class