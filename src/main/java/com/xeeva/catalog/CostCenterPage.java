package com.xeeva.catalog;

import java.util.List;
import java.util.ResourceBundle;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Element;
import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Listbox;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.Sleeper;
import com.orasi.utils.TestReporter;

/**
 * @summary This page contains Cost Center page objects
 * @author  Lalitha Banda
 * @date    15/09/16
 */

public class CostCenterPage {

	private OrasiDriver driver = null;
	private ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);

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

		List<WebElement> CommentDisplayed = txtCommentDisplayed;
		if(txtCommentDisplayed.size()>0){
			for(WebElement fullComment :txtCommentDisplayed){
				String Expected = txtCommentDisplayed.get(0).getText();
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
	 * @summary Method to verify CC at Line level
	 * @author  Lalitha Banda
	 * @date    16/09/16
	 */
	public boolean verifyCC_LineLevel(String itemNumber,String CCValue) {
		boolean statusflag = false;
		List<WebElement> readLinks = driver.findElements(By.xpath(".//*[@id='customfa']/tbody/tr/td[1]/a"));
		for(WebElement inputLink : readLinks){
			List<WebElement> readSelects = driver.findElements(By.xpath(".//*[@id='customfa']/tbody/tr/td/select"));
			for(WebElement inputSelects: readSelects){
				String updatedCC = new Select(inputSelects).getFirstSelectedOption().getText();
				if(inputLink.getText().equalsIgnoreCase(itemNumber) &&updatedCC.equalsIgnoreCase(CCValue)){
					statusflag = true;			
					break;
				}
			}
		}
		return statusflag;
	}


	/**
	 * @summary Method to change CC at Line level
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
			TestReporter.assertTrue(verifyCC_LineLevel(itemNumber,CC),"Cost Center Updated at Line Level Successfully!!");
			break;

		case "headerlevel" :
			lstCostCenterHeaderLevel.select(CC);
			break;

		default : System.out.println();
		}
	}
}


