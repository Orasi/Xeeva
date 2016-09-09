package com.xeeva.catalog;

import java.util.List;
import java.util.ResourceBundle;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.orasi.core.interfaces.Element;
import com.orasi.core.interfaces.Webtable;
import com.orasi.core.interfaces.impl.WebtableImpl;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.TestReporter;

/**
 * @summary: This page class contains all the methods and locators of CartInformation page.
 * @author praveen varma,@version: Created 8-9-2016
 */
public class CartInformationPage {

	private OrasiDriver driver = null;
	private ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);

	/**Page Elements**/
	@FindBy(css="#fancybox-outer") private Element eleCartInformationPage;	
	@FindBy(xpath="//*[@id='tblCartInfo']/tbody") private WebElement tblCartInfo;

	/**Constructor**/
	public CartInformationPage(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}

	
	public void pageLoaded(){
		eleCartInformationPage.syncVisible(15, false);
	}

	/**Page Interactions**/

	/**
	 * @summary: Method to verify UnitPrice is not editable for added item into cart.
	 * @author: Praveen Namburi, @version: Created 08-09-2016,@param strUOMValue
	 */
	public void verifyUnitPriceIsNotEditable(String itemNumber){
		pageLoaded();
		List<WebElement> itemNumLinks = driver.findElements(By.xpath("//*[@id='tblCartInfo']/tbody/tr"));
		int getRowsCount = itemNumLinks.size();
		System.out.println("Total no. of rows : " + getRowsCount);
		
		for(WebElement itemNumberLink : itemNumLinks){
			System.out.println(itemNumberLink.findElement(By.tagName("td[1]")).getText().trim());
			/*if(itemNumberLink.findElement(By.tagName("td[1]")).getText().trim().equalsIgnoreCase(itemNumber)){
				List<WebElement> unitPriceItems = itemNumberLink.findElement(By.tagName("td[6]")).findElements(By.tagName("input"));
				int itemsCount = unitPriceItems.size();
				TestReporter.assertTrue(itemsCount==0, "Requestor is not able to change the price.");
				break;
			}*/
		}
		
	 }	
	
}		
 



