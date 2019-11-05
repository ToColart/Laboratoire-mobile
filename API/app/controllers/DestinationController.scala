package controllers

import java.sql.PreparedStatement
import javax.inject._
import model._
import play.api._
import play.api.db._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._

@Singleton
class DestinationController @Inject()(db:Database, cc: ControllerComponents) extends AbstractController(cc) {

  /**--- Variables implicites ---**/

  /**POST**/

  implicit val destinationWrites: Writes[Destination] =
    (JsPath \ "id").write[Int]
      .and((JsPath \ "name").write[String])
      .and((JsPath \ "description").write[String])
      .and((JsPath \ "audio").write[String])
      .and((JsPath \ "coordX").write[Double])
      .and((JsPath \ "coordY").write[Double])(unlift(Destination.unapply))

  implicit val postDestinationWrites: Writes[PostDestination] =
      (JsPath \ "name").write[String]
      .and((JsPath \ "description").write[String])
      .and((JsPath \ "audio").write[String])
      .and((JsPath \ "coordX").write[Double])
      .and((JsPath \ "coordY").write[Double])(unlift(PostDestination.unapply))

  implicit val CoordAroundWrites: Writes[CoordArroundData] =
    (JsPath \ "coordX").write[Double]
      .and((JsPath \ "coordY").write[Double])(unlift(CoordArroundData.unapply))

  /**GET**/

  implicit val destinationReads: Reads[Destination] =
    (JsPath \ "id").read[Int]
      .and((JsPath \ "name").read[String])
      .and((JsPath \ "description").read[String])
      .and((JsPath \ "audio").read[String])
      .and((JsPath \ "coordX").read[Double])
      .and((JsPath \ "coordY").read[Double])(Destination.apply _)

  implicit val postDestinationReads: Reads[PostDestination] =
      (JsPath \ "name").read[String]
      .and((JsPath \ "description").read[String])
      .and((JsPath \ "audio").read[String])
      .and((JsPath \ "coordX").read[Double])
      .and((JsPath \ "coordY").read[Double])(PostDestination.apply _)

  implicit val CoordAroundDataReads: Reads[CoordArroundData] =
      (JsPath \ "coordX").read[Double]
      .and((JsPath \ "coordY").read[Double])(CoordArroundData.apply _)

  /**--- Méthodes ---**/

  def getDestinations = Action {
    var destinations = List[Destination]()
    val conn = db.getConnection()

    try {
      val stmt = conn.createStatement
      val rs   = stmt.executeQuery("SELECT * FROM destination")

      while (rs.next()) {
        destinations = Destination(rs.getInt("id"), rs.getString("name"),rs.getString("description"), rs.getString("audio"), rs.getDouble("coordX"), rs.getDouble("coordY"))::destinations
      }
    } finally {
      conn.close()
    }
    Ok(Json.toJson(destinations))
  }

  /**GET/id**/

  def getDestination(id: Int) = Action {

    val conn = db.getConnection()

    try {
      val insertStatement =  "SELECT * FROM destination WHERE id = ?".stripMargin
      val preparedStatement:PreparedStatement = conn.prepareStatement(insertStatement)
      preparedStatement.setInt(1, id)
      val rs = preparedStatement.executeQuery()

      if (rs.next()) {
        val dest = Destination(rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getString("audio"), rs.getDouble("coordX"), rs.getDouble("coordY"))
        Ok(Json.toJson(dest))
      }
      else {
        NotFound("NOT_FOUND")
      }
    }
    finally {
      conn.close()
    }
  }

  def getDestinationAround = Action(parse.json) { request =>
    val destinationResult = request.body.validate[CoordArroundData]
    destinationResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      destination => {
        var destinations = List[Destination]()
        val conn = db.getConnection()

        try {     //Pour l'exemple --> coordX = 50.46200 et coordY = 4.862092
          val insertStatement =  "SELECT * FROM DESTINATION WHERE COORDX BETWEEN ? - 0.01 AND ? + 0.01 AND COORDY BETWEEN ? - 0.01 AND ? + 0.01".stripMargin
          val preparedStatement:PreparedStatement = conn.prepareStatement(insertStatement)
          preparedStatement.setDouble(1, destination.coordX)
          preparedStatement.setDouble(2, destination.coordX)
          preparedStatement.setDouble(3, destination.coordY)
          preparedStatement.setDouble(4, destination.coordY)
          val rs = preparedStatement.executeQuery()

          while (rs.next()) {
            destinations = Destination(rs.getInt("id"), rs.getString("name"),rs.getString("description"), rs.getString("audio"), rs.getDouble("coordX"), rs.getDouble("coordY"))::destinations
          }
        } finally {
          conn.close()
        }
        Ok(Json.toJson(destinations))
      }
    )
  }

  def saveDestination = Action(parse.json) { request =>
    val destinationResult = request.body.validate[PostDestination]
    destinationResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      destination => {
        val conn = db.getConnection()

        try {
          val insertStatement =
            """
              | insert into destination (name, description, audio, coordx, coordy)
              | values (?,?,?,?,?)
              """.stripMargin
          val preparedStatement:PreparedStatement = conn.prepareStatement(insertStatement)
          preparedStatement.setString(1, destination.name)
          preparedStatement.setString(2, destination.description)
          preparedStatement.setString(3, destination.audio)
          preparedStatement.setDouble(4, destination.coordX)
          preparedStatement.setDouble(5, destination.coordY)
          preparedStatement.execute()
        } finally {
          conn.close()
        }
        Created(Json.obj("status" -> "OK", "message" -> ("Destination '" + destination.name + "' saved.")))
      }
    )
  }
}
