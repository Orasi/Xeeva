package com.xeeva.catalog;

import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.Listbox;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.PageLoaded;
import com.orasi.utils.Sleeper;
import com.orasi.utils.TestReporter;

/**
 * @summary This page contains Recent Order Information page objects.
 * @author  Praveen Namburi, @version: Created 07-09-2016.
 */
public class RecentOrderInformationPage {

	
	PageLoaded pl = new PageLoaded();
	private OrasiDriver driver = null;
	private ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);
	//String xpath = "//tr/td/table/tbody/tr/td/table/tbody/tr/td[4]/a";
	String xpath = ".//*[@id='gvRecentOdersGrid']/tbody/tr/td[1]/a";

	/**Page Elements**/
	@FindBy(linkText = "lnkShowPopup") private Link lnkCartItem;
	@FindBy(id = "txtBasicSearchCriteria")	private Textbox catalogSearch;
	@FindBy(xpath = "//*[@class='RecentOrderDetailsGrid ']/tbody/tr[2]/td[4]/a") private Link lnkItemNumber;
	@FindBy(xpath = ".//*[@class='RecentOrderDetailsGrid']/tbody/tr") private List<WebElement> RecentOrderDetailsGrid;
	@FindBy(name = "ButtonClose") private Button btnBack;
	@FindBy(xpath = ".//*[@id='aTab5']") private Button btnRejectOrderTab;

	//SelectUOMValue,AddToCartItemsGrid
	@FindBy(xpath="//select[@class='textFieldList width90Px']") private Listbox lstSelectUOMValue;
	@FindBy(xpath="//td[@class='vAlignMiddle textAlignLeft']/a/div") private List<WebElement> lstAddToCartItemsGrid;
	@FindBy(xpath="//table[@id='gvLocalSearch']/tbody/tr/td[2]/span") private List<WebElement> localItemsGrid;
	@FindBy(xpath="//table[@id='gvGlobalSearch']/tbody/tr/td[2]/span") private List<WebElement> globalItemsGrid;
	@FindBy(xpath="//a[@id='aTab1']/span[2]") private Label lblLocalItems;
	@FindBy(xpath="//a[@id='aTab2']/span") private Label lblGlobalItems;

	@FindBy(xpath="//*[@title='Copy Item']") private List<WebElement> lstCopyItemGrid;
	
	
	/**Constructor**/
	public RecentOrderInformationPage(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}

	public void pageLoaded(){
		catalogSearch.syncVisible(10, false);
	}

	/**Page Interactions**/

	/**
	 * @summary Method to click on Back button  
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/
	public void clickBack(){
		btnBack.click();
	}

	/**
	 * @summary Method to click on Cart-Items Link  
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/
	public void clickCartItemsLink(){
		pageLoaded();
		lnkCartItem.syncVisible(5, false);
		lnkCartItem.click();
	}


	/**
	 * @summary Method to click on ItemNumber link
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/
	public void clickItemNumberLink(){
		pageLoaded();
		lnkItemNumber.syncVisible(10, false);
		lnkItemNumber.click();
		driver.setPageTimeout(3);
	}

	/**
	 * @summary This Method Clicks on Item Number Link from Recent Orders Information
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/	
	public void clcik_RecentOrderItemLink(){
		List<WebElement> ItemNumberLinks = driver.findElements(By.xpath(xpath));
		if(ItemNumberLinks.size()!=0){
			for(WebElement href :ItemNumberLinks){
				// Click on Item Number
				TestReporter.logStep( "Item Number : "+href.getText());
				driver.findElement(By.linkText(href.getText())).jsClick();
				break;
			}
		}else{
			TestReporter.assertFalse(ItemNumberLinks.size()==0, "No Item Links available for Selected Order!!");
		}
	}

	/**
	 * @Summary: Method to click on Price Agreement Item link from Recent Orders page.
	 * @author: Praveen Namburi, @version: Created on 14-09-2016
	 * @param REQValue
	 */
	public void clickPAItemLinkFromRecentOrders(String REQValue){
		pageLoaded();
		pl.isDomInteractive(driver);
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
						driver.setPageTimeout(3);
						//driver.setPageTimeout(3);
						break;
					}
					break;
				}
			}
		}
	}


	/**
	 * @summary This Method Clicks on RFQ Item Number Link from Recent Orders Tab
	 * @author Lalitha Banda
	 * @date 27/9/16
	 **/	
	public void click_ItemLink_RecentOrderPage() {
		List<WebElement> ItemNumberLinks = driver.findElements(By.xpath(xpath));
		if(ItemNumberLinks.size()!=0){
			for(WebElement href :ItemNumberLinks){
				List<WebElement> RFQNumber = driver.findElements(By.xpath(".//*[@id='gvRecentOdersGrid']/tbody/tr/td[4]/span"));
				for(WebElement input : RFQNumber){
					if(input.getText()!=null && !input.getText().isEmpty()){
						// Click on Item Number
						TestReporter.logStep( "Item Number : "+href.getText());
						driver.findElement(By.linkText(href.getText())).jsClick();
						break;
					}
				}
				break;
			}
		}else{
			TestReporter.assertFalse(ItemNumberLinks.size()==0, "No Item Links available for Selected Order!!");
		}
	}


	/**
	 * @summary This Method Click Item Number Link - Recent Order Information Page
	 * @author Lalitha Banda
	 * @date 27/9/16
	 **/
	public void Click_ItemLink_RecentOrderInfoPage(){
		List<WebElement> recentOrderLinks  = driver.findElements(By.xpath("//tr/td/table/tbody/tr/td/table/tbody/tr/td[4]"));
		for(WebElement inputLink : recentOrderLinks){
			List<WebElement> Links = inputLink.findElements(By.tagName("a"));
			if(Links.size()>0){
				TestReporter.logStep( "Item Number in Recent Order Information Page : "+Links.get(0).getText());
				driver.findElement(By.linkText(Links.get(0).getText())).jsClick();
				break;
			}else{
				TestReporter.assertTrue(false, "The Specified Random RFQ value Having No Item Number ");
			}
		}

	}


	/**
	 * @summary This Method Clicks on Reject Orders Tab
	 * @author Lalitha Banda
	 * @date 27/9/16
	 **/ 
	public void click_RejectOrder(){
		pageLoaded();
		btnRejectOrderTab.syncVisible(5, false);
		btnRejectOrderTab.click();
	}

	/**
	 * @summary This Method Clicks Item Link in Reject Orders Tab
	 * @author Lalitha Banda
	 * @date 27/9/16
	 **/ 
	public void Click_ItemLink_RejectOrdersPage(){
		pl.isDomComplete(driver);
		List<WebElement> rejectOrderItemLinks = driver.findElements(By.xpath(".//*[@class='gridtextLink']"));
		if(rejectOrderItemLinks.size()>0){
			TestReporter.logStep("Reject Order Item Number : "+rejectOrderItemLinks.get(0).getText());
			driver.findElement(By.linkText(rejectOrderItemLinks.get(0).getText())).jsClick();
		}else{
			TestReporter.assertTrue(false, "No Items available in Reject Order Tab !!");
		}
	}

	/**
	 * @summary This Method captures Item Number from Recent Orders Information
	 * @author Praveen Varma
	 * @date 28/9/16
	 **/	
	public String getItemNumber(){
		String itemNumber = null;
		List<WebElement> ItemNumberLinks = driver.findElements(By.xpath(xpath));
		if(ItemNumberLinks.size()!=0){
			for(WebElement href :ItemNumberLinks){
				// Capture on Item Number
				itemNumber = href.getText();
				TestReporter.logStep( "Item Number : "+itemNumber);
				break;
			}
		}else{
			TestReporter.assertTrue(false, "No Item Links available for Selected Order!!");
		}
		return itemNumber;
	}
	
	/**
	 * @summary This Method Clicks on CopyItem Link from Recent Orders Information
	 * @author  Praveen Varma @date 28/09/16
	 **/	
	public void click_CopyItemLink(){
		lstCopyItemGrid.get(0).click();
	}

}

