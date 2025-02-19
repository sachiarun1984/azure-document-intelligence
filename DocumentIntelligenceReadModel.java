package aidocumentintelligence.com.cvs.document.intelligence;

import java.io.File;
import java.nio.file.Path;

import com.azure.ai.documentintelligence.DocumentIntelligenceClient;
import com.azure.ai.documentintelligence.DocumentIntelligenceClientBuilder;
import com.azure.ai.documentintelligence.models.AnalyzeDocumentOptions;
import com.azure.ai.documentintelligence.models.AnalyzeOperationDetails;
import com.azure.ai.documentintelligence.models.AnalyzeResult;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.util.BinaryData;
import com.azure.core.util.polling.SyncPoller;

/**
 * Azure Document Intelligence - Read Model
 *
 */
public class DocumentIntelligenceReadModel {

	private static final String key = "1MS3yttMAeSMHHjxYWM8jFtJPUdlMNWaFwkMuPRVPZVKimsanQEiJQQJ99BBACYeBjFXJ3w3AAALACOGVseQ";
	private static final String endpoint = "https://adm-cvshealth-documentintelligence.cognitiveservices.azure.com/";

	public static void main(String[] args) {

		File readDocument = new File("C:\\Users\\151976\\OneDrive - Cognizant\\Desktop\\input_read_model.pdf");
		Path filePath = readDocument.toPath();
		BinaryData readDocumentData = BinaryData.fromFile(filePath, (int) readDocument.length());

		// create `DocumentIntelligenceClient` instance
		DocumentIntelligenceClient client = new DocumentIntelligenceClientBuilder()
				.credential(new AzureKeyCredential(key)).endpoint(endpoint).buildClient();

		String modelId = "prebuilt-read";

		SyncPoller<AnalyzeOperationDetails, AnalyzeResult> analyzeReadResultPoller = client
				.beginAnalyzeDocument(modelId, new AnalyzeDocumentOptions(readDocumentData));

		AnalyzeResult analyzeReadResult = analyzeReadResultPoller.getFinalResult();

		// pages
		analyzeReadResult.getPages().forEach(documentPage -> {
			System.out.printf("Page has width: %.2f and height: %.2f, measured with unit: %s%n",
					documentPage.getWidth(), documentPage.getHeight(), documentPage.getUnit());

			// lines
			documentPage.getLines()
					.forEach(documentLine -> System.out.printf("Line %s is within a bounding polygon %s.%n",
							documentLine.getContent(), documentLine.getPolygon().toString()));

			// words
			documentPage.getWords()
					.forEach(documentWord -> System.out.printf("Word '%s' has a confidence score of %.2f.%n",
							documentWord.getContent(), documentWord.getConfidence()));
		});
	}
}
