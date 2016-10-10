package com.xeeva.catalog.SearchItems;

import java.util.List;
import java.util.ResourceBundle;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.PageLoaded;
import com.orasi.utils.Sleeper;
import com.orasi.utils.TestReporter;

/**
 * @summary This page contains BPO Items Tab objects
 * @author  Lalitha Banda
 * @date    06/09/16
 */
public class BPOItemsTab {
	PageLoaded pl = new PageLoaded();
	private OrasiDriver driver = null;
	private ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);

	/**Page Elements**/
	@FindBy(id = "aTab3")	private Link bpoItemsTab;
	@FindBy(xpath = ".//*[@id='gvBPOSearch']/tbody/tr/td/span") private  List<WebElement> bpoItemsGrid;


	/**Constructor**/

	public BPOItemsTab(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}

	private void pageLoaded(){
		bpoItemsTab.syncVisible(10, false);
	}

	/**Page Interactions**/


	/**
	 * @summary Clicks on BPO Item Tab
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/
	public void click_bpoItemsTab(){
		pageLoaded();
		//bpoItemsTab.click();
		driver.executeJavaScript("arguments[0].click();", bpoItemsTab);
		pl.isDomComplete(driver);
	}

	/**
	 * @summary Method to read bpo Item Numbers 	
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/

	public String  getBPOItemNumber(){
		String ItemNumber = null;
		click_bpoItemsTab();
		List<WebElement> bpoItems= bpoItemsGrid;
		if(bpoItems.size()>0){
			for(WebElement inputItem :bpoItems){
				ItemNumber = inputItem.getText();
				break;
			}
		}else{
			TestReporter.assertFalse(!(bpoItems.size()>0), "Global Items Count Empty!!");
		}
		return ItemNumber;
	}

}

