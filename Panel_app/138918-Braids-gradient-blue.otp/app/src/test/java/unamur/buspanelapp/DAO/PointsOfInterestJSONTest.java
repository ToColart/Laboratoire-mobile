package unamur.buspanelapp.DAO;

import org.junit.Test;

import java.util.ArrayList;

import unamur.buspanelapp.Model.PointOfInterestModel;

import static org.junit.Assert.*;


public class PointsOfInterestJSONTest {
    @Test
    public void getPointsOfInterestAround() throws Exception {
        PointsOfInterestJSON pij = new PointsOfInterestJSON();
        ArrayList<PointOfInterestModel> test = pij.getPointsOfInterestAround("50.46200", "4.862092");
        assertEquals(new ArrayList<PointOfInterestModel>(), test);
    }

}