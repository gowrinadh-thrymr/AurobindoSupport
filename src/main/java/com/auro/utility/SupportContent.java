package com.auro.utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONObject;

import com.ibm.workplace.wcm.api.Content;
import com.ibm.workplace.wcm.api.ContentComponent;
import com.ibm.workplace.wcm.api.DateComponent;
import com.ibm.workplace.wcm.api.DocumentIdIterator;
import com.ibm.workplace.wcm.api.DocumentIterator;
import com.ibm.workplace.wcm.api.DocumentTypes;
import com.ibm.workplace.wcm.api.NumericComponent;
import com.ibm.workplace.wcm.api.Repository;
import com.ibm.workplace.wcm.api.RichTextComponent;
import com.ibm.workplace.wcm.api.ShortTextComponent;
import com.ibm.workplace.wcm.api.SiteArea;
import com.ibm.workplace.wcm.api.TextComponent;
import com.ibm.workplace.wcm.api.WCM_API;
import com.ibm.workplace.wcm.api.Workspace;
import com.ibm.workplace.wcm.api.exceptions.AuthorizationException;
import com.ibm.workplace.wcm.api.exceptions.ComponentNotFoundException;
import com.ibm.workplace.wcm.api.exceptions.DocumentRetrievalException;
import com.ibm.workplace.wcm.api.exceptions.OperationFailedException;
import com.ibm.workplace.wcm.api.exceptions.ServiceNotAvailableException;

public class SupportContent {

	private static SupportContent supportContent;
	
	private final String siteAreaName = "SA_Support";
	private final String libraryName = "Aurobindo_ContentLib";

	private SupportContent() {

	}

	public static SupportContent getInstance() {
		if (supportContent == null) {
			supportContent = new SupportContent();
		}
		return supportContent;
	}

	public Workspace getWorkSpace() {
		// retrieve repository
		Repository repository = WCM_API.getRepository();
		// get contentWorkSpace
		Workspace contentWorkSpace = null;
		try {
			contentWorkSpace = repository.getWorkspace();
		} catch (ServiceNotAvailableException | OperationFailedException e) {
			e.printStackTrace();
		}
		return contentWorkSpace;
	}

	public List<SiteArea> getSupportSiteArea(Workspace contentWorkSpace) {
		List<SiteArea> siteAreas = null;
		if (contentWorkSpace != null) {
			siteAreas = new ArrayList<SiteArea>();
			// Set library
			try {
				contentWorkSpace.setCurrentDocumentLibrary(contentWorkSpace
						.getDocumentLibrary(libraryName.trim()));
			} catch (Exception e1) {
				e1.printStackTrace();
				System.err.println("error setting the current library to "+ libraryName);
			}

			// find sitearea by name
			DocumentIdIterator siteAreaIterator = null;
			try {
				siteAreaIterator = contentWorkSpace.findByName(
						DocumentTypes.SiteArea, siteAreaName);
			} catch (Exception e1) {
				e1.printStackTrace();
				System.err.println("error finding the siteArea " + siteAreaName);
			}

			SiteArea siteArea = null;
			if (siteAreaIterator != null) {
				if (siteAreaIterator.hasNext()) {
					try {
						siteArea = (SiteArea) contentWorkSpace
								.getById(siteAreaIterator.nextId());
						if (siteArea != null) {
							siteAreas.add(siteArea);
						}
					} catch (DocumentRetrievalException
							| AuthorizationException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return siteAreas;
	}
	
	
	
	public LinkedList<JSONObject> getContent(String name,Workspace contentWorkSpace, List<SiteArea> siteAreas) {
		
		LinkedList<JSONObject> listObject = new LinkedList<JSONObject>();
		for (SiteArea siteArea : siteAreas) {
			DocumentIterator it = contentWorkSpace.getByIds(siteArea.getAllChildren(), false, true);
			if (it != null) {
				Object obj = null;
				Content content = null;
				while (it.hasNext()) {
					obj = it.next();
					if (obj instanceof Content) {
						content = (Content) obj;
						if(content.getName().equalsIgnoreCase(name.trim())){
//							System.out.println("ENTERED INTO CONTENT "+name);
							try {
								JSONObject map = new JSONObject();
								ContentComponent division = (ContentComponent) content.getComponent("Division");
								ShortTextComponent divisionComponent = (ShortTextComponent) division;
								map.put("division",divisionComponent.getText().trim());
								
								ContentComponent unitId = (ContentComponent) content.getComponent("Unit Id");
								TextComponent unitComponent = (TextComponent) unitId;
								map.put("unitId", unitComponent.getText().trim());
								
								ContentComponent helpdeskEmail = (ContentComponent) content.getComponent("HelpDesk Email");
								TextComponent helpdeskEmailComponent = (TextComponent) helpdeskEmail;
								map.put("helpdeskEmail", helpdeskEmailComponent.getText().trim());
								
								ContentComponent internalCode = (ContentComponent) content.getComponent("Internal Code");
								NumericComponent internalCodeComponent = (NumericComponent) internalCode;
								map.put("internalCode", internalCodeComponent.getNumber());
								
								ContentComponent helpdeskContactNo = (ContentComponent) content.getComponent("HelpDesk Contact No");
								TextComponent helpdeskContactNoComponent = (TextComponent) helpdeskContactNo;
								map.put("helpdeskContactNo", helpdeskContactNoComponent.getText().trim());
								
								ContentComponent isManagerName = (ContentComponent) content.getComponent("IS Manager Name");
								TextComponent isManagerNameComponent = (TextComponent) isManagerName;
								map.put("isManagerName", isManagerNameComponent.getText().trim());
								
								try {
									ContentComponent isManagerMailId = (ContentComponent) content.getComponent("IS Manager Mail IDs");
									TextComponent isManagerMailIdComponent = (TextComponent) isManagerMailId;
									map.put("isManagerMailId", isManagerMailIdComponent.getText().trim());
								} catch (Exception e) {
									e.printStackTrace();
									map.put("isManagerMailId", "");
								}
								
								ContentComponent isManagerMobileNo = (ContentComponent) content.getComponent("IS Manager Mobile number");
								TextComponent isManagerMobileNoComponent = (TextComponent) isManagerMobileNo;
								map.put("isManagerMobileNo", isManagerMobileNoComponent.getText().trim());
								
								ContentComponent ext = (ContentComponent) content.getComponent("Ext");
								TextComponent extComponent = (TextComponent) ext;
								map.put("ext", extComponent.getText().trim());
								
								ContentComponent unitAddress = (ContentComponent) content.getComponent("Unit Address");
								RichTextComponent unitAddressComponent = (RichTextComponent) unitAddress;
								map.put("unitAddress", unitAddressComponent.getRichText().trim());
								
								System.out.println("ADDED TO LIST ");
								listObject.add(map);
								
							} catch (ComponentNotFoundException e) {
								e.printStackTrace();
								System.out.println("EXCEPTION @@@@@ "+e.getMessage());
							}
						}
					}
				}
			}
		}
		return listObject;
	}
	
	
	
	public LinkedList<JSONObject> getAllContent(Workspace contentWorkSpace, List<SiteArea> siteAreas) {
		LinkedList<JSONObject> listObject = new LinkedList<JSONObject>();
		for (SiteArea siteArea : siteAreas) {
			DocumentIterator it = contentWorkSpace.getByIds(siteArea.getAllChildren(), false, true);
			if (it != null) {
				Object obj = null;
				Content content = null;
				while (it.hasNext()) {
					obj = it.next();
					if (obj instanceof Content) {
						content = (Content) obj;
							try {
								JSONObject map = new JSONObject();
								ContentComponent division = (ContentComponent) content.getComponent("Division");
								ShortTextComponent divisionComponent = (ShortTextComponent) division;
								map.put("division",divisionComponent.getText().trim());
								
								ContentComponent unitId = (ContentComponent) content.getComponent("Unit Id");
								TextComponent unitComponent = (TextComponent) unitId;
								map.put("unitId", unitComponent.getText().trim());
								
								ContentComponent helpdeskEmail = (ContentComponent) content.getComponent("HelpDesk Email");
								TextComponent helpdeskEmailComponent = (TextComponent) helpdeskEmail;
								map.put("helpdeskEmail", helpdeskEmailComponent.getText().trim());
								
								ContentComponent internalCode = (ContentComponent) content.getComponent("Internal Code");
								NumericComponent internalCodeComponent = (NumericComponent) internalCode;
								map.put("internalCode", internalCodeComponent.getNumber());
								
								ContentComponent helpdeskContactNo = (ContentComponent) content.getComponent("HelpDesk Contact No");
								TextComponent helpdeskContactNoComponent = (TextComponent) helpdeskContactNo;
								map.put("helpdeskContactNo", helpdeskContactNoComponent.getText().trim());
								
								ContentComponent isManagerName = (ContentComponent) content.getComponent("IS Manager Name");
								TextComponent isManagerNameComponent = (TextComponent) isManagerName;
								map.put("isManagerName", isManagerNameComponent.getText().trim());
								
								try {
									ContentComponent isManagerMailId = (ContentComponent) content.getComponent("IS Manager Mail IDs");
									TextComponent isManagerMailIdComponent = (TextComponent) isManagerMailId;
									map.put("isManagerMailId", isManagerMailIdComponent.getText().trim());
								} catch (Exception e) {
									e.printStackTrace();
									map.put("isManagerMailId", "");
								}
								
								ContentComponent isManagerMobileNo = (ContentComponent) content.getComponent("IS Manager Mobile number");
								TextComponent isManagerMobileNoComponent = (TextComponent) isManagerMobileNo;
								map.put("isManagerMobileNo", isManagerMobileNoComponent.getText().trim());
								
								ContentComponent ext = (ContentComponent) content.getComponent("Ext");
								TextComponent extComponent = (TextComponent) ext;
								map.put("ext", extComponent.getText().trim());
								
								ContentComponent unitAddress = (ContentComponent) content.getComponent("Unit Address");
								RichTextComponent unitAddressComponent = (RichTextComponent) unitAddress;
								map.put("unitAddress", unitAddressComponent.getRichText().trim());
								
								listObject.add(map);
								
							} catch (ComponentNotFoundException e) {
								e.printStackTrace();
								System.out.println("EXCEPTION @@@@@ "+e.getMessage());
							}
					}
				}
			}
		}
		return listObject;
	}
}
