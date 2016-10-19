package com.xeeva.approvals;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.impl.ElementImpl;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.AlertHandler;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.PageLoaded;
import com.orasi.utils.Sleeper;
import com.orasi.utils.TestReporter;
import com.xeeva.catalog.CostCenterPage;

/**
 * @summary This page contains Approvals Page objects
 * @author  Lalitha Banda
 *
 */
public class ApprovalsPage {

	PageLoaded pl = new PageLoaded();
	private OrasiDriver driver = null;
	private ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);
	private String ReqRow = "//*[@id='divREQList'][@class='Datagridborder']/tbody/tr";

	//**Page Elements**//*
	@FindBy(id = "lnkAMS")	private Link approvalsTab;
	@FindBy(xpath = ".//*[@id='aTab2']")	private Link approvalsSubTab;
	@FindBy(xpath= "//*[@id='divREQList']/div/input[1]")private Button btnApproveSelected;
	@FindBy(xpath = "//*[@id='divREQList'][@class='Datagridborder']/tbody/tr") private List<WebElement> ReqDetailsGrid;
	@FindBy(xpath = ".//*[@for='chkApproveAll'][@class='css-label']") private Label chkApprove;
	@FindBy(xpath = ".//*[@id='accChangeID']") private Link lnkGLCCEdit;


	// Page Objects for GL/CC Change Pop up 
	@FindBy(css = "#ddlGLAccount")	private WebElement eleGLAccount;
	@FindBy(css = "#ddlCostCenter")	private WebElement eleCC;
	@FindBy(id = "txtCCComments")	private Textbox txtGLCC_Comments;
	@FindBy(id = "btnCCChange")	private Button btnSubmit;

	@FindBy(xpath = ".//*[@class='TextBoxCal width60px'][@type='text']") private List<WebElement> lstNTEGrid;
	@FindBy(xpath = ".//*[@id='btnProcess']") private List<WebElement> lstProcessGrid;

	// Filter Your Criteria 
	@FindBy(id = "txtRfqNumberSearch")	private Textbox txtRFQ;
	@FindBy(id = "btnSearch")	private Button btnSearch;

	//**Constructor**//*

	public ApprovalsPage(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}

	private void pageLoaded(){
		btnApproveSelected.syncVisible(20, false);
	}

	//**Page Interactions**//*

	public void click_ApprovalsSubTab(){
		pl.isDomComplete(driver,5);
		approvalsSubTab.syncVisible(50, false);
		driver.executeJavaScript("arguments[0].click();", approvalsSubTab);
		Sleeper.sleep(5000);
	}


	/**
	 * @summary  Method to clcik on Approvals Tab
	 * @author Lalitha Banda
	 * @date  04/10/16
	 **/
	public void click_ApprovalsTab(){
		approvalsTab.syncVisible(40, false);
		driver.executeJavaScript("arguments[0].click();", approvalsTab);
		driver.manage().timeouts().implicitlyWait(Constants.PAGE_TIMEOUT, TimeUnit.SECONDS);
	}

	/**
	 * @summary  Methods to click on REQ Tab , And Read RFQ number
	 * @author Lalitha Banda
	 * @date  05/10/16
	 **/
	public void click_REQ(String inputRFQ){
		pl.isDomComplete(driver,5);
		Sleeper.sleep(3000);
		// Waiting till Record gets updated 
		String Rfq = driver.findElement(By.xpath(".//*[@id='divREQList']/tbody/tr[1]/td[8]/span")).getText();
		TestReporter.logStep("RFQ : "+Rfq);
		do{
			Sleeper.sleep(5000);
			click_Search();
			String requiredRFQ =  driver.findElement(By.xpath(".//*[@id='divREQList']/tbody/tr[1]/td[8]/span")).getText();
			if(requiredRFQ.equalsIgnoreCase(inputRFQ)){
				break;
			}
		}while(!Rfq.equalsIgnoreCase(inputRFQ));
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		pl.isDomComplete(driver, 5);

		// Clcik First Record to Approve 
		driver.executeJavaScript("arguments[0].click();", 
				driver.findElement(By.xpath(ReqRow+"["+selectOrderToApprove()+"]//td[7]/div/a")));
		//driver.findElement(By.xpath(ReqRow+"["+selectOrderToApprove()+"]//td[7]/div/a")).click();
	}
	public String ReadRFQNumber(){
		String returnValue = null;
		if(selectOrderToApprove()!=0){
			TestReporter.logStep("Selected Row : "+selectOrderToApprove());
			if(driver.findElement(By.xpath(ReqRow+"["+selectOrderToApprove()+"]//td[8]/span")).isDisplayed()){
				returnValue = driver.findElement(By.xpath(ReqRow+"["+selectOrderToApprove()+"]//td[8]/span")).getText();}
		}else{
			TestReporter.assertTrue(false, "No records found with 'Waiting For Approval' status!!");
		}
		return returnValue;
	}

	/**
	 * @summary  Method to click on Approve CheckBox
	 * @author Lalitha Banda
	 * @date  05/10/16
	 **/
	public void click_Approve(){
		pageLoaded();
		pl.isDomComplete(driver,5);
		Sleeper.sleep(3000);
		try{chkApprove.click();}catch(Exception e){
			driver.executeJavaScript("arguments[0].click();", chkApprove);	
		}
	}



	/**
	 * @summary  Method to click on search
	 * @author Lalitha Banda
	 * @date  10/10/16
	 **/
	public void click_Search(){
		btnSearch.syncVisible(15);
		driver.executeJavaScript("arguments[0].click();", btnSearch);
		driver.setElementTimeout(Constants.ELEMENT_TIMEOUT);
	}
	/**
	 * @summary  Method to click on GL/CC Edit Link
	 * @author Lalitha Banda
	 * @date  05/10/16
	 **/
	public void click_GLCCEdit(){
		lnkGLCCEdit.syncVisible(5, false);
		lnkGLCCEdit.click();
	}


	/**
	 * @summary  Method to click on process button
	 * @author Lalitha Banda
	 * @date  05/10/16
	 **/
	public void click_Process(){
		lstProcessGrid.get(0).click();
	}

	/**
	 * @summary  Method to select an order to approve
	 * @author Lalitha Banda
	 * @date  04/10/16
	 * @return rowNumber
	 **/
	public int selectOrderToApprove(){
		pageLoaded();
		pl.isDomInteractive(driver,5);
		int reqRecordsCount = ReqDetailsGrid.size();
		System.out.println("No of REQ Records : "+reqRecordsCount);
		int selectedRow = 0; boolean rowSelected = false;
		for(int iterator=1;iterator<=reqRecordsCount;iterator++){
			TestReporter.logStep(driver.findElement(By.xpath(ReqRow+"["+iterator+"]/td[14]")).getText().trim());
			String Actual = driver.findElement(By.xpath(ReqRow+"["+iterator+"]/td[14]")).getText().trim();
			if(Actual.equalsIgnoreCase("Waiting for Approval")){
				selectedRow = iterator;
				rowSelected = true;
			}
			if(rowSelected)break;
		}
		return selectedRow;
	}

	/**
	 * @summary  Method to click on Hold button for selected row and validates the R&A Status
	 * @author Lalitha Banda
	 * @date  04/10/16
	 * @return NA
	 **/
	public void clickonHoldAndValidate(int row,String expMessage,String status){
		pageLoaded();
		pl.isDomInteractive(driver);
		if(driver.findElement(By.xpath(ReqRow+"["+row+"]/td[1]/input[1]")).getAttribute("value").trim().equalsIgnoreCase(status)){
			driver.findElement(By.xpath(ReqRow+"["+row+"]/td[1]/input[1]")).click();
			// Handles Alert if any 
			handleAlert();
			pl.isDomInteractive(driver);
			Sleeper.sleep(5000);
			new ElementImpl(driver.findElement(By.xpath(ReqRow+"["+row+"]/td[14]"))).syncVisible(25,false);
			String actualMsg = driver.findElement(By.xpath(ReqRow+"["+row+"]/td[14]")).getText().trim();
			System.out.println(actualMsg);
			TestReporter.assertTrue(actualMsg.equalsIgnoreCase(expMessage),"R&A Status after Click on Hold is ["+actualMsg+"]");
		}

	}

	/**
	 * @summary  Method to Handle Alert 
	 * @author Lalitha Banda
	 * @date  10/10/16
	 * @return NA
	 **/
	public void handleAlert(){
		String alertMsg = null;
		AlertHandler al = new AlertHandler();
		if(al.isAlertPresent(driver, 3)){
			Alert alert = driver.switchTo().alert();
			alertMsg = alert.getText().trim();
			TestReporter.log(alertMsg);
			alert.accept();
		}
	}


	// Method to Perform GL/CC Change 
	public void perform_GL_CC_Change(String inputstring){
		new Select(eleGLAccount).selectByIndex(0);
		new Select(eleCC).selectByIndex(0);
		txtGLCC_Comments.safeSet(inputstring);
		btnSubmit.click();
		handleAlert();
	}


	// Method For Change NTE
	public void add_NTEValue(String inputString){
		pl.isDomComplete(driver);
		lstNTEGrid.get(0).clear();
		lstNTEGrid.get(0).sendKeys(inputString);
	}


	/**
	 * @summary Method to Add Comments 
	 * @author  Lalitha Banda
	 * @date    05/10/2016
	 */
	public void add_Comments(String inputString){
		/**Reading Method available in CostCenter page for adding internal comments 
		 * in Confirm Request Page 
		 */
		CostCenterPage ccPage = new CostCenterPage(driver);
		ccPage.AddInternalComment(inputString);
	}


	/**
	 * @summary  Method to Perform ApprovalProcess
	 * @author  Lalitha Banda
	 * @date    10/10/2016
	 */
	public void performApprovalProcess(String inputRFQ){
		click_REQ(inputRFQ);
		click_Approve();
		click_Process();
	}


	/**
	 * @summary  Method to perform RFQ search 
	 * @author  Lalitha Banda
	 * @date    10/10/2016
	 */
	public void perform_RFQSearch(String rfqNumber){
		pl.isDomComplete(driver);
		Sleeper.sleep(5000);
		txtRFQ.clear();
		txtRFQ.sendKeys(rfqNumber);
		click_Search();

	}

	/**
	 * @summary Method to read RFQ status
	 * @author  Lalitha Banda
	 * @date    10/10/2016
	 */
	public String read_RFQStatus(String inputRFQ){
		String status =null;
		pageLoaded();
		click_ApprovalsTab();
		pl.isDomInteractive(driver);
		int reqRecordsCount = ReqDetailsGrid.size();
		System.out.println("No of REQ Records : "+reqRecordsCount);
		for(int iterator=1;iterator<=reqRecordsCount;){
			String expectedStatus = driver.findElement(By.xpath(ReqRow+"["+iterator+"]/td[14]")).getText().trim();
			driver.setElementTimeout(Constants.ELEMENT_TIMEOUT);
			click_Search();
			pl.isDomComplete(driver, 5);
			System.out.println("Record Status : "+expectedStatus);

			if(expectedStatus.equalsIgnoreCase("Waiting for Approval")){
				// Performing Approval Process
				performApprovalProcess(inputRFQ);	
			}
			// Reading REQ Status  			
			do{
				Sleeper.sleep(5000);
				click_ApprovalsTab();
				pl.isDomComplete(driver);
				String requiredStatus = driver.findElement(By.xpath(ReqRow+"["+iterator+"]/td[14]")).getText().trim();
				System.out.println("Required Record Status : "+requiredStatus);
				if(requiredStatus.equalsIgnoreCase("Released")){
					status = requiredStatus;
					break;
				}
			}while(expectedStatus.equalsIgnoreCase("In-Progress"));
			break;
		}
		return status;
	}


	// Method for Reading status For Approval Tab Approving Process
	public String read_RFQStatus_ApprovalProcess(String inputRFQ){
		String status =null;
		pageLoaded();
		click_ApprovalsSubTab();
		pl.isDomInteractive(driver);
		int reqRecordsCount = ReqDetailsGrid.size();
		System.out.println("No of REQ Records : "+reqRecordsCount);
		for(int iterator=1;iterator<=reqRecordsCount;){
			String expectedStatus = driver.findElement(By.xpath(ReqRow+"["+iterator+"]/td[14]")).getText().trim();
			driver.setElementTimeout(Constants.ELEMENT_TIMEOUT);
			click_Search();
			pl.isDomComplete(driver, 5);
			System.out.println("Record Status : "+expectedStatus);

			if(expectedStatus.equalsIgnoreCase("Waiting for Approval")){
				driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
				// Performing Approval Process
				click_ApprovalsTab();
				click_ApprovalsSubTab();
				performApprovalProcess(inputRFQ);	
			}

			// Reading REQ Status  			
			do{
				Sleeper.sleep(5000);
				click_ApprovalsTab();
				click_ApprovalsSubTab();
				pl.isDomComplete(driver);
				String requiredStatus = driver.findElement(By.xpath(ReqRow+"["+iterator+"]/td[14]")).getText().trim();
				System.out.println("Required Record Status : "+requiredStatus);
				if(requiredStatus.equalsIgnoreCase("Released")){
					status = requiredStatus;
					break;
				}
			}while(expectedStatus.equalsIgnoreCase("In-Progress"));
			break;
		}
		return status;
	}

}
