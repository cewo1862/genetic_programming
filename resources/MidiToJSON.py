import music21
import json
import os
import argparse
from collections import OrderedDict

def main():
    parser = argparse.ArgumentParser(
        formatter_class=argparse.ArgumentDefaultsHelpFormatter)
    parser.add_argument("file",type=str,help="path to file with parameters")

    args = parser.parse_args()
    inFile = "./" + args.file + ".midi"
    outFile ="./" + args.file + ".json"
    if not os.path.isfile(inFile):
        print(inFile + " is no file")
        return
    convertedFile = MidiToJSON(inFile)
    isFirst = 1
    with open(outFile, "w") as outputfile:
        outputfile.write("{\"element\": [")
        for data in convertedFile['data']:
            if isFirst == 0:
                outputfile.write(","+"\n")
                outputfile.write(json.dumps(data))
            if isFirst == 1:
                outputfile.write(json.dumps(data))
                isFirst = 0
        outputfile.write("]}")
    print("\nExported result to " + outFile)
    return

def getDuration(durationIn):
        ''' Get the duration as a float value of a string.
            Returns 1.0 if converting fails! '''

        duration = 1.0

        try:
            durstr = str(durationIn)
            # check for fraction
            dursplit = durstr.split("/")
            if len(dursplit) > 1:
                duration = round(float(dursplit[0]) / float(dursplit[1]), 2)
            else:
                duration = float(durstr)
        except Exception as n:
            print("Failed to convert duration: " + durstr)
            print(n)

        return duration


def MidiToJSON(file):
        
        midi = music21.converter.parse(file)
        instruments = music21.instrument.partitionByInstrument(midi)
        notes = None
        output = []

        if instruments:
            notes = instruments.parts[0].recurse()
        else:
            notes = midi.flat.notes

        for e in notes:
            if isinstance(e, music21.note.Note):
                output.append(OrderedDict([
                    ('type', 'note'),
                    ('name', str(e.name)),
                    ('octave', int(e.octave)),
                    ('pitch', str(e.pitch)),
                    ('offset', float(e.offset)),
                    ('duration', getDuration(e.duration.quarterLength))
                ]))
            elif isinstance(e, music21.chord.Chord):
                chord_notes = [str(p) for p in e.pitches]
                output.append(OrderedDict([
                    ('type', 'chord'),
                    ('name', str(e.commonName)),
                    ('offset', float(e.offset)),
                    ('duration', getDuration(e.duration.quarterLength)),
                    ('pitch', chord_notes)
                ]))
        return {
        'data': output
        }

if __name__ == '__main__':
    main()
