// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/Pierre/Documents/Unamur/Master 1/Laboratoire en informatique ambiante et mobile/Laboratoire-mobile/API/conf/routes
// @DATE:Fri Oct 18 10:29:32 CEST 2019

import play.api.mvc.Call


import _root_.controllers.Assets.Asset

// @LINE:7
package controllers {

  // @LINE:7
  class ReverseHomeController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:9
    def tutorial(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "tutorial")
    }
  
    // @LINE:11
    def savePlace(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "places")
    }
  
    // @LINE:12
    def testDb(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "testdb")
    }
  
    // @LINE:10
    def listPlaces(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "places")
    }
  
    // @LINE:8
    def explore(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "explore")
    }
  
    // @LINE:7
    def index(): Call = {
      
      Call("GET", _prefix)
    }
  
  }

  // @LINE:15
  class ReverseAssets(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:15
    def versioned(file:Asset): Call = {
      implicit lazy val _rrc = new play.core.routing.ReverseRouteContext(Map(("path", "/public"))); _rrc
      Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[play.api.mvc.PathBindable[Asset]].unbind("file", file))
    }
  
  }


}
