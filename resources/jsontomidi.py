from music21 import stream, note, chord, instrument, duration
from pprint import pprint
import argparse
import sys
import os
import json

def main():
    parser = argparse.ArgumentParser(
        formatter_class=argparse.ArgumentDefaultsHelpFormatter)
    parser.add_argument("file",type=str,help="path to file with parameters")

    args = parser.parse_args()
    inFile = args.file + ".json"
    outFile = args.file + ".mid"
    if not os.path.isfile(inFile):
        print(inFile + " is no file")
        return

    with open(inFile) as f:
        data = json.load(f)

    s1 = stream.Stream()

    for item in enumerate(data["element"]):
        type = data["element"][item[0]]["type"]
        if type == "chord":
            notes = []
            numNotes = 0
            for pitches in enumerate(data["element"][item[0]]["pitch"]):
                pitch1 = data["element"][item[0]]["pitch"][pitches[0]]
                offset = data["element"][item[0]]["offset"]
                duration = data["element"][item[0]]["duration"]
                subnote = note.Note(nameWithOctave = pitch1)
                subnote.duration.quarterLength = duration
                subnote.offset = offset
                numNotes = pitches[0]+1
                notes.append(subnote)

            if numNotes == 2:
                chord1 = chord.Chord([notes[0],notes[1]])
                #print(chord1)
            elif numNotes == 3:
                chord1 = chord.Chord([notes[0],notes[1],notes[2]])
                #print(chord1)
            elif numNotes == 4:
                chord1 = chord.Chord([notes[0],notes[1],notes[2],notes[3]])
                #print(chord1)

            s1.append(chord1)

        elif type == "note":
            pitch = data["element"][item[0]]["pitch"]
            offset = data["element"][item[0]]["offset"]
            duration = data["element"][item[0]]["duration"]

            note1 = note.Note(nameWithOctave = pitch)
            note1.duration.quarterLength = duration
            note1.offset = offset

            s1.append(note1)
    print(s1)
    s1.write('midi', fp= outFile)
    #s1.show('midi') # to hear the stream


    #tmp = data["maps"][0]["id"]
    #print(tmp)


if __name__ == '__main__':
    main()
