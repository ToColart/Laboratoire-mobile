// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/Pierre/Downloads/play-samples-2.7.x/play-samples-2.7.x/play-scala-hello-world-tutorial/conf/routes
// @DATE:Thu Oct 17 18:50:45 CEST 2019


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
