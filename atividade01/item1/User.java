package atividade01.item1;

/*
 * User: Classe para lidar com as contas de usuário utilizada no comando CONNECT
 * Funções de autenticaca e criptografia com SHA-512
 * 
 * Autores:
 *       Iago Sasaki
 *       Pedro Acácio
 * 
 * Data de Criacao: 06 de abril de 2023
 * Ultima atualizacao: 11 de abril de 2023
 */

import java.math.BigInteger;
import java.security.MessageDigest;

class User {
    String login;
    String password;
  
    public User(String user, String password) {
      this.login = user;
      this.password = password;
    }
  
    
    /** 
     * Devolve a senha criptografada caso seja solicitada
     * @return String
     */
    public String getSHA512() {
      String toReturn = null;
      try {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        digest.reset();
        digest.update(this.password.getBytes("utf8"));
        toReturn = String.format("%0128x", new BigInteger(1, digest.digest()));
      } catch (Exception e) {
        e.printStackTrace();
      }
  
      return toReturn;
    }
  
    public Boolean comparePassword(String password) {
      String passwordHash = getSHA512();
      return passwordHash.equals(password);
    }
  }