package com.xeeva.catalog;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Checkbox;
import com.orasi.core.interfaces.Element;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.AlertHandler;
import com.orasi.utils.Constants;
import com.orasi.utils.FileHandler;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.PageLoaded;
import com.orasi.utils.Sleeper;
import com.orasi.utils.TestReporter;

/**
 * @summary This page contains Confirm Request Page objects
 * @author  Lalitha Banda
 * @date    28/09/16
 */

public class ConfirmRequestPage {

	PageLoaded pl = new PageLoaded();
	private OrasiDriver driver = null;
	private ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);
	String xpath = ".//*[@id='customfa']/tbody/tr/td/select";


	/**Page Elements**/
	@FindBy(id ="btnCheckOutConfirm") private Button btnConfirm;
	@FindBy(xpath =".//*[@for='chkNonBudgeted']") private Checkbox chkNonBudgeted;	
	@FindBy(xpath =".//*[@for='chkBudgeted']") private Checkbox chkBudgeted;	
	@FindBy(xpath =".//*[@class='Datagridborder1 TDtextField100px']/tbody/tr/td[8]") private List<WebElement> lstTotalUSD;
	@FindBy(css =".textAlignRight.borderRight>strong") private List<WebElement> lstTotalValues;
	@FindBy(xpath="//input[@name='Button4'][@value='Back']") private Button btnBack;

	// 
	@FindBy(xpath = "//*[@title='Upload File(s)']") private List<WebElement> lstAttachFile;
	@FindBy(xpath = "//*[@title='Show Attachments']") private List<WebElement> lstshowAttachment;
	@FindBy(id ="lblAttach_0") private Link lnkUploadedFile;
	@FindBy(id ="ajaxUploadButton") private Button btnUpload;
	@FindBy(id ="txtUploadFile") private Button btnBrowse;
	@FindBy(id ="rbtnInternal") private WebElement btnInternal;
	@FindBy(id ="rbtnExternal") private WebElement btnExternal;
	@FindBy(xpath =".//*[@id='fancybox-close']") private Element eleClose;

	/**Constructor**/
	public ConfirmRequestPage(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}


	public void pageLoaded(){
		btnConfirm.syncVisible(20, false);
	}

	/**Page Interactions**/

	/**
	 * @summary Method to click on non budgeted
	 * @author  Lalitha Banda
	 * @date    28/09/16
	 */
	public void click_NonBudgeted(){
		pl.isDomComplete(driver);
		chkNonBudgeted.syncVisible(5, false);
		chkNonBudgeted.click();
	}

	/**
	 * @summary Method to click on  budgeted
	 * @author  Lalitha Banda
	 * @date    28/09/16
	 */
	public void click_Budgeted(){
		pageLoaded();
		chkBudgeted.syncVisible(5, false);
		chkBudgeted.jsToggle();
	}

	/**
	 * @summary Method to click on confirm
	 * @author  Lalitha Banda
	 * @date    28/09/16
	 */
	public void click_Confirm(){
		click_NonBudgeted();
		btnConfirm.syncVisible(5, false);
		btnConfirm.jsClick();
	}


	/**
	 * @summary Method to Add Comments Line Level 
	 * @author  Lalitha Banda
	 * @date    28/09/16
	 */
	public void add_Comments(String inputString){
		/**Reading Method available in CostCenter page for adding internal comments 
		 * in Confirm Request Page 
		 */
		CostCenterPage ccPage = new CostCenterPage(driver);
		ccPage.AddInternalComment(inputString);
	}

	/**
	 * @summary Method to calculate Sub total
	 * @author  Lalitha Banda
	 * @date    28/09/16
	 */
	public String verifySubTotal(){
		double Total = 0 ;
		// Adding Total from Each Line Item 
		for(WebElement inputAmount : lstTotalUSD){
			String[] amountArray = inputAmount.getText().split(" ");
			Total = Total+Double.parseDouble(amountArray[0]);

		}
		// Set precision for Sub Total Value 
		String ExpectedSubTotal = new BigDecimal(String.valueOf(Total)).setScale(5, BigDecimal.ROUND_HALF_UP).toString();
		// Reading Sub Total 
		String[] ActualSubTotal = lstTotalValues.get(0).getText().split(" ");

		// Verify Sub Total 
		TestReporter.assertTrue(ExpectedSubTotal.equals(ActualSubTotal[0]), "Subtotal is available as addition of all item total cost in requester currency");
		TestReporter.logStep("Sub Total : "+ExpectedSubTotal);
		return ExpectedSubTotal;
	}

	/**
	 * @summary Method to calculate Grand total tax
	 * @author  Lalitha Banda
	 * @date    29/09/16
	 */
	public String verifyGrandTotalTax(){
		// Reading Grand total tax 
		String[] ActualGrandTotalTax = lstTotalValues.get(1).getText().split(" ");

		// Verify Grand Total Tax  - For Catalog Grand Total Tax will not impact , should be 0 
		TestReporter.assertTrue(ActualGrandTotalTax[0].equals("0.00000"), "Grand Total Value Verified!!");
		TestReporter.logStep("Grand Total Tax : "+ActualGrandTotalTax[0]);

		return ActualGrandTotalTax[0];

	}

	/**
	 * @summary Method to calculate Grand total 
	 * @author  Lalitha Banda
	 * @date    29/09/16
	 */
	public void verifyGrandTotal(){
		String subTotal = verifySubTotal();
		String grandTotalTax =  verifyGrandTotalTax();

		/**Calculate Grand total by adding Sub total and Grand total tax */
		double grandTotal = Double.parseDouble(subTotal)+Double.parseDouble(grandTotalTax);
		String ExpectedGrandTotal = new BigDecimal(String.valueOf(grandTotal)).setScale(5, BigDecimal.ROUND_HALF_UP).toString();

		// Reading Grand Total
		String[] ActualGrandTotal = lstTotalValues.get(2).getText().split(" ");

		// Verifying  Grand Total 
		TestReporter.assertTrue(ExpectedGrandTotal.equals(ActualGrandTotal[0]), "Grand Total available as sum of sub total and grand total tax");
		TestReporter.logStep("Grand Total : "+ExpectedGrandTotal);

	}

	/**
	 * @Summary: Method to click on Back button.
	 * @author: Praveen Namburi, @Version: Created 30-09-2016
	 */
	public void clickbtnBack_ConfirmRequestPage(){
		pageLoaded();
		pl.isDomComplete(driver);
		btnBack.syncEnabled(30);
		driver.executeJavaScript("arguments[0].click();", btnBack);
		driver.manage().timeouts().implicitlyWait(Constants.PAGE_TIMEOUT, TimeUnit.SECONDS);
	}
	
	/**
	 * @summary Method to click on checkbox- either Budget/Non-Budget and confirm the process.
	 * @author  Praveen Namburi, @Version: Created 03-10-2016
	 */
	public void clickbtnConfirm(){
		pl.isDomComplete(driver);
		if(chkBudgeted.isDisplayed()){
			click_Budgeted();
		}else if (chkNonBudgeted.isDisplayed()) {
			click_NonBudgeted();
		}else {
			btnConfirm.syncVisible(5, false);
			driver.executeJavaScript("arguments[0].click();", btnConfirm);
			driver.manage().timeouts().implicitlyWait(Constants.PAGE_TIMEOUT, TimeUnit.SECONDS);
		}
		
	}

	/**
	 * @summary Method to attach a file 
	 * @author  Lalitha Banda
	 * @date    03/10/16
	 */
	public void attachfile(WebElement typeOfAttachment,String filename){
		String fileLocation = System.getProperty("user.home") + "\\temp.txt";
		
		pageLoaded();
		
		//click the attach file link for the first item in the cart
		driver.executeJavaScript("arguments[0].click();", lstAttachFile.get(0));
		//click whether its an internal or external file depending on the element parameter
		driver.executeJavaScript("arguments[0].click();", typeOfAttachment);
		//Use the file handler class to create a text file if it doesn't already exist on the system
		FileHandler fh = new FileHandler();
		fh.createNewFile(fileLocation);
		//bypass the file system dialog box for uploading a file
		btnBrowse.sendKeys(fileLocation);
		//Click upload
		btnUpload.jsClick();

       //Handle Alert if present
	   if(AlertHandler.isAlertPresent(driver, 1)){
		   TestReporter.log("Too many files were already uploaded");
		   AlertHandler.handleAlert(driver, 1);
	   }

	}



	/**
	 * @summary Method to perform Attach File Internal/External
	 * @author  Lalitha Banda
	 * @date    03/10/16
	 */
	public void  perform_FileAttachmentProcess(String filetype,String filename){
		if(filetype.contains("External")){
			attachfile(btnExternal,filename);
		}else {
			attachfile(btnInternal,filename);
		}
	}

	/**
	 * @summary Method to verify Attached File
	 * @author  Lalitha Banda
	 * @date    03/10/16
	 */
	public void verifyAttachment(String fileName){
		// Clicking on attachment 
		pageLoaded();
		pl.isDomComplete(driver);
		driver.executeJavaScript("arguments[0].click();", lstshowAttachment.get(0));
		Sleeper.sleep(1000);
		
		// Verify Attachment 
		TestReporter.assertTrue(lnkUploadedFile.getText().contains(fileName), "Verify that the file was attached successfully");

		// Closing PopUp
		eleClose.syncVisible(5, false);
		driver.executeJavaScript("arguments[0].click();", eleClose);
		pl.isDomComplete(driver);


	}

}
