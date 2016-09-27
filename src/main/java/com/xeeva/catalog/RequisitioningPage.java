package com.xeeva.catalog;

import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.Listbox;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.Webtable;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.AlertHandler;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.PageLoaded;
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

	PageLoaded pageLoad = new PageLoaded();
	private OrasiDriver driver = null;
	private ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);

	//**Page Elements**//*
	@FindBy(linkText = "Requisitioning")	private Link ReqTab;
	@FindBy(id = "txtBasicSearchCriteria")	private Textbox catalogSearch;
	@FindBy(id = "lnkCatalog")	private Link lnkCatalog;
	@FindBy(id = "aSearchButton")	private Link searchButton;
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
	@FindBy(xpath="//tr/td/table/tbody/tr/td/table/tbody/tr/td[4]/a") 
	private List<WebElement> ItemLinks;

	@FindBy(css=".fa.fa-star.fa-2x.cursor-pointer") private Link lnkShowFavItems;
	//SelectUOMValue,AddToCartItemsGrid
	@FindBy(xpath="//select[@class='textFieldList width90Px']") private Listbox lstSelectUOMValue;
	@FindBy(xpath="//td[@class='vAlignMiddle textAlignLeft']/a/div") private List<WebElement> lstAddToCartItemsGrid;
	@FindBy(xpath="//table[@id='gvLocalSearch']/tbody/tr/td[2]/span") private List<WebElement> localItemsGrid;
	@FindBy(xpath="//table[@id='gvGlobalSearch']/tbody/tr/td[2]/span") private List<WebElement> globalItemsGrid;
	@FindBy(xpath="//a[@id='aTab1']/span[2]") private Label lblLocalItems;
	@FindBy(xpath="//a[@id='aTab2']/span") private Label lblGlobalItems;
	@FindBy(xpath="//*[@class='Datagridborder mainRecentOdersGrid']/tbody/tr") private List<WebElement> mainRecentOdersGrid;
	//@FindBy(xpath="//select[@class='textFieldList width90Px']") private Listbox lstSelUOMValue;
	@FindBy(xpath="//div[@id='divAppInfoMsg'][@class='addMessage']") private Label lblCartItemAddedMessage;
	@FindBy(xpath="//td/textarea[@id='txtComments']") private Textbox txtComments;
	@FindBy(xpath="//input[@id='btnCommentsSubmit']") private Button btnCommentsSubmit;
	@FindBy(xpath="//input[@value='SEE MORE'][@class='buttonClass marginRight0px']") private Button btnSeeMore;
	@FindBy(xpath="//*[@class='Datagridborder mainRecentOdersGrid']/tbody/tr") private List<WebElement> tblMainRecentOrdersGrid;
	
	// Search criteria details
	@FindBy(xpath="//input[@id='txtCart']") private Textbox txtCart;
	@FindBy(xpath="//input[@id='txtRfq']") private Textbox txtRFQ;
	@FindBy(id="txtOrderDescription") private Textbox txtOrderDesc;
	@FindBy(id="txtReq") private Textbox txtREQ;
	@FindBy(id="ddl_OrderStatus") private Listbox lstLocation;
	@FindBy(id="btnROSearchSubmit") private Button btnSearch;
	
	@FindBy(xpath="//div/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr[2]/td[5]") 
	private WebElement tblRecentOrder_CartItem;
	@FindBy(xpath="//div/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr[2]/td[4]")  
	private WebElement tblRecentOrder_RFQNum;
	@FindBy(xpath="//div/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr[2]/td[3]")  
	private WebElement tblRecentOrder_REQNum;
	@FindBy(id="divRejectedOrderResult") private WebElement eleRejectedOrderResult;
	@FindBy(xpath="//div/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr") 
	private List<WebElement> tblRejectedOrdersResult;
	
	@FindBy(xpath="//div/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr") 
	private List<WebElement> lblRFQStatus;
	//xpath="//div/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td[14]/span"
	//**Constructor**//*

	public RequisitioningPage(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}

	private void pageLoaded(){
		catalogSearch.syncVisible(20, false);
	}

	//**Page Interactions**//*

	/**
	 * @summary  Method to clcik on REQTab
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/
	public void click_ReqTab(){
		ReqTab.syncVisible(20, false);
		ReqTab.click();
	}


	/**
	 * @summary  Method to click on CreateSmartForm Link 
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/
	private void click_SmartFormRequest(){
		pageLoaded();
		smartFormRequest.click();
	}


	/**
	 * @summary  Method to click on submit  
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/
	private void click_Submit(){
		pageLoaded();
		btnSubmit.click();
		driver.setPageTimeout(3,TimeUnit.SECONDS);

	}

	/**
	 * @summary  Method to Select Requisition Type
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/
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

	/**
	 * @summary  Method For Material Request Form
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/
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



	/**
	 * @summary   This Method Generates Smart form Request
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/
	public void createSmartFormRequest(String RequisitionType,String ItemDescription,String UNSPS,String SS,
			String CategoryType,String Category,String SubCategory,String MN,String MPN,String Quantity,String UnitofMeasure,String UnitPrice){
		click_ReqTab();
		click_SmartFormRequest();
		selectRequisitionType(RequisitionType);
		createMaterialRequest(ItemDescription,UNSPS,SS,CategoryType,Category,SubCategory,MN,MPN,Quantity,UnitofMeasure,UnitPrice );
		click_Submit();
		Verify_SmartFormItem();

	}


	/**
	 * @summary   This Method Verifies Smart Form Item
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/
	public void Verify_SmartFormItem(){
		lblCartItemAddedMessage.syncVisible(5, false);
		String getCartItemAddedMessage = lblCartItemAddedMessage.getText();
		TestReporter.assertTrue(getCartItemAddedMessage.equalsIgnoreCase("The item has been added successfully!"), "Item added to the cart.");
	}


	/**
	 * @summary  This method to performs catalog search 
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/
	// This method to performs catalog search 
	//Added the step - searchButton.syncVisible(20,false) by Praveen - 14-09-2016.
	public void  perform_CatalogSearch(String searchItem){
		catalogSearch.clear();
		catalogSearch.safeSet(searchItem);
		searchButton.syncVisible(20,false);
		searchButton.click();
		driver.setPageTimeout(4);

	}


	/**
	 * @summary Clicks on Global Item Tab  from Search Result 
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/
	public String  get_SearchResultItem(WebElement inputElement){
		return inputElement.getText();
	}


	/**
	 * @summary Method to Verify catalog Search Functionality
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/
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

	/**
	 *@summary: Method to click on Recent Orders tab.
	 *@author Praveen Namburi, @Version: Created 14-09-2016
	 */
	public void clickRecentOrdersTab(){
		lblRecentOrders.syncVisible(5, false);
		lblRecentOrders.click();
	}

	/**
	 *@summary: Method to click on Rejected Orders tab.
	 *@author Praveen Namburi, @Version: Created 23-09-2016
	 */
	public void clickRejectedOrdersTab(){
		pageLoad.isDomComplete(driver);
		lblRejectedOrders.syncVisible(20, false);
		driver.executeJavaScript("arguments[0].click();", lblRejectedOrders);
		//lblRejectedOrders.click();
	}
	
	/**
	 * @summary: Method to click on Requisition cart link # which has only REQ Number.
	 * @Author: Praveen Namburi,@version: Created 07-09-2016
	 * @param ItemTpye
	 * @return getREQValue
	 */
	public String  clickRequisitionCartLink(String ItemTpye){
		String returnValue = null;
		clickRecentOrdersTab();
		tblRecentOrdersGrid.syncVisible();
		List<WebElement> getRows = driver.findElements(By.xpath("//*[@class='Datagridborder mainRecentOdersGrid']/tbody/tr"));
		int rowsCount = getRows.size();
		System.out.println("Total rows in RecentOrders Grid table: "+ rowsCount);

		for(int row=1; row<=rowsCount; row++){
			String getRFQValue = driver.findElement(By.xpath("//*[@id='gvRecentOdersGrid']/tbody/"
					+ "tr["+ row +"]/td[4]/span")).getText();

			if(getRFQValue.isEmpty()&& ItemTpye.equalsIgnoreCase("PAI")){
				String getREQValue = driver.findElement(By.xpath("//*[@id='gvRecentOdersGrid']/tbody"
						+ "/tr["+ row +"]/td[3]/span")).getText();

				System.out.println("Clicked on the cart link which has no RFQ value.");
				driver.findElement(By.xpath("//*[@id='gvRecentOdersGrid']/tbody/"
						+ "tr["+ row +"]/td[1]")).click();
				returnValue = getREQValue;
				break;

			}else if(getRFQValue!=null && !getRFQValue.isEmpty() && ItemTpye.equalsIgnoreCase("NPAI")){
				driver.findElement(By.xpath("//*[@id='gvRecentOdersGrid']/tbody/"
						+ "tr["+ row +"]/td[1]")).click();
				break;
			}else{
				TestReporter.assertTrue(false, "Selected item having no REQ orRFQ values !!");
			}
		}

		return returnValue;
	}

	/**
	 * @summary: Method to click on Show Favourite items.
	 * @author: Praveen Namburi, @version: Created 09-09-2016
	 */
	public void clickShowFavouriteItems(){
		lnkShowFavItems.syncVisible(15, false);
		lnkShowFavItems.click();
		
	}

	/**
	 * @summary: Method to add price agreement from favourite folder.
	 * @author: Praveen Namburi,@version: Created 09-09-2016
	 * @param strUOMValue
	 */
	public void addPriceAgreementItemsFromFavFolder(String strUOMValue){
		pageLoaded();
		String getLocalItemsCount = lblLocalItems.getText();
		TestReporter.log("Local-Items Count is: "+ getLocalItemsCount);

		String getGlobalItemsCount = lblGlobalItems.getText();
		TestReporter.log("Global-Items Count is: "+ getGlobalItemsCount);

		if(!getLocalItemsCount.contains("0")){
			TestReporter.log("Click on Local-Items tab.");
			LocalItemsTab localItem = new LocalItemsTab(driver);
			//localItem.click_localItemsTab();
			
			String itemNumber = null;
			List<WebElement> localItems = localItemsGrid;
			for(WebElement inputItem : localItems){
				itemNumber = inputItem.getText();
				TestReporter.log("Item number is: "+itemNumber);
				//Select the UOM option from the listbox.
				List<WebElement> listboxUOMs = driver.findElements(By.xpath("//select[@class='textFieldList width90Px']"));
				Select select = new Select(listboxUOMs.get(0));
				select.selectByVisibleText(strUOMValue);
				//new Select(listboxUOMs.get(0)).selectByVisibleText(strUOMValue);
				//Click on Add-to-cart link.
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
				TestReporter.log("Item number is: "+itemNumber);
				//Select the UOM option from the listbox.
				List<WebElement> listboxUOMs = driver.findElements(By.xpath("//select[@class='textFieldList']"));
				new Select(listboxUOMs.get(0)).selectByVisibleText(strUOMValue);
				//Click on Add-to-cart link.
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
		}else if(getGlobalItemsCount.contains("0")){
			//If there are no records in both local & global items, then test should fail.
			TestReporter.assertFalse(getGlobalItemsCount.contains("0"), " 'No Records Found !!' from Local & Global Items tab.");
			
		}

	}


	/**
	 * @summary Method to click on FavoriteItem Link 
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/
	public void click_FavoriteItem(){
		pageLoaded();
		lnkShowFavItems.syncVisible(5, false);
		TestReporter.assertTrue(lnkShowFavItems.isDisplayed(), "Fav Icon element is Displaying!!");
		lnkShowFavItems.click();
		driver.setPageTimeout(3);
	}
	
	
	  
	  /**
	   * @summary: Method to get the Item Number from catalog table.
	   * @author praveen namburi,@version: Created 13-09-2016
	   * @return getItemNumber
	   */
	  public String  getItemNumberFromCatalog(){
		  driver.setPageTimeout(2);
		  String getItemNumber = null;
		  String getLocalItemsCount = lblLocalItems.getText();
		  TestReporter.log("Local-Items Count is: "+ getLocalItemsCount);
		  
		  if(!getLocalItemsCount.contains("0")){
				TestReporter.log("Click on Local-Items tab.");
				LocalItemsTab localItem = new LocalItemsTab(driver);
				localItem.click_localItemsTab();
				List<WebElement> localItems = localItemsGrid;
				for(WebElement inputItem : localItems){
					String itemNumber = inputItem.getText().trim();
					getItemNumber = itemNumber;
					break;
				}
		  }else if(getLocalItemsCount.contains("0")){
				TestReporter.assertTrue(false," 'No Records Found !!' in Local Items tab. ");
		  }
		return getItemNumber;
	  
	  }	
	  
	  /**
	   * @summary: Method to cancel the requisition record and verify them.
	   * @author praveen namburi, @Version: Created 23-09-2016
	   * @param comments
	   */
	  public void cancelRequisitionFromRecentOrders(String comments){
		  tblRecentOrdersGrid.syncVisible();
		  btnSeeMore.syncEnabled(10);
		  driver.executeJavaScript("arguments[0].click();", btnSeeMore);
		  driver.setElementTimeout(Constants.ELEMENT_TIMEOUT);
		  List<WebElement> getRows = tblMainRecentOrdersGrid;
		  int rowsCount = getRows.size();
		  TestReporter.log("Total rows in RecentOrders Grid table: "+ rowsCount);
			  
		    for(int row=1; row<=rowsCount; row++){
				String getStatus = driver.findElement(By.xpath("//*[@id='gvRecentOdersGrid']/tbody/"
						+ "tr["+ row +"]/td[8]/span")).getText();
				if(!getStatus.contains("Canceled By Requester")){
					driver.setPageTimeout(2);
					driver.findElement(By.xpath("//*[@id='gvRecentOdersGrid']/tbody"
							+ "/tr["+ row +"]/td[13]/div/a/i")).jsClick();
					//Handle Alert if present
					if(AlertHandler.isAlertPresent(driver, 6)){
						AlertHandler.handleAlert(driver, 6);
					}
					txtComments.syncVisible(5);
					txtComments.safeSet(comments);
					btnCommentsSubmit.syncVisible(5);
					btnCommentsSubmit.click();
					Sleeper.sleep(5000);
					String getStatusAfterCancelReqLink = driver.findElement(By.xpath("//*[@id='gvRecentOdersGrid']/tbody/"
							+ "tr["+ row +"]/td[8]/span")).getText();
					TestReporter.logStep("Get Status After Cancel Requisition: "+getStatusAfterCancelReqLink);
					TestReporter.assertTrue(getStatusAfterCancelReqLink.contains("Canceled By Requester"), 
							"Cancelled the requisition record sucessfully.");
					break;
		       }
		    }
	  }
	 
	  /**
	   * @Summary: Method to get the Cart number from Rejected Orders.
	   * @author: Praveen Namburi, @version: Created 26-09-2016
	   * @return getCartValue
	   */
	  public String getCart_RejectedOrders(){
		  String getCartValue="";
		  getCartValue = tblRecentOrder_CartItem.getText().trim();
		  TestReporter.log("Cart Number: "+getCartValue);
		  return getCartValue;
	  }
	  
	  /**
	   * @Summary: Method to get the RFQ number from Rejected Orders tab.
	   * @author: Praveen Namburi, @version: Created 26-09-2016
	   * @return getRFQValue
	   */
	  public String getRFQ_RejectedOrders(){
		  String getRFQValue="";
		  getRFQValue = tblRecentOrder_RFQNum.getText().trim();
		  TestReporter.log("RFQ Number: "+getRFQValue);
		  return getRFQValue;
	  }
	  
	  /**
	   * @Summary: Method to get the REQ number from Rejected Orders.
	   * @author: Praveen Namburi, @version: Created 26-09-2016
	   * @return
	   */
	  public String getREQ_RejectedOrders(){
		  String getREQValue="";
		  String REQValue = tblRecentOrder_REQNum.getText().trim();
		  TestReporter.log("REQ Number: "+ REQValue);
		  if(REQValue.contains("-")){
			  String strREQValue = REQValue.replace("-", "");
			  getREQValue = strREQValue;
		  }
		  return getREQValue;
	  }
	 
	  /**
	   * @summary: Method to enter search Criteria for Rejected Orders and Verify them.
	   * @author: Praveen Namburi, @version: Created 26-09-2016
	   * @param cart, @param RFQ, @param OrderDesc, @param location
	   */
	  public void enterSearchCriteriaAndVerifyRejectedOrders(String location){
		  	driver.manage().timeouts().implicitlyWait(7, TimeUnit.SECONDS);
		    pageLoaded();
		    Sleeper.sleep(4000);
		  	String cartValue = getCart_RejectedOrders();
		  	String RFQValue = getRFQ_RejectedOrders();
		  	String REQValue = getREQ_RejectedOrders();
		  	
		  	pageLoad.isDomComplete(driver);
		  	txtCart.syncVisible(10);
		  	txtCart.click();
		  	txtCart.safeSet(cartValue);
		  	txtRFQ.syncVisible(5);
		  	txtRFQ.click();
		  	txtRFQ.safeSet(RFQValue);
		  	txtREQ.syncVisible(5);
		  	txtREQ.click();
		  	txtREQ.safeSet(REQValue);
		  	lstLocation.syncEnabled(5);
		  	lstLocation.select(location);
		  	btnSearch.syncVisible(5);
		  	driver.executeJavaScript("arguments[0].click();", btnSearch);
		  	driver.manage().timeouts().implicitlyWait(6, TimeUnit.SECONDS);
		  	//Verifying the Filtered Rejected Oder details.
		  	List<WebElement> rejectedOrdersResult = tblRejectedOrdersResult;
		  	int getRowsCount = rejectedOrdersResult.size();
		  	if(getRowsCount>0){
		  		TestReporter.assertEquals(getCart_RejectedOrders(), cartValue, 
			  			"Found the Rejected order details with Cart no. - ["+ cartValue +"]");
		  		TestReporter.assertEquals(getRFQ_RejectedOrders(), RFQValue, 
			  			"Found the Rejected order details with Cart no. - ["+ RFQValue +"]");
			  	TestReporter.assertEquals(getREQ_RejectedOrders(), REQValue,
			  			"Found the Rejected order details with Cart no. - ["+ REQValue +"]");
		  	}else{
		  		TestReporter.assertTrue(false, "No Records found!!!");
		  	}
		  	
	  }
	  
	 /**
	  * @summary: Method to search for RFQ Cancelled item from Rejected Orders tab.
	  * @author: Praveen Namburi, @version: Created 26-09-2016
	  */
	  public void copyRFQCancelledItem(){
		    int evenNum = 0;
            List<WebElement> RFQStatusList = lblRFQStatus;
  		    int getRFQStatusCount = RFQStatusList.size();
  		    TestReporter.log("Total Rows in Rejected Orders table: " + getRFQStatusCount);
  		    //Iterate even number rows
  		    for(int rows=2; rows<=getRFQStatusCount-1;rows++){
  		    	//To get even numbers rows
  		    	if(rows % 2 == 0){
  		    		evenNum = rows;
  		    		String getRFQStatus = driver.findElement(By.xpath("//table[@class='RecentOrderDetailsGrid ']"
  		    				+ "/tbody/tr[" + evenNum + "]/td[14]/span")).getText();
  	  		        //Get the RFQ cancelled status from Rejected Orders grid.
  		    		if(getRFQStatus.trim().contains("Cancelled")){
  		    			//Click on Copy-item link.
  	  		        	driver.findElement(By.xpath("//table[@class='RecentOrderDetailsGrid ']/tbody/"
  	  		        			+ "tr[" + evenNum + "]/td[17]/a[1]/i")).jsClick();
  	  		        	break;
  	  		        }
  	  		     if(getRFQStatus.trim().contains("Cancelled")) break;  
  		    	}
  		    }
  		   driver.manage().timeouts().implicitlyWait(Constants.PAGE_TIMEOUT, TimeUnit.SECONDS);
	  }
	  
	  
}
