package com.xeeva.catalog;

import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Element;
import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.Listbox;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.AlertHandler;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.PageLoaded;
import com.orasi.utils.Sleeper;
import com.orasi.utils.TestReporter;
import com.orasi.utils.date.DateTimeConversion;
import com.xeeva.navigation.MainNav;

/**
 * @summary This page contains Cost Center page objects
 * @author  Lalitha Banda
 * @date    15/09/16
 */

public class CostCenterPage {

	PageLoaded pl = new PageLoaded();
	private OrasiDriver driver = null;
	private ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);
	String xpath = ".//*[@id='customfa']/tbody/tr/td/select";


	//**Page Elements**//*
	@FindBy(id ="countrydivcontainer") private Element costCenterContainer;	
	@FindBy(xpath="//*[@id='customfa']/tbody") private WebElement tblCostCenterInfo;
	@FindBy(id ="ddlApplyCC") private Listbox lstCostCenterHeaderLevel;
	@FindBy(className ="fa fa-calendar cursor-pointer font16px") private Button btnCalender;
	@FindBy(id ="txtRequiredby") private Textbox txtRequiredBy;
	@FindBy(xpath="//*[@title='Internal Comment']") private List<WebElement> lstInternalCommentGrid;
	@FindBy(id ="txtInternalComment") private Textbox txtInternalComment;
	@FindBy(id ="btnSaveComments") private Button btnSaveComments;
	@FindBy(id ="txtShowFullComment") private Textbox txtShowFullComment;
	@FindBy(xpath="//*[@id='txtShowFullComment']") private List<WebElement> txtCommentDisplayed;
	@FindBy(id ="fancybox-close") private Button btnClosePopUp;
	@FindBy(xpath="//*[@title='Copy Item']") private List<WebElement> lstCopyItemGrid;
	@FindBy(xpath=".//*[@class='gridtextLink']") private List<WebElement> lstItemNoGrid;
	@FindBy(css=".costCentername") private Label lblCC;
	@FindBy(xpath=".//*[@id='customfa']/tbody/tr/td/input[@class='QtyNumericTextBoxClass']") private List<WebElement> lstQty;
	@FindBy(xpath=".//*[@title='Save changes']") private List<WebElement> lstSave;
	@FindBy(xpath="//div[@id='divAppInfoMsg'][@class='addMessage']") private Label conformationMsg;
	@FindBy(xpath=".//*[@value='Save Cart'][contains(@onclick, 'javascript')]") private WebElement btnSaveCart;

	@FindBy(xpath="//td[2]/a[1]/input") private Button btnSaveCartCC;
	@FindBy(xpath="//tr[2]/td[2]/a[2]/input") private Button btnShopMoreItemsCC;
	@FindBy(xpath="//*[@id='btnCheckOut']/input") private Button btnContinueCheckoutCC;
	@FindBy(xpath=".//*[@id='customfa']/tbody/tr/td/select") private List<Listbox> ddCCLineLevel;
	@FindBy(xpath="//*[@id='tblbasicTable']/tbody/tr/td/div/a") private List<WebElement> lstSelectCC;
	@FindBy(xpath="//*[@id='tblbasicTable']/tbody/tr/td/div/span") private List<WebElement> lstSelectCCValue;



	@FindBy(xpath="//table[@id='customfa']/tbody/tr/td/table/tbody/tr/td/input") private List<WebElement> dateCCInputLineLevel;
	@FindBy(xpath="//table[@id='customfa']/tbody/tr/td/table/tbody/tr/td/i") private List<WebElement> dateCCLineLevel;
	@FindBy(xpath="//div[@id='divAPRPARList']/ul/li[4]/i") private Link lnkDateCC_HeaderLvel;

	@FindBy(xpath="//div[@id='divAppInfoMsg'][@class='addMessage']") private Label lblCartItemAddedMessage;
	@FindBy(xpath="//input[@id='txtRequiredby']") private Textbox txtReqByHeaderLevel;
	@FindBy(xpath=".//*[@class='buttonClass'][@value='SHOP FOR MORE ITEMS']") private Button btnShopForMoreItems;
	@FindBy(xpath=".//*[@id='customfa']/tbody/tr/td[10]/a[2]") private List<WebElement> lstEditItemGrid;
	@FindBy(xpath=".//*[@value='CONTINUE Checkout']") private Button btnContinueCheckOut;
	@FindBy(xpath=".//*[@id='ddlshippingaddress']") private WebElement lstShippingAdd;


	//**Constructor**//*
	public CostCenterPage(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}


	public void pageLoaded(){
		costCenterContainer.syncVisible(20, false);
	}

	/**Page Interactions**/


	/**
	 * @summary Clicking Internal Comment Link
	 * @author  Lalitha Banda
	 * @date    15/09/16
	 */
	public void click_InternalComment(){
		pageLoaded();
		driver.executeJavaScript("arguments[0].click();", lstInternalCommentGrid.get(0));
		//lstInternalCommentGrid.get(0).click();
	}

	/**
	 * @summary Clicking CopyItem Link
	 * @author  Lalitha Banda
	 * @date    15/09/16
	 */
	public void click_CopyItemLink(){
		Sleeper.sleep(5000);
		lstCopyItemGrid.get(0).click();
	}


	/**
	 * @summary Adding Internal Comment
	 * @author  Lalitha Banda
	 * @date    15/09/16
	 */
	public void AddInternalComment(String InternalComment){
		lblCC.syncVisible(30, false);
		click_InternalComment();
		txtInternalComment.safeSet(InternalComment);
		btnSaveComments.click();

		if(txtCommentDisplayed.size()>0){
			for(WebElement fullComment :txtCommentDisplayed){
				String Expected = fullComment.getText();
				TestReporter.logStep("Actual :"+ InternalComment + ": " + "Expected : "+ Expected);
				TestReporter.assertTrue(Expected.equals(InternalComment), "Internal Comment Displayed is Verified!!");
				break;
			}
		}else{
			TestReporter.assertTrue(false, "Internal Comment Wrongly Displayed");
		}
		btnClosePopUp.click();
	}


	// Method to change Quantity 
	public void changeQuantity(String QtyValue ,WebElement inputElement){
		lstQty.get(0).clear();
		lstQty.get(0).sendKeys(QtyValue);
		driver.executeJavaScript("arguments[0].click();",inputElement);
		//handle alert pop up if present
		//TODO this is not a graceful way to handle multiple items that each have an alert...
		if (AlertHandler.isAlertPresent(driver, 6)) {
			AlertHandler.handleAlert(driver, 6);
			if (AlertHandler.isAlertPresent(driver, 6)) {
				AlertHandler.handleAlert(driver, 6);
			}
		}
		//Wait for the success message to disappear so that the qty is fully updated
		Sleeper.sleep(2000);

		System.out.println("Total number of qty text boxes : "+lstQty.size());
		String updatedQty =lstQty.get(0).getAttribute("value");
		TestReporter.assertTrue(updatedQty.equalsIgnoreCase(QtyValue), "Quantity Updated Successfully!!");
	}

	public void VerifyDelete(){

	}



	/**
	 * @summary Method to verify Cost Center
	 * @author  Lalitha Banda, @date    20/09/16
	 * @param verifyType, @param itemNumber,@param CCValue,@param QuantityValue
	 * @return
	 */
	public boolean verifyCostCenter(String verifyType,String itemNumber,String CCValue,String QuantityValue,String ItemDescription){
		boolean statusFlag = false;
		List<WebElement> readLinks = driver.findElements(By.xpath(".//*[@id='customfa']/tbody/tr/td[1]/a"));
		List<WebElement> readSelects = driver.findElements(By.xpath(".//*[@id='customfa']/tbody/tr/td/select"));
		List<WebElement> readDeleteIcons = driver.findElements(By.xpath(".//*[@title='Delete']"));
		List<WebElement> readText = driver.findElements(By.xpath(".//*[@id='customfa']/tbody/tr/td[2]"));

		switch(verifyType.toLowerCase()){
		case "linelevel":
			for(WebElement inputLink : readLinks){
				for(WebElement inputSelects: readSelects){
					String updatedCC = new Select(inputSelects).getFirstSelectedOption().getText();
					if(inputLink.getText().equalsIgnoreCase(itemNumber) &&updatedCC.equalsIgnoreCase(CCValue)){
						statusFlag = true;			
						break;
					}
				}
			}

			break;

		case "headerlevel": 
			for(int i=0;i<readSelects.size();i++){
				String ExpectedCC = new Select(readSelects.get(i)).getFirstSelectedOption().getText();
				if(ExpectedCC.equalsIgnoreCase(CCValue)){
					statusFlag =true;
				}
			}
			break;

		case "copyitem":
			for(WebElement input :readSelects){
				if(new Select(input).getFirstSelectedOption().getText().contains(CCValue)){
					String randomID =input.getAttribute("id").replaceAll("\\D+", "");
					WebElement element = driver.findElement(By.xpath(".//*[@id='trCCRow_"+randomID+"']/td[9]"));
					driver.executeJavaScript("arguments[0].click();", element);
					if(txtShowFullComment.isDisplayed()){
						TestReporter.assertTrue(false, "Copied Item having Internal Comments");
					}else{
						TestReporter.assertTrue(true, "Copied Item is not having Internal Comments");
					}
				}
			}
			break;

		case "quantity": 
			// Change Quantity - Line Level record
			changeQuantity(QuantityValue,lstSave.get(0));
			break;
		case "savecart": 
			// Verify Save Cart 
			changeQuantity(QuantityValue,btnSaveCartCC);
			break;

		case "delete":
			// Perform Delete 
			int sizeBeofreDelete =lstQty.size();
			driver.executeJavaScript("arguments[0].click();", readDeleteIcons.get(0));
			//Handle Alert if present
			if(AlertHandler.isAlertPresent(driver, 6)){
				AlertHandler.handleAlert(driver, 6);
			}
			// Waiting for quantity table 
			Sleeper.sleep(2000);
			int sizeAfterDelete =lstQty.size();
			TestReporter.assertTrue(sizeAfterDelete<sizeBeofreDelete, "Item Deleted Successfully!!");
			break;
		case "edititem" :
			//Read item description here
			for(WebElement input :readText){
				if(input.getText().equalsIgnoreCase(ItemDescription)){
					TestReporter.log("ItemDescription is :"+ItemDescription);
					if(lstEditItemGrid.get(lstEditItemGrid.size()-1).isEnabled()){
						lstEditItemGrid.get(lstEditItemGrid.size()-1).click();
						TestReporter.assertTrue(true, "Clicked Edit Link on Non-Price Agreement Item");
						break;
					}
				}
			}
			break;
		default : System.out.println();

		}
		return statusFlag;
	}



	/**
	 * @summary: Method to get the date as per the daysout value.
	 * @param daysOut
	 * @return
	 */
	public String getDate(String daysOut){
		DateTimeConversion dteTimeCon = new DateTimeConversion();
		String setDaysOut = dteTimeCon.getDaysOut(daysOut, "MM/dd/yyyy").toString();
		TestReporter.logStep("No. of days-out : " + daysOut);
		//System.out.println("Date : "+setDaysOut);
		return setDaysOut;
	}

	/**
	 * @summary Method to change Cost Center
	 * @author  Lalitha Banda
	 * @date    16/09/16
	 */
	public void change_CC(String changeType,String CC){
		// after cart check out, application taking time to load cost center page 
		lblCC.syncVisible(30, false);
		//lblCC.isDisplayed();
		switch(changeType.toLowerCase()){

		case "linelevel" : 
			TestReporter.logStep( "CC before updation : "+ddCCLineLevel.get(0).getFirstSelectedOption().getText());
			//Read item number here
			String randomID =ddCCLineLevel.get(0).getAttribute("id").replaceAll("\\D+", "");
			String itemNumber = driver.findElement(By.xpath(".//*[@id='trCCRow_"+randomID+"']/td/a")).getText();
			TestReporter.logStep("itemNumber :"+ itemNumber);
			ddCCLineLevel.get(0).select(CC);
			System.out.println(verifyCostCenter(changeType,itemNumber,CC,null,null));
			TestReporter.assertTrue(verifyCostCenter(changeType,itemNumber,CC,null,null),"Cost Center Updated at Line Level Successfully!!");
			break;

		case "headerlevel" :
			driver.setElementTimeout(Constants.ELEMENT_TIMEOUT);
			lstCostCenterHeaderLevel.syncVisible(30);
			lstCostCenterHeaderLevel.select(CC);
			//Handle Alert if present
			if(AlertHandler.isAlertPresent(driver, 6)){
				AlertHandler.handleAlert(driver, 6);
			}
			System.out.println(verifyCostCenter(changeType,null,CC,null,null));
			TestReporter.assertTrue( verifyCostCenter(changeType,null,CC,null,null),"Cost Center Updated at Header Level Successfully!!");
			break;

		default : System.out.println();
		}
	}

	public String selectCCToCopyItem(){
		String ccValue = lstSelectCCValue.get(0).getText();
		System.out.println("CostCenter Name "+lstSelectCCValue.get(0).getText());
		driver.executeJavaScript("arguments[0].click();", lstSelectCC.get(0));
		Sleeper.sleep(5000);
		return ccValue;
	}

	public String readItemNuber(){
		return lstItemNoGrid.get(0).getText();
	}



	/**
	 * @summary: Method to get the RequiredByDate from textbox at CC_line-level.
	 * @author: Praveen Namburi, @Version: Created 21-09-2016
	 * @return getReqByDate
	 */
	public String getReqByDateAtLineLevel(){
		String getRequreDate = "";
		List<WebElement> reqByDates = dateCCInputLineLevel;
		for(WebElement reqByDate : reqByDates){
			String getReqByDate_beforeChange1 = reqByDate.getAttribute("value");
			//System.out.println(getReqByDate_beforeChange1);
			getRequreDate=getReqByDate_beforeChange1;
			break;
		}
		return getRequreDate;
	}

	/**
	 * @summary: Method to verify the RequiredBydate has been updated Successfully at CC_level.
	 * @author: Praveen Namburi, @version: Created 21-09-2016.
	 */
	public void verify_RequiredByDateUpdated_atCCLevel(){

		//Added wait statement to wait until the Cart item added successfull message to be displayed.
		WebDriverWait wait = new WebDriverWait(driver,10);
		WebElement lblCartAddItemMessage =wait.until(ExpectedConditions.
				visibilityOfElementLocated(By.xpath("//div[@id='divAppInfoMsg'][@class='addMessage']")));
		String getReqByDateMessage = lblCartItemAddedMessage.getText();
		TestReporter.logStep("Message after changing the RequiredBy date at CC_level : "+ getReqByDateMessage);
		TestReporter.assertTrue(getReqByDateMessage.contains("date has been updated."), 
				"Required by date has been updated at CC-Level.");

	}

	/**
	 * @summary: Method to change the RequiredBy value at linelevel.
	 * @author: Praveen Namburi, @version: Created 20-09-2016.
	 */
	public void change_RequiredByAtLineLevel(){
		// after cart check out, application taking time to load cost center page 
		lblCC.syncVisible(30, false);
		String getReqByDate_beforeChange = getReqByDateAtLineLevel();
		TestReporter.log("Get ReqByDate_before Change: " + getReqByDate_beforeChange);

		driver.executeJavaScript("arguments[0].click();", dateCCLineLevel.get(0));
		List<WebElement> nextMonthArrows = driver.findElements(By.xpath("html/body/div[@class='calendar']/"
				+ "table/thead/tr[2]/td[4]"));
		int getNextMonthArrowsSize = nextMonthArrows.size();
		System.out.println("Next month arrows size: "+getNextMonthArrowsSize);
		for(WebElement nextMonth : nextMonthArrows){
			nextMonth.click();
			List<WebElement> selectDates = driver.findElements(By.xpath("html/body/div[@class='calendar']/"
					+ "table/tbody/tr[4]/td[@class='day']"));
			for(WebElement selWeekDate : selectDates){
				selWeekDate.click();
				break;
			}
		}

		//Verify the message -'The RequiredBy date has been updated' is displayed.
		verify_RequiredByDateUpdated_atCCLevel();

		List<WebElement> inputDates = dateCCInputLineLevel;
		int getInputDatesSize = inputDates.size();
		TestReporter.log("Total no. of input dates available at CC_line-level: " + getInputDatesSize);
		//Iterate through each ReqBydate element and get text,
		//verify the expected date with actual values.
		for(WebElement inputDate : inputDates){
			String getModifiedDate = inputDate.getAttribute("value");
			TestReporter.log("Get ReqByDate after change: "+getModifiedDate);
			TestReporter.assertNotEquals(getReqByDate_beforeChange, getModifiedDate, 
					"Date has been updated at line-level for the id: - ["+inputDate.getAttribute("id")+"]");
			break;
		}

	}

	/**
	 * @summary: Method to change the RequiredBy value at Header level.
	 * @author: Praveen Namburi, @version: Created 22-09-2016.
	 * @param daysOut
	 */
	public void change_RequiredByAtHeaderLevel(){
		// after cart check out, application taking time to load cost center page 
		lblCC.syncVisible(30, false);
		//To click on calender at CC_HeaderLevel.
		driver.executeJavaScript("arguments[0].click();", lnkDateCC_HeaderLvel);
		List<WebElement> nextMonthArrows = driver.findElements(By.xpath("html/body/div[@class='calendar']/"
				+ "table/thead/tr[2]/td[4]"));
		int loopCount=0;
		for(WebElement nextMonth : nextMonthArrows){
			nextMonth.click();
			driver.setElementTimeout(3);
			List<WebElement> selectDates = driver.findElements(By.xpath("html/body/div[@class='calendar']/"
					+ "table/tbody/tr[4]/td[@class='day']"));
			for(WebElement selWeekDate : selectDates){
				selWeekDate.click();
				//driver.setPageTimeout(5);
				loopCount++;
				break;
			}
			if(loopCount>0) break;
		}

		//Verify the message -'The RequiredBy date has been updated' is displayed.
		verify_RequiredByDateUpdated_atCCLevel();
		//driver.setElementTimeout(4);

		//Get the RequiredBy date at header level.
		String getReqByDate_AtHeaderLevel = txtReqByHeaderLevel.getText();
		TestReporter.log("Get ReqByDate_before Change: " + getReqByDate_AtHeaderLevel);

		List<WebElement> inputDates = dateCCInputLineLevel;
		int getInputDatesSize = inputDates.size();
		TestReporter.log("Total no. of input dates available at CC_line-level: " + getInputDatesSize);
		//Iterate through each ReqBydate element and get text,
		//verify the expected date with actual values.
		for(WebElement inputDate : inputDates){
			String getModifiedDate = inputDate.getAttribute("value");
			TestReporter.assertEquals(getReqByDate_AtHeaderLevel, getModifiedDate, 
					"Date has been updated at line-level for the id: - ["+inputDate.getAttribute("id")+"]");
		}

	}


	/**
	 * @summary Method To click button - Shop For More Items 
	 * @author  Lalitha Banda
	 * @date    28/09/16
	 */
	public void click_ShopForMoreItems(){
		pageLoaded();
		btnShopForMoreItems.syncVisible(5, false);
		btnShopForMoreItems.jsClick();
	}


	/**
	 * @summary Method to verify Edit link Enabled or Disabled in costcenter page
	 * @author  Praveen Namburi
	 * @date    27/09/16
	 */
	public void verifyEDITlinkEnabledOrNot(){
		// Verify Edit for Non Price agreement 
		List<WebElement> unitPriceEdits = driver.findElements(By.xpath(".//*[@id='customfa']/tbody/tr/td[6]/div/input"));
		for(WebElement input : unitPriceEdits){
			String randomID =input.getAttribute("name").replaceAll("\\D+", "");
			String classValue = driver.findElement(By.xpath(".//*[@id='trCCRow_"+randomID+"']/td[10]/a[2]/i")).getAttribute("class");
			if(classValue.contains("cursor-pointer")){
				TestReporter.assertTrue(true, "Edit Item for non Price Agreement Item is Enabled !!");
				break;
			}
		}

		// Verify Edit for  Price agreement ,PA Expired 
		List<WebElement> unitPriceEdits1 = driver.findElements(By.xpath(".//*[@id='customfa']/tbody/tr/td[6]/div/span"));
		String randomID =unitPriceEdits1.get(1).getAttribute("id").replaceAll("\\D+", "");
		String classValue = driver.findElement(By.xpath(".//*[@id='trCCRow_"+randomID+"']/td[10]/a[2]/i")).getAttribute("class");
		if(classValue.contains("opacity")){
			TestReporter.assertTrue(true, "Edit Item for  Price Agreement Item is Disabled !!");
		}
	}

	/**
	 * @summary Method to Select Shipping Address
	 * @author  Lalitha Banda
	 * @date    28/09/16
	 */
	public void selectShippingAddress(){
		pl.isDomComplete(driver);
		new Select(lstShippingAdd).selectByIndex(0);
	}

	/**
	 * @summary Method to click continue checkout
	 * @author  Lalitha Banda
	 * @date    28/09/16
	 */
	public void click_ContinueCheckOut(){
		selectShippingAddress();
		pageLoaded();
		btnContinueCheckOut.syncVisible(5, false);
		btnContinueCheckOut.jsClick();
	}
	
	/**
	 * @summary Method  verify whether Quantity updated successfully
	 * @author  Praveen Varma, @date 28/09/16
	 */
	public void verifyUpdatedQuantity(String QtyValue){
		TestReporter.logStep("Total size : "+lstQty.size());
		String updatedQty =lstQty.get((lstQty.size()-1)).getAttribute("value");
		TestReporter.logStep("Updated Quantity : "+updatedQty);
		TestReporter.assertTrue(!updatedQty.equalsIgnoreCase(QtyValue), "Quantity Updated Successfully!!");
	}
	
	public String getCostCenterPageTitle(){
		//driver.manage().timeouts().implicitlyWait(Constants.PAGE_TIMEOUT, TimeUnit.SECONDS);
		String actualPageTitle = driver.getTitle();
		return actualPageTitle;
	}
	
	/**
	 * @Summary: Method to select the CC value at header level and verify the CC is updated.
	 * @author: Praveen Namburi, @Version: Created 03-10-2016
	 * @param costCenter
	 */
	public void selectCCValueAtHeaderLevel(String costCenter){
		driver.manage().timeouts().implicitlyWait(Constants.PAGE_TIMEOUT, TimeUnit.SECONDS);
		lstCostCenterHeaderLevel.syncEnabled(30);
		selectShippingAddress();
		Sleeper.sleep(2000);
		lstCostCenterHeaderLevel.select(costCenter.toUpperCase().trim());
		if(AlertHandler.isAlertPresent(driver, 6)){
			AlertHandler.handleAlert(driver, 6);
		}
		// Waiting for CC updated message to be visible. 
		verify_CostCenterUpdatedAtHeaderLevel();
		pageLoaded();
		//driver.setElementTimeout(Constants.ELEMENT_TIMEOUT);
		// Click on Continue CheckOut.
		TestReporter.logStep("Click on 'Continue CheckOut' button.");
		btnContinueCheckOut.syncEnabled(15,false);
		btnContinueCheckOut.jsClick();
	}
	
	/**
	 * @summary: Method to verify the CostCenter/PAR is Updated at HeaderLevel.
	 * @author: Praveen Namburi, @Version: Created 03-10-2016.
	 */
	public void verify_CostCenterUpdatedAtHeaderLevel(){
		//Added wait statement to wait until the Cart item added successfull message to be displayed.
		pl.isDomComplete(driver,3);
		WebDriverWait wait = new WebDriverWait(driver,15);
		WebElement lblCartAddItemMessage =wait.until(ExpectedConditions.
				visibilityOfElementLocated(By.xpath("//div[@id='divAppInfoMsg'][@class='addMessage']")));
		String getCCupdatedMessage = lblCartItemAddedMessage.getText();
		TestReporter.logStep("Message after changing the Cost Center/PAR at CC_level : "+ getCCupdatedMessage);
		TestReporter.assertTrue(getCCupdatedMessage.contains("Cost Center/PAR has been updated"), 
				"The Cost Center/PAR has been updated.");

	}
		
 }
