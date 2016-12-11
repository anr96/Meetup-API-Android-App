//package com.fall16.csc413.team12.eventbrowserfinale;
//
//import android.content.Context;
//import android.widget.ImageView;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
///**
// * Created by AmandaNikkole on 11/26/16.
// */
//
//public class StoryLab {
//    //singleton for stories
//    private static StoryLab sStoryLab;
//    private ImageView mImageView;
//
//    private ArrayList<Story> mStories;
//
//    public static StoryLab get(Context context){
//        if(sStoryLab == null){
//            sStoryLab = new StoryLab(context);
//        }
//        return sStoryLab;
//    }
//
//    private StoryLab(Context context){
//        mStories = new ArrayList<>();
//
//        Story story1 = new Story("Frankenstein", "Ugly monster looking for love" +
//                " gets constantly rejected.");
//        story1.setName("Card " + 1);
//        mStories.add(story1);
//
//        Story story2 = new Story("Shrek", "Extremely Shrektastic ogre betrays" +
//                " short king in order to find true love.");
//        story2.setName("Card " + 2);
//        mStories.add(story2);
//
//        Story story3 = new Story("Dracula", "Man journals about why he is soooo over" +
//				" Transylvania.");
//        story3.setName("Card " + 3);
//        mStories.add(story3);
//
//        Story story4 = new Story("Harry Potter", "Boy with forehead scar is desperate to" +
//                " get rid of guy without a nose.");
//        story4.setName("Card " + 4);
//        mStories.add(story4);
//
//        Story story5 = new Story("1Q84", "Weird things coming out of weird places.");
//        story5.setName("Card " + 5);
//        mStories.add(story5);
//
//		Story story6 = new Story("Con Air", "LITERALLY THE BEST MOVIE OF ALL TIME. NO QUESTION.");
//		story6.setName("Card " + 6);
//		mStories.add(story6);
//
//		Story story7 = new Story("Rocky XV: Drago's Revenge", "Ivan Drago is back...with a" +
//				" vengeance.");
//		story7.setName("Card " + 7);
//		mStories.add(story7);
//
//		Story story8 = new Story("Batman vs. Predator", "The Dark Knight faces his biggest" +
//				" challenge yet in finding the beast from outer space.");
//		story8.setName("Card " + 8);
//		mStories.add(story8);
//
//		Story story9 = new Story("Entourage: The Sequel to the Sequel", "The gang is back once" +
//				" more minus Turtle. No one likes Turtle.");
//		story9.setName("Card " + 9);
//		mStories.add(story9);
//
//		Story story10 = new Story("Con Air 2", "OMG THIS IS LITERALLY THE BEST SEQUEL EVER!");
//		story10.setName("Card " + 10);
//		mStories.add(story10);
//
//    }
//
//    public List<Story> getStories() {
//        return mStories;
//    }
//
//    public Story getStory(UUID id) {
//        for (Story story : mStories) {
//            if (story.getUuid().equals(id)) {
//                return story;
//            }
//        }
//        return null;
//    }
//}
//
