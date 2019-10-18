package model

import akka.http.scaladsl.model.DateTime

case class Weather_information(timeS: DateTime, temperature:Double, humidity:Double, id_destination:Int)
{
  /*------METHODS------*/

  override def toString = timeS + " -->\n\t " + temperature +" °c\n\t humidité : " +humidity
}
