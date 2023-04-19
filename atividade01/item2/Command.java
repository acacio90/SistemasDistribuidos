package item2;

import java.io.ByteArrayInputStream;
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
import java.io.IOException;
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
    public byte[] handleAddFile(ByteArrayInputStream message) {
      byte tamanhoNomeArquivo = (byte) message.read();
      System.out.println(tamanhoNomeArquivo);
      byte[] nomeArquivoBytes = new byte[tamanhoNomeArquivo];
      message.read(nomeArquivoBytes, 0, tamanhoNomeArquivo);
      String fileName = new String(nomeArquivoBytes);
      ByteArrayOutputStream messageOut = new ByteArrayOutputStream();
  
      try (FileOutputStream fileOutputStream = new FileOutputStream("item2/files1/" + fileName)) {
          byte[] buffer = new byte[1024];
          int bytesRead;
          while ((bytesRead = message.read(buffer)) != -1) {
              fileOutputStream.write(buffer, 0, bytesRead);
          }
  
          messageOut.write(0x02); // mensagem tipo 2
          messageOut.write(0x01); // comando 1
          messageOut.write(0x01); // Status 1 sucesso
          byte[] mensagemBytes = messageOut.toByteArray();
  
          return mensagemBytes;
      } catch (IOException e) {
          e.printStackTrace();
          messageOut.write(0x02); // mensagem tipo 2
          messageOut.write(0x01); // comando 1
          messageOut.write(0x02); // Status 2 error
          byte[] mensagemBytes = messageOut.toByteArray();
          return mensagemBytes;
      }
  }
  

  public byte[] handleGetFile(ByteArrayInputStream message) {
    byte tamanhoNomeArquivo = (byte) message.read();
    byte[] nomeArquivoBytes = new byte[tamanhoNomeArquivo];
    message.read(nomeArquivoBytes, 0, tamanhoNomeArquivo);
    String fileName = new String(nomeArquivoBytes);
    ByteArrayOutputStream messageOut = new ByteArrayOutputStream();

    try {
        File file = new File("item2/files1/" + fileName);
        if (file.exists()) {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] fileBytes = new byte[(int) file.length()];
            fileInputStream.read(fileBytes);
            fileInputStream.close();
            messageOut.write(0x02); // mensagem tipo 2
            messageOut.write(0x04); // comando 4
            messageOut.write(0x01); // status 1 sucesso
            messageOut.write(fileBytes);
        } else {
            messageOut.write(0x02); // mensagem tipo 2
            messageOut.write(0x04); // comando 3
            messageOut.write(0x02); // status 2 error
        }
        byte[] mensagemBytes = messageOut.toByteArray();
        return mensagemBytes;
    } catch (Exception e) {
        e.printStackTrace();
        messageOut.write(0x02); // mensagem tipo 2
        messageOut.write(0x04); // comando 3
        messageOut.write(0x02); // status 2 error
        byte[] mensagemBytes = messageOut.toByteArray();
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

    public byte[] handleDelete(ByteArrayInputStream message) {
      byte tamanhoNomeArquivo = (byte) message.read();
      byte[] nomeArquivoBytes = new byte[tamanhoNomeArquivo];
      message.read(nomeArquivoBytes, 0, tamanhoNomeArquivo);
      String fileName = new String(nomeArquivoBytes);
      ByteArrayOutputStream messageOut = new ByteArrayOutputStream();
  
      try {
          File file = new File("item2/files1/" + fileName);
          if (file.delete()) {
              messageOut.write(0x02); // mensagem tipo 2
              messageOut.write(0x02); // comando 2
              messageOut.write(0x01); // status 1 sucesso
          } else {
              messageOut.write(0x02); // mensagem tipo 2
              messageOut.write(0x02); // comando 2
              messageOut.write(0x02); // status 2 error
          }
          byte[] mensagemBytes = messageOut.toByteArray();
          return mensagemBytes;
      } catch (SecurityException e) {
          e.printStackTrace();
          messageOut.write(0x02); // mensagem tipo 2
          messageOut.write(0x02); // comando 2
          messageOut.write(0x02); // status 2 error
          byte[] mensagemBytes = messageOut.toByteArray();
          return mensagemBytes;
      }
  }
  
}