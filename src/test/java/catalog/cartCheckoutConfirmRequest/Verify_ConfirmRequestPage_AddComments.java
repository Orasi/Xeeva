package catalog.cartCheckoutConfirmRequest;

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
import com.xeeva.catalog.ConfirmRequestPage;
import com.xeeva.catalog.CostCenterPage;
import com.xeeva.catalog.ItemDetailsPage;
import com.xeeva.catalog.RequisitioningPage;
import com.xeeva.catalog.SearchItems.GlobalItemsTab;
import com.xeeva.login.LoginPage;
import com.xeeva.navigation.MainNav;

/**
 * @summary Test To verify add comments functionality in confirm request page
 * @author  Lalitha Banda
 * @version	28/09/2016
 * *
 */

public class Verify_ConfirmRequestPage_AddComments extends TestEnvironment{

	// **************
	// Data Provider
	// **************
	@DataProvider(name = "dataScenario")
	public Object[][] scenarios() {
		try {
			Object[][] excelData = new ExcelDataProvider("/datasheets/UpdateCostCenter.xlsx","CostCenter").getTestData();
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
		testStart("Verify_ConfirmRequestPage_AddComments");
	}

	@AfterTest
	public void close(ITestContext testResults){
		endTest("TestAlert", testResults);
	}

	@Test(dataProvider = "dataScenario")
	public void CostCenterLineLevel(String role, String location,String InternalComment,String GlobalItem,String UnitofMeasure,String Quantity,
			String UnitPrice,String updateLineLevel,String updateHeaderLevel,String CCvalue,String QtValue){

		// Application Login 
		TestReporter.logStep("Login into application");
		LoginPage loginPage = new LoginPage(getDriver());
		loginPage.loginWithCredentials(role,location);

		TestReporter.logStep("Clicking the GlobalItems Link");
		RequisitioningPage reqPage = new RequisitioningPage(getDriver());
		reqPage.click_ReqTab();

		TestReporter.logStep("Navigating to MainNav Page");
		MainNav mainNav = new MainNav(getDriver());
		boolean getStatus = mainNav.verifyCartValue(GlobalItem);
		if(getStatus!=true){
			TestReporter.logStep("Clicking the GlobalItems Link");
			reqPage.perform_CatalogSearch(GlobalItem);

			TestReporter.logStep("Clicking the GlobalItems Link");
			GlobalItemsTab globalitems = new GlobalItemsTab(getDriver());
			globalitems.click_GlobalItemsTab();

			TestReporter.logStep("ItemDetailsPage  - Modifing Item Details");
			ItemDetailsPage itemdetails = new ItemDetailsPage(getDriver());
			itemdetails.add_TwoDiffrent_ItemsToCart(UnitPrice,Quantity,UnitofMeasure);
		}

		TestReporter.logStep("Cart CheckOut");
		mainNav.cart_CheckOut();

		TestReporter.logStep("Click on Continue CheckOut");
		CostCenterPage ccPage = new CostCenterPage(getDriver());
		ccPage.click_ContinueCheckOut();

		TestReporter.logStep("Adding Comments Line Level");
		ConfirmRequestPage crPage = new ConfirmRequestPage(getDriver());
		crPage.add_Comments(InternalComment);

		TestReporter.logStep("Application Logout");
		mainNav.clickLogout();

	}

}
