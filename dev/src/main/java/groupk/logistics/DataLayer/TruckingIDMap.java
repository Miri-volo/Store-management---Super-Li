package groupk.logistics.DataLayer;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TruckingIDMap {

    private Map<Integer, TruckingDTO> truckingsMap;
    private List<TruckingDTO> truckingsList;
    private static TruckingIDMap singletonTruckingIDMapInstance = null;
    private List<Integer> usersHaveUpdatedData;
    private List<String> vehiclesHaveUpdatedData;

    private TruckingIDMap() {
        truckingsMap = new ConcurrentHashMap<Integer, TruckingDTO>();
        truckingsList = new LinkedList<TruckingDTO>();
        usersHaveUpdatedData = new LinkedList<Integer>();
        vehiclesHaveUpdatedData = new LinkedList<String>();
    }

    public static TruckingIDMap getInstance() {
        if (singletonTruckingIDMapInstance == null)
            singletonTruckingIDMapInstance = new TruckingIDMap();
        return singletonTruckingIDMapInstance;
    }

    public void resetData() {
        truckingsMap = new ConcurrentHashMap<Integer, TruckingDTO>();
        truckingsList = new LinkedList<TruckingDTO>();
        usersHaveUpdatedData = new LinkedList<Integer>();
        vehiclesHaveUpdatedData = new LinkedList<String>();
    }

    public void insertTrucking(TruckingDTO trucking) {
        if (truckingsMap.containsKey(trucking.getId())) {
            removeTrucking(trucking.getId());
            addToList(trucking);
            truckingsMap.put(trucking.getId(), trucking);
        }
        else {
            addToList(trucking);
            truckingsMap.put(trucking.getId(), trucking);
        }
    }

    public void removeTrucking(int truckingID) {
        if (truckingsMap.remove(truckingID)!=null)
            removeTruckingFromList(truckingID);
    }

    public void updateUser(int ID) {
        usersHaveUpdatedData.add(ID);
    }

    public boolean isUserHasUpdatedData(int ID) {
        return usersHaveUpdatedData.contains(ID);
    }

    public void updateVehicle(String registrationPlate) {
        vehiclesHaveUpdatedData.add(registrationPlate);
    }

    public boolean isVehicleHasUpdatedData(String registrationPlate) {
        return vehiclesHaveUpdatedData.contains(registrationPlate);
    }

    public boolean contains(int truckingID) {
        return truckingsMap.containsKey(truckingID);
    }

    public TruckingDTO getTruckingById(int truckingID) {
        return truckingsMap.get(truckingID);
    }

    public List<TruckingDTO> getTruckingsOfTruckManager(int truckManagerID) {
        List<TruckingDTO> toReturn = new LinkedList<TruckingDTO>();
        for (TruckingDTO trucking : truckingsList) {
            if (trucking.getTruckManager() == truckManagerID)
                toReturn.add(trucking);
        }
        return toReturn;
    }

    public List<TruckingDTO> getHistoryOfTruckManager(int truckManagerID) {
        List<TruckingDTO> toReturn = new LinkedList<TruckingDTO>();
        for (TruckingDTO trucking : truckingsList) {
            if (trucking.getDate().isAfter(LocalDateTime.now()))
                return toReturn;
            if (trucking.getTruckManager() == truckManagerID)
                toReturn.add(trucking);
        }
        return toReturn;
    }

    public List<TruckingDTO> getFutureTruckingsOfTruckManager(int truckManagerID) {
        List<TruckingDTO> toReturn = new LinkedList<TruckingDTO>();
        boolean found = false;
        for (TruckingDTO trucking : truckingsList) {
            if (found) {
                if (trucking.getTruckManager() == truckManagerID)
                    toReturn.add(trucking);
            }
            else if (trucking.getDate().isAfter(LocalDateTime.now())) {
                found = true;
                toReturn.add(trucking);
            }
        }
        return toReturn;
    }

    public List<TruckingDTO> getTruckingsOfDriver(int driverID) {
        List<TruckingDTO> toReturn = new LinkedList<TruckingDTO>();
        for (TruckingDTO trucking : truckingsList) {
            if(trucking.getDriverUsername() == driverID)
                toReturn.add(trucking);
        }
        return toReturn;
    }

    public List<TruckingDTO> getHistoryOfDriver(int driverID) {
        List<TruckingDTO> toReturn = new LinkedList<TruckingDTO>();
        for (TruckingDTO trucking : truckingsList) {
            if (trucking.getDate().isAfter(LocalDateTime.now()))
                return toReturn;
            if (trucking.getDriverUsername() == driverID)
                toReturn.add(trucking);
        }
        return toReturn;
    }

    public List<TruckingDTO> getFutureTruckingsOfDriver(int driverID) {
        List<TruckingDTO> toReturn = new LinkedList<TruckingDTO>();
        boolean found = false;
        for (TruckingDTO trucking : truckingsList) {
            if (found) {
                if (trucking.getDriverUsername() == driverID)
                    toReturn.add(trucking);
            }
            else if (trucking.getDate().isAfter(LocalDateTime.now())) {
                found = true;
                toReturn.add(trucking);
            }
        }
        return toReturn;
    }

    public List<TruckingDTO> getTruckingsOfVehicle(String registrationPlate) {
        List<TruckingDTO> toReturn = new LinkedList<TruckingDTO>();
        for (TruckingDTO trucking : truckingsList) {
            if(trucking.getVehicleRegistrationPlate().equals(registrationPlate))
                toReturn.add(trucking);
        }
        return toReturn;
    }

    public List<TruckingDTO> getHistoryOfVehicle(String registrationPlate) {
        List<TruckingDTO> toReturn = new LinkedList<TruckingDTO>();
        for (TruckingDTO trucking : truckingsList) {
            if (trucking.getDate().isAfter(LocalDateTime.now()))
                return toReturn;
            if (trucking.getVehicleRegistrationPlate().equals(registrationPlate))
                toReturn.add(trucking);
        }
        return toReturn;
    }

    public List<TruckingDTO> getFutureTruckingsOfVehicle(String registrationPlate) {
        List<TruckingDTO> toReturn = new LinkedList<TruckingDTO>();
        boolean found = false;
        for (TruckingDTO trucking : truckingsList) {
            if (found) {
                if (trucking.getVehicleRegistrationPlate().equals(registrationPlate))
                    toReturn.add(trucking);
            }
            else if (trucking.getDate().isAfter(LocalDateTime.now())) {
                found = true;
                toReturn.add(trucking);
            }
        }
        return toReturn;
    }

    private void addToList(TruckingDTO truckingDTO) {
        if (truckingsList.size() == 0) {
            truckingsList.add(truckingDTO);
            return;
        }
        ListIterator<TruckingDTO> truckingListIterator = truckingsList.listIterator();
        while (truckingListIterator.hasNext()) {
            if (truckingListIterator.next().getDate().isAfter(truckingDTO.getDate())) {
                truckingListIterator.add(truckingDTO);
                return;
            }
        }
        truckingsList.add(truckingDTO);
    }

    private void removeTruckingFromList(int truckingID) {
        ListIterator<TruckingDTO> truckingListIterator = truckingsList.listIterator();
        while (truckingListIterator.hasNext()) {
            if (truckingListIterator.next().getId() == truckingID) {
                truckingListIterator.remove();
                return;
            }
        }
    }

}

