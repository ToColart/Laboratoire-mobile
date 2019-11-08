package model

case class Destination(id:Int, name:String, description:String, audio:String, coordX:Double, coordY:Double, picture:String)
{
  /*------REQUIRE------*/

  require(name != null)
  require(description != null)
  require(audio != null)

  /*------METHODS------*/

  override def toString = name + ",\n\n" + description
}
