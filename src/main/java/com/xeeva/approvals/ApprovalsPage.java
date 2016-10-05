package com.xeeva.approvals;

import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.impl.ElementImpl;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.AlertHandler;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.PageLoaded;
import com.orasi.utils.Sleeper;
import com.orasi.utils.TestReporter;

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
	@FindBy(xpath= "//*[@id='divREQList']/div/input[1]")private Button btnApproveSelected;
	@FindBy(xpath = "//*[@id='divREQList'][@class='Datagridborder']/tbody/tr") private List<WebElement> ReqDetailsGrid;
	@FindBy(xpath = ".//*[@for='chkApproveAll'][@class='css-label']") private Label chkApprove;
	@FindBy(xpath = ".//*[@id='accChangeID']") private Link lnkGLCCEdit;
	
	//**Constructor**//*

	public ApprovalsPage(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}

	private void pageLoaded(){
		btnApproveSelected.syncVisible(20, false);
	}

	//**Page Interactions**//*

	/**
	 * @summary  Method to clcik on Approvals Tab
	 * @author Lalitha Banda
	 * @date  04/10/16
	 **/
	public void click_ApprovalsTab(){
		approvalsTab.syncVisible(30, false);
		driver.executeJavaScript("arguments[0].click();", approvalsTab);
		driver.manage().timeouts().implicitlyWait(Constants.PAGE_TIMEOUT, TimeUnit.SECONDS);
	}

	/**
	 * @summary  Method to click on REQ Tab
	 * @author Lalitha Banda
	 * @date  05/10/16
	 **/
	public void click_REQ(){
		// Clicking on REQ 
		driver.findElement(By.xpath(ReqRow+"["+selectOrderToApprove()+"]//td[7]/div/a")).click();
	}
	
	/**
	 * @summary  Method to click on Approve CheckBox
	 * @author Lalitha Banda
	 * @date  05/10/16
	 **/
	public void click_Approve(){
		pageLoaded();
		pl.isDomComplete(driver);
		chkApprove.click();
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
	 * @summary  Method to select an order to approve
	 * @author Lalitha Banda
	 * @date  04/10/16
	 * @return rowNumber
	 **/
	public int selectOrderToApprove(){
		pageLoaded();
		pl.isDomInteractive(driver);
		int reqRecordsCount = ReqDetailsGrid.size();
		System.out.println("No of REQ Records : "+reqRecordsCount);
		int selectedRow = 0; boolean rowSelected = false;
		for(int iterator=1;iterator<=reqRecordsCount;iterator++){
			System.out.println(driver.findElement(By.xpath(ReqRow+"["+iterator+"]/td[14]")).getText().trim());
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
		}else{
			TestReporter.assertTrue(false, "No records found with Hold status!!");
		}
	}
	
	// Method to Handle Alert 
	public void handleAlert(){
		String alertMsg = null;
		AlertHandler al = new AlertHandler();
		if(al.isAlertPresent(driver, 3)){
			Alert alert = driver.switchTo().alert();
			alertMsg = alert.getText().trim();
			TestReporter.log(alertMsg);
			alert.accept();
		}
		//TestReporter.assertTrue(alertMsg.equalsIgnoreCase("This action will call the requisition to be placed on hold. Do you want to proceed?"), "Alert Handled");
	}
	
	
	
}
