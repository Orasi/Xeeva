package catalog.nonPriceAgreementItems;

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
import com.xeeva.catalog.ItemDetailsPage;
import com.xeeva.catalog.RecentOrderInformationPage;
import com.xeeva.catalog.RequisitioningPage;
import com.xeeva.login.LoginPage;
import com.xeeva.navigation.MainNav;

/**
 * @summary Test To add non price agreement item from recent orders list
 * @author  Lalitha Banda
 * @version	08/09/2016
 * *
 */

public class AddNonPriceAgreementItem_FromRecentOrders extends TestEnvironment{

	// **************
	// Data Provider
	// **************
	@DataProvider(name = "dataScenario")
	public Object[][] scenarios() {
		try {
			Object[][] excelData = new ExcelDataProvider("/datasheets/AddPriceAgreement_RecentOrder.xlsx","AddPriceAgrmnt_RecentOrder").getTestData();
			return excelData;
		}
		catch (RuntimeException e){
			TestReporter.assertTrue(false, "An error occured with accessing the data provider: " + e);
		}
		return new Object[][] {{}};
	}


	@BeforeTest
	@Parameters({ "runLocation", "browserUnderTest", "browserVersion","operatingSystem", "environment" })
	public void setup(@Optional String runLocation, String browserUnderTest,String browserVersion, String operatingSystem, String environment) {
		setApplicationUnderTest("XEEVA");
		setBrowserUnderTest(browserUnderTest);
		setBrowserVersion(browserVersion);
		setOperatingSystem(operatingSystem);
		setRunLocation(runLocation);
		setTestEnvironment(environment);
		testStart("AddingNonPriceAgreement");
	}

	@AfterTest
	public void close(ITestContext testResults){
		endTest("TestAlert", testResults);
	}

	@Test(dataProvider = "dataScenario")
	public void recentOrders(String role, String location,String selectUOM,String PAItem,String NPAItem,String ID,String UP,String Qty){

		// Application Login 
		TestReporter.logStep("Login into application");
		LoginPage loginPage = new LoginPage(getDriver());
		loginPage.loginWithCredentials(role,location);

		// Requisition Page  - Navigating to requisition page to create Smart Form Request
		RequisitioningPage reqPage = new RequisitioningPage(getDriver());
		TestReporter.logStep("Navigating to the Requisitioning Page.");
		reqPage.click_ReqTab();
		reqPage.clickRequisitionCartLink(NPAItem);

		RecentOrderInformationPage recentOrderInfoPage = new RecentOrderInformationPage(getDriver());
		TestReporter.logStep("Navigating to Recent Order Information page.");
		recentOrderInfoPage.clcik_RecentOrderItemLink();

		// Navigating to Item Details page - to add the Item to cart.
		ItemDetailsPage itemDetailsPage = new ItemDetailsPage(getDriver());
		TestReporter.logStep("Navigating to Item Details page.");
		itemDetailsPage.selectUOMValueAndAddNonPriceItemToCart(selectUOM);

	    // Application Logout
		TestReporter.logStep("Application Logout");
		MainNav mainNav = new MainNav(getDriver());
		mainNav.clickLogout();

	}

}

