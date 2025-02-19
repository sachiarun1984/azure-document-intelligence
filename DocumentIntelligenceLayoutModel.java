package aidocumentintelligence.com.cvs.document.intelligence;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import com.azure.ai.documentintelligence.DocumentIntelligenceClient;
import com.azure.ai.documentintelligence.DocumentIntelligenceClientBuilder;
import com.azure.ai.documentintelligence.models.AnalyzeDocumentOptions;
import com.azure.ai.documentintelligence.models.AnalyzeOperationDetails;
import com.azure.ai.documentintelligence.models.AnalyzeResult;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.ai.documentintelligence.models.DocumentTable;
import com.azure.core.util.BinaryData;
import com.azure.core.util.polling.SyncPoller;

/**
 * Azure Document Intelligence - Layout Model
 *
 */
public class DocumentIntelligenceLayoutModel {

	private static final String key = "1MS3yttMAeSMHHjxYWM8jFtJPUdlMNWaFwkMuPRVPZVKimsanQEiJQQJ99BBACYeBjFXJ3w3AAALACOGVseQ";
	private static final String endpoint = "https://adm-cvshealth-documentintelligence.cognitiveservices.azure.com/";

	public static void main(String[] args) {

		File readDocument = new File("C:\\Users\\151976\\OneDrive - Cognizant\\Desktop\\input_layout_model.pdf");
		Path filePath = readDocument.toPath();
		BinaryData layoutDocumentData = BinaryData.fromFile(filePath, (int) readDocument.length());

		// create `DocumentIntelligenceClient` instance
		DocumentIntelligenceClient client = new DocumentIntelligenceClientBuilder()
				.credential(new AzureKeyCredential(key)).endpoint(endpoint).buildClient();

		String modelId = "prebuilt-layout";

		SyncPoller<AnalyzeOperationDetails, AnalyzeResult> analyzeLayoutResultPoller = client
				.beginAnalyzeDocument(modelId, new AnalyzeDocumentOptions(layoutDocumentData));

		AnalyzeResult analyzeLayoutResult = analyzeLayoutResultPoller.getFinalResult();

		// pages
		analyzeLayoutResult.getPages().forEach(documentPage -> {
			System.out.printf("Page has width: %.2f and height: %.2f, measured with unit: %s%n",
					documentPage.getWidth(), documentPage.getHeight(), documentPage.getUnit());

			// lines
			documentPage.getLines()
					.forEach(documentLine -> System.out.printf("Line '%s' is within a bounding box %s.%n",
							documentLine.getContent(), documentLine.getPolygon().toString()));

			// selection marks
			documentPage.getSelectionMarks()
					.forEach(documentSelectionMark -> System.out.printf(
							"Selection mark is '%s' and is within a bounding box %s with confidence %.2f.%n",
							documentSelectionMark.getState().toString(), documentSelectionMark.getPolygon().toString(),
							documentSelectionMark.getConfidence()));

		});

		// tables
		List<DocumentTable> tables = analyzeLayoutResultPoller.getFinalResult().getTables();
		for (int i = 0; i < tables.size(); i++) {
			DocumentTable documentTable = tables.get(i);
			System.out.printf("Table %d has %d rows and %d columns.%n", i, documentTable.getRowCount(),
					documentTable.getColumnCount());
			documentTable.getCells().forEach(documentTableCell -> {
				System.out.printf("Cell '%s', has row index %d and column index %d.%n", documentTableCell.getContent(),
						documentTableCell.getRowIndex(), documentTableCell.getColumnIndex());
			});
			System.out.println();
		}

		// styles
		analyzeLayoutResult.getStyles().forEach(
				documentStyle -> System.out.printf("Document is handwritten %s.%n", documentStyle.isHandwritten()));
	}
}
