package com.xeeva.quote;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Checkbox;
import com.orasi.core.interfaces.Element;
import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.Listbox;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.Webtable;
import com.orasi.core.interfaces.impl.WebtableImpl;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.PageLoaded;
import com.orasi.utils.Sleeper;
import com.orasi.utils.TestReporter;

public class QuotePage {

	PageLoaded pl = new PageLoaded();
	private OrasiDriver driver = null;
	private ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);

	//**Page Elements**//*

	@FindBy(linkText = "Quote")	private Link quoteTab;
	@FindBy(id = "aTab1")	private Button btnRequisition;
	//search criteria

	//@FindBy(xpath = ".//*[@id='ddcl-ddlStatus']/span/div")	private Listbox lstStatus;
	@FindBy(xpath = ".//*[@id='ddcl-ddlStatus']/span/span") private WebElement lstStatus;
	@FindBy(xpath="//div/label[@class='ui-dropdownchecklist-text']") private List<WebElement> lstStatuses;
	@FindBy(xpath = "//*[@id='ddcl-ddlStatus-ddw']/div']")	private Listbox lstStatusInside;
	@FindBy(id="txtRFQNumber")	private Textbox txtRFQNumber;
	@FindBy(id = "btnRFQSearch")	private Button btnRFQSearch;

	//Requisition
	@FindBy(xpath = ".//*[@id='tblRFQ']/tbody/tr/td[2]/div/i")	private List<WebElement> lstMaximise;
	@FindBy(xpath = "//*[@title='RFQ Information']")	private List<WebElement> lstRFQInfo;
	@FindBy(xpath = "//*[@title='Take Ownership']")	private List<WebElement> lstTakeOwnership;
	@FindBy(xpath = "//*[@title='Edit RFQ Detail']")	private List<WebElement> lstEditRFQDetail;
	@FindBy(id = "btnBack")	private Button btnBack;


	//Edit RFQ Information	
	@FindBy(className = "DataGridHeader borderBottomE0E0E1")	private Label lblRFQInformation;
	@FindBy(id = "ddlCategortyType")	private Listbox categoryType;
	@FindBy(id = "ddlCategory")	private Listbox category;
	@FindBy(id = "ddlSubCategory")	private Listbox subCategory;
	@FindBy(id = "txtQuantity")	private Textbox txtQuantity;
	@FindBy(id = "ddlUOM")	private Listbox unitOfMeasure;
	@FindBy(id = "txtUnitPrice")  private Textbox txtUnitPrice;
	@FindBy(id = "txtManufacturer") private Textbox txtMN;
	@FindBy(id = "txtMPN")  private Textbox txtMPN;
	@FindBy(id = "ddlTaxType")	private Listbox lstTaxType;
	@FindBy(id = "ddlTaxCode")	private Listbox lstTaxCode;
	@FindBy(id = "txtHeadDueDate")  private Textbox txtQuoteDueDate;
	@FindBy(id = "btnUploadDocument")	private Button btnUploadDoc;
	@FindBy(id = "btnSave")	private Button btnSave;
	@FindBy(id = "btnSubmit")	private Button btnSubmitForPreApproval;
	@FindBy(xpath = "//*[@for='chkAll']")	private Checkbox chkSelect;
	@FindBy(id = "txtUploadFile")	private Button btnBrowse;
	@FindBy(id = "ajaxUploadButton")	private Button btnUpload;
	@FindBy(xpath = ".//*[@id='tblRFQList']/tbody/tr[2]/td[5]")	private Element approverEmail;


	@FindBy(xpath = "//*[@id='ddlDestinationType']/option[1]")	private Listbox lstDestinationSupplier;
	@FindBy(xpath = "//*[@id='ddlDestinationType']/option[2]")	private Listbox lstDestinationInternal;
	@FindBy(id="ddlDestinationType") private Listbox lstDestination;	
	@FindBy(css = ".fa.fa-reply.font16px.cursor-pointer") private Link lnkRevertOwnership;

	@FindBy(id="btnRFQSearch") private Button btnSearch;
	@FindBy(id="lnkRFQ") private Link lnkQuote;
	@FindBy(xpath="//table[@id='tblRFQ']/tbody/tr") private List<WebElement> tblRFQ;
	
	//RFQ Information
	//@FindBy(id="ddlUOM") private Listbox lstUOM;
	@FindBy(xpath="//table/tbody/tr[2]/td/table/tbody/tr[3]/td[4]/i") private Link lnkRequiredByCalender;
	//@FindBy(xpath="//table/tbody/tr[2]/td/table/tbody/tr[3]/td[6]/i") private Link lnkQuoteDueCalender;
	//@FindBy(id="btnApply") private Button btnApply;
	//@FindBy(id="tblRFQLines") private Webtable tblRFQLines;
	//@FindBy(id="chkAll") private Checkbox chkALLRFQLines;
	//@FindBy(xpath="//div[@id='divAppInfoMsg'][@class='addMessage']") private Label lblInfoMsg;
	@FindBy(xpath="//table[@id='tblRFQ']/tbody/tr") private Webtable tblRFQLine;
	@FindBy(xpath="//*[@title='Edit RFQ Detail']") private List<WebElement> lstEditRFQs;
	
	// Select supplier
	//@FindBy(id="txtSearchSupplier") private Textbox txtSelectSupplier;
	@FindBy(xpath="//table[@id='tblLeftManuRFQLine']/tbody/tr[1]/td[1]/input") private Checkbox chkSelectAll;
	//@FindBy(xpath="//div[@id='DivSupplierFilters']/table/tbody/tr") private List<WebElement> tblSupplierDetails;
	//@FindBy(id="btnAddSupplier") private Button btnAddSupplier;
	//@FindBy(id="btnSubmit") private Button btnSubmit;
	//@FindBy(id="btnSend") private Button btnSend;
	@FindBy(id="btnContinue") private Button btnContinue;
	//@FindBy(xpath="//table[@id='tblRFQ']/tbody/tr") private List<WebElement> tblRFQ;

	@FindBy(id="ddlUOM") private Listbox lstUOM;
	@FindBy(xpath="//table/tbody/tr[2]/td/table/tbody/tr[3]/td[6]/i") private Link lnkQuoteDueCalender;
	@FindBy(id="btnApply") private Button btnApply;
	@FindBy(id="tblRFQLines") private Webtable tblRFQLines;
	@FindBy(id="chkAll") private Checkbox chkALLRFQLines;
	@FindBy(xpath="//div[@id='divAppInfoMsg'][@class='addMessage']") private Label lblInfoMsg;
	 
	// Select supplier
	@FindBy(id="txtSearchSupplier") private Textbox txtSelectSupplier;
	//@FindBy(xpath="//table[@id='tblLeftManuRFQLine']/tbody/tr[1]/td[1]/input") private Checkbox chkSelectAll;
	@FindBy(xpath="//div[@id='DivSupplierFilters']/table/tbody/tr") private List<WebElement> tblSupplierDetails;
	@FindBy(id="btnAddSupplier") private Button btnAddSupplier;
	@FindBy(id="btnSubmit") private Button btnSubmit;
	@FindBy(id="btnSend") private Button btnSend;
	 

	//**Constructor**//*

	public QuotePage(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}

	private void pageLoaded(){
		btnRequisition.syncVisible(5);
	}

	//**Page Interactions**//*

	/**
	 * @summary Method for clicking Quote Tab 
	 * @author  Lalitha Banda
	 * @date    10/10/16
	 */
	public void click_quoteTab(){
		pl.isDomComplete(driver);
		driver.executeJavaScript("arguments[0].click();",quoteTab);
		driver.manage().timeouts().implicitlyWait(Constants.PAGE_TIMEOUT, TimeUnit.SECONDS);
	}


	/**
	 * @summary Method for maximize line Item
	 * @author  Lalitha Banda
	 * @date    10/10/16
	 */
	public void click_MaximizeRFQ(){
		pl.isDomComplete(driver);
		Sleeper.sleep(2000);
		driver.executeJavaScript("arguments[0].click();",lstMaximise.get(0));
	}

	/**
	 * @summary Method for clicking RFQ Info
	 * @author  Lalitha Banda
	 * @date    10/10/16
	 */
	public void click_RFQInfo(){
		pageLoaded();
		lstRFQInfo.get(0).click();
	}

	/**
	 * @summary Method for Ownership Functionality
	 * @author  Lalitha Banda
	 * @date    10/10/16
	 */
	public void click_TakeOwnership(){
		pageLoaded();
		pl.isDomComplete();
		if(lnkRevertOwnership.isDisplayed()){
			driver.executeJavaScript("arguments[0].click();",lnkRevertOwnership);
		}
		driver.executeJavaScript("arguments[0].click();",lstTakeOwnership.get(0));
	}

	/**
	 * @summary Method to click on Edit 
	 * @author  Lalitha Banda
	 * @date    10/10/16
	 */
	public void click_EditRFQDetail(){
		pageLoaded();
		pl.isDomComplete(driver);
		driver.executeJavaScript("arguments[0].click();",lstEditRFQDetail.get(0));
	}


	/**
	 * @summary Method to perform RFQ search functionality
	 * @author  Lalitha Banda
	 * @date    10/10/16
	 */
	public void enter_RFQNumber(String text){
		txtRFQNumber.clear();
		Sleeper.sleep(2000);
		txtRFQNumber.sendKeys(text);
		driver.executeJavaScript("arguments[0].click();",btnRFQSearch);
	}



	/**
	 * @summary Method to perform Requisition
	 * @author  Lalitha Banda
	 * @date    10/10/16
	 */
	public void perform_Requisition(String text){
		txtRFQNumber.clear();
		txtRFQNumber.sendKeys(text);
		btnRFQSearch.syncVisible(10);
		driver.executeJavaScript("arguments[0].click();",btnRFQSearch);
		pl.isDomComplete(driver);
		click_TakeOwnership();
		click_EditRFQDetail();
	}


	/**
	 * @summary: Method to verify the message after taking the ownership in .
	 * @author: Praveen Namburi, @version: Created 10-04-2016.
	 */
	public void verify_UpdatedOwnership(){
		WebDriverWait wait = new WebDriverWait(driver,3);
		WebElement lblOwnerShipMessage =wait.until(ExpectedConditions.
				visibilityOfElementLocated(By.xpath("//div[@id='divAppInfoMsg'][@class='addMessage']")));
		String getOwnershipUpdatedMessage = lblOwnerShipMessage.getText();
		TestReporter.logStep("Message after updating the quantity in cartInfo page: "+ getOwnershipUpdatedMessage);
		TestReporter.assertTrue(getOwnershipUpdatedMessage.contains("Ownership has been taken"), 
				"Ownership has been taken successfully!");

	}

	/**
	 * @summary: Method to verify the message after taking the ownership in .
	 * @author: Praveen Namburi, @version: Created 10-04-2016.
	 *//*
	public void verify_LineItemsSaved(){
		WebDriverWait wait = new WebDriverWait(driver,3);
		WebElement lblLineItemsSaved =wait.until(ExpectedConditions.
				visibilityOfElementLocated(By.xpath("//div[@id='divAppInfoMsg'][@class='addMessage']")));
		String getLineItemsSaved = lblLineItemsSaved.getText();
		TestReporter.assertTrue(getLineItemsSaved.contains("The Line Items "), 
				"The Line Items have been saved successfully!");

	}*/


	/**
	 * @summary: Method to verify the message after RFQ Info Submitted
	 * @author: Praveen Namburi, @version: Created 10-04-2016.
	 *//*
	public void verify_RFQSubmitted(){
		pl.isDomComplete(driver);
		WebDriverWait wait = new WebDriverWait(driver,3);
		WebElement lblRFQSubmittedMessage =wait.until(ExpectedConditions.
				visibilityOfElementLocated(By.xpath("//div[@id='divAppInfoMsg'][@class='addMessage']")));
		String getRFQSubmittedMessage = lblRFQSubmittedMessage.getText();
		TestReporter.assertTrue(getRFQSubmittedMessage.contains("The RFQ has been submitted"), 
				"The RFQ has been submitted for pre-approval successfully!");

	}
*/

	/**
	 * @summary Method to perform Submit RFQ
	 * @author  Lalitha Banda
	 * @date    03/10/16
	 */
	public void SubmitRFQ(String inputRFQ){
		pl.isDomComplete(driver);
		driver.executeJavaScript("arguments[0].click();",chkSelect);
		click_Save();
		click_SubmitForPreApproval(inputRFQ);
	}


	/**
	 * @summary Method to attach a file 
	 * @author  Lalitha Banda
	 * @throws InterruptedException 
	 * @date    03/10/16
	 */
	public boolean attachfile(String filename,String filetype) {
		boolean statusFlag = false;
		chkSelect.syncVisible(3);
		chkSelect.check();
		btnUploadDoc.click();
		pl.isDomComplete(driver);
		lstDestination.select(filetype); 
		btnBrowse.click();
		Sleeper.sleep(3000);

		String fileLocation = System.getProperty("user.dir") + "\\" + filename+".xlsx";
		TestReporter.logStep(fileLocation);

		StringSelection filepath = new StringSelection(fileLocation);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(filepath, null);


		try {
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);

		} catch (AWTException e) {e.printStackTrace();}

		btnUpload.jsClick();
		statusFlag = true;
		return statusFlag;
	}



	/**
	 * @summary Method to perform Attach File Internal/External
	 * @author  Lalitha Banda
	 * @date    03/10/16
	 */
	public void  perform_FileAttachmentProcess(String filetype,String filename){
		if(filetype.contains("Supplier")?attachfile(filename,filetype):false);
		if(filetype.contains("Internal")?attachfile(filename,filetype):false);
	}

	/**
	 * @summary Method for Save button 
	 * @author  Lalitha Banda
	 * @date    10/10/16
	 */
	public void click_Save(){
		driver.executeJavaScript("arguments[0].click();",btnSave);
	}



	/**
	 * @summary Method To Perform SubmitForPreApproval
	 * @author  Lalitha Banda
	 * @date    10/10/16
	 */
	public void click_SubmitForPreApproval(String inputRFQ){
		btnSubmitForPreApproval.syncVisible(10);
		if(btnSubmitForPreApproval.isDisplayed()){
			driver.executeJavaScript("arguments[0].click();",btnSubmitForPreApproval);
		}else{
			btnBack.syncVisible(5);
			driver.executeJavaScript("arguments[0].click();",btnBack);
			pl.isDomComplete(driver);
			perform_Requisition(inputRFQ);
			SubmitRFQ(inputRFQ);
		}

		pl.isDomComplete(driver);
	}

	/**
	 * @summary Method for grabbing approvals email 
	 * @author  Lalitha Banda
	 * @date    10/10/16
	 */
	public String getApproverEmail() {
		String returnValue = null;
		String xpath = ".//*[@id='tblRFQList']/tbody/tr[2]/td[5]";
		click_MaximizeRFQ();
		Sleeper.sleep(5000);
		WebDriverWait wait = new WebDriverWait(driver, 5);
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
		element.click();
		Sleeper.sleep(2000);
		List<WebElement> totTable = driver.findElements(By.cssSelector(".DataGridrowa>td"));
		for(WebElement inputTable :totTable){
			if(inputTable.getText().contains("@")){
				returnValue =inputTable.getText();
				break;
			}

		}
		return returnValue;
	}

	/**
	 * @summary: Method to search RFQ with Draft status and click on Edit link.
	 * @author: Praveen Namburi, @Version: Created 07-10-2016
	 * @param RFQNumber
	 */
	public void filterAndEditRFQ_withDraftStatus(String RFQNumber){
		pageLoaded();
		txtRFQNumber.syncVisible(5);
		txtRFQNumber.set(RFQNumber);
		btnSearch.syncEnabled(5,false);
		driver.executeJavaScript("arguments[0].click();",btnSearch);
		driver.setPageTimeout(3);
		pl.isDomComplete(driver);
		List<WebElement> requisitionTblRows = tblRFQ;
		int totalRows = requisitionTblRows.size();
		TestReporter.log("Total rows in Requisition table: " + totalRows);
		for(int row=1; row<=totalRows-1; row++){
			if(row % 2 == 0){
			   String getRFQNumber = driver.findElement(By.xpath("//table[@id='tblRFQ']/tbody/"
						+ "tr["+row+"]/td[4]/span")).getText().trim();
			   String getStatus = driver.findElement(By.xpath("//table[@id='tblRFQ']/tbody/"
						+ "tr["+row+"]/td[9]")).getText().trim();
			   if(getRFQNumber.equalsIgnoreCase(RFQNumber) && getStatus.contains("Draft")){
				  driver.findElement(By.xpath("//table[@id='tblRFQ']/tbody/"
						+ "tr["+row+"]/td[2]/div/a[3]/i")).jsClick();
				  break;
			   }
			}
		}
		driver.setElementTimeout(Constants.ELEMENT_TIMEOUT);
	}

	/**
	 * @summary: Method to set RFQ-Info and chk all RFQ Line items to continue. 
	 * @author: Praveen Namburi, @Version: Created 07-10-2016
	 * @param UOM, @param quantity
	 */
	public void setRFQInfoAndContinue(String UOM,String quantity){
		pl.isDomComplete(driver);
		lstUOM.syncVisible(5, false);
		lstUOM.select(UOM);
		txtQuantity.syncVisible(5);
		txtQuantity.set(quantity);
		selectReqBy_And_QuoteDueDates(lnkRequiredByCalender);
		chkALLRFQLines.syncEnabled(5, false);
		driver.executeJavaScript("arguments[0].click();",chkALLRFQLines);
		btnApply.syncEnabled(5, false);
		btnApply.jsClick();
		tblRFQLines.isDisplayed();
		btnSave.isEnabled();
		btnSave.jsClick();
		verify_LineItemsSaved();
		
		
	}
	
	/**
	 * @Summary: Method to select the Quote Reqby/Due Date from calender.
	 * @author: Praveen Namburi. @Version: Created 07-10-2016
	 */
	public void selectReqBy_And_QuoteDueDates(WebElement element){
		lnkQuoteDueCalender.syncVisible(10);
		driver.executeJavaScript("arguments[0].click();", element);
		List<WebElement> nextMonthArrows = driver.findElements(By.xpath("html/body/div[@class='calendar']/"
				+ "table/thead/tr[2]/td[4]"));
		int loopCount=0;
		for(WebElement nextMonth : nextMonthArrows){
			Sleeper.sleep(1000);
			nextMonth.click();
			List<WebElement> selectDates = driver.findElements(By.xpath("html/body/div[@class='calendar']/"
					+ "table/tbody/tr[4]/td[@class='day']"));
			for(WebElement selWeekDate : selectDates){
				pl.isDomComplete(driver);
				selWeekDate.click();
				loopCount++;
				break;
			}
			if(loopCount>0) break;
		}
	}
	
	/**
	 * @summary: Method to verify the Line Items have been saved successfully.
	 * @author: Praveen Namburi, @version: Created 07-10-2016.
	 */
	public void verify_LineItemsSaved(){
		WebDriverWait wait = new WebDriverWait(driver,10);
		wait.until(ExpectedConditions.
				visibilityOfElementLocated(By.xpath("//div[@id='divAppInfoMsg'][@class='addMessage']")));
		String getLineItemsMessage = lblInfoMsg.getText();
		TestReporter.logStep("Message after updating the Line Items: "+ getLineItemsMessage);
		TestReporter.assertTrue(getLineItemsMessage.contains("saved successfully") , 
				"The Line Items have been saved successfully!");

	}
	
	/**
	 * @Summary: Method to select and add supplier to the list.
	 * @author: Praveen Namburi, @Version: Created 07-10-2016 
	 * @param supplier
	 */
	public void selectSupplier(String supplier){
		pl.isDomComplete(driver);
		btnContinue.syncEnabled(5,false);
		btnContinue.jsClick();
		txtSelectSupplier.syncVisible(10,false);
		txtSelectSupplier.set(supplier);
		WebDriverWait wait = new WebDriverWait(driver,7);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='DivSupplierFilters']")));
		List<WebElement> supplierTable = tblSupplierDetails;
		pl.isDomComplete(driver);
		String getSupplierName = supplierTable.get(0).getText().trim();
		TestReporter.log("Selected the supplier:" + getSupplierName);
		supplierTable.get(0).click();
		chkSelectAll.syncEnabled(5, false);
		chkSelectAll.jsClick();
		btnAddSupplier.syncEnabled(10);
		btnAddSupplier.jsClick();
		verify_SupplierIsAdded();
		
	}
	
	/**
	 * @summary: Method to verify the supplier has been added successfully.
	 * @author: Praveen Namburi, @version: Created 07-10-2016.
	 */
	public void verify_SupplierIsAdded(){
		WebDriverWait wait = new WebDriverWait(driver,5);
		wait.until(ExpectedConditions.
				visibilityOfElementLocated(By.xpath("//div[@id='divAppInfoMsg'][@class='addMessage']")));
		String getSupplierAddedMessage = lblInfoMsg.getText();
		TestReporter.logStep("Message after adding the Supplier: "+ getSupplierAddedMessage);
		TestReporter.assertTrue(getSupplierAddedMessage.contains("added successfully") , 
				"The supplier has been added successfully!");

	}
	
	/**
	 * @Summary: Method to click on Submit button in Supplier selection screen.
	 * @author: Praveen Namburi, @Version: Craeted 10-10-2016
	 */
	public void clickSubmit(){
		pl.isDomComplete(driver);
		btnSubmit.syncEnabled(5);
		btnSubmit.jsClick();
	}
	
	/**
	 * @Summary: Method to click on SendRFQ button.
	 * @author: Praveen Namburi, @Version: Craeted 10-10-2016
	 */
	public void clickSendRFQ(){
		pl.isDomComplete(driver);
		btnSend.syncVisible(6,false);
		driver.executeJavaScript("arguments[0].click();", btnSend);
	}
	
	/**
	 * @summary: Method to verify the RFQ has been submitted successfully.
	 * @author: Praveen Namburi, @version: Created 07-10-2016.
	 */
	public void verify_RFQSubmitted(){
		WebDriverWait wait = new WebDriverWait(driver,5);
		wait.until(ExpectedConditions.
				visibilityOfElementLocated(By.xpath("//div[@id='divAppInfoMsg'][@class='addMessage']")));
		String getRFQSubmittedMessage = lblInfoMsg.getText();
		TestReporter.logStep("Message after sending the RFQ: "+ getRFQSubmittedMessage);
		TestReporter.assertTrue(getRFQSubmittedMessage.contains("submitted successfully") , 
				"The RFQ has been submitted successfully!");

	}
	
	/**
	 * @summary: Method to verify the RFQ status after submitting RFQ.
	 * @author: Praveen Namburi, @Version: Craeted 10-10-2016.
	 * @param RFQNumber
	 */
	public void verify_RFQStatus_AfterSubmittingRFQ(String RFQNumber){
		tblRFQLine.syncVisible(6,false);
		List<WebElement> requisitionTblRows = tblRFQ;
		int totalRows = requisitionTblRows.size();
		TestReporter.log("Total rows in Requisition table: " + totalRows);
		for(int row=1; row<=totalRows; row++){
			if(row % 2 == 0){
			   String getRFQNumber = driver.findElement(By.xpath("//table[@id='tblRFQ']/tbody/"
			   		  + "tr["+row+"]/td[4]/span")).getText();
			   TestReporter.log("RFQ Number is: " + getRFQNumber);
			   String getStatus = driver.findElement(By.xpath("//table[@id='tblRFQ']/tbody/"
			   		  + "tr["+row+"]/td[9]")).getText().trim();
			   TestReporter.log("RFQ Number is: " + getStatus);
			   if(getRFQNumber.equalsIgnoreCase(RFQNumber) && getStatus.contains("Active") ){
				   TestReporter.assertTrue(getStatus.contains("Active"), "RFQ Status is 'Active'.");
				  break;
			   }
			}
		}
	  
	}
	
	
}


