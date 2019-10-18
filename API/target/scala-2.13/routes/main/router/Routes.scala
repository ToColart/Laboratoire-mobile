// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/Pierre/Documents/Unamur/Master 1/Laboratoire en informatique ambiante et mobile/Laboratoire-mobile/API/conf/routes
// @DATE:Fri Oct 18 12:21:05 CEST 2019

package router

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._

import play.api.mvc._

import _root_.controllers.Assets.Asset

class Routes(
  override val errorHandler: play.api.http.HttpErrorHandler, 
  // @LINE:7
  HomeController_2: controllers.HomeController,
  // @LINE:13
  DestinationController_1: controllers.DestinationController,
  // @LINE:17
  Assets_0: controllers.Assets,
  val prefix: String
) extends GeneratedRouter {

   @javax.inject.Inject()
   def this(errorHandler: play.api.http.HttpErrorHandler,
    // @LINE:7
    HomeController_2: controllers.HomeController,
    // @LINE:13
    DestinationController_1: controllers.DestinationController,
    // @LINE:17
    Assets_0: controllers.Assets
  ) = this(errorHandler, HomeController_2, DestinationController_1, Assets_0, "/")

  def withPrefix(addPrefix: String): Routes = {
    val prefix = play.api.routing.Router.concatPrefix(addPrefix, this.prefix)
    router.RoutesPrefix.setPrefix(prefix)
    new Routes(errorHandler, HomeController_2, DestinationController_1, Assets_0, prefix)
  }

  private[this] val defaultPrefix: String = {
    if (this.prefix.endsWith("/")) "" else "/"
  }

  def documentation = List(
    ("""GET""", this.prefix, """controllers.HomeController.index"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """explore""", """controllers.HomeController.explore"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """tutorial""", """controllers.HomeController.tutorial"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """places""", """controllers.HomeController.listPlaces"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """places""", """controllers.HomeController.savePlace"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """testdb""", """controllers.HomeController.testDb"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """destination/getDestinations""", """controllers.DestinationController.getDestinations"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """destination/save""", """controllers.DestinationController.saveDestination"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """assets/""" + "$" + """file<.+>""", """controllers.Assets.versioned(path:String = "/public", file:Asset)"""),
    Nil
  ).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
    case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
    case l => s ++ l.asInstanceOf[List[(String,String,String)]]
  }}


  // @LINE:7
  private[this] lazy val controllers_HomeController_index0_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix)))
  )
  private[this] lazy val controllers_HomeController_index0_invoker = createInvoker(
    HomeController_2.index,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "index",
      Nil,
      "GET",
      this.prefix + """""",
      """ An example controller showing a sample home page""",
      Seq()
    )
  )

  // @LINE:8
  private[this] lazy val controllers_HomeController_explore1_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("explore")))
  )
  private[this] lazy val controllers_HomeController_explore1_invoker = createInvoker(
    HomeController_2.explore,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "explore",
      Nil,
      "GET",
      this.prefix + """explore""",
      """""",
      Seq()
    )
  )

  // @LINE:9
  private[this] lazy val controllers_HomeController_tutorial2_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("tutorial")))
  )
  private[this] lazy val controllers_HomeController_tutorial2_invoker = createInvoker(
    HomeController_2.tutorial,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "tutorial",
      Nil,
      "GET",
      this.prefix + """tutorial""",
      """""",
      Seq()
    )
  )

  // @LINE:10
  private[this] lazy val controllers_HomeController_listPlaces3_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("places")))
  )
  private[this] lazy val controllers_HomeController_listPlaces3_invoker = createInvoker(
    HomeController_2.listPlaces,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "listPlaces",
      Nil,
      "GET",
      this.prefix + """places""",
      """""",
      Seq()
    )
  )

  // @LINE:11
  private[this] lazy val controllers_HomeController_savePlace4_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("places")))
  )
  private[this] lazy val controllers_HomeController_savePlace4_invoker = createInvoker(
    HomeController_2.savePlace,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "savePlace",
      Nil,
      "POST",
      this.prefix + """places""",
      """""",
      Seq()
    )
  )

  // @LINE:12
  private[this] lazy val controllers_HomeController_testDb5_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("testdb")))
  )
  private[this] lazy val controllers_HomeController_testDb5_invoker = createInvoker(
    HomeController_2.testDb,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "testDb",
      Nil,
      "GET",
      this.prefix + """testdb""",
      """""",
      Seq()
    )
  )

  // @LINE:13
  private[this] lazy val controllers_DestinationController_getDestinations6_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("destination/getDestinations")))
  )
  private[this] lazy val controllers_DestinationController_getDestinations6_invoker = createInvoker(
    DestinationController_1.getDestinations,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.DestinationController",
      "getDestinations",
      Nil,
      "GET",
      this.prefix + """destination/getDestinations""",
      """""",
      Seq()
    )
  )

  // @LINE:14
  private[this] lazy val controllers_DestinationController_saveDestination7_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("destination/save")))
  )
  private[this] lazy val controllers_DestinationController_saveDestination7_invoker = createInvoker(
    DestinationController_1.saveDestination,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.DestinationController",
      "saveDestination",
      Nil,
      "POST",
      this.prefix + """destination/save""",
      """""",
      Seq()
    )
  )

  // @LINE:17
  private[this] lazy val controllers_Assets_versioned8_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("assets/"), DynamicPart("file", """.+""",false)))
  )
  private[this] lazy val controllers_Assets_versioned8_invoker = createInvoker(
    Assets_0.versioned(fakeValue[String], fakeValue[Asset]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Assets",
      "versioned",
      Seq(classOf[String], classOf[Asset]),
      "GET",
      this.prefix + """assets/""" + "$" + """file<.+>""",
      """ Map static resources from the /public folder to the /assets URL path""",
      Seq()
    )
  )


  def routes: PartialFunction[RequestHeader, Handler] = {
  
    // @LINE:7
    case controllers_HomeController_index0_route(params@_) =>
      call { 
        controllers_HomeController_index0_invoker.call(HomeController_2.index)
      }
  
    // @LINE:8
    case controllers_HomeController_explore1_route(params@_) =>
      call { 
        controllers_HomeController_explore1_invoker.call(HomeController_2.explore)
      }
  
    // @LINE:9
    case controllers_HomeController_tutorial2_route(params@_) =>
      call { 
        controllers_HomeController_tutorial2_invoker.call(HomeController_2.tutorial)
      }
  
    // @LINE:10
    case controllers_HomeController_listPlaces3_route(params@_) =>
      call { 
        controllers_HomeController_listPlaces3_invoker.call(HomeController_2.listPlaces)
      }
  
    // @LINE:11
    case controllers_HomeController_savePlace4_route(params@_) =>
      call { 
        controllers_HomeController_savePlace4_invoker.call(HomeController_2.savePlace)
      }
  
    // @LINE:12
    case controllers_HomeController_testDb5_route(params@_) =>
      call { 
        controllers_HomeController_testDb5_invoker.call(HomeController_2.testDb)
      }
  
    // @LINE:13
    case controllers_DestinationController_getDestinations6_route(params@_) =>
      call { 
        controllers_DestinationController_getDestinations6_invoker.call(DestinationController_1.getDestinations)
      }
  
    // @LINE:14
    case controllers_DestinationController_saveDestination7_route(params@_) =>
      call { 
        controllers_DestinationController_saveDestination7_invoker.call(DestinationController_1.saveDestination)
      }
  
    // @LINE:17
    case controllers_Assets_versioned8_route(params@_) =>
      call(Param[String]("path", Right("/public")), params.fromPath[Asset]("file", None)) { (path, file) =>
        controllers_Assets_versioned8_invoker.call(Assets_0.versioned(path, file))
      }
  }
}
