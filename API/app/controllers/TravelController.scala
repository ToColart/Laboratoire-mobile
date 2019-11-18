package controllers

import java.sql.PreparedStatement
import javax.inject._
import model._
import play.api.db._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._

@Singleton
class TravelController @Inject()(db:Database, cc: ControllerComponents) extends AbstractController(cc) {

  /**GET**/

  implicit val travelRead: Reads[Travel] =
    (JsPath \ "id_destination").read[Int]
      .and((JsPath \ "id_user").read[Int])
      .and((JsPath \ "hasVisited").read[Boolean])(Travel.apply _)

  /**POST**/

  implicit val travelWrites: Writes[Travel] =
    (JsPath \ "id_destination").write[Int]
      .and((JsPath \ "id_user").write[Int])
      .and((JsPath \ "hasVisited").write[Boolean])(unlift(Travel.unapply))


  def getTravels(idUser: Int) = Action {

    var listId = List[Int]()
    val conn   = db.getConnection()

    try {
      val insertStatement =  "SELECT id_destination FROM travel WHERE id_user = ?".stripMargin
      val preparedStatement:PreparedStatement = conn.prepareStatement(insertStatement)
      preparedStatement.setInt(1, idUser)
      val rs = preparedStatement.executeQuery()

      while(rs.next()) {
        listId = rs.getInt("id_destination") :: listId
      }
      Ok(Json.toJson(listId))
    }
    finally {
      conn.close()
    }
  }

  def getTravelsListUser(id_destination: Int) = Action {

    var listId = List[Int]()
    val conn   = db.getConnection()

    try {
      val insertStatement =  "SELECT id_user FROM travel WHERE id_destination = ?".stripMargin
      val preparedStatement:PreparedStatement = conn.prepareStatement(insertStatement)
      preparedStatement.setInt(1, id_destination)
      val rs = preparedStatement.executeQuery()

      while(rs.next()) {
        listId = rs.getInt("id_user") :: listId
      }
      Ok(Json.toJson(listId))
    }
    finally {
      conn.close()
    }
  }

  def getTravelsVisitedAround(idUser: Int, coordX:Double, coordY:Double, maxDistanceInKm: Double) = Action {
    var listId = List[Int]()
    val conn   = db.getConnection()

    try {
      val insertStatement =  "SELECT id_destination FROM (SELECT *, 111*SQRT(POWER(COORDX - ?,2)+POWER(COORDY - ?,2)) as DIST_IN_KM FROM DESTINATION) as X, travel as t WHERE X.DIST_IN_KM < ? AND t.id_user = ? AND t.id_destination = x.id ".stripMargin
      val preparedStatement:PreparedStatement = conn.prepareStatement(insertStatement)
      preparedStatement.setDouble(1, coordX)
      preparedStatement.setDouble(2, coordY)
      preparedStatement.setDouble(3, maxDistanceInKm)
      preparedStatement.setInt(4, idUser)
      val rs = preparedStatement.executeQuery()

      while(rs.next()) {
        listId = rs.getInt("id_destination") :: listId
      }
      Ok(Json.toJson(listId))
    }
    finally {
      conn.close()
    }
  }

  def saveTravel = Action(parse.json) { request =>
    val travel = request.body.validate[Travel]
    travel.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      travel => {
        val conn = db.getConnection()

        try {
          val insertStatement =
            """
              | insert into travel (id_destination, id_user, hasVisited)
              | values (?,?,?)
              """.stripMargin
          val preparedStatement:PreparedStatement = conn.prepareStatement(insertStatement)
          preparedStatement.setInt(1, travel.id_destination)
          preparedStatement.setInt(2, travel.id_user)
          preparedStatement.setBoolean(3, travel.hasVisited)
          preparedStatement.execute()
        } finally {
          conn.close()
        }
        Created(Json.obj("status" -> "OK", "message" -> ("Travel saved.")))
      }
    )
  }
}
