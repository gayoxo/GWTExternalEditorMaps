package ucm.fdi.ilsa.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MVCArray;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.MouseEvent;
import com.google.maps.gwt.client.Polyline;
import com.google.maps.gwt.client.PolylineOptions;
import com.google.maps.gwt.client.places.Autocomplete;
import com.google.maps.gwt.client.places.AutocompleteOptions;
import com.google.maps.gwt.client.places.PlaceGeometry;
import com.google.maps.gwt.client.places.PlaceResult;
import com.google.maps.gwt.client.places.Autocomplete.PlaceChangedHandler;

import fdi.ucm.server.interconect.model.StructureJSON;

public class MapInternalMultiple{

	
	
	private CompositeDocumentEditionMaps padre;
	private Autocomplete autoComplete;

	public MapInternalMultiple(CompositeDocumentEditionMaps compositeDocumentEditionMaps, SimplePanelMap panelMapa2, SimplePanel panelTA) {
		
		padre=compositeDocumentEditionMaps;
		
		TextBox textea = new TextBox();
		textea.setWidth("90%");
		
		panelTA.add(textea);
		
		List<ButtonStruc> lista=new ArrayList<ButtonStruc>();
		HashMap<ButtonStruc,StructureJSON> TS=new HashMap<ButtonStruc,StructureJSON>();
		
		for (ButtonStruc latLng : compositeDocumentEditionMaps.getDisponibles()) {
			if (latLng.getLL()!=null)
				{
				TS.put(latLng, latLng.getStructElem());
				lista.add(latLng);
				}
			
		}
		
		
		
		if (lista.size()>0)
		{
			LatLng Center0=lista.get(0).getLL();
			
			
			
			
			panelMapa2.setWidth("100%");
			panelMapa2.setHeight((compositeDocumentEditionMaps.getHeigh()-30)+"px");
	        MapOptions options = MapOptions.create();
	        
	        if (Center0!=null)
	        	options.setCenter(Center0);
	        else
	        {
	        	Double Lat=CompositeDocumentEditionMaps.getLatitude();
	        	Double Lon=CompositeDocumentEditionMaps.getLongitude();
	        	LatLng LL=LatLng.create(Lat, Lon);
	        	options.setCenter(LL);
	        	}

	        options.setZoom(13);
	        options.setMapTypeId(MapTypeId.ROADMAP);
	        options.setDraggable(true);
	        options.setMapTypeControl(true);
	        options.setScaleControl(true);
	        options.setScrollwheel(true);
	        options.setMapMaker(true);
//	        Button btn = new Button();
	        GoogleMap gMap = GoogleMap.create(panelMapa2.getElement(), options);
	       
	        panelMapa2.setVar(Center0,gMap);
	       
	        
	        MVCArray<LatLng> latLngArray = MVCArray.create();  
	        for (ButtonStruc lng : lista) {  
	            latLngArray.push(lng.getLL());  
	        }  
	        PolylineOptions polyOpts = PolylineOptions.create();  
	        polyOpts.setPath(latLngArray);  
	        polyOpts.setStrokeColor("red");  
	        polyOpts.setStrokeOpacity(0.5);  
	        polyOpts.setStrokeWeight(5);  
	        Polyline path = Polyline.create(polyOpts);  
	        path.setMap(gMap);
		
		for (int i = 0; i < lista.size(); i++) {
			
			LatLng late =lista.get(i).getLL();
			StructureJSON SS=TS.get(lista.get(i));
			
	        MarkerOptions mOpts = MarkerOptions.create();
	        mOpts.setPosition(late);        
	        
	        String Icon=CompositeDocumentEditionMaps.GEOICONYEL;
	        if (i==0)
	        	Icon=CompositeDocumentEditionMaps.GEOICONRED;
	        else
	        	if (i==lista.size()-1)
	        		Icon=CompositeDocumentEditionMaps.GEOICOBLUE;
	        
	        String Title = lista.get(i).getValorT();
	        
	        MarkerGeo.create(mOpts,SS,gMap,Icon,Title,true);
		
		}
		
		
		gMap.addClickListener(new GoogleMap.ClickHandler() {
			
			@Override
			public void handle(MouseEvent event) {
						
				LatLng act = event.getLatLng();
				
				
				
				if (padre.getActual()!=null&&padre.getActual().getLL()!=null)
				{
					if (Window.confirm(CompositeDocumentEditionMaps.CONFIRMACION_EDIT))
						{
						padre.getActual().setLL(act);
						padre.RefreshMap();
						}
					else{
						
					}
				}
				else
					{
					GWT.log("nuevo Punto");
					padre.getActual().setLL(act);
					padre.RefreshMap();
				        
					}
						
				
			}
		});
		
		
		InputElement element = InputElement.as(textea.getElement());
        
        AutocompleteOptions options5 = AutocompleteOptions.create();
        
//		options5.setTypes(types);
        options5.setBounds(gMap.getBounds());
        
        gMap.triggerResize();
        
        
        autoComplete = Autocomplete.create(element, options5);

        
        autoComplete.addPlaceChangedListener(new PlaceChangedHandler() {


		@Override
		public void handle() {
			  PlaceResult result = autoComplete.getPlace();

	            PlaceGeometry geomtry = result.getGeometry();
	            LatLng center = geomtry.getLocation();

	            if (padre.getActual()!=null&&padre.getActual().getLL()!=null)
				{
					if (Window.confirm(CompositeDocumentEditionMaps.CONFIRMACION_EDIT))
						{
						padre.getActual().setLL(center);
						padre.RefreshMap();

						
						
						}
					else{
						
					}
				}
				else
					{
					GWT.log("nuevo Punto");
					padre.getActual().setLL(center);
					padre.RefreshMap();
				        
					}
	            
	            
            // mapWidget.setZoom(8);

	            GWT.log("place changed center=" + center);
			
		}


        });
		
		
		
		}
		
		
	}
	

	
	
}
