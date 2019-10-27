package model

case class ConnectingUser(email:String, password:String)
{
  require(email != null)
  require(password != null)

  /*------METHODS------*/

  override def toString = email
}
