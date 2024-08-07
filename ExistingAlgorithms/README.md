# Line Addition Problem Algorithms

## 1. Route planning algorithms

A study on the redesign of the WMATA network using existing rail connections, where existing connections between stations are maintained and no new rail connections between stations are built. Only the lines are redesigned

Algorithms include:

### 1. PIA

Parameters:

D0min: 0.65

D01min: 1

Max circuity factor: 2

Results:

Line 1: [wiehle -> spring hill, spring hill -> greensboro, greensboro -> tysons corner, tysons corner -> mclean, mclean -> east falls church, east falls church -> ballston, ballston -> virginia square-gmu, virginia square-gmu -> clarendon, clarendon -> court house, court house -> rosslyn, rosslyn -> foggy bottom, foggy bottom -> farragut west]

Line 2: [new carrollton -> landover, landover -> cheverly, cheverly -> deanwood, deanwood -> minnesota avenue, minnesota avenue -> stadium-armory, stadium-armory -> potomac avenue, potomac avenue -> eastern market, eastern market -> capitol south, capitol south -> federal center sw, federal center sw -> lenfant plaza, lenfant plaza -> smithsonian, smithsonian -> federal triangle, federal triangle -> metro center, metro center -> mcpherson square, mcpherson square -> farragut west, farragut west -> foggy bottom, foggy bottom -> rosslyn, rosslyn -> arlington cemetery, arlington cemetery -> pentagon, pentagon -> pentagon city, pentagon city -> crystal city, crystal city -> reagan washington national airport, reagan washington national airport -> potomac yard, potomac yard -> braddock road, braddock road -> king street, king street -> van dorn street, van dorn street -> franconia-springfield]

Line 3: [huntington -> eisenhower avenue, eisenhower avenue -> king street, king street -> braddock road, braddock road -> potomac yard, potomac yard -> reagan washington national airport, reagan washington national airport -> crystal city, crystal city -> pentagon city, pentagon city -> pentagon, pentagon -> lenfant plaza, lenfant plaza -> smithsonian, smithsonian -> federal triangle, federal triangle -> metro center, metro center -> mcpherson square, mcpherson square -> farragut west, farragut west -> foggy bottom, foggy bottom -> rosslyn, rosslyn -> court house, court house -> clarendon, clarendon -> virginia square-gmu, virginia square-gmu -> ballston, ballston -> east falls church, east falls church -> west falls church, west falls church -> dunn loring, dunn loring -> vienna]

Line 4: [glenmont -> wheaton, wheaton -> forest glen, forest glen -> silver spring, silver spring -> takoma, takoma -> fort totten, fort totten -> brookland, brookland -> rhode island avenue, rhode island avenue -> new york ave, new york ave -> union station, union station -> judiciary square, judiciary square -> gallery place-chinatown, gallery place-chinatown -> archives-navy memorial, archives-navy memorial -> lenfant plaza, lenfant plaza -> smithsonian, smithsonian -> federal triangle, federal triangle -> metro center, metro center -> farragut north, farragut north -> dupont circle, dupont circle -> woodley park-zoo, woodley park-zoo -> cleveland park, cleveland park -> van ness-udc, van ness-udc -> tenleytown-au, tenleytown-au -> friendship heights, friendship heights -> bethesda, bethesda -> medical center, medical center -> grosvenor, grosvenor -> north bethesda, north bethesda -> twinbrook, twinbrook -> rockville, rockville -> shady grove]

Line 5: [wiehle -> spring hill, spring hill -> greensboro, greensboro -> tysons corner, tysons corner -> mclean, mclean -> east falls church, east falls church -> ballston, ballston -> virginia square-gmu, virginia square-gmu -> clarendon, clarendon -> court house, court house -> rosslyn, rosslyn -> arlington cemetery, arlington cemetery -> pentagon, pentagon -> lenfant plaza, lenfant plaza -> archives-navy memorial, archives-navy memorial -> gallery place-chinatown, gallery place-chinatown -> mcpherson square, mcpherson square -> metro center]

Line 6: [columbia heights -> u street-cardozo, u street-cardozo -> shaw-howard university, shaw-howard university -> mt vernon sq, mt vernon sq -> gallery place-chinatown, gallery place-chinatown -> archives-navy memorial, archives-navy memorial -> lenfant plaza, lenfant plaza -> pentagon, pentagon -> pentagon city, pentagon city -> crystal city, crystal city -> reagan washington national airport, reagan washington national airport -> potomac yard, potomac yard -> braddock road, braddock road -> king street, king street -> eisenhower avenue, eisenhower avenue -> huntington]

Line 7: [greenbelt -> college park-u of md, college park-u of md -> hyattsville crossing, hyattsville crossing -> west hyattsville, west hyattsville -> fort totten, fort totten -> brookland, brookland -> rhode island avenue, rhode island avenue -> new york ave, new york ave -> union station, union station -> judiciary square, judiciary square -> gallery place-chinatown, gallery place-chinatown -> archives-navy memorial, archives-navy memorial -> lenfant plaza, lenfant plaza -> pentagon, pentagon -> arlington cemetery, arlington cemetery -> rosslyn, rosslyn -> court house, court house -> clarendon, clarendon -> virginia square-gmu, virginia square-gmu -> ballston, ballston -> east falls church, east falls church -> west falls church, west falls church -> dunn loring, dunn loring -> vienna]

Line 8: [downtown largo -> morgan blvd, morgan blvd -> addison road, addison road -> capitol heights, capitol heights -> benning road, benning road -> stadium-armory, stadium-armory -> potomac avenue, potomac avenue -> eastern market, eastern market -> capitol south, capitol south -> federal center sw, federal center sw -> lenfant plaza, lenfant plaza -> smithsonian, smithsonian -> federal triangle, federal triangle -> metro center, metro center -> mcpherson square, mcpherson square -> farragut west]

Line 9: [branch avenue -> suitland, suitland -> naylor road, naylor road -> southern avenue, southern avenue -> congress heights, congress heights -> anacostia, anacostia -> navy yard, navy yard -> waterfront, waterfront -> lenfant plaza, lenfant plaza -> archives-navy memorial, archives-navy memorial -> gallery place-chinatown, gallery place-chinatown -> mt vernon sq, mt vernon sq -> shaw-howard university, shaw-howard university -> u street-cardozo, u street-cardozo -> columbia heights, columbia heights -> union station, union station -> georgia avenue-petworth, georgia avenue-petworth -> fort totten, fort totten -> brookland, brookland -> rhode island avenue, rhode island avenue -> new york ave]

Number of lines: 9

D0: 0.7010037164677346

D01: 1.0

Observations:

Need for a join line procedure - by examination it is obvious line 1 and line 8 could be joined, but in PIA there is no procedure for this operation

### 2. RGA

Parameters:

D0min: 0.70

D01min: 1

Max circuity factor: 1.5

Node sharing factor: 1

Number of initial skeletons: 9

Results:

Line 1: [glenmont -> wheaton, wheaton -> forest glen, forest glen -> silver spring, silver spring -> takoma, takoma -> fort totten, fort totten -> brookland, brookland -> rhode island avenue, rhode island avenue -> new york ave, new york ave -> union station, union station -> judiciary square, judiciary square -> gallery place-chinatown, gallery place-chinatown -> metro center, metro center -> farragut north, farragut north -> dupont circle, dupont circle -> woodley park-zoo, woodley park-zoo -> cleveland park, cleveland park -> van ness-udc, van ness-udc -> tenleytown-au, tenleytown-au -> friendship heights, friendship heights -> bethesda, bethesda -> medical center, medical center -> grosvenor, grosvenor -> north bethesda, north bethesda -> twinbrook, twinbrook -> rockville, rockville -> shady grove]

Line 2: [vienna -> dunn loring, dunn loring -> west falls church, west falls church -> east falls church, east falls church -> ballston, ballston -> virginia square-gmu, virginia square-gmu -> clarendon, clarendon -> court house, court house -> rosslyn, rosslyn -> foggy bottom, foggy bottom -> farragut west, farragut west -> mcpherson square, mcpherson square -> metro center, metro center -> federal triangle, federal triangle -> smithsonian, smithsonian -> lenfant plaza, lenfant plaza -> pentagon, pentagon -> pentagon city, pentagon city -> crystal city, crystal city -> reagan washington national airport]

Line 3: [mt vernon sq -> shaw-howard university, shaw-howard university -> u street-cardozo, u street-cardozo -> columbia heights, columbia heights -> georgia avenue-petworth, georgia avenue-petworth -> fort totten, fort totten -> brookland, brookland -> rhode island avenue, rhode island avenue -> new york ave, new york ave -> union station, union station -> judiciary square, judiciary square -> gallery place-chinatown, gallery place-chinatown -> metro center, metro center -> mcpherson square, mcpherson square -> farragut west, farragut west -> foggy bottom, foggy bottom -> rosslyn, rosslyn -> court house, court house -> clarendon, clarendon -> virginia square-gmu, virginia square-gmu -> ballston, ballston -> east falls church, east falls church -> mclean, mclean -> tysons corner, tysons corner -> greensboro, greensboro -> spring hill, spring hill -> wiehle]

Line 4: [shady grove -> rockville, rockville -> twinbrook, twinbrook -> north bethesda, north bethesda -> grosvenor, grosvenor -> medical center, medical center -> bethesda, bethesda -> friendship heights, friendship heights -> tenleytown-au, tenleytown-au -> van ness-udc, van ness-udc -> cleveland park, cleveland park -> woodley park-zoo, woodley park-zoo -> dupont circle, dupont circle -> farragut north, farragut north -> metro center, metro center -> federal triangle, federal triangle -> smithsonian, smithsonian -> lenfant plaza, lenfant plaza -> federal center sw, federal center sw -> capitol south, capitol south -> eastern market, eastern market -> potomac avenue, potomac avenue -> stadium-armory, stadium-armory -> benning road, benning road -> capitol heights, capitol heights -> addison road, addison road -> morgan blvd, morgan blvd -> downtown largo]

Line 5: [reagan washington national airport -> crystal city, crystal city -> pentagon city, pentagon city -> pentagon, pentagon -> arlington cemetery, arlington cemetery -> rosslyn, rosslyn -> foggy bottom, foggy bottom -> farragut west, farragut west -> mcpherson square, mcpherson square -> metro center, metro center -> farragut north, farragut north -> dupont circle, dupont circle -> woodley park-zoo, woodley park-zoo -> cleveland park, cleveland park -> van ness-udc, van ness-udc -> tenleytown-au, tenleytown-au -> friendship heights, friendship heights -> bethesda, bethesda -> medical center, medical center -> grosvenor, grosvenor -> north bethesda, north bethesda -> twinbrook, twinbrook -> rockville, rockville -> shady grove]

Line 6: [new carrollton -> landover, landover -> cheverly, cheverly -> deanwood, deanwood -> minnesota avenue, minnesota avenue -> stadium-armory, stadium-armory -> potomac avenue, potomac avenue -> eastern market, eastern market -> capitol south, capitol south -> federal center sw, federal center sw -> lenfant plaza, lenfant plaza -> smithsonian, smithsonian -> federal triangle, federal triangle -> metro center, metro center -> mcpherson square, mcpherson square -> farragut west, farragut west -> foggy bottom, foggy bottom -> rosslyn, rosslyn -> court house, court house -> clarendon, clarendon -> virginia square-gmu, virginia square-gmu -> ballston, ballston -> east falls church, east falls church -> mclean, mclean -> tysons corner, tysons corner -> greensboro, greensboro -> spring hill, spring hill -> wiehle]

Line 7:[huntington -> eisenhower avenue, eisenhower avenue -> king street, king street -> braddock road, braddock road -> potomac yard, potomac yard -> reagan washington national airport, reagan washington national airport -> crystal city, crystal city -> pentagon city, pentagon city -> pentagon, pentagon -> lenfant plaza, lenfant plaza -> federal center sw, federal center sw -> capitol south, capitol south -> eastern market, eastern market -> potomac avenue, potomac avenue -> stadium-armory, stadium-armory -> benning road, benning road -> capitol heights, capitol heights -> addison road, addison road -> morgan blvd, morgan blvd -> downtown largo]

Line 8: [huntington -> eisenhower avenue, eisenhower avenue -> king street, king street -> braddock road, braddock road -> potomac yard, potomac yard -> reagan washington national airport, reagan washington national airport -> crystal city, crystal city -> pentagon city, pentagon city -> pentagon, pentagon -> lenfant plaza, lenfant plaza -> archives-navy memorial, archives-navy memorial -> gallery place-chinatown, gallery place-chinatown -> mt vernon sq, mt vernon sq -> shaw-howard university, shaw-howard university -> u street-cardozo, u street-cardozo -> columbia heights, columbia heights -> georgia avenue-petworth, georgia avenue-petworth -> fort totten, fort totten -> west hyattsville, west hyattsville -> hyattsville crossing, hyattsville crossing -> college park-u of md, college park-u of md -> greenbelt]

Line 9: [franconia-springfield -> van dorn street, van dorn street -> king street, king street -> braddock road, braddock road -> potomac yard, potomac yard -> reagan washington national airport, reagan washington national airport -> crystal city, crystal city -> pentagon city, pentagon city -> pentagon, pentagon -> arlington cemetery, arlington cemetery -> rosslyn, rosslyn -> foggy bottom, foggy bottom -> farragut west, farragut west -> mcpherson square, mcpherson square -> metro center, metro center -> gallery place-chinatown, gallery place-chinatown -> archives-navy memorial, archives-navy memorial -> lenfant plaza, lenfant plaza -> waterfront, waterfront -> navy yard, navy yard -> anacostia, anacostia -> congress heights, congress heights -> southern avenue, southern avenue -> naylor road, naylor road -> suitland, suitland -> branch avenue]

D0: 0.7354823602872307

D01: 1.0

Number of lines: 9

It is worth noting that these algorithms are designed to be run on a transit network with easy-to-modify connections (such as a bus network). In such networks connections between stations can be modified and created easily, and the algorithms require a graph that contains all feasible connections between stations (i.e. if stations A and B are in adjacent "regions", then there exists an edge between them on the graph). For example, PIA takes a graph G where vertices are zones of the city, and edges between vertices exist if the two zones are connected and a connection can be logically created (Mauttone & Urquhart). In the case of metro networks, doing so is a lot less feasible because metro stops don't have to necessarily be in directly adjacent areas for a feasible connection to exist between them. Take the WMATA red line for example, every station on the west side from Shady Grove to Farragut North can have a logical connection to every station on the east side from Fort Totten to Glenmont. Such considerations will greatly complicate the generation of the input graph for such algorithms. Additionally, as most cities already have some form of an established metro system (or some form of overground rail), establishing new connections and rerouting existing lines can be expensive and in many cases infeasible.

The route expansion procedure in RGA means that in most cases every skeleton will be expanded into a line that reaches two terminal stations; exceptions occur when depending on the parameters such as circuity factor or node sharing factor, no feasible terminal stations can be added. In PIA, lines don't always reach terminal stations, as the expansion of a single skeleton into a full line is less prioritized than the insertion of node pairs into suitable lines. Multiple lines terminate at the Farragut West and Metro Center stations, and in some cases some of these lines can be joined together. This can happen when demand originating or terminating from one station is high, resulting in a number of lines expanding towards and terminating at that station.

## 2. Route finding algorithms in network

### 1. PathPlanning (used to design a path between two stations accounting for line transfers)
