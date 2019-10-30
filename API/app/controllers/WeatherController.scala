package controllers

import java.sql.{PreparedStatement, ResultSet}
import java.time.LocalDateTime
import java.util.Date

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

  implicit val weatherRead: Reads[Weather_information] =
    (JsPath \ "timeW").read[LocalDateTime]
      .and((JsPath \ "temperature").read[Double])
      .and((JsPath \ "humidity").read[Double])
      .and((JsPath \ "id_destination").read[Int])(Weather_information.apply _)

  implicit val postWeatherRead: Reads[PostWeather] =
    (JsPath \ "temperature").read[Double]
      .and((JsPath \ "humidity").read[Double])
      .and((JsPath \ "id_destination").read[Int])(PostWeather.apply _)

  /**POST**/

  implicit val weatherWrite: Writes[Weather_information] =
    (JsPath \ "timeW").write[LocalDateTime]
      .and((JsPath \ "temperature").write[Double])
      .and((JsPath \ "humidity").write[Double])
      .and((JsPath \ "id_destination").write[Int])(unlift(Weather_information.unapply))

  implicit val postWeatherWrite: Writes[PostWeather] =
    (JsPath \ "temperature").write[Double]
      .and((JsPath \ "humidity").write[Double])
      .and((JsPath \ "id_destination").write[Int])(unlift(PostWeather.unapply))


  def getWeathers(id:Int) = Action {
    var weathers = List[Weather_information]()
    val conn      = db.getConnection()

    try {
      val stmt = conn.prepareStatement("SELECT * FROM Weather_information where id_destination = ?")
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
      val insertStatement =  "select top 1 * from weather_information where id_destination = ? order by timew desc".stripMargin
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
    Weather_information(rs.getTimestamp("timeW").toLocalDateTime,
                        rs.getDouble("temperature"),
                        rs.getDouble("humidity"),
                        rs.getInt("id_destination"))
  }

  def saveWeather = Action(parse.json) { request =>
    val WeatherResult = request.body.validate[PostWeather]
    WeatherResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      weather_information => {
        val conn = db.getConnection()

        try {
          val insertStatement =
            """
              | insert into weather_information (timeW, temperature, humidity, id_destination)
              | values (CURRENT_TIMESTAMP(),?,?,?)
              """.stripMargin
          val preparedStatement:PreparedStatement = conn.prepareStatement(insertStatement)
          preparedStatement.setDouble(1, weather_information.temperature)
          preparedStatement.setDouble(2, weather_information.humidity)
          preparedStatement.setInt(3, weather_information.id_destination)
          preparedStatement.execute()
        } finally {
          conn.close()
        }
        Created(Json.obj("status" -> "OK", "message" -> ("Weather saved.")))
      }
    )
  }
}
