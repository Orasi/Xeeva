package com.xeeva.catalog;

import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.omg.CORBA.LocalObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.Listbox;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.Webtable;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.Sleeper;
import com.orasi.utils.TestReporter;
import com.xeeva.catalog.SearchItems.GlobalItemsTab;
import com.xeeva.catalog.SearchItems.LocalItemsTab;


/**
 * @summary This page contains requisitioning page objects
 * @author  Lalitha Banda
 *
 */
public class RequisitioningPage {

	private OrasiDriver driver = null;
	private ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);

	//**Page Elements**//*
	@FindBy(linkText = "Requisitioning")	private Link ReqTab;
	@FindBy(id = "txtBasicSearchCriteria")	private Textbox catalogSearch;
	@FindBy(id = "lnkCatalog")	private Link lnkCatalog;
	@FindBy(id = "aSearchButton")	private Link searchButton;
	@FindBy(xpath = ".//*[@id='searchBoxArea']/table/tbody/tr/td[3]/div[2]/a")	private Link smartFormRequest;
	@FindBy(id = "btnSubmit")	private Button btnSubmit;
	@FindBy(css=".fa.fa-star.fa-2x.cursor-pointer") private Link lnkShowFavItems;


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
	@FindBy(id = "txtUNSPSCCODE")private Textbox txtUNSPS;
	@FindBy(id = "txtSuggestedSupplier")private Textbox txtSS;
	@FindBy(id = "txtManufacturer")private Textbox txtMN;
	@FindBy(id = "txtManufacturerPart")private Textbox txtMPN;
	//txtManufacturer

	// Cart Item  
	@FindBy(xpath = ".//*[@id='tblMultiline']/tbody/tr[4]/td[1]/span")	private WebElement cartItem;
	@FindBy(xpath = ".//*[@id='gvLocalSearch']/tbody/tr/td[2]/span/span")	private WebElement searchItem_local;
	@FindBy(xpath = ".//*[@id='gvGlobalSearch']/tbody/tr/td[2]/span/span")	private WebElement searchItem_global;
	@FindBy(xpath = ".//*[@id='gvBPOSearch']/tbody/tr/td[2]/span/span")	private WebElement searchItem_bpo;

	// search tabs
	@FindBy(id = "aTab2")	private Link globalItemsTab;
	@FindBy(id = "aTab3")	private Link bpoItemsTab;

	//RecentOrders table.
	@FindBy(className="Datagridborder mainRecentOdersGrid") private Webtable tblRecentOrdersGrid;

	//Recent Orders,Rejected Orders tabs
	@FindBy(id="aTab1") private Label lblRecentOrders;
	@FindBy(id="aTab5") private Label lblRejectedOrders;

	//SelectUOMValue,AddToCartItemsGrid
	@FindBy(xpath="//select[@class='textFieldList width90Px']") private Listbox lstSelectUOMValue;
	@FindBy(xpath="//td[@class='vAlignMiddle textAlignLeft']/a/div") private List<WebElement> lstAddToCartItemsGrid;
	@FindBy(xpath="//table[@id='gvLocalSearch']/tbody/tr/td[2]/span") private List<WebElement> localItemsGrid;
	@FindBy(xpath="//table[@id='gvGlobalSearch']/tbody/tr/td[2]/span") private List<WebElement> globalItemsGrid;
	@FindBy(xpath="//a[@id='aTab1']/span[2]") private Label lblLocalItems;
	@FindBy(xpath="//a[@id='aTab2']/span") private Label lblGlobalItems;
	
	//**Constructor**//*

	public RequisitioningPage(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}

	private void pageLoaded(){
		catalogSearch.syncVisible(20, false);
	}

	//**Page Interactions**//*

	public void click_ReqTab(){
		ReqTab.syncVisible(10, false);
		ReqTab.click();
		Sleeper.sleep(3000);
	}

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
	private void createMaterialRequest(String ID,String UNSPS,String SS,String CategoryType ,String Category,String SubCategory,
			String MN,String MPN,String Quantity,String UOM,String Price){
		itemDescription.sendKeys(ID);
		txtUNSPS.safeSet(UNSPS);
		txtSS.safeSet(SS);
		categoryType.select(CategoryType);
		category.select(Category);
		subCategory.select(SubCategory);
		txtMN.safeSet(MN);
		txtMN.click();
		txtMPN.safeSet(MPN);
		txtQuantity.safeSet(Quantity);
		unitOfMeasure.select(UOM);
		txtUnitPrice.safeSet(Price);
	}


	// This Method Generates Smart form Request
	public void createSmartFormRequest(String RequisitionType,String ItemDescription,String UNSPS,String SS,
			String CategoryType,String Category,String SubCategory,String MN,String MPN,String Quantity,String UnitofMeasure,String UnitPrice){
		click_ReqTab();
		click_SmartFormRequest();
		selectRequisitionType(RequisitionType);
		createMaterialRequest(ItemDescription,UNSPS,SS,CategoryType,Category,SubCategory,MN,MPN,Quantity,UnitofMeasure,UnitPrice );
		click_Submit();

	}

	// This Method Verifies Smart Form Item
	public void Verify_SmartFormItem(String ItemDescription){
		TestReporter.logStep("Expected : "+"["+cartItem.getText().trim()+"]"+"Actual : "+"["+ItemDescription.trim() +"]");
		TestReporter.assertTrue(cartItem.getText().trim().contains(ItemDescription.trim()), "Smart Form Item is Verified !!");
	}


	// This method to performs catalog search 
	public void  perform_CatalogSearch(String searchItem){
		catalogSearch.clear();
		catalogSearch.safeSet(searchItem);
		searchButton.click();
		Sleeper.sleep(5000);

	}

	// Clicks on Global Item Tab  from Search Result 
	public String  get_SearchResultItem(WebElement inputElement){
		return inputElement.getText();
	}


	// Method to Verify catalog Search Functionality
	public void verify_SearchItems(String ItemType,String ItemNumber){
		switch(ItemType){

		case "local":
			perform_CatalogSearch(ItemNumber);
			System.out.println("Local : "+ItemNumber+":"+get_SearchResultItem(searchItem_local));
			TestReporter.assertTrue(get_SearchResultItem(searchItem_local).trim().equalsIgnoreCase(ItemNumber), "Local Search Item verified !!");
			break;

		case "global": 
			perform_CatalogSearch(ItemNumber);
			globalItemsTab.syncVisible(5, false);
			globalItemsTab.click();
			System.out.println( "Global : " + ItemNumber+":"+get_SearchResultItem(searchItem_global));
			TestReporter.assertTrue(get_SearchResultItem(searchItem_global).trim().equalsIgnoreCase(ItemNumber), "Global Search Item verified !!");
			break;

		case "bpo":
			perform_CatalogSearch(ItemNumber);
			bpoItemsTab.syncVisible(5, false);
			bpoItemsTab.click();
			System.out.println( "BPO : " +ItemNumber+":"+get_SearchResultItem(searchItem_bpo));
			TestReporter.assertTrue(get_SearchResultItem(searchItem_bpo).trim().equalsIgnoreCase(ItemNumber), "BPO Search Item verified !!");
			break;

		default : System.out.println();

		}
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

	/**
	 * @summary: Method to click on Show Favourite items.
	 * @author: Praveen Namburi, @version: Created 09-09-2016
	 */
	public void clickShowFavouriteItems(){
		lnkShowFavItems.syncVisible(15, false);
		lnkShowFavItems.click();
		Sleeper.sleep(4000);
	}

	/**
	 * @summary: Method to add price agreement from favourite folder.
	 * @author: Praveen Namburi,@version: Created 09-09-2016
	 * @param strUOMValue
	 */
	public void addPriceAgreementItemsFromFavFolder(String strUOMValue){
		String getLocalItemsCount = lblLocalItems.getText();
		TestReporter.log("Local-Items Count is: "+ getLocalItemsCount);
		
		String getGlobalItemsCount = lblGlobalItems.getText();
		TestReporter.log("Global-Items Count is: "+ getGlobalItemsCount);
		
		if(!getLocalItemsCount.contains("0")){
			TestReporter.log("Click on Local-Items tab.");
			LocalItemsTab localItem = new LocalItemsTab(driver);
			localItem.click_localItemsTab();
			String itemNumber = null;
			List<WebElement> localItems = localItemsGrid;
			
			for(WebElement inputItem : localItems){
				  itemNumber = inputItem.getText();
				  System.out.println("Item number is: "+itemNumber);
				  lstSelectUOMValue.select(strUOMValue);
				  List<WebElement> localAddToCartItemLinks = lstAddToCartItemsGrid;
					if(localAddToCartItemLinks.size()>0){
						for(WebElement linkAddToCartItem :localAddToCartItemLinks){
							TestReporter.log("Click on 'Add-To-Cart' link.");
							linkAddToCartItem.click();
							break;
						}
					}
				  break;
			}
		}else if(getLocalItemsCount.contains("0")){
			TestReporter.log(" 'No Records Found !!' in Local Items tab. "
					+ "Searching for Global items.");
			
		}else if(!getGlobalItemsCount.contains("0")){
			TestReporter.log("Click on Global-Items tab.");
			GlobalItemsTab globalItem = new GlobalItemsTab(driver);
			globalItem.click_GlobalItemsTab();
			
			String itemNumber = null;
			List<WebElement> globalItems = globalItemsGrid;
			for(WebElement inputItem : globalItems){
				  itemNumber = inputItem.getText();
				  System.out.println("Item number is: "+itemNumber);
				  lstSelectUOMValue.select(strUOMValue);
				  List<WebElement> globalAddToCartItemLinks = lstAddToCartItemsGrid;
					if(globalAddToCartItemLinks.size()>0){
						for(WebElement linkAddToCartItem :globalAddToCartItemLinks){
							TestReporter.log("Click on 'Add-To-Cart' link.");
							linkAddToCartItem.click();
							break;
						}
					}
				  break;
			}
			
		}else{
			TestReporter.log(" 'No Records Found !!' in Global Items tab.");
		}
		
		/*String localvalue = getLocalItemsCount.replaceAll("()", "");
		System.out.println(localvalue);*/

		/*driver.findElement(By.id("aTab1")).click();
		
		List<WebElement> localItems= driver.findElements(By.xpath(".//*[@id='gvLocalSearch']/tbody/tr/td/span"));
		if(localItems.size()>0){
			for(WebElement inputItem :localItems){
				ItemNumber = inputItem.getText();
				break;
			}
		}else{
			TestReporter.logStep("No Records Found !!");
		}
		*/
			
		
		//System.out.println(""+getLocalItemsNum);
		
	}
}

