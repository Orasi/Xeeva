package catalog.CartCheckoutDetails;

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
import com.xeeva.catalog.RecentOrderInformationPage;
import com.xeeva.catalog.RequisitioningPage;
import com.xeeva.catalog.SearchItems.GlobalItemsTab;
import com.xeeva.login.LoginPage;
import com.xeeva.navigation.MainNav;

/**
 * @summary Test To verify requester is able to add more items at CheckOut Details Page
 * @author  Lalitha Banda
 * @version	28/09/2016
 * *
 */

public class Verify_CheckOutDetails_AddMoreItems extends TestEnvironment {

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
	public void setup(@Optional String runLocation, String browserUnderTest,String browserVersion, 
			String operatingSystem, String environment) {
		setApplicationUnderTest("XEEVA");
		setBrowserUnderTest(browserUnderTest);
		setBrowserVersion(browserVersion);
		setOperatingSystem(operatingSystem);
		setRunLocation(runLocation);
		setTestEnvironment(environment);
		testStart("Verify_CheckOutDetails_AddMoreItems");
	}

	@AfterTest
	public void close(ITestContext testResults){
		endTest("TestAlert", testResults);
	}

	@Test(dataProvider = "dataScenario")
	public void CostCenterLineLevel(String role, String location,String InternalComment,String GlobalItem,
			String UnitofMeasure,String Quantity,String UnitPrice,String updateLineLevel,
			String updateHeaderLevel,String CCvalue,String QtValue) {

		// Application Login 
		TestReporter.logStep("Login into application");
		LoginPage loginPage = new LoginPage(getDriver());
		loginPage.loginWithCredentials(role,location);

		// Requisition Page   - Clicking the Requisition Tab
		TestReporter.logStep("Clicking the Requisition Tab");
		RequisitioningPage reqPage = new RequisitioningPage(getDriver());
		reqPage.click_ReqTab();

		// Main Navigation Page  - Navigating to MainNav Page and adding items if cart is empty
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

		//Clicking the CartItemsLink and Checkout Button
		TestReporter.logStep("Clicking on CartItemsLink and Checkout Button");
		mainNav.cart_CheckOut();

		// CostCenter Page - Clicking on Continue CheckOut Button 
		TestReporter.logStep("Clicking on Continue CheckOut Button");
		CostCenterPage ccPage = new CostCenterPage(getDriver());
		ccPage.click_ContinueCheckOut();

		// ConfirmRequest Page - Clicking on Confirm Button
		TestReporter.logStep("Clicking on Confirm Button");
		ConfirmRequestPage cReqPage = new ConfirmRequestPage(getDriver());
		cReqPage.click_Confirm();

		// CheckOutDetails Page - Clicking on Shop for more items button 
		TestReporter.logStep("Clicking on Shop for more items ");
		ccPage.click_ShopForMoreItems();

		//Requisition Page 
		TestReporter.logStep("Adding Items from Search ");
		TestReporter.logStep("Clicking the GlobalItems Link");
		reqPage.perform_CatalogSearch(GlobalItem);

		// GlobalItemsTab  - Clicking the GlobalItems Link
		TestReporter.logStep("Clicking the GlobalItems Link");
		GlobalItemsTab globalitems = new GlobalItemsTab(getDriver());
		globalitems.click_GlobalItemsTab();

		//Adding two different items to cart 
		TestReporter.logStep("ItemDetailsPage  - Modifing Item Details");
		ItemDetailsPage itemdetails = new ItemDetailsPage(getDriver());
		itemdetails.add_TwoDiffrent_ItemsToCart(UnitPrice,Quantity,UnitofMeasure);

		// Requisition Page   - Clicking the Requisition Tab
		TestReporter.logStep("Clicking the Requisition Tab");
		reqPage.click_ReqTab();

		// Verify And Add Items From Recent Orders Tab
		TestReporter.logStep("Cart CheckOut");
		mainNav.cart_CheckOut();

		// CostCenter Page - Clicking on Continue CheckOut Button 
		TestReporter.logStep("Clicking on Continue CheckOut Button");
		ccPage.click_ContinueCheckOut();

		// ConfirmRequest Page - Clicking on Confirm Button
		TestReporter.logStep("Clicking on Confirm Button");
		cReqPage.click_Confirm();

		// CheckOutDetails Page - Clicking on Shop for more items button 
		TestReporter.logStep("Clicking on Shop for more items ");
		ccPage.click_ShopForMoreItems();

		// Requisition Page   - Clicking the Requisition Tab
		TestReporter.logStep("Clicking the Requisition Tab");
		reqPage.click_ReqTab();

		TestReporter.logStep("Adding Items from Recent Orders");
		RecentOrderInformationPage recentOrderInfoPage = new RecentOrderInformationPage(getDriver());
		TestReporter.logStep("Navigating to Recent Order Information page.");
		recentOrderInfoPage.click_ItemLink_RecentOrderPage();
		recentOrderInfoPage.Click_ItemLink_RecentOrderInfoPage();

		ItemDetailsPage itemDetailsPage = new ItemDetailsPage(getDriver());
		TestReporter.logStep("Navigating to Item Details page.");
		itemDetailsPage.selectUOMValueAndAddNonPriceItemToCart(UnitofMeasure);

		// Requisition Page   - Clicking the Requisition Tab
		TestReporter.logStep("Clicking the Requisition Tab");
		reqPage.click_ReqTab();
		// Verify And Add Items From Reject Orders Tab
		TestReporter.logStep("Cart CheckOut");
		mainNav.cart_CheckOut();

		// CostCenter Page - Clicking on Continue CheckOut Button 
		TestReporter.logStep("Clicking on Continue CheckOut Button");
		ccPage.click_ContinueCheckOut();

		// ConfirmRequest Page - Clicking on Confirm Button
		TestReporter.logStep("Clicking on Confirm Button");
		cReqPage.click_Confirm();

		// CheckOutDetails Page - Clicking on Shop for more items button 
		TestReporter.logStep("Clicking on Shop for more items ");
		ccPage.click_ShopForMoreItems();

		// Requisition Page   - Clicking the Requisition Tab
		TestReporter.logStep("Clicking the Requisition Tab");
		reqPage.click_ReqTab();

		TestReporter.logStep("Add Item From Reject Orders Tab");
		recentOrderInfoPage.click_RejectOrder();
		recentOrderInfoPage.Click_ItemLink_RejectOrdersPage();

		TestReporter.logStep("Navigating to Item Details page.");
		itemDetailsPage.selectUOMValueAndAddNonPriceItemToCart(UnitofMeasure);

		TestReporter.logStep(" Click on Req Tab ");
		reqPage.click_ReqTab();

		// Verify And Add Items From Reject Orders Tab
		TestReporter.logStep("Cart CheckOut");
		mainNav.cart_CheckOut();

		// CostCenter Page - Clicking on Continue CheckOut Button 
		TestReporter.logStep("Clicking on Continue CheckOut Button");
		ccPage.click_ContinueCheckOut();

		// ConfirmRequest Page - Clicking on Confirm Button
		TestReporter.logStep("Clicking on Confirm Button");
		cReqPage.click_Confirm();

		// Application Logout
		TestReporter.logStep("Application Logout");
		mainNav.clickLogout();
	}
}


