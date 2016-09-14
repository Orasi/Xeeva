package com.xeeva.catalog;

import java.util.ResourceBundle;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Element;
import com.orasi.core.interfaces.Listbox;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;


/**
 * @summary This page contains CostCenter Page objects
 * @author  Lalitha Banda
 * @date    06/09/16
 */
public class CostCenterPage {
	
	private OrasiDriver driver = null;
	private ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);

	/**Page Elements**/
	@FindBy(className ="costCentername") private Element costCenternamePage;	
	@FindBy(xpath="//*[@id='customfa']/tbody") private WebElement tblCostCenterInfo;
	@FindBy(id ="ddlApplyCC") private Listbox lstCostCentername;
	@FindBy(className ="fa fa-calendar cursor-pointer font16px") private Button btnCalender;
	@FindBy(id ="txtRequiredby") private Textbox txtRequiredBy;
		
	@FindBy(xpath="//td[2]/a[1]/input") private Button btnSaveCartCC;
	@FindBy(xpath="//tr[2]/td[2]/a[2]/input") private Button btnShopMoreItemsCC;
	@FindBy(xpath="//*[@id='btnCheckOut']/input") private Button btnContinueCheckoutCC;

	
	/**Constructor**/
	public CostCenterPage(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}

	
	public void pageLoaded(){
		costCenternamePage.syncVisible(15, false);
	}

	/**Page Interactions**/


}
