# perform the specified clustering method
# esegue il metodo di clustering specificato
clusterizing <- function(dataset, distanceMethod, clusterMethod, clustersNumber, outPath) {

	# this code executes the hierarchical clustering method
	# questo codice esegue il clustering gerarchico
	distanceMatrix <- dist(dataset[,1:ncol(dataset)-1], method=distanceMethod, diag=TRUE, upper=TRUE);
	hlm <- hclust(distanceMatrix, method=clusterMethod);

	# this code makes the file names for the confusion matrix
	# questo codice crea il nome del file per la matrice di confusione
	numberOfFeatures <- ncol(dataset)-1;
	nome <- paste("Features", numberOfFeatures, sep="_")
	nome <- paste(outPath, nome, sep="");
	fileName <- paste(nome, ".txt", sep="");

	# this code makes the confusion matrix
	# questo codice crea la matrice di confusione
	groups <- cutree(hlm, k=clustersNumber);
	cm <- table(dataset[,ncol(dataset)], groups);
	write.table(cm, fileName, sep=" ");

	return(hlm);

}
