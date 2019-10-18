// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/Thomas/Desktop/UNamur/Master/Laboratoire/Laboratoire-mobile/API/conf/routes
// @DATE:Fri Oct 18 12:02:34 CEST 2019


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
