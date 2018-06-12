package ucm.fdi.ilsa.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GWTExternalMaps implements EntryPoint {
	
	private static CompositeDocumentEditionMaps Actual;
	private static boolean init;

	static {
        export();
        CompositeDocumentDescriptionMaps.init();
    }
	
	public GWTExternalMaps() {
		
	}
	
	
	/**
     * Makes our setData method accessible from plain JS
     */
    private static native void export() /*-{
    	
    	$wnd.GMapsSetContext = @ucm.fdi.ilsa.client.GWTExternalMaps::setContext(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZZZZ)
    	$wnd.GMapsGetIcon = @ucm.fdi.ilsa.client.GWTExternalMaps::getIcon()
    	$wnd.GMapsPersist = @ucm.fdi.ilsa.client.GWTExternalMaps::getPersist()
    	
    }-*/;

    public static void setContext(String IdVentana,String contextId,String Height,boolean isgrammar,boolean edit,boolean views,boolean CompleteView) {
//		try {
    		
			Long contLong=Long.parseLong(contextId);
			Integer heiInteger=Integer.parseInt(Height);
			if (edit)
				{
				
				Actual=new CompositeDocumentEditionMaps(IdVentana, contLong, heiInteger, isgrammar);
				}
			else
			{
				
				new CompositeDocumentDescriptionMaps(IdVentana, contLong, heiInteger, CompleteView, isgrammar, views);
			}
//		} catch (Exception e) {
//			Window.alert(e.getMessage());
//			Window.
//			e.printStackTrace();
//		}
		
		
	}
    
    public static String getIcon() {
    	return CompositeDocumentDescriptionMaps.getIcon();

		
		
	}
    
    public static void getPersist() {
    	if (Actual!=null)
    		Actual.persistJS();
		
		
	}


	@Override
	public void onModuleLoad() {
		GWT.log("Maps Load");
		
	}
}
