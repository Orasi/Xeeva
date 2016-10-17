package com.xeeva.marketPlace;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.util.ResourceBundle;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Checkbox;
import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.Listbox;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.Constants;
import com.orasi.utils.FileHandler;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.PageLoaded;
import com.orasi.utils.TestReporter;
import com.orasi.utils.date.SimpleDate;

/**
 * @summary This page contains Supplier Page objects
 * @author  Lalitha Banda
 * @version 14/10/2016
 *
 */

public class Supplier {

	PageLoaded pl = new PageLoaded();
	private OrasiDriver driver = null;
	private ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);

	//**Page Elements**//*

	@FindBy(xpath = "//*[@id='step2']/a")	private Button btnOpportunitiesTab;
	@FindBy(linkText = "No Records found..")	private Link lnkNoRecords;
	@FindBy(xpath = "//*[@class='ng-valid ng-dirty ng-valid-parse ng-touched']/option[1]")	private Listbox lstRFQCode;
	@FindBy(xpath = "//*[@class='ng-valid ng-dirty ng-valid-parse ng-touched']/option[2]")	private Listbox lstBuyer;
	@FindBy(xpath = "//*[@type='text']")	private Textbox txtSearchInput;
	@FindBy(css=".btn-u.btn-u-sm.rounded.btn-info") private Button btnSearch;

	@FindBy(xpath = "//*[@id='Revenue_fieldset']/div[2]/div[4]/div[2]")	private Button btnRFQ;
	@FindBy(xpath = "//*[@id='rfq']//div[4]/form/div[2]/div[1]/label/i")	private Checkbox chkSelectAll;
	@FindBy(xpath = "//*[@id='rfq']//div[1]/div[3]/button[1]")	private Button btnRespondtoRFQ;

	@FindBy(xpath = "//*[@id='divAllContainer']/div[1]/div[2]/h3")	private Label lblResponsetoRFQ;
	@FindBy(id="Price_Valid")	private Textbox txtValidDate;
	@FindBy(id="UnitPrice")	private Textbox txtUnitPrice;
	@FindBy(id="MinimumQuantity")	private Textbox txtQuantity;
	@FindBy(id="txtLeadTime")	private Textbox txtLeadTime;
	@FindBy(id="FreightTermsID")	private Listbox lstFreightID;
	@FindBy(xpath = "//*[@ng-click='OnfileAttach()']")	private Button btnAttachFile;

	@FindBy(id="btnNxt")	private Button btnNext;
	@FindBy(xpath = "//*[@id='divAllContainer']/div[1]/div[3]/span/button[2]")	private Button btnSubmitResponse;
	@FindBy(xpath = "//*[@id='divAllContainer']/div[2]/h5/span[2]")	private Label lblDueDate;
	@FindBy(xpath = "//*[@id='toast-container']/div/div[2]")	private Label lblInfoMsg;
	@FindBy(xpath = "//*[@id='divAllContainer']/div[1]/div[2]/h3")	private Label lblResponse;


	//Supplier Logout
	@FindBy(xpath = "//*[@id='step7']/a/span")	private Button btnSupplier;
	@FindBy(xpath = "//*[@id='step7']/ul/li[5]/a")	private Button btnLogOut;
	@FindBy(linkText = "Skip")	private Link lnkSkip;



	//**Constructor**//*

	public Supplier(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}

	private void pageLoaded(){

	}

	//**Page Interactions**//*


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
	
	/**
	 * @summary Method for click_OpportunitiesTab
	 * @author  Lalitha Banda
	 * @version 14/10/2016
	 *
	 */
	public void click_OpportunitiesTab(){
		pl.isDomComplete(driver);
		lnkSkip.syncVisible(2, false);
		try{lnkSkip.click();}catch(Exception e){ driver.executeJavaScript("arguments[0].click();", lnkSkip);}
		btnOpportunitiesTab.syncVisible(2);
		try{btnOpportunitiesTab.click();}catch(Exception e){driver.executeJavaScript("arguments[0].click();", btnOpportunitiesTab);}
	}

	
	/**
	 * @summary Method to perform_RFQSearch
	 * @author  Lalitha Banda
	 * @version 14/10/2016
	 *
	 */
	public void perform_RFQSearch(String input){

		if(lnkNoRecords.isDisplayed()){
			try{lstRFQCode.click();}catch(Exception e){ driver.executeJavaScript("arguments[0].click();", lstRFQCode);}
			txtSearchInput.sendKeys(input);
			try{btnSearch.click();}catch(Exception e){ driver.executeJavaScript("arguments[0].click();", btnSearch);}
		}else{
			pl.isDomComplete(driver);
			try{btnRFQ.click();}catch(Exception e){ driver.executeJavaScript("arguments[0].click();", btnRFQ);}
			chkSelectAll.syncVisible(3);
			try{chkSelectAll.click();}catch(Exception e){ driver.executeJavaScript("arguments[0].click();", chkSelectAll);}
			try{btnRespondtoRFQ.click();}catch(Exception e){ driver.executeJavaScript("arguments[0].click();", btnRespondtoRFQ);}
		}
	}

	
	/**
	 * @summary Method to get DueDate
	 * @author  Lalitha Banda
	 * @version 14/10/2016
	 *
	 */
	public String getDueDate(){
		String date = lblDueDate.getText();
		return date;
	}

	
	/**
	 * @summary Method to handle Date picker 
	 * @author  Lalitha Banda
	 * @version 14/10/2016
	 *
	 */
	public void datePicker() throws ParseException{

		SimpleDate sd = new SimpleDate();
		long days = sd.daysOut(getDueDate().toString());
		WebElement ele = driver.findElement(By.xpath(".//*[@id='Price_Valid']"));
		ele.click();
		for(int i=1; i<=days;i++){
			ele.sendKeys(Keys.ARROW_RIGHT);
		}
		ele.sendKeys(Keys.ENTER);
	}

	
	/**
	 * @summary Method to submit_Response RFQ
	 * @author  Lalitha Banda
	 * @version 14/10/2016
	 *
	 */
	public void submit_Response(String price,String quantity,String leadTime,
			String freightID) throws ParseException{
		pl.isDomComplete(driver);
		datePicker();
		txtUnitPrice.set(price);
		txtQuantity.set(quantity);
		txtLeadTime.set(leadTime);
		lstFreightID.select(freightID);
		try{lblResponse.click();}catch(Exception e){ driver.executeJavaScript("arguments[0].click();", lblResponse);}
		if(btnNext.isDisplayed()){
			btnNext.click();
			pl.isDomComplete(driver);
			datePicker();
			txtUnitPrice.set(price);
			txtQuantity.set(quantity);
			txtLeadTime.set(leadTime);
			lstFreightID.select(freightID);
			try{lblResponse.click();}catch(Exception e){ driver.executeJavaScript("arguments[0].click();", lblResponse);}
		}else{
		}
		btnSubmitResponse.syncVisible(5, false);
		try{btnSubmitResponse.click();}catch(Exception e){ driver.executeJavaScript("arguments[0].click();", btnSubmitResponse);}
		verify_RFQResponse();
	}

	
	/**
	 * @summary Method to verify_RFQ Response
	 * @author  Lalitha Banda
	 * @version 14/10/2016
	 *
	 */
	public void verify_RFQResponse(){
		WebDriverWait wait = new WebDriverWait(driver,5);
		wait.until(ExpectedConditions.
				visibilityOfElementLocated(By.xpath("//*[@id='toast-container']/div/div[2]")));
		String getRFQResponseMessage = lblInfoMsg.getText();
		TestReporter.logStep("Message after responding to RFQ : "+ getRFQResponseMessage);
		TestReporter.assertTrue(getRFQResponseMessage.contains("RFQ Response") , 
				"RFQ Response submitted successfully.");
	}

}

