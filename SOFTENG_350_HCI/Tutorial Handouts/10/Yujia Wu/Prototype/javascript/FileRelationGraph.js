// hide the popup showing relationship between files
function hideFileRelationGraph() {
	document.getElementById( "file-relation-popup" ).style.display = "none";
}

// on the popup showing relationship graph, only show files related to draft.doc
function showDraftGraphOnly() {
	// show the go back button
	document.getElementById( "back-icon" ).style.display = "inline";

	// hide the relationship graph of slides.ppt
	document.getElementById( "slide-related-graph-container" ).style.display = "none";

	// place the graph in the middle
	document.getElementById( "draft-related-graph-container" ).classList.remove( "w3-cell" );
	const button  = document.getElementsByClassName( "graph-container" )[0];
	button.style.marginLeft = "150px";
	button.style.marginRight = "150px";

	// enable the share button
	document.getElementById( "share-related-graph-button" ).disabled = false;
}

// show all the relationship graphs for draft.doc and slides.ppt
function showAllGraphs() {
	// hide the back button
	document.getElementById( "back-icon" ).style.display = "none";

	// display all graphs except the empty graph
	document.getElementById( "draft-related-graph-container" ).style.display = "table-cell";
	document.getElementById( "slide-related-graph-container" ).style.display = "table-cell";
	document.getElementById( "empty-container" ).style.display = "none";

	// place the empty graph in the middle
	document.getElementById( "draft-related-graph-container" ).classList.add( "w3-cell" );
	const button  = document.getElementsByClassName( "graph-container" )[0];
	button.style.marginLeft = "0";
	button.style.marginRight = "0";

	// disable the share button
	document.getElementById( "share-related-graph-button" ).disabled = true;
}

// function triggered when users click the share button
function shareRelatedGraph() {
	// show the share file popup
	showPopup();

	// show the relationship graph in the share file popup
	showOrHideRelatedFiles( "share-all-file-button" );
}

// function triggered when users click the graph button, but there is no file inside the folder
function showEmptyGraph() {
	// hide all graphs, display only the empty one
	document.getElementById( "draft-related-graph-container" ).style.display = "none";
	document.getElementById( "slide-related-graph-container" ).style.display = "none";
	document.getElementById( "empty-container" ).style.display = "block";

	// hide the text showing folder directory
	document.getElementById( "file-position" ).style.display = "none";
}