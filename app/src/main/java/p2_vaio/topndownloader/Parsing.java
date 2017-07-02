package p2_vaio.topndownloader;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by p2_vaio on 7/1/2017.
 */

public class Parsing {
    private static final String TAG = "Parsing";
    private ArrayList<FeedEntry> list ;

    public Parsing() {
        this.list = new ArrayList<>();
    }

    public ArrayList<FeedEntry> getList() {
        return list;
    }
    public boolean parse(String xmlData){
        boolean status = true;
        FeedEntry currentRecord=null ;
        boolean inentry = false;
        boolean gotimage = false;
        String textvalue = "";

        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xmlData));
            int eventType = xpp.getEventType();
            while(eventType!=XmlPullParser.END_DOCUMENT){
                String tagname = xpp.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG:
//                        Log.d(TAG, "Parse: Starting tag for "+tagname);
                        if("entry".equalsIgnoreCase(tagname)){
                            currentRecord = new FeedEntry();
                            inentry =true;
                        }else if(inentry && "image".equalsIgnoreCase(tagname)){
                            String imageresolution = xpp.getAttributeValue(null , "height");
                            if(imageresolution!=null){
                                gotimage= "60".equalsIgnoreCase(imageresolution);
                            }
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textvalue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
//                        Log.d(TAG, "Parse: Ending tag for "+tagname);
                        if(inentry){
                            if("entry".equalsIgnoreCase(tagname)){
                                list.add(currentRecord);
                                inentry=false;
                            }
                            else if("name".equalsIgnoreCase(tagname)){
                                currentRecord.setTitle(textvalue);
                            }
                            else if("summary".equalsIgnoreCase(tagname)){
                                currentRecord.setSummary(textvalue);
                            }
                            else if("duration".equalsIgnoreCase(tagname)){
                                currentRecord.setDuration(textvalue);
                            }
                            else if("image".equalsIgnoreCase(tagname)){
                                if(gotimage)
                                    currentRecord.setImageURL(textvalue);
                            }
                            else if("releasedate".equalsIgnoreCase(tagname)){
                                currentRecord.setReleasedate(textvalue);
                            }
                        }
                        break;
                    default:
                        //Nothing
                }
                eventType = xpp.next();
            }
/*            for(FeedEntry x :list){
                Log.d(TAG, "***********");
                Log.d(TAG, x.toString());
            }*/

        }catch(Exception e){
            status = false;
            Log.d(TAG, "Parse: Error" + e.getMessage());
            e.printStackTrace();
        }
        return status;

    }
}
