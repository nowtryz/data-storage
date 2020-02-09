# Data storage with the Multiple Knapsack Problem (MKP)

<img src="http://yuml.me/diagram/scruffy/class/[Representation of the graph{bg:wheat}],[User0{bg:orange}]->[SystemNode0],[SystemNode0]<-->[SystemNode1], [SystemNode0]<-->[SystemNode2], [SystemNode1]<-->[SystemNode3], [SystemNode2]<->[SystemNode3], [SystemNode3]<-[User1{bg:orange}], [User2{bg:orange}]->[SystemNode1]" alt="graph example">

## MKP

here:
 - The collections (Knapsacks) are the [SystemNodes](src/main/java/net/nowtryz/datastorage/entity/SystemNode.java);
 - The items to place in are the [Data](src/main/java/net/nowtryz/datastorage/entity/Data.java);
 - The weight of each item is size of the [Data](src/main/java/net/nowtryz/datastorage/entity/Data.java);
 - The max weight of the collection is the capacity of the
   [SystemNode](src/main/java/net/nowtryz/datastorage/entity/SystemNode.java);
 - The value of each item is the sum of inverses of weighted distances from all
   [users](src/main/java/net/nowtryz/datastorage/entity/User.java) that are interested in the
   [Data](src/main/java/net/nowtryz/datastorage/entity/Data.java);
 
## My way to solve it

As in the MKP, the objective is to pick some of the items, with maximal total profit *p*, while obeying that the maximum
total weight *w* of the chosen items must not exceed *W* of each collection
