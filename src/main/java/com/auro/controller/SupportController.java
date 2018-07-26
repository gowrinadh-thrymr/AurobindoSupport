package com.auro.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.auro.utility.ConnectionInfo;
import com.auro.utility.SupportContent;
import com.ibm.portal.um.PumaHome;
import com.ibm.portal.um.PumaProfile;
import com.ibm.portal.um.User;
import com.ibm.portal.um.exceptions.PumaAttributeException;
import com.ibm.portal.um.exceptions.PumaException;
import com.ibm.portal.um.exceptions.PumaMissingAccessRightsException;
import com.ibm.portal.um.exceptions.PumaModelException;
import com.ibm.portal.um.exceptions.PumaSystemException;
import com.ibm.workplace.wcm.api.SiteArea;
import com.ibm.workplace.wcm.api.Workspace;

@Controller
@RequestMapping(value = "VIEW")
public class SupportController {

	private PumaHome pumaHome;

	@PostConstruct
	public void init() {
		javax.naming.Context ctx;
		try {
			ctx = new javax.naming.InitialContext();
			pumaHome = (PumaHome) ctx.lookup(PumaHome.JNDI_NAME);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public User getCurrentUser() throws PumaException {
		// To retrieve current User
		PumaProfile profile = pumaHome.getProfile();
		return profile.getCurrentUser();
	}

	public Map<String, Object> getUserAttributes(User user)
			throws PumaAttributeException, PumaSystemException, PumaModelException, PumaMissingAccessRightsException {
		List<String> returnAttributes = new ArrayList<String>();
		returnAttributes.add("uid");
		returnAttributes.add("cn");
		PumaProfile profile = pumaHome.getProfile();
		Map<String, Object> values = profile.getAttributes(user, returnAttributes);
		return values;
	}

	@RenderMapping
	public String defaultView(ModelMap map) {
		String userName = "";
		String redirectionPage = "";
		try {
			JSONObject json = null;
			LinkedList<JSONObject> list = new LinkedList<>();
			InitialLdapContext ctx = ConnectionInfo.getConnectionInstance();
			Map<String, Object> getUser = getUserAttributes(getCurrentUser());
			for (Entry<String, Object> entry : getUser.entrySet()) {
				if (entry.getKey().equalsIgnoreCase("uid")) {
					userName = entry.getValue().toString();
				}
			}
		
		if (userName.equalsIgnoreCase("anonymous portal user")) {
			redirectionPage = "anonymous";
			System.out.println("USERNAME  " + userName);
			Workspace workspace = SupportContent.getInstance().getWorkSpace();
			if (workspace != null) {
				List<SiteArea> siteAreas = SupportContent.getInstance().getSupportSiteArea(workspace);
				if (siteAreas != null) {
			list = SupportContent.getInstance().getAllContent(workspace, siteAreas);
				}
			} else {

			}
			map.addAttribute("supportjson", list);
		} else {
			redirectionPage = "support";
			if (userName != null && !userName.isEmpty()) {
				System.out.println("USERNAME  " + userName);
				json = getUserBasicAttributes(userName, ctx);
				System.out.println(" USER ATTRIBUTES JSON " + json);
				Workspace workspace = SupportContent.getInstance().getWorkSpace();
				if (workspace != null) {
					List<SiteArea> siteAreas = SupportContent.getInstance().getSupportSiteArea(workspace);
					if (siteAreas != null) {
				list = SupportContent.getInstance().getContent(json.get("unit").toString(), workspace,siteAreas);
					}
				} else {

				}
			}
			map.addAttribute("supportjson", list);
		}

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("EXCEPTION @@@@@@@@@@ " + ex);
		}
		System.out.println("REDIRECTING TO "+redirectionPage);
		return redirectionPage;
	}

	private JSONObject getUserBasicAttributes(String username, LdapContext ctx) {
		JSONObject userAttributes = new JSONObject();
		try {
			SearchControls constraints = new SearchControls();
			String filter = "(uid=" + username + ")";
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
			String[] attrIDs = { "cn", "mail", "Unit", "Division","Entity" };
			constraints.setReturningAttributes(attrIDs);
			String searchBase = "CN=hydhoconn,DC=corp,DC=aurobindo,DC=com";

			// perform search on directory
			NamingEnumeration answer = ctx.search(searchBase, filter, constraints);
			if (answer != null && answer.hasMore()) {
				// System.out.println("ANSWERS" + answer);
				Attributes attrs = ((SearchResult) answer.next()).getAttributes();
				if(attrs.get("cn").get()!=null)
				userAttributes.put("cn", (String) attrs.get("cn").get());
				
				if(attrs.get("mail").get()!=null)
				userAttributes.put("mail", (String) attrs.get("mail").get());
				
				if(attrs.get("Unit").get()!=null)
				userAttributes.put("unit", (String) attrs.get("Unit").get());
				
				if(attrs.get("Division").get()!=null)
				userAttributes.put("division", (String) attrs.get("Division").get());

			} else {
				System.out.println("--------------");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Exception@@@@@@@  " + ex.getMessage());
		}
		return userAttributes;
	}
}
