package controllers

import javax.inject._
import play.api.db._
import play.api.mvc._

@Singleton
class HomeController @Inject()(db:Database, cc: ControllerComponents) extends AbstractController(cc) {
  
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }
  
  def explore() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.explore())
  }
  
  def tutorial() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.tutorial())
  }
}
