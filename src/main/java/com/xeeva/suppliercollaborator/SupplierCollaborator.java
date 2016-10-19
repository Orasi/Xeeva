package com.xeeva.suppliercollaborator;

import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Checkbox;
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

public class SupplierCollaborator {

	PageLoaded pl = new PageLoaded();
	private OrasiDriver driver = null;
	private ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);

	//**Page Elements**//*

	@FindBy(linkText = "Supplier Collaborator")	private Link supplierCollaboratorTab;
	@FindBy(className="DataGridHeader borderBottomE0E0E1")	private Label lblFilterCriteria;
	@FindBy(className="DataGridHeader border1pxe0e0e0")	private Label lblRecPurchaseOrder;
	@FindBy(id="btnReset")	private Button btnReset;
	@FindBy(id="btnSearch")	private Button btnSearch;
	@FindBy(xpath = ".//*[@id='ddcl-ddlOrderStatus']/span") private WebElement lstOrderStatus;
	@FindBy(xpath=".//*[@id='ddcl-ddlOrderStatus-ddw']//div[1]/label") private List<WebElement> lstOrderStatuses;
	@FindBy(xpath=".//*[@id='ddlOrderStatus']/option[@title='Acknowledged']") private WebElement lstAcknowledged;
	@FindBy(css=".fa.fa-exchange.font16px.cursor-pointer.receiveUnreceive")	private List<WebElement> lstReceive;

	//Receive Purchase Order
	@FindBy(xpath="//*[@id='tblSCMultipleRecieving']//tr[1]/th[1]//span")	private Checkbox chkReceiveAll;
	@FindBy(id="txtPkgSlipNumberTop")	private Textbox txtPkgSlipNumber;
	@FindBy(xpath="//*[@id='tblSCMultipleRecieving']/tbody/tr[1]/th[11]/label/span")	private Checkbox chkCopyToAll;
	@FindBy(id="btnSaveMulti")	private Button btnSave;
	@FindBy(id="fancybox-close")	private Button btnClose;
	//@FindBy(xpath="//*[@id='tblSCMultipleRecieving']/tbody/tr/td[2]/span")	private Label lblPONumber;
	@FindBy(xpath="//*[@id='tblSCMultipleRecieving']/tbody/tr[2]/td[2]/span")	private Element lblPONumber;
	@FindBy(id="txtPO")	private Textbox txtPONumber;
	@FindBy(xpath="//*[@id='tblSCList']/tbody/tr[2]/td[18]/div/span")	private Element elemousehover;
	


	//**Constructor**//*

	public SupplierCollaborator(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}

	private void pageLoaded(){
		lblFilterCriteria.syncVisible(5);
	}

	//**Page Interactions**//*


	/**
	 * @summary Method for clicking Supplier Collaborator Tab 
	 * @author  Lalitha Banda
	 * @date    18/10/16
	 */
	public void click_SupplierCollaboratorTab(){
		pl.isDomComplete(driver);
		driver.executeJavaScript("arguments[0].click();",supplierCollaboratorTab);
		driver.manage().timeouts().implicitlyWait(Constants.PAGE_TIMEOUT, TimeUnit.SECONDS);
	}


	/**
	 * @summary Method to perform Search
	 * @author  Lalitha Banda
	 * @date    18/10/16
	 */
	public void performSearch(){
		btnReset.syncVisible(3);
		btnReset.jsClick();
		try{btnSearch.click();}catch(Exception e){driver.executeJavaScript("arguments[0].click();",btnSearch);}
	}

	/**
	 * @summary Method to select Receive MultiplePOs
	 * @author  Lalitha Banda
	 * @date    18/10/16
	 */
	public void select_ReceiveMultiplePOs(){

		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.elementToBeClickable(lstReceive.get(0)));
		driver.findElement(By.xpath("//*[@class='viewList']/ul/li[2]/a")).jsClick();
	}


	/**
	 * @summary Method to get PONumber
	 * @author  Lalitha Banda
	 * @date    18/10/16
	 */
	public String getPONumber(){
		String PoNum = lblPONumber.getText().trim();
		TestReporter.log("PO Number : "+PoNum);
		return PoNum;
	}


	/**
	 * @summary Method to perform Receive Purchase Order
	 * @author  Lalitha Banda
	 * @date    18/10/16
	 */
	public void perform_RecPurchaseOrder(String input){
		lblRecPurchaseOrder.isEnabled();
		try{chkReceiveAll.click();}catch(Exception e){driver.executeJavaScript("arguments[0].click();",chkReceiveAll);}
		TestReporter.logStep("PO Number :"+getPONumber());
		List<WebElement> rows = driver.findElements(By.xpath("//*[@id='tblSCMultipleRecieving']/tbody/tr"));
		int totalRows = rows.size();
		for(int row=2;row<=totalRows;row++){
			String getOpenOrder = driver.findElement(By.xpath("//*[@id='tblSCMultipleRecieving']/tbody/tr["+row+"]/td[8]")).getText().trim();
			WebElement enterReceiveOrder = driver.findElement(By.xpath("//*[@id='tblSCMultipleRecieving']/tbody/tr["+row+"]/td[7]/input"));
			enterReceiveOrder.sendKeys(getOpenOrder);
		}
		txtPkgSlipNumber.set(input);
		try{chkCopyToAll.click();}catch(Exception e){driver.executeJavaScript("arguments[0].click();",chkCopyToAll);}
		if (AlertHandler.isAlertPresent(driver, 6)) {
			AlertHandler.handleAlert(driver, 6);
		}
		try{btnSave.click();}catch(Exception e){driver.executeJavaScript("arguments[0].click();",btnSave);}
		try{btnClose.click();}catch(Exception e){driver.executeJavaScript("arguments[0].click();",btnClose);}
	}

	/**
	 * @summary Method to perform search_PONumber
	 * @author  Lalitha Banda
	 * @date    18/10/16
	 */
	public void search_PONumber(String poNum){
		driver.executeJavaScript("arguments[0].click();",supplierCollaboratorTab);
		pl.isDomComplete(driver);
		btnReset.syncHidden(5,false);
		driver.executeJavaScript("arguments[0].click();",btnReset);
		btnSearch.syncHidden(3, false);
		txtPONumber.syncVisible(10, false);
		txtPONumber.click();
		txtPONumber.sendKeys(poNum);
		Sleeper.sleep(2000);
		driver.executeJavaScript("arguments[0].click();",btnSearch);
	}
	/**
	 * @summary Method for grabbing Supplier email 
	 * @author  Lalitha Banda
	 * @date    18/10/16
	 */
	public String getSupplierEmail() {
		String returnValue = null;
		Sleeper.sleep(5000);
		WebDriverWait wait = new WebDriverWait(driver, 15);
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='tblSCList']/tbody/tr[2]/td[22]/div/span")));
		element.click();
		Sleeper.sleep(4000);
		WebDriverWait wait1 = new WebDriverWait(driver, 15);
		WebElement element1 = wait1.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='tblSCList']/tbody/tr[2]/td[18]/div/span")));
		element1.click();
		Sleeper.sleep(5000);
		List<WebElement> totTable = driver.findElements(By.cssSelector(".DataGridrowa>td"));
		TestReporter.logStep("size : "+totTable.size());
		for(WebElement inputTable :totTable){
			if(inputTable.getText().contains("@")){
				returnValue =inputTable.getText();
				break;
			}
		}
		return returnValue;
	}
}
