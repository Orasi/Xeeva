package com.xeeva.catalog;

import java.util.ResourceBundle;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.Listbox;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.TestReporter;


/**
 * @summary This page contains requisitioning page objects
 * @author  Lalitha Banda
 *
 */
public class RequisitioningPage {

	private OrasiDriver driver = null;
	private ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);

	/**Page Elements**/
	@FindBy(id = "txtBasicSearchCriteria")	private Textbox catalogSearch;
	@FindBy(xpath = ".//*[@id='searchBoxArea']/table/tbody/tr/td[3]/div[2]/a")	private Link smartFormRequest;
	@FindBy(id = "btnSubmit")	private Button btnSubmit;


	// Requisition Type
	@FindBy(linkText = "Catalog Spot Buy Request")	private Link catalogSpotBuyRequest;
	@FindBy(linkText = "Service Request - General")	private Link serviceRequestGeneral;

	// Material Request	
	@FindBy(xpath = ".//*[@id='txtItem']")	private Textbox itemDescription;
	@FindBy(id = "ddlMainCategory")	private Listbox categoryType;
	@FindBy(id = "ddlCategory")	private Listbox category;
	@FindBy(id = "ddlSubCategory")	private Listbox subCategory;
	@FindBy(id = "txtQuantity")	private Textbox txtQuantity;
	@FindBy(id = "ddlUOM")	private Listbox unitOfMeasure;
	@FindBy(id = "txtUnitPrice")private Textbox txtUnitPrice;

	// Cart Item  
	@FindBy(xpath = ".//*[@id='tblMultiline']/tbody/tr[4]/td[1]/span")	private WebElement cartItem;
	
	/**Constructor**/

	public RequisitioningPage(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}

	private void pageLoaded(){
		catalogSearch.syncVisible(10, false);
	}

	/**Page Interactions**/

	// Method to click on CreateSmartForm Link 
	private void click_SmartFormRequest(){
		pageLoaded();
		smartFormRequest.click();
	}

	// Method to click on submit 
	private void click_Submit(){
		pageLoaded();
		btnSubmit.click();

	}

	// Method to Select Requisition Type
	private void selectRequisitionType(String RequisitionType){
		switch(RequisitionType){

		case "catalogSpotBuyRequest" :
			catalogSpotBuyRequest.click();
			break;

		case "serviceRequestGeneral" :
			serviceRequestGeneral.click();
			break;

		default : catalogSpotBuyRequest.click();

		}
	}

	// Method For Material Request Form
	private void createMaterialRequest(String ID,String CategoryType ,String Category,String SubCategory,String Quantity,String UOM,String Price){
		itemDescription.sendKeys(ID);
		categoryType.select(CategoryType);
		category.select(Category);
		subCategory.select(SubCategory);
		txtQuantity.safeSet(Quantity);
		unitOfMeasure.select(UOM);
		txtUnitPrice.safeSet(Price);
	}

	
	// This Method Generates Smart form Request
	public void createSmartFormRequest(String RequisitionType,String ItemDescription,String CategoryType,String Category,String SubCategory,String Quantity,String UnitofMeasure,String UnitPrice){
		click_SmartFormRequest();
		selectRequisitionType(RequisitionType);
		createMaterialRequest(ItemDescription,CategoryType,Category,SubCategory,Quantity,UnitofMeasure,UnitPrice );
		click_Submit();
	}
	
	// This Method Verifies Smart Form Item
	public void Verify_SmartFormItem(String ItemDescription){
		TestReporter.logStep("Expected : "+"["+cartItem.getText().trim()+"]"+"Actual : "+"["+ItemDescription.trim() +"]");
		TestReporter.assertTrue(cartItem.getText().trim().contains(ItemDescription.trim()), "Smart Form Item is Verified !!");
	}
	
}
