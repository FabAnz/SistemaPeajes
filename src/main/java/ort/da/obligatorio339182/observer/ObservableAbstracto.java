package ort.da.obligatorio339182.observer;

import java.util.ArrayList;

public abstract class ObservableAbstracto implements Observable {
    protected ArrayList<Observador> observadores = new ArrayList<>();

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

    public void avisar(Object evento) {
        ArrayList<Observador> copia = new ArrayList<>(observadores);
        for (Observador obs : copia) {
            obs.actualizar(evento, this);
        }
    }

}
