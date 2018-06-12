/**
 * 
 */
package ucm.fdi.ilsa.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ScriptElement;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MVCArray;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.Polyline;
import com.google.maps.gwt.client.PolylineOptions;
import fdi.ucm.server.interconect.model.DocumentCompleteJSON;
import fdi.ucm.server.interconect.model.GrammarJSON;
import fdi.ucm.server.interconect.model.OperationalValueTypeJSON;
import fdi.ucm.server.interconect.model.StructureJSON;
import fdi.ucm.server.interconect.model.StructureJSON.TypeOfStructureEnum;

/**
 * @author Joaquin Gayoso-Cabada
 *
 */
public class CompositeDocumentDescriptionMaps {

	private static final String GEOLOCATION_POINTS = "Geolocation Points";
	private static final String GEOLOCATION_ROUTE= "Geolocation Route";
	private static final String ERROR_GRAMMAR = "Error Context can be applied to a grammar";
	public static final String GEOICONRED = "Geo/IconoRojo.png";
	public static final String GEOICOBLUE = "Geo/IconoAzul.png";
	public static final String GEOICONYEL = "Geo/IconoAmarillo.png";
	private static final String POINT = "Map Point: ";
	private static boolean added = false;
	private Panel PanelPrincipal;
	private String RandomIdVars;
	private int Heigh;
	private boolean CompleteView;
	private boolean Views;
	private Long ContextId;
	private StructureJSON SuperS;
	private int Width;

	
	public static void init()
	{
		Element head = Document.get().getElementsByTagName("head").getItem(0);
    ScriptElement sce = Document.get().createScriptElement();
    sce.setType("text/javascript");
    sce.setSrc("https://maps.googleapis.com/maps/api/js?key=AIzaSyBPj8iz7libsW74GvKYCU7VdtggaOA8814&v=3&sensor=true&libraries=places");
    head.appendChild(sce);
    
   
    
    }

	public CompositeDocumentDescriptionMaps(String randomIdVars, Long contextId, int Height, boolean CompleteView,boolean Grammar, boolean views2) {
		
		

		
		RandomIdVars=randomIdVars;
		this.Heigh=Height-32;
		this.CompleteView=CompleteView;
		this.Views=views2;
		ContextId=contextId;
		
Width=Window.getClientWidth();
		
		
		
int Variacion=315;

if (Width>515)
	Width=Width-Variacion;
		
		
	com.google.gwt.user.client.ui.RootPanel RP=com.google.gwt.user.client.ui.RootPanel.get(RandomIdVars);
		
		
		VerticalPanel VP = new VerticalPanel();
		VP.setSpacing(20);
		RP.add(VP);
		
		PanelPrincipal=VP;
		
		
	String SDocumentoS =getVariableBaseJSONOBJS(RandomIdVars);
		
//		Window.alert("hELLO Panel1 "+ SDocumentoS);
		
		JSONObject JSOSucion = (JSONObject) JSONParser.parseStrict(SDocumentoS);
		
//		Window.alert("hELLO Panel2 "+ JSOSucion.toString());
		

		
		
		
//		JSONValue JSOSucionV = JSOSucion.get(JSOSucion.keySet().iterator().next());
//		if (JSOSucionV.isObject()!=null)
//			JSOSucion=JSOSucionV.isObject();
		
		DocumentCompleteJSON Documento = CreateJSONObject.create(JSOSucion);
		
		GWT.log(Documento.getDocumento().getId()+"");
		
		
		if (Grammar)
			{
			Errordale();
			return;
			}
		
		StructureJSON S=null;
		for (GrammarJSON gramarpos : Documento.getGramatica()) {
			S=gotContext(gramarpos.getListaS(),contextId);
			
			if (S!=null)
				{
				
				SuperS=S;
			
//			if (!S.getClaseOf().equals(contextId))
//				{
//				S=gotContext(gramarpos.getListaS(),S.getClaseOf());
//				ContextId=S.getClaseOf();
//				contextId=S.getClaseOf();
//				}
			
				break;
				}
			
		}
		
		if (S!=null&&isEditorCorrecto(S))
			{
			GWT.log("Encontrado: "+S.getName() );
			
			if (S.isMultivalued()&&isROUTE(S))
			{
				ProcesoEnBloque(contextId,Documento,S);
			}
			else
				Proceso_simple(contextId,Documento,S);
			
			
			}
		else
		{
			Errordale();
			return;
		}
		
		
	}

	

	private void Proceso_simple(Long contextId, DocumentCompleteJSON documento, StructureJSON s2) {
		Long encontradoLat=0l;
		Long encontradoLong=0l;
		for (StructureJSON hijos : s2.getSons()) {
			if (hijos.getTypeOfStructure()==TypeOfStructureEnum.Text)
				for (OperationalValueTypeJSON ov : hijos.getShows()) {
					if (ov.getView().toLowerCase().equals("clavy"))
						if (ov.getName().toLowerCase().equals("gmaps"))
							{
							
							if (ov.getDefault().toLowerCase().equals("latitude"))
								encontradoLat=hijos.getClaseOf();
							
							if (ov.getDefault().toLowerCase().equals("longitude"))
								encontradoLong=hijos.getClaseOf();
							}
				}
				
	}
		
		
		List<StructureJSON> S=null;
		for (GrammarJSON gramarpos : documento.getGramatica()) {
			S=gotMultivaluedContext(gramarpos.getListaS(),contextId);
			if (S!=null)
				break;
		}
		
		if (S==null)
			{
			Errordale();
			return;
			}
		
		
		List<LatLng> lista=new ArrayList<LatLng>();
		HashMap<LatLng,StructureJSON> TS=new HashMap<LatLng,StructureJSON>();
		for (StructureJSON punto : S) {
			LatLng L=procesaPoint(punto,encontradoLat,encontradoLong);
			if (L!=null)
				{
				lista.add(L);
				TS.put(L, punto);
				}
		}
		
		GWT.log(lista.size()+"");
		VerticalPanel ListaV=new VerticalPanel();
		ListaV.setSize(Width+"px", "100%");
		PanelPrincipal.add(ListaV);
		
		VerticalPanel Glue=new VerticalPanel();
		Glue.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		Glue.add(new Label(GEOLOCATION_POINTS));
		Glue.setHeight("30px");
		ListaV.add(Glue);
		
		
		for (int i = 0; i < lista.size(); i++) {
		LatLng late =lista.get(i);
		StructureJSON SS=TS.get(late);
			VerticalPanel PanelMapaIndi=new VerticalPanel();
			PanelMapaIndi.setWidth("100%");
			ListaV.add(PanelMapaIndi);
			Label LL=new Label(POINT+(i+1));
			PanelMapaIndi.add(LL);
			PanelMapaIndi.add(Mapa(late,SS,GEOICONRED));
		}
	}

	private Widget Mapa(LatLng late, StructureJSON sS,String Icono) {
		SimplePanelMap panel=new SimplePanelMap();
		panel.setWidth("100%");
        panel.setHeight("400px");
        MapOptions options = MapOptions.create();
        options.setCenter(late);
        options.setZoom(13);
        options.setMapTypeId(MapTypeId.ROADMAP);
        options.setDraggable(true);
        options.setMapTypeControl(true);
        options.setScaleControl(true);
        options.setScrollwheel(true);
        options.setMapMaker(true);
//        Button btn = new Button();
        GoogleMap gMap = GoogleMap.create(panel.getElement(), options);

        panel.setVar(late,gMap);
        
        
//        LatLng centerIcon = LatLng.create(40.4169,-3.7033);
//        MarkerImage markerImage = MarkerImage.create();
        MarkerOptions mOpts = MarkerOptions.create();
//        mOpts.setIcon(markerImage);
        mOpts.setPosition(late);
        
        MarkerGeo.create(mOpts,sS,gMap,Icono,"",false);
        
        
        
        
        
		return panel;
	}

	private boolean isROUTE(StructureJSON s) {
		for (OperationalValueTypeJSON ov : s.getShows()) {
			if (ov.getView().toLowerCase().equals("clavy"))
				if (ov.getName().toLowerCase().equals("gmaps"))
					if (ov.getDefault().toLowerCase().equals("route"))
						return isEditorCorrectoChildrens(s);
		}
		return false;
	}

	private void ProcesoEnBloque(Long contextId, DocumentCompleteJSON documento, StructureJSON s2) {
		
		Long encontradoLat=0l;
		Long encontradoLong=0l;
		for (StructureJSON hijos : s2.getSons()) {
			if (hijos.getTypeOfStructure()==TypeOfStructureEnum.Text)
				for (OperationalValueTypeJSON ov : hijos.getShows()) {
					if (ov.getView().toLowerCase().equals("clavy"))
						if (ov.getName().toLowerCase().equals("gmaps"))
							{
							
							if (ov.getDefault().toLowerCase().equals("latitude"))
								encontradoLat=hijos.getClaseOf();
							
							if (ov.getDefault().toLowerCase().equals("longitude"))
								encontradoLong=hijos.getClaseOf();
							}
				}
				
	}
		
		
		List<StructureJSON> S=null;
		for (GrammarJSON gramarpos : documento.getGramatica()) {
			S=gotMultivaluedContext(gramarpos.getListaS(),contextId);
			if (S!=null)
				break;
		}
		
		if (S==null)
			{
			Errordale();
			return;
			}
		
		
		List<LatLng> lista=new ArrayList<LatLng>();
		HashMap<LatLng,StructureJSON> TS=new HashMap<LatLng,StructureJSON>();
		for (StructureJSON punto : S) {
			LatLng L=procesaPoint(punto,encontradoLat,encontradoLong);
			if (L!=null)
			{
				lista.add(L);
				TS.put(L, punto);
				}
		}
		
		GWT.log(lista.size()+"");
		
		VerticalPanel ListaV=new VerticalPanel();
		
		ListaV.setSize(Width+"px", "100%");
		PanelPrincipal.add(ListaV);
		
		VerticalPanel Glue=new VerticalPanel();
		Glue.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		Glue.add(new Label(GEOLOCATION_ROUTE));
		Glue.setHeight("30px");
		ListaV.add(Glue);
		
		
		if (lista.size()>0)
		{
			LatLng Center0=lista.get(0);
			SimplePanelMap panel=new SimplePanelMap();
			panel.setWidth("100%");
	        panel.setHeight((Heigh-30)+"px");
	        MapOptions options = MapOptions.create();
	        options.setCenter(Center0);
	        options.setZoom(13);
	        options.setMapTypeId(MapTypeId.ROADMAP);
	        options.setDraggable(true);
	        options.setMapTypeControl(true);
	        options.setScaleControl(true);
	        options.setScrollwheel(true);
	        options.setMapMaker(true);
//	        Button btn = new Button();
	        GoogleMap gMap = GoogleMap.create(panel.getElement(), options);
	       
	        panel.setVar(Center0,gMap);
	        ListaV.add(panel);
	        
	        MVCArray<LatLng> latLngArray = MVCArray.create();  
	        for (LatLng lng : lista) {  
	            latLngArray.push(lng);  
	        }  
	        PolylineOptions polyOpts = PolylineOptions.create();  
	        polyOpts.setPath(latLngArray);  
	        polyOpts.setStrokeColor("red");  
	        polyOpts.setStrokeOpacity(0.5);  
	        polyOpts.setStrokeWeight(5);  
	        Polyline path = Polyline.create(polyOpts);  
	        path.setMap(gMap);
		
		for (int i = 0; i < lista.size(); i++) {
			
			LatLng late =lista.get(i);
			StructureJSON SS=TS.get(late);
			
	        MarkerOptions mOpts = MarkerOptions.create();
	        mOpts.setPosition(late);        
	        
	        String Icon=GEOICONYEL;
	        if (i==0)
	        	Icon=GEOICONRED;
	        else
	        	if (i==lista.size()-1)
	        		Icon=GEOICOBLUE;
	        
	        MarkerGeo.create(mOpts,SS,gMap,Icon,"",false);
		
//			VerticalPanel PanelMapaIndi=new VerticalPanel();
//			PanelMapaIndi.setWidth("100%");
//			ListaV.add(PanelMapaIndi);
//			Label LL=new Label(POINT+(i+1));
//			PanelMapaIndi.add(LL);
//			PanelMapaIndi.add(Mapa(late,SS,GEOICONRED));
		}
		}

	}

	private LatLng procesaPoint(StructureJSON punto, Long encontradoLat, Long encontradoLong) {
		String LatS=null;
		String LongS=null;		
		for (StructureJSON hijos : punto.getSons()) {
			if (hijos.getClaseOf().equals(encontradoLat))
				LatS=hijos.getValue();
			if (hijos.getClaseOf().equals(encontradoLong))
				LongS=hijos.getValue();
		}
		
		try {
			double Latd=Double.parseDouble(LatS);
			double Longd=Double.parseDouble(LongS);
			
			return LatLng.create(Latd, Longd);
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		//	return LatLng.create(40.4169,-3.7033);
		}
	}

	private boolean isEditorCorrecto(StructureJSON s) {
		for (OperationalValueTypeJSON ov : s.getShows()) {
			if (ov.getView().toLowerCase().equals("clavy"))
				if (ov.getName().toLowerCase().equals("editor"))
					if (ov.getDefault().toLowerCase().equals("gmaps")||ov.getDefault().toLowerCase().equals("gmaps2"))
						return isEditorCorrectoChildrens(s);
		}
		return false;
	}

	private boolean isEditorCorrectoChildrens(StructureJSON s) {
		boolean encontradoLat=false;
		boolean encontradoLong=false;
		for (StructureJSON hijos : s.getSons()) {
				if (hijos.getTypeOfStructure()==TypeOfStructureEnum.Text)
					for (OperationalValueTypeJSON ov : hijos.getShows()) {
						if (ov.getView().toLowerCase().equals("clavy"))
							if (ov.getName().toLowerCase().equals("gmaps")||ov.getDefault().toLowerCase().equals("gmaps2"))
								{
								
								if (ov.getDefault().toLowerCase().equals("latitude"))
									encontradoLat=true;
								
								if (ov.getDefault().toLowerCase().equals("longitude"))
									encontradoLong=true;
								}
					}
					
		}
		return encontradoLat&&encontradoLong;
	}

	private void Errordale() {
		PanelPrincipal.add(new Label(ERROR_GRAMMAR));
		
	}
	
	
	private StructureJSON gotContext(List<StructureJSON> listaS, Long contextId) {
		for (StructureJSON structterJSON : listaS) {
			if (structterJSON.getId().get(0).equals(contextId))
				return structterJSON;
			else
				{
				StructureJSON S=gotContext(structterJSON.getSons(),contextId);
				if (S!=null)
					return S;
				}
		}
		return null;
	}
	
	
	private List<StructureJSON> gotMultivaluedContext(List<StructureJSON> listaS, Long contextId) {
		List<StructureJSON> Hermanos=new ArrayList<StructureJSON>();
		for (StructureJSON structterJSON : listaS) {
			if (structterJSON.getId().get(0).equals(contextId))
				{
					for (StructureJSON structureJSON2 : listaS) {
						if (structureJSON2.getClaseOf().equals(SuperS.getClaseOf())&&(SuperS.getFather()==structureJSON2.getFather()||
								(SuperS.getFather()!=null&&structureJSON2.getFather()!=null&&SuperS.getFather().equals(structureJSON2.getFather()))))
							Hermanos.add(structureJSON2);
					}
					
					return Hermanos;
				}
			else
				{
				List<StructureJSON> S=gotMultivaluedContext(structterJSON.getSons(),contextId);
				if (S!=null)
					return S;
				}
		}
		return null;
	}



	private GrammarJSON gotContext(ArrayList<GrammarJSON> gramatica, Long contextId) {
		for (GrammarJSON grammarJSON : gramatica) {
			if (grammarJSON.getId().equals(contextId))
				return grammarJSON;
		}
		return null;
		
	}
	
	
	public static native Panel getContextDiv(String ContextID) /*-{

	$wnd.daletmp = '$wnd.dale = $wnd.VDocExpand'+ContextID;
	eval($wnd.daletmp)
	  return  $wnd.dale;

	}-*/;
	
	
	public static native DocumentCompleteJSON getVariableBase(String documentID2) /*-{
	$wnd.daletmp = '$wnd.dale = $wnd.DocExpand'+documentID2;
eval($wnd.daletmp)
  return  $wnd.dale;	  

}-*/;

	public static String getIcon() {
		return "gmaps.png";
		
	}

	
	

	public static native String getVariableBaseJSONOBJS(String documentID2) /*-{
	$wnd.daletmp = '$wnd.dale = $wnd.JSDocExpand'+documentID2;
	eval($wnd.daletmp)
	return  $wnd.dale;	  

}-*/;
	
	
	
}
