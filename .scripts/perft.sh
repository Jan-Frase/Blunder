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

# Change to the project root directory
cd .. || exit

# Execute Java program with arguments
./gradlew --quiet --console=plain runPerftTest --args="$DEPTH \"$FEN_STRING\" ${MOVES_ARRAY[*]}"
