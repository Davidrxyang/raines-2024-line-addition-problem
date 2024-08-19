# CONFIGURATION SETuP FOR LINE ADDITION ALGORITHM

The line addition algorithm takes in a config file to specify parameters. Read more about specific parameters from the research paper. 

## Usage

The format for the config file is:

`field-name:value:comments`

If a field is not specified or does not exist in the user-created config file, the program will use default values.

## Configuration Fields

### experiment-name

type: string
name of the experiment run

### p-max

type: double
defines the circuity factor

### max-length

type: double
defines the max length of a valid line

### min-length

type: double
defines the min length of a valid line

### corridor-height

type: double
defines the dimensions of the line construction corridor

### demand-adjustment-weight

type: double
defines the weight of adjusted additional demand

### target-efficiency

type: double
defines the threshold efficiency to terminate the algorithm

### logging

type: boolean (true/false)
toggles logging output functionality

### evaluation-config

type: string
file name and path for evaluation config parameters file