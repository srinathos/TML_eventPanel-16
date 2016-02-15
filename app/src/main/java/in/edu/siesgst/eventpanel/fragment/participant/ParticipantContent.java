package in.edu.siesgst.eventpanel.fragment.participant;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ParticipantContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Participant> ITEMS = new ArrayList<Participant>();


    private static int getCount(){
        return ITEMS.size();
    }

    private static void addItem(Participant item) {
        ITEMS.add(item);
    }

    private static Participant createParticipantItem(int position) {
        return new Participant(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class Participant {
        public final String phone;
        public final String name;
        public final String paymentStatus;

        public Participant(String phone, String name, String paymentStatus) {
            this.phone = phone;
            this.name= name;
            this.paymentStatus = paymentStatus;
        }
    }
}
