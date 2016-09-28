package com.xeeva.catalog;

import java.util.ResourceBundle;

import org.openqa.selenium.support.FindBy;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Checkbox;
import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.PageLoaded;
import com.orasi.utils.TestReporter;

public class CheckoutDetailPage {
	
	PageLoaded pl = new PageLoaded();
	private OrasiDriver driver = null;
	private ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);
	

	/**Page Elements**/
	@FindBy(xpath ="//table/tbody/tr[5]/td/span[1]") private Label lblREQ;
	@FindBy(xpath ="//table/tbody/tr[5]/td/span[3]") private Label lblRFQ;

	/**Constructor**/
	public CheckoutDetailPage(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}


	public void pageLoaded(){
		lblREQ.syncVisible(20, false);
	}

	/**Page Interactions**/
	
	//Method to click on non budgeted
	public void verify_SplitOrder(){
		pageLoaded();
		// Verify REQ
		if(lblREQ.isDisplayed()){
		if(lblREQ.getText().contains("REQ")){
			TestReporter.assertTrue(true, "Order splitted as REQ!!");
		}}
		
		// Verify RFQ
		if(lblRFQ.isDisplayed()){
		if(lblRFQ.getText().contains("RFQ")){
			TestReporter.assertTrue(true, "Order splitted as RFQ!!");
		}}
	}

	

}
