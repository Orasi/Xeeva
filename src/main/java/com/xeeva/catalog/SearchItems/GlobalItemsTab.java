package com.xeeva.catalog.SearchItems;

import java.util.List;
import java.util.ResourceBundle;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.orasi.core.interfaces.Element;
import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.Webtable;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.Sleeper;
import com.orasi.utils.TestReporter;



/**
 * @summary This page contains Global Items Tab objects
 * @author  Lalitha Banda
 * @date 	06/09/16
 */
public class GlobalItemsTab {
	private OrasiDriver driver = null;
	private ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);

	/**Page Elements**/
	@FindBy(id = "aTab2")	private Link globalItemsTab;
	@FindBy(xpath = ".//*[@id='aTab1']/span[2]") private Label localCount;
	@FindBy(xpath = ".//*[@id='gvGlobalSearch']/tbody/tr/td/span") private List<WebElement> globalItemsGrid;

	/**Constructor**/

	public GlobalItemsTab(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}

	private void pageLoaded(){
		globalItemsTab.syncVisible(20, false);
	}

	/**Page Interactions**/

	// Clicks on Global Item Tab 
	public void click_GlobalItemsTab(){
		pageLoaded();
		globalItemsTab.click();
		Sleeper.sleep(3000);
	}

	// Method to read global Item Numbers 	
	public String  getGlobalItemNumber(){
		String ItemNumber = null;
		
		click_GlobalItemsTab();
		List<WebElement> localItems= globalItemsGrid;
		if(localItems.size()>0){
			for(WebElement inputItem :localItems){
				ItemNumber = inputItem.getText();
				break;
			}
		}
		return ItemNumber;
	}

}
