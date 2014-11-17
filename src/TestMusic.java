import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;


public class TestMusic {

	public static Random r = new Random();
	
	private Sequencer sequencer;
	
	public static void main(String[] args)
	{
		new TestMusic().run();
	}
	
	public void run()
	{
		playNotes();
		int rating = getUserRating();
		System.out.printf("the rating was %d%n", rating);
		this.sequencer.close();
	}
	
	public int getUserRating()
	{
		System.out.printf("Rate what you just heard from 1-10%n");
		Scanner scan = new Scanner(System.in);
		int input = -1;
		while (!(input >= 1 && input <= 10))
		{
			while (!scan.hasNextInt()) {
				scan.next();
			}
			input = scan.nextInt();
		}
		scan.close();
		return input;
	}
	
	public void playNotes()
	{
		try{
			Sequence seq = new Sequence(Sequence.PPQ, 4);
			makeTrack(seq);
			sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequencer.setSequence(seq);
            sequencer.setTempoInBPM(60.0f);
            sequencer.start();
            while(sequencer.isRunning()){
            	Thread.yield();
            }
            sequencer.stop();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	private void makeTrack(Sequence seq)
	{
		Track track = seq.createTrack();
		MusicTree mt = MusicTree.RandomTree(r.nextLong(), 7);
		addTree(track, mt);
//		int time = 0;
//		for (int i = 0; i < 20; i++)
//		{
//			Note n = Note.randomNote();
//			addNote(track, n, time);
//			time += n.duration;
//		}
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
