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
			makeTrack(seq);
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequencer.setSequence(seq);
            sequencer.setTempoInBPM(60.0f);
            sequencer.start();
            //sequencer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	private static void makeTrack(Sequence seq)
	{
		Track track = seq.createTrack();
		Random r = new Random();
		int time = 0;
		for (int i = 0; i < 20; i++)
		{
			Note n = Note.randomNote(r.nextLong());
			addNote(track, n, time);
			System.out.printf("Made note with duration %d%n", n.duration);
			time += n.duration;
		}
		
//		Track track = seq.createTrack();
//		for (int i = 0; i < 120; i+= 4) {
//			int d = (int) (i / 4) % 24 + 32;
//            track.add(makeEvent(ShortMessage.NOTE_ON, 1, d, 127, i));
//            //track.add(makeEvent(ShortMessage.CONTROL_CHANGE, 1, 127, 0, i));
//            track.add(makeEvent(ShortMessage.NOTE_OFF, 1, d, 127, i+4));
//        }
	}

	private static void addNote(Track track, Note n, int start)
	{
		//duration is in terms of the PPQ value as defined by the sequence
		int duration = n.duration;
		//Pitch is between 0 and 128, not inclusive
		int pitch = n.getMidiPitch();
		//The channel here is hard defined to be 1; may need changing
		//The velocity is also hard defined at 127
		track.add(makeEvent(ShortMessage.NOTE_ON, 1, pitch, 127, start));
		track.add(makeEvent(ShortMessage.NOTE_OFF, 1, pitch, 127, start + duration));
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
