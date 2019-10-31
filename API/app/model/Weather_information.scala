package model

import java.time.LocalDateTime
import java.util.Date

case class Weather_information(beginning: LocalDateTime, ending : LocalDateTime, temperature:Option[Double], humidity:Option[Double], luminosity:Option[Double], sound:Option[Double], id_destination:Int)
{
  /*------METHODS------*/

}
