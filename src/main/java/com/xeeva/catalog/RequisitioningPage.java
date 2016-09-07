package com.xeeva.catalog;

import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Element;
import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.Listbox;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.Webtable;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.PageLoaded;
import com.orasi.utils.Sleeper;
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
	@FindBy(linkText = "Requisitioning") private Link ReqTab;
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
	
	//Recent Orders,Rejected Orders tabs
	@FindBy(id="aTab1") private Label lblRecentOrders;
	@FindBy(id="aTab5") private Label lblRejectedOrders;

	//Link Requisitioning
	@FindBy(linkText=".selected") private Link lnkRequisitioning;
	
	//RecentOrders table.
	@FindBy(className="Datagridborder mainRecentOdersGrid") private Webtable tblRecentOrdersGrid;
	
	/**Constructor**/
	public RequisitioningPage(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}

	public void pageLoaded(){
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
	
	public void click_ReqTab(){
	    ReqTab.syncVisible(10, false);
	    ReqTab.click();
	    Sleeper.sleep(2000);
	}
	
	// This method clicks on Recent Orders tab. - Author[Praveen]
	public void clickRecentOrdersTab(){
		lblRecentOrders.syncVisible(5, false);
		lblRecentOrders.click();
	}

	// This method clicks on Requisition cart link # which has only REQ Number. - Author[Praveen]
	public void clickRequisitionCartLink(){
		clickRecentOrdersTab();
		tblRecentOrdersGrid.syncVisible();
		List<WebElement> getRows = driver.findElements(By.xpath("//*[@class='Datagridborder mainRecentOdersGrid']/tbody/tr"));
		int rowsCount = getRows.size();
		System.out.println("Total rows in RecentOrders Grid table: "+ rowsCount);
		
		for(int row=1; row<=rowsCount; row++){
			String getRFQValue = driver.findElement(By.xpath("//*[@id='gvRecentOdersGrid']/tbody/tr["+ row +"]/td[4]/span")).getText();
			System.out.println("RFQ Value is: " + getRFQValue);
			
			if(getRFQValue.isEmpty()){
				System.out.println("Clicked on the cart link which has no RFQ value.");
				driver.findElement(By.xpath("//*[@id='gvRecentOdersGrid']/tbody/tr["+ row +"]/td[1]")).click();
				break;
			}
		}
		
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}
}
