package controllers

import java.sql.{Connection, PreparedStatement, ResultSet, Timestamp}
import java.time.LocalDateTime

import javax.inject._
import model._
import play.api.db._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._

@Singleton
class WeatherController @Inject()(db:Database, cc: ControllerComponents) extends AbstractController(cc) {

  /**GET**/
/**
  implicit val weatherRead: Reads[Weather_information] =
    (JsPath \ "beginning").read[LocalDateTime]
      .and((JsPath \ "ending").read[LocalDateTime])
      .and((JsPath \ "temperature").read[Double])
      .and((JsPath \ "humidity").read[Double])
      .and((JsPath \ "luminosity").read[Double])
      .and((JsPath \ "sound").read[Double])
      .and((JsPath \ "id_destination").read[Int])(Weather_information.apply _)
**/
  implicit val postWeatherRead: Reads[PostWeather] =
    (JsPath \ "temperature").read[Double]
      .and((JsPath \ "humidity").read[Double])
      .and((JsPath \ "luminosity").read[Double])
      .and((JsPath \ "id_destination").read[Int])(PostWeather.apply _)

  implicit val postSoundRead: Reads[PostSound] =
    (JsPath \ "sound").read[Double]
      .and((JsPath \ "id_destination").read[Int])(PostSound.apply _)

  /**POST**/

  implicit val weatherWrite: Writes[Weather_information] =
    (JsPath \ "beginning").write[LocalDateTime]
      .and((JsPath \ "ending").write[LocalDateTime])
      .and((JsPath \ "temperature").write[Option[Double]])
      .and((JsPath \ "humidity").write[Option[Double]])
      .and((JsPath \ "luminosity").write[Option[Double]])
      .and((JsPath \ "sound").write[Option[Double]])
      .and((JsPath \ "id_destination").write[Int])(unlift(Weather_information.unapply))

  implicit val postWeatherWrite: Writes[PostWeather] =
    (JsPath \ "temperature").write[Double]
      .and((JsPath \ "humidity").write[Double])
      .and((JsPath \ "luminosity").write[Double])
      .and((JsPath \ "id_destination").write[Int])(unlift(PostWeather.unapply))

  implicit val postSoundWrite: Writes[PostSound] =
    (JsPath \ "sound").write[Double]
      .and((JsPath \ "id_destination").write[Int])(unlift(PostSound.unapply))

  def getWeathers(id:Int) = Action {
    var weathers = List[Weather_information]()
    val conn      = db.getConnection()

    try {
      val stmt = conn.prepareStatement("SELECT * FROM Weather_information where id_destination = ? order by ending desc")
      stmt.setInt(1, id)
      val rs   = stmt.executeQuery()

      while (rs.next()) {
        weathers = createWeatherInfoFromResultLine(rs)::weathers
      }
    } finally {
      conn.close()
    }
    Ok(Json.toJson(weathers))
  }

  /**GET/id**/

  def getMostRecentWeather(id:Int) = Action {

    val conn = db.getConnection()

    try {
      val insertStatement =  "select top 1 * from weather_information where id_destination = ? order by ending desc".stripMargin
      val preparedStatement:PreparedStatement = conn.prepareStatement(insertStatement)
      preparedStatement.setInt(1, id)
      val rs = preparedStatement.executeQuery()

      if (rs.next()) {
        val wea = createWeatherInfoFromResultLine(rs)
        Ok(Json.toJson(wea))
      }
      else {
        NotFound("NOT_FOUND")
      }
    }
    finally {
      conn.close()
    }
  }

  def createWeatherInfoFromResultLine(rs:ResultSet) : Weather_information = {
    Weather_information(rs.getTimestamp("beginning").toLocalDateTime,
                        rs.getTimestamp("ending").toLocalDateTime,
                        if(rs.getObject("temperature") != null) Some(rs.getDouble("temperature")) else None,
                        if(rs.getObject("humidity") != null) Some(rs.getDouble("humidity")) else None,
                        if(rs.getObject("luminosity") != null) Some(rs.getDouble("luminosity")) else None,
                        if(rs.getObject("sound") != null) Some(rs.getDouble("sound")) else None,
                        rs.getInt("id_destination"))
  }

  //VA DEMANDER DES CHANGEMENTS, JE TE LAISSE FAIRE PIERRE

  private def getCurrentInterval : (LocalDateTime, LocalDateTime) = {
    var intervalBeginning = LocalDateTime.now()
    intervalBeginning = intervalBeginning.minusNanos(intervalBeginning.getNano)
    intervalBeginning = intervalBeginning.minusSeconds(intervalBeginning.getSecond)
    intervalBeginning = intervalBeginning.minusMinutes(intervalBeginning.getMinute % 15)
    (intervalBeginning, intervalBeginning.plusMinutes(15))
  }

  private def checkIfIntervalExists(conn : Connection, intervals:(LocalDateTime, LocalDateTime), idDest:Int):Boolean = {
    val ps = conn.prepareStatement("select * from WEATHER_INFORMATION where id_destination = ? and beginning = ? and ending = ?")
    ps.setInt(1, idDest)
    ps.setTimestamp(2, Timestamp.valueOf(intervals._1))
    ps.setTimestamp(3, Timestamp.valueOf(intervals._2))
    val rs = ps.executeQuery()
    rs.next()
  }

  def saveWeather = Action(parse.json) { request =>
    val WeatherResult = request.body.validate[PostWeather]
    val interval = getCurrentInterval
    WeatherResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      weather_information => {
        val conn = db.getConnection()
        try {
          if (checkIfIntervalExists(conn, interval, weather_information.id_destination)) {
            val updateStatement =
              """
                | update WEATHER_INFORMATION
                | set humidity = ?, temperature = ?, luminosity = ?
                | where id_destination = ? and beginning = ? and ending = ?
                |""".stripMargin
            val ps = conn.prepareStatement(updateStatement)
            ps.setDouble(1, weather_information.humidity)
            ps.setDouble(2, weather_information.temperature)
            ps.setDouble(3, weather_information.luminosity)
            ps.setInt(4, weather_information.id_destination)
            ps.setTimestamp(5, Timestamp.valueOf(interval._1))
            ps.setTimestamp(6, Timestamp.valueOf(interval._2))
            ps.execute()
          }
          else {
            val insertStatement =
              """
                | insert into weather_information (beginning, ending, temperature, humidity, luminosity, id_destination)
                | values (?,?,?,?,?,?)
              """.stripMargin
            val preparedStatement: PreparedStatement = conn.prepareStatement(insertStatement)
            preparedStatement.setTimestamp(1, Timestamp.valueOf(interval._1))
            preparedStatement.setTimestamp(2, Timestamp.valueOf(interval._2))
            preparedStatement.setDouble(3, weather_information.temperature)
            preparedStatement.setDouble(4, weather_information.humidity)
            preparedStatement.setDouble(5, weather_information.luminosity)
            preparedStatement.setInt(6, weather_information.id_destination)
            preparedStatement.execute()
          }
        }
        finally {
          conn.close
        }
        Created(Json.obj("status" -> "OK", "message" -> ("Weather created in interval " + interval._1 + " to " + interval._2)))
      }
    )
  }

  def saveSound = Action(parse.json) { request =>
    val SoundResult = request.body.validate[PostSound]
    val interval = getCurrentInterval
    SoundResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      sound_information => {
        val conn = db.getConnection()
        try {
          if (checkIfIntervalExists(conn, interval, sound_information.id_destination)) {
            val updateStatement =
              """
                | update WEATHER_INFORMATION
                | set sound = ?
                | where id_destination = ? and beginning = ? and ending = ?
                |""".stripMargin
            val ps = conn.prepareStatement(updateStatement)
            ps.setDouble(1, sound_information.sound)
            ps.setInt(2, sound_information.id_destination)
            ps.setTimestamp(3, Timestamp.valueOf(interval._1))
            ps.setTimestamp(4, Timestamp.valueOf(interval._2))
            ps.execute()
          }
          else {
            val insertStatement =
              """
                | insert into weather_information (beginning, ending, sound, id_destination)
                | values (?,?,?,?,?,?)
              """.stripMargin
            val preparedStatement: PreparedStatement = conn.prepareStatement(insertStatement)
            preparedStatement.setTimestamp(1, Timestamp.valueOf(interval._1))
            preparedStatement.setTimestamp(2, Timestamp.valueOf(interval._2))
            preparedStatement.setDouble(3, sound_information.sound)
            preparedStatement.setInt(4, sound_information.id_destination)
            preparedStatement.execute()
          }
        }
        finally {
          conn.close
        }
        Created(Json.obj("status" -> "OK", "message" -> ("Weather created in interval " + interval._1 + " to " + interval._2)))
      }
    )
  }
}
