package aidocumentintelligence.com.cvs.document.intelligence;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;
import java.time.LocalDate;

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
 * Azure Document Intelligence - Prebuilt Check Model
 *
 */
public class DocumentIntelligencePrebuiltBankCheck {

	private static final String key = "Replace this text with your doc intelligence access key";
	private static final String endpoint = "Replace this text with your doc intelligence endpoint";

	public static void main(String[] args) {
		File checkDocument = new File(
				"C:\\Users\\151976\\OneDrive - Cognizant\\Desktop\\input_prebuilt_check_model.PNG");
		Path filePath = checkDocument.toPath();
		BinaryData prebuiltCheckDocumentData = BinaryData.fromFile(filePath, (int) checkDocument.length());

		// create `DocumentIntelligenceClient` instance
		DocumentIntelligenceClient client = new DocumentIntelligenceClientBuilder()
				.credential(new AzureKeyCredential(key)).endpoint(endpoint).buildClient();

		String modelId = "prebuilt-check.us";

		SyncPoller<AnalyzeOperationDetails, AnalyzeResult> analyzeprebuiltCheckResultPoller = client
				.beginAnalyzeDocument(modelId, new AnalyzeDocumentOptions(prebuiltCheckDocumentData));

		AnalyzeResult analyzeCheckResult = analyzeprebuiltCheckResultPoller.getFinalResult();

		for (int i = 0; i < analyzeCheckResult.getDocuments().size(); i++) {
			AnalyzedDocument analyzedCheck = analyzeCheckResult.getDocuments().get(i);
			Map<String, DocumentField> checkFields = analyzedCheck.getFields();

			System.out.printf("----------- Analyzing Check  %d -----------%n", i);

			DocumentField bankNameField = checkFields.get("BankName");
			if (bankNameField != null) {
				if (DocumentFieldType.STRING == bankNameField.getType()) {
					String merchantName = bankNameField.getValueString();
					System.out.printf("Vendor Name: %s, confidence: %.2f%n", merchantName,
							bankNameField.getConfidence());
				}
			}

			DocumentField checkDateField = checkFields.get("CheckDate");
			if (checkDateField != null) {
				if (DocumentFieldType.DATE == checkDateField.getType()) {
					LocalDate checkDate = checkDateField.getValueDate();
					System.out.printf("Check Date: %s, confidence: %.2f%n", checkDate, checkDateField.getConfidence());
				}
			}

			DocumentField micrField = checkFields.get("MICR");
			if (micrField != null) {
				if (DocumentFieldType.OBJECT == micrField.getType()) {
					String accountNumber = micrField.getValueMap().get("AccountNumber").getValueString();
					String routingNumber = micrField.getValueMap().get("RoutingNumber").getValueString();
					String checkNumber = micrField.getValueMap().get("CheckNumber").getValueString();
					System.out.printf("Account Number: %s, confidence: %.2f%n", accountNumber,
							micrField.getConfidence());
					System.out.printf("Routing Number: %s, confidence: %.2f%n", routingNumber,
							micrField.getConfidence());
					System.out.printf("Check Number: %s, confidence: %.2f%n", checkNumber, micrField.getConfidence());
				}
			}

			DocumentField numberAmountField = checkFields.get("NumberAmount");
			if (numberAmountField != null) {
				if (DocumentFieldType.NUMBER == numberAmountField.getType()) {
					double dollarAmount = numberAmountField.getValueNumber();
					System.out.printf("Dollar Amount: %s, confidence: %.2f%n", dollarAmount,
							numberAmountField.getConfidence());
				}

			}
			
			DocumentField payerNameField = checkFields.get("PayerName");
			if (payerNameField != null) {
				if (DocumentFieldType.STRING == payerNameField.getType()) {
					String payerName = payerNameField.getValueString();
					System.out.printf("Payer Name: %s, confidence: %.2f%n", payerName,
							payerNameField.getConfidence());
				}

			}
			
			DocumentField payerToField = checkFields.get("PayTo");
			if (payerToField != null) {
				if (DocumentFieldType.STRING == payerToField.getType()) {
					String payerTo = payerToField.getValueString();
					System.out.printf("Payer To: %s, confidence: %.2f%n", payerTo,
							payerToField.getConfidence());
				}

			}
			
		}
	}
}
