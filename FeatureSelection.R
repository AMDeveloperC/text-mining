library('FSelector');

# return the top k word most frequent
top_k <- function(dataset, features, etichette) {

	mdataset <- as.matrix(dataset[,1:length(dataset)-1]);
	selected <- mdataset[, order(colSums(mdataset), decreasing=T)][,1:features];
	selected <- cbind(selected, etichette);
	colnames(selected)[ncol(selected)] <- "Classe";
	return(selected);

}


# perform the feature selection using information gain method
informationGainSelection <- function(dataset, features, etichette) {

	weights <- information.gain(Classe~., dataset);
	subset <- cutoff.k(weights, features);
	selected <- dataset[subset];
	selected <- cbind(selected, etichette);
	colnames(selected)[ncol(selected)] <- "Classe";
	return(selected);

}

# perform the feature selection using chiquadro method
chiquadroSelection <- function(dataset, features, etichette) {

	weights <- chi.squared(Classe~., dataset);
	subset <- cutoff.k(weights, features);
	selected <- dataset[subset];
	selected <- cbind(selected, etichette);
	colnames(selected)[ncol(selected)] <- "Classe";
	return(selected);

}
