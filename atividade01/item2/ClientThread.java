package item2;

import java.io.ByteArrayInputStream;

/* 
 * ClientThread: Thread de cliente
 * Descricao: Define a rotina da thread criando os buffers de entrada e saída; e realiza as devidas chamadas
 * para os comando requisitados
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
        while (true) {
          byte[] mensagemBytes = new byte[500];
          int tamanhoMensagem = in.read(mensagemBytes);
          ByteArrayInputStream mensagem = new ByteArrayInputStream(mensagemBytes);
          
          byte tipoMensagem = (byte) mensagem.read();
          byte codigoComando = (byte) mensagem.read();
          byte tamanhoNomeArquivo = (byte) mensagem.read();
          byte[] nomeArquivoBytes = new byte[tamanhoNomeArquivo];
          mensagem.read(nomeArquivoBytes, 0, tamanhoNomeArquivo);
          String nomeArquivo = new String(nomeArquivoBytes);
          byte[] dadosArquivo = new byte[500];
          mensagem.read(dadosArquivo);

          System.out.println(tipoMensagem);
          System.out.println(codigoComando);
          System.out.println(tamanhoMensagem);
          System.out.println(nomeArquivo);
          System.out.println(dadosArquivo);

          // System.out.println("Cliente disse: " + buffer);
          // c.setCommand();
          // Split no buffer
          // String[] c_split = buffer.split(" ");
  
          switch(codigoComando) {
            case 1:
                c.handleAddFile(nomeArquivo, dadosArquivo);
                break;
            // case "DELETE":
            //     // buffer = c.handlePwd();
            //     break;
            // case "GETFILESLIST":
            //     buffer = c.handleGetFileList();
            //     break;
            // case "GETFILE":
            //     // buffer = c.handlePwd();
            //     break;
            // case "EXIT":
            //     break;
            // default:
            //     break;
        }
  
          // out.writeUTF(buffer);
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
    } 
}
