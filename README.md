# Data storage with the Multiple Knapsack Problem (MKP)

This project was made as a school project in order to find how to solve a Multiple Knapsack Proble

## Solution

<img src="http://yuml.me/diagram/scruffy/class/[Representation of the graph{bg:wheat}],[User0{bg:orange}]->[SystemNode0],[SystemNode0]<-->[SystemNode1], [SystemNode0]<-->[SystemNode2], [SystemNode1]<-->[SystemNode3], [SystemNode2]<->[SystemNode3], [SystemNode3]<-[User1{bg:orange}], [User2{bg:orange}]->[SystemNode1]" alt="graph example">

### MKP

here:
 - The collections (Knapsacks) are the [SystemNodes](src/main/java/net/nowtryz/datastorage/entity/SystemNode.java);
 - The items to place in are the [Data](src/main/java/net/nowtryz/datastorage/entity/Data.java);
 - The weight of each item is size of the [Data](src/main/java/net/nowtryz/datastorage/entity/Data.java);
 - The max weight of the collection is the capacity of the
   [SystemNode](src/main/java/net/nowtryz/datastorage/entity/SystemNode.java);
 - The value of each item is the inverse of the quadratic mean of weighted distances from all
   [users](src/main/java/net/nowtryz/datastorage/entity/User.java) that are interested in the
   [data](src/main/java/net/nowtryz/datastorage/entity/Data.java);
 
### My way to solve it

As in the MKP, the objective is to pick some of the items, with maximal total profit *p*, while obeying that the maximum
total weight *w* of the chosen items that must not exceed the max weight *W* of each collection. As the project involve
an MKP, we place data on the node where it have the best profit and the solve single nodes as a 0-1 knapsack problem.
Then we take items that not fit in the collection and loop the algorithm removing already filled nodes.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing
purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

To be able to run the project, you need to have
[java 8](https://www.oracle.com/technetwork/java/javase/downloads/java-archive-javase8u211-later-5573849.html) or upper
installed and as well as [maven](https://maven.apache.org/)

### Running the project

You first need to clone the project
```shell script
git clone git@github.com:nowtryz/data-storage.git
cd data-storage
```

#### From command line
To run the project, follow the steps bellow:
 - package the app with maven
   ```shell script
   mvn clean package
   ```
 - run the archive
   ```shell script
   java -jar taget/data-storage-1.0-SNAPSHOT.jar
   ```

#### From IntelliJ
Open the [*.iml*](Data%20Storage.iml) module

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management
* [JGraphT](https://jgrapht.org/) - Used to manipulate graphs

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the
[tags on this repository](https://github.com/your/project/tags). 

## License

This project is licensed under the MIT License
