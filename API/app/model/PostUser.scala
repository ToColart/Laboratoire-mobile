package model

import java.util.Date

case class PostUser(name:String, firstname:String, birthdate:Date, email:String, password:String)
{
  /*------REQUIRE------*/

  require(name != null)
  require(firstname != null)
  require(birthdate != null)
  require(email != null)
  require(password != null)

  /*------METHODS------*/

  override def toString = name + " " + firstname + " - " + email
}
