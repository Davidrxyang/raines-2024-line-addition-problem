# Efficiency and Cost Evaluation Methods for Public Transportation Networks

line construction costs and transit route efficiencies are calculated using separate methods. The results are combined to generate network efficiency analysis. 

Efficiency is measured in arbitrary units and should be used in to compare different networks. The same configuration file should be used for all networks within the comparison. Details regarding configuration are specified later in this document.

## Cost Function

The estimated construction cost of a new line is captured based on empirical construction cost data provided by the Eno Center for Transportation (Aevez & Lewis 2020).

## Efficiency Evaluation Function

Efficiency is calculated at the route level and the network level. 

### Route Efficiency Evaluation

A route is defined as the most efficient path from one specific station in the network to a different specific station in the network. Routes between distinct station pairs are generated using the PathPlanning algorithm (Liu et al., 2001) found in existing-algorithms/PathPlanning.java. 

The efficiency of an individual route is calculated on a per-rider basis and accounts for travel time (which is calculated based on distance covered, assuming constant travel speed,) number of stations (stops) and number of transfers required. 

Route efficiency is measured in arbitrary units.

### Network Efficiency Evaluation

The overall efficiency of a network is calculated as the sum of the efficiencies of all possible routes within the network weighed by their route ridership (based on empirical ridership data), divided by the total estimated construction cost of the network. Total network efficiency is measured in units per dollar.

## Usage

The evaluation class takes in a config file that specifies parameters such as costMode. The format for the config file is:

`field-name:value:comments`

If a field is not specified or does not exist in the user-created config file, the program will use default values.

## Configuration Fields

### Cost Mode

defines the way cost data is used in the construction cost calculation

options: 

`cost-mode:global-average` 

considers all cost data in the calculation

`cost-mode:state-average-DC`

considers only cost data from the specified state in the calculation. NOTE: replace "DC" with any state abbreviation within the dataset.

### Weights

defines how each parameter is valued within the efficiency calculation algorithm

`
transfer-weight:0.4
station-weight:0.2
distance-weight:0.8
`

each value following the field name should be a floating point number within the range [0.0 - 1.0]

a higher value represents greater significance within the efficiency consideration

### Regression

defines the regression mode for the overall network efficiency calculation

options:

`regression:linear-linear`

considers both efficiency and cost as linear variables

`regression:log-linear`

considers efficiency as logarithmic and cost as linear


`regression:linear-log`

considers efficiency as linear and cost as logarithmic

`regression:log-log`

considers both efficiency and cost as logarithmic variables




