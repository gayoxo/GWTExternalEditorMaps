/**
 * 
 */
package ucm.fdi.ilsa.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;

/**
 * @author Joaquin Gayoso-Cabada
 *
 */
public class SimplePanelMap extends SimplePanel {

	
	private LatLng Lat;
	private GoogleMap gMap;

	@Override
	protected void onAttach() {
		super.onAttach();
		gMap.triggerResize();
		if (Lat!=null)
			{
			gMap.triggerResize();
			gMap.triggerCenterChanged();
			gMap.setCenter(Lat);
			GWT.log(gMap.getCenter().lat()+":"+gMap.getCenter().lng());
//			Window.alert(gMap.getCenter().lat()+":"+gMap.getCenter().lng());
			}
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		gMap.triggerResize();
		if (Lat!=null)
			{
			gMap.triggerResize();
			gMap.triggerCenterChanged();
			gMap.setCenter(Lat);
			GWT.log(gMap.getCenter().lat()+":"+gMap.getCenter().lng());
//			Window.alert(gMap.getCenter().lat()+":"+gMap.getCenter().lng());
			}
	}
	

	public void setVar(LatLng late, GoogleMap gMap) {
		Lat=late;
		this.gMap=gMap;
	}
}
