package stanchat.util;


import java.util.Set;
import java.util.regex.Pattern;

public class Facts {
    private static final Set<String> SAD_WORDS = Set.of("sad","down","depressed","unhappy","lonely","miserable","cry");
    private static final Set<String> HAPPY_WORDS = Set.of("happy","great","awesome","good","joy","excited","glad");
    private static final Set<String> ANGRY_WORDS = Set.of("angry","mad","furious","upset","annoyed");

    public static String detectTone(String text) {
        String lowered = text.toLowerCase();
        for (String w : SAD_WORDS) if (lowered.contains(w)) return "empathetic";
        for (String w : ANGRY_WORDS) if (lowered.contains(w)) return "calm";
        for (String w : HAPPY_WORDS) if (lowered.contains(w)) return "playful";
        return "neutral";
    }

    private static final Pattern[] HALLU_PATTERNS = new Pattern[] {
            Pattern.compile("did you see me", Pattern.CASE_INSENSITIVE),
            Pattern.compile("were you there", Pattern.CASE_INSENSITIVE),
            Pattern.compile("what do i look like", Pattern.CASE_INSENSITIVE),
            Pattern.compile("where were you", Pattern.CASE_INSENSITIVE),
            Pattern.compile("did you watch", Pattern.CASE_INSENSITIVE)
    };

    public static boolean isPersonalImpossibleQuestion(String text) {
        for (Pattern p : HALLU_PATTERNS) {
            if (p.matcher(text).find()) return true;
        }
        return false;
    }
}

