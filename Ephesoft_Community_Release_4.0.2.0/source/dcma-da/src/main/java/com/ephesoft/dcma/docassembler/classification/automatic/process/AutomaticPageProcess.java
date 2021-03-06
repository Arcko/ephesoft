/********************************************************************************* 
* Ephesoft is a Intelligent Document Capture and Mailroom Automation program 
* developed by Ephesoft, Inc. Copyright (C) 2015 Ephesoft Inc. 
* 
* This program is free software; you can redistribute it and/or modify it under 
* the terms of the GNU Affero General Public License version 3 as published by the 
* Free Software Foundation with the addition of the following permission added 
* to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK 
* IN WHICH THE COPYRIGHT IS OWNED BY EPHESOFT, EPHESOFT DISCLAIMS THE WARRANTY 
* OF NON INFRINGEMENT OF THIRD PARTY RIGHTS. 
* 
* This program is distributed in the hope that it will be useful, but WITHOUT 
* ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
* FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more 
* details. 
* 
* You should have received a copy of the GNU Affero General Public License along with 
* this program; if not, see http://www.gnu.org/licenses or write to the Free 
* Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 
* 02110-1301 USA. 
* 
* You can contact Ephesoft, Inc. headquarters at 111 Academy Way, 
* Irvine, CA 92617, USA. or at email address info@ephesoft.com. 
* 
* The interactive user interfaces in modified source and object code versions 
* of this program must display Appropriate Legal Notices, as required under 
* Section 5 of the GNU Affero General Public License version 3. 
* 
* In accordance with Section 7(b) of the GNU Affero General Public License version 3, 
* these Appropriate Legal Notices must retain the display of the "Ephesoft" logo. 
* If the display of the logo is not reasonably feasible for 
* technical reasons, the Appropriate Legal Notices must display the words 
* "Powered by Ephesoft". 
********************************************************************************/ 

package com.ephesoft.dcma.docassembler.classification.automatic.process;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.constant.BatchConstants;
import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Field;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.Batch.DocumentClassificationTypes;
import com.ephesoft.dcma.batch.schema.DocField.AlternateValues;
import com.ephesoft.dcma.batch.schema.Document.Pages;
import com.ephesoft.dcma.batch.schema.Page.PageLevelFields;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.EphesoftProperty;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.service.DocumentTypeService;
import com.ephesoft.dcma.da.service.PageTypeService;
import com.ephesoft.dcma.docassembler.DocumentAssembler;
import com.ephesoft.dcma.docassembler.constant.DocumentAssemblerConstants;
import com.ephesoft.dcma.docassembler.constant.PlaceHolder;
import com.ephesoft.dcma.util.EphesoftStringUtil;

/**
 * This class will process every page present at document type Unknown. This will read all the pages one by one and basis of the max of
 * other classification types it will create new documents and delete the current page from the document type Unknown.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.docassembler.DocumentAssembler
 */
public class AutomaticPageProcess {

	private static final String FORMAT = "#.##";

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AutomaticPageProcess.class);

	/**
	 * pageTypeService PageTypeService.
	 */
	private PageTypeService pageTypeService;

	/**
	 * Instance of PluginPropertiesService.
	 */
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * xmlDocuments List<DocumentType>.
	 */
	private List<Document> xmlDocuments;

	/**
	 * Batch instance ID.
	 */
	private String batchInstanceID;

	/**
	 * Reference of BatchSchemaService.
	 */
	private BatchSchemaService batchSchemaService;

	/**
	 * Reference of propertyMap.
	 */
	private Map<String, String> propertyMap;

	/**
	 * barcode Confidence score.
	 */
	private String barcodeConfidence;

	/**
	 * barcode Classification.
	 */
	private String barcodeClassification;

	/**
	 * Instance of PluginPropertiesService.
	 */
	private DocumentTypeService documentTypeService;

	/**
	 * 
	 */
	private String batchClassIdentifier;

	/**
	 * @return the propertyMap
	 */
	public Map<String, String> getPropertyMap() {
		return propertyMap;
	}

	/**
	 * @param propertyMap the propertyMap to set
	 */
	public void setPropertyMap(Map<String, String> propertyMap) {
		this.propertyMap = propertyMap;
	}

	/**
	 * @return the batchSchemaService
	 */
	public final BatchSchemaService getBatchSchemaService() {
		return batchSchemaService;
	}

	/**
	 * @param batchSchemaService the batchSchemaService to set
	 */
	public final void setBatchSchemaService(final BatchSchemaService batchSchemaService) {
		this.batchSchemaService = batchSchemaService;
	}

	/**
	 * @return the pageTypeService
	 */
	public final PageTypeService getPageTypeService() {
		return pageTypeService;
	}

	/**
	 * @param pageTypeService the pageTypeService to set
	 */
	public final void setPageTypeService(final PageTypeService pageTypeService) {
		this.pageTypeService = pageTypeService;
	}

	/**
	 * @return List<DocumentType>
	 */
	public final List<Document> getXmlDocuments() {
		return xmlDocuments;
	}

	/**
	 * @param xmlDocuments List<DocumentType>
	 */
	public final void setXmlDocuments(final List<Document> xmlDocuments) {
		this.xmlDocuments = xmlDocuments;
	}

	/**
	 * @return batchInstanceID
	 */
	public final String getBatchInstanceID() {
		return batchInstanceID;
	}

	/**
	 * @param batchInstanceID String
	 */
	public final void setBatchInstanceID(final String batchInstanceID) {
		this.batchInstanceID = batchInstanceID;
	}

	/**
	 * @return the pluginPropertiesService
	 */
	public PluginPropertiesService getPluginPropertiesService() {
		return pluginPropertiesService;
	}

	/**
	 * @param pluginPropertiesService the pluginPropertiesService to set
	 */
	public void setPluginPropertiesService(PluginPropertiesService pluginPropertiesService) {
		this.pluginPropertiesService = pluginPropertiesService;
	}

	/**
	 * @return the barcodeConfidence
	 */
	public final String getBarcodeConfidence() {
		return barcodeConfidence;
	}

	/**
	 * @param barcodeConfidence the barcodeConfidence to set
	 */
	public final void setBarcodeConfidence(final String barcodeConfidence) {
		this.barcodeConfidence = barcodeConfidence;
	}

	/**
	 * @return the barcodeClassification
	 */
	public final String getBarcodeClassification() {
		return barcodeClassification;
	}

	/**
	 * @param barcodeClassification the barcodeClassification to set
	 */
	public final void setBarcodeClassification(final String barcodeClassification) {
		this.barcodeClassification = barcodeClassification;
	}

	/**
	 * @return
	 */
	public DocumentTypeService getDocumentTypeService() {
		return documentTypeService;
	}

	/**
	 * @param documentTypeService
	 */
	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}

	/**
	 * @return
	 */
	public String getBatchClassIdentifier() {
		return batchClassIdentifier;
	}

	/**
	 * @param batchClassIdentifier
	 */
	public void setBatchClassIdentifier(String batchClassIdentifier) {
		this.batchClassIdentifier = batchClassIdentifier;
	}

	/**
	 * This method will set the document type name and ConfidenceThreshold.
	 * 
	 * @param docType DocumentType
	 * @param pageTypeName String
	 */
	public void setDocTypeNameAndConfThreshold(final Document docType, final String pageTypeName, final boolean isFromWebservice) {
		List<com.ephesoft.dcma.da.domain.DocumentType> docTypes = null;
		// if the call is from a web service then we wont have a batch instance identifier then we will have to fetch document types
		// using batch class identifier.
		if (isFromWebservice) {
			docTypes = documentTypeService.getDocTypeByBatchClassIdentifier(batchClassIdentifier, -1, -1);
		} else {
			docTypes = pluginPropertiesService.getDocTypeByPageTypeName(batchInstanceID, pageTypeName);
		}

		String docTypeName = null;
		String docTypeDesc = null;
		float minConfidenceThreshold = 0;

		if (null == docTypes || docTypes.isEmpty()) {
			LOGGER.info("No Document Type found for the input page type name.");
		} else if (isFromWebservice) {
			Iterator<com.ephesoft.dcma.da.domain.DocumentType> itr = docTypes.iterator();
			while (itr.hasNext()) {
				com.ephesoft.dcma.da.domain.DocumentType docTypeDB = itr.next();
				docTypeName = docTypeDB.getName();
				docTypeDesc = docTypeDB.getDescription();
				minConfidenceThreshold = docTypeDB.getMinConfidenceThreshold();
				LOGGER.debug(EphesoftStringUtil.concatenate("DocumentType name : ", docTypeName, "  minConfidenceThreshold : ",
						minConfidenceThreshold));
				if (null != docTypeName && null != pageTypeName && pageTypeName.indexOf(docTypeName) != -1) {
					break;
				}
			}
			if (null == docTypeName) {
				docTypeName = EphesoftProperty.UNKNOWN.getProperty();
				docTypeDesc = EphesoftProperty.UNKNOWN.getProperty();
				minConfidenceThreshold = 0;
			}
		} else {
			Iterator<com.ephesoft.dcma.da.domain.DocumentType> itr = docTypes.iterator();
			while (itr.hasNext()) {
				com.ephesoft.dcma.da.domain.DocumentType docTypeDB = itr.next();
				docTypeName = docTypeDB.getName();
				docTypeDesc = docTypeDB.getDescription();
				minConfidenceThreshold = docTypeDB.getMinConfidenceThreshold();
				LOGGER.debug(EphesoftStringUtil.concatenate("DocumentType name : ", docTypeName, "  minConfidenceThreshold : ",
						minConfidenceThreshold));
				if (null != docTypeName) {
					break;
				}
			}
		}

		if (null != docTypeName) {
			docType.setType(docTypeName);
			docType.setDescription(docTypeDesc);
			DecimalFormat twoDForm = new DecimalFormat(FORMAT);
			minConfidenceThreshold = Float.valueOf(twoDForm.format(minConfidenceThreshold));
			docType.setConfidenceThreshold(minConfidenceThreshold);
		} else {
			String errMsg = "DocumentType name is not found in the data base " + "for page type name: " + pageTypeName;
			LOGGER.info(errMsg);
		}
	}

	/**
	 * This method will create new document for pages that was found in the batch.xml file for Unknown type document.
	 * 
	 * @param docPageInfo List<PageType>
	 * @throws DCMAApplicationException Check for input parameters, create new documents for page found in document type Unknown.
	 */
	public final void createDocForPages(List<Document> insertAllDocument, final List<Page> docPageInfo, boolean isFromWebService)
			throws DCMAApplicationException {

		String errMsg = null;

		if (!isFromWebService && null == this.xmlDocuments) {
			throw new DCMAApplicationException("Unable to write pages for the document.");
		}

		try {

			List<Integer> removeIndexList = new ArrayList<Integer>();

			Document document = null;
			Long idGenerator = 0L;

			boolean isLast = true;
			boolean isFirst = true;

			for (int index = 0; index < docPageInfo.size(); index++) {

				Page pgType = docPageInfo.get(index);
				DocField docFieldType = getPgLevelField(pgType);
				if (null == docFieldType) {
					errMsg = "Invalid format of page level fields. DocFieldType found for "
							+ getPropertyMap().get(DocumentAssemblerConstants.AUTOMATIC_CLASSIFICATION) + " classification is null.";
					throw new DCMAApplicationException(errMsg);
				}

				String value = docFieldType.getValue();
				float confidenceScore = docFieldType.getConfidence();

				if (null == value) {
					errMsg = "Invalid format of page level fields. Value found for "
							+ getPropertyMap().get(DocumentAssemblerConstants.AUTOMATIC_CLASSIFICATION) + " classification is null.";
					throw new DCMAApplicationException(errMsg);
				}

				// check for zero confidence score value
				// for zero value just leave the page to unknown type.

				if (confidenceScore == 0) {
					document = new Document();
					Pages pages = new Pages();
					idGenerator++;
					document.setIdentifier(EphesoftProperty.DOCUMENT.getProperty() + idGenerator);
					document.setConfidence(BatchConstants.DEFAULT_FLOAT_INITIALIZIATION_VALUE);
					document.setConfidenceThreshold(BatchConstants.DEFAULT_FLOAT_INITIALIZIATION_VALUE);
					document.setReviewed(Boolean.FALSE);
					document.setValid(Boolean.FALSE);
					document.setPages(pages);
					document.setDocumentDisplayInfo(BatchConstants.EMPTY);
					insertAllDocument.add(document);
					document.getPages().getPage().add(pgType);
					document.setType(EphesoftProperty.UNKNOWN.getProperty());
					document.setDescription(EphesoftProperty.UNKNOWN.getProperty());
					removeIndexList.add(index);
					continue;
				}

				if (isLast) {
					document = new Document();
					Pages pages = new Pages();
					idGenerator++;
					document.setIdentifier(EphesoftProperty.DOCUMENT.getProperty() + idGenerator);
					document.setConfidence(BatchConstants.DEFAULT_FLOAT_INITIALIZIATION_VALUE);
					document.setConfidenceThreshold(BatchConstants.DEFAULT_FLOAT_INITIALIZIATION_VALUE);
					document.setReviewed(Boolean.FALSE);
					document.setValid(Boolean.FALSE);
					document.setPages(pages);
					document.setDocumentDisplayInfo(BatchConstants.EMPTY);
					insertAllDocument.add(document);
					isLast = false;
					isFirst = false;
				}

				if (value.contains(getPropertyMap().get(DocumentAssemblerConstants.CHECK_FIRST_PAGE))) {
					if (isFirst) {
						document = new Document();
						Pages pages = new Pages();
						idGenerator++;
						document.setIdentifier(EphesoftProperty.DOCUMENT.getProperty() + idGenerator);
						document.setConfidence(BatchConstants.DEFAULT_FLOAT_INITIALIZIATION_VALUE);
						document.setConfidenceThreshold(BatchConstants.DEFAULT_FLOAT_INITIALIZIATION_VALUE);
						document.setReviewed(Boolean.FALSE);
						document.setValid(Boolean.FALSE);
						document.setPages(pages);
						document.setDocumentDisplayInfo(BatchConstants.EMPTY);
						insertAllDocument.add(document);
					}
					isFirst = true;
					document.getPages().getPage().add(pgType);
					removeIndexList.add(index);
				} else {
					if (value.contains(getPropertyMap().get(DocumentAssemblerConstants.CHECK_MIDDLE_PAGE))) {
						document.getPages().getPage().add(pgType);
						removeIndexList.add(index);
						isFirst = true;
					} else {
						if (value.contains(getPropertyMap().get(DocumentAssemblerConstants.CHECK_LAST_PAGE))) {
							document.getPages().getPage().add(pgType);
							removeIndexList.add(index);
							isLast = true;
						} else {
							errMsg = "For page type value: " + value + "  and page ID : " + pgType.getIdentifier()
									+ " , Data format is not correct in the batch.xml file. "
									+ getPropertyMap().get(DocumentAssemblerConstants.CHECK_FIRST_PAGE) + " , "
									+ getPropertyMap().get(DocumentAssemblerConstants.CHECK_MIDDLE_PAGE) + " and "
									+ getPropertyMap().get(DocumentAssemblerConstants.CHECK_LAST_PAGE)
									+ " any of the three are not present to the name <Value> tag.";
							LOGGER.error(errMsg);
						}
					}
				}
			}
			if (isFromWebService) {
				updateBatchXMLAPI(insertAllDocument, isFromWebService);
			} else {
				// update the xml file.
				updateBatchXML(insertAllDocument, removeIndexList, isFromWebService);
			}

		} catch (Exception e) {
			errMsg = "Unable to write pages for the document. " + e.getMessage();
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg, e);
		}
	}

	/**
	 * Update Batch XML file.
	 * 
	 * @param insertAllDocument List<DocumentType>
	 * @param isFromWebService
	 * @param removeIndexList List<Integer>
	 * @throws DCMAApplicationException Check for input parameters, update the batch xml.
	 */
	private void updateBatchXMLAPI(List<Document> insertAllDocument, boolean isFromWebService) throws DCMAApplicationException {
		// set the confidence score and document type name on the basis of
		// defined rules.
		setDocConfAndDocType(insertAllDocument, isFromWebService);
		// Fetching the list of all the document types defined for a particular batch.
		List<DocumentType> documnetTypes = documentTypeService.getDocTypeByBatchClassIdentifier(batchClassIdentifier, -1, -1);
		// merge the unknown documents on the basis of a check
		String mergeSwitch = getPropertyMap().get(DocumentAssemblerConstants.DA_MERGE_UNKNOWN_DOCUMENT_SWITCH);
		int xmlDocSize = insertAllDocument.size();
		if (mergeSwitch != null && mergeSwitch.equalsIgnoreCase(DocumentAssemblerConstants.DA_SWITCH_ON) && xmlDocSize > 1) {
			for (int i = 1; i < insertAllDocument.size();) {
				final Document document = insertAllDocument.get(i);
				String docType = document.getType();
				if (null != docType && docType.equals(EphesoftProperty.UNKNOWN.getProperty())) {
					final Document prevDocument = insertAllDocument.get(i - 1);
					if (null != prevDocument.getType() && !prevDocument.getType().equals(EphesoftProperty.UNKNOWN.getProperty())) {
						prevDocument.getPages().getPage().addAll(document.getPages().getPage());
						insertAllDocument.remove(i);
					} else {
						i++;
					}
				} else {
					i++;
				}
			}
		}
		// resetting the list of identifiers.
		insertAllDocument = DocumentAssembler.setUpdatedListIdentifiers(insertAllDocument);

		// set default document type for unknown documents if the switch is on.
		String switchUnknownDocType = getPropertyMap().get(DocumentAssemblerConstants.SWITCH_UNKNOWN_PREDEFINED_DOCUMENT_TYPE);
		String unknownDocType = getPropertyMap().get(DocumentAssemblerConstants.UNKNOWN_PREDEFINED_DOCUMENT_TYPE).trim();
		if (null != switchUnknownDocType && DocumentAssemblerConstants.DA_SWITCH_ON.equalsIgnoreCase(switchUnknownDocType)) {
			if (null != unknownDocType && !unknownDocType.isEmpty()) {
				DocumentType unknownDocTypeDesc = DocumentAssembler.getDescriptionForDocument(pluginPropertiesService, unknownDocType,
						batchClassIdentifier, isFromWebService, documentTypeService);
				if (null != unknownDocTypeDesc) {
					for (Document doc : xmlDocuments) {
						if (EphesoftProperty.UNKNOWN.getProperty().equals(doc.getType())) {
							doc.setType(unknownDocType);
							doc.setDescription(unknownDocTypeDesc.getDescription());
						}
					}
				}
			}
		}
		// Fetching the value of the delete document's first page switch.
		String deleteDocumentFirstPageSwitch = getPropertyMap().get(DocumentAssemblerConstants.SWITCH_DELETE_DOCUMENT_FIRST_PAGE);
		for (int i = 0; i < insertAllDocument.size(); i++) {
			final Document document = insertAllDocument.get(i);
			// If there is only one document type defined except UNKNOWN then set the type of document to the name of that document. By
			// default the list will return a document UNKNOWN also so default value for one document type in web service and Test
			// content classification is set to 2.
			if (documnetTypes != null && documnetTypes.size() == 2) {
				DocumentType documentDesc = DocumentAssembler.getDescriptionForDocument(pluginPropertiesService, documnetTypes.get(1)
						.getName(), batchClassIdentifier, isFromWebService, documentTypeService);
				document.setType(documnetTypes.get(1).getName());
				document.setDescription(documentDesc.getDescription());
			}
			// Deleting the document's first page if the switch is ON.
			if (DocumentAssemblerConstants.DA_SWITCH_ON.equalsIgnoreCase(deleteDocumentFirstPageSwitch)) {
				DocumentAssembler.removeFirstPageOfDocument(document);
			}
		}
		LOGGER.info("updateBatchXML for web services done.");
	}

	/**
	 * Update Batch XML file.
	 * 
	 * @param insertAllDocument List<DocumentType>
	 * @param removeIndexList List<Integer>
	 * @throws DCMAApplicationException Check for input parameters, update the batch xml.
	 */
	private void updateBatchXML(final List<Document> insertAllDocument, final List<Integer> removeIndexList, boolean isFromWebService)
			throws DCMAApplicationException {

		String errMsg = null;

		Batch batch = batchSchemaService.getBatch(batchInstanceID);

		List<Document> xmlDocuments = batch.getDocuments().getDocument();
		if (null != xmlDocuments && null != insertAllDocument && !insertAllDocument.isEmpty()) {
			xmlDocuments.addAll(insertAllDocument);
		}

		List<Page> docPageInfo = null;

		int indexDocType = -1;

		for (int i = 0; i < xmlDocuments.size(); i++) {
			final Document document = xmlDocuments.get(i);
			String docType = document.getType();
			if (null != docType && docType.equals(EphesoftProperty.UNKNOWN.getProperty())) {
				docPageInfo = document.getPages().getPage();
				indexDocType = i;
				break;
			}
		}

		if (!removeIndexList.isEmpty()) {
			for (int index = removeIndexList.size() - 1; index >= 0; index--) {
				int rIndex = removeIndexList.get(index);
				if (rIndex < docPageInfo.size()) {
					docPageInfo.remove(rIndex);
				} else {
					errMsg = "Invalid argument for removal of pages for document type : " + EphesoftProperty.UNKNOWN.getProperty();
					LOGGER.error(errMsg);
					throw new DCMAApplicationException(errMsg);
				}
			}
		}

		// Delete the document type "Unknown" if no more pages are present.
		if (indexDocType != -1 && null != docPageInfo && docPageInfo.isEmpty()) {
			xmlDocuments.remove(indexDocType);
		}

		// set the confidence score and document type name on the basis of
		// defined rules.
		setDocConfAndDocType(xmlDocuments, isFromWebService);

		// merge the unknown documents on the basis of a check
		String mergeSwitch = getPropertyMap().get(DocumentAssemblerConstants.DA_MERGE_UNKNOWN_DOCUMENT_SWITCH);
		int xmlDocSize = xmlDocuments.size();
		if (mergeSwitch != null && mergeSwitch.equalsIgnoreCase(DocumentAssemblerConstants.DA_SWITCH_ON) && xmlDocSize > 1) {
			for (int i = 1; i < xmlDocuments.size();) {
				final Document document = xmlDocuments.get(i);
				String docType = document.getType();
				if (null != docType && docType.equals(EphesoftProperty.UNKNOWN.getProperty())) {
					final Document prevDocument = xmlDocuments.get(i - 1);
					if (null != prevDocument.getType() && !prevDocument.getType().equals(EphesoftProperty.UNKNOWN.getProperty())) {
						prevDocument.getPages().getPage().addAll(document.getPages().getPage());
						xmlDocuments.remove(i);
					} else {
						i++;
					}
				} else {
					i++;
				}
			}
		}
		// resetting the list of identifiers.
		xmlDocuments = DocumentAssembler.setUpdatedListIdentifiers(xmlDocuments);

		// set default document type for unknown documents if the switch is on.
		String switchUnknownDocType = getPropertyMap().get(DocumentAssemblerConstants.SWITCH_UNKNOWN_PREDEFINED_DOCUMENT_TYPE);
		String unknownDocType = getPropertyMap().get(DocumentAssemblerConstants.UNKNOWN_PREDEFINED_DOCUMENT_TYPE).trim();
		if (null != switchUnknownDocType && DocumentAssemblerConstants.DA_SWITCH_ON.equalsIgnoreCase(switchUnknownDocType)) {
			if (null != unknownDocType && !unknownDocType.isEmpty()) {
				DocumentType unknownDocTypeDesc = DocumentAssembler.getDescriptionForDocument(pluginPropertiesService, unknownDocType,
						batchInstanceID, isFromWebService, null);
				if (null != unknownDocTypeDesc) {
					for (Document doc : xmlDocuments) {
						if (EphesoftProperty.UNKNOWN.getProperty().equals(doc.getType())) {
							doc.setType(unknownDocType);
							doc.setDescription(unknownDocTypeDesc.getDescription());
						}
					}
				}
			}
		}
		// setting the batch level documentClassificationTypes.
		DocumentClassificationTypes documentClassificationTypes = new DocumentClassificationTypes();
		List<String> documentClassificationType = documentClassificationTypes.getDocumentClassificationType();
		documentClassificationType.add(getPropertyMap().get(DocumentAssemblerConstants.FACTORY_CLASSIFICATION));
		batch.setDocumentClassificationTypes(documentClassificationTypes);

		// Fetching the list of all the document types defined.
		List<String> documnetTypes = DocumentAssembler.getDocumentTypes(pluginPropertiesService, batchInstanceID);
		// Fetching the value of the delete document's first page switch.
		String deleteDocumentFirstPageSwitch = getPropertyMap().get(DocumentAssemblerConstants.SWITCH_DELETE_DOCUMENT_FIRST_PAGE);
		// Set the error message explicitly to blank to display the node in batch xml
		for (int i = 0; i < xmlDocuments.size(); i++) {
			final Document document = xmlDocuments.get(i);
			// If there is only one document type defined then set the type of document to the name of that document.
			if (documnetTypes != null && documnetTypes.size() == 1) {
				DocumentType documentDesc = DocumentAssembler.getDescriptionForDocument(pluginPropertiesService, documnetTypes.get(0),
						batchInstanceID, isFromWebService, null);
				document.setType(documnetTypes.get(0));
				document.setDescription(documentDesc.getDescription());
			}
			document.setErrorMessage("");
			// Deleting the document's first page if the switch is ON.
			if (DocumentAssemblerConstants.DA_SWITCH_ON.equalsIgnoreCase(deleteDocumentFirstPageSwitch)) {
				DocumentAssembler.removeFirstPageOfDocument(document);
			}
		}
		// now write the state of the object to the xml file.
		batchSchemaService.updateBatch(batch);

		LOGGER.info("updateBatchXML done.");

	}

	/**
	 * This method will retrieve the page level field type docFieldType for any input page type for automatic classification. It
	 * fetches the type with a max confidence score.In case of equal max scores , the precedence is given to the order as defined in
	 * the include list in the properties file.
	 * 
	 * @param pgType PageType
	 * @return docFieldType DocFieldType
	 */
	private DocField getPgLevelField(final Page pgType) {
		StringTokenizer tokenList = new StringTokenizer(getPropertyMap().get(DocumentAssemblerConstants.AUTOMATIC_INCLUDE_LIST), ";=");
		ArrayList<String> includeList = new ArrayList<String>();
		while (tokenList.hasMoreElements()) {
			String includeListElem = (String) tokenList.nextElement();
			includeList.add(includeListElem);
		}
		String name = "";
		int lastIncludeListIndex = -1, includeListIndex = -1;
		DocField docFieldType = null;
		Float confidence, maxConfidence = -1f;
		if (null != pgType) {
			PageLevelFields pageLevelFields = pgType.getPageLevelFields();
			if (null != pageLevelFields) {
				List<DocField> pageLevelField = pageLevelFields.getPageLevelField();
				for (DocField docFdType : pageLevelField) {
					if (null != docFdType) {
						name = docFdType.getName();
						includeListIndex = contains(includeList, name);
						if (includeListIndex == -1) {
							continue;
						}
						confidence = docFdType.getConfidence();
						if (maxConfidence == -1 || null == docFieldType || confidence >= maxConfidence) {
							if (confidence > maxConfidence) {
								maxConfidence = confidence;
								docFieldType = docFdType;
								lastIncludeListIndex = includeListIndex;
							} else if (includeListIndex < lastIncludeListIndex) {
								maxConfidence = confidence;
								docFieldType = docFdType;
								lastIncludeListIndex = includeListIndex;
							}
						}
					}
				}
			}
		}

		return docFieldType;

	}

	/**
	 * This method will set the confidence score of every document on the basis of average of all the page confidence score.
	 * 
	 * @param xmlDocuments List<DocumentType>
	 * @param isFromWebService
	 * @throws DCMAApplicationException
	 */
	@SuppressWarnings("unchecked")
	private void setDocConfAndDocType(final List<Document> xmlDocuments, boolean isFromWebService) throws DCMAApplicationException {

		Map<String, Object[]> docConfidence = null;
		String classificationType = null;
		for (Document docType : xmlDocuments) {
			String type = docType.getType();
			if (null != type && type.equals(EphesoftProperty.UNKNOWN.getProperty())) {
				continue;
			}
			classificationType = checkBarcodeDAClassification(docType, isFromWebService);

			if (classificationType != null && classificationType.indexOf(DocumentAssemblerConstants.BARCODE_TYPE_NAME) > -1) {
				continue;
			}
			docConfidence = new HashMap<String, Object[]>();
			processDocument(docType, classificationType, docConfidence, isFromWebService);
			type = docType.getType();
			if (type == null || type.isEmpty()) {
				docType.setType(EphesoftProperty.UNKNOWN.getProperty());
				docType.setDescription(EphesoftProperty.UNKNOWN.getProperty());
			}
		}

	}

	@SuppressWarnings("unchecked")
	private void processDocument(final Document docType, final String classificationType, final Map<String, Object[]> docConfidence,
			boolean isFromWebService) throws DCMAApplicationException {

		float confidenceScoreMax = 0.0f;
		String pageTypeName = null;
		float localSum = 0.0f;

		Pages pages = docType.getPages();
		if (null != pages) {
			List<Page> pageList = pages.getPage();
			for (Page pgType : pageList) {
				if (null != pgType) {
					PageLevelFields pageLevelFields = pgType.getPageLevelFields();
					if (null != pageLevelFields) {
						traversePgFds(pageLevelFields, docConfidence, classificationType);
					}
				}
			}
		}
		if (docConfidence != null && !docConfidence.isEmpty()) {
			Set<String> set = docConfidence.keySet();
			Iterator<String> itr = set.iterator();
			while (itr.hasNext()) {
				String key = itr.next();
				Object[] objArr = docConfidence.get(key);
				List<Float> conFloatList = (List<Float>) objArr[1];
				localSum = 0.0f;
				for (Float f : conFloatList) {
					localSum += f;
				}
				if (confidenceScoreMax < localSum) {
					confidenceScoreMax = localSum;
					pageTypeName = key;
				}
			}

			Object[] objArr = docConfidence.get(pageTypeName);
			List<Float> conFloatList = (List<Float>) objArr[1];
			List<String> checkTypeList = (List<String>) objArr[0];

			float multiplyingFactor = 1.00f;
			try {
				multiplyingFactor = multiplyingFactor(checkTypeList);
			} catch (Exception e) {
				LOGGER.error("Invalid multiplyingFactor. " + e.getMessage());
			}

			float confidenceScore = (multiplyingFactor * confidenceScoreMax) / conFloatList.size();
			LOGGER.info("multiplyingFactor : " + multiplyingFactor + "  confidence score : " + confidenceScore
					+ " for document Type ID : " + docType.getIdentifier());

			DecimalFormat twoDForm = new DecimalFormat(FORMAT);
			confidenceScore = Float.valueOf(twoDForm.format(confidenceScore));

			docType.setConfidence(confidenceScore);
			if (null != checkTypeList && !checkTypeList.isEmpty()) {
				StringBuilder stringBuilder = new StringBuilder("");
				stringBuilder.append(pageTypeName);
				stringBuilder.append(checkTypeList.get(0));
				pageTypeName = stringBuilder.toString();
			} else {
				StringBuilder stringBuilder = new StringBuilder("");
				stringBuilder.append(pageTypeName);
				stringBuilder.append(getPropertyMap().get(DocumentAssemblerConstants.CHECK_FIRST_PAGE));
				pageTypeName = stringBuilder.toString();
			}
			if (isFromWebService) {
				setDocTypeNameAndConfThresholdAPI(docType, pageTypeName);
			} else {
				setDocTypeNameAndConfThreshold(docType, pageTypeName, isFromWebService);
			}
		}
	}

	public void setDocTypeNameAndConfThresholdAPI(final Document docType, final String pageTypeName) {

		/*
		 * List<com.ephesoft.dcma.da.domain.DocumentType> docTypes = pageTypeService.getDocTypeByPageTypeName(pageTypeName,
		 * batchInstanceID);
		 */
		List<com.ephesoft.dcma.da.domain.DocumentType> docTypes = documentTypeService.getDocTypeByBatchClassIdentifier(
				batchClassIdentifier, -1, -1);

		String docTypeName = null, docTypeNameTemp = null;
		String docTypeDesc = null;
		float minConfidenceThreshold = 0;

		final Iterator<com.ephesoft.dcma.da.domain.DocumentType> itr = docTypes.iterator();
		while (itr.hasNext()) {
			final com.ephesoft.dcma.da.domain.DocumentType docTypeDB = itr.next();
			docTypeNameTemp = docTypeDB.getName();
			if (pageTypeName.contains(docTypeNameTemp)) {
				docTypeName = docTypeNameTemp;
				docTypeDesc = docTypeDB.getDescription();
				minConfidenceThreshold = docTypeDB.getMinConfidenceThreshold();
				LOGGER.debug(EphesoftStringUtil.concatenate("DocumentType name : ", docTypeName, "  minConfidenceThreshold : ",
						minConfidenceThreshold));
				break;
			}
		}

		if (null == docTypeName) {
			final String errMsg = "DocumentType name is not found in the data base " + "for page type name: " + pageTypeName;
			LOGGER.info(errMsg);
		} else {
			docType.setType(docTypeName);
			docType.setDescription(docTypeDesc);
			final DecimalFormat twoDForm = new DecimalFormat(FORMAT);
			minConfidenceThreshold = Float.valueOf(twoDForm.format(minConfidenceThreshold));
			docType.setConfidenceThreshold(minConfidenceThreshold);
		}
	}

	private String checkBarcodeDAClassification(final Document docType, final boolean isFromWebservice)
			throws DCMAApplicationException {

		List<Page> pages = docType.getPages().getPage();
		String errMsg = null;
		String name = null;
		String value = DocumentAssemblerConstants.EMPTY;
		for (int index = 0; index < pages.size(); index++) {

			Page pgType = pages.get(index);
			DocField docField = getPgLevelField(pgType);
			value = docField.getValue();

			if (null == value) {
				errMsg = "Invalid format of page level fields. Value found for " + getBarcodeClassification()
						+ " classification is null.";
				throw new DCMAApplicationException(errMsg);
			}
			name = docField.getName();
			if (docField.getName().indexOf(DocumentAssemblerConstants.BARCODE_TYPE_NAME) > -1) {
				setDocTypeNameAndConfThreshold(docType, value, isFromWebservice);
				int confidenceValue = 0;
				try {
					confidenceValue = Integer.parseInt(getBarcodeConfidence());
				} catch (NumberFormatException nfe) {
					errMsg = "Invalid integer for barcode confidence score in properties file." + nfe.getMessage();
					LOGGER.error(errMsg);
					throw new DCMAApplicationException(errMsg, nfe);
				}
				docType.setConfidence(Float.valueOf(confidenceValue));
				break;
			}
		}
		return name;
	}

	/**
	 * This method will apply the rule to calculate the confidence score.
	 * 
	 * @param checkTypeList List<String>
	 * @return multiplyingFactor float
	 */
	private float multiplyingFactor(List<String> checkTypeList) {

		// fp + mp + lp = 1
		// fp = 0.50
		// lp = 0.50
		// mp = 0.25
		// fp + lp = 0.75
		// fp + mp = 0.50
		// mp + lp = 0.50

		float multiplyingFactor = 1.00f;
		float intialFactor = 1.00f;
		String checkFirstPage = getPropertyMap().get(DocumentAssemblerConstants.CHECK_FIRST_PAGE);
		String checkMiddlePage = getPropertyMap().get(DocumentAssemblerConstants.CHECK_MIDDLE_PAGE);
		String checkLastPage = getPropertyMap().get(DocumentAssemblerConstants.CHECK_LAST_PAGE);
		int fmlPage = Integer.parseInt(getPropertyMap().get(DocumentAssemblerConstants.RULE_FML_PAGE));
		int fPage = Integer.parseInt(getPropertyMap().get(DocumentAssemblerConstants.RULE_F_PAGE));
		int mPage = Integer.parseInt(getPropertyMap().get(DocumentAssemblerConstants.RULE_M_PAGE));
		int lPage = Integer.parseInt(getPropertyMap().get(DocumentAssemblerConstants.RULE_L_PAGE));
		int fmPage = Integer.parseInt(getPropertyMap().get(DocumentAssemblerConstants.RULE_FM_PAGE));
		int flPage = Integer.parseInt(getPropertyMap().get(DocumentAssemblerConstants.RULE_FL_PAGE));
		int mlPage = Integer.parseInt(getPropertyMap().get(DocumentAssemblerConstants.RULE_ML_PAGE));

		if (null != checkTypeList) {

			// A = First_Page
			// B = Middle_Page
			// C = Last_Page
			// CBA
			// 101 = 5
			// 100 = 4
			// 111 = 7
			// 010 = 2

			int placeHolder = 0;

			if (checkTypeList.contains(checkFirstPage)) {
				placeHolder = placeHolder | PlaceHolder.FP.getValue();
			}

			if (checkTypeList.contains(checkMiddlePage)) {
				placeHolder = placeHolder | PlaceHolder.MP.getValue();
			}

			if (checkTypeList.contains(checkLastPage)) {
				placeHolder = placeHolder | PlaceHolder.LP.getValue();
			}

			switch (PlaceHolder.getPlaceHolder(placeHolder)) {
				case FP:
					// multiplyingFactor = 0.50f;
					multiplyingFactor = (fPage * intialFactor) / fmlPage;
					break;
				case MP:
					// multiplyingFactor = 0.25f;
					multiplyingFactor = (mPage * intialFactor) / fmlPage;
					break;
				case FMP:
					// multiplyingFactor = 0.50f;
					multiplyingFactor = (fmPage * intialFactor) / fmlPage;
					break;
				case LP:
					// multiplyingFactor = 0.50f;
					multiplyingFactor = (lPage * intialFactor) / fmlPage;
					break;
				case FLP:
					// multiplyingFactor = 0.75f;
					multiplyingFactor = (flPage * intialFactor) / fmlPage;
					break;
				case MLP:
					// multiplyingFactor = 0.50f;
					multiplyingFactor = (mlPage * intialFactor) / fmlPage;
					break;
				case FMLP:
					// multiplyingFactor = 1.00f;
					multiplyingFactor = (fmlPage * intialFactor) / fmlPage;
					break;
				default:
					// multiplyingFactor = 0.50f;
					multiplyingFactor = (fPage * intialFactor) / fmlPage;
					break;
			}
		}

		return multiplyingFactor;
	}

	/**
	 * This method will traverse the page level fields.
	 * 
	 * @param pageLevelFields PageLevelFields
	 * @param docConfidence Map<String, List<Float>>
	 */
	@SuppressWarnings("unchecked")
	private void traversePgFds(PageLevelFields pageLevelFields, Map<String, Object[]> docConfidence, String classificationType) {

		String value = null;
		String checkType = null;
		String name = null;
		List<DocField> pageLevelField = pageLevelFields.getPageLevelField();
		for (DocField docFdType : pageLevelField) {
			if (null != docFdType) {
				name = docFdType.getName();
				if (null != name && name.contains(classificationType)) {
					AlternateValues alternateValues = docFdType.getAlternateValues();
					if (null != alternateValues) {
						List<Field> alternateValue = alternateValues.getAlternateValue();
						value = docFdType.getValue();
						if (value.contains(getPropertyMap().get(DocumentAssemblerConstants.CHECK_FIRST_PAGE))) {
							checkType = getPropertyMap().get(DocumentAssemblerConstants.CHECK_FIRST_PAGE);
						} else if (value.contains(getPropertyMap().get(DocumentAssemblerConstants.CHECK_MIDDLE_PAGE))) {
							checkType = getPropertyMap().get(DocumentAssemblerConstants.CHECK_MIDDLE_PAGE);
						} else if (value.contains(getPropertyMap().get(DocumentAssemblerConstants.CHECK_LAST_PAGE))) {
							checkType = getPropertyMap().get(DocumentAssemblerConstants.CHECK_LAST_PAGE);
						} else {
							LOGGER.info("In valid page level value tag.");
						}
						//if doc field type is empty then do nothing.
						if (null != checkType) {
							for (Field fdType : alternateValue) {
								String val = fdType.getValue();
								float confidence = fdType.getConfidence();
								if (null != val && val.contains(checkType)) {
									String[] arr = val.split(checkType);
									if (null != arr && arr.length > 0) {
										Object[] objArr = docConfidence.get(arr[0]);
										List<Float> conFloatList = null;
										List<String> checkTypeList = null;
										if (null == objArr) {
											objArr = new Object[2];
											conFloatList = new ArrayList<Float>();
											checkTypeList = new ArrayList<String>();
										} else {
											checkTypeList = (List<String>) objArr[0];
											conFloatList = (List<Float>) objArr[1];
										}
										checkTypeList.add(checkType);
										conFloatList.add(confidence);
										objArr[0] = checkTypeList;
										objArr[1] = conFloatList;
										docConfidence.put(arr[0], objArr);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * This method will read all the pages of the document for document type Unknown.
	 * 
	 * @return docPageInfo List<PageType>
	 * @throws DCMAApplicationException Check for input parameters and read all pages of the document.
	 */
	public final List<Page> readAllPages() throws DCMAApplicationException {
		LOGGER.info("Reading the document for Document Assembler.");
		List<Page> docPageInfo = null;

		final Batch batch = batchSchemaService.getBatch(this.batchInstanceID);

		this.xmlDocuments = batch.getDocuments().getDocument();

		for (int i = 0; i < this.xmlDocuments.size(); i++) {
			final Document document = this.xmlDocuments.get(i);
			String docType = document.getType();
			if (null != docType && docType.equals(EphesoftProperty.UNKNOWN.getProperty())) {
				docPageInfo = document.getPages().getPage();
				break;
			}
		}

		return docPageInfo;
	}

	/**
	 * Method to check whether one of the include list String as mentioned in the properties file , is contained in the xml
	 * 
	 * @param list include list from the properties file
	 * @param str String to be checked
	 * @return index at which the string is present in the include list
	 */
	private int contains(List<String> list, String str) {
		int isFound = -1;
		if (str == null) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) == null) {
					isFound = i;
				}
			}
		} else {
			for (int i = 0; i < list.size(); i++) {
				if (str.equalsIgnoreCase(list.get(i)) || str.indexOf(list.get(i)) > -1) {
					isFound = i;
				}
			}
		}
		return isFound;
	}

	/**
	 * This method reclassifies any document that is not classified yet and set confidence threshold for the same.
	 * 
	 * @param documentList List{@link Document}
	 * @throws {@link DCMAApplicationException}
	 */
	public void reClassifyDocuments(final List<Document> documentList) throws DCMAApplicationException {
		LOGGER.debug("Entering method reClassifyDocuments for batch instance identifier = " + batchInstanceID);
		if (documentList != null && !documentList.isEmpty()) {
			Map<String, Object[]> docConfidence = null;
			String docId = null;
			Long idGenerator = 0L;
			for (Document document : documentList) {
				String classificationType = checkBarcodeDAClassification(document, false);
				if (classificationType != null && classificationType.indexOf(getBarcodeClassification()) > -1) {
					continue;
				}
				idGenerator++;
				document.setDocumentDisplayInfo(BatchConstants.EMPTY);
				docId = EphesoftProperty.DOCUMENT.getProperty() + idGenerator;
				document.setIdentifier(docId);
				String docType = document.getType();
				docConfidence = new HashMap<String, Object[]>();
				LOGGER.debug("Processing document " + docId + " having type " + docType);
				if (docType == null || docType.isEmpty()) {

					// If document type is null or empty, perform reclassification.
					processDocument(document, classificationType, docConfidence, false);
				} else {

					// If document has already been classified, set its confidence threshold.
					setConfThreshold(docType, document);
				}
				docType = document.getType();
				if (docType == null || docType.isEmpty()) {
					document.setType(EphesoftProperty.UNKNOWN.getProperty());
					document.setDescription(EphesoftProperty.UNKNOWN.getProperty());
				}
			}
		}
		LOGGER.debug("Exiting method reClassifyDocuments for batch instance identifier = " + batchInstanceID);
	}

	private void setConfThreshold(final String docTypeName, final Document document) {
		if (docTypeName != null && document != null) {
			com.ephesoft.dcma.da.domain.DocumentType docType = documentTypeService.getDocTypeByBatchClassAndDocTypeName(
					batchClassIdentifier, docTypeName);
			if (docType != null) {
				float confThreshold = docType.getMinConfidenceThreshold();
				document.setConfidenceThreshold(confThreshold);
			}
		}
	}
}
