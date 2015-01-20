package com.mendeleypaperreader.utl;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.mendeleypaperreader.contentProvider.MyContentProvider;
import com.mendeleypaperreader.db.DatabaseOpenHelper;

/**
 * Created by pedro on 29/12/14.
 */
public class GetDataBaseInformation {

    Context mContext;
    
    public GetDataBaseInformation(Context context){

        mContext = context;
        
    }


    public String getProfileInformation(String columnName) {

        String[] projection;
        String selection = null;

        projection = new String[]{columnName + " as _id"};
        Uri uri = MyContentProvider.CONTENT_URI_PROFILE;

        Cursor cursorProfiel = mContext.getContentResolver().query(uri, projection, selection, null, null);
        cursorProfiel.moveToPosition(0);

        return cursorProfiel.getString(cursorProfiel.getColumnIndex(DatabaseOpenHelper._ID));

    }

    /**
     * *
     * @return
     */
    
    
    public Cursor getFile() {
        if (Globalconstant.LOG)
            Log.d(Globalconstant.TAG, "getFile - DOC_DETAILS");


        String[] projection = new String[]{DatabaseOpenHelper.FILE_ID + " as _id", DatabaseOpenHelper.FILE_NAME, DatabaseOpenHelper.FILE_MIME_TYPE, DatabaseOpenHelper.DOCUMENT_ID};

        String selection = null;
        Uri uri = MyContentProvider.CONTENT_URI_FILES;

        Cursor cursorFile = mContext.getContentResolver().query(uri, projection, selection, null, null);
        cursorFile.moveToPosition(0);


        return cursorFile;

    }

    
    public Cursor getdocDetails(String docId) {

        if (Globalconstant.LOG)
            Log.d(Globalconstant.TAG, "getdocDetails - DOC_DETAILS");

        String[] projection = new String[]{DatabaseOpenHelper.TYPE + " as _id", DatabaseOpenHelper.TITLE, DatabaseOpenHelper.AUTHORS, DatabaseOpenHelper.SOURCE, DatabaseOpenHelper.YEAR, DatabaseOpenHelper.VOLUME, DatabaseOpenHelper.PAGES, DatabaseOpenHelper.ISSUE, DatabaseOpenHelper.ABSTRACT, DatabaseOpenHelper.WEBSITE, DatabaseOpenHelper.DOI, DatabaseOpenHelper.PMID, DatabaseOpenHelper.ISSN, DatabaseOpenHelper.STARRED, DatabaseOpenHelper.READER_COUNT, DatabaseOpenHelper.IS_DOWNLOAD, DatabaseOpenHelper.TAGS};
        String selection = DatabaseOpenHelper._ID + " = '" + docId + "'";
        Uri uri = Uri.parse(MyContentProvider.CONTENT_URI_DOC_DETAILS + "/id");

        Cursor cursorDetails = mContext.getContentResolver().query(uri, projection, selection, null, null);

        return cursorDetails;

    }

    public String getDocNotes(String docId) {

        if (Globalconstant.LOG)
            Log.d(Globalconstant.TAG, "getdocDetails - DOC_NOTES");


        String[] projection = new String[]{DatabaseOpenHelper.TEXT + " as _id"};
        String selection = DatabaseOpenHelper.DOCUMENT_ID + " = '" + docId + "'";
        Uri uri = Uri.parse(MyContentProvider.CONTENT_URI_DOC_NOTES + "/id");

        Cursor cursorNotes = mContext.getContentResolver().query(uri, projection, selection, null, null);


        if (cursorNotes.getCount() > 0) {
            cursorNotes.moveToPosition(0);

            return cursorNotes.getString(cursorNotes.getColumnIndex(DatabaseOpenHelper._ID));
        }

        return "";

    }

    public Cursor getDocId(String doc_title) {

        String[] projection = new String[]{DatabaseOpenHelper._ID};

        if (doc_title.contains("'")) {
            doc_title = doc_title.replaceAll("'", "''");
        }

        String selection = DatabaseOpenHelper.TITLE + " = '" + doc_title + "'";
        Uri uri = Uri.parse(MyContentProvider.CONTENT_URI_DOC_DETAILS + "/id");

        return mContext.getContentResolver().query(uri, projection, selection, null, null);
    }
    
}
