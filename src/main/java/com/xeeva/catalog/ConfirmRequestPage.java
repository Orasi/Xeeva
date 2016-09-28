package com.xeeva.catalog;

import java.util.List;
import java.util.ResourceBundle;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Checkbox;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
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
	

}
