/**
 * 
 */
package ucm.fdi.ilsa.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.PositionError;
import com.google.gwt.geolocation.client.Position.Coordinates;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;

import fdi.ucm.server.interconect.model.DocumentCompleteJSON;
import fdi.ucm.server.interconect.model.GrammarJSON;
import fdi.ucm.server.interconect.model.OperationalValueTypeJSON;
import fdi.ucm.server.interconect.model.StructureJSON;
import fdi.ucm.server.interconect.model.StructureJSON.TypeOfStructureEnum;

/**
 * @author Joaquin Gayoso-Cabada
 *
 */
public class CompositeDocumentEditionMaps {

	private static final String GEOLOCATION_POINTS = "Geolocation Points";
	private static final String GEOLOCATION_ROUTE= "Geolocation Route";
	private static final String ERROR_GRAMMAR = "Error Context can be applied to a grammar";
	protected static final String CONFIRMACION_EDIT = "Are you sure change the position of the point?";
	private static final String PUSH_THIS_BUTTON_MOVE_UP_ITERABLE_ELEMENT = "Push this button to move up iterable elemnt";
	private static final String PUSH_THIS_BUTTON_MOVE_DOWN_ITERABLE_ELEMENT = "Push this button to move down iterable element";
	private static final String IMGUP = "Direccionales/Arriba.gif";
	private static final String IMGDOWN = "Direccionales/Abajo.gif";
	public static final String GEOICONRED = "Geo/IconoRojo.png";
	public static final String GEOICOBLUE = "Geo/IconoAzul.png";
	public static final String GEOICONYEL = "Geo/IconoAmarillo.png";
	private static final String POINT = "Map Point: ";
	private static final String YOU_ARE_ON_TOP = "You are on the top";
	private static final String YOU_ARE_ON_BOTTON = "You are on the botton";
	private static final String ICONOS_EDIT_ADD_PNG = "Geo/EditAdd.png";
	private static double Latitud=40.45284581040915;
	private static double Longitud=-3.7332916259765625;

	private static boolean added = false;
	private Panel PanelPrincipal;
	private String RandomIdVars;
	private int Heigh;
	private boolean Views;
	private Long ContextId;
	private DocumentCompleteJSON Documento;
	private List<ButtonStruc> Disponibles;
	private ButtonStruc Actual;
	private VerticalPanel VPpanel;
	private StructureJSON SuperS;

	private static GoogleMap Nulo=null;
	
	public static void init()
	{
		Latitud=40.45284581040915;
		Longitud=-3.7332916259765625;
	}

	public CompositeDocumentEditionMaps(String randomIdVars, Long contextId, int Height, boolean Grammar) {
		RandomIdVars=randomIdVars;
		this.Heigh=Height-32;
		ContextId=contextId;
		Disponibles=new ArrayList<ButtonStruc>();
		
		getPosition();
		
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
		
		Documento=CreateJSONObject.create(JSOSucion);
		
		GWT.log(Documento.getDocumento().getId()+"");
		
		if (Grammar)
			{
			setError();
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
			setError();
			return;
		}
		
		
	}
	
	
	private void getPosition() {
		 Latitud=40.45284581040915;
		    Longitud=-3.7332916259765625;
		    
			 Geolocation geolocation = Geolocation.getIfSupported();
				if (geolocation != null) {
				    geolocation.watchPosition(new Callback<Position, PositionError>() {
				      @Override
				      public void onFailure(PositionError reason) {
				        //TODO handle error
				      }

				      @Override
				      public void onSuccess(Position result) {
				        Coordinates coor = result.getCoordinates();
				       Latitud=coor.getLatitude();
				       Longitud=coor.getLongitude();

				      }
				    });
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
			setError();
			return;
			}
		
		
		VerticalPanel ListaV=new VerticalPanel();
		ListaV.setSize("100%", "100%");
		PanelPrincipal.add(ListaV);
		
		VerticalPanel Glue=new VerticalPanel();
		Glue.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		Glue.add(new Label(GEOLOCATION_POINTS));
		Glue.setHeight("30px");
		ListaV.add(Glue);

		for (int i = 0; i < S.size(); i++) {
		    StructureJSON punto =S.get(i);
			LatLng L=procesaPoint(punto,encontradoLat,encontradoLong);
			
			VerticalPanel PanelMapaIndi=new VerticalPanel();
			PanelMapaIndi.setWidth("100%");
			ListaV.add(PanelMapaIndi);
			Label LL=new Label(POINT+(i+1));
			PanelMapaIndi.add(LL);
			SimplePanel SPtext=new SimplePanel();
			SPtext.setWidth("100%");
			PanelMapaIndi.add(SPtext);
			PanelMapaIndi.add(Mapa(L,punto,GEOICONRED,encontradoLat,encontradoLong,SPtext));
		}
		
		if (s2.isMultivalued())
			{
			Button B= new Button("Add new location");
			VerticalPanel Glue2=new VerticalPanel();
			Glue2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			Glue2.setHeight("30px");
			ListaV.add(Glue2);
			ListaV.add(B);
			B.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					createIterator(Long.toString(ContextId),Long.toString(Documento.getDocumento().getId()),"gmaps",false);
//					ControladorEditor.createIteration(Long.toString(ContextId),Long.toString(Documento.getDocumento().getId()),"gmaps",false);
					
				}
			});
			}
	}

	private Widget Mapa(LatLng late, StructureJSON sS,String Icono,Long encontradoLat,Long encontradoLong, SimplePanel textAreaPanel) {
		SimplePanelMap panel=new SimplePanelMap();
		
		new MapInternalSingle(panel,textAreaPanel,late,sS,Icono,encontradoLat,encontradoLong);
		
        
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
			setError();
			return;
			}
		
		
		
		List<LatLng> lista=new ArrayList<LatLng>();
		
		HashMap<StructureJSON,LatLng> TSI=new HashMap<StructureJSON,LatLng>();
		for (StructureJSON punto : S) {
			LatLng L=procesaPoint(punto,encontradoLat,encontradoLong);
			if (L!=null)
			{
				lista.add(L);
				TSI.put(punto, L);
				}
		}
		
		GWT.log(lista.size()+"");
		
		VerticalPanel Vertila=new VerticalPanel();
		Vertila.setWidth("100%");
		PanelPrincipal.add(Vertila);
		
		VerticalPanel Glue=new VerticalPanel();
		Glue.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		Glue.add(new Label(GEOLOCATION_ROUTE));
		Glue.setHeight("30px");
		Vertila.add(Glue);
		
		DockLayoutPanel ListaV = new DockLayoutPanel(Unit.PX);
		
		ListaV.setSize("100%", (Heigh-30)+"px");
		Vertila.add(ListaV);
		
		ScrollPanel ScrolPanel=new ScrollPanel();
		ScrolPanel.setWidth("100%");
		VerticalPanel VP=new VerticalPanel();
		ScrolPanel.add(VP);
		
		for (int i = 0; i < S.size(); i++) {
			
			HorizontalPanel ElementPanelActual = new HorizontalPanel();
			
			StructureJSON hijos = S.get(i);
			ButtonStruc Boton;
			LatLng LL=TSI.get(hijos);
			
			Boton=new ButtonStruc(Disponibles.size()+1,hijos,LL,encontradoLat,encontradoLong);
			
			Boton.setWidth("100%");
			
			Boton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent arg0) {
					try {
						ButtonStruc BS=(ButtonStruc)arg0.getSource();
						Actual.removeStyleName("Seleccionado");
						Actual=BS;
						Actual.addStyleName("Seleccionado");
					} catch (Exception e) {
						// falla la aplicacion del estilo, cosa que no deberia fallar nunca
						GWT.log(e.getLocalizedMessage());
						e.printStackTrace();
					}
					
					
				}
			});
				
			
			
			
			Disponibles.add(Boton);	
			
			
			
			Image MoveUp=new ImageButtonStruc(IMGUP,Boton);
			MoveUp.setSize("22px", "22px");
			MoveUp.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
					try {
						ImageButtonStruc II=(ImageButtonStruc) event.getSource();
						procesoUPIterador(II);
					} catch (Exception e) {
						// TODO: handle exception
					}
					
					
				}
			});
			MoveUp.setTitle(PUSH_THIS_BUTTON_MOVE_UP_ITERABLE_ELEMENT);
			ElementPanelActual.add(MoveUp);
			
			Image MoveDown=new ImageButtonStruc(IMGDOWN,Boton);
			MoveDown.setSize("22px", "22px");
			MoveDown.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
					ImageButtonStruc II=(ImageButtonStruc) event.getSource();
					procesoDOWNIterador(II);
					
				}
			});
			MoveDown.setTitle(PUSH_THIS_BUTTON_MOVE_DOWN_ITERABLE_ELEMENT);
			ElementPanelActual.add(MoveDown);
			
			
			ElementPanelActual.add(Boton);
			
			
			VP.add(ElementPanelActual);
			
			
			
			
			if (i==0)
				{
				Boton.addStyleName("Seleccionado");
				Actual=Boton;
				}
			
		}
		
		Image botonMas=new Image(ICONOS_EDIT_ADD_PNG);
		
		botonMas.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				createIterator(Long.toString(ContextId),Long.toString(Documento.getDocumento().getId()),"gmaps",false);
//				ControladorEditor.createIteration(Long.toString(ContextId),Long.toString(Documento.getDocumento().getId()),"gmaps",false);
				
			}
		});
		
		VP.add(botonMas);
		
		
		VPpanel=new VerticalPanel();
		
		//SINO NO ME CARGA EL MAPA
		VPpanel.setSize(ListaV.getOffsetWidth()-200+"px", "100%");
		
		
		
		
		
		
		ListaV.addWest(ScrolPanel, 200);
		
		ListaV.add(VPpanel);
				
		RefreshMap();


	}
	
	
	protected void procesoDOWNIterador(ImageButtonStruc iI) {
		
		ButtonStruc Encontrado=null;
		ButtonStruc Siguiente=null;
		ButtonStruc Buscar=iI.getBoton();
		for (ButtonStruc botones : Disponibles) {
			if (botones==Buscar)
				Encontrado=botones;
			else
				{
				//la primera vez que pasa por aqui encuentra al hermano la segunda no
				if (Encontrado!=null)
					Siguiente=botones;

					Encontrado=null;
				}
				
		}
		
		if (Encontrado!=null)
		{
			Window.alert(YOU_ARE_ON_BOTTON);		
			return;
		}
		
		
		moveDOWN(Long.toString(ContextId),Long.toString(Siguiente.getStructElem().getId().get(0)),Long.toString(Buscar.getStructElem().getId().get(0)),Long.toString(Documento.getDocumento().getId()),"gmaps",false);
//		ControladorEditor.moveDOWN(Long.toString(ContextId),Long.toString(Siguiente.getStructElem().getId().get(0)),Long.toString(Buscar.getStructElem().getId().get(0)),Long.toString(Documento.getDocumento().getId()),"gmaps",false);

		
	}

	protected void procesoUPIterador(ImageButtonStruc iI) {
		
		ButtonStruc Anterior=null;
		ButtonStruc Buscar=iI.getBoton();
		for (ButtonStruc botones : Disponibles) {
			if (botones==Buscar)
				break;
			else
				Anterior=botones;
		}
		
		if (Anterior==null)
		{
			Window.alert(YOU_ARE_ON_TOP);		
			return;
		}
		
		moveUP(Long.toString(ContextId),Long.toString(Anterior.getStructElem().getId().get(0)),Long.toString(Buscar.getStructElem().getId().get(0)),Long.toString(Documento.getDocumento().getId()),"gmaps",false);
		
//		ControladorEditor.moveUP(Long.toString(ContextId),Long.toString(Anterior.getStructElem().getId().get(0)),Long.toString(Buscar.getStructElem().getId().get(0)),Long.toString(Documento.getDocumento().getId()),"gmaps",false);
		
		
		
	//	RefreshMap();
		
	}

	public void RefreshMap() {
		
		if (VPpanel!=null)
			{
			VPpanel.clear();
			
			SimplePanel panelTA = new SimplePanel();
			panelTA.setSize("100%", "100%");
			
			VPpanel.add(panelTA);
			VPpanel.add(RefreshMap(panelTA));
			
			}
		
		

	}
	

	private SimplePanelMap RefreshMap(SimplePanel panelTA) {
		
		SimplePanelMap panelMapa = new SimplePanelMap();
		
		new MapInternalMultiple(this,panelMapa,panelTA);
		
		return panelMapa;
		
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

	private void setError() {
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

	public static native void setVariableBase(DocumentCompleteJSON DocumentoExpandido, String idrandomdoct) /*-{


	$wnd.tmp=DocumentoExpandido;
	$wnd.str = '$wnd.DocExpand'+idrandomdoct +' = $wnd.tmp';
	console.log($wnd.str);
	eval($wnd.str)

	}-*/;

//	@Override
	public void persistJS() {
		GWT.log(Documento.toString());
		setVariableBase2(Documento,RandomIdVars);
		
	}
	
	public List<ButtonStruc> getDisponibles() {
		return Disponibles;
	}
	
	public int getHeigh() {
		return Heigh;
	}
	
	public ButtonStruc getActual() {
		return Actual;
	}

	public static Double getLatitude() {
		return Latitud;
	}

	public static Double getLongitude() {
		return Longitud;
	}

//	@Override
	public boolean isWaitingUpdate() {
		return false;
	}



//	@Override
	public boolean updateContext() {
		return true;
	}



//	@Override
	public void setWaitingUpdate(boolean update) {
		//NADA
	}

	
	public static native String getVariableBaseJSONOBJS(String documentID2) /*-{
	$wnd.daletmp = '$wnd.dale = $wnd.JSDocExpand'+documentID2;
	eval($wnd.daletmp)
	return  $wnd.dale;	  

}-*/;
	
	
	private JSONObject setVariableBase2(DocumentCompleteJSON documento2, String randomIdVars2) {
		JSONObject DocumentoJ = CreateJSONObject.create(documento2);
//		JSONObject DocumentoJ = CreateJSONObject(documento2);
		setVariableBase3(DocumentoJ.toString(),randomIdVars2);
		return DocumentoJ;
		
	}
	
	public static native void setVariableBase3(String DocumentoExpandido, String idrandomdoct) /*-{


	$wnd.tmp=DocumentoExpandido;
	$wnd.str = '$wnd.JSDocExpand'+idrandomdoct +' = $wnd.tmp';
	console.log($wnd.str);
	eval($wnd.str)

	}-*/;
	
	
	private static native String createIterator(String contextId, String documentId, String Editor, boolean isgrammar) /*-{
	 return window.parent.createIterator(contextId,documentId,Editor,isgrammar);
}-*/;
	
	
	private static native String moveUP(String contextId, String Anterior, String Elemento, String documentId, String Editor, boolean isgrammar) /*-{
	 return window.parent.moveUP(contextId,Anterior,Elemento,documentId,Editor,isgrammar);
}-*/;
	
	
	
	private static native String moveDOWN(String contextId, String Anterior, String Elemento, String documentId, String Editor, boolean isgrammar) /*-{
	 return window.parent.moveDOWN(contextId,Anterior,Elemento,documentId,Editor,isgrammar);
}-*/;
	
	
	
}
