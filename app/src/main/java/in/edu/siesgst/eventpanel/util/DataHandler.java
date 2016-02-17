package in.edu.siesgst.eventpanel.util;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.edu.siesgst.eventpanel.fragment.participant.ParticipantContent;

/**
 * Created by vishal on 5/1/16.
 */
public class DataHandler {

    Context context;
    JSONArray dataArray;

    public DataHandler(Context context) {
        this.context = context;
    }


    public void pushEvents(JSONArray array) {   //TODO check column names
        JSONObject object;
        try {
            for (int i = 0; i < array.length(); i++) {
                object = array.optJSONObject(i);
                String[] data = new String[12];
                data[0] = object.optString("eName");
                data[1] = object.optString("eDay");
                data[2] = object.optString("eVenue");
                data[3] = object.optString("eCategory");
                data[4] = object.optString("eSubCategory");
                data[5] = object.optString("eDetails");
                data[6] = object.optString("eHead1");
                data[7] = object.optString("ePhone1");
                data[8] = object.optString("eHead2");
                data[9] = object.optString("ePhone2");
                data[10] = object.optString("eCreated");
                data[11] = object.optString("eModified");

                new LocalDBHandler(context).insertEventData(data);
            }
        } catch (NullPointerException e) {

        }
    }

    public void pushParticipants(JSONArray array){
        JSONObject object;
        try {
            for (int i = 0; i < array.length(); i++) {
                object = array.optJSONObject(i);
                String[] data = new String[12];
                data[0] = object.optString("uID");
                data[1] = object.optString("uName");
                data[2] = object.optString("uEmail");
                data[3] = object.optString("uPhone");
                data[4] = object.optString("Year");
                data[5] = object.optString("Branch");
                data[6] = object.optString("Division");
                data[7] = object.optString("College");
                data[8] = object.optString("uCreated");
                data[9] = object.optString("uModified");
                data[10] = object.optString("uPaymentStatus");
                data[11]="F";
                new LocalDBHandler(context).insertPartcipantData(data);
            }
        } catch (NullPointerException e) {

        }
    }

    public static List<ParticipantContent.Participant> getParticipantList(Context context){
        LocalDBHandler localDBHandler=new LocalDBHandler(context);
        ArrayList<String> participantNames = localDBHandler.getParticipantNames();
        ArrayList<String> participantPhones= localDBHandler.getParticipantPhone();
        ArrayList<String> participantPaymentStatus = localDBHandler.getParticipantPaymentStatus();
        List<ParticipantContent.Participant> participantList=new ArrayList<>();
        for(int i=0;i<participantNames.size();i++)
            participantList.add(new ParticipantContent.Participant(participantPhones.get(i),participantNames.get(i),participantPaymentStatus.get(i)));
        return participantList;
    }

    public static List<String> getEventStats(Context context){
        LocalDBHandler localDBHandler=new LocalDBHandler(context);
        ArrayList<String> eventStats=new ArrayList<>();
        eventStats.add(localDBHandler.getTotalParticipants());
        eventStats.add(localDBHandler.getPresentParticipants());
        eventStats.add(localDBHandler.getAbsentParticipants());
        eventStats.add(localDBHandler.getPaymentCompleteParticipants());
        eventStats.add(localDBHandler.getPaymentIncompleteParticipants());
        eventStats.add(localDBHandler.getPaymentPartialCompleteParticipants());
        return eventStats;
    }
}