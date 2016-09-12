package com.xeeva.catalog;

import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.Sleeper;
import com.orasi.utils.TestReporter;

/**
 * @summary This page contains Recent Order Information page objects.
 * @author  Praveen Namburi, @version: Created 07-09-2016.
 */
public class RecentOrderInformationPage {
	
		private OrasiDriver driver = null;
		private ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);

		/**Page Elements**/
		@FindBy(linkText = "lnkShowPopup") private Link lnkCartItem;
		@FindBy(id = "txtBasicSearchCriteria")	private Textbox catalogSearch;
		@FindBy(xpath = "//*[@class='RecentOrderDetailsGrid ']/tbody/tr[2]/td[4]/a") private Link lnkItemNumber;
		
		/**Constructor**/
		public RecentOrderInformationPage(OrasiDriver driver){
			this.driver = driver;
			ElementFactory.initElements(driver, this);
		}

		public void pageLoaded(){
			catalogSearch.syncVisible(10, false);
		}

		/**Page Interactions**/

		// Method to click on Cart-Items Link 
		public void clickCartItemsLink(){
			pageLoaded();
			lnkCartItem.syncVisible(5, false);
			lnkCartItem.click();
		}

		//Method to click on ItemNumber link
		public void clickItemNumberLink(String itemNumber){
			pageLoaded();
			Sleeper.sleep(2000);
			List<WebElement> itemNumberLinks = driver.findElements(By.xpath("//td[@class='DataGridrowa whiteSpaceNoWrap']/a"));
			int itemNumberLinksCount = itemNumberLinks.size();
			System.out.println("Total no. of item number links found: "+itemNumberLinksCount);
			
			for(WebElement itemNumLink : itemNumberLinks ){
				String getItemNumber = itemNumLink.getText();
				TestReporter.log("Click on Item Number : "+getItemNumber);
				if(getItemNumber.trim().equalsIgnoreCase(itemNumber))
				itemNumLink.click();
				Sleeper.sleep(3000);
				break;
			}
			
		}
}
