package ort.da.obligatorio339182.observer;

import java.util.ArrayList;

public class ObservableConcreto extends ObservableAbstracto {
    public void avisar(Object evento, Observable origen) {
        ArrayList<Observador> copia = new ArrayList<>(super.observadores);
        for (Observador obs : copia) {
            obs.actualizar(evento, origen);
        }
    }
}
