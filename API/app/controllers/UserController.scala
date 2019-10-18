package controllers

import java.sql.PreparedStatement
import java.util.Date

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
class UserController @Inject()(db:Database, cc: ControllerComponents) extends AbstractController(cc) {

  /**GET**/

  implicit val userWrites: Writes[User] =
    (JsPath \ "id").write[Int]
      .and((JsPath \ "name").write[String])
      .and((JsPath \ "firstname").write[String])
      .and((JsPath \ "birthdate").write[Date])
      .and((JsPath \ "email").write[String])
      .and((JsPath \ "password").write[String])(unlift(User.unapply))

  def getUsers = Action {
    var users = List[User]()
    val conn      = db.getConnection()

    try {
      val stmt = conn.createStatement
      val rs   = stmt.executeQuery("SELECT * FROM user")

      while (rs.next()) {
        users = User(rs.getInt("id"), rs.getString("name"), rs.getString("firstname"), rs.getDate("birthdate"), rs.getString("email"), rs.getString("password"))::users
      }
    } finally {
      conn.close()
    }
    Ok(Json.toJson(users))
  }

  /**GET/id**/

  def getUser(id: Int) = Action {

    val conn = db.getConnection()

    val stmt = conn.createStatement
    val rs = stmt.executeQuery("SELECT * FROM user WHERE id = " + id)

    if (rs.next()) {
      val user = User(rs.getInt("id"), rs.getString("name"), rs.getString("firstname"), rs.getDate("birthdate"), rs.getString("email"), rs.getString("password"))
      conn.close()
      Ok(Json.toJson(user))
    }
    else {
      val list = List[User]()
      conn.close()
      Ok(Json.toJson(list))
    }
  }

  /**POST**/
  implicit val userReads: Reads[User] =
    (JsPath \ "id").read[Int]
      .and((JsPath \ "name").read[String])
      .and((JsPath \ "firstname").read[String])
      .and((JsPath \ "birthdate").read[Date])
      .and((JsPath \ "email").read[String])
      .and((JsPath \ "password").read[String])(User.apply _)

  def saveUser = Action(parse.json) { request =>
    val userResult = request.body.validate[User]
    userResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      user => {
        val conn      = db.getConnection()

        try {
          val insertStatement =
            """
              | insert into user (id, name, firstname, birthdate, email, password)
              | values (?,?,?,'2019-10-01',?,?)
              """.stripMargin
          val preparedStatement:PreparedStatement = conn.prepareStatement(insertStatement)
          preparedStatement.setInt(1, user.id)
          preparedStatement.setString(2, user.name)
          preparedStatement.setString(3, user.firstname)
          //preparedStatement.setDate(4, user.birthdate)
          preparedStatement.setString(4, user.email)
          preparedStatement.setString(5, user.password)
          preparedStatement.execute()
        } finally {
          conn.close()
        }
        Created(Json.obj("status" -> "OK", "message" -> ("Place '" + user.name + "' saved.")))
      }
    )
  }


}
