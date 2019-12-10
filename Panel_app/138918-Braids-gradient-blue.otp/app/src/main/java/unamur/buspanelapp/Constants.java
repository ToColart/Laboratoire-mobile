package unamur.buspanelapp;

import unamur.buspanelapp.Model.StopModel;

public class Constants {
    //URL de base pour db azur :
    public static String URL_API_PointsOfInterest_BASE = "http://192.168.0.26:9000/";//Localhost: 10.0.2.2 | Pierre: 192.168.137.1 //Maison: 192.168.0.26
    public static final String URL_API_Stops_BASE = "https://www.odwb.be/api/records/1.0/search/?dataset=arrets-tec&facet=commune";
    public static final String URL_API_Buses_BASE = "";

    public static final Integer NUMBER_PICTURES_DISPLAYED = 3;
    public static final Integer NUMBER_LINES_STOPS_DISPLAYED = 2;
    public static final Integer NUMBER_BUSES_DISPLAYED = 6;

    public static StopModel CURRENT_STOP = new StopModel("7eb8580ad6b0f3b2b1158af3f9000053f2c81c7b", "LA PLANTE Marronnier", "50.449948", "4.856821");
}
