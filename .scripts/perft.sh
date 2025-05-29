#!/bin/bash

# Check if required arguments are provided
if [ "$#" -lt 2 ]; then
    echo "Usage: $0 <depth> <fen_string> [moves...]"
    exit 1
fi

# Extract depth and fen string from arguments
DEPTH=$1
FEN_STRING=$2
shift 2

# Compile moves array from remaining arguments
MOVES_ARRAY=("$@")

# Execute Java program with arguments
java -cp target/classes:target/test-classes de.janfrase.blunder.engine.movegen.PerftTest "$DEPTH" "$FEN_STRING" "${MOVES_ARRAY[@]}"
