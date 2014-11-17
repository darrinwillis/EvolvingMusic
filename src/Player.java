import java.util.List;
import java.util.Random;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;


public class Player {

	public static Random r = new Random();
	
	public float BPM = 60.0f;
	
	private Sequencer sequencer;
	
	public Player()
	{
		try {
			sequencer = MidiSystem.getSequencer();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	private void open() throws MidiUnavailableException
	{
		if (!sequencer.isOpen())
		{
			sequencer.open();
		}
	}
	
	public void close()
	{
		if (sequencer.isOpen())
		{
			sequencer.close();
		}
	}
	
	public void playTree(MusicTree mt)
	{
		try {
			Sequence seq = synthesize(mt);
			playSequence(seq);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}
	
	public void playSequence(Sequence seq)
	{
		try{
            open();
            sequencer.setSequence(seq);
            sequencer.setTempoInBPM(BPM);
            sequencer.start();
            while(sequencer.isRunning()){
            	Thread.yield();
            }
            sequencer.stop();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	private Sequence synthesize(MusicTree mt) throws InvalidMidiDataException
	{
		Sequence seq = new Sequence(Sequence.PPQ, 4);
		addTree(seq.createTrack(), mt);
		return seq;
	}

	private void addTree(Track track, MusicTree mt)
	{
		List<MusicEvent> events = mt.render();
		for (MusicEvent me : events)
		{
			addNote(track, me.note, me.time);
		}
		
	}
	
	private void addNote(Track track, Note n, int start)
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
	
    private MidiEvent makeEvent(int cmd, int chan, int d1, int d2, int tick) {
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
