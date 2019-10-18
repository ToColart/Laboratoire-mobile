// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/Pierre/Documents/Unamur/Master 1/Laboratoire en informatique ambiante et mobile/Laboratoire-mobile/API/conf/routes
// @DATE:Fri Oct 18 12:04:21 CEST 2019

package controllers;

import router.RoutesPrefix;

public class routes {
  
  public static final controllers.ReverseDestinationController DestinationController = new controllers.ReverseDestinationController(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseHomeController HomeController = new controllers.ReverseHomeController(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseAssets Assets = new controllers.ReverseAssets(RoutesPrefix.byNamePrefix());

  public static class javascript {
    
    public static final controllers.javascript.ReverseDestinationController DestinationController = new controllers.javascript.ReverseDestinationController(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseHomeController HomeController = new controllers.javascript.ReverseHomeController(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseAssets Assets = new controllers.javascript.ReverseAssets(RoutesPrefix.byNamePrefix());
  }

}
