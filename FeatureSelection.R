library('FSelector');

# return the top k word most frequent
# restituisce le prime k parole più frequenti nel corpus
top_k <- function(dataset, features, etichette) {
  
  mdataset <- as.matrix(dataset[,1:length(dataset)-1]);
  selected <- mdataset[, order(colSums(mdataset), decreasing=T)][,1:features];
  selected <- cbind(selected, etichette);
  colnames(selected)[ncol(selected)] <- "Classe";
  return(selected);
  
}


# perform the feature selection using information gain method
# esegue il criterio di features selection dell'information gain
informationGainSelection <- function(dataset, features, etichette) {

	weights <- information.gain(Classe~., dataset);
	subset <- cutoff.k(weights, features);
	selected <- dataset[subset];
	selected <- cbind(selected, etichette);
	colnames(selected)[ncol(selected)] <- "Classe";
	return(selected);

}

# perform the feature selection using chiquadro method
# esegue il criterio di features selection del chi quadrato
chiquadroSelection <- function(dataset, features, etichette) {

	weights <- chi.squared(Classe~., dataset);
	subset <- cutoff.k(weights, features);
	selected <- dataset[subset];
	selected <- cbind(selected, etichette);
	colnames(selected)[ncol(selected)] <- "Classe";
	return(selected);

}
