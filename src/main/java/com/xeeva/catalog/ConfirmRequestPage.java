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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Checkbox;
import com.orasi.core.interfaces.Element;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.AlertHandler;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.PageLoaded;
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
	@FindBy(linkText ="Catalog_ExternalFileAttachment.xlsx") private Link lnkUploadedFile;
	@FindBy(id ="ajaxUploadButton") private Button btnUpload;
	@FindBy(xpath ="//*[@id='lbl_uploadfile']/div/a") private WebElement btnBrowse;
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
		chkNonBudgeted.syncVisible(3, false);
		chkNonBudgeted.click();
	}

	/**
	 * @summary Method to click on  budgeted
	 * @author  Lalitha Banda
	 * @date    28/09/16
	 */
	public void click_Budgeted(){
		pageLoaded();
		chkBudgeted.syncVisible(3, false);
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
			btnConfirm.syncVisible(2, false);
			driver.executeJavaScript("arguments[0].click();", btnConfirm);
			driver.manage().timeouts().implicitlyWait(Constants.PAGE_TIMEOUT, TimeUnit.SECONDS);
		}
		
	}

	/**
	 * @summary Method to attach a file 
	 * @author  Lalitha Banda
	 * @date    03/10/16
	 */
	public boolean attachfile(WebElement Element,String filename){
		boolean statusFlag = false;

		pageLoaded();
		driver.executeJavaScript("arguments[0].click();", lstAttachFile.get(0));
		driver.executeJavaScript("arguments[0].click();", Element);
		btnBrowse.click();
		try {Thread.sleep(3000);}catch (InterruptedException e){ e.printStackTrace();}

		String fileLocation = System.getProperty("user.dir") + "\\src\\test\\resources\\datasheets\\" + filename+".xlsx";
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

		//Handle Alert if present
		if(AlertHandler.isAlertPresent(driver, 6)){
			AlertHandler.handleAlert(driver, 6);
		}
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
		if(filetype.contains("External")?attachfile(btnExternal,filename):false);
		if(filetype.contains("Internal")?attachfile(btnInternal,filename):false);
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
		// Verify Attachment 
		if(lnkUploadedFile.getText().contains(fileName)){
			TestReporter.assertTrue(true, "File Attached Sucessfully!!");
		}
		// Closing PopUp
		if(AlertHandler.isAlertPresent(driver, 5)){
			AlertHandler.handleAlert(driver, 5);
		}else{
			eleClose.syncVisible(5, false);
			driver.executeJavaScript("arguments[0].click();", eleClose);	
		}

	}

}
