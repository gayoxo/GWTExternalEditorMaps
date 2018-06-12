/**
 * 
 */
package ucm.fdi.ilsa.client;

import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.InfoWindow;
import com.google.maps.gwt.client.InfoWindowOptions;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.MarkerImage;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.MouseEvent;
import com.google.maps.gwt.client.Marker.ClickHandler;
import com.google.maps.gwt.client.Marker.DblClickHandler;

import fdi.ucm.server.interconect.model.OperationalValueTypeJSON;
import fdi.ucm.server.interconect.model.StructureJSON;

/**
 * @author Joaquin Gayoso-Cabada
 *
 */
public class MarkerGeo {

	private Marker Punto;
	private StructureJSON SS;
	private GoogleMap GMap;
	private Boolean Editor;
	

	public MarkerGeo(MarkerOptions mOpts, StructureJSON sS, GoogleMap gMap, String icono,String title,Boolean editor) {
		Punto =Marker.create(mOpts);
		SS=sS;
		GMap=gMap;
		Punto.setMap(gMap);
		Punto.setIcon(MarkerImage.create(icono));
		Editor=editor;
		
		if (title!=null&&!title.isEmpty())
			Punto.setTitle(title);
		
		if (editor||hasValues(sS))
		{
		
		Punto.addClickListener(new ClickHandler() {
			
			@Override
			public void handle(MouseEvent event) {
				InfoWindowOptions options = InfoWindowOptions.create();
				 //TODO Aqui pondriamos un boton molon que hiciera lo que debe de hacer

				if (Editor)
				    options.setContent("<button onclick=\"setContext('"+SS.getId().get(0)+"',null,false)\">See more info</button>");
				else
					 options.setContent("<button onclick=\"setContextD('"+SS.getId().get(0)+"',null,false)\">See more info</button>");

				    InfoWindow iw = InfoWindow.create(options);
				    iw.open(GMap, Punto);
				
			}
		});	
			
		Punto.addDblClickListener(new DblClickHandler() {
			
			@Override
			public void handle(MouseEvent event) {
				InfoWindowOptions options = InfoWindowOptions.create();
				 //TODO Aqui pondriamos un boton molon que hiciera lo que debe de hacer

				    options.setContent("<button onclick=\"setContext('"+SS.getId()+"',null,false)\">See more info</button>");

				    InfoWindow iw = InfoWindow.create(options);
				    iw.open(GMap, Punto);
				
			}
		});
		
		}
	}

	private boolean hasValues(StructureJSON sS2) {
		if (NotEspe(sS2)&&sS2.getValue()!=null&&!sS2.getValue().isEmpty())
			return true;
		for (StructureJSON sss : sS2.getSons()) {
			
			if (hasValues(sss))
				return true;
			
		}
		return false;
	}

	private boolean NotEspe(StructureJSON sS2) {
		for (OperationalValueTypeJSON ov : sS2.getShows()) {

		if (ov.getView().toLowerCase().equals("clavy"))
			if (ov.getName().toLowerCase().equals("gmaps"))
				{
				
				if (ov.getDefault().toLowerCase().equals("latitude"))
					return false;
				
				if (ov.getDefault().toLowerCase().equals("longitude"))
					return false;
				}
			if (ov.getName().toLowerCase().equals("editor"))
			{
				return false;
			}
		}
		return true;
	}

	public static MarkerGeo create(MarkerOptions mOpts, StructureJSON sS, GoogleMap gMap, String icono, String title,boolean editor) {
		
		return new MarkerGeo(mOpts,sS,gMap,icono,title,editor);
		
	}

	public Marker getPunto() {
		return Punto;
	}

	public void setPunto(Marker punto) {
		Punto = punto;
	}

	public GoogleMap getGMap() {
		return GMap;
	}
	
	public void setGMap(GoogleMap gMap) {
		GMap = gMap;
	}
	
	
	public StructureJSON getSS() {
		return SS;
	}
	
	public void setSS(StructureJSON sS) {
		SS = sS;
	}
}
