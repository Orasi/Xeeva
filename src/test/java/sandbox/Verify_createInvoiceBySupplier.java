package sandbox;

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
import com.xeeva.login.LoginPage;
import com.xeeva.marketPlace.InvoicesPage;

/**
 * @Summary: To verify the creation of invoice by supplier from Market Place application.
 * @author Praveen Namburi, @version: Created 18-10-2016
 */
public class Verify_createInvoiceBySupplier extends TestEnvironment{

		public String RequisitionType = "serviceRequestGeneral";
		
		// **************
		// Data Provider
		// **************
		@DataProvider(name = "dataScenario")
		public Object[][] scenarios() {
			try {
				Object[][] excelData = new ExcelDataProvider("/datasheets/Verify_CreateInvoiceBySupplier.xlsx",
						"CreateInvoiceBySupplier").getTestData();
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
			setApplicationUnderTest("XEEVAMKT");
			setBrowserUnderTest(browserUnderTest);
			setBrowserVersion(browserVersion);
			setOperatingSystem(operatingSystem);
			setRunLocation(runLocation);
			setTestEnvironment(environment);
			testStart("CreateInvoiceBySupplier");
		}

		/**
		 * @Description: Close the driver instance.
		 * @param testResults
		 */
		/*@AfterTest
		public void close(ITestContext testResults){
			endTest("TestAlert", testResults);
		}*/
		
		/**
		 * @Description: Main business-logic of the test-case resides here.
		 * @param role,location
		 */
		@Test(dataProvider = "dataScenario")
		public void createInvoiceBySupplier(String role,String location){
			
			/*,String ItemDescription,String UNSPSCCode,String SS,
			String CategoryType,String Category,String SubCategory,String MN,String MPN,
			String Quantity,String UnitofMeasure,String Price,String changeType,String selectCC,String BuyerRole,String Taxtype,
			String TaxCode,String ItemName,String ExpectedMsg,String ExpectedStatus,String supplier*/
			
			/*String[] QuantityArray = Quantity.split(";");
			String[] UOMArray = UnitofMeasure.split(";");
			String[] UPArray = Price.split(";");
			String[] ExpMsg = Quantity.split(";");
			String[] ExpStatus = UnitofMeasure.split(";");*/
			String PO_Number = "MJ100141";
			
			TestReporter.logStep("********************************************************************************");
			TestReporter.logStep("Login as Requester to Perform Check out Process and Genarate REQ Number");
			TestReporter.logStep("********************************************************************************");

			// Application Login 
			TestReporter.logStep("Login into application");
			LoginPage loginPage = new LoginPage(getDriver());
			String SupplierRole = "jyoti.mehta@xeeva.com";
			loginPage.loginWithRuntimeSupplierCredentials(SupplierRole, location);

			// Invoices Page  - Navigating to MarketPlace Home page.
			TestReporter.logStep("Navigate to MarketPlace home page and Click on Invoices link.");
			InvoicesPage invoicesPage = new InvoicesPage(getDriver());
			invoicesPage.clickInvoices();
			
			// Click on Create new Invoice button.
			TestReporter.log("Click on Create new Invoice button.");
			invoicesPage.clickbtnCreateNewInvoice();
			
			// Select Purchase Order to Create Invoice.
			TestReporter.log("Select Purchase Order to Create Invoice.");
			invoicesPage.selectPurchaseOrderToCreateInvoice(PO_Number);
			
			// SelectItem to CreateInvoice.
			TestReporter.log("SelectItem to CreateInvoice.");
			invoicesPage.selectItemToCreateInvoice();
			
			// Create Memo and Sumit Invoice details.
			TestReporter.log("Create Memo and Sumit Invoice details.");
			invoicesPage.createMemoAndSubmitInvoice(PO_Number);
			
			// Supplier Logout.
			TestReporter.log("Supplier Logout.");
			invoicesPage.supplierLogout();
		
		}
					
  }


