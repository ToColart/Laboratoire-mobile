package controllers

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

  /**new stuff**/
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

  /**POST**/
  implicit val locationReads: Reads[Location] =
    (JsPath \ "lat").read[Double].and((JsPath \ "long").read[Double])(Location.apply _)

  implicit val placeReads: Reads[Place] =
    (JsPath \ "name").read[String].and((JsPath \ "location").read[Location])(Place.apply _)

  def savePlace = Action(parse.json) { request =>
    val placeResult = request.body.validate[Place]
    placeResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      place => {
        Place.save(place)
        Ok(Json.obj("status" -> "OK", "message" -> ("Place '" + place.name + "' saved.")))
      }
    )
  }


}
