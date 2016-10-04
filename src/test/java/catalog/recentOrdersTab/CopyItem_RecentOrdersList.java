package catalog.recentOrdersTab;

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
import com.xeeva.catalog.CostCenterPage;
import com.xeeva.catalog.RecentOrderInformationPage;
import com.xeeva.catalog.RequisitioningPage;
import com.xeeva.login.LoginPage;
import com.xeeva.navigation.MainNav;

/**
 * @Summary: To verify that requester is able to copy item from recent order list.
 * @author praveen varma, @version: Created 28-09-2016
 */
public class CopyItem_RecentOrdersList extends TestEnvironment{

	public String itemType = "Quantity";
	public String itemNumber = "";
	//public String itemNumber = null;

	// **************
	// Data Provider
	// **************
	@DataProvider(name = "dataScenario")
	public Object[][] scenarios() {
		try {
			Object[][] excelData = new ExcelDataProvider("/datasheets/CopyItem_RecentOrdersList.xlsx",
					"CopyItem_RecentOrdersList").getTestData();
			return excelData;
		}
		catch (RuntimeException e){
			TestReporter.assertTrue(false, "An error occured with accessing the data provider: " + e);
		}
		return new Object[][] {{}};
	}

	/**
	 * @description: To initialize the driver and setup the environment.
	 * @param runLocation,browserUnderTest,@param browserVersion
	 * @param operatingSystem,@param environment
	 */
	@BeforeTest
	@Parameters({ "runLocation", "browserUnderTest", "browserVersion","operatingSystem", "environment" })
	public void setup(@Optional String runLocation, String browserUnderTest,
			String browserVersion, String operatingSystem, String environment) {
		setApplicationUnderTest("XEEVA");
		setBrowserUnderTest(browserUnderTest);
		setBrowserVersion(browserVersion);
		setOperatingSystem(operatingSystem);
		setRunLocation(runLocation);
		setTestEnvironment(environment);
		testStart("CopyItem_RecentOrdersList");
	}

	/**
	 * @description: Close the driver instance.
	 * @param testResults
	 */
	@AfterTest
	public void close(ITestContext testResults){
		endTest("TestAlert", testResults);
	}

	/**
	 * @Description: Main business-logic of the test-case resides here.
	 * @param role,location,comments
	 */
	@Test(dataProvider = "dataScenario")
	public void recentOrdersList(String role, String location,String QtValue){

		// Application Login 
		LoginPage loginPage = new LoginPage(getDriver());
		TestReporter.logStep("Launch the application and Login with valid Requestor credentials");
		loginPage.loginWithCredentials(role,location);

		// Requisition Page  - Navigating to requisition page to create Smart Form Request
		TestReporter.logStep("Navigate to Requisitioning Page and Click on Recent Orders Tab.");
		RequisitioningPage reqPage = new RequisitioningPage(getDriver());
		reqPage.click_ReqTab();

		// Requisition Page  - Clicking on RecentOrders Tab
		TestReporter.logStep("Clicking on RecentOrders Tab");
		reqPage.clickRecentOrdersTab();

		// Requisition Page  - Clicking on Cart Number which consists REQ Number
		TestReporter.logStep("Clicking on Cart Number which consists REQ Number");
		reqPage.clickCartwithREQNumber();

		//Recent Order Information Page  - Capture ItemNumber on Recent Order Information Page
		TestReporter.logStep("Capture ItemNumber on Recent Order Information Page");
		RecentOrderInformationPage roiPage = new RecentOrderInformationPage(getDriver());
		roiPage.getItemNumber();

		//Recent Order Information Page  -  Clicking on CopyItem Link
		TestReporter.logStep("Clicking on CopyItem Link");
		roiPage.click_CopyItemLink();

		//Requisition Page  - Clicking on Cart Number which consists REQ Number
		TestReporter.logStep("Clicking on Cart Number which consists REQ Number");
		reqPage.re_enterQuantity(QtValue);

		//Main Navigation Page  -  Navigating to Cart CheckOut Page
		TestReporter.logStep("Navigating to Cart CheckOut Page ");
		MainNav mainNav = new MainNav(getDriver());
		mainNav.cart_CheckOut();

		//Cost Center Page -  Verifying the Updated Quantity
		TestReporter.logStep("Verifying the Updated Quantity ");
		CostCenterPage ccPage = new CostCenterPage(getDriver());
		ccPage.verifyUpdatedQuantity(QtValue);

		// Application Logout
		TestReporter.logStep("Application Logout");
		mainNav.clickLogout();
	}
}

