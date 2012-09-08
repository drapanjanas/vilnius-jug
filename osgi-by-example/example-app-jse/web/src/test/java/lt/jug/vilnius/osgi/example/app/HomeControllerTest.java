package lt.jug.vilnius.osgi.example.app;

import junit.framework.Assert;
import lt.jug.vilnius.osgi.example.app.HomeController;

import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

public class HomeControllerTest {

	@Test
	public void testController() {
		HomeController controller = new HomeController();
		Model model = new ExtendedModelMap();
		Assert.assertEquals("home",controller.home(model));
		
		Object message = model.asMap().get("controllerMessage");
		Assert.assertEquals("Please login to inbox",message);
		
	}
}
