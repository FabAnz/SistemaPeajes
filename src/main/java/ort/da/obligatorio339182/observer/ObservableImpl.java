package ort.da.obligatorio339182.observer;

import java.util.ArrayList;

public class ObservableImpl implements Observable {
    private ArrayList<Observador> observadores = new ArrayList<>();

    @Override
    public void agregarObservador(Observador obs) {
        if (!observadores.contains(obs)) {
            observadores.add(obs);
        }
    }

    @Override
    public void quitarObservador(Observador obs) {
        observadores.remove(obs);
    }

    public void avisar(Object evento, Observable origen) {
        ArrayList<Observador> copia = new ArrayList<>(observadores);
        for (Observador obs : copia) {
            obs.actualizar(evento, origen);
        }
    }

}
