package aidocumentintelligence.com.cvs.document.intelligence;

import java.io.File;
import java.nio.file.Path;
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
 * Azure Document Intelligence - Custom Extraction Model
 *
 */
public class DocumentIntelligenceCustomExtractionModel {

	private static final String key = "1MS3yttMAeSMHHjxYWM8jFtJPUdlMNWaFwkMuPRVPZVKimsanQEiJQQJ99BBACYeBjFXJ3w3AAALACOGVseQ";
	private static final String endpoint = "https://adm-cvshealth-documentintelligence.cognitiveservices.azure.com/";

	public static void main(String[] args) {
		File indiaPassportDocument = new File(
				"C:\\Users\\151976\\OneDrive - Cognizant\\Desktop\\151976_Passport_Front.png\\");
		Path filePath = indiaPassportDocument.toPath();
		BinaryData customExtractionIndiaPassportDocumentData = BinaryData.fromFile(filePath, (int) indiaPassportDocument.length());

		// create `DocumentIntelligenceClient` instance
		DocumentIntelligenceClient client = new DocumentIntelligenceClientBuilder()
				.credential(new AzureKeyCredential(key)).endpoint(endpoint).buildClient();

		String modelId = "india.passport.doc.model";

		SyncPoller<AnalyzeOperationDetails, AnalyzeResult> analyzeCustomExtractionIndPassportResultPoller = client
				.beginAnalyzeDocument(modelId, new AnalyzeDocumentOptions(customExtractionIndiaPassportDocumentData));

		AnalyzeResult analyzeIndiaPassportResult = analyzeCustomExtractionIndPassportResultPoller.getFinalResult();
		
		for (int i = 0; i < analyzeIndiaPassportResult.getDocuments().size(); i++) {
			AnalyzedDocument analyzedIndiaPassport = analyzeIndiaPassportResult.getDocuments().get(i);
			Map<String, DocumentField> indiaPassportFields = analyzedIndiaPassport.getFields();
			
			DocumentField passportNumberField = indiaPassportFields.get("passportNumber");
			if (passportNumberField != null) {
				if (DocumentFieldType.STRING == passportNumberField.getType()) {
					String passportNo = passportNumberField.getValueString();
					System.out.printf("Passport Number: %s, confidence: %.2f%n", passportNo,
							passportNumberField.getConfidence());
				}
			}
			DocumentField countryCodeField = indiaPassportFields.get("countryCode");
			if (countryCodeField != null) {
				if (DocumentFieldType.STRING == countryCodeField.getType()) {
					String countryCode = countryCodeField.getValueString();
					System.out.printf("Country Code: %s, confidence: %.2f%n", countryCode,
							countryCodeField.getConfidence());
				}
			}
			DocumentField givenNameField = indiaPassportFields.get("givenName");
			if (givenNameField != null) {
				if (DocumentFieldType.STRING == givenNameField.getType()) {
					String name = givenNameField.getValueString();
					System.out.printf("Given Name: %s, confidence: %.2f%n", name,
							givenNameField.getConfidence());
				}
			}
			DocumentField nationalityField = indiaPassportFields.get("nationality");
			if (nationalityField != null) {
				if (DocumentFieldType.STRING == nationalityField.getType()) {
					String nationality = nationalityField.getValueString();
					System.out.printf("Nationality: %s, confidence: %.2f%n", nationality,
							nationalityField.getConfidence());
				}
			}
			DocumentField placeOfBirthField = indiaPassportFields.get("placeOfBirth");
			if (placeOfBirthField != null) {
				if (DocumentFieldType.STRING == placeOfBirthField.getType()) {
					String placeOfBirth = placeOfBirthField.getValueString();
					System.out.printf("Place of Birth: %s, confidence: %.2f%n", placeOfBirth,
							placeOfBirthField.getConfidence());
				}
			}
			DocumentField placeOfIssueField = indiaPassportFields.get("placeOfIssue");
			if (placeOfIssueField != null) {
				if (DocumentFieldType.STRING == placeOfIssueField.getType()) {
					String placeOfIssue = placeOfIssueField.getValueString();
					System.out.printf("Place of Issue: %s, confidence: %.2f%n", placeOfIssue,
							placeOfIssueField.getConfidence());
				}
			}
			DocumentField dateOfBirthField = indiaPassportFields.get("dateOfBirth");
			if (dateOfBirthField != null) {
				if (DocumentFieldType.STRING == dateOfBirthField.getType()) {
					String dateofbirth = dateOfBirthField.getValueString();
					System.out.printf("Date Of Birth: %s, confidence: %.2f%n", dateofbirth,
							dateOfBirthField.getConfidence());
				}
			}
		}

	}

}
