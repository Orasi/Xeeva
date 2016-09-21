package com.xeeva.catalog;

import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.apache.commons.beanutils.converters.DateConverter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Element;
import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Listbox;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.AlertHandler;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.Sleeper;
import com.orasi.utils.TestReporter;
import com.orasi.utils.date.DateTimeConversion;

/**
 * @summary This page contains Cost Center page objects
 * @author  Lalitha Banda
 * @date    15/09/16
 */

public class CostCenterPage {

	private OrasiDriver driver = null;
	private ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);
	String xpath = ".//*[@id='customfa']/tbody/tr/td/select";

	/**Page Elements**/
	@FindBy(id ="countrydivcontainer") private Element costCenterContainer;	
	@FindBy(xpath="//*[@id='customfa']/tbody") private WebElement tblCostCenterInfo;
	@FindBy(id ="ddlApplyCC") private Listbox lstCostCenterHeaderLevel;
	@FindBy(className ="fa fa-calendar cursor-pointer font16px") private Button btnCalender;
	@FindBy(id ="txtRequiredby") private Textbox txtRequiredBy;
	@FindBy(xpath="//*[@title='Internal Comment']") private List<WebElement> lstInternalCommentGrid;
	@FindBy(id ="txtInternalComment") private Textbox txtInternalComment;
	@FindBy(id ="btnSaveComments") private Button btnSaveComments;
	@FindBy(id ="txtShowFullComment") private Textbox txtShowFullComment;
	@FindBy(xpath="//*[@id='txtShowFullComment']") private List<WebElement> txtCommentDisplayed;
	@FindBy(id ="fancybox-close") private Button btnClosePopUp;
	@FindBy(xpath="//*[@title='Copy Item']") private List<WebElement> lstCopyItemGrid;
	@FindBy(css=".costCentername") private Label lblCC;

	@FindBy(xpath="//td[2]/a[1]/input") private Button btnSaveCartCC;
	@FindBy(xpath="//tr[2]/td[2]/a[2]/input") private Button btnShopMoreItemsCC;
	@FindBy(xpath="//*[@id='btnCheckOut']/input") private Button btnContinueCheckoutCC;
	@FindBy(xpath=".//*[@id='customfa']/tbody/tr/td/select") private List<Listbox> ddCCLineLevel;

	@FindBy(xpath="//table[@id='customfa']/tbody/tr/td/table/tbody/tr/td/input") private List<WebElement> dateCCInputLineLevel;
	@FindBy(xpath="//table[@id='customfa']/tbody/tr/td/table/tbody/tr/td/i") private List<WebElement> dateCCLineLevel;
	
	@FindBy(xpath="//div[@id='divAppInfoMsg'][@class='addMessage']") private Label lblCartItemAddedMessage;
	
	/**Constructor**/
	public CostCenterPage(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}


	public void pageLoaded(){
		costCenterContainer.syncVisible(20, false);
	}

	/**Page Interactions**/


	/**
	 * @summary Clicking Internal Comment Link
	 * @author  Lalitha Banda
	 * @date    15/09/16
	 */
	public void click_InternalComment(){
		pageLoaded();
		driver.setPageTimeout(3);
		lstInternalCommentGrid.get(0).click();
	}

	/**
	 * @summary Clicking CopyItem Link
	 * @author  Lalitha Banda
	 * @date    15/09/16
	 */
	public void click_CopyItemLink(){
		lstCopyItemGrid.get(0).click();
	}


	/**
	 * @summary Adding Internal Comment
	 * @author  Lalitha Banda
	 * @date    15/09/16
	 */
	public void AddInternalComment(String InternalComment){
		lblCC.syncVisible(30, false);
		lblCC.isDisplayed();
		click_InternalComment();
		txtInternalComment.safeSet(InternalComment);
		btnSaveComments.click();

		if(txtCommentDisplayed.size()>0){
			for(WebElement fullComment :txtCommentDisplayed){
				String Expected = fullComment.getText();
				TestReporter.logStep("Actual :"+ InternalComment + ": " + "Expected : "+ Expected);
				TestReporter.assertTrue(Expected.equals(InternalComment), "Internal Comment Displayed is Verified!!");
				break;
			}
		}else{
			TestReporter.assertTrue(false, "Internal Comment Wrongly Displayed");
		}
		driver.setPageTimeout(2);
		btnClosePopUp.click();
	}


	/**
	 * @summary Method to verify Cost Center
	 * @author  Lalitha Banda
	 * @date    20/09/16
	 */
	public boolean verifyCostCenter(String verifyType,String itemNumber,String CCValue){
		boolean statusFlag = false;
		List<WebElement> readLinks = driver.findElements(By.xpath(".//*[@id='customfa']/tbody/tr/td[1]/a"));
		List<WebElement> readSelects = driver.findElements(By.xpath(".//*[@id='customfa']/tbody/tr/td/select"));
		
		switch(verifyType.toLowerCase()){
		case "linelevel":
			for(WebElement inputLink : readLinks){
				for(WebElement inputSelects: readSelects){
					String updatedCC = new Select(inputSelects).getFirstSelectedOption().getText();
					if(inputLink.getText().equalsIgnoreCase(itemNumber) &&updatedCC.equalsIgnoreCase(CCValue)){
						statusFlag = true;			
						break;
					}
				}
			}

			break;
		case "headerlevel": 
			for(int i=0;i<readSelects.size();i++){
				String ExpectedCC = new Select(readSelects.get(i)).getFirstSelectedOption().getText();
				if(ExpectedCC.equalsIgnoreCase(CCValue)){
					statusFlag =true;
				}
			}
			break;
		case "RequiredBy_LineLvel":
			
			
		default : System.out.println();

		}
		return statusFlag;
	}

	// reading Handler method from AlertHandler class
	public static void handleAlert(WebDriver driver){
		AlertHandler handleAlert = new AlertHandler();
		handleAlert.handleAlert(driver, 3);
	}


	/**
	 * @summary: Method to get the date as per the daysout value.
	 * @param daysOut
	 * @return
	 */
	public String getDate(String daysOut){
		 DateTimeConversion dteTimeCon = new DateTimeConversion();
		 String setDaysOut = dteTimeCon.getDaysOut(daysOut, "MM/dd/yyyy").toString();
		 TestReporter.logStep("No. of days-out : " + daysOut);
		 //System.out.println("Date : "+setDaysOut);
		 return setDaysOut;
	}
	
	/**
	 * @summary Method to change Cost Center
	 * @author  Lalitha Banda
	 * @date    16/09/16
	 */
	public void change_CC(String changeType,String CC){
		// after cart check out, application taking time to load cost center page 
		lblCC.syncVisible(30, false);
		lblCC.isDisplayed();
		switch(changeType.toLowerCase()){

		case "linelevel" : 
			TestReporter.logStep( "CC before updation : "+ddCCLineLevel.get(0).getFirstSelectedOption().getText());
			//Read item number here
			String randomID =ddCCLineLevel.get(0).getAttribute("id").replaceAll("\\D+", "");
			String itemNumber = driver.findElement(By.xpath(".//*[@id='trCCRow_"+randomID+"']/td/a")).getText();
			TestReporter.logStep("itemNumber :"+ itemNumber);
			ddCCLineLevel.get(0).select(CC);
			System.out.println(verifyCostCenter(changeType,itemNumber,CC));
			TestReporter.assertTrue(verifyCostCenter(changeType,itemNumber,CC),"Cost Center Updated at Line Level Successfully!!");
			break;

		case "headerlevel" :
			lstCostCenterHeaderLevel.select(CC);
			handleAlert(driver);
			System.out.println(verifyCostCenter(changeType,null,CC));
			TestReporter.assertTrue( verifyCostCenter(changeType,null,CC),"Cost Center Updated at Header Level Successfully!!");
			break;

		default : System.out.println();
		}
	}
	
	/**
	 * @summary: Method to get the RequiredByDate from textbox at CC_line-level.
	 * @author: Praveen Namburi, @Version: Created 21-09-2016
	 * @return getReqByDate
	 */
	public String getReqByDate(){
		String getRequreDate = null;
	    List<WebElement> reqByDates = dateCCInputLineLevel;
	    for(WebElement reqByDate : reqByDates){
		   String getReqByDate_beforeChange = reqByDate.getAttribute("value");
		   System.out.println(getReqByDate_beforeChange);
		   getRequreDate=getReqByDate_beforeChange;
		   break;
	    }
		return getRequreDate;
	}
	
	/**
	 * @summary: Method to verify the RequiredBydate has been updated Successfully at CC_ line-level.
	 * @author: Praveen Namburi, @version: Created 21-09-2016.
	 */
	public void verify_RequiredByDateUpdated_atCCLineLevel(){
		lblCartItemAddedMessage.syncVisible(20, false);
		String getReqByDateMessage = lblCartItemAddedMessage.getText();
		TestReporter.logStep("Message after changing the RequiredBy date at CC_line-level : "+ getReqByDateMessage);
		TestReporter.assertTrue(getReqByDateMessage.contains("date has been updated."), 
				"Required by date has been updated at CC-Level.");

	}

	/**
	 * @summary: Method to change the RequiredBy value at linelevel.
	 * @author: Praveen Namburi, @version: Created 20-09-2016.
	 * @param daysOut
	 */
	public void change_RequiredByAtLineLevel(String daysOut){
		  // after cart check out, application taking time to load cost center page 
		  lblCC.syncVisible(30, false);
		  lblCC.isDisplayed();
		  driver.setPageTimeout(3);
		  
		  String getReqByDate_beforeChange = getReqByDate();
		  TestReporter.log("Get ReqByDate_before Change: " + getReqByDate_beforeChange);
		  
		  driver.executeJavaScript("arguments[0].click();", dateCCLineLevel.get(0));
		  List<WebElement> nextMonthArrows = driver.findElements(By.xpath("html/body/div[@class='calendar']/"
		  		+ "table/thead/tr[2]/td[4]"));
		  int getNextMonthArrowsSize = nextMonthArrows.size();
		  System.out.println("Next month arrows size: "+getNextMonthArrowsSize);
		  for(WebElement nextMonth : nextMonthArrows){
			   driver.setPageTimeout(2);
			   nextMonth.click();
			   driver.setPageTimeout(2);
			   List<WebElement> selectDates = driver.findElements(By.xpath("html/body/div[@class='calendar']/"
			   		+ "table/tbody/tr[4]/td[@class='day']"));
			   //int getWeekendDates = selectDates.size();
			   //System.out.println("======> : " + getWeekendDates);
			   for(WebElement selWeekDate : selectDates){
				   selWeekDate.click();
			       driver.setPageTimeout(6);
			       break;
			   }
		  }
		  String getReqByDate_AfterChange = getReqByDate();
		  TestReporter.log("Get ReqByDate_after Change: " + getReqByDate_AfterChange);
		  //Verify the RequiredBy date is modified at line-level.
		  TestReporter.assertNotEquals(getReqByDate_beforeChange, getReqByDate_AfterChange, "RequiredBy date is modified at line-level.");

	}
	

 }

		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  /*String randomID =ddCCLineLevel.get(0).getAttribute("id").replaceAll("\\D+", "");
		  String itemNumber = driver.findElement(By.xpath(".//*[@id='trCCRow_"+randomID+"']/td/a")).getText();
		  TestReporter.logStep("itemNumber is:"+ itemNumber);
		  int count = 0;*/
		  /*List<WebElement> readLinks = driver.findElements(By.xpath(".//*[@id='customfa']/tbody/tr/td[1]/a"));
		  List<WebElement> reqByDates = driver.findElements(By.xpath("//table[@id='customfa']/tbody/tr/td/table/tbody/tr/td/input"));
		  ////table[@id='customfa']/tbody/tr[@class='DataGridTRRowa']/td[5]/table/tbody/tr/td/input
			for(WebElement inputLink : readLinks){
				for(WebElement reqByDate : reqByDates){
					String getReqByDate_beforeChange = reqByDate.getAttribute("value");
					TestReporter.logStep("Get RequiredBy date before Change : " + getReqByDate_beforeChange);
					if(inputLink.getText().equalsIgnoreCase(itemNumber)){
						List<WebElement> nextMonthArrows = driver.findElements(By.xpath("html/body/div[@class='calendar']/table/thead/tr[2]/td[4]"));
						int getNextMonthArrowsSize = nextMonthArrows.size();
						for(WebElement nextMonth : nextMonthArrows){
							driver.setPageTimeout(2);
							nextMonth.click();
							driver.setPageTimeout(2);
							break;
						}*/
						
						
						//String getReqByDate_afterChange = getDate(daysOut);
						/*TestReporter.logStep("Get RequiredBy date after Change : "+ (getReqByDate_afterChange));
						driver.setPageTimeout(3);
						reqByDate.clear();
						//reqByDate.click();
						reqByDate.sendKeys(getReqByDate_afterChange);
						driver.setPageTimeout(5);
						//driver.setPageTimeout(2);
						TestReporter.assertNotEquals(getReqByDate_beforeChange, getReqByDate_afterChange, "RequiredBy date is modified at line-level.");
						count++;
						break;*/
						
						//List<WebElement> getdays = driver.findElements(By.xpath(""));
						
				/*	}
					if(count==1)break;
				}
				if(count==1)break;
			}
		  
		*/

