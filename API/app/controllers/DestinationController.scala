package controllers

import java.sql.{PreparedStatement, ResultSet, Timestamp}
import java.time.LocalDateTime

import javax.inject._
import model._
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
      .and((JsPath \ "coordY").write[Double])
      .and((JsPath \ "picture").write[String])
      .and((JsPath \ "url").write[String])(unlift(Destination.unapply))

  implicit val postDestinationWrites: Writes[PostDestination] =
      (JsPath \ "name").write[String]
      .and((JsPath \ "description").write[String])
      .and((JsPath \ "audio").write[String])
      .and((JsPath \ "coordX").write[Double])
      .and((JsPath \ "coordY").write[Double])
      .and((JsPath \ "picture").write[String])
      .and((JsPath \ "url").write[String])(unlift(PostDestination.unapply))

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
      .and((JsPath \ "coordY").read[Double])
      .and((JsPath \ "picture").read[String])
      .and((JsPath \ "url").read[String])(Destination.apply _)

  implicit val postDestinationReads: Reads[PostDestination] =
      (JsPath \ "name").read[String]
      .and((JsPath \ "description").read[String])
      .and((JsPath \ "audio").read[String])
      .and((JsPath \ "coordX").read[Double])
      .and((JsPath \ "coordY").read[Double])
      .and((JsPath \ "picture").read[String])
      .and((JsPath \ "url").read[String])(PostDestination.apply _)

  implicit val CoordAroundDataReads: Reads[CoordArroundData] =
      (JsPath \ "coordX").read[Double]
      .and((JsPath \ "coordY").read[Double])(CoordArroundData.apply _)

  /**--- Méthodes ---**/

  def getDestinations = Action {
    var destinations = List[Destination]()
    val conn = db.getConnection()

    try {
      val stmt = conn.createStatement
      val rs   = stmt.executeQuery("SELECT * FROM destination ORDER BY id")

      while (rs.next()) {
        destinations = createDestinationFromResultSet(rs)::destinations
      }
    } finally {
      conn.close()
    }
    Ok(Json.toJson(destinations))
  }

  private def createDestinationFromResultSet(rs: ResultSet):Destination = {
    Destination(
      rs.getInt("id"),
      rs.getString("name"),
      rs.getString("description"),
      rs.getString("audio"),
      rs.getDouble("coordX"),
      rs.getDouble("coordY"),
      rs.getString("picture"),
      rs.getString("url"))
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
        val dest = createDestinationFromResultSet(rs)
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

  def getDestinationAround(coordX:Double, coordY:Double, maxDistanceInKm:Double) = Action {
    var destinations = List[Destination]()
    val conn = db.getConnection()

    try {     //Pour l'exemple --> coordX = 50.46200 et coordY = 4.862092
      val altStatement = "SELECT * FROM (SELECT *, 111*SQRT(POWER(COORDX - ?,2)+POWER(COORDY - ?,2)) as DIST_IN_KM FROM DESTINATION) as X WHERE X.DIST_IN_KM < ?"
      val preparedStatement:PreparedStatement = conn.prepareStatement(/*insertStatement*/altStatement)
      preparedStatement.setDouble(1, coordX)
      preparedStatement.setDouble(2, coordY)
      preparedStatement.setDouble(3, maxDistanceInKm)

      val rs = preparedStatement.executeQuery()

      while (rs.next()) {
        destinations = createDestinationFromResultSet(rs)::destinations
      }
    } finally {
      conn.close()
    }
    Ok(Json.toJson(destinations))
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
              | insert into destination (name, description, audio, coordx, coordy, picture, url)
              | values (?,?,?,?,?,?,?)
              """.stripMargin
          val preparedStatement:PreparedStatement = conn.prepareStatement(insertStatement)
          preparedStatement.setString(1, destination.name)
          preparedStatement.setString(2, destination.description)
          preparedStatement.setString(3, destination.audio)
          preparedStatement.setDouble(4, destination.coordX)
          preparedStatement.setDouble(5, destination.coordY)
          preparedStatement.setString(6, destination.picture)
          preparedStatement.setString(7, destination.url)
          preparedStatement.execute()
        } finally {
          conn.close()
        }
        Created(Json.obj("status" -> "OK", "message" -> ("Destination '" + destination.name + "' saved.")))
      }
    )
  }

  def getDestinationAroundMult(coordX: List[Double], coordY: List[Double], maxDistanceInKm: Double): Action[AnyContent] = Action {
    if(coordX.length != coordY.length) BadRequest("Different number of coordX and coordY")
    else {
      var destinations = List[Destination]()
      val conn = db.getConnection()
      try{
      coordX.zip(coordY).foreach(coord => {
          //Pour l'exemple --> coordX = 50.46200 et coordY = 4.862092
          val altStatement = "SELECT * FROM (SELECT *, 111*SQRT(POWER(COORDX - ?,2)+POWER(COORDY - ?,2)) as DIST_IN_KM FROM DESTINATION) as X WHERE X.DIST_IN_KM < ?"
          val preparedStatement: PreparedStatement = conn.prepareStatement(/*insertStatement*/ altStatement)
          preparedStatement.setDouble(1, coord._1)
          preparedStatement.setDouble(2, coord._2)
          preparedStatement.setDouble(3, maxDistanceInKm)

          val rs = preparedStatement.executeQuery()

          while (rs.next()) {
            val destination = createDestinationFromResultSet(rs)
            if(!destinations.contains(destination)) destinations = destination:: destinations
          }
        }
        )
      } finally {
        conn.close()
      }
      Ok(Json.toJson(destinations))
    }
  }

  def getSelectedDestination(idStop : String) = Action {
    val conn   = db.getConnection()

    try {
      val selectStatement =  "SELECT * FROM bus_stop, destination WHERE id_stop = ? AND bus_stop.id_destination = destination.id".stripMargin
      val preparedStatement:PreparedStatement = conn.prepareStatement(selectStatement)
      preparedStatement.setString(1, idStop)
      val rs = preparedStatement.executeQuery()

      if(rs.next()) {
        if(rs.getTimestamp("timestamp").toLocalDateTime.plusMinutes(30).isAfter(LocalDateTime.now()))
          Ok(Json.toJson(createDestinationFromResultSet(rs)))
        else
          NoContent
      }
      else {
        NotFound("Requested chip nr could not be found")
      }
    }
    finally {
      conn.close()
    }
  }

  def updateSelectedDestination(idStop : String, idDestination : Int) = Action {
    val conn = db.getConnection()

    try {
      val updateStatement =  "UPDATE bus_stop SET id_destination = ?, timestamp = ? WHERE id_stop = ?".stripMargin
      val preparedStatement:PreparedStatement = conn.prepareStatement(updateStatement)
      preparedStatement.setInt(1, idDestination)
      preparedStatement.setString(3, idStop)
      preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()))
      val nbLinesUpdated = preparedStatement.executeUpdate()

      if(nbLinesUpdated > 0) {
        Ok
      }
      else {
        NotFound("Requested bus stop could not be found")
      }
    }
    finally {
      conn.close()
    }
  }
}
