	package catalog.cartCheckoutProcess;
	
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
	import com.xeeva.catalog.ItemDetailsPage;
	import com.xeeva.catalog.RequisitioningPage;
	import com.xeeva.catalog.SearchItems.GlobalItemsTab;
	import com.xeeva.login.LoginPage;
	import com.xeeva.navigation.MainNav;
	
	
	/**
	 * @summary Test To verify copy item at line level for existing reservation
	 * @author  Lalitha Banda
	 * @version	20/09/2016
	 * *
	 */
	
	public class Verify_CopiedItem_LineLevel extends TestEnvironment{
		public String itemType = "copyitem";
	
		// **************
		// Data Provider
		// **************
		@DataProvider(name = "dataScenario")
		public Object[][] scenarios() {
			try {
				Object[][] excelData = new ExcelDataProvider("/datasheets/AddInternalComments.xlsx","Verify_AddInternalComments").getTestData();
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
			testStart("Verify_CopiedItem_LineLevel");
		}
	
		@AfterTest
		public void close(ITestContext testResults){
			endTest("TestAlert", testResults);
		}
	
		@Test(dataProvider = "dataScenario")
		public void CopiedItem(String role, String location,String InternalComment,String GlobalItem,String UnitofMeasure,String Quantity,String UnitPrice){
	
			// Application Login 
			TestReporter.logStep("Application Login");
			LoginPage loginPage = new LoginPage(getDriver());
			loginPage.loginWithCredentials(role,location);
	
			// Requisition Page   
			TestReporter.logStep("Navigating to requisition page to perform catalog search");
			RequisitioningPage reqPage = new RequisitioningPage(getDriver());
			reqPage.click_ReqTab();
	
			TestReporter.logStep("Navigating to MainNav Page");
			MainNav mainNav = new MainNav(getDriver());
			boolean getStatus = mainNav.verifyCartValue(GlobalItem);
			if(getStatus!=true){
				TestReporter.logStep("Performing Catalog Search");
				reqPage.perform_CatalogSearch(GlobalItem);
	
				TestReporter.logStep("Clicking the GlobalItems Link");
				GlobalItemsTab globalitems = new GlobalItemsTab(getDriver());
				globalitems.click_GlobalItemsTab();
	
				TestReporter.logStep("ItemDetailsPage  - Modifing Item Details");
				ItemDetailsPage itemdetails = new ItemDetailsPage(getDriver());
				itemdetails.add_TwoDiffrent_ItemsToCart(UnitPrice,Quantity,UnitofMeasure);
			}
	
			//Clicking the CartItemsLink
			TestReporter.logStep("Clicked on CartItemsLink");
			mainNav.cart_CheckOut();
	
			//Clicking and Adding Internal Comments in CostCenter Page
			TestReporter.logStep("Clicking and Adding Internal Comments");
			CostCenterPage costCenterPage = new CostCenterPage(getDriver());
			costCenterPage.AddInternalComment(InternalComment);
	
			//Clicking CopyItem Link in CostCenter Page
			TestReporter.logStep("Clicking Copy Item Link");
			String ItemNumber = costCenterPage.readItemNuber();
			System.out.println( "Item Number : "+ItemNumber);
			costCenterPage.click_CopyItemLink();
	
			//Clicking Select Button of CostCenter to Copy Item
			TestReporter.logStep("Clicking on Select Button of CostCenter to Copy Item");
			String ccValue = costCenterPage.selectCCToCopyItem();
	
			//Clicking CopyItem Link in CostCenter Page
			TestReporter.logStep("Clicking Copy Item Link");
			costCenterPage.verifyCostCenter(itemType,ItemNumber,ccValue,null);
	
			// Application Logout
			TestReporter.logStep("Application Logout");
			mainNav.clickLogout();
		}
	}
