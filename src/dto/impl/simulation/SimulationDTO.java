package dto.impl.simulation;

import dto.api.AbstractDTO;

import java.util.ArrayList;

public class SimulationDTO extends AbstractDTO<ArrayList<Simulation>> {
    public SimulationDTO(boolean success, ArrayList<Simulation> data) {
        super(success, data);
    }

    public ArrayList<Simulation> getSimulations() {
        return data;
    }


}
