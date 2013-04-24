/*
 * Copyright (c) 2013 Megha Nidhi Dahal. All rights reserved. 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: Megha Nidhi Dahal
 */
package org.eclipse.birt.report.engine.emitter.json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IListGroupContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.emitter.json.common.JsonEmitterConstants;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.json.simple.JSONObject;

public class JsonEmitter implements IContentEmitter {

	private List<JSONObject> jsonObjectList = new ArrayList<JSONObject>();;
	private JSONObject jsonObject;
	private OutputStream outputStream;
	private File outputDirectory;

	@Override
	public void end(IReportContent arg0) throws BirtException {
		try {
			for (JSONObject jsonObj : jsonObjectList) {
				outputStream.write(jsonObj.toJSONString().getBytes());
				outputStream.write(System.getProperty("line.separator").getBytes());
			}
		} catch (IOException e) {
			throw new BirtException(e.getMessage());
		} finally {
			try {
				if (outputStream != null)
					outputStream.close();
			} catch (IOException e) {
				throw new BirtException(e.getMessage());
			}
		}
	}

	@Override
	public void endCell(ICellContent arg0) throws BirtException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endContainer(IContainerContent arg0) throws BirtException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endContent(IContent arg0) throws BirtException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endGroup(IGroupContent arg0) throws BirtException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endList(IListContent listContent) throws BirtException {
		// String listName = listContent.getName();
		// writeOutput(listName);
	}

	@Override
	public void endListBand(IListBandContent arg0) throws BirtException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endListGroup(IListGroupContent arg0) throws BirtException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endPage(IPageContent arg0) throws BirtException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endRow(IRowContent row) throws BirtException {
		if (!jsonObject.isEmpty()) {
			jsonObjectList.add(jsonObject);
		}
	}

	@Override
	public void endTable(ITableContent tableContent) throws BirtException {
		String tableName = tableContent.getName();
		writeOutput(tableName);
	}

	private void writeOutput(String fileName) throws BirtException {
		if (outputDirectory == null || !outputDirectory.exists()) {
			return;
		}
		fileName = fileName == null ? "table" + new Random().nextInt() : fileName;
		fileName = fileName + JsonEmitterConstants.JSON_FILE_EXTENSION;
		OutputStream os = null;
		try {
			os = new FileOutputStream(new File(outputDirectory, fileName));
			for (JSONObject jsonObj : jsonObjectList) {
				os.write(jsonObj.toJSONString().getBytes());
				os.write(System.getProperty("line.separator").getBytes());
			}
			jsonObjectList.clear();
		} catch (FileNotFoundException e) {
			throw new BirtException(e.getMessage());
		} catch (IOException e) {
			throw new BirtException(e.getMessage());
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				throw new BirtException(e.getMessage());
			}
		}
	}

	@Override
	public void endTableBand(ITableBandContent arg0) throws BirtException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endTableGroup(ITableGroupContent arg0) throws BirtException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getOutputFormat() {
		return "json";
	}

	@Override
	public void initialize(IEmitterServices services) throws BirtException {
		// check the output stream
		String outputDirectoryName = (String) services.getRenderOption().getOption(JsonEmitterConstants.RENDEROPTION_OUTPUT_DIR);
		if (outputDirectoryName != null) {
			outputDirectory = new File(outputDirectoryName);
			outputDirectory.mkdirs();
			if (!outputDirectory.exists()) {
				throw new BirtException("Output directory doesn't exist." + outputDirectoryName);
			}
		} else {
			outputStream = services.getRenderOption().getOutputStream();
			if (outputStream == null) {
				String outputFileName = services.getRenderOption().getOutputFileName();
				try {
					outputStream = new FileOutputStream(outputFileName);
				} catch (FileNotFoundException e) {
					throw new BirtException(e.getMessage());
				}
			}
			// throw new
			// BirtException("OUTPUT_DIR is not set in render option.");
		}
	}

	@Override
	public void start(IReportContent arg0) throws BirtException {

	}

	@Override
	public void startAutoText(IAutoTextContent arg0) throws BirtException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startCell(ICellContent cell) throws BirtException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startContainer(IContainerContent arg0) throws BirtException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startContent(IContent arg0) throws BirtException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startData(IDataContent data) throws BirtException {
		if (data == null || data.getValue() == null) {
			return;
		}
		DataItemDesign dataItemDesign = (DataItemDesign) data.getGenerateBy();
		jsonObject.put(dataItemDesign.getBindingColumn(), data.getValue());
	}

	@Override
	public void startForeign(IForeignContent arg0) throws BirtException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startGroup(IGroupContent arg0) throws BirtException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startImage(IImageContent arg0) throws BirtException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startLabel(ILabelContent arg0) throws BirtException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startList(IListContent listContent) throws BirtException {

	}

	@Override
	public void startListBand(IListBandContent arg0) throws BirtException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startListGroup(IListGroupContent arg0) throws BirtException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startPage(IPageContent arg0) throws BirtException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startRow(IRowContent rowContent) throws BirtException {
		jsonObject = new JSONObject();
	}

	@Override
	public void startTable(ITableContent tableContent) throws BirtException {

	}

	@Override
	public void startTableBand(ITableBandContent arg0) throws BirtException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startTableGroup(ITableGroupContent arg0) throws BirtException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startText(ITextContent arg0) throws BirtException {
		// TODO Auto-generated method stub

	}

}
