package com.fall16.csc413.team12.eventbrowserfinale;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by AmandaNikkole on 12/10/16.
 * Singleton for meetups, used for second activity
 */

public class MeetUpLab {

    private static MeetUpLab sMeetUpLab;
    private ArrayList<MeetUp> mMeetUps;

    public MeetUpLab(Context context){
        mMeetUps = new ArrayList<>();
    }

    public static MeetUpLab get(Context context){
        if (sMeetUpLab == null){
            sMeetUpLab = new MeetUpLab(context);
        }
        return sMeetUpLab;
    }


     public List<MeetUp> getMeetUps() {return mMeetUps;}

     public MeetUp getMeetUp(UUID meetUpId) {
        for (MeetUp meetUp : mMeetUps) {
            if (meetUp.getMeetUpId().equals(meetUpId)) {
            return meetUp;
            }
        }
        return null;
     }


}
