package se.lu.bos.ext;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 */
@RestController
@RequestMapping("/rest/missionbuilder")
public class MissionDataServiceBean {

    @RequestMapping(method = RequestMethod.GET, value = "/actionTypes", produces = "application/json")
    public ResponseEntity<List<ActionType>> getActionTypes() {
        return new ResponseEntity(ActionType.values(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/mission/{id}", produces = "application/json")
    public ResponseEntity<ClientMission> getClientMission(Long id) {
        return null;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/planeTypes", produces = "application/json")
    public ResponseEntity<List<PlaneType>> getPlaneTypes() {
        return new ResponseEntity(PlaneType.values(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/vehicleTypes", produces = "application/json")
    public ResponseEntity<List<VehicleType>> getVehicleTypes() {
        return new ResponseEntity(VehicleType.values(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/staticObjectTypes", produces = "application/json")
    public ResponseEntity<List<StaticObjectType>> getStaticObjectTypes() {
        return new ResponseEntity(StaticObjectType.values(), HttpStatus.OK);
    }

}
