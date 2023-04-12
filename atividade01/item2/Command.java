package item2;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

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
    public byte[] handleAddFile(String fileName , byte[] dadosArquivo) {
      String name = ("item2/files2/"+fileName);
      File file = new File(name);
      ByteArrayOutputStream message = new ByteArrayOutputStream();

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
        
        message.write(0x02); // mensagem tipo 2
        message.write(0x01); // comando 1
        message.write(0x01); // Status 1 sucesso
        byte[] mensagemBytes = message.toByteArray();

        return mensagemBytes;
      } catch (Exception e) {
        message.write(0x02); // mensagem tipo 2
        message.write(0x01); // comando 1
        message.write(0x02); // Status 2 error
        byte[] mensagemBytes = message.toByteArray();
        return mensagemBytes;
      }

    }

    public byte[] handleGetFile(String fileName) {
      String name = ("item2/files1/"+fileName);
      File file = new File(name);
      ByteArrayOutputStream message = new ByteArrayOutputStream();

      try {
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] fileBytes = new byte[(int) file.length()];
        fileInputStream.read(fileBytes);

        message.write(0x02); // mensagem tipo 1
        message.write(0x04); // comando 1
        message.write(0x01); // comando 1
        message.write(file.getName().length()); // tamanho do nome do arquivo
        message.write(file.getName().getBytes()); // nome do arquivo em bytes
        message.write(fileBytes); // dados do arquivo

        byte[] mensagemBytes = message.toByteArray();

        return mensagemBytes;
      } catch (Exception e) {
        message.write(0x02); // mensagem tipo 2
        message.write(0x01); // comando 1
        message.write(0x02); // Status 2 error
        byte[] mensagemBytes = message.toByteArray();
        return mensagemBytes;
      }

    }
  
    /** 
     * Tratamento do comando GETFILES
     */
    public byte[] handleGetFilesList() {
      ByteArrayOutputStream message = new ByteArrayOutputStream();
      try {
        File[] listOfFiles = file.listFiles();
        int num = 0;
        String res = new String("");
  
        
        message.write(0x02); // mensagem tipo 2
        message.write(0x03); // comando 1
        message.write(0x01); // Status 2 error
        for (int i = 0; i < listOfFiles.length; i++) {
          if (listOfFiles[i].isFile()) {
            num++;
          }
        }
        message.write(num);
        for (int i = 0; i < listOfFiles.length; i++) {
          if (listOfFiles[i].isFile()) {
            message.write(listOfFiles[i].getName().length());
            message.write(listOfFiles[i].getName().getBytes());
            num++;
          }
        }
        byte[] mensagemBytes = message.toByteArray();

        return mensagemBytes;
      } catch (Exception e) {
        message.write(0x02); // mensagem tipo 2
        message.write(0x03); // comando 1
        message.write(0x02); // Status 2 error
        byte[] mensagemBytes = message.toByteArray();
        return mensagemBytes;
      }
    }

    public byte[] handleDelete(String fileName) {
      ByteArrayOutputStream message = new ByteArrayOutputStream();
      try {
        // Define o diretório de destino para o arquivo recebido
        String diretorioDestino = "item2/files1/";

        // Define o caminho completo do arquivo de destino
        String caminhoCompletoArquivo = diretorioDestino + fileName;

        File file = new File(caminhoCompletoArquivo);
        file.delete();
        
        message.write(0x02); // mensagem tipo 2
        message.write(0x02); // comando 2
        message.write(0x01); // Status 1 sucesso
        byte[] mensagemBytes = message.toByteArray();

        return mensagemBytes;
      } catch (Exception e) {
        message.write(0x02); // mensagem tipo 2
        message.write(0x02); // comando 2
        message.write(0x02); // Status 2 error
        byte[] mensagemBytes = message.toByteArray();
        return mensagemBytes;
      }

    }
}