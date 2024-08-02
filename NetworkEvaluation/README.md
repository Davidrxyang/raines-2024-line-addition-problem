# Efficiency and Cost Evaluation Methods for Public Transportation Networks

line construction costs and transit route efficiencies are calculated using separate methods. The results are combined to generate network efficiency analysis. 

Efficiency is measured in arbitrary units and should be used in to compare different networks. The same configuration file should be used for all networks within the comparison. Details regarding configuration are specified later in this document.

## Cost Function

The estimated construction cost of a new line is captured based on empirical construction cost data provided by the Eno Center for Transportation (Aevez & Lewis 2020).

## Efficiency Evaluation Function

Efficiency is calculated at the route level and the network level. 

### Route Efficiency Evaluation

A route is defined as the most efficient path from one specific station in the network to a different specific station in the network. Routes between distinct station pairs are generated using the PathPlanning algorithm (Liu et al., 2001) found in existing-algorithms/PathPlanning.java. 

The complexity of an individual route is calculated on a per-rider basis and accounts for travel time (which is calculated based on distance covered, assuming constant travel speed,) number of stations (stops) and number of transfers required. 

Route complexity is measured in arbitrary units.

### Network Efficiency Evaluation

The overall efficiency of a network is calculated as the sum of the efficiencies of all possible routes within the network weighed by their route ridership (based on empirical ridership data), multiplied by the total estimated construction cost of the network. Total network efficiency is measured in units per dollar.

### Line Efficiency Evaluation

The efficiency of a line is calculated as the sum of all complexities of all possible origin destination combinations of its stations, weighted by ridership data, multiplied by the construction cost of the line.

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

### Evaluation mode

defines the logic behind evaluating route efficiency

options: 

`eval-mode:standard`

simply considers the distance covered, amount of transfers, and amount of station stops along the route.

mathematical formula for adjusted route complexity:

Standardcomplexity = (Transfers * c1) + (Stations * c2) + (Distance * c3)

where c1, c2, c3 are specified parameters, details below.

route complexity is measured in arbitrary units, generally representing the complexity of a route. 

`eval-mode:adjusted`

considers the above metrics, adjusted accounting for the direct geographical distance between the origin and destination station and a weight coefficient. 

mathematical formula for adjusted route complexity:

FinalComplexity = (StandardComplexity) * (StandardComplexity / (c1 * GeographicalDistance))

where c1 is a specified parameter

Logic: adjusted evaluation mode adjusts route complexity by comparing the public transit route with the direct geographical "route" between the origin and destination. For two origin-destination pairs with the same geographical separation, if one origin-destination pair's public transit route has a higher complexity than the other, then this complexity should be further "penalized."

### Line Evaluation Mode

defines evaluation logic behind route complexity, but for line efficiency calculations. Works the same way. 

`line-eval-mode:standard`

`line-eval-mode:adjusted`


### Weights

defines how each parameter is valued within the complexity calculation algorithm

`
transfer-weight:0.4
station-weight:0.2
distance-weight:0.8
adjustment-weight:0.5
`

each value following the field name should be a floating point number within the range [-1.0 - 1.0]

adjustment-weight represents the weight of the direct geographical distance adjustment used with the adjusted evaluation mode

a higher value represents greater significance within the complexity consideration

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

### Line Regression

defines the regression mode for the line efficiency evaluation

options:

`line-regression:linear-linear`

considers both efficiency and cost as linear variables

`line-regression:log-linear`

considers efficiency as logarithmic and cost as linear


`line-regression:linear-log`

considers efficiency as linear and cost as logarithmic

`line-regression:log-log`

considers both efficiency and cost as logarithmic variables