package helper

import org.mindrot.jbcrypt.BCrypt

object HashingPassword {

  def getHash(str: String) : String = {

    BCrypt.hashpw(str, "$2a$10$BDAqaJVVST4MOAW94tyDCu")
  }
  def checkHash(str: String, strHashed: String): Boolean = {
    BCrypt.checkpw(str,strHashed)
  }

}
