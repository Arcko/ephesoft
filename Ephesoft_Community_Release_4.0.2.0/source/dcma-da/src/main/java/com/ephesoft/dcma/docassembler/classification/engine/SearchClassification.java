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

package com.ephesoft.dcma.docassembler.classification.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchPlugin;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.docassembler.DocumentAssembler;
import com.ephesoft.dcma.docassembler.DocumentAssemblerProperties;
import com.ephesoft.dcma.docassembler.classification.DocumentClassification;
import com.ephesoft.dcma.docassembler.classification.engine.process.LucenePageProcess;
import com.ephesoft.dcma.docassembler.constant.DocumentAssemblerConstants;
import com.ephesoft.dcma.docassembler.factory.DocumentClassificationFactory;

/**
 * This class does the actual processing on the basis of Search classification for document assembly. It will read all the pages of the
 * document type Unknown and then create new documents for all these pages found.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.docassembler.classification.DocumentClassification
 */
public class SearchClassification implements DocumentClassification {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchClassification.class);

	/**
	 * This method will process all the unclassified pages present at document type Unknown. Process every page one by one and create
	 * new documents.
	 * 
	 * @param documentAssembler DocumentAssembler
	 * @param batchInstanceID Long
	 * @throws DCMAApplicationException If any invalid parameter found.
	 */
	@Override
	public final void processUnclassifiedPages(final DocumentAssembler documentAssembler, final String batchInstanceIdentifier,
			final PluginPropertiesService pluginPropertiesService) throws DCMAApplicationException {

		LOGGER.info("Setting all the fields for LucenePageProcess.");

		LucenePageProcess lucenePageProcess = new LucenePageProcess();

		lucenePageProcess.setPageTypeService(documentAssembler.getPageTypeService());
		lucenePageProcess.setPluginPropertiesService(pluginPropertiesService);
		lucenePageProcess.setBatchInstanceIdentifier(batchInstanceIdentifier);
		lucenePageProcess.setBatchSchemaService(documentAssembler.getBatchSchemaService());

		Map<String, String> propertyMap = new HashMap<String, String>();
		propertyMap.put(DocumentAssemblerConstants.LUCENE_CLASSIFICATION, documentAssembler.getLuceneClassification());
		propertyMap.put(DocumentAssemblerConstants.CHECK_FIRST_PAGE, documentAssembler.getCheckFirstPage());
		propertyMap.put(DocumentAssemblerConstants.CHECK_MIDDLE_PAGE, documentAssembler.getCheckMiddlePage());
		propertyMap.put(DocumentAssemblerConstants.CHECK_LAST_PAGE, documentAssembler.getCheckLastPage());

		LOGGER.info("Initializing properties...");
		BatchPlugin batchPlugin = pluginPropertiesService.getPluginProperties(batchInstanceIdentifier,
				DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN);

		String factoryClassification = batchPlugin.getPluginConfigurationValue(DocumentAssemblerProperties.DA_FACTORY_CLASS);
		String ruleFMLPage = batchPlugin.getPluginConfigurationValue(DocumentAssemblerProperties.DA_RULE_FP_MP_LP);
		String ruleFPage = batchPlugin.getPluginConfigurationValue(DocumentAssemblerProperties.DA_RULE_FP);
		String ruleMPage = batchPlugin.getPluginConfigurationValue(DocumentAssemblerProperties.DA_RULE_MP);
		String ruleLPage = batchPlugin.getPluginConfigurationValue(DocumentAssemblerProperties.DA_RULE_LP);
		String ruleFLPage = batchPlugin.getPluginConfigurationValue(DocumentAssemblerProperties.DA_RULE_FP_LP);
		String ruleMLPage = batchPlugin.getPluginConfigurationValue(DocumentAssemblerProperties.DA_RULE_MP_LP);
		String ruleFMPage = batchPlugin.getPluginConfigurationValue(DocumentAssemblerProperties.DA_RULE_FP_MP);
		String mergeUnknownSwitch = batchPlugin
				.getPluginConfigurationValue(DocumentAssemblerProperties.DA_MERGE_UNKNOWN_DOCUMENT_SWITCH);
		String deleteDocFirstPageSwitch = batchPlugin
				.getPluginConfigurationValue(DocumentAssemblerProperties.DA_DELETE_DOCUMENT_FIRST_PAGE_SWITCH);
		String firstPageConfThreshold = batchPlugin
				.getPluginConfigurationValue(DocumentAssemblerProperties.DA_FIRST_PAGE_CONFIDENCE_THRESHOLD);
		String middlePageConfThreshold = batchPlugin
				.getPluginConfigurationValue(DocumentAssemblerProperties.DA_MIDDLE_PAGE_CONFIDENCE_THRESHOLD);
		String lastPageConfThreshold = batchPlugin
				.getPluginConfigurationValue(DocumentAssemblerProperties.DA_LAST_PAGE_CONFIDENCE_THRESHOLD);
		String advancedDASwitch = batchPlugin.getPluginConfigurationValue(DocumentAssemblerProperties.DA_ADVANCED_ALGORITHM);
		String predefinedDocType = batchPlugin.getPluginConfigurationValue(DocumentAssemblerProperties.DA_PREDEFINED_DOCUMENT_TYPE);
		String predefinedDocTypeConfThreshold = batchPlugin
				.getPluginConfigurationValue(DocumentAssemblerProperties.DA_PREDEFINED_DOCUMENT_CONFIDENCE_THRESHOLD);
		String unknownPredefinedDocType = batchPlugin
				.getPluginConfigurationValue(DocumentAssemblerProperties.DA_UNKNOWN_PREDEFINED_DOCUMENT_TYPE);
		String switchUnknownPredefinedDocType = batchPlugin
				.getPluginConfigurationValue(DocumentAssemblerProperties.DA_SWITCH_UNKNOWN_PREDEFINED_DOCUMENT_TYPE);
		LOGGER.info("Properties Initialized Successfully");
		if(null == predefinedDocType){
			predefinedDocType = DocumentAssemblerConstants.EMPTY;
		}
		if(null == unknownPredefinedDocType){
			unknownPredefinedDocType = DocumentAssemblerConstants.EMPTY;
		}
		
		propertyMap.put(DocumentAssemblerConstants.FACTORY_CLASSIFICATION, factoryClassification);
		propertyMap.put(DocumentAssemblerConstants.RULE_FL_PAGE, ruleFLPage);
		propertyMap.put(DocumentAssemblerConstants.RULE_FML_PAGE, ruleFMLPage);
		propertyMap.put(DocumentAssemblerConstants.RULE_FM_PAGE, ruleFMPage);
		propertyMap.put(DocumentAssemblerConstants.RULE_F_PAGE, ruleFPage);
		propertyMap.put(DocumentAssemblerConstants.RULE_L_PAGE, ruleLPage);
		propertyMap.put(DocumentAssemblerConstants.RULE_ML_PAGE, ruleMLPage);
		propertyMap.put(DocumentAssemblerConstants.RULE_M_PAGE, ruleMPage);
		propertyMap.put(DocumentAssemblerConstants.DA_MERGE_UNKNOWN_DOCUMENT_SWITCH, mergeUnknownSwitch);
		propertyMap.put(DocumentAssemblerConstants.SWITCH_DELETE_DOCUMENT_FIRST_PAGE, deleteDocFirstPageSwitch);
		propertyMap.put(DocumentAssemblerConstants.FIRST_PAGE_CONFIDENCE_THRESHOLD, firstPageConfThreshold);
		propertyMap.put(DocumentAssemblerConstants.MIDDLE_PAGE_CONFIDENCE_THRESHOLD, middlePageConfThreshold);
		propertyMap.put(DocumentAssemblerConstants.LAST_PAGE_CONFIDENCE_THRESHOLD, lastPageConfThreshold);
		propertyMap.put(DocumentAssemblerConstants.PREDEFINED_DOCUMENT_TYPE, predefinedDocType);
		propertyMap.put(DocumentAssemblerConstants.PREDEFINED_DOCUMENT_CONFIDENCE_THRESHOLD, predefinedDocTypeConfThreshold);
		propertyMap.put(DocumentAssemblerConstants.DA_ADVANCED_ALGORITHM, advancedDASwitch);
		propertyMap.put(DocumentAssemblerConstants.UNKNOWN_PREDEFINED_DOCUMENT_TYPE, unknownPredefinedDocType);
		propertyMap.put(DocumentAssemblerConstants.SWITCH_UNKNOWN_PREDEFINED_DOCUMENT_TYPE, switchUnknownPredefinedDocType);

		lucenePageProcess.setPropertyMap(propertyMap);
		// read all the pages of the document for document type
		// Unknown.
		List<Page> docPageInfo = lucenePageProcess.readAllPages();

		if (null != docPageInfo) {
			// create new document for pages that was found in the
			// batch.xml file for Unknown type document.
			List<Document> insertAllDocument = new ArrayList<Document>();
			lucenePageProcess.createDocForPages(insertAllDocument, docPageInfo, false);
		}

	}

	public List<Document> processUnclassifiedPagesAPI(List<Page> docPageInfo, DocumentAssembler documentAssembler,
			String batchClassID, PluginPropertiesService pluginPropertiesService) throws DCMAApplicationException {
		LOGGER.info("Setting all the fields for LucenePageProcess.");

		LucenePageProcess lucenePageProcess = new LucenePageProcess();

		setProperties(documentAssembler, batchClassID, pluginPropertiesService, lucenePageProcess);

		List<Document> insertAllDocument = new ArrayList<Document>();
		lucenePageProcess.createDocForPages(insertAllDocument, docPageInfo, true);
		return insertAllDocument;
	}

	private void setProperties(final DocumentAssembler documentAssembler, final String batchClassID,
			final PluginPropertiesService pluginPropertiesService, final LucenePageProcess lucenePageProcess) {
		lucenePageProcess.setPageTypeService(documentAssembler.getPageTypeService());
		lucenePageProcess.setPluginPropertiesService(pluginPropertiesService);
		lucenePageProcess.setBatchClassIdentifier(batchClassID);
		lucenePageProcess.setDocTypeService(documentAssembler.getDocTypeService());
		lucenePageProcess.setBatchSchemaService(documentAssembler.getBatchSchemaService());

		Map<String, String> propertyMap = new HashMap<String, String>();
		propertyMap.put(DocumentAssemblerConstants.LUCENE_CLASSIFICATION, documentAssembler.getLuceneClassification());
		propertyMap.put(DocumentAssemblerConstants.CHECK_FIRST_PAGE, documentAssembler.getCheckFirstPage());
		propertyMap.put(DocumentAssemblerConstants.CHECK_MIDDLE_PAGE, documentAssembler.getCheckMiddlePage());
		propertyMap.put(DocumentAssemblerConstants.CHECK_LAST_PAGE, documentAssembler.getCheckLastPage());

		LOGGER.info("Initializing properties...");
		String factoryClassification = DocumentClassificationFactory.SEARCHCLASSIFICATION.toString();
		String ruleFMLPage = pluginPropertiesService.getPropertyValue(batchClassID,
				DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN, DocumentAssemblerProperties.DA_RULE_FP_MP_LP);
		String ruleFPage = pluginPropertiesService.getPropertyValue(batchClassID,
				DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN, DocumentAssemblerProperties.DA_RULE_FP);
		String ruleMPage = pluginPropertiesService.getPropertyValue(batchClassID,
				DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN, DocumentAssemblerProperties.DA_RULE_MP);
		String ruleLPage = pluginPropertiesService.getPropertyValue(batchClassID,
				DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN, DocumentAssemblerProperties.DA_RULE_LP);
		String ruleFLPage = pluginPropertiesService.getPropertyValue(batchClassID,
				DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN, DocumentAssemblerProperties.DA_RULE_FP_LP);
		String ruleMLPage = pluginPropertiesService.getPropertyValue(batchClassID,
				DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN, DocumentAssemblerProperties.DA_RULE_MP_LP);
		String ruleFMPage = pluginPropertiesService.getPropertyValue(batchClassID,
				DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN, DocumentAssemblerProperties.DA_RULE_FP_MP);
		String mergeUnknownSwitch = pluginPropertiesService.getPropertyValue(batchClassID,
				DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN, DocumentAssemblerProperties.DA_MERGE_UNKNOWN_DOCUMENT_SWITCH);
		String deleteDocFirstPageSwitch = pluginPropertiesService
				.getPropertyValue(batchClassID, DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN,
						DocumentAssemblerProperties.DA_DELETE_DOCUMENT_FIRST_PAGE_SWITCH);
		String firstPageConfThreshold = pluginPropertiesService.getPropertyValue(batchClassID,
				DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN, DocumentAssemblerProperties.DA_FIRST_PAGE_CONFIDENCE_THRESHOLD);
		String middlePageConfThreshold = pluginPropertiesService.getPropertyValue(batchClassID,
				DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN, DocumentAssemblerProperties.DA_MIDDLE_PAGE_CONFIDENCE_THRESHOLD);
		String lastPageConfThreshold = pluginPropertiesService.getPropertyValue(batchClassID,
				DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN, DocumentAssemblerProperties.DA_LAST_PAGE_CONFIDENCE_THRESHOLD);
		String advancedDASwitch = pluginPropertiesService.getPropertyValue(batchClassID,
				DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN, DocumentAssemblerProperties.DA_ADVANCED_ALGORITHM);
		String predefinedDocType = pluginPropertiesService.getPropertyValue(batchClassID,
				DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN, DocumentAssemblerProperties.DA_PREDEFINED_DOCUMENT_TYPE);
		String predefinedDocTypeConfThreshold = pluginPropertiesService.getPropertyValue(batchClassID,
				DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN, DocumentAssemblerProperties.DA_PREDEFINED_DOCUMENT_CONFIDENCE_THRESHOLD);
		String unknownPredefinedDocType = pluginPropertiesService.getPropertyValue(batchClassID,
				DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN, DocumentAssemblerProperties.DA_UNKNOWN_PREDEFINED_DOCUMENT_TYPE);
		String switchUnknownPredefinedDocType = pluginPropertiesService.getPropertyValue(batchClassID,
				DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN, DocumentAssemblerProperties.DA_SWITCH_UNKNOWN_PREDEFINED_DOCUMENT_TYPE);
		LOGGER.info("Properties Initialized Successfully");
		if(null == predefinedDocType){
			predefinedDocType = DocumentAssemblerConstants.EMPTY;
		}
		if(null == unknownPredefinedDocType){
			unknownPredefinedDocType = DocumentAssemblerConstants.EMPTY;
		}
		propertyMap.put(DocumentAssemblerConstants.FACTORY_CLASSIFICATION, factoryClassification);
		propertyMap.put(DocumentAssemblerConstants.RULE_FL_PAGE, ruleFLPage);
		propertyMap.put(DocumentAssemblerConstants.RULE_FML_PAGE, ruleFMLPage);
		propertyMap.put(DocumentAssemblerConstants.RULE_FM_PAGE, ruleFMPage);
		propertyMap.put(DocumentAssemblerConstants.RULE_F_PAGE, ruleFPage);
		propertyMap.put(DocumentAssemblerConstants.RULE_L_PAGE, ruleLPage);
		propertyMap.put(DocumentAssemblerConstants.RULE_ML_PAGE, ruleMLPage);
		propertyMap.put(DocumentAssemblerConstants.RULE_M_PAGE, ruleMPage);
		propertyMap.put(DocumentAssemblerConstants.DA_MERGE_UNKNOWN_DOCUMENT_SWITCH, mergeUnknownSwitch);
		propertyMap.put(DocumentAssemblerConstants.SWITCH_DELETE_DOCUMENT_FIRST_PAGE, deleteDocFirstPageSwitch);
		propertyMap.put(DocumentAssemblerConstants.FIRST_PAGE_CONFIDENCE_THRESHOLD, firstPageConfThreshold);
		propertyMap.put(DocumentAssemblerConstants.MIDDLE_PAGE_CONFIDENCE_THRESHOLD, middlePageConfThreshold);
		propertyMap.put(DocumentAssemblerConstants.LAST_PAGE_CONFIDENCE_THRESHOLD, lastPageConfThreshold);
		propertyMap.put(DocumentAssemblerConstants.PREDEFINED_DOCUMENT_TYPE, predefinedDocType);
		propertyMap.put(DocumentAssemblerConstants.PREDEFINED_DOCUMENT_CONFIDENCE_THRESHOLD, predefinedDocTypeConfThreshold);
		propertyMap.put(DocumentAssemblerConstants.DA_ADVANCED_ALGORITHM, advancedDASwitch);
		propertyMap.put(DocumentAssemblerConstants.UNKNOWN_PREDEFINED_DOCUMENT_TYPE, unknownPredefinedDocType);
		propertyMap.put(DocumentAssemblerConstants.SWITCH_UNKNOWN_PREDEFINED_DOCUMENT_TYPE, switchUnknownPredefinedDocType);

		lucenePageProcess.setPropertyMap(propertyMap);
	}

	/**
	 * Processes passed document list and does reclassification for each document. Reclassification is done for documents whose
	 * document type information not present.
	 * 
	 * @param batchInstanceIdentifer
	 * @param batchClassIdentifier
	 * @param documentList
	 * @throws DCMAApplicationException
	 */
	@Override
	public void processDocumentTypes(final DocumentAssembler documentAssembler, final String batchInstanceIdentifier,
			final String batchClassIdentifier, final List<Document> documentList) throws DCMAApplicationException {
		LOGGER.debug("Entering method processDocumentTypes for " + batchInstanceIdentifier);
		LucenePageProcess lucenePageProcess = new LucenePageProcess();
		lucenePageProcess.setBatchInstanceIdentifier(batchInstanceIdentifier);
		setProperties(documentAssembler, batchClassIdentifier, documentAssembler.getPluginPropertiesService(), lucenePageProcess);
		lucenePageProcess.reClassifyDocuments(documentList);
		LOGGER.debug("Exiting method processDocumentTypes for " + batchInstanceIdentifier);
	}

}
