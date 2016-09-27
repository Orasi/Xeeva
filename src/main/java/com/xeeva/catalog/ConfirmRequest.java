package com.xeeva.catalog;

import java.util.ResourceBundle;

import org.openqa.selenium.support.FindBy;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Checkbox;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.PageLoaded;

public class ConfirmRequest {
	
	PageLoaded pl = new PageLoaded();
	private OrasiDriver driver = null;
	private ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);
	String xpath = ".//*[@id='customfa']/tbody/tr/td/select";
	

	/**Page Elements**/
	@FindBy(id ="btnCheckOutConfirm") private Button btnConfirm;
	@FindBy(xpath =".//*[@for='chkNonBudgeted'][@class='css-label']") private Checkbox chkNonBudgeted;	
	@FindBy(id ="chkBudgeted") private Checkbox chkBudgeted;	
	

	/**Constructor**/
	public ConfirmRequest(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}


	public void pageLoaded(){
		btnConfirm.syncVisible(20, false);
	}

	/**Page Interactions**/
	
	//Method to click on non budgeted
	public void click_NonBudgeted(){
		pl.isDomComplete(driver);
		chkNonBudgeted.syncVisible(5, false);
		chkNonBudgeted.click();
	}

	//Method to click on  budgeted
	public void click_Budgeted(){
		pageLoaded();
		chkBudgeted.click();
	}
	
	// Method to click on confirm
	public void click_Confirm(){
		click_Budgeted();
		btnConfirm.syncVisible(5, false);
		btnConfirm.click();
	}

}
