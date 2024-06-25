# Efficiency and Cost Evaluation Methods for Public Transportation Networks

line construction costs and transit route efficiencies are calculated using separate methods. The results are combined to generate network efficiency analysis. 

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