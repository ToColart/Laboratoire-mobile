package controllers

import java.sql.{PreparedStatement, ResultSetMetaData}
import java.util.Date
import javax.inject._
import model._
import helper._
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

  /**--- Variables implicites ---**/

  /**POST**/

  implicit val userWrites: Writes[User] =
    (JsPath \ "id").write[Int]
      .and((JsPath \ "name").write[String])
      .and((JsPath \ "firstname").write[String])
      .and((JsPath \ "birthdate").write[Date])
      .and((JsPath \ "email").write[String])
      .and((JsPath \ "password").write[String])(unlift(User.unapply))

  implicit val postUserWrites: Writes[PostUser] =
      (JsPath \ "name").write[String]
      .and((JsPath \ "firstname").write[String])
      .and((JsPath \ "birthdate").write[Date])
      .and((JsPath \ "email").write[String])
      .and((JsPath \ "password").write[String])(unlift(PostUser.unapply))

  /**GET**/

  implicit val userReads: Reads[User] =
    (JsPath \ "id").read[Int]
      .and((JsPath \ "name").read[String])
      .and((JsPath \ "firstname").read[String])
      .and((JsPath \ "birthdate").read[Date])
      .and((JsPath \ "email").read[String])
      .and((JsPath \ "password").read[String])(User.apply _)

  implicit val postUserReads: Reads[PostUser] =
      (JsPath \ "name").read[String]
      .and((JsPath \ "firstname").read[String])
      .and((JsPath \ "birthdate").read[Date])
      .and((JsPath \ "email").read[String])
      .and((JsPath \ "password").read(minLength[String](6) andKeep pattern("(?=.*?[0-9])(?=.*?[A-Za-z]).+".r)))(PostUser.apply _)

  implicit val connectingUserReads: Reads[ConnectingUser] =
    (JsPath \ "email").read[String]
      .and((JsPath \ "password").read[String])(ConnectingUser.apply _)

  /**--- Méthodes ---**/

  def getUsers = Action {
    var users = List[User]()
    val conn  = db.getConnection()

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

  def getUser = Action(parse.json) { request =>
    val userResult = request.body.validate[ConnectingUser]
    userResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      user => {
        val conn = db.getConnection()

        try {
          val insertStatement = "SELECT * FROM user WHERE email = ? AND password = ?".stripMargin
          val preparedStatement: PreparedStatement = conn.prepareStatement(insertStatement)
          preparedStatement.setString(1, user.email)

          val password = HashingPassword.getHash(user.password)
          preparedStatement.setString(2, password)

          val rs = preparedStatement.executeQuery()

          if (rs.next()) {
            val user = User(rs.getInt("id"), rs.getString("name"), rs.getString("firstname"), rs.getDate("birthdate"), rs.getString("email"), rs.getString("password"))
            Ok(Json.toJson(user))
          }
          else {
            NotFound("NOT_FOUND")
          }
        }
        finally {
          conn.close()
        }
      }
    )
  }

  /**DELETE/ID**/

  def deleteUser(id: Int) = Action {

    val conn = db.getConnection()

    try {
      val select = "SELECT count(*) FROM User WHERE id = ?".stripMargin
      val prepSelect: PreparedStatement = conn.prepareStatement(select)
      prepSelect.setInt(1, id)
      val rs = prepSelect.executeQuery()

      var count = -1
      if (rs.next) count = rs.getInt(1)

      if(count>0){
        val insertStatement =  "DELETE FROM user WHERE id = ?".stripMargin
        val preparedStatement = conn.prepareStatement(insertStatement)
        preparedStatement.setInt(1, id)
        preparedStatement.execute()
        Ok(Json.obj("status" -> "OK", "message" -> ("User '" + id + "' deleted.")))
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

  def saveUser = Action(parse.json) { request =>
    val userResult = request.body.validate[PostUser]
    userResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      user => {
        val conn = db.getConnection()

        try {
          val select = "SELECT count(*) FROM User WHERE email = ?".stripMargin
          val prepSelect: PreparedStatement = conn.prepareStatement(select)
          prepSelect.setString(1, user.email)
          val rs = prepSelect.executeQuery()

          var count = -1
          if (rs.next) count = rs.getInt(1)

          if (count == 0) {
            val insertStatement =
              """
                | insert into user (name, firstname, birthdate, email, password)
                | values (?,?,?,?,?)
              """.stripMargin

            val sqlDate = new Date(user.birthdate.getTime)
            val preparedStatement: PreparedStatement = conn.prepareStatement(insertStatement)
            preparedStatement.setString(1, user.name)
            preparedStatement.setString(2, user.firstname)
            preparedStatement.setObject(3, sqlDate)
            preparedStatement.setString(4, user.email)

            val password = HashingPassword.getHash(user.password)

            preparedStatement.setString(5, password)
            preparedStatement.execute()

            Created(Json.obj("status" -> "OK", "message" -> (user.toString)))
          }
          else {
            BadRequest(Json.obj("status" -> "KO", "error" -> "Email pas unique"))
          }
        }
        finally {
            conn.close()
          }
      }
    )
  }
}
