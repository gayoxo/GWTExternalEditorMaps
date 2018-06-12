package ucm.fdi.ilsa.client;

import com.google.gwt.user.client.ui.Image;

public class ImageButtonStruc extends Image {

	
	private ButtonStruc Boton;
	
	public ImageButtonStruc(String texto, ButtonStruc boton) {
		super(texto);
		Boton=boton;
	}

	
	public ButtonStruc getBoton() {
		return Boton;
	}
	
}
