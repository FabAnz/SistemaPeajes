package ort.da.obligatorio339182.model.domain.estados;

public interface Estado {

	public boolean puedeIngresarAlSistema();

	public boolean puedeRealizarTransitos();

	public boolean puedeAsignarBonificaciones();

	public boolean aplicanBonificaciones();

	public boolean recibeNotificaciones();

	public String getNombre();

}
