package item2;

import java.io.ByteArrayOutputStream;

/*
 * Command: Classe contendo os tratamentos para cada chamada de um comando conhecido e atributos necessários.
 * Comandos Conhecidos: CONNECT
 *                      PWD
 *                      CHDIR
 *                      GETFILES
 *                      GETDIRS
 * 
 * Autores:
 *       Iago Sasaki
 *       Pedro Acácio
 * 
 * Data de Criacao: 06 de abril de 2023
 * Ultima atualizacao: 11 de abril de 2023
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.Socket;

class Command {

    String command;
    File file = new File("item2/files1/");
    String rootPath = file.getAbsolutePath();
  
    public Command() {
  
    }
  
    
    /** 
     * Padroniza a string para caixa baixa
     * @param command
     */
    public void setCommand(String command) {
      this.command = command.toLowerCase();
    }
  
    /** 
     * Tratamento do comando ADDFILE
     */
    public String handleAddFile(String fileName , byte[] dadosArquivo) {
      String name = ("item2/files2/"+fileName);
      File file = new File(name);

      try {
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] fileBytes = new byte[(int) file.length()];
        fileInputStream.read(fileBytes);

        // Define o diretório de destino para o arquivo recebido
        String diretorioDestino = "item2/files1/";

        // Define o caminho completo do arquivo de destino
        String caminhoCompletoArquivo = diretorioDestino + fileName;
        System.out.println(caminhoCompletoArquivo);
        // Cria um objeto FileOutputStream para escrever o arquivo recebido
        FileOutputStream fileOutputStream = new FileOutputStream(caminhoCompletoArquivo);

        // Escreve os bytes do arquivo recebido no arquivo de saída
        // int tamanhoDadosArquivo = mensagemBytes.length - 500;
        fileOutputStream.write(dadosArquivo);

        // Fecha o objeto FileOutputStream
        fileOutputStream.close();

        return "1";
      } catch (Exception e) {
        System.err.println("ERRO: " + e);
        return "-1";
      }
    }
  
    /** 
     * Tratamento do comando CHDIR
     */
    public String handleChdir() {
      try {
        String[] command_split = command.split(" ");
        file = new File(command_split[1]);
  
        if (!file.exists())
          throw new Exception("ERRO");
        System.out.println(command_split[1]);
  
        return "SUCCESS";
  
      } catch (Exception e) {
        return "ERROR";
      }
    }
  
    /** 
     * Tratamento do comando GETFILES
     */
    public String handleGetFileList() {
      try {
        File[] listOfFiles = file.listFiles();
        int num = 0;
        String res = new String("");
  
        for (int i = 0; i < listOfFiles.length; i++) {
          if (listOfFiles[i].isFile()) {
            res = res.concat(listOfFiles[i].getName() + "\n");
            num++;
          }
          ;
        }
  
        res = "Numero de arquivos: " + num + "\n" + res;
  
        return res;
      } catch (Exception e) {
        System.err.println("ERRO: " + e);
        return "-1";
      }
    }

    /** 
     * Tratamento do comando GETDIRS
     */
    public String handleGetdirs() {
        try {
          File[] listOfFiles = file.listFiles();
          int num = 0;
          String res = new String("");
    
          for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isDirectory()) {
              res = res.concat(listOfFiles[i].getName() + "\n");
              num++;
            }
          }
    
          res = "Numero de diretórios: " + num + "\n" + res;
    
          return res;
        } catch (Exception e) {
          return "ERROR";
        }
      }
}