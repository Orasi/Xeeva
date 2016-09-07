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
import com.orasi.utils.TestReporter;



/**
 * @summary This page contains Local Items Tab objects
 * @author  Lalitha Banda
 * @date    06/09/16
 */
public class LocalItemsTab {
	private OrasiDriver driver = null;
	private ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);

	/**Page Elements**/
	@FindBy(id = "aTab1")	private Link localItemsTab;
	@FindBy(xpath = ".//*[@id='aTab1']/span[2]") private Label localCount;
	@FindBy(xpath = ".//*[@id='gvLocalSearch']/tbody/tr/td/span") private  List<WebElement> localItemsGrid;



	/**Constructor**/

	public LocalItemsTab(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}

	private void pageLoaded(){
		localItemsTab.syncVisible(10, false);
	}

	/**Page Interactions**/

	// Clicks on local Item Tab 
	public void click_localItemsTab(){
		localItemsTab.click();
	}

	// Method to read Local Item Numbers 	
	public String  getLocalItemNumber(){
		String ItemNumber = null;

		click_localItemsTab();
		List<WebElement> localItems= localItemsGrid;
		if(localItems.size()>0){
			for(WebElement inputItem :localItems){
				ItemNumber = inputItem.getText();
				break;
			}
		}else{
			TestReporter.logStep("No Records Found !!");
		}

		return ItemNumber;
	}

}
