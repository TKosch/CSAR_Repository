package org.opentosca.csarrepo.service;

import java.net.MalformedURLException;
import java.net.URL;

import org.opentosca.csarrepo.exception.PersistenceException;
import org.opentosca.csarrepo.model.WineryServer;
import org.opentosca.csarrepo.model.repository.WineryServerRepository;

public class CreateWineryServerService extends AbstractService {

	public CreateWineryServerService(long userId, String name, String uri) {
		super(userId);

		// validate the name
		name.trim();
		if (name.isEmpty() || name.length() > 255) {
			AbstractServlet.addError("nameLengthError");
		}

		// validate uri
		try {
			URL address = new URL(uri);
			if (!super.hasErrors()) {
				// no errors save
				WineryServer ws = new WineryServer();
				ws.setName(name);
				ws.setAddress(address);
				WineryServerRepository repo = new WineryServerRepository();
				try {
					repo.save(ws);
				} catch (PersistenceException e) {
					AbstractServlet.addError("savingWineryFailed");
				}
			}
		} catch (MalformedURLException e1) {
			AbstractServlet.addError("invalidURIError");
		}
	}

	public boolean getResult() {
		return !super.hasErrors();
	}
}
