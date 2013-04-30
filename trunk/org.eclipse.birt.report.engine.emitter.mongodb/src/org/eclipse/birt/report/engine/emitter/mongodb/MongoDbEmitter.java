/*
 * Copyright (c) 2013 Megha Nidhi Dahal. All rights reserved. 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: Megha Nidhi Dahal
 */

package org.eclipse.birt.report.engine.emitter.mongodb;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

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
import org.eclipse.birt.report.engine.emitter.mongodb.common.MongoDbEmitterConstants;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.ImageItemDesign;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

public class MongoDbEmitter implements IContentEmitter {

	private MongoClient mongoClient;
	private List<DBObject> dbObjectList = new ArrayList<DBObject>();;
	private DBObject dbObject;
	private DB mongoDatabase;

	@Override
	public void end(IReportContent arg0) throws BirtException {
		// close connection to mongodb
		mongoClient.close();
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
		dbObjectList.add(dbObject);
	}

	@Override
	public void endTable(ITableContent tableContent) throws BirtException {
		String tableName = tableContent.getName();
		writeOutput(tableName);
	}

	private void writeOutput(String tableName) throws BirtException {
		if (tableName == null || "".equals(tableName.trim())) {
			return;
		}
		// write json object list to this table/collection
		DBCollection collection = mongoDatabase.getCollection(tableName);
		WriteResult result = collection.insert(dbObjectList);
		result.getError();
		dbObjectList.clear();
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
		return "mongodb";
	}

	@Override
	public void initialize(IEmitterServices services) throws BirtException {
		// open connection to mongodb
		try {
			String serverAddress = (String) services.getRenderOption().getOption(MongoDbEmitterConstants.OPTION_MONGODB_HOST_ADDRESS);
			Integer port = 27017;
			String portStr = (String) services.getRenderOption().getOption(MongoDbEmitterConstants.OPTION_MONGODB_PORT);
			if (portStr != null && !"".equals(portStr.trim())) {
				port = Integer.valueOf(portStr);
			}
			mongoClient = new MongoClient(serverAddress, port);
			String dbName = (String) services.getRenderOption().getOption(MongoDbEmitterConstants.OPTION_MONGODB_DB_NAME);
			mongoDatabase = mongoClient.getDB(dbName);
			/*
			 * boolean auth = mongoDatabase.authenticate("userName",
			 * "password".toCharArray()); if (auth == false) { throw new
			 * MongoDbEmitterException("Authentication Failed..."); }
			 */
		} catch (UnknownHostException e) {
			throw new BirtException(e.getMessage());
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
		DataItemDesign dataItemDesign = (DataItemDesign) data.getGenerateBy();
		dbObject.put(dataItemDesign.getBindingColumn(), data.getValue());
	}

	@Override
	public void startForeign(IForeignContent foreignContent) throws BirtException {
		// TODO Auto-generated method stub
	}

	@Override
	public void startGroup(IGroupContent arg0) throws BirtException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startImage(IImageContent imageContent) throws BirtException {
		ImageItemDesign design = (ImageItemDesign) imageContent.getGenerateBy();
		dbObject.put(design.getName(), imageContent.getImageMap());
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
		dbObject = new BasicDBObject();
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
