package com.xeeva.marketPlace;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

import org.openqa.selenium.Keys;
import org.openqa.selenium.support.FindBy;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Checkbox;
import com.orasi.core.interfaces.Element;
import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.Constants;
import com.orasi.utils.FileHandler;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.PageLoaded;
import com.orasi.utils.Sleeper;
import com.orasi.utils.TestReporter;

/**
 * @summary This page contains Invoices Page objects
 * @author  Praveen Namburi
 * @version 17/10/2016
 */
public class InvoicesPage {

		PageLoaded pl = new PageLoaded();
		private OrasiDriver driver = null;
		private ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);
	
		//**Page Elements**//*
		@FindBy(partialLinkText = "Invoices") private Link lnkInvoices;
		@FindBy(xpath ="//div[@id='Revenue_new']/div[1]/div/div/div[2]/div[1]/div[2]/button") 
		private Button btnCreateNewInvoice;
		@FindBy(xpath ="//div[@id='Revenue_new']/div[1]/div/div/div[1]/div[1]/form/section/label[2]/input") 
		private Textbox txtInvoiceNumber;
		@FindBy(xpath ="//button[@class='btn-u btn-u-sm rounded btn-info padding-left-right-3px']")  
		private Button btnShowItems;
		
		// View/Create/Edit Memo page elements.
		@FindBy(xpath="//div[@id='Revenue_new']/div[1]/div/div/div[2]/div[4]/div[1]") 
		private Element lblSelectPurchaseOrder;
		@FindBy(css=".Sup_Revenue_row_head>div>label>input") private Checkbox chkHeaderLinePO;
		@FindBy(css=".btn-u.btn-u-blue.rounded.btn-u-sm.margin-right-5") private Button btnCreateInvoice;
		@FindBy(name="InvoiceNumber") private Textbox txtInvoiceNum;
		@FindBy(id="Invoice_Date") private Textbox txtInvoiceDate;
		@FindBy(xpath = "//*[@ng-click='OnfileAttach()']") private Button btnAttachFile;
		@FindBy(xpath="//button[@ng-click='ValidateInvoiceHeader(InvoiceHeader,true,1)']")
		private Button btnInvoiceNext;
		@FindBy(xpath="//*[@id='InvoiceItemDetailsForm']/div[1]/div[2]/button[2]")
		private Button btnSubmit;
		@FindBy(xpath="//*[@id='InvoiceItemDetailsForm']/div[3]/div[1]/div[2]/label[2]")
		private Label lblReceivedQuantity;
		@FindBy(id="InvoiceQty") private Textbox txtInvoiceQty;
		
		//Supplier Logout
		@FindBy(xpath = "//*[@id='step7']/a/span")	private Button btnSupplier;
		@FindBy(xpath = "//*[@id='step7']/ul/li[5]/a")	private Button btnLogOut;
		@FindBy(linkText = "Skip")	private Link lnkSkip;
	
		//**Constructor**//*
		public InvoicesPage(OrasiDriver driver){
			this.driver = driver;
			ElementFactory.initElements(driver, this);
		}
	
		private void pageLoaded(){
			pl.isDomComplete(driver);
			lnkInvoices.syncVisible(10, false);
		}
	
		//**Page Interactions**//*
	
		/**
		 * @Summary: Method to click on Invoices link.
		 * @author: Praveen Namburi, @Version: Created 17-10-2016
		 */
		public void clickInvoices(){
			pageLoaded();
			driver.executeJavaScript("arguments[0].click();",lnkInvoices);
		}
		
		/**
		 * @Summary: Method to click on 'Create new Invoice' button.
		 * @auhtor: Praveen Namburi, @Version: Created 17-10-2016
		 */
		public void clickbtnCreateNewInvoice(){
			pl.isDomComplete(driver, 5);
			btnCreateNewInvoice.syncEnabled(10, false);
			btnCreateNewInvoice.jsClick();
		}
		
		/**
		 * @Summary: Method to Search with PO Number.
		 * @author: Praveen Namburi, @Version: Created 17-10-2016
		 * @param PO_Number
		 */
		public void searchWithPO(String PO_Number){
			pl.isDomComplete(driver,5);
			txtInvoiceNumber.syncVisible(10, false);
			driver.executeJavaScript("arguments[0].click();",txtInvoiceNumber);
			txtInvoiceNumber.sendKeys(PO_Number);
			driver.setElementTimeout(5);
			btnShowItems.syncHidden(10,false);
			driver.setElementTimeout(Constants.ELEMENT_TIMEOUT);
			btnShowItems.jsClick();
		}
		
		/**
		 * @Summary: Method to select Purchase Order to CreateInvoice.
		 * @author: Praveen Namburi, @Version: Created 17-10-2016
		 * @param PO_Number
		 */
		public void selectPurchaseOrderToCreateInvoice(String PO_Number){
			searchWithPO(PO_Number);
			pl.isDomComplete(driver, 5);
			lblSelectPurchaseOrder.syncVisible(10, false);
			lblSelectPurchaseOrder.jsClick();

		}
		
		/**
		 * @Summary: Method to select Item to CreateInvoice.
		 * @author: Praveen Namburi, @Version: Created 18-10-2016
		 */
		public void selectItemToCreateInvoice(){
			pl.isDomComplete(driver, 5);
			chkHeaderLinePO.syncHidden(8, false);
			chkHeaderLinePO.jsClick();
			btnCreateInvoice.syncVisible(5, false);
			btnCreateInvoice.jsClick();
			
		}
		
		/**
		 * @Summary: Method to View/Create/Edit Memo and set Invoice Information.
		 * @author: Praveen Namburi, @Version: Created 18-10-2016
		 */
		public void createMemoAndSubmitInvoice(String PO){
			pl.isDomComplete(driver, 5);
			txtInvoiceNum.syncHidden(6, false);
			String newInvoiceNum = PO.replace("MJ", "");
			System.out.println("Invoice number: " + newInvoiceNum);
			txtInvoiceNum.sendKeys("IMV_" + newInvoiceNum);
			txtInvoiceDate.syncEnabled();
			txtInvoiceDate.jsClick();
			txtInvoiceDate.sendKeys(Keys.ARROW_RIGHT);
			txtInvoiceDate.sendKeys(Keys.ENTER);
			attachfile(); //Attach a file
			pl.isDomComplete(driver, 5);
			btnInvoiceNext.syncHidden(6, false);
			btnInvoiceNext.jsClick();
			btnSubmit.syncVisible(10, false);
			pl.isDomComplete(driver,5);
			txtInvoiceQty.syncVisible(5);
			driver.executeJavaScript("arguments[0].click();",txtInvoiceQty);
			txtInvoiceQty.clear();
			txtInvoiceQty.sendKeys(getReceivedQuantity());
			btnSubmit.syncEnabled(5, false);
			btnSubmit.jsClick();
		}
		
		/**
		 * @Summary: Method capture Received quantity.
		 * @author: Praveen Namburi, @Version: Created 18-10-2016
		 */
		public String getReceivedQuantity(){
			lblReceivedQuantity.syncVisible(5);
			String receivedQty = lblReceivedQuantity.getText().trim();
			TestReporter.log("Captured Received quantity: " + receivedQty);
			return receivedQty;
		}
		
		/**
		 * @summary Method to attach a file.
		 * @author: Praveen Namburi, @Version: Created 18-10-2016
		 * @return
		 */
		public boolean attachfile() {
			boolean statusFlag = false;
			String fileLocation = System.getProperty("user.home") + "\\temp.txt";
			
			pl.isDomComplete(driver);
			btnAttachFile.jsClick();
			Sleeper.sleep(3000);
			//Use the file handler class to create a text file 
			//if it doesn't already exist on the system
			FileHandler fh = new FileHandler();
			fh.createNewFile(fileLocation);
			
			try {
				Robot robot = new Robot();
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);

			} catch (AWTException e) {e.printStackTrace();}

			statusFlag = true;
			return statusFlag;
		}
		
		/**
		 * @summary Method for Supplier Logout Functionality
		 * @author  Lalitha Banda
		 * @version 14/10/2016
		 *
		 */
		public void supplierLogout(){
			try{btnSupplier.click();}catch(Exception e){ driver.executeJavaScript("arguments[0].click();", btnSupplier);}
			try{btnLogOut.click();}catch(Exception e){ driver.executeJavaScript("arguments[0].click();", btnLogOut);}
		}
				
 }


