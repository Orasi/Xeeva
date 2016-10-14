package com.xeeva.review;

import java.util.List;
import java.util.ResourceBundle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Checkbox;
import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.PageLoaded;
import com.xeeva.approvals.ApprovalsPage;

public class ReviewPage {

	PageLoaded pl = new PageLoaded();
	private OrasiDriver driver = null;
	private ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);

	//**Page Elements**//*

	@FindBy(xpath = "//*[@id='gooeymenu4']/li[7]")	private Link reviewTab;
	@FindBy(xpath = "//*[@id='Resultpanel']/div[1]/table/tbody/tr[1]/td")	private Label lblFilterCriteria;
	@FindBy(id="btnSearch")	private Button btnSearch;
	@FindBy(xpath = "//*[@title='Edit RFQ Detail']")	private List<WebElement> lstEditRFQDetail;
	@FindBy(className="checkboxPlace")	private Checkbox chkDetails;
	@FindBy(id="btnSave")	private Button btnApprove;
	@FindBy(id="btnBack")	private Button btnBack;
	@FindBy(id="txtRFQNumber")	private Textbox txtRFQNumber;
	//**Constructor**//*

	public ReviewPage(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}

	//**Page Interactions**//*

	public void reviewRFQ(String inputString){
		pl.isDomComplete(driver);
		txtRFQNumber.syncVisible(5);
		txtRFQNumber.sendKeys(inputString);
		driver.executeJavaScript("arguments[0].click();",btnSearch);
		driver.executeJavaScript("arguments[0].click();",lstEditRFQDetail.get(0));
		driver.executeJavaScript("arguments[0].click();",chkDetails);
		driver.executeJavaScript("arguments[0].click();",btnApprove);
		//Handle Alert if present
		/**Navigating to Approvals Page to Handle Alert * */
		ApprovalsPage aPage = new ApprovalsPage(driver);
		aPage.handleAlert();
	}



}
