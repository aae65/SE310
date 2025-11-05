package abstractedInputOutput;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

/**
 * Please note this class is here as an example and the error checking is minimal.
 *
 * @author Sean Grimes, sean@seanpgrimes.com
 */
public class AudioOut extends Out {
    // The voice model to use
    private final String voice_name = "kevin16";
    // The manager and voice objects we need to use freetts
    private final VoiceManager voiceManager;
    private final Voice voice;

    // No public c'tor to fit the singleton requirements
    // NOTE: This c'tor needs to have some code to setup the audio library, unlike the
    // other abstractedInputOutput.Out classes
    private AudioOut() {
        System.setProperty("freetts.voices",
                "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        this.voiceManager = VoiceManager.getInstance();
        this.voice = voiceManager.getVoice(voice_name);
    }
    // The instance that's returned in the getInstance() call
    private static AudioOut instance;

    // Return a abstractedInputOutput.ConsoleOut object in a threadsafe manner with lazy initialization
    public static AudioOut getInstance() {
        if(instance == null) {
            synchronized (AudioOut.class) {
                if(instance == null)
                    instance = new AudioOut();
            }
        }
        return instance;
    }

    public void say (Object msg) {
        // Some extra steps to setup and deconstruct the voice
        this.voice.allocate();
        this.voice.speak(msg.toString());
        this.voice.deallocate();
    }
}
