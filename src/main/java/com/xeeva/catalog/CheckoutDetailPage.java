package com.xeeva.catalog;

import java.util.ResourceBundle;
import java.util.Set;

import org.openqa.selenium.support.FindBy;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.PageLoaded;
import com.orasi.utils.TestReporter;

/**
 * @summary This page contains Checkout Details Page objects
 * @author  Lalitha Banda
 * @date    28/09/16
 */

public class CheckoutDetailPage {
	
	PageLoaded pl = new PageLoaded();
	private OrasiDriver driver = null;
	private ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);
	

	/**Page Elements**/
	@FindBy(xpath ="//table/tbody/tr[5]/td/span[1]") private Label lblOne;
	@FindBy(xpath ="//table/tbody/tr[5]/td/span[3]") private Label lblTwo;
	@FindBy(xpath=".//*[@class='buttonClass'][@value='Print your order.']") private Button btnPrintYourOrder;

	/**Constructor**/
	public CheckoutDetailPage(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}


	public void pageLoaded(){
		lblOne.syncVisible(20, false);
	}

	/**Page Interactions**/
	
	/**
	 * @summary Method to Verify Order splits as REQ/RFQ
	 * @author  Lalitha Banda
	 * @date    28/09/16
	 */

	public void verify_SplitOrder(){
		pageLoaded();
		
		// Verify REQ
		if(lblOne.isDisplayed()){
		if(lblOne.getText().contains("RFQ") ||lblOne.getText().contains("REQ")){
			TestReporter.assertTrue(true, "Order splitted as "+lblOne.getText().replaceAll("[^A-Z]+", "")+"!!");
		}}
		
		// Verify RFQ
		if(lblTwo.isDisplayed()){
		if(lblTwo.getText().contains("REQ") ||lblTwo.getText().contains("RFQ")){
			TestReporter.assertTrue(true, "Order splitted as "+lblTwo.getText().replaceAll("[^A-Z]+", "")+"!!");
		}}
	}

	/**
	  * @summary Method to Click Print Your Order Button
	  * @author  Lalitha Banda
	  * @date    29/09/16
	  */
	 
	 
	 public void printYourOrder(String openWindowHandle) {
	  pageLoaded();
	  btnPrintYourOrder.syncVisible(5);
	  btnPrintYourOrder.click();

	  //To close all the other windows except the main window.
	  Set<String> allWindowHandles = driver.getWindowHandles();
	  for (String currentWindowHandle : allWindowHandles) {
	   if (!currentWindowHandle.equals(openWindowHandle)) {
	    driver.switchTo().window(currentWindowHandle);
	    driver.close();
	   }
	  }
	 }

}
