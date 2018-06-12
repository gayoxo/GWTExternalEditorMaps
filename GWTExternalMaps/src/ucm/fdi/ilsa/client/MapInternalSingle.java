package ucm.fdi.ilsa.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.GoogleMap.ClickHandler;
import com.google.maps.gwt.client.places.Autocomplete;
import com.google.maps.gwt.client.places.AutocompleteOptions;
import com.google.maps.gwt.client.places.PlaceGeometry;
import com.google.maps.gwt.client.places.PlaceResult;
import com.google.maps.gwt.client.places.Autocomplete.PlaceChangedHandler;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.MouseEvent;

import fdi.ucm.server.interconect.model.StructureJSON;

public class MapInternalSingle {

	
//	protected static final String CONFIRMACION_EDIT = "Are you sure change the position of the point?";
	private String Icono;
	private StructureJSON Struct;
	private MarkerGeo MG;
	private GoogleMap gMap;
	private StructureJSON LatS;
	private StructureJSON LongS;
	private Autocomplete autoComplete;
	private static GoogleMap Nulo=null;

	public MapInternalSingle(SimplePanelMap panel, SimplePanel textAreaPanel, LatLng late, StructureJSON sS, String icono,Long encontradoLat,Long encontradoLong) {
		
		
		
		
		TextBox textea = new TextBox();
		textea.setWidth("90%");
		
		Icono=icono;
		Struct=sS;
		
		textAreaPanel.setWidget(textea);
		
		panel.setWidth("100%");
        panel.setHeight("400px");
        MapOptions options = MapOptions.create();
        
        if (late!=null)
        	options.setCenter(late);
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
//        Button btn = new Button();
        gMap = GoogleMap.create(panel.getElement(), options);

        panel.setVar(late,gMap);
        
        MG=null;
        
        LatS=null;
        LongS=null;		
		for (StructureJSON hijos : sS.getSons()) {
			if (hijos.getClaseOf().equals(encontradoLat))
				LatS=hijos;
			if (hijos.getClaseOf().equals(encontradoLong))
				LongS=hijos;
		}
		
	
        
        
        if (late!=null)     	
        	setPoint(late);


        
        gMap.addClickListener(new ClickHandler() {
			
			@Override
			public void handle(MouseEvent event) {
				LatLng act = event.getLatLng();
				if (MG!=null)
					if (Window.confirm(CompositeDocumentEditionMaps.CONFIRMACION_EDIT))
						{
						MG.getPunto().setMap(Nulo);
						
						setPoint(act);
						
						 LatS.setValue(Double.toString(act.lat()));
					        LongS.setValue(Double.toString(act.lng()));
						
						}
					else{
						
					}
				else
					{
					
						setPoint(act);
				        
				        LatS.setValue(Double.toString(act.lat()));
				        LongS.setValue(Double.toString(act.lng()));
				        
					}
						
				
			}
		});
        
        InputElement element = InputElement.as(textea.getElement());
        
        AutocompleteOptions options5 = AutocompleteOptions.create();
        
//		options5.setTypes(types);
        options5.setBounds(gMap.getBounds());
        
        autoComplete = Autocomplete.create(element, options5);

        
        autoComplete.addPlaceChangedListener(new PlaceChangedHandler() {


		@Override
		public void handle() {
			  PlaceResult result = autoComplete.getPlace();

	            PlaceGeometry geomtry = result.getGeometry();
	            LatLng center = geomtry.getLocation();

	            if (Window.confirm(CompositeDocumentEditionMaps.CONFIRMACION_EDIT))
	            {
	            gMap.panTo(center);
	            setPoint(center);
	            // mapWidget.setZoom(8);
	            LatS.setValue(Double.toString(center.lat()));
		        LongS.setValue(Double.toString(center.lng()));
	            
	            GWT.log("place changed center=" + center);
	            }
		}


        });
        
	}

	protected void setPoint(LatLng act) {

		 MarkerOptions mOpts = MarkerOptions.create();
//       mOpts.setIcon(markerImage);
       mOpts.setPosition(act);
       
       MG=MarkerGeo.create(mOpts,Struct,gMap,Icono,"",true);
		
	}
}
