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

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class DestinationController @Inject()(db:Database, cc: ControllerComponents) extends AbstractController(cc) {

  /**GET**/

  implicit val destinationWrites: Writes[Destination] =
    (JsPath \ "id").write[Int]
      .and((JsPath \ "name").write[String])
      .and((JsPath \ "description").write[String])
      .and((JsPath \ "audio").write[String])
      .and((JsPath \ "coordX").write[Double])
      .and((JsPath \ "coordY").write[Double])(unlift(Destination.unapply))


  def getDestinations = Action {
    var destinations = List[Destination]()
    val conn      = db.getConnection()

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

  /**POST**/
  implicit val locationReads: Reads[Destination] =
    (JsPath \ "id").read[Int]
      .and((JsPath \ "name").read[String])
      .and((JsPath \ "description").read[String])
      .and((JsPath \ "audio").read[String])
      .and((JsPath \ "coordX").read[Double])
      .and((JsPath \ "coordY").read[Double])(Destination.apply _)

  def saveDestination = Action(parse.json) { request =>
    val destinationResult = request.body.validate[Destination]
    destinationResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      destination => {
        val conn      = db.getConnection()

        try {
          val insertStatement =
            """
              | insert into destination (id, name, description, audio, coordx, coordy)
              | values (?,?,?,?,?,?)
              """.stripMargin
          val preparedStatement:PreparedStatement = conn.prepareStatement(insertStatement)
          preparedStatement.setInt(1, destination.id)
          preparedStatement.setString(2, destination.name)
          preparedStatement.setString(3, destination.description)
          preparedStatement.setString(4, destination.audio)
          preparedStatement.setDouble(5, destination.coordX)
          preparedStatement.setDouble(6, destination.coordY)
          preparedStatement.execute()
        } finally {
          conn.close()
        }
        Created(Json.obj("status" -> "OK", "message" -> ("Place '" + destination.name + "' saved.")))
      }
    )
  }


}
