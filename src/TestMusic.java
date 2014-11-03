import java.util.Random;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;


public class TestMusic {

	public static Random r = new Random();
	
	public static void main(String[] args)
	{
		System.out.printf("Hello world%n");
		playNotes();
	}
	
	public static void playNotes()
	{
		try{
			Sequence seq = new Sequence(Sequence.PPQ, 4);
			Track track = seq.createTrack();
			for (int i = 0; i < 120; i+= 4) {
				int d = (int) (i / 4) % 24 + 32;
                track.add(makeEvent(ShortMessage.NOTE_ON, 1, d, 127, i));
                //track.add(makeEvent(ShortMessage.CONTROL_CHANGE, 1, 127, 0, i));
                track.add(makeEvent(ShortMessage.NOTE_OFF, 1, d, 127, i+4));
            }
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequencer.setSequence(seq);
            sequencer.start();
            //sequencer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static  MidiEvent makeEvent(int cmd, int chan, int d1, int d2, int tick) {
        MidiEvent event = null;
        try {
            ShortMessage sm = new ShortMessage();
            sm.setMessage(cmd, chan, d1, d2);
            event = new MidiEvent(sm, tick);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace(System.err);
        }
        return event;
    }
}
