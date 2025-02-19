package aidocumentintelligence.com.cvs.document.intelligence;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Map;

import com.azure.ai.documentintelligence.DocumentIntelligenceClient;
import com.azure.ai.documentintelligence.DocumentIntelligenceClientBuilder;
import com.azure.ai.documentintelligence.models.AnalyzeDocumentOptions;
import com.azure.ai.documentintelligence.models.AnalyzeOperationDetails;
import com.azure.ai.documentintelligence.models.AnalyzeResult;
import com.azure.ai.documentintelligence.models.AnalyzedDocument;
import com.azure.ai.documentintelligence.models.DocumentField;
import com.azure.ai.documentintelligence.models.DocumentFieldType;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.util.BinaryData;
import com.azure.core.util.polling.SyncPoller;

/**
 * Azure Document Intelligence - Prebuilt Invoice Model
 *
 */
public class DocumentIntelligencePrebuiltInvoice {

	private static final String key = "Replace this text with your doc intelligence access key";
	private static final String endpoint = "Replace this text with your doc intelligence endpoint";

	public static void main(String[] args) {
		File invoiceDocument = new File(
				"C:\\Users\\151976\\OneDrive - Cognizant\\Desktop\\input_prebuilt_invoice_model.PNG");
		Path filePath = invoiceDocument.toPath();
		BinaryData prebuiltInvoiceDocumentData = BinaryData.fromFile(filePath, (int) invoiceDocument.length());

		// create `DocumentIntelligenceClient` instance
		DocumentIntelligenceClient client = new DocumentIntelligenceClientBuilder()
				.credential(new AzureKeyCredential(key)).endpoint(endpoint).buildClient();

		String modelId = "prebuilt-invoice";

		SyncPoller<AnalyzeOperationDetails, AnalyzeResult> analyzeprebuiltInvoiceResultPoller = client
				.beginAnalyzeDocument(modelId, new AnalyzeDocumentOptions(prebuiltInvoiceDocumentData));

		AnalyzeResult analyzeInvoiceResult = analyzeprebuiltInvoiceResultPoller.getFinalResult();

		for (int i = 0; i < analyzeInvoiceResult.getDocuments().size(); i++) {
			AnalyzedDocument analyzedInvoice = analyzeInvoiceResult.getDocuments().get(i);
			Map<String, DocumentField> invoiceFields = analyzedInvoice.getFields();

			System.out.printf("----------- Recognizing invoice %d -----------%n", i);

			DocumentField vendorNameField = invoiceFields.get("VendorName");
			if (vendorNameField != null) {
				if (DocumentFieldType.STRING == vendorNameField.getType()) {
					String vendorName = vendorNameField.getValueString();
					System.out.printf("Vendor Name: %s, confidence: %.2f%n", vendorName,
							vendorNameField.getConfidence());
				}
			}

			DocumentField vendorAddressField = invoiceFields.get("VendorAddress");
			if (vendorAddressField != null) {
				if (DocumentFieldType.ADDRESS == vendorAddressField.getType()) {
					String streetAddress = vendorAddressField.getValueAddress().getStreetAddress();
					String city = vendorAddressField.getValueAddress().getCity();
					String state = vendorAddressField.getValueAddress().getState();
					String postalCode = vendorAddressField.getValueAddress().getPostalCode();
					System.out.printf("Vendor Address: %s, confidence: %.2f%n",
							streetAddress + " " + city + " " + state + " " + postalCode,
							vendorAddressField.getConfidence());
				}
			}

			DocumentField customerNameField = invoiceFields.get("CustomerName");
			if (customerNameField != null) {
				if (DocumentFieldType.STRING == customerNameField.getType()) {
					String customerName = customerNameField.getValueString();
					System.out.printf("Customer Name: %s, confidence: %.2f%n", customerName,
							customerNameField.getConfidence());
				}
			}

			DocumentField customerIdField = invoiceFields.get("CustomerId");
			if (customerIdField != null) {
				if (DocumentFieldType.STRING == customerIdField.getType()) {
					String customerId = customerIdField.getValueString();
					System.out.printf("Customer Id: %s, confidence: %.2f%n", customerId,
							customerIdField.getConfidence());
				}
			}

			DocumentField customerAddressField = invoiceFields.get("CustomerAddress");
			if (customerAddressField != null) {
				if (DocumentFieldType.ADDRESS == customerAddressField.getType()) {
					String streetAddress = customerAddressField.getValueAddress().getStreetAddress();
					String city = customerAddressField.getValueAddress().getCity();
					String state = customerAddressField.getValueAddress().getState();
					String postalCode = customerAddressField.getValueAddress().getPostalCode();
					System.out.printf("Customer Address: %s, confidence: %.2f%n",
							streetAddress + " " + city + " " + state + " " + postalCode,
							customerAddressField.getConfidence());
				}
			}

			DocumentField InvoiceIdField = invoiceFields.get("InvoiceId");
			if (InvoiceIdField != null) {
				if (DocumentFieldType.STRING == InvoiceIdField.getType()) {
					String invoiceId = InvoiceIdField.getValueString();
					System.out.printf("Invoice Id: %s, confidence: %.2f%n", invoiceId, InvoiceIdField.getConfidence());
				}
			}

			DocumentField invoiceDateField = invoiceFields.get("InvoiceDate");
			if (invoiceDateField != null) {
				if (DocumentFieldType.DATE == invoiceDateField.getType()) {
					LocalDate invoiceDate = invoiceDateField.getValueDate();
					System.out.printf("Invoice Date: %s, confidence: %.2f%n", invoiceDate,
							invoiceDateField.getConfidence());
				}

			}

		}

	}

}
