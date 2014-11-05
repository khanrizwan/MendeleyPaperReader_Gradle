package com.mendeleypaperreader.jsonParser;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mendeleypaperreader.contentProvider.MyContentProvider;
import com.mendeleypaperreader.db.DatabaseOpenHelper;
import com.mendeleypaperreader.sessionManager.SessionManager;
import com.mendeleypaperreader.utl.GlobalConstant;
import com.mendeleypaperreader.utl.JSONParser;

/**
 * Classname: LoadData 
 * 	 
 * 
 * @date July 8, 2014
 * @author PedroLourenco (pdrolourenco@gmail.com)
 */



public class LoadData {

	private Context context;
	private static SessionManager session;
	private static String access_token;
	public LoadData(Context context) {
		this.context = context;

		session = new SessionManager(this.context);  
		access_token = session.LoadPreference("access_token");


	}





    public void getGroupDocs(){

        Cursor groups = getGroups();

        while (groups.moveToNext()) {


            Log.d(GlobalConstant.TAG, "ID GROUP: " + groups.getString(groups.getColumnIndex(DatabaseOpenHelper._ID)));

            String url = GlobalConstant.get_docs_in_groups.replace("#groupId#",groups.getString(groups.getColumnIndex(DatabaseOpenHelper._ID)));

            Log.d(GlobalConstant.TAG, "URL GROUP: " + url + access_token);

            getUserLibrary(url + access_token);

        }




    }


    private Cursor getGroups(){

        if(GlobalConstant.LOG)
            Log.d(GlobalConstant.TAG, "getGROUPS- LOAD DATA");


        String[] projection = null;
        String selection = null;

        projection = new String[] {DatabaseOpenHelper._ID + " as _id"};
        Uri  uri = MyContentProvider.CONTENT_URI_GROUPS;


        return this.context.getContentResolver().query(uri, projection, selection, null, null);



    }


    public void getGroups(String url){

        ContentValues values = new ContentValues();

        JSONParser jParser = new JSONParser();
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        List<InputStream> link = new ArrayList<InputStream>();
        link = jParser.getJACKSONFromUrl(url,true);

        Log.d(GlobalConstant.TAG, "url" + url);


        try {
            for( InputStream oneItem : link ) {
                JsonParser jp = factory.createParser(oneItem);
                JsonNode rootNode = mapper.readTree(jp);

                Iterator<JsonNode> ite = rootNode.iterator();

                while (ite.hasNext() ) {
                    JsonNode temp = ite.next();

                    if(temp.has(GlobalConstant.GROUP_NAME)){
                        values.put(DatabaseOpenHelper.GROUPS_NAME,temp.get(GlobalConstant.GROUP_NAME).asText());

                        Log.d(GlobalConstant.TAG, "GROUP_NAME" + temp.get(GlobalConstant.GROUP_NAME).asText());
                    }

                    if(temp.has(GlobalConstant.ID)){
                        values.put(DatabaseOpenHelper._ID,temp.get(GlobalConstant.ID).asText());

                        Log.d(GlobalConstant.TAG, "ID: " + temp.get(GlobalConstant.ID).asText());
                    }
                    Uri uri = this.context.getContentResolver().insert(MyContentProvider.CONTENT_URI_GROUPS, values);

                }

                jp.close();
            }



        } catch (Exception e) {
            e.printStackTrace();
        }


    }





    public void getFiles(String url){

		ContentValues values = new ContentValues();

		JSONParser jParser = new JSONParser();
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getFactory(); 
		List<InputStream> link = new ArrayList<InputStream>();
		link = jParser.getJACKSONFromUrl(url,true);

		try {
			for( InputStream oneItem : link ) {
				JsonParser jp = factory.createParser(oneItem);	
				JsonNode rootNode = mapper.readTree(jp);

				Iterator<JsonNode> ite = rootNode.iterator();

				while (ite.hasNext() ) {
					JsonNode temp = ite.next();

					if(temp.has(GlobalConstant.ID)){
						values.put(DatabaseOpenHelper.FILE_ID,temp.get(GlobalConstant.ID).asText());
					}

					if(temp.has(GlobalConstant.FILE_DOC_ID)){
						values.put(DatabaseOpenHelper.FILE_DOC_ID,temp.get(GlobalConstant.FILE_DOC_ID).asText());
					}else{
						values.put(DatabaseOpenHelper.FILE_DOC_ID, "");
					}

					if(temp.has(GlobalConstant.FILE_NAME)){
						values.put(DatabaseOpenHelper.FILE_NAME,temp.get(GlobalConstant.FILE_NAME).asText());
					}else{
						values.put(DatabaseOpenHelper.FILE_NAME, "");
					}

					if(temp.has(GlobalConstant.FILE_MIME_TYPE)){
						values.put(DatabaseOpenHelper.FILE_MIME_TYPE,temp.get(GlobalConstant.FILE_MIME_TYPE).asText());
					}else{
						values.put(DatabaseOpenHelper.FILE_MIME_TYPE, "");
					}
					if(temp.has(GlobalConstant.FILE_FILEHASH)){
						values.put(DatabaseOpenHelper.FILE_FILEHASH,temp.get(GlobalConstant.FILE_FILEHASH).asText());
					}else{
						values.put(DatabaseOpenHelper.FILE_FILEHASH, "");
					}

					Uri uri = this.context.getContentResolver().insert(MyContentProvider.CONTENT_URI_FILES, values);

				}

				jp.close();
			}



		} catch (Exception e) {
			e.printStackTrace();
		}

	}




	public void getFolders(String url){

		ContentValues values = new ContentValues();

		JSONParser jParser = new JSONParser();
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getFactory(); 
		List<InputStream> link = new ArrayList<InputStream>();
		link = jParser.getJACKSONFromUrl(url,true);

		try {
			for( InputStream oneItem : link ) {
				JsonParser jp = factory.createParser(oneItem);	
				JsonNode rootNode = mapper.readTree(jp);

				Iterator<JsonNode> ite = rootNode.iterator();

				while (ite.hasNext() ) {
					JsonNode temp = ite.next();

					if(temp.has(GlobalConstant.ID)){
						values.put(DatabaseOpenHelper.FOLDER_ID,temp.get(GlobalConstant.ID).asText());
					}

					if(temp.has(GlobalConstant.NAME)){
						values.put(DatabaseOpenHelper.FOLDER_NAME,temp.get(GlobalConstant.NAME).asText());
					}else{
						values.put(DatabaseOpenHelper.FOLDER_NAME, "");
					}

					if(temp.has(GlobalConstant.PARENT_ID)){
						values.put(DatabaseOpenHelper.FOLDER_PARENT,temp.get(GlobalConstant.PARENT_ID).asText());
					}else{
						values.put(DatabaseOpenHelper.FOLDER_PARENT, "");
					}

					if(temp.has(GlobalConstant.ADDED)){
						values.put(DatabaseOpenHelper.FOLDER_ADDED,temp.get(GlobalConstant.ADDED).asText());
					}else{
						values.put(DatabaseOpenHelper.FOLDER_ADDED, "");
					}
					if(temp.has(GlobalConstant.GROUP)){
						values.put(DatabaseOpenHelper.FOLDER_GROUP,temp.get(GlobalConstant.GROUP).asText());
					}else{
						values.put(DatabaseOpenHelper.FOLDER_GROUP, "");
					}

					Uri uri = this.context.getContentResolver().insert(MyContentProvider.CONTENT_URI_FOLDERS, values);
					getDocsInFolder(temp.get(GlobalConstant.ID).asText());
				}

				jp.close();
			}



		} catch (Exception e) {
			e.printStackTrace();
		}


	}


	public void getUserLibrary(String url) {

		ContentValues values = new ContentValues();
		ContentValues authors_values = new ContentValues();
		JSONParser jParser = new JSONParser();
		String docTitle ;
		String docId = null ;

		List<InputStream> link = new ArrayList<InputStream>();

		ObjectMapper mapper = new ObjectMapper();

		JsonFactory factory = mapper.getFactory();
		link = jParser.getJACKSONFromUrl(url,true);

		try {

			for( InputStream oneItem : link ) {

				JsonParser jp = factory.createParser(oneItem);	
				JsonNode rootNode = mapper.readTree(jp);

				Iterator<JsonNode> ite = rootNode.iterator();
				while (ite.hasNext() ) {

					JsonNode temp = ite.next();

					if(temp.has(GlobalConstant.ID)){
						docId = temp.get(GlobalConstant.ID).asText();
						values.put(DatabaseOpenHelper._ID,docId);
					}

					if(temp.has(GlobalConstant.TITLE)){
						docTitle = temp.get(GlobalConstant.TITLE).asText();
						values.put(DatabaseOpenHelper.TITLE,docTitle);
					}else{
						docTitle = "";
						values.put(DatabaseOpenHelper.TITLE, docTitle);
					}	

					if(temp.has(GlobalConstant.TYPE)){
						values.put(DatabaseOpenHelper.TYPE,temp.get(GlobalConstant.TYPE).asText());
					}else{
						values.put(DatabaseOpenHelper.TYPE, "");
					}

					if(temp.has(GlobalConstant.MONTH)){
						values.put(DatabaseOpenHelper.MONTH,temp.get(GlobalConstant.MONTH).asText());
					}else{
						values.put(DatabaseOpenHelper.MONTH, "");
					}
					if(temp.has(GlobalConstant.YEAR)){
						values.put(DatabaseOpenHelper.YEAR,temp.get(GlobalConstant.YEAR).asText());
					}else{
						values.put(DatabaseOpenHelper.YEAR, "");
					}
					if(temp.has(GlobalConstant.LAST_MODIFIED)){
						values.put(DatabaseOpenHelper.LAST_MODIFIED,temp.get(GlobalConstant.LAST_MODIFIED).asText());
					}else{
						values.put(DatabaseOpenHelper.LAST_MODIFIED, "");
					}

					if(temp.has(GlobalConstant.CREATED)){
						values.put(DatabaseOpenHelper.ADDED,temp.get(GlobalConstant.CREATED).asText());
					}else{
						values.put(DatabaseOpenHelper.ADDED, "");
					}
					 
					if(temp.has(GlobalConstant.GROUP_ID)){
						values.put(DatabaseOpenHelper.GROUP_ID,temp.get(GlobalConstant.GROUP_ID).asText());
					}else{
						values.put(DatabaseOpenHelper.GROUP_ID, "");
					}
					if(temp.has(GlobalConstant.SOURCE)){
						values.put(DatabaseOpenHelper.SOURCE,temp.get(GlobalConstant.SOURCE).asText());
					}else{
						values.put(DatabaseOpenHelper.SOURCE, "");
					}
					
					if(temp.has(GlobalConstant.PAGES)){
						values.put(DatabaseOpenHelper.PAGES, temp.get(GlobalConstant.PAGES).asText());
					}else{
						values.put(DatabaseOpenHelper.PAGES, "");
					}

					if(temp.has(GlobalConstant.VOLUME)){
						values.put(DatabaseOpenHelper.VOLUME, temp.get(GlobalConstant.VOLUME).asText());
					}else{
						values.put(DatabaseOpenHelper.VOLUME, "");
					}
					if(temp.has(GlobalConstant.ISSUE)){
						values.put(DatabaseOpenHelper.ISSUE, temp.get(GlobalConstant.ISSUE).asText());
					}else{
						values.put(DatabaseOpenHelper.ISSUE, "");
					}
					
					if(temp.has(GlobalConstant.STARRED)){
						values.put(DatabaseOpenHelper.STARRED, temp.get(GlobalConstant.STARRED).asText());
					}else{
						values.put(DatabaseOpenHelper.STARRED, "");
					}
					if(temp.has(GlobalConstant.AUTHORED)){
						values.put(DatabaseOpenHelper.AUTHORED, temp.get(GlobalConstant.AUTHORED).asText());

					}else{
						values.put(DatabaseOpenHelper.AUTHORED, "");
					}
					
					if(temp.has(GlobalConstant.ABSTRACT)){
						values.put(DatabaseOpenHelper.ABSTRACT, temp.get(GlobalConstant.ABSTRACT).asText());
					}else{
						values.put(DatabaseOpenHelper.ABSTRACT, "");
					}

					//Array
					//authors":[{"first_name":"Asger","last_name":"Hobolth"},{"first_name":"Ole F","last_name":"Christensen"},{"first_name":"Thomas","last_name":"Mailund"},{"first_name":"Mikkel H","last_name":"Schierup"}]
					if(temp.has(GlobalConstant.AUTHORS)){
						Iterator<JsonNode> authorsIterator = temp.get(GlobalConstant.AUTHORS).elements();
						String authors = "";
						String aux_surname = null, aux_forenamed = null;
						
						while (authorsIterator.hasNext() ){

							JsonNode author = authorsIterator.next();
							
							if(author.has(GlobalConstant.FORENAME)){
								aux_forenamed = author.get(GlobalConstant.FORENAME).asText();
								
							}else{
								aux_forenamed = "";
							}
									
							if(author.has(GlobalConstant.SURNAME)){
								aux_surname = author.get(GlobalConstant.SURNAME).asText();
								
							}
							else{
								aux_surname = "";
							}
							
							author.get(GlobalConstant.SURNAME);

							String author_name = aux_forenamed	+ " "+ aux_surname;

							authors += author_name + ",";
							values.put(DatabaseOpenHelper.AUTHORS, authors.substring(0,authors.length()-1));

							authors_values.put(DatabaseOpenHelper.DOC_DETAILS_ID,temp.get("id").asText());
							authors_values.put(DatabaseOpenHelper.AUTHOR_NAME,author_name);

							Uri uri_authors = context.getContentResolver().insert(MyContentProvider.CONTENT_URI_AUTHORS,authors_values);

						}
					}else{
						values.put(DatabaseOpenHelper.AUTHORS, "");
					}


					if(temp.has(GlobalConstant.IDENTIFIERS)){

						Iterator<Entry<String, JsonNode>> identifierIterator = temp.get(GlobalConstant.IDENTIFIERS).fields();

						values.put(DatabaseOpenHelper.ISSN, "");
						values.put(DatabaseOpenHelper.ISBN, "");
						values.put(DatabaseOpenHelper.PMID, "");
						values.put(DatabaseOpenHelper.SCOPUS, "");
						values.put(DatabaseOpenHelper.SSN, "");
						values.put(DatabaseOpenHelper.ARXIV, "");
						values.put(DatabaseOpenHelper.DOI, "");

						while (identifierIterator.hasNext() ){

							Map.Entry<String, JsonNode> entry = (Map.Entry<String, JsonNode>) identifierIterator.next();

							values.put(entry.getKey(),entry.getValue().asText());
						}
					}else{
						values.put(DatabaseOpenHelper.ISSN, "");
						values.put(DatabaseOpenHelper.ISBN, "");
						values.put(DatabaseOpenHelper.PMID, "");
						values.put(DatabaseOpenHelper.SCOPUS, "");
						values.put(DatabaseOpenHelper.SSN, "");
						values.put(DatabaseOpenHelper.ARXIV, "");
						values.put(DatabaseOpenHelper.DOI, "");
					}

					Uri uri = this.context.getContentResolver().insert(MyContentProvider.CONTENT_URI_DOC_DETAILS, values);

				}
				jp.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}




	private void getDocsInFolder(String folderId){

		ContentValues values = new ContentValues();

		String auxurl = GlobalConstant.get_docs_in_folders;
		String url = auxurl.replace("id", folderId) + access_token; 

		JSONParser jParser = new JSONParser();
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getFactory(); 
		List<InputStream> link = new ArrayList<InputStream>();
		link = jParser.getJACKSONFromUrl(url,true);


		try {

			for( InputStream oneItem : link ) {
				JsonParser jp = factory.createParser(oneItem);	
				JsonNode rootNode = mapper.readTree(jp);

				Iterator<JsonNode> ite = rootNode.iterator();
				while (ite.hasNext() ) {
					JsonNode temp = ite.next();

					if(temp.has(GlobalConstant.ID)){

						values.put(DatabaseOpenHelper.FOLDER_ID, folderId);
						values.put(DatabaseOpenHelper.DOC_DETAILS_ID, temp.get(GlobalConstant.ID).asText());

					}else{
						values.put(DatabaseOpenHelper.FOLDER_ID, "");
						values.put(DatabaseOpenHelper.DOC_DETAILS_ID, "");
					}
					Uri uri = this.context.getContentResolver().insert(MyContentProvider.CONTENT_URI_FOLDERS_DOCS, values);

				}
				jp.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private Cursor getDocInfo(){


		String[] projection = null;
		String selection = null;
		String orderBy = null;
		Cursor query;

		projection = new String[] {DatabaseOpenHelper._ID + " as _id",  DatabaseOpenHelper.PMID,DatabaseOpenHelper.DOI, DatabaseOpenHelper.ISSN, DatabaseOpenHelper.ISBN, DatabaseOpenHelper.SCOPUS, DatabaseOpenHelper.ARXIV};

		Uri  uri = MyContentProvider.CONTENT_URI_DOC_DETAILS;
		query = this.context.getContentResolver().query(uri, projection, selection, null, orderBy);

		return query;

	}





	public void getCatalogId(){

		ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();
		ContentValues academic_docs_values = new ContentValues();
		Cursor cursorDocs;
		String urlfilter = null;
		boolean toProcess = true;
		Uri uri_ = Uri.parse(MyContentProvider.CONTENT_URI_DOC_DETAILS + "/id");

		cursorDocs = getDocInfo();

		while(cursorDocs.moveToNext()){

			String docId = cursorDocs.getString(cursorDocs.getColumnIndex(DatabaseOpenHelper._ID));
			String auxPmid = cursorDocs.getString(cursorDocs.getColumnIndex(DatabaseOpenHelper.PMID));
			String pmid = URLEncoder.encode(auxPmid);
			String auxDoi = cursorDocs.getString(cursorDocs.getColumnIndex(DatabaseOpenHelper.DOI));
			String doi = URLEncoder.encode(auxDoi);
			String auxIssn = cursorDocs.getString(cursorDocs.getColumnIndex(DatabaseOpenHelper.ISSN));
			String issn = URLEncoder.encode(auxIssn);
			String auxIsbn = cursorDocs.getString(cursorDocs.getColumnIndex(DatabaseOpenHelper.ISBN));
			String isbn = URLEncoder.encode(auxIsbn);
			String auxScopus = cursorDocs.getString(cursorDocs.getColumnIndex(DatabaseOpenHelper.SCOPUS));
			String scopus = URLEncoder.encode(auxScopus);
			String auxArxiv = cursorDocs.getString(cursorDocs.getColumnIndex(DatabaseOpenHelper.ARXIV));
			String arxiv = URLEncoder.encode(auxArxiv);
			
			String where = DatabaseOpenHelper._ID + " = '" + docId + "'";
			String where2 = DatabaseOpenHelper._ID + " = '" + docId + "' and " + DatabaseOpenHelper.READER_COUNT + " IS NULL";
            String where3 = DatabaseOpenHelper._ID + " = '" + docId + "' and " + DatabaseOpenHelper.WEBSITE + " IS NULL";



			if(!pmid.isEmpty()){
				toProcess = true;
				urlfilter = "pmid=" + pmid;		
			}else if(!doi.isEmpty()){	
				toProcess = true;
				urlfilter = "doi=" + doi;	
			}else if(!issn.isEmpty()){	
				toProcess = true;
				urlfilter = "issn=" + issn;	
			}else if(!doi.isEmpty()){	
				toProcess = true;
				urlfilter = "isbn=" + isbn;	
			} else if(!scopus.isEmpty()){
				toProcess = true;
				urlfilter = "scopus=" + scopus;	
			}else if(!arxiv.isEmpty()){		
				toProcess = true;
				urlfilter = "arxiv=" + arxiv;	
			}else{
				toProcess = false;
				//update table
				values.put(DatabaseOpenHelper.READER_COUNT, "0");	
				this.context.getContentResolver().update(uri_, values, where, null);
			}


			if(toProcess){

				String url = GlobalConstant.get_catalog_url + urlfilter + "&view=stats&access_token=" + access_token;

				if(GlobalConstant.LOG)
					Log.d(GlobalConstant.TAG, "getCatalogId url: " + url);

				JSONParser jParser = new JSONParser();
				ObjectMapper mapper = new ObjectMapper();
				JsonFactory factory = mapper.getFactory(); 
				List<InputStream> link = new ArrayList<InputStream>();
				link = jParser.getJACKSONFromUrl(url,false);

				try {

					for( InputStream oneItem : link ) {
						JsonParser jp = factory.createParser(oneItem);	
						JsonNode rootNode = mapper.readTree(jp);


						Iterator<JsonNode> ite = rootNode.iterator();

						while (ite.hasNext() ) {
							JsonNode temp = ite.next();

							if (temp.has("link")){
								values.put(DatabaseOpenHelper.WEBSITE, temp.get("link").asText());
							}


							if (temp.has("reader_count")){
								values.put(DatabaseOpenHelper.READER_COUNT, temp.get("reader_count").asText());
							}

							//update table			
							this.context.getContentResolver().update(uri_, values, where, null);				

							if (temp.has("reader_count_by_academic_status")){

								Iterator<Entry<String, JsonNode>> identifierIterator = temp.get("reader_count_by_academic_status").fields();

								while (identifierIterator.hasNext() ){

									Map.Entry<String, JsonNode> entry = (Map.Entry<String, JsonNode>) identifierIterator.next();

									academic_docs_values.put(DatabaseOpenHelper.DOC_DETAILS_ID, docId);
									academic_docs_values.put(DatabaseOpenHelper.STATUS,entry.getKey());
									academic_docs_values.put(DatabaseOpenHelper.COUNT,entry.getValue().asText());
									Uri uri = this.context.getContentResolver().insert(MyContentProvider.CONTENT_URI_ACADEMIC_DOCS, academic_docs_values); 
								}
							}
						}

					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			values.put(DatabaseOpenHelper.READER_COUNT, "0");
			values1.put(DatabaseOpenHelper.WEBSITE, "");
			this.context.getContentResolver().update(uri_, values, where2, null);
            this.context.getContentResolver().update(uri_, values1, where3, null);

		}
	}



	public void getProfileInfo(String url){

		ContentValues values = new ContentValues();


		JSONParser jParser = new JSONParser();
		ObjectMapper mapper = new ObjectMapper(); 
		List<InputStream> link = new ArrayList<InputStream>();
		link = jParser.getJACKSONFromUrl(url,true);

		try {

			for( InputStream oneItem : link ) {

				Map<String, Object> mapObject = mapper.readValue(oneItem, new TypeReference<Map<String, Object>>() {});

				values.put(DatabaseOpenHelper.PROFILE_ID, mapObject.get(GlobalConstant.ID).toString());
				values.put(DatabaseOpenHelper.PROFILE_FIRST_NAME, mapObject.get(GlobalConstant.FORENAME).toString());
				values.put(DatabaseOpenHelper.PROFILE_LAST_NAME, mapObject.get(GlobalConstant.SURNAME).toString() );
				values.put(DatabaseOpenHelper.PROFILE_DISPLAY_NAME,	mapObject.get(GlobalConstant.PROFILE_DISPLAY_NAME).toString());
				values.put(DatabaseOpenHelper.PROFILE_LINK,mapObject.get(GlobalConstant.PROFILE_LINK).toString());

				Uri uri = this.context.getContentResolver().insert(MyContentProvider.CONTENT_URI_PROFILE, values);


			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}






}

