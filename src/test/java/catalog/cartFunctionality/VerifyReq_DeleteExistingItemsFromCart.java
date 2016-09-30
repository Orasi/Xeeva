package catalog.cartFunctionality;


import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.orasi.utils.TestEnvironment;
import com.orasi.utils.TestReporter;
import com.orasi.utils.dataProviders.ExcelDataProvider;
import com.xeeva.catalog.CartInformationPage;
import com.xeeva.catalog.RequisitioningPage;
import com.xeeva.catalog.SearchItems.LocalItemsTab;
import com.xeeva.login.LoginPage;
import com.xeeva.navigation.MainNav;

/**
 * @Summary: To verity that requester is able to delete the items from already existing items into cart.
 * @author praveen varma, @version: Created 28-09-2016
 */
public class VerifyReq_DeleteExistingItemsFromCart extends TestEnvironment{

	public String RequisitionType = "serviceRequestGeneral";
	
	// **************
	// Data Provider
	// **************
	@DataProvider(name = "dataScenario")
	public Object[][] scenarios() {
		try {
			Object[][] excelData = new ExcelDataProvider("/datasheets/VerifyReq_DeleteExistingItemsFromCart.xlsx",
					"DeleteExistingItemsFromCart").getTestData();
			return excelData;
		}
		catch (RuntimeException e){
			TestReporter.assertTrue(false, "An error occured with accessing the data provider: " + e);
		}
		return new Object[][] {{}};
    }
	
	/**
	 * @Description: To initialize the driver and setup the environment.
	 * @param runLocation,browserUnderTest,@param browserVersion
	 * @param operatingSystem,@param environment
	 */
	@BeforeTest
	@Parameters({ "runLocation", "browserUnderTest", "browserVersion","operatingSystem", "environment" })
	public void setup(@Optional String runLocation, String browserUnderTest,String browserVersion, String operatingSystem, String environment) {
		setApplicationUnderTest("XEEVA");
		setBrowserUnderTest(browserUnderTest);
		setBrowserVersion(browserVersion);
		setOperatingSystem(operatingSystem);
		setRunLocation(runLocation);
		setTestEnvironment(environment);
		testStart("Add_PriceAgreementItem_InCart");
	}

	/**
	 * @Description: Close the driver instance.
	 * @param testResults
	 */
	@AfterTest
	public void close(ITestContext testResults){
		endTest("TestAlert", testResults);
	}
	
	/**
	 * @Description: Main business-logic of the test-case resides here.
	 * @param role,location
	 */
	@Test(dataProvider = "dataScenario")
	public void updateQuantityAndSaveCart(String role, String location){
		
		// Application Login 
		LoginPage loginPage = new LoginPage(getDriver());
		TestReporter.logStep("Launch the application and Login with valid Requestor credentials");
		loginPage.loginWithCredentials(role,location);
		
		// Requisition Page  - Navigating to requisition page.
		RequisitioningPage reqPage = new RequisitioningPage(getDriver());
		TestReporter.logStep("Navigate to Requisitioning Page.");
		reqPage.click_ReqTab();
		
		//Get Cart-Items count
		MainNav mainNav = new MainNav(getDriver());
		int cartItemsCount = mainNav.getCartItemsCount();
		TestReporter.log("Total no. of Cart-Items in Cart Info page: " + cartItemsCount);
		
		if(cartItemsCount>0){
			//Click on Cart-Items link and Navigate to Cart-Info page.
			LocalItemsTab localItemsPage = new LocalItemsTab(getDriver());
			TestReporter.logStep("Click on Cart-Items link and Navigate to Cart-Info page.");
			localItemsPage.clickCartItemsLink();
			
			//Delete the existing Cart-Items and Verify the Cart.
			TestReporter.logStep("Update the quantity and Verify Save cart functionality.");
			CartInformationPage cartInfoPage = new CartInformationPage(getDriver());
			cartInfoPage.deleteExistingCartItems();
			
			//Close cart-Info page.
			TestReporter.logStep("Close Cart-Information page.");
			cartInfoPage.closeCartInfoPage();
			
		}else{
			TestReporter.assertTrue(false, "Cart is Empty!!!");
		}
		
		// Application Logout
		TestReporter.logStep("Application Logout");
		mainNav.clickLogout();
			
	 }
			
  }


