package prog1;

/* 
 * TCPServer: Servidor TCP
 * Descricao: Espera conexão e executa a thread cliente
 * 
 * Autores:
 *       Iago Sasaki
 *       Pedro Acácio
 * 
 * Data de Criacao: 06 de abril de 2023
 * Ultima atualizacao: 11 de abril de 2023
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

  /** 
   * @param args[]
   */
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
      }

    } catch (IOException e) {
      System.out.println("Listen socket:" + e.getMessage());
    }
  }
}