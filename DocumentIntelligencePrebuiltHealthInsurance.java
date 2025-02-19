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
 * Azure Document Intelligence - Prebuilt Health Insurance ID Model
 *
 */
public class DocumentIntelligencePrebuiltHealthInsurance {
	

	private static final String key = "Replace this text with your doc intelligence access key";
	private static final String endpoint = "Replace this text with your doc intelligence endpoint";

	public static void main(String[] args) {
		File healthInsuranceDocument = new File(
				"C:\\Users\\151976\\OneDrive - Cognizant\\Desktop\\input_prebuilt_healthinsurance_model.PNG");
		Path filePath = healthInsuranceDocument.toPath();
		BinaryData prebuiltHealthInsuranceDocumentData = BinaryData.fromFile(filePath, (int) healthInsuranceDocument.length());

		// create `DocumentIntelligenceClient` instance
		DocumentIntelligenceClient client = new DocumentIntelligenceClientBuilder()
				.credential(new AzureKeyCredential(key)).endpoint(endpoint).buildClient();

		String modelId = "prebuilt-healthInsuranceCard.us";

		SyncPoller<AnalyzeOperationDetails, AnalyzeResult> analyzeprebuiltHealthInsuranceResultPoller = client
				.beginAnalyzeDocument(modelId, new AnalyzeDocumentOptions(prebuiltHealthInsuranceDocumentData));

		AnalyzeResult analyzeHealthInsuranceResult = analyzeprebuiltHealthInsuranceResultPoller.getFinalResult();
		
		for (int i = 0; i < analyzeHealthInsuranceResult.getDocuments().size(); i++) {
			AnalyzedDocument analyzedHealthInsurance = analyzeHealthInsuranceResult.getDocuments().get(i);
			Map<String, DocumentField> healthInsuranceFields = analyzedHealthInsurance.getFields();
			
			DocumentField groupNumberField = healthInsuranceFields.get("GroupNumber");
			if (groupNumberField != null) {
				if (DocumentFieldType.STRING == groupNumberField.getType()) {
					String groupNumber = groupNumberField.getValueString();
					System.out.printf("Group Number: %s, confidence: %.2f%n", groupNumber,
							groupNumberField.getConfidence());
				}
			}
			
			DocumentField idNumberField = healthInsuranceFields.get("IdNumber");
			if (idNumberField != null) {
				if (DocumentFieldType.OBJECT == idNumberField.getType()) {
					String number = idNumberField.getValueMap().get("Number").getValueString();
					String prefix = idNumberField.getValueMap().get("Prefix").getValueString();
					System.out.printf("IDCard Number: %s, confidence: %.2f%n", prefix+number,
							idNumberField.getConfidence());
				}
			}
			
			DocumentField insurerField = healthInsuranceFields.get("Insurer");
			if (insurerField != null) {
				if (DocumentFieldType.STRING == insurerField.getType()) {
					String insurerName = insurerField.getValueString();
					System.out.printf("Insurer: %s, confidence: %.2f%n", insurerName,
							insurerField.getConfidence());
				}
			}
			
			DocumentField memberInfoField = healthInsuranceFields.get("Member");
			if (memberInfoField != null) {
				if (DocumentFieldType.OBJECT == memberInfoField.getType()) {
					String employerName = memberInfoField.getValueMap().get("Employer").getValueString();
					String idNumberSuffix = memberInfoField.getValueMap().get("IdNumberSuffix").getValueString();
					String name = memberInfoField.getValueMap().get("Name").getValueString();
					System.out.printf("Employer Name: %s, confidence: %.2f%n", employerName,
							memberInfoField.getConfidence());
					System.out.printf("ID Number Suffix: %s, confidence: %.2f%n", idNumberSuffix,
							memberInfoField.getConfidence());
					System.out.printf("Member Name: %s, confidence: %.2f%n", name,
							memberInfoField.getConfidence());
				}
			}
			
			DocumentField prescriptionInfoField = healthInsuranceFields.get("PrescriptionInfo");
			if (prescriptionInfoField != null) {
				if (DocumentFieldType.OBJECT == prescriptionInfoField.getType()) {
					String RxBin = prescriptionInfoField.getValueMap().get("RxBIN").getValueString();
					String RxGrp = prescriptionInfoField.getValueMap().get("RxGrp").getValueString();
					System.out.printf("RxBin: %s, confidence: %.2f%n", RxBin,
							prescriptionInfoField.getConfidence());
					System.out.printf("RxGrp: %s, confidence: %.2f%n", RxGrp,
							prescriptionInfoField.getConfidence());
				}
			}
			
			
		}

	}

}
