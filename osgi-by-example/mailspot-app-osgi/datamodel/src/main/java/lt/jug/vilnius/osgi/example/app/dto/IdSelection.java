package lt.jug.vilnius.osgi.example.app.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author arturas
 *
 * @param <T>
 */
public class IdSelection{
	List<Long> selected = new ArrayList<Long>();

	public List<Long> getSelected() {
		return selected;
	}

	public void setSelected(List<Long> selected) {
		this.selected = selected;
	}
	
}
