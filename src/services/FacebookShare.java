package services;

import com.codename1.facebook.FaceBookAccess;
import com.codename1.io.NetworkEvent;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import java.io.IOException;
/**
 *
 * @author Fenina Malek
 */
public class FacebookShare {
    private static String token = "EAACEdEose0cBAEX0XgZCd8SVh8QpdE6lVZBBa2E7oTyJxZBiZCcVMPwTcZCnB0ZAtIo0WLIK44pZBqo4ZBasc0n0PCAyMjcgZAIwhFX1ivnRyt0ZAYKGo13R8FfKEXP0f6Yh22eqcJ9tMZBjVPNH5hrlvP5UOpxtKMm9PCJjXfExtnykLfEmy2OH8NWnsc1OdCWgrP6me4BQHoKPgZDZD";

    public FacebookShare(String token) {
        FaceBookAccess.setToken(token);
    }
    
    public void share(String text) throws IOException {
	FaceBookAccess.getInstance().addResponseCodeListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent evt) {
		NetworkEvent ne = (NetworkEvent) evt;
		int code = ne.getResponseCode();
		FaceBookAccess.getInstance().removeResponseCodeListener(this);
	    }
	});
	FaceBookAccess.getInstance().postOnWall("me", text);
    }
    
}
