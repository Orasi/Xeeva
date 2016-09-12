package com.xeeva.catalog;

import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.Listbox;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.Webtable;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.Sleeper;
import com.orasi.utils.TestReporter;
import com.xeeva.catalog.SearchItems.GlobalItemsTab;
import com.xeeva.catalog.SearchItems.LocalItemsTab;

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
		@FindBy(xpath = ".//*[@class='RecentOrderDetailsGrid']/tbody/tr") private List<WebElement> RecentOrderDetailsGrid;
		@FindBy(name = "ButtonClose") private Button btnBack;
		
		//SelectUOMValue,AddToCartItemsGrid
		@FindBy(xpath="//select[@class='textFieldList width90Px']") private Listbox lstSelectUOMValue;
		@FindBy(xpath="//td[@class='vAlignMiddle textAlignLeft']/a/div") private List<WebElement> lstAddToCartItemsGrid;
		@FindBy(xpath="//table[@id='gvLocalSearch']/tbody/tr/td[2]/span") private List<WebElement> localItemsGrid;
		@FindBy(xpath="//table[@id='gvGlobalSearch']/tbody/tr/td[2]/span") private List<WebElement> globalItemsGrid;
		@FindBy(xpath="//a[@id='aTab1']/span[2]") private Label lblLocalItems;
		@FindBy(xpath="//a[@id='aTab2']/span") private Label lblGlobalItems;

		/**Constructor**/
		public RecentOrderInformationPage(OrasiDriver driver){
			this.driver = driver;
			ElementFactory.initElements(driver, this);
		}
	
		public void pageLoaded(){
			catalogSearch.syncVisible(10, false);
		}
	
		/**Page Interactions**/
		
		public void clickBack(){
			btnBack.click();
		}
	
		// Method to click on Cart-Items Link 
		public void clickCartItemsLink(){
			pageLoaded();
			lnkCartItem.syncVisible(5, false);
			lnkCartItem.click();
		}
	
		//Method to click on ItemNumber link
		public void clickItemNumberLink(){
			pageLoaded();
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			lnkItemNumber.syncVisible(5, false);
			lnkItemNumber.click();
			Sleeper.sleep(2000);
			//driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		}


		// This Method Clicks on Item Number Link from Recent Orders Information
		public void clcik_RecentOrderItemLink(){
			List<WebElement> ItemNumberLinks = driver.findElements(By.xpath("//tr/td/table/tbody/tr/td/table/tbody/tr/td[4]/a"));
			if(ItemNumberLinks.size()!=0){
				for(WebElement href :ItemNumberLinks){
					// Click on Item Number
					TestReporter.logStep( "Item Number : "+href.getText());
					driver.findElement(By.linkText(href.getText())).jsClick();
					break;
				}
			}else{
				TestReporter.logStep("No Item Links available for Selected Order!!");
			}
		}

		/*//Method to click on ItemNumber link
		public void clickItemNumbrLink(String itemNumber){
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
			
		}*/
		
		/**
		 * @Summary: Method to click on Price Agreement Item link from Recent Orders page
		 * @param REQValue
		 */
		public void clickPAItemLinkFromRecentOrders(String REQValue){
			pageLoaded();
			Sleeper.sleep(2000);
			List<WebElement> getRecentOrderRows = driver.findElements(By.xpath("//table[@class='RecentOrderDetailsGrid ']/tbody/tr"));
			int getRecentOrdRowsCount = getRecentOrderRows.size();
			
			for(int rows=2; rows<=getRecentOrdRowsCount; rows++){
				List<WebElement> getREQValues = driver.findElements(By.xpath("//table[@class='RecentOrderDetailsGrid ']/tbody/tr[" + rows + "]/td[1]/span"));
				for(WebElement getREQValue : getREQValues){
					if(getREQValue.getText().trim().contains(REQValue)){
						List<WebElement> itemNumberLinks = driver.findElements(By.xpath("//table[@class='RecentOrderDetailsGrid ']/tbody/tr[" + rows + "]/td[4]/a"));
						for(WebElement itemNumLink : itemNumberLinks ){
							String getItemNumLink = itemNumLink.getText();
							TestReporter.log("Clicking on Item Number link for Price agreement item : " + getItemNumLink);
							itemNumLink.click();
							Sleeper.sleep(3000);
							break;
						}
						break;
					}
				}
			}
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		}
			
		/**
		 * @summary: Method to click on Show Favourite items.
		 * @author: Praveen Namburi, @version: Created 09-09-2016
		 *//*
		public void clickShowFavouriteItems(){
			lnkShowFavItems.syncVisible(15, false);
			lnkShowFavItems.click();
			Sleeper.sleep(4000);
		}
	
		*//**
		 * @summary: Method to add price agreement from favourite folder.
		 * @author: Praveen Namburi,@version: Created 09-09-2016
		 * @param strUOMValue
		 *//*
		public void addPriceAgreementItemsFromFavFolder(String strUOMValue){
			String getLocalItemsCount = lblLocalItems.getText();
			TestReporter.log("Local-Items Count is: "+ getLocalItemsCount);
			
			String getGlobalItemsCount = lblGlobalItems.getText();
			TestReporter.log("Global-Items Count is: "+ getGlobalItemsCount);
			
			if(!getLocalItemsCount.contains("0")){
				TestReporter.log("Click on Local-Items tab.");
				LocalItemsTab localItem = new LocalItemsTab(driver);
				localItem.click_localItemsTab();
				String itemNumber = null;
				List<WebElement> localItems = localItemsGrid;
				
				for(WebElement inputItem : localItems){
					  itemNumber = inputItem.getText();
					  System.out.println("Item number is: "+itemNumber);
					  lstSelectUOMValue.select(strUOMValue);
					  List<WebElement> localAddToCartItemLinks = lstAddToCartItemsGrid;
						if(localAddToCartItemLinks.size()>0){
							for(WebElement linkAddToCartItem :localAddToCartItemLinks){
								TestReporter.log("Click on 'Add-To-Cart' link.");
								linkAddToCartItem.click();
								break;
							}
						}
					  break;
				}
			}else if(getLocalItemsCount.contains("0")){
				TestReporter.log(" 'No Records Found !!' in Local Items tab. "
						+ "Searching for Global items.");
				
			}else if(!getGlobalItemsCount.contains("0")){
				TestReporter.log("Click on Global-Items tab.");
				GlobalItemsTab globalItem = new GlobalItemsTab(driver);
				globalItem.click_GlobalItemsTab();
				
				String itemNumber = null;
				List<WebElement> globalItems = globalItemsGrid;
				for(WebElement inputItem : globalItems){
					  itemNumber = inputItem.getText();
					  System.out.println("Item number is: "+itemNumber);
					  lstSelectUOMValue.select(strUOMValue);
					  List<WebElement> globalAddToCartItemLinks = lstAddToCartItemsGrid;
						if(globalAddToCartItemLinks.size()>0){
							for(WebElement linkAddToCartItem :globalAddToCartItemLinks){
								TestReporter.log("Click on 'Add-To-Cart' link.");
								linkAddToCartItem.click();
								break;
							}
						}
					  break;
				}				
	        }
       
		}*/
		
}
