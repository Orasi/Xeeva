package com.xeeva.catalog.SearchItems;

import java.util.List;
import java.util.ResourceBundle;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.PageLoaded;
import com.orasi.utils.Sleeper;
import com.orasi.utils.TestReporter;

/**
 * @summary This page contains Global Items Tab objects
 * @author  Lalitha Banda
 * @date 	06/09/16
 */
public class GlobalItemsTab {
	PageLoaded pl = new PageLoaded();
	private OrasiDriver driver = null;
	private ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);

	/**Page Elements**/
	@FindBy(id = "aTab2")	private Link globalItemsTab;
	@FindBy(css = "#lblGlobalCount") private Label globalCount;
	@FindBy(css = ".css-label") private List<WebElement> chkCompare;
	@FindBy(xpath = ".//*[@id='divCompareGlobalItem']/table/tbody/tr[3]/td/input") private Button btnCompare;
	@FindBy(xpath = ".//*[@id='gvGlobalSearch']/tbody/tr/td/span") private List<WebElement> globalItemsGrid;

	// Global Search - Add to cart
	@FindBy(xpath = ".//*[@id='gvGlobalSearch']/tbody/tr[1]/td[3]/table/tbody/tr[7]/td/table/tbody/tr/td[4]/a/div") 
	private Button AddToCart;

	/**Constructor**/

	public GlobalItemsTab(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}

	private void pageLoaded(){
		globalItemsTab.syncVisible(20, false);
	}

	/**Page Interactions**/


	/**
	 * @summary Click on Global Item Tab 	
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/
	public void click_GlobalItemsTab(){
		globalItemsTab.syncVisible(20, false);
		//globalItemsTab.click();
		driver.executeJavaScript("arguments[0].click();", globalItemsTab);
		pl.isDomComplete(driver);
	}

	/**
	 * @summary Method to read global Item Numbers 	
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/
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


	/**
	 * @summary Method to click Add-To-Cart Button
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/
	public void click_AddToCartButton(){
		AddToCart.syncVisible(10, false);
		AddToCart.click();
		driver.setPageTimeout(3);
	}

	/**
	 * @summary Method to Read Number of Records from Global Table
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/
	public int read_GlobalItemsCount(){
		return Integer.parseInt(globalCount.getText().replaceAll("\\D+", ""));
	}

	
	/**
	 * @summary Method to compare selected Item from Comparison Screen
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/
	public void perform_ItemsComparison(){
		TestReporter.assertTrue(read_GlobalItemsCount()>3, read_GlobalItemsCount() +" - Global Item Records available!!");
		for(int i=0;i<3;i++){
			driver.executeJavaScript("arguments[0].click();", chkCompare.get(i));
		}
		btnCompare.jsClick();
	}
	
	/**
	 * @summary Method to select GlobalItems from Comparison screen.
	 * @author Praveen Namburi,@Version: Created 16-09-2016
	 **/
	public void selectGlobalItems_FromComparison(){
		TestReporter.assertTrue(read_GlobalItemsCount()>3, read_GlobalItemsCount() +" - Global Item Records available!!");
		click_GlobalItemsTab();
		for(int i=1;i<4;i++){
			driver.executeJavaScript("arguments[0].click();", chkCompare.get(i));
		}
		btnCompare.jsClick();
	}

}



