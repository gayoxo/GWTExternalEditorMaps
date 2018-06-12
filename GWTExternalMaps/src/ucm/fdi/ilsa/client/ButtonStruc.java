/**
 * 
 */
package ucm.fdi.ilsa.client;

import com.google.gwt.user.client.ui.Button;
import com.google.maps.gwt.client.LatLng;

import fdi.ucm.server.interconect.model.StructureJSON;

/**
 * @author Joaquin Gayoso Cabada
 *
 */
public class ButtonStruc extends Button{

	private StructureJSON StructElem;
	private LatLng LL;
	private int I;
	private StructureJSON LatS;
	private StructureJSON LongS;
	private String valorT;
	private static final String POINT = "Map Point: ";

	public ButtonStruc(int i, StructureJSON hijos, LatLng lL,Long encontradoLat,Long encontradoLong) {
		super(POINT+i);
		
		I=i;
		
		valorT=POINT+i;
		
		if (lL!=null)
			setText(valorT+" \"in use\"");
		
		StructElem=hijos;
		LL=lL;
		
		for (StructureJSON hijosS : 	StructElem.getSons()) {
			if (hijosS.getClaseOf().equals(encontradoLat))
				LatS=hijosS;
			if (hijosS.getClaseOf().equals(encontradoLong))
				LongS=hijosS;
		}
		
	}
	
	public StructureJSON getStructElem() {
		return StructElem;
	}
	
	public void setStructElem(StructureJSON structElem) {
		StructElem = structElem;
	}

	
	public LatLng getLL() {
		return LL;
	}
	
	public void setLL(LatLng lL) {
		LL = lL;
		if (lL!=null)
			{
			setText(POINT+I+" \"in use\"");
			try {
				LatS.setValue(Double.toString(LL.lat()));
				LongS.setValue(Double.toString(LL.lng()));
			} catch (Exception e) {
				// TODO: handle exception
			}
			}
		else
			setText(POINT+I);
		
		
	}
	
	public String getValorT() {
		return valorT;
	}
	
}
