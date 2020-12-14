package com.example.cddlemptyproject.logic.models;

import br.ufma.lsdi.cddl.message.SensorDataMessage;

public class BikeMessage extends SensorDataMessage {

    private Double velocidadeInstantanea;

    public Double getVelocidadeInstantanea() {
        return velocidadeInstantanea;
    }

    public void setVelocidadeInstantanea(Double velocidadeInstantanea) {
        this.velocidadeInstantanea = velocidadeInstantanea;
    }
}
