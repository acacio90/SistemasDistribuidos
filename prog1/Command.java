package prog1;

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
import java.util.ArrayList;
import java.util.List;

class Command {

    List<User> arrayList = new ArrayList<User>();
  
    User user = new User("admin", "1234");
  
    User[] Users = { user };
  
    String command;
    File file = new File("./");
    String rootPath = file.getAbsolutePath();
  
    public Command() {
  
    }
  
    public void setCommand(String command) {
      this.command = command.toLowerCase();
    }
  
    public String handleConnect() {
      try {
        String[] command_split = command.split(" ");
        String[] userPassword = command_split[1].split(",");

        for (int i = 0; i < Users.length; i++) {
          System.out.println(Users[i].password);
          if (Users[i].login.equals(userPassword[0]))
            if (Users[i].comparePassword(userPassword[1]))
              return "SUCCESS";
        }
  
        return "ERROR";
      } catch (Exception e) {
        System.err.println("ERROR: " + e);
        return "-1";
      }
    }
  
    public String handlePwd() {
      try {
  
        String t = file.getAbsolutePath();
  
        return t;
      } catch (Exception e) {
        System.err.println("ERRO: " + e);
        return "-1";
      }
    }
  
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
  
    public String handleGetfiles() {
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