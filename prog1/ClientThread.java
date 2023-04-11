package prog1;

/* 
 * ClientThread: Thread responsavel pela comunicacao
 * Descricao: Rebebe um socket, cria os objetos de leitura e escrita,
 * aguarda msgs clientes e chama o método responsável pelo comando chamado.
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
import java.net.Socket;

class ClientThread extends Thread {

    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
  
    public ClientThread(Socket clientSocket) {
      try {
        this.clientSocket = clientSocket;
        in = new DataInputStream(clientSocket.getInputStream());
        out = new DataOutputStream(clientSocket.getOutputStream());
      } catch (IOException ioe) {
        System.out.println("Connection:" + ioe.getMessage());
      } // catch
    } // construtor
  
    /* metodo executado ao iniciar a thread - start() */
    @Override
    public void run() {
      try {
        Command c = new Command();
        String buffer = "";
        while (true) {
          buffer = in.readUTF(); /* aguarda o envio de dados */
  
          System.out.println("Cliente disse: " + buffer);
          c.setCommand(buffer);
          // Split no buffer
          String[] c_split = buffer.split(" ");
  
          switch(c_split[0]) {
            case "CONNECT":
                buffer = c.handleConnect();
                break;
            case "PWD":
                if (buffer.equals("PWD"))
                    buffer = c.handlePwd();
                break;
            case "CHDIR":
                buffer = c.handleChdir();
                break;
            case "GETFILES":
                if (buffer.equals("GETFILES"))
                    buffer = c.handleGetfiles();
                break;
            case "GETDIRS":
                if (buffer.equals("GETDIRS"))
                    buffer = c.handleGetdirs();
                break;
            case "EXIT":
                break;
            default:
                break;
        }
  
          out.writeUTF(buffer);
        }
      } catch (EOFException eofe) {
        System.out.println("EOF: " + eofe.getMessage());
      } catch (IOException ioe) {
        System.out.println("IOE: " + ioe.getMessage());
      } finally {
        try {
          in.close();
          out.close();
          clientSocket.close();
        } catch (IOException ioe) {
          System.err.println("IOE: " + ioe);
        }
      }
      System.out.println("Thread comunicação cliente finalizada.");
    } // run
}
