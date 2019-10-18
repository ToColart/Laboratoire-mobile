package model

import java.util.Date

case class User(id:Int, name:String, firstname:String, birthdate:Date, email:String, password:String)
{
  /*------REQUIRE------*/

  require(name != null)
  require(firstname != null)
  require(birthdate != null)
  require(email != null)
  require(password != null)
  require(password.length > 6)

  /*------METHODS------*/

  override def toString = name + " " + firstname + " - " + birthdate
}
